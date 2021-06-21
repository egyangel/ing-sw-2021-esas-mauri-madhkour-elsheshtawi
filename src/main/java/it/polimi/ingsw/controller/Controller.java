package it.polimi.ingsw.controller;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enumclasses.MarbleColor;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.VirtualView;
import it.polimi.ingsw.utility.JsonConverter;
import it.polimi.ingsw.utility.messages.*;

import java.lang.reflect.Type;
import java.util.*;

import static it.polimi.ingsw.utility.messages.LeaderActionContext.ActionStep.*;
import static it.polimi.ingsw.utility.messages.ActivateProdActionContext.ActionStep.*;
import static it.polimi.ingsw.utility.messages.TakeResActionContext.ActionStep.*;
import static it.polimi.ingsw.utility.messages.BuyDevCardActionContext.ActionStep.*;
import static it.polimi.ingsw.utility.messages.CVEvent.EventType.*;

// ALSO IMPLEMENTS Publisher<CVEvent> but ABSTRACT OUT LATER
public class Controller implements Listener<VCEvent> {

    protected Server server;
    protected Game game;
    protected Map<Integer, String> userIDtoUsernames = new HashMap<>();
    protected Map<Integer, VirtualView> userIDtoVirtualViews = new HashMap<>();

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

    protected void sendTurnOrderAssign() {
        TurnManager.assignTurnOrder();
        for (Map.Entry<Integer, VirtualView> entry : userIDtoVirtualViews.entrySet()) {
            Integer userTurn = TurnManager.getOrderOfUserID(entry.getKey());
            if (userTurn == 1) {
                TurnManager.registerResponse(entry.getKey());   // first player doesnt choose init res, its lack of response counts as a response
            }
            InitFatihPoints(entry.getKey(), userTurn);
            CVEvent turnAssignEvent = new CVEvent(CVEvent.EventType.ASSIGN_TURN_ORDER, userTurn);
            entry.getValue().update(turnAssignEvent);
        }
    }

    protected void InitFatihPoints(Integer userID, Integer userTurn) {
        if (userTurn == 3 || userTurn == 4) {
            game.getPersonalBoard(userID).increaseFaitPoint(1);
        }
    }

    protected void beginTurn() {
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
            // todo written by Omer: for solo mode I think it would be easier to create its own initial setup messages
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
                if (TurnManager.hasAllClientsResponded() ) {
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
                BuyDevCardActionContext buyDevContext = (BuyDevCardActionContext) vcEvent.getEventPayload(BuyDevCardActionContext.class);
                handleBuyDevCardAction(userID, buyDevContext);
                break;
            case ACTIVATE_PROD_ACTION_SELECTED:
                ActivateProdActionContext emptyActivateDevCardContext = new ActivateProdActionContext();
                emptyActivateDevCardContext.setLastStep(CHOOSE_DEV_SLOTS);
                handleSlotAvailableChoosen(userID, emptyActivateDevCardContext);
                cvEvent = new CVEvent(ACTIVATE_PROD_FILL_CONTEXT, emptyActivateDevCardContext);
                userIDtoVirtualViews.get(userID).update(cvEvent);
                break;
            case ACTIVATE_PROD_CONTEXT_FILLED:
                ActivateProdActionContext ActivateDevContext = (ActivateProdActionContext) vcEvent.getEventPayload(ActivateProdActionContext.class);
                handleActivateDevCardAction(userID, ActivateDevContext);
                break;
            case ACTIVATE_LEADER_CONTEXT_SELECTED:
                LeaderActionContext emptyActivateLeaderContext = new LeaderActionContext();
                emptyActivateLeaderContext.setPlayerCard(game.getPersonalBoard(userID).getInactiveLeaderCards());
                emptyActivateLeaderContext.setTotalResources(game.getPersonalBoard(userID).getTotalResources());
                emptyActivateLeaderContext.setLastStep(CHOOSE_ACTION);
                cvEvent = new CVEvent(ACTIVATE_LEADER_FILL_CONTEXT, emptyActivateLeaderContext);
                userIDtoVirtualViews.get(userID).update(cvEvent);
                break;
            case ACTIVATE_LEADER_CONTEXT_FILLED:
                LeaderActionContext activateLeaderContext = (LeaderActionContext) vcEvent.getEventPayload(LeaderActionContext.class);
                handleActivateLeaderAction(userID, activateLeaderContext);
                break;
            case TAKE_RES_ACTION_ENDED:

            case BUY_DEVCARD_ACTION_ENDED:
            case ACTIVATE_PROD_ACTION_ENDED:
                CVEvent cvEventTwo = new CVEvent(SELECT_MINOR_ACTION);
                userIDtoVirtualViews.get(userID).update(cvEventTwo);
                break;

                case END_TURN:
                    this.endTurn(userID);
                break;
        }//todo change the implementation of leader card action. put it as the other event,move everything in controller side
    }

    //handle TakeResAction
    private void handleTakeResAction(Integer userID, TakeResActionContext context) {
        switch (context.getLastStep()) {
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

    private void handleRowColumnIndex(Integer userID, TakeResActionContext context) {
        List<MarbleColor> marbleList;
        if (context.isRow())
            marbleList = game.getMarketTray().selectRow(context.getIndex());
        else
            marbleList = game.getMarketTray().selectColumn(context.getIndex());
        List<LeaderCard> whiteConverters = new ArrayList<>();
        //todo Omer also this part has something that doesn't convince me,inside getActiveLeaderCards there are only the owned cards and not the active one
        for (LeaderCard leaderCard : game.getPersonalBoard(userID).getActiveLeaderCards()) {
            if (leaderCard.getAbility().getAbilityType() == SpecialAbility.AbilityType.CONVERTWHITE) {
                whiteConverters.add(leaderCard);
            }
        }
        int whiteMarbles = 0;
        Resources resources = new Resources();
        for (MarbleColor marble : marbleList) {
            if (marble.getValue() == MarbleColor.WHITE) whiteMarbles++;
        }
        if (whiteConverters.size() == 0 || whiteMarbles == 0) {
            for (MarbleColor marble : marbleList) {
                Resources.ResType resType = marble.getResourceType();
                if (resType != null)
                    resources.add(resType, 1);
            }
            context.setLastStep(CHOOSE_SHELVES);
        } else if (whiteConverters.size() == 1) {
            for (MarbleColor marble : marbleList) {
                Resources.ResType resType = marble.getResourceType();
                if (resType == null) {
                    resType = whiteConverters.get(0).getAbility().getResType();
                }
                resources.add(resType, 1);
            }
            context.setLastStep(CHOOSE_SHELVES);
        } else if (whiteConverters.size() == 2) {
            for (MarbleColor marble : marbleList) {
                Resources.ResType resType = marble.getResourceType();
                if (resType != null)
                    resources.add(resType, 1);
            }
            context.setLastStep(CHOOSE_LEADER_TO_CONVERT_WHITE);
            context.setWhiteConverters(whiteConverters);
            context.setWhiteMarbleNumber(whiteMarbles);
        }
        String marketTrayDescription = JsonConverter.toJson(game.getMarketTray());
        MVEvent marketTrayEvent = new MVEvent(userID, MVEvent.EventType.MARKET_TRAY_UPDATE, marketTrayDescription);
        game.updateAllAboutChange(marketTrayEvent);
        context.setResources(resources);
        context.convertResIntoFaith();
    }

    private void handleClearShelf(Integer userID, TakeResActionContext context) {
        Shelf.shelfPlace place = context.getShelf();
        int discarded = game.getPersonalBoard(userID).clearShelf(place);
        context.addDiscardedRes(discarded);
        context.setLastStep(CHOOSE_SHELVES);
        String warehouseDescription = game.getPersonalBoard(userID).describeWarehouse();
        MVEvent warehouseEvent = new MVEvent(userID, MVEvent.EventType.WAREHOUSE_UPDATE, warehouseDescription);
        game.updateAllAboutChange(warehouseEvent);
    }

    private void handleSwapShelf(Integer userID, TakeResActionContext context) {
        Shelf.shelfPlace[] places = context.getShelves();
        int discarded = game.getPersonalBoard(userID).swapShelves(places);
        context.addDiscardedRes(discarded);
        context.setLastStep(CHOOSE_SHELVES);
        String warehouseDescription = game.getPersonalBoard(userID).describeWarehouse();
        MVEvent warehouseEvent = new MVEvent(userID, MVEvent.EventType.WAREHOUSE_UPDATE, warehouseDescription);
        game.updateAllAboutChange(warehouseEvent);
    }

    private void handlePutResourcesChosen(Integer userID, TakeResActionContext context) {
        Map<Shelf.shelfPlace, Resources.ResType> map = context.getShelfPlaceResTypeMap();
        Map<Shelf.shelfPlace, Boolean> shelfToResult = new HashMap<>();
        boolean result;
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

    protected void endTurn(Integer userId){
        //TODO to implement all checks

         TurnManager.registerResponse(userId);
         TurnManager.goToNextTurn();
        Integer currentUserID = TurnManager.getCurrentPlayerID();
        game.sendMarketAndDevCardMatrixTo(currentUserID);
        CVEvent beginTurnEvent = new CVEvent(CVEvent.EventType.SELECT_ALL_ACTION);
        userIDtoVirtualViews.get(currentUserID).update(beginTurnEvent);

    }

    //handle BuyDevCardAction
    private void handleBuyDevCardAction(Integer userID, BuyDevCardActionContext context) {
        switch (context.getLastStep()) {
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

    private void handleDevSlotChosen(Integer userID, BuyDevCardActionContext context) {
        DevCard selectedCard = context.getSelectedCard();
        Resources costOfCard = selectedCard.getCost();
        context.setRemainingCost(costOfCard);
        context.setLastStep(CHOOSE_PAY_COST_FROM_WHERE);
    }

    private void handleColorLevelChosen(Integer userID, BuyDevCardActionContext context) {
        DevCard selectedCard = game.peekTopDevCard(context.getColor(), context.getLevel());
        context.setSelectedCard(selectedCard);
        if (selectedCard == null) {
            context.setLastStep(EMPTY_DEVCARD_DECK_ERROR);
        } else if (!game.getPersonalBoard(userID).isThereEnoughRes(selectedCard)) {
            context.setLastStep(NOT_ENOUGH_RES_FOR_DEVCARD_ERROR);
        } else if (!game.getPersonalBoard(userID).isCardSuitableForSlots(selectedCard)) {
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

    private void handlePayFromWhereChosen(Integer userID, BuyDevCardActionContext context) {

        Resources payFromWarehouse = context.getPayFromWarehouse();
        Resources payFromStrongbox = context.getPayFromStrongbox();
        Resources warehouseRes = game.getPersonalBoard(userID).getWarehouseResources();
        Resources strongboxRes = game.getPersonalBoard(userID).getStrongboxResources();

        if (warehouseRes.smallerOrEqual(payFromWarehouse)) {
            context.setPayFromWarehouse(new Resources());
            context.setPayFromStrongbox(new Resources());
            context.setRemainingCost(context.getSelectedCard().getCost());
            context.setLastStep(NOT_ENOUGH_RES_IN_WAREHOUSE);
        } else if (strongboxRes.smallerOrEqual(payFromStrongbox)) {
            context.setPayFromWarehouse(new Resources());
            context.setPayFromStrongbox(new Resources());
            context.setRemainingCost(context.getSelectedCard().getCost());
            context.setLastStep(NOT_ENOUGH_RES_IN_STRONGBOX);
        } else {
            game.getPersonalBoard(userID).subtractFromWarehouse(payFromWarehouse);
            game.getPersonalBoard(userID).subtractFromStrongbox(payFromStrongbox);
            game.getPersonalBoard(userID).putDevCardOnSlot(context.getSelectedCard(), context.getSelectedSlot());
            game.getPersonalBoard(userID).setOwnedCard(context.getSelectedCard());
            game.getPersonalBoard(userID).countVictoryPoints(context.getSelectedCard().getVictoryPoints());

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
            if (game.getPersonalBoard(userID).getOwnedCard().size() == 7) {
                triggerTheEndGame(userID);
            }
        }
    }

    //handle the activation of production
    private void handleActivateDevCardAction(Integer userID, ActivateProdActionContext context) {
        switch (context.getLastStep()) {
            case DEV_SLOTS_CHOOSEN:
                handleActivateDevSlotsProductionChosen(userID, context);
                break;
            /*case PAY_PRODUCTION_FROM_WHERE_CHOSEN:
                 handleCheckProductionPayment(userID, context);
                 break;*/
        }
        CVEvent cvEvent = new CVEvent(ACTIVATE_PROD_FILL_CONTEXT, context);
        userIDtoVirtualViews.get(userID).update(cvEvent);
    }

    private void handleSlotAvailableChoosen(Integer userID, ActivateProdActionContext context) {
        int j = 0;
        List<DevSlot> slotAvailable = new ArrayList<>();
        List<DevSlot.slotPlace> placeList = new ArrayList<>(Arrays.asList(DevSlot.slotPlace.LEFT, DevSlot.slotPlace.CENTER, DevSlot.slotPlace.RIGHT));

        while (j < 3) {
            DevSlot temp = new DevSlot(placeList.get(j));
            if (game.getPersonalBoard(userID).getDevCardOnSlot(temp) != null) {
                slotAvailable.add(temp);
            }
            j++;
        }
        context.setSlotAvailable(slotAvailable);
        slotAvailable.clear();
    }

    private void handleActivateDevSlotsProductionChosen(Integer userID, ActivateProdActionContext context) {
        int j = 0;
        List<DevCard> selectedCard = new ArrayList<>();

        while (j < context.getSlots().size()) {
            if (game.getPersonalBoard(userID).getDevCardOnSlot(context.getSlots().get(j)) != null) {
                selectedCard.add(game.getPersonalBoard(userID).getDevCardOnSlot(context.getSlots().get(j)));
            }
            context.setSelectedCard(selectedCard);
            handleCheckProductionPayment(userID, context);
            //context.setLastStep(CHOOSE_PRODUCTION_COST_FROM_WHERE);
        }
    }

    //this method handle the activation phase of dev Card, it checks if there are enough resources for all cards;
    private void handleCheckProductionPayment(Integer userID, ActivateProdActionContext context) {
        int j = 0;
        Resources totLeftCost = new Resources();
        Resources totRightCost = new Resources();

        // check if there are the total resources to activate all the things chosen from the player in the production action
        if (context.getBaseProdPower()) {
            totLeftCost.add(context.getBaseProductionCard().getLHS());
            totRightCost.add(context.getBaseProductionCard().getRHS());
        }
        if (context.getSlots().size() > 0) {
            while (j < context.getSlots().size()) {
                totLeftCost.add(context.getSelectedCard().get(j).getLHS());
                totRightCost.add(context.getSelectedCard().get(j).getRHS());
                j++;
            }
        }
        if (context.getActivationLeaderCardProduction()) {
            totLeftCost.add(context.getLhlLeaderCard());
            totRightCost.add(context.getRhlLeaderCard());
        }

        if (totLeftCost.smallerOrEqual(game.getPersonalBoard(userID).getTotalResources())) {
            context.resetTotalRightCost();
            context.setLastStep(NOT_ENOUGH_RES_FOR_PRODUCTION);
        }
        if (!context.getLastStep().equals(NOT_ENOUGH_RES_FOR_PRODUCTION)) {
            context.setTotalRightCost(totRightCost);
            handleProductionPayment(userID, context);
        }
    }

    private void handleProductionPayment(Integer userID, ActivateProdActionContext context) {
        int j = 0;
        if (context.getActivationLeaderCardProduction()) {
            handleActivationLeaderProductionPayment(userID, context);
        }


        if (context.getBaseProductionCard().getLHS().smallerOrEqual(game.getPersonalBoard(userID).getWarehouseResources()))
            game.getPersonalBoard(userID).subtractFromWarehouse(context.getBaseProductionCard().getLHS());
        else
            game.getPersonalBoard(userID).subtractFromStrongbox(context.getBaseProductionCard().getLHS());

        if (context.getSelectedCard().size() > 0) {
            while (j < context.getSelectedCard().size()) {
                if (context.getSelectedCard().get(j).getLHS().smallerOrEqual(game.getPersonalBoard(userID).getWarehouseResources()))
                    game.getPersonalBoard(userID).subtractFromWarehouse(context.getSelectedCard().get(j).getLHS());
                else
                    game.getPersonalBoard(userID).subtractFromStrongbox(context.getSelectedCard().get(j).getLHS());

                j++;
            }
        }
        game.getPersonalBoard(userID).putResInStrongBox(context.getTotalRightCost());
        context.setLastStep(COST_PAID);

        String warehouseDescription = game.getPersonalBoard(userID).describeWarehouse();
        MVEvent warehouseEvent = new MVEvent(userID, MVEvent.EventType.WAREHOUSE_UPDATE, warehouseDescription);
        game.updateAllAboutChange(warehouseEvent);
        String devSlotsDescription = game.getPersonalBoard(userID).describeDevSlots();
        MVEvent devslotsEvent = new MVEvent(userID, MVEvent.EventType.DEVSLOTS_UPDATE, devSlotsDescription);
        game.updateAllAboutChange(devslotsEvent);
        String strongBoxDescription = game.getPersonalBoard(userID).describeStrongbox();
        MVEvent strongboxEvent = new MVEvent(userID, MVEvent.EventType.STRONGBOX_UPDATE, strongBoxDescription);
        game.updateAllAboutChange(strongboxEvent);

        context.resetActivationProduction();


    }

    private void handleActivationLeaderProductionPayment(Integer userID, ActivateProdActionContext context) {

        if (context.getLhlLeaderCard().smallerOrEqual(game.getPersonalBoard(userID).getWarehouseResources()))
            game.getPersonalBoard(userID).subtractFromWarehouse(context.getLhlLeaderCard());
        else
            game.getPersonalBoard(userID).subtractFromStrongbox(context.getLhlLeaderCard());

        if (context.getNumberOfActiveLeaderProduction() == 1)
            game.getPersonalBoard(userID).increaseFaitPoint(1);
        else
            game.getPersonalBoard(userID).increaseFaitPoint(2);


    }

    //handle the activation of prodution
    private void handleActivateLeaderAction(Integer userID, LeaderActionContext context) {
        switch (context.getLastStep()) {
            case DISCARD_LEADER_CARD:
                handleDiscardLeaderChosen(userID, context);
                context.setLastStep(END_LEADER_ACTION);
                break;
            case LEADER_CARD_ACTIVATED_CHOOSEN:
                handleActivateLeaderChoosen(userID, context);
                context.setLastStep(END_LEADER_ACTION);
                break;
            case BOTH_ACTIONS:
                handleDiscardLeaderChosen(userID, context);
                handleActivateLeaderChoosen(userID, context);
                context.setLastStep(END_LEADER_ACTION);
                break;
            case LEADER_CARD_NOT_ACTIVATED_CHOOSEN:
                context.setLastStep(END_LEADER_ACTION);
                break;
        }
        CVEvent cvEvent = new CVEvent(ACTIVATE_PROD_FILL_CONTEXT, context);
        userIDtoVirtualViews.get(userID).update(cvEvent);
    }

    private void handleDiscardLeaderChosen(Integer userID, LeaderActionContext context) {

        game.getPersonalBoard(userID).changePlayerCard(context.discardedPlayerCard());
        game.getPersonalBoard(userID).increaseFaitPoint(context.discardedPlayerCard().size());

    }

    private void handleActivateLeaderChoosen(Integer userID, LeaderActionContext context) {
        game.getPersonalBoard(userID).setActiveLeaderCards(context.getActiveLeaderCard());
    }

    private void computeVictoryPoint(Integer userID) {

        int respoint;
        game.getPersonalBoard(userID).countVictoryPoints(game.getPersonalBoard(userID).getTurnPopeFavorTile());
        respoint = game.getPersonalBoard(userID).getTotalResources().sumOfValues();
        respoint = respoint / 5;

        game.getPersonalBoard(userID).countVictoryPoints(respoint);
    }

    private void triggerTheEndGame(Integer userID) {

        computeVictoryPoint(userID);

        CVEvent cvEvent = new CVEvent(END_GAME);
        userIDtoVirtualViews.get(userID).update(cvEvent);

    }


    public void handleGameMessage(Integer userID, Message msg) {
        userIDtoVirtualViews.get(userID).handleGameMessage(msg);

    }
}
