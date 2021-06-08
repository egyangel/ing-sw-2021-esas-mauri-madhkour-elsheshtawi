package it.polimi.ingsw.controller;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enumclasses.MarbleColor;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.VirtualView;
import it.polimi.ingsw.utility.messages.*;

import java.lang.reflect.Type;
import java.util.*;

import static it.polimi.ingsw.utility.messages.ActivateProdActionContext.ActionStep.*;
import static it.polimi.ingsw.utility.messages.TakeResActionContext.ActionStep.*;
import static it.polimi.ingsw.utility.messages.BuyDevCardActionContext.ActionStep.*;
import static it.polimi.ingsw.utility.messages.CVEvent.EventType.*;

// ALSO IMPLEMENTS Publisher<CVEvent> but ABSTRACT OUT LATER
public class Controller implements Listener<VCEvent> {

    private Server server;
    protected Game game;
    private Map<Integer, String> userIDtoUsernames = new HashMap<>();
    private Map<Integer, VirtualView> userIDtoVirtualViews = new HashMap<>();

    public Controller(Game game, Server server) {
        this.game = game;
        this.server = server;
    }

    public void createMatch(Map<Integer, String> userIDtoNameMap) {
        userIDtoUsernames.putAll(userIDtoNameMap);

        for (Integer userID : userIDtoUsernames.keySet()) {
            game.addPlayer(userID);
            VirtualView virtualView = new VirtualView(userID, server.getClientHandler(userID));
            virtualView.subscribe(this);
            game.subscribe(userID, virtualView);
            userIDtoVirtualViews.put(userID, virtualView);
            TurnManager.putUserID(userID);
        }
        game.createGameObjects();
    }

    public void startMatch() {
        game.shuffleLeaderCards();
        sendFourLeaderCards();
    }


    private void sendFourLeaderCards() {
        int calls = 0;
        for (VirtualView virtualView : userIDtoVirtualViews.values()) {
            CVEvent leaderCardEvent = new CVEvent(CVEvent.EventType.CHOOSE_TWO_LEADER_CARD, game.getFourLeaderCard(calls));
            calls++;
            virtualView.update(leaderCardEvent);
        }
    }

    private void sendTurnOrderAssign() {
        TurnManager.assignTurnOrder();
        for (Map.Entry<Integer, VirtualView> entry : userIDtoVirtualViews.entrySet()) {
            // TODO: have to be fixed. it returns userID 0 but user id start from 1
            Integer userTurn = TurnManager.getIndexOfUserID(entry.getKey());
            InitFatihPoints(entry.getKey(), userTurn);
            CVEvent turnAssignEvent = new CVEvent(CVEvent.EventType.ASSIGN_TURN_ORDER, userTurn);
            entry.getValue().update(turnAssignEvent);
        }
    }

    private void InitFatihPoints(Integer userID, Integer userTurn) {
        if (userTurn == 3 || userTurn == 4) {
            game.getPersonalBoard(userID).increaseFaitPoint(1);
        }
    }
    private void beginTurn() {
        Integer currentUserID = TurnManager.getCurrentPlayerID();
        game.sendMarketAndDevCardMatrixTo(currentUserID);
        CVEvent beginTurnEvent = new CVEvent(CVEvent.EventType.SELECT_ALL_ACTION);
        userIDtoVirtualViews.get(currentUserID).update(beginTurnEvent);
    }

    @Override
    public void update(VCEvent vcEvent) {
        Integer userID = vcEvent.getUserID();
        Resources resources;
        CVEvent cvEvent;
        switch (vcEvent.getEventType()) {
            case LEADER_CARDS_CHOOSEN:
                Type type1 = new TypeToken<List<LeaderCard>>() {
                }.getType();
                List<LeaderCard> selectedCards = (List<LeaderCard>) vcEvent.getEventPayload(type1);
                game.getPersonalBoard(userID).putSelectedLeaderCards(selectedCards);
                TurnManager.registerResponse(userID);
                if (TurnManager.hasAllClientsResponded()) {
                    sendTurnOrderAssign();
                }
                break;
            case INIT_RES_CHOOSEN:
                resources = (Resources) vcEvent.getEventPayload(Resources.class);
                game.getPersonalBoard(userID).putToWarehouseWithoutCheck(resources);
                TurnManager.registerResponse(userID);
                if (TurnManager.hasAllClientsResponded()) {
                    beginTurn();
                }
                break;
            case TAKE_RES_ACTION_SELECTED:
                TakeResActionContext emptyTakeResContext = new TakeResActionContext();
                emptyTakeResContext.setLastStep(CHOOSE_ROW_COLUMN);
                cvEvent = new CVEvent(TAKE_RES_FILL_CONTEXT, emptyTakeResContext);
                userIDtoVirtualViews.get(userID).update(cvEvent);
                break;
            case TAKE_RES_CONTEXT_FILLED:
                TakeResActionContext takeResContext = (TakeResActionContext) vcEvent.getEventPayload(TakeResActionContext.class);
                handleTakeResAction(userID, takeResContext);
                break;
            case BUY_DEVCARD_ACTION_SELECTED:
                BuyDevCardActionContext emptyBuyDevCardContext = new BuyDevCardActionContext();
                emptyBuyDevCardContext.setLastStep(CHOOSE_COLOR_LEVEL);
                cvEvent = new CVEvent(BUY_DEVCARD_FILL_CONTEXT, emptyBuyDevCardContext);
                userIDtoVirtualViews.get(userID).update(cvEvent);
                break;
            case BUY_DEVCARD_CONTEXT_FILLED:
                BuyDevCardActionContext buyDevContext = (BuyDevCardActionContext) vcEvent.getEventPayload(ActivateProdActionContext.class);
                handleBuyDevCardAction(userID, buyDevContext);
                break;
            case ACTIVATE_PROD_ACTION_SELECTED:
                ActivateProdActionContext emptyActivateDevCardContext = new ActivateProdActionContext();
                emptyActivateDevCardContext.setLastStep(CHECK_ACTIVE_LEADER_PRODOCTION);
                handleActivateDevCardAction(userID, emptyActivateDevCardContext);
                break;
            case ACTIVATE_PROD_CONTEXT_FILLED:
                ActivateProdActionContext ActivateDevContext = (ActivateProdActionContext) vcEvent.getEventPayload(ActivateProdActionContext.class);
                handleActivateDevCardAction(userID, ActivateDevContext);
                break;
            case TAKE_RES_ACTION_ENDED:
            case BUY_DEVCARD_ACTION_ENDED:
            case ACTIVATE_PROD_ACTION_ENDED:
                CVEvent cvEventTwo = new CVEvent(SELECT_MINOR_ACTION);
                userIDtoVirtualViews.get(userID).update(cvEventTwo);
                break;
        }
    }

    private void handleTakeResAction(Integer userID, TakeResActionContext context){
        switch (context.getLastStep()){
            case ROW_COLUMN_CHOSEN:
                handleRowColumnIndex(userID, context);
                break;
            case RES_FROM_WHITE_ADDED_TO_CONTEXT:
                context.setLastStep(CHOOSE_SHELVES);
                break;
            case CLEAR_SHELF_CHOSEN:
                handleClearShelf(userID, context);
                break;
            case SWAP_SHELVES_CHOSEN:
                handleSwapShelf(userID, context);
                break;
            case PUT_RESOURCES_CHOSEN:
                handlePutResourcesChosen(userID, context);
                break;
        }
        CVEvent cvEvent = new CVEvent(TAKE_RES_FILL_CONTEXT, context);
        userIDtoVirtualViews.get(userID).update(cvEvent);
    }

    private void handleRowColumnIndex(Integer userID, TakeResActionContext context){
        List<MarbleColor> marbleList;
        if(context.isRow())
            marbleList = game.getMarketTray().selectRow(context.getIndex());
        else
            marbleList = game.getMarketTray().selectColumn(context.getIndex());
        List<LeaderCard> whiteConverters = new ArrayList<>();
        for (LeaderCard leaderCard : game.getPersonalBoard(userID).getActiveLeaderCards()) {
            if (leaderCard.getAbility().getAbilityType() == SpecialAbility.AbilityType.CONVERTWHITE) {
                whiteConverters.add(leaderCard);
            }
        }
        int whiteMarbles = 0;
        Resources resources = new Resources();
        for(MarbleColor marble: marbleList) {
            if(marble.getValue() == MarbleColor.WHITE) whiteMarbles++;
        }
        if (whiteConverters.size() == 0 || whiteMarbles == 0){
            for(MarbleColor marble: marbleList){
                resources.add(marble.getResourceType(),1);
            }
            context.setLastStep(CHOOSE_SHELVES);
        } else if(whiteConverters.size() == 1) {
            for (MarbleColor marble : marbleList) {
                Resources.ResType resType = marble.getResourceType();
                if (resType == null) {
                    resType = whiteConverters.get(0).getAbility().getResType();
                }
                resources.add(resType, 1);
            }
            context.setLastStep(CHOOSE_SHELVES);
        } else if(whiteConverters.size() == 2){
            for(MarbleColor marble: marbleList){
                Resources.ResType resType = marble.getResourceType();
                if (resType != null)
                    resources.add(resType,1);
            }
            context.setLastStep(CHOOSE_LEADER_TO_CONVERT_WHITE);
            context.setWhiteConverters(whiteConverters);
            context.setWhiteMarbleNumber(whiteMarbles);
        }
        String marketTrayDescription = game.getMarketTray().describeMarketTray();
        MVEvent marketTrayEvent = new MVEvent(userID, MVEvent.EventType.MARKET_TRAY_UPDATE, marketTrayDescription);
        game.updateAllAboutChange(marketTrayEvent);
        context.setResources(resources);
        context.convertResIntoFaith();
    }

    private void handleClearShelf(Integer userID, TakeResActionContext context){
        Shelf.shelfPlace place = context.getShelf();
        int discarded = game.getPersonalBoard(userID).clearShelf(place);
        context.addDiscardedRes(discarded);
        context.setLastStep(CHOOSE_SHELVES);
        String warehouseDescription = game.getPersonalBoard(userID).describeWarehouse();
        MVEvent warehouseEvent = new MVEvent(userID, MVEvent.EventType.WAREHOUSE_UPDATE, warehouseDescription);
        game.updateAllAboutChange(warehouseEvent);
    }

    private void handleSwapShelf(Integer userID, TakeResActionContext context){
        Shelf.shelfPlace[] places = context.getShelves();
        int discarded = game.getPersonalBoard(userID).swapShelves(places);
        context.addDiscardedRes(discarded);
        context.setLastStep(CHOOSE_SHELVES);
        String warehouseDescription = game.getPersonalBoard(userID).describeWarehouse();
        MVEvent warehouseEvent = new MVEvent(userID, MVEvent.EventType.WAREHOUSE_UPDATE, warehouseDescription);
        game.updateAllAboutChange(warehouseEvent);
    }

    private void handlePutResourcesChosen(Integer userID, TakeResActionContext context){
        Map<Shelf.shelfPlace, Resources.ResType> map = context.getShelfPlaceResTypeMap();
        Map<Shelf.shelfPlace, Boolean> shelfToResult = new HashMap<>();
        Boolean result;
        for (Map.Entry<Shelf.shelfPlace, Resources.ResType> entry : map.entrySet()) {
            Resources resToPut = new Resources();
            resToPut.add(entry.getValue(), context.getResources().getNumberOfType(entry.getValue()));
            result = game.getPersonalBoard(userID).putToWarehouse(entry.getKey(), resToPut);
            shelfToResult.put(entry.getKey(), result);
        }
        context.setPutResultMap(shelfToResult);
        context.removeResourcesPutToShelf();
        context.setLastStep(CHOOSE_SHELVES); //choose shelves is correct, I did it this way intentionally
        String warehouseDescription = game.getPersonalBoard(userID).describeWarehouse();
        MVEvent warehouseEvent = new MVEvent(userID, MVEvent.EventType.WAREHOUSE_UPDATE, warehouseDescription);
        game.updateAllAboutChange(warehouseEvent);
    }

    private void handleBuyDevCardAction(Integer userID, BuyDevCardActionContext context){
        switch (context.getLastStep()){
            case COLOR_LEVEL_CHOSEN:
                handleColorLevelChosen(userID, context);
                break;
            case DEVSLOT_CHOSEN:
                handleDevSlotChosen(userID, context);
                break;
            case PAY_FROM_WHERE_CHOSEN:
                handlePayFromWhereChosen(userID, context);
                break;
        }
        CVEvent cvEvent = new CVEvent(BUY_DEVCARD_FILL_CONTEXT, context);
        userIDtoVirtualViews.get(userID).update(cvEvent);
    }

    private void handleActivationLeaderProductionPayment(Integer userID, ActivateProdActionContext context) {
        Resources warehouseRes = game.getPersonalBoard(userID).getWarehouseResources();
        Resources strongboxRes = game.getPersonalBoard(userID).getStrongboxResources();
        Resources totLeftCost = new Resources();
        Resources totRightCost = new Resources();
        int i = 0;
        while (i < context.getNumberOfActiveLeaderProduction()) {
                totLeftCost.add(context.getProducerCard().get(i).getAbility().getResType(),1);
                totRightCost.add(context.getRhlLeaderCard().get(i));
        }
        if (context.getFromWhereToPayForLeader()) {
            if (totLeftCost.smallerOrEqual(warehouseRes)) {
                context.setLastStep( NOT_ENOUGH_RES_FOR_PRODUCTION_IN_WAREHOUSE);

            } else {
                game.getPersonalBoard(userID).subtractFromWarehouse(totLeftCost);
                game.getPersonalBoard(userID).putResInStrongBox(totRightCost);
                context.resetRhlLeaderCard();
                context.setLastStep(COST_PAID);
                String warehouseDescription = game.getPersonalBoard(userID).describeWarehouse();
                MVEvent warehouseEvent = new MVEvent(userID, MVEvent.EventType.WAREHOUSE_UPDATE, warehouseDescription);
                game.updateAllAboutChange(warehouseEvent);
            }
        } else {
            if (totLeftCost.smallerOrEqual(strongboxRes)) {
                context.setLastStep(NOT_ENOUGH_RES_FOR_PRODUCTION_IN_STRONGBOX);
            } else {
                game.getPersonalBoard(userID).subtractFromStrongbox(totLeftCost);
                game.getPersonalBoard(userID).putResInStrongBox(totRightCost);
                context.resetRhlLeaderCard();
                context.setLastStep(COST_PAID);
                String strongBoxDescription = game.getPersonalBoard(userID).describeStrongbox();
                MVEvent strongboxEvent = new MVEvent(userID, MVEvent.EventType.STRONGBOX_UPDATE, strongBoxDescription);
                game.updateAllAboutChange(strongboxEvent);
            }
        }

        context.setFromWhereToPayForLeader(false);
        context.setNumberOfActiveLeaderProduction(0);

    }

    private void handleCheckProductionPayment(Integer userID, ActivateProdActionContext context) {
        int j = 0, i = 0;

        Resources warehouseRes = game.getPersonalBoard(userID).getWarehouseResources();
        Resources strongboxRes = game.getPersonalBoard(userID).getStrongboxResources();
        Resources devCost = new Resources();
        Resources defualtProdCost = new Resources();
        Resources totLeftCost = new Resources();
        Resources totRightCost = new Resources();

// check if there are the total resources to activate all the things choosen from the player in the production action
        if (context.getBaseProdPower()) {
            totLeftCost.add(context.getBaseProductionCard().getLHS());
            totRightCost.add(context.getBaseProductionCard().getRHS());
            /*defualtProdCost.add(context.getBaseProductionCard().getLHS());
            if (context.getFromWhereToPayForDefault()) {
                if (defualtProdCost.smallerOrEqual(warehouseRes))
                    context.setLastStep(NOT_ENOUGH_RES_FOR_PRODUCTION_IN_WAREHOUSE);
            } else {
                if (defualtProdCost.smallerOrEqual(strongboxRes))
                    context.setLastStep(NOT_ENOUGH_RES_FOR_PRODUCTION_IN_STRONGBOX);

            }*/
        }
        if (context.getSlots().size() > 0) {
            while (j < context.getSlots().size()) {
                totLeftCost.add(context.getSelectedCard().get(j).getLHS());
                totRightCost.add(context.getSelectedCard().get(j).getRHS());
              //  devCost.add(context.getSelectedCard().get(j).getLHS());
                j++;
            }
           /* if (context.getFromWhereToPayForDevslots()) {
                if (devCost.smallerOrEqual(warehouseRes))
                    context.setLastStep(NOT_ENOUGH_RES_FOR_PRODUCTION_IN_WAREHOUSE);
            } else {
                if (devCost.smallerOrEqual(strongboxRes))
                    context.setLastStep(NOT_ENOUGH_RES_FOR_PRODUCTION_IN_STRONGBOX);

            }*/
        }
        if (context.getFromWhereToPayForDefault() && context.getFromWhereToPayForDevslots()) {
            if (totLeftCost.smallerOrEqual(warehouseRes))
                context.setLastStep(NOT_ENOUGH_RES_FOR_PRODUCTION_IN_WAREHOUSE);
        } else {
            if (totLeftCost.smallerOrEqual(strongboxRes))
                context.setLastStep(NOT_ENOUGH_RES_FOR_PRODUCTION_IN_STRONGBOX);

        }
        if (!context.getLastStep().equals(NOT_ENOUGH_RES_FOR_PRODUCTION_IN_WAREHOUSE) && !context.getLastStep().equals(NOT_ENOUGH_RES_FOR_PRODUCTION_IN_STRONGBOX) ) {
            context.setTotalLeftCost(totLeftCost);
            context.setTotalRightCost(totRightCost);
            handleProductionPayment(userID,context);
        }

    }
    private void handleProductionPayment(Integer userID, ActivateProdActionContext context){


        if (context.getFromWhereToPayForDevslots() || context.getFromWhereToPayForDefault())
        {
                game.getPersonalBoard(userID).subtractFromWarehouse(context.getTotalLeftCost());
                game.getPersonalBoard(userID).putResInStrongBox(context.getTotalRightCost());
                context.setLastStep(COST_PAID);
                String warehouseDescription = game.getPersonalBoard(userID).describeWarehouse();
                MVEvent warehouseEvent = new MVEvent(userID, MVEvent.EventType.WAREHOUSE_UPDATE, warehouseDescription);
                game.updateAllAboutChange(warehouseEvent);
        }else {
                game.getPersonalBoard(userID).subtractFromStrongbox(context.getTotalLeftCost());
                game.getPersonalBoard(userID).putResInStrongBox(context.getTotalRightCost());
                context.setLastStep(COST_PAID);
                String strongBoxDescription = game.getPersonalBoard(userID).describeStrongbox();
                MVEvent strongboxEvent = new MVEvent(userID, MVEvent.EventType.STRONGBOX_UPDATE, strongBoxDescription);
                game.updateAllAboutChange(strongboxEvent);
        }



        context.setFromWhereToPayForLeader(false);
        context.setNumberOfActiveLeaderProduction(0);


        String warehouseDescription = game.getPersonalBoard(userID).describeWarehouse();
        MVEvent warehouseEvent = new MVEvent(userID, MVEvent.EventType.WAREHOUSE_UPDATE, warehouseDescription);
        game.updateAllAboutChange(warehouseEvent);
        String devSlotsDescription = game.getPersonalBoard(userID).describeDevSlots();
        MVEvent devslotsEvent = new MVEvent(userID, MVEvent.EventType.DEVSLOTS_UPDATE, devSlotsDescription);
        game.updateAllAboutChange(devslotsEvent);
        String strongBoxDescription = game.getPersonalBoard(userID).describeStrongbox();
        MVEvent strongboxEvent = new MVEvent(userID, MVEvent.EventType.STRONGBOX_UPDATE, strongBoxDescription);
        game.updateAllAboutChange(strongboxEvent);


    }
    private void handleActivateDevCardAction(Integer userID, ActivateProdActionContext context){
        switch (context.getLastStep()){
            case CHECK_ACTIVE_LEADER_PRODOCTION:
                handleActivateLeaderChosen(userID, context);
                break;
            case DEV_SLOTS_CHOOSEN:
                handleActivateDevSlotsChosen(userID, context);
                break;
            case LEADER_CARD_CHOOSEN:
                handleActivationLeaderProductionPayment(userID, context);
                break;
            case LEADER_CARD_NOT_CHOOSEN:
                context.setLastStep(CHOOSE_DEV_SLOTS);
                break;
            case PAY_PRODUCTION_FROM_WHERE_CHOSEN:
                 handleCheckProductionPayment(userID, context);
                 break;
        }
        CVEvent cvEvent = new CVEvent(ACTIVATE_PROD_FILL_CONTEXT, context);
        userIDtoVirtualViews.get(userID).update(cvEvent);
    }//todo set a variable that tell avoid to create duplicated object so that  i don't dill it with elemnt that are already in the context
    private void  handleActivateLeaderChosen(Integer userID, ActivateProdActionContext context){
        int j = 0 ;
        List<LeaderCard> prodLeaderCard = new ArrayList<>();
        List<DevSlot>  slotAvailable = new ArrayList<>();
        List<DevSlot.slotPlace> placeList = new ArrayList<>( Arrays.asList(DevSlot.slotPlace.LEFT,DevSlot.slotPlace.CENTER,DevSlot.slotPlace.RIGHT));

        for (LeaderCard leaderCard : game.getPersonalBoard(userID).getActiveLeaderCards()) {
             if (leaderCard.getAbility().getAbilityType() == SpecialAbility.AbilityType.ADDPROD) {
                   prodLeaderCard.add(leaderCard);
             }
        }
        while (j < 3 ) {
            DevSlot temp= new DevSlot (placeList.get(j));
            if (game.getPersonalBoard(userID).getDevCardOnSlot(temp) != null) {
                slotAvailable.add(temp);
            }
            j++;
        }
        context.setSlotAvailable(slotAvailable);
        context.setLeaderProd(prodLeaderCard);
        context.setLastStep(CHOOSE_LEADER_TO_PRODUCE);
        slotAvailable.clear();
        CVEvent vcEvent = new CVEvent(ACTIVATE_PROD_FILL_CONTEXT, context);
        userIDtoVirtualViews.get(userID).update(vcEvent);
    }

    //this method handle the activation phase of dev Card, it checks if there are enough resources for all cards;
    private void  handleActivateDevSlotsChosen(Integer userID, ActivateProdActionContext context) {
        int j = 0;
        List<DevCard> selectedCard = new ArrayList<>();

        while (j < context.getSlots().size()) {
            if (!game.getPersonalBoard(userID).getDevCardOnSlot(context.getSlots().get(j)).equals(null)) {
                selectedCard.add(game.getPersonalBoard(userID).getDevCardOnSlot(context.getSlots().get(j)));
            }
            context.setSelectedCard(selectedCard);
            context.setLastStep(CHOOSE_PAY_COST_FOR_PRODUCTION_FROM_WHERE);
        }
    }
    private void handleDevSlotChosen(Integer userID, BuyDevCardActionContext context){
        DevCard selectedCard = context.getSelectedCard();
        Resources costOfCard = selectedCard.getCost();
        context.setRemainingCost(costOfCard);
        context.setLastStep(CHOOSE_PAY_COST_FROM_WHERE);
    }
    private void handleColorLevelChosen(Integer userID, BuyDevCardActionContext context){
        DevCard selectedCard = game.peekTopDevCard(context.getColor(), context.getLevel());
        context.setSelectedCard(selectedCard);
        if (selectedCard == null){
            context.setLastStep(EMPTY_DEVCARD_DECK_ERROR);
        } else if (!game.getPersonalBoard(userID).isThereEnoughRes(selectedCard)){
            context.setLastStep(NOT_ENOUGH_RES_FOR_DEVCARD_ERROR);
        } else if (!game.getPersonalBoard(userID).isCardSuitableForSlots(selectedCard)){
            context.setLastStep(UNSUITABLE_FOR_DEVSLOTS_ERROR);
        } else {
            game.removeTopDevCard(context.getColor(), context.getLevel());
            List<DevSlot.slotPlace> slotPlaceList = game.getPersonalBoard(userID).getSuitablePlaces(selectedCard);
            context.setSuitableSlots(slotPlaceList);
            context.setLastStep(CHOOSE_DEV_SLOT);
            String devCardMatrixDescription = game.describeDevCardMatrix();
            MVEvent warehouseEvent = new MVEvent(userID, MVEvent.EventType.DEVCARD_MATRIX_UPDATE, devCardMatrixDescription);
            game.updateAllAboutChange(warehouseEvent);
        }
    }
    private void handlePayFromWhereChosen(Integer userID, BuyDevCardActionContext context){

        Resources payFromWarehouse = context.getPayFromWarehouse();
        Resources payFromStrongbox = context.getPayFromStrongbox();
        Resources warehouseRes = game.getPersonalBoard(userID).getWarehouseResources();
        Resources strongboxRes = game.getPersonalBoard(userID).getStrongboxResources();

        if (warehouseRes.smallerOrEqual(payFromWarehouse)) {
            context.setPayFromWarehouse(new Resources());
            context.setPayFromStrongbox(new Resources());
            context.setRemainingCost(context.getSelectedCard().getCost());
            context.setLastStep(NOT_ENOUGH_RES_IN_WAREHOUSE);
        }else if(strongboxRes.smallerOrEqual(payFromStrongbox)){
            context.setPayFromWarehouse(new Resources());
            context.setPayFromStrongbox(new Resources());
            context.setRemainingCost(context.getSelectedCard().getCost());
            context.setLastStep(NOT_ENOUGH_RES_IN_STRONGBOX);
        } else {
            game.getPersonalBoard(userID).subtractFromWarehouse(payFromWarehouse);
            game.getPersonalBoard(userID).subtractFromStrongbox(payFromStrongbox);
            game.getPersonalBoard(userID).putDevCardOnSlot(context.getSelectedCard(), context.getSelectedSlot());
            context.setLastStep(COST_PAID_DEVCARD_PUT);
            String warehouseDescription = game.getPersonalBoard(userID).describeWarehouse();
            MVEvent warehouseEvent = new MVEvent(userID, MVEvent.EventType.WAREHOUSE_UPDATE, warehouseDescription);
            game.updateAllAboutChange(warehouseEvent);
            String strongBoxDescription = game.getPersonalBoard(userID).describeStrongbox();
            MVEvent strongboxEvent = new MVEvent(userID, MVEvent.EventType.STRONGBOX_UPDATE, strongBoxDescription);
            game.updateAllAboutChange(strongboxEvent);
            String devSlotsDescription = game.getPersonalBoard(userID).describeDevSlots();
            MVEvent devslotsEvent = new MVEvent(userID, MVEvent.EventType.DEVSLOTS_UPDATE, devSlotsDescription);
            game.updateAllAboutChange(devslotsEvent);
        }
    }

    public void handleGameMessage(Integer userID, Message msg) {
        userIDtoVirtualViews.get(userID).handleGameMessage(msg);

    }
}
