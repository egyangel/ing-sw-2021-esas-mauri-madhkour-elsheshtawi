package it.polimi.ingsw.controller;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enumclasses.MarbleColor;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.VirtualView;
import it.polimi.ingsw.utility.messages.*;

import java.lang.reflect.Type;
import java.util.*;
import static it.polimi.ingsw.utility.messages.ActivateProdAlternativeContext.ActionStep.*;
import static it.polimi.ingsw.utility.messages.TakeResActionContext.ActionStep.*;
import static it.polimi.ingsw.utility.messages.BuyDevCardActionContext.ActionStep.*;
import static it.polimi.ingsw.utility.messages.CVEvent.EventType.*;
/**
 *  Controller class , Accepts input and converts it to commands for the model or view.
 * */
// ALSO IMPLEMENTS Publisher<CVEvent> but ABSTRACT OUT LATER
public class Controller implements Listener<VCEvent> {

    protected Server server;
    protected Game game;
    protected Map<Integer, String> userIDtoUsernames = new HashMap<>();
    protected Map<Integer, VirtualView> userIDtoVirtualViews = new HashMap<>();
    private List<Integer> userIDs = new ArrayList<>();
    /**
     * Constructor of the class
     *@param game game object, it is the the model
     *@param server server object for managing connections with clients
     */
    public Controller(Game game, Server server) {
        this.game = game;
        this.server = server;
    }
    /**
     * method that create the game
     *@param userIDtoNameMap it is the map of all user that are in the game
     */
    public void createMatch(Map<Integer, String> userIDtoNameMap) {
        userIDtoUsernames.putAll(userIDtoNameMap);
        userIDs.addAll(userIDtoUsernames.keySet());
        for (Integer userID : userIDs) {
            VirtualView virtualView = new VirtualView(userID, server.getClientHandler(userID));
            virtualView.subscribe(this);
            game.subscribe(userID, virtualView);
            userIDtoVirtualViews.put(userID, virtualView);
            TurnManager.putUserID(userID);
        }
        TurnManager.setGame(game);
        game.createGameObjects();
    }
    /**
     * method that handle the beginning of the match of leader cards and send them to the player w.r.t. the rule game
     */
    public void startMatch() {
        sendInitPersonalBoardDescriptions();
        updateAboutMarketTray();
        game.shuffleLeaderCards();
        updateAboutDevCardMatrix();
        sendFourLeaderCards();
    }

    /**
     * method that send  the four leader cards to the player  at the beginning of a match
     */
    private void sendFourLeaderCards() {
        int calls = 0;
        for (VirtualView virtualView : userIDtoVirtualViews.values()) {
            CVEvent leaderCardEvent = new CVEvent(CVEvent.EventType.CHOOSE_TWO_LEADER_CARD, game.getFourLeaderCard(calls));
            calls++;
            virtualView.update(leaderCardEvent);
        }
    }
    /**
     * method that manages the assignment of the order of the players
     */
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
    /**
     * method that assign faith point to the 3rd and 4th player at the beginning of the game as written in the rule
     */
    protected void InitFatihPoints(Integer userID, Integer userTurn) {
        if (userTurn == 3 || userTurn == 4) {
            game.getPersonalBoard(userID).increaseFaitPoint(1);
        }
    }
    /**
     * method that handle the begin of the game
     */
    protected void beginMatch() {
        Integer inkwellUserID = TurnManager.getInkwellUserID();
        sendAllActionDisplay(inkwellUserID);
    }
    /**
     * method that send to the player the main menu of the game
     */
    protected void sendAllActionDisplay(Integer userID){
        CVEvent cvEvent = new CVEvent(SELECT_ALL_ACTION);
        userIDtoVirtualViews.get(userID).update(cvEvent);
    }
    /**
     * method that send to the player the menu to see the personal board
     */
    protected void sendMinorActionDisplay(Integer userID){
        CVEvent cvEvent = new CVEvent(SELECT_MINOR_ACTION);
        userIDtoVirtualViews.get(userID).update(cvEvent);
    }
    /**
     * method that show the initial personal board description
     */
    private void sendInitPersonalBoardDescriptions(){
        for(Integer userId: userIDs){
            updateAboutWarehouseOfId(userId);
            updateAboutStrongboxOfId(userId);
            updateAboutFaithPointOfId(userId);
            updateAboutFaithTrackOfId(userId);
            updateAboutLeaderCardsOfId(userId);
            updateAboutDevSlotOfId(userId);
        }
    }
    /**
     * method showing the updated market tray
     */
    protected void updateAboutMarketTray(){
        MVEvent marketTrayEvent = new MVEvent(MVEvent.EventType.MARKET_TRAY_UPDATE, game.getMarketTray());
        game.updateAllAboutChange(marketTrayEvent);
    }
    /**
     * method showing the updated development card matrix
     */
    protected void updateAboutDevCardMatrix(){
        MVEvent mvEvent = game.createDevCardMVEvent();
        game.updateAllAboutChange(mvEvent);
    }
    /**
     * method showing the updated warehouse
     * @param userId  player id
     */
    protected void updateAboutWarehouseOfId(Integer userId){
        List<Shelf> shelves;
        shelves = game.getPersonalBoard(userId).getShelves();
        MVEvent warehouseUpdate = new MVEvent(userId, MVEvent.EventType.WAREHOUSE_UPDATE, shelves);
        game.updateAllAboutChange(warehouseUpdate);
    }
    /**
     * method showing the updated strongbox
     * @param userId  player id
     */
    protected void updateAboutStrongboxOfId(Integer userId){
        Resources strongbox;
        strongbox = game.getPersonalBoard(userId).getStrongboxResources();
        MVEvent strongboxUpdate = new MVEvent(userId, MVEvent.EventType.STRONGBOX_UPDATE, strongbox);
        game.updateAllAboutChange(strongboxUpdate);
    }
    /**
     * method showing the updated faith point
     * @param userId  player id
     */
    protected void updateAboutFaithPointOfId(Integer userId){
        Integer currentFaithPoints = game.getPersonalBoard(userId).getFaithPoints();
        MVEvent mvEventTwo = new MVEvent(userId, MVEvent.EventType.FAITHPOINT_UPDATE,currentFaithPoints);
        game.updateAllAboutChange(mvEventTwo);
    }
    /**
     * method showing the updated faith point track
     * @param userId  player id
     */
    protected void updateAboutFaithTrackOfId(Integer userId){
        Map<PersonalBoard.PopeArea, Boolean> map = game.getPersonalBoard(userId).getPopeAreaMap();
        MVEvent mvEvent = new MVEvent(userId, MVEvent.EventType.FAITHTRACK_UPDATE, map);
        game.updateAllAboutChange(mvEvent);
    }
    /**
     * method showing the updated development slot of the player
     * @param userId  player id
     */
    protected void updateAboutDevSlotOfId(Integer userId){
        List<DevSlot> slots;
        slots = game.getPersonalBoard(userId).getDevSlots();
        MVEvent devSlotMVevent = new MVEvent(userId, MVEvent.EventType.DEVSLOTS_UPDATE, slots);
        game.updateAllAboutChange(devSlotMVevent);
    }
    /**
     * method showing the updated leader cards of the player
     * @param userId  player id
     */
    protected void updateAboutLeaderCardsOfId(Integer userId){
        List<LeaderCard> activeLeaders = game.getPersonalBoard(userId).getActiveLeaderCards();
        List<LeaderCard> inActiveLeaders = game.getPersonalBoard(userId).getInactiveLeaderCards();
        MVEvent activeLeaderMVEvent = new MVEvent(userId, MVEvent.EventType.ACTIVE_LEADER_CARD_UPDATE, activeLeaders);
        game.updateAllAboutChange(activeLeaderMVEvent);
        MVEvent inActiveLeaderMVEvent = new MVEvent(userId, MVEvent.EventType.INACTIVE_LEADER_CARD_UPDATE, inActiveLeaders);
        game.updateAllAboutChange(inActiveLeaderMVEvent);
    }
    /**
     * method that based on vcEvent do the corresponding action
     * @param vcEvent  event send from the view to the controller
     */
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
                game.getPersonalBoard(userID).getInactiveLeaderCards().addAll(selectedCards);
                updateAboutLeaderCardsOfId(userID);
                TurnManager.registerResponse(userID);
                if (TurnManager.hasAllClientsResponded()) {
                    sendTurnOrderAssign();
                }
                break;
            case INIT_RES_CHOOSEN:
                resources = (Resources) vcEvent.getEventPayload(Resources.class);
                game.getPersonalBoard(userID).putToWarehouseWithoutCheck(resources);
                updateAboutWarehouseOfId(userID);
                TurnManager.registerResponse(userID);
                if (TurnManager.hasAllClientsResponded()) {
                    beginMatch();
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
                ActivateProdAlternativeContext emptyActivateDevCardContext = new ActivateProdAlternativeContext();
                setAvailableCardsForProd(userID,emptyActivateDevCardContext);
                setAvailableAddProdLeaders(userID,emptyActivateDevCardContext);
                emptyActivateDevCardContext.setLastStep(CHOOSE_DEV_SLOTS_FOR_PROD);
                cvEvent = new CVEvent(ACTIVATE_PROD_FILL_CONTEXT, emptyActivateDevCardContext);
                userIDtoVirtualViews.get(userID).update(cvEvent);
                break;
            case ACTIVATE_PROD_CONTEXT_FILLED:
                ActivateProdAlternativeContext ActivateDevContext = (ActivateProdAlternativeContext) vcEvent.getEventPayload(ActivateProdAlternativeContext.class);
                handleActivateDevCardAction(userID, ActivateDevContext);
                break;
            case ACTIVATE_LEADER_SELECTED:
                LeaderActionContext emptyLeaderActionContext = new LeaderActionContext();
                emptyLeaderActionContext.setInactiveCards(game.getPersonalBoard(userID).getInactiveLeaderCards());
                cvEvent = new CVEvent(ACTIVATE_LEADER_FILL_CONTEXT, emptyLeaderActionContext);
                userIDtoVirtualViews.get(userID).update(cvEvent);
                break;
            case DISCARD_LEADER_SELECTED:
                LeaderActionContext emptyLeaderActionContextTwo = new LeaderActionContext();
                emptyLeaderActionContextTwo.setInactiveCards(game.getPersonalBoard(userID).getInactiveLeaderCards());
                emptyLeaderActionContextTwo.setActiveCards(game.getPersonalBoard(userID).getActiveLeaderCards());
                cvEvent = new CVEvent(DISCARD_LEADER_FILL_CONTEXT, emptyLeaderActionContextTwo);
                userIDtoVirtualViews.get(userID).update(cvEvent);
                break;
            case ACTIVATE_LEADER_CONTEXT_FILLED:
                LeaderActionContext leaderActionContext = (LeaderActionContext) vcEvent.getEventPayload(LeaderActionContext.class);
                handleActivateLeaderAction(userID, leaderActionContext);
                updateAboutLeaderCardsOfId(userID);
                if (TurnManager.isMajorActionDone(userID)){
                    sendAllActionDisplay(userID);
                } else {
                    sendMinorActionDisplay(userID);
                }
                break;
            case DISCARD_LEADER_CONTEXT_FILLED:
                LeaderActionContext leaderActionContextTwo = (LeaderActionContext) vcEvent.getEventPayload(LeaderActionContext.class);
                handleDiscardLeaderAction(userID, leaderActionContextTwo);
                updateAboutLeaderCardsOfId(userID);
                if (!TurnManager.isMajorActionDone(userID)){
                    sendAllActionDisplay(userID);
                } else {
                    sendMinorActionDisplay(userID);
                }
                break;
            case TAKE_RES_ACTION_ENDED:
                TakeResActionContext takeResContextTwo = (TakeResActionContext) vcEvent.getEventPayload(TakeResActionContext.class);
                int faithPointsToAdd = takeResContextTwo.getFaithPoints();
                if (faithPointsToAdd > 0){
                    game.getPersonalBoard(userID).increaseFaitPoint(faithPointsToAdd);
                    updateAboutFaithPointOfId(userID);
                }
                int discardedRes = takeResContextTwo.getDiscardedRes();
                Resources remaining = takeResContextTwo.getResources();
                discardedRes += remaining.sumOfValues();
                if (discardedRes > 0){
                    userIDs.remove(userID);
                    for(Integer anOtherUserId: userIDs){
                        game.getPersonalBoard(anOtherUserId).increaseFaitPoint(discardedRes);
                        updateAboutFaithPointOfId(anOtherUserId);
                    }
                    userIDs.add(userID);
                }
                TurnManager.registerMajorActionDone(userID);
                sendMinorActionDisplay(userID);
                break;
            case BUY_DEVCARD_ACTION_ENDED:
            case ACTIVATE_PROD_ACTION_ENDED:
                TurnManager.registerMajorActionDone(userID);
                sendMinorActionDisplay(userID);
                break;
            case END_TURN:
                handleEndTurn(userID);
                break;
        }
    }
    /**
     * method that based on the choice of the player related to the take res action compute the related action
     * @param userID player id
     * @param context is the context of take res action. it is filled in both view and controller side with info needed to complete the action
     */
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
            case EXTRA_SLOT_CHOSEN:
                handleExtraSlotChosen(userID, context);
                break;
        }
        CVEvent cvEvent = new CVEvent(TAKE_RES_FILL_CONTEXT, context);
        userIDtoVirtualViews.get(userID).update(cvEvent);
    }
    /**
     * method that handle the index of the row/column chooses by the player. It draw the related res from the market tray based on player's choice
     * @param userID player id
     * @param context is the context of take res action. it is filled in both view and controller side with info needed to complete the action
     */
    private void handleRowColumnIndex(Integer userID, TakeResActionContext context){
        List<MarbleColor> marbleList;
        if(context.isRow())
            marbleList = game.getMarketTray().selectRow(context.getIndex());
        else
            marbleList = game.getMarketTray().selectColumn(context.getIndex());
        List<LeaderCard> whiteConverters = new ArrayList<>();
        List<LeaderCard> extraSlotLeaders = new ArrayList<>();
        for (LeaderCard leaderCard : game.getPersonalBoard(userID).getActiveLeaderCards()) {
            if (leaderCard.getAbility().getAbilityType() == SpecialAbility.AbilityType.CONVERTWHITE) {
                whiteConverters.add(leaderCard);
            }
            if (leaderCard.getAbility().getAbilityType() == SpecialAbility.AbilityType.EXTRASLOT) {
                extraSlotLeaders.add(leaderCard);
            }
        }
        context.setExtraSlotLeaders(extraSlotLeaders);
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
        updateAboutMarketTray();
        context.setResources(resources);
        context.convertResIntoFaith();
    }
    /**
     * method that clear the shelf on player's personal board
     * @param userID player id
     * @param context is the context of take res action. it is filled in both view and controller side with info needed to complete the action
     */
    private void handleClearShelf(Integer userID, TakeResActionContext context){
        Shelf.shelfPlace place = context.getShelf();
        Resources discarded = game.getPersonalBoard(userID).clearShelf(place);
        context.addToRemainingResources(discarded);
        context.setLastStep(CHOOSE_SHELVES);
        updateAboutWarehouseOfId(userID);
    }
    /**
     * method that handle the swap shelf action
     * @param userID player id
     * @param context is the context of take res action. it is filled in both view and controller side with info needed to complete the action
     */
    private void handleSwapShelf(Integer userID, TakeResActionContext context){
        Shelf.shelfPlace[] places = context.getShelves();
        Resources discarded = game.getPersonalBoard(userID).swapShelves(places);
        if (discarded.sumOfValues() > 0)
            context.addToRemainingResources(discarded);
        context.setLastStep(CHOOSE_SHELVES);
        updateAboutWarehouseOfId(userID);
    }
    /**
     * method that handle the insertion of resources inside the shelf and update the warehouse
     * @param userID player id
     * @param context  is the context of take res action. it is filled in both view and controller side with info needed to complete the action
     */
    private void handlePutResourcesChosen(Integer userID, TakeResActionContext context){
        Map<Shelf.shelfPlace, Resources.ResType> map = context.getShelfPlaceResTypeMap();
        Map<Shelf.shelfPlace, Boolean> shelfToResult = new HashMap<>();
        for (Map.Entry<Shelf.shelfPlace, Resources.ResType> entry : map.entrySet()) {
            Shelf.shelfPlace shelfPlaceToPut = entry.getKey();
            Resources.ResType resTypeToPut = entry.getValue();
            boolean alreadyExistInWarehouse = game.getPersonalBoard(userID).isTypePutInAnotherShelf(shelfPlaceToPut, resTypeToPut);
            if (!alreadyExistInWarehouse) {
                boolean isEmptyShelf = game.getPersonalBoard(userID).checkEmptyShelf(shelfPlaceToPut);
                boolean isSameType = false;
                if (!isEmptyShelf) {
                    isSameType = game.getPersonalBoard(userID).checkSameType(shelfPlaceToPut, resTypeToPut);
                }
                if (isEmptyShelf || isSameType) {
                    Resources resToPut = new Resources(resTypeToPut, context.getResources().getNumberOfType(resTypeToPut));
                    int discardedSameTypeRes = game.getPersonalBoard(userID).putToWarehouse(shelfPlaceToPut, resToPut);
                    Resources discardedRes = new Resources(resTypeToPut, discardedSameTypeRes);
                    Resources successPut = new Resources();
                    successPut.add(resToPut);
                    successPut.subtract(discardedRes);
                    context.subtractFromResources(successPut);
                }
            }
        }
        context.setLastStep(CHOOSE_SHELVES); //choose shelves is correct, I did it this way intentionally
        updateAboutWarehouseOfId(userID);
    }
    /**
     * method that handle the insertion of resources on the  ExtraSlot
     * @param userID player id
     * @param context  is the context of take res action. it is filled in both view and controller side with info needed to complete the action
     */
    private void handleExtraSlotChosen(Integer userID, TakeResActionContext context){
        List<LeaderCard> leaderCardList = game.getPersonalBoard(userID).getActiveLeaderCards();
        List<LeaderCard> extraSlotLeaders = new ArrayList<>();
        for(LeaderCard card: leaderCardList){
            if(card.getAbility().getAbilityType() == SpecialAbility.AbilityType.EXTRASLOT){
                extraSlotLeaders.add(card);
            }
        }
        Map<Resources.ResType, Integer> map = context.getExtraSlotResToAddMap();
        List<Resources.ResType> resTypesToPut = new ArrayList<>(map.keySet());
        for(Resources.ResType resType: resTypesToPut){
            int howManyToPut = map.getOrDefault(resType, 0);
            if (howManyToPut > 0){
                Resources resToPut = new Resources(resType, howManyToPut);
                for (LeaderCard card: extraSlotLeaders){
                    if (card.getAbility().getResType() == resType){
                        int discarded = card.getAbility().addToHolder(resToPut);
                        context.addDiscardedRes(discarded);
                        Resources putOnTheCard = new Resources(resType,howManyToPut-discarded);
                        context.removeResourcePutToExtraSlot(putOnTheCard);
                    }
                }
            }
        }
        context.setExtraSlotLeaders(extraSlotLeaders);
        context.setLastStep(CHOOSE_SHELVES);
        updateAboutLeaderCardsOfId(userID);
    }

    /**
     * method that based on the choice of the player related to buying development action showing the updated leader cards of the player
     * @param userID player id
     * @param context  is the context of buy card action. it is filled in both view and controller side with info needed to complete the action
     */
    private void handleBuyDevCardAction(Integer userID, BuyDevCardActionContext context){
        switch (context.getLastStep()){
            case COLOR_LEVEL_CHOSEN:
                handleColorLevelChosen(userID, context);
                break;
            case DEVSLOT_CHOSEN:
                handleDevSlotChosen(userID, context);
                break;
        }
        CVEvent cvEvent = new CVEvent(BUY_DEVCARD_FILL_CONTEXT, context);
        userIDtoVirtualViews.get(userID).update(cvEvent);
    }
    /**
     * method that handle the development card purchase action. it check if the requirement for the card are satisfied
     * @param userID player id
     * @param context  is the context of buy card action. it is filled in both view and controller side with info needed to complete the action
     */
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
            updateAboutDevCardMatrix();
        }
    }
    /**
     * method that handle the insertion of development card inside the personal board's slot .
     * @param userID player id
     * @param context  is the context of buy card action. it is filled in both view and controller side with info needed to complete the action
     */
    private void handleDevSlotChosen(Integer userID, BuyDevCardActionContext context){
        DevCard selectedCard = context.getSelectedCard();
        DevSlot.slotPlace selectedSlot = context.getSelectedSlot();
        Resources costToBePaid = new Resources();
        Resources totalDiscount = new Resources();
        costToBePaid.add(selectedCard.getCost());
        List<LeaderCard> activeLeaders = game.getPersonalBoard(userID).getActiveLeaderCards();
        for(LeaderCard leader: activeLeaders) {
            if(leader.getAbility().getAbilityType() == SpecialAbility.AbilityType.DISCOUNT)
                totalDiscount.add(leader.getAbility().getResType(),1);
        }
        if(!totalDiscount.isEmpty()){
            costToBePaid.subtract(totalDiscount);
            context.setTotalDiscount(totalDiscount);
        }
        payCostFromPersonalBoard(userID, costToBePaid);
        game.getPersonalBoard(userID).putDevCardOnSlot(selectedCard, selectedSlot);
        updateAboutWarehouseOfId(userID);
        updateAboutStrongboxOfId(userID);
        updateAboutLeaderCardsOfId(userID);
        updateAboutDevSlotOfId(userID);
        context.setLastStep(COST_PAID_DEVCARD_PUT);
    }
    /**
     * method that based on the choice of the player related to buying development action showing the updated leader cards of the player
     * @param userID player id
     * @param context is the context of activation production action. it is filled in both view and controller side with info needed to complete the action
     */
    private void handleActivateDevCardAction(Integer userID, ActivateProdAlternativeContext context){
        if (context.getLastStep() == DEVLSLOTS_CHOOSEN_FOR_PROD) {
            handleActivateDevSlotsProductionChosen(userID, context);
        }
        CVEvent cvEvent = new CVEvent(ACTIVATE_PROD_FILL_CONTEXT, context);
        userIDtoVirtualViews.get(userID).update(cvEvent);
    }
    /**
     * method that fill the context with the available slots of the player's personal board .
     * @param userID player id
     * @param context is the context of activation production action. it is filled in both view and controller side with info needed to complete the action
     */
    private void setAvailableCardsForProd(Integer userID, ActivateProdAlternativeContext context){
        Map<DevSlot.slotPlace, DevCard> slotMap = new HashMap<>();
        DevCard card;
        for(DevSlot.slotPlace place: DevSlot.slotPlace.values()){
            card = game.getPersonalBoard(userID).getDevCardOnSlot(place);
            if(card != null){
                slotMap.put(place, card);
            }
        }
        context.setSlotMap(slotMap);
    }
    /**
     * method that fill the context with leader card with ability additional production if there are any on personal board
     * @param userID player id
     * @param context is the context of activation production action. it is filled in both view and controller side with info needed to complete the action
     */
    private void setAvailableAddProdLeaders(Integer userID, ActivateProdAlternativeContext context){
        List<LeaderCard> addProdLeaderList = new ArrayList<>();
        List<LeaderCard> leaderCardList = new ArrayList<>(game.getPersonalBoard(userID).getActiveLeaderCards());
        for(LeaderCard leaderCard : leaderCardList){
            if (leaderCard.getAbility().getAbilityType() == SpecialAbility.AbilityType.ADDPROD){
                addProdLeaderList.add(leaderCard);
            }
        }
        if (addProdLeaderList.isEmpty()){
            context.setAddProdOptionAvailable(false);
        } else {
            context.setAddProdOptionAvailable(true);
            context.setAddProdLeaders(addProdLeaderList);
        }
    }
    /**
     * method that check if there are all the resources in the personal board to activate production
     * @param userID player id
     * @param context is the context of activation production action. it is filled in both view and controller side with info needed to complete the action
     */

    private void  handleActivateDevSlotsProductionChosen(Integer userID, ActivateProdAlternativeContext context) {
        Resources totalCost = new Resources();
        Resources totalProduction = new Resources();
        boolean basicProdChosen = context.getBasicProdOptionSelected();
        if (basicProdChosen){
            totalCost.add(context.getBasicProdLHS());
            totalProduction.add(context.getBasicProdRHS());
        }
        List<DevCard> devCardList = context.getSelectedDevCards();
        for (DevCard card: devCardList){
            totalCost.add(card.getLHS());
            totalProduction.add(card.getRHS());
        }
        boolean addProdOption = context.getAddProdOptionSelected();
        if (addProdOption){
            totalCost.add(context.getLeaderCosts());
            totalProduction.add(context.getLeaderProds());
        }
        Resources totalResources = game.getPersonalBoard(userID).getTotalResources();
        boolean enoughRes = totalResources.includes(totalCost);
        if(!enoughRes){
            context.setLastStep(NOT_ENOUGH_RES_ON_PERSONAL_BOARD);
        } else {
            payCostFromPersonalBoard(userID, totalCost);
            Resources faithRes = totalProduction.splitThisType(Resources.ResType.FAITH);
            game.getPersonalBoard(userID).increaseFaitPoint(faithRes.sumOfValues());
            game.getPersonalBoard(userID).addToStrongBox(totalProduction);
            updateAboutWarehouseOfId(userID);
            updateAboutStrongboxOfId(userID);
            updateAboutFaithPointOfId(userID);
            updateAboutLeaderCardsOfId(userID);
            context.setLastStep(PRODUCTION_DONE);
        }
    }
    /**
     * method that remove the the total cost of activation production from player's personal-board for the activation production
     * @param userID player id
     * @param totalCost is the cost of activation production
     */
    private void payCostFromPersonalBoard(Integer userID, Resources totalCost){
        // automatic payment from warehouse
        Resources warehouseRes = game.getPersonalBoard(userID).getWarehouseResources();
        for(Resources.ResType type: totalCost.getResTypes()){
            Resources oneTypeRes = totalCost.cloneThisType(type);
            if(warehouseRes.includes(oneTypeRes)){
                game.getPersonalBoard(userID).subtractFromWarehouse(oneTypeRes);
                totalCost.subtract(oneTypeRes);
            } else {
                Resources paidPartFromWarehouse = warehouseRes.cloneThisType(type);
                game.getPersonalBoard(userID).subtractFromWarehouse(paidPartFromWarehouse);
                totalCost.subtract(paidPartFromWarehouse);
            }
        }
        // automatic payment from active extra slot leaders
        Resources extraSlotRes = game.getPersonalBoard(userID).getExtraSlotResources();
        for(Resources.ResType type: totalCost.getResTypes()){
            Resources oneTypeRes = totalCost.cloneThisType(type);
            if(extraSlotRes.includes(oneTypeRes)){
                game.getPersonalBoard(userID).subtractFromExtraSlot(oneTypeRes);
                totalCost.subtract(oneTypeRes);
            } else {
                Resources paidPartFromExtraSlot = extraSlotRes.cloneThisType(type);
                game.getPersonalBoard(userID).subtractFromExtraSlot(paidPartFromExtraSlot);
                totalCost.subtract(paidPartFromExtraSlot);
            }
        }
        // automatic payment from strongbox
        Resources strongboxRes = game.getPersonalBoard(userID).getStrongboxResources();
        for(Resources.ResType type: totalCost.getResTypes()){
            Resources oneTypeRes = totalCost.cloneThisType(type);
            if(strongboxRes.includes(oneTypeRes)){
                game.getPersonalBoard(userID).subtractFromStrongbox(oneTypeRes);
                totalCost.subtract(oneTypeRes);
            } else {
                Resources paidPartFromStrongbox = strongboxRes.cloneThisType(type);
                game.getPersonalBoard(userID).subtractFromStrongbox(paidPartFromStrongbox);
                totalCost.subtract(paidPartFromStrongbox);
            }
        }
    }
    /**
     * method that handle the activation of the player's leader cards
     * @param userID player id
     * @param context is the leader card context. it is filled in both view and controller side with info needed to complete the action
     */
    protected void handleActivateLeaderAction(Integer userID, LeaderActionContext context){
        // using indexes of cards instead of card objects, to use the correct hashcode of the leader card object during removal
        List<Integer> cardIndexToActivateList =  context.getSelectedInactiveCardIndexes();
        List<LeaderCard> inactiveLeaders = game.getPersonalBoard(userID).getInactiveLeaderCards();
        if (cardIndexToActivateList.size() == 1){
            LeaderCard card = inactiveLeaders.get(cardIndexToActivateList.get(0));
            boolean okeyToActivate = RequirementChecker.check(game.getPersonalBoard(userID), card);
            if (okeyToActivate){
                inactiveLeaders.remove(card);
                game.getPersonalBoard(userID).getActiveLeaderCards().add(card);
            }
        } else if (cardIndexToActivateList.size() == 2){
            LeaderCard cardOne = inactiveLeaders.get(0);
            LeaderCard cardTwo = inactiveLeaders.get(1);
            boolean okeyToActivateOne = RequirementChecker.check(game.getPersonalBoard(userID), cardOne);
            boolean okeyToActivateTwo = RequirementChecker.check(game.getPersonalBoard(userID), cardTwo);
            if (okeyToActivateOne){
                inactiveLeaders.remove(cardOne);
                game.getPersonalBoard(userID).getActiveLeaderCards().add(cardOne);
            }
            if (okeyToActivateTwo){
                inactiveLeaders.remove(cardTwo);
                game.getPersonalBoard(userID).getActiveLeaderCards().add(cardTwo);
            }
        }
    }
    /**
     * method that handle discard action of the leader cards
     * @param userID player id
     * @param context is the leader card context. it is filled in both view and controller side with info needed to complete the action
     */
    protected void handleDiscardLeaderAction(Integer userID, LeaderActionContext context){
        // using indexes of cards instead of card objects, to use the correct hashcode of the leader card object during removal
        List<Integer> activeCardIndexToDiscardList =  context.getSelectedActiveCardIndexes();
        List<Integer> inactiveCardIndexToDiscardList =  context.getSelectedInactiveCardIndexes();
        List<LeaderCard> activeLeaders = game.getPersonalBoard(userID).getActiveLeaderCards();
        List<LeaderCard> inactiveLeaders = game.getPersonalBoard(userID).getInactiveLeaderCards();
        int removedCards = 0;
        if(activeCardIndexToDiscardList.size() == 1){
            LeaderCard card = activeLeaders.get(activeCardIndexToDiscardList.get(0));
            activeLeaders.remove(card);
            removedCards++;
        } else if (activeCardIndexToDiscardList.size() == 2){
            activeLeaders.clear();
            removedCards += 2;
        }
        if(inactiveCardIndexToDiscardList.size() == 1){
            LeaderCard card = inactiveLeaders.get(inactiveCardIndexToDiscardList.get(0));
            inactiveLeaders.remove(card);
            removedCards++;
        } else if (inactiveCardIndexToDiscardList.size() == 2){
            inactiveLeaders.clear();
            removedCards += 2;
        }
        if(removedCards > 0){
            game.getPersonalBoard(userID).increaseFaitPoint(removedCards);
            updateAboutFaithPointOfId(userID);
        }

    }
    /**
     * method that handle the end of the game
     * @param userID player id
     */
    protected void handleEndTurn(Integer userID){
        boolean isEndTriggeredBefore = TurnManager.checkIfEndOfGame(userID);
        boolean isEndTriggeredJustNow = TurnManager.checkIfEndTriggered(userID);
        if(!isEndTriggeredBefore){
            if(isEndTriggeredJustNow){
                for(Integer aUserID: userIDs){
                    CVEvent cvEvent = new CVEvent(END_GAME_TRIGGERED);
                    userIDtoVirtualViews.get(aUserID).update(cvEvent);
                }
            }
            sendAllActionDisplay(TurnManager.goToNextTurn());
        } else {
            int remainingTurns = TurnManager.getRemainingNumberOfTurns();
            if (remainingTurns > 0){
                sendAllActionDisplay(TurnManager.goToNextTurn());
            } else {
                Map<Integer, Integer> userIDtoVP = computeVictoryPointsForAll();
                Map<String, Integer> usernametoVP = new HashMap<>();
                for (Map.Entry<Integer, Integer> entry : userIDtoVP.entrySet()) {
                    usernametoVP.put(userIDtoUsernames.get(entry.getKey()), entry.getValue());
                }
                updateAllAboutRanks(userIDtoVP);
                updateAllAboutVictoryPoints(usernametoVP);
                server.closeAllAndExit();
            }
        }
    }

    public void handleGameMessage(Integer userID, Message msg) {
        userIDtoVirtualViews.get(userID).handleGameMessage(msg);

    }

    public void takeVaticanReports(PersonalBoard.PopeArea area){
        for(Integer userID: userIDs){
            game.getPersonalBoard(userID).giveVaticanReport(area);
            updateAboutFaithPointOfId(userID);
            updateAboutFaithTrackOfId(userID);
        }
    }

    /**
     * method that compute the total victory point of the player
     */
    protected Map<Integer,Integer> computeVictoryPointsForAll(){
        Map<Integer, Integer> userIDtoVP = new HashMap<>();
        for (Integer userID: userIDs){
            int VP = game.getPersonalBoard(userID).getVP();
            userIDtoVP.put(userID, VP);
        }
        return userIDtoVP;
    }
    /**
     * method that handle compute the winner of the game

     */



    protected void updateAllAboutRanks(Map<Integer,Integer> userIDtoVPMap){
        List<Integer> VPlist = new ArrayList<>(userIDtoVPMap.values());
        int maxVP = Collections.max(VPlist);
        CVEvent cvEvent;
        for (Map.Entry<Integer, Integer> entry : userIDtoVPMap.entrySet()) {
            Integer userID = entry.getKey();
            if(entry.getValue() == maxVP){
                cvEvent = new CVEvent(CVEvent.EventType.END_RESULT, "You win!");
            } else {
                cvEvent = new CVEvent(CVEvent.EventType.END_RESULT, "You lost!");
            }
            userIDtoVirtualViews.get(userID).update(cvEvent);
        }
    }

    protected void updateAllAboutVictoryPoints(Map<String,Integer> usernametoVPMap){
        for(Integer userId: userIDs){
            CVEvent cvEvent = new CVEvent(CVEvent.EventType.END_VP_COUNTED, usernametoVPMap);
            userIDtoVirtualViews.get(userId).update(cvEvent);
        }
    }
}

/*
    public void debugInitializeAll(){
        debugInitilizeWarehouse();
        debugInitializeStrongbox();
        debugAutoChooseTwoLeadersToAll();
        debugPutDevCardsOnSlots();
         private void debugInitilizeWarehouse(){
//        Resources topres = new Resources(Resources.ResType.STONE,1 );
        Resources midres = new Resources(Resources.ResType.SHIELD, 2);
        Resources bottomres = new Resources(Resources.ResType.SERVANT, 2);
        // initialize only with one-type res, zero values does not work
        for (Map.Entry<Integer, VirtualView> entry : userIDtoVirtualViews.entrySet()) {
//            game.getPersonalBoard(entry.getKey()).putToWarehouse(Shelf.shelfPlace.TOP, topres);
            game.getPersonalBoard(entry.getKey()).putToWarehouse(Shelf.shelfPlace.MIDDLE, midres);
            game.getPersonalBoard(entry.getKey()).putToWarehouse(Shelf.shelfPlace.BOTTOM, bottomres);
            updateAboutWarehouseOfId(entry.getKey());
        }
    }

    private void debugInitializeStrongbox(){
        Resources strongBoxRes = new Resources(Resources.ResType.STONE,1);
        for (Map.Entry<Integer, VirtualView> entry : userIDtoVirtualViews.entrySet()) {
            game.getPersonalBoard(entry.getKey()).addToStrongBox(strongBoxRes);
            updateAboutStrongboxOfId(entry.getKey());
        }
    }

    private void debugAutoChooseTwoLeadersToAll(){
        List<LeaderCard> list1 = new ArrayList<>();
        Resources resources1 = new Resources();
        resources1.add(Resources.ResType.COIN, 5);
        Requirement requirement = new Requirement(Requirement.reqType.THREECARD, DevCard.CardColor.GREEN, DevCard.CardColor.PURPLE);
        SpecialAbility ability = new SpecialAbility(SpecialAbility.AbilityType.EXTRASLOT, Resources.ResType.SHIELD);
        list1.add(new LeaderCard(requirement, 5, ability));
        Resources resources2 = new Resources(5,0,0,0);
//        resources2.add(Resources.ResType.COIN, 5);
        Requirement requirement2 = new Requirement(Requirement.reqType.THREECARD, DevCard.CardColor.YELLOW, DevCard.CardColor.BLUE);
        ability = new SpecialAbility(SpecialAbility.AbilityType.EXTRASLOT, Resources.ResType.SERVANT);
        list1.add(new LeaderCard(requirement2, 5, ability));

        List<LeaderCard> list2 = new ArrayList<>();
        requirement = new Requirement(Requirement.reqType.TWOCARD, DevCard.CardColor.BLUE, DevCard.CardColor.PURPLE);
        ability = new SpecialAbility(SpecialAbility.AbilityType.DISCOUNT, Resources.ResType.SHIELD);
        list2.add(new LeaderCard(requirement, 2, ability));
        requirement = new Requirement(Requirement.reqType.THREECARD, DevCard.CardColor.GREEN, DevCard.CardColor.PURPLE);
        ability = new SpecialAbility(SpecialAbility.AbilityType.CONVERTWHITE, Resources.ResType.SHIELD);
        list2.add(new LeaderCard(requirement, 5, ability));

        List<List<LeaderCard>> twoList = new ArrayList<>();
        twoList.add(list1);
        twoList.add(list2);
        int calls = 0;
        for (Map.Entry<Integer, VirtualView> entry : userIDtoVirtualViews.entrySet()) {
            CVEvent leaderCardEvent = new CVEvent(CVEvent.EventType.CHOOSE_TWO_LEADER_CARD, twoList.get(calls));
            calls++;
            entry.getValue().update(leaderCardEvent);
        }
    }

    private void debugPutDevCardsOnSlots(){
        DevSlot.slotPlace place = DevSlot.slotPlace.LEFT;
        Resources cost = new Resources(Resources.ResType.SERVANT,1);
        cost.add(Resources.ResType.COIN,1);
        cost.add(Resources.ResType.STONE,1);
        Resources LHS = new Resources(Resources.ResType.SERVANT, 1);
//        LHS.add(Resources.ResType.STONE, 2);
        Resources RHS = new Resources(Resources.ResType.STONE,1);
//        RHS.add(Resources.ResType.FAITH,1);
        int VP = 2;
        int level = 1;
        DevCard card = new DevCard(level, DevCard.CardColor.BLUE, LHS,RHS,cost, VP);

        DevSlot.slotPlace place2 = DevSlot.slotPlace.CENTER;
        Resources cost2 = new Resources(Resources.ResType.COIN,2);
        cost2.add(Resources.ResType.SERVANT,2);
        Resources LHS2 = new Resources(Resources.ResType.SHIELD, 1);
        LHS2.add(Resources.ResType.STONE, 1);
        Resources RHS2 = new Resources(Resources.ResType.FAITH,1);
        RHS2.add(Resources.ResType.SERVANT,2);
        int VP2 = 4;
        int level2 = 1;
        DevCard card2 = new DevCard(level2, DevCard.CardColor.BLUE, LHS2,RHS2,cost2, VP2);

        List<DevCard> devCardList = new ArrayList<>();
        devCardList.add(card);
        devCardList.add(card2);

        List<DevSlot.slotPlace> placeList = new ArrayList<>();
        placeList.add(place);
        placeList.add(place2);

        for (Integer userID: userIDs) {
            int calls = 0;
            game.getPersonalBoard(userID).putDevCardOnSlot(devCardList.get(calls), placeList.get(calls));
            calls++;
            game.getPersonalBoard(userID).putDevCardOnSlot(devCardList.get(calls), placeList.get(calls));
            updateAboutDevSlotOfId(userID);
        }
    }

 */
