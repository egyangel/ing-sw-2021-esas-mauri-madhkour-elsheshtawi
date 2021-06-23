package it.polimi.ingsw.controller;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enumclasses.MarbleColor;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.VirtualView;
import it.polimi.ingsw.utility.messages.*;

import java.lang.reflect.Type;
import java.util.*;
import static it.polimi.ingsw.utility.messages.LeaderActionContext.ActionStep.*;
import static it.polimi.ingsw.utility.messages.ActivateProdAlternativeContext.ActionStep.*;
import static it.polimi.ingsw.utility.messages.TakeResActionContext.ActionStep.*;
import static it.polimi.ingsw.utility.messages.BuyDevCardActionContext.ActionStep.*;
import static it.polimi.ingsw.utility.messages.CVEvent.EventType.*;
/**
 *  Controller class , Accepts input and converts it to commands for the model or view.
 *
 *   @author

 *
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
     *
     *@param game game object, it is the the model
     *@param server server object for managing connections with clients
     */
    public Controller(Game game, Server server) {
        this.game = game;
        this.server = server;
    }
    /**
     * method that create the game
     *
     *@param userIDtoNameMap it is the map of all user that are in the game

     */
    public void createMatch(Map<Integer, String> userIDtoNameMap) {
        userIDtoUsernames.putAll(userIDtoNameMap);
        userIDs.addAll(userIDtoUsernames.keySet());
        for (Integer userID : userIDs) {
            game.addPlayer(userID);
            VirtualView virtualView = new VirtualView(userID, server.getClientHandler(userID));
            virtualView.subscribe(this);
            game.subscribe(userID, virtualView);
            userIDtoVirtualViews.put(userID, virtualView);
            TurnManager.putUserID(userID);
        }
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
        //TODO FOR DEBUG, DONT FORGET TO REMOVE COMMENT FROM sendFourLeaderCards();
        debugInitilizeWarehouse();
        debugInitializeStrongbox();
        debugAutoChooseTwoLeadersToAll();
        debugPutDevCardsOnSlots();
//        sendFourLeaderCards();
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
    private void debugInitilizeWarehouse(){
        Resources topres = new Resources(Resources.ResType.STONE,1 );
        Resources midres = new Resources(Resources.ResType.SHIELD, 1);
        Resources bottomres = new Resources(Resources.ResType.COIN, 1);
        // initialize only with one-type res, zero values does not work
        for (Map.Entry<Integer, VirtualView> entry : userIDtoVirtualViews.entrySet()) {
            game.getPersonalBoard(entry.getKey()).putToWarehouse(Shelf.shelfPlace.TOP, topres);
            game.getPersonalBoard(entry.getKey()).putToWarehouse(Shelf.shelfPlace.MIDDLE, midres);
            game.getPersonalBoard(entry.getKey()).putToWarehouse(Shelf.shelfPlace.BOTTOM, bottomres);
            updateAboutWarehouseOfId(entry.getKey());
        }
    }

    private void debugInitializeStrongbox(){
        Resources strongboxres = new Resources(1,1,1,1);;
        for (Map.Entry<Integer, VirtualView> entry : userIDtoVirtualViews.entrySet()) {
            game.getPersonalBoard(entry.getKey()).addToStrongBox(strongboxres);
            updateAboutStrongboxOfId(entry.getKey());
        }
    }

    private void debugAutoChooseTwoLeadersToAll(){
        List<LeaderCard> list1 = new ArrayList<>();
        Requirement requirement = new Requirement(new Resources(0,0,0,5));
        SpecialAbility ability = new SpecialAbility(SpecialAbility.AbilityType.DISCOUNT, Resources.ResType.COIN);
        list1.add(new LeaderCard(requirement, 4, ability));
        requirement = new Requirement(new Resources(0,5,0,5));
        ability = new SpecialAbility(SpecialAbility.AbilityType.DISCOUNT, Resources.ResType.SERVANT);
        list1.add(new LeaderCard(requirement, 4, ability));

        List<LeaderCard> list2 = new ArrayList<>();
        requirement = new Requirement(new Resources(10,10,10,10));
        ability = new SpecialAbility(SpecialAbility.AbilityType.DISCOUNT, Resources.ResType.STONE);
        list2.add(new LeaderCard(requirement, 4, ability));
        requirement = new Requirement(new Resources(3,0,0,5));
        ability = new SpecialAbility(SpecialAbility.AbilityType.DISCOUNT, Resources.ResType.SHIELD);
        list2.add(new LeaderCard(requirement, 4, ability));

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
        Resources cost = new Resources(Resources.ResType.COIN,3);
        Resources LHS = new Resources(Resources.ResType.SHIELD, 1);
        LHS.add(Resources.ResType.STONE, 2);
        Resources RHS = new Resources(Resources.ResType.SHIELD,1);
        RHS.add(Resources.ResType.FAITH,1);
        int VP = 4;
        int level = 1;
        DevCard card = new DevCard(level, DevCard.CardColor.BLUE, LHS,RHS,cost, VP);
        for (Map.Entry<Integer, VirtualView> entry : userIDtoVirtualViews.entrySet()) {
            game.getPersonalBoard(entry.getKey()).putDevCardOnSlot(card, place);
            updateAboutDevSlotOfId(entry.getKey());
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
    //todo ask to omer what actually this methods do
    /**
     * method that handle the begin of the game
     */
    protected void beginTurn() {
        Integer currentUserID = TurnManager.getCurrentPlayerID();
        CVEvent beginTurnEvent = new CVEvent(CVEvent.EventType.SELECT_ALL_ACTION);
        userIDtoVirtualViews.get(currentUserID).update(beginTurnEvent);
    }
    /**
     * method that show the initial personal board description
     */
    private void sendInitPersonalBoardDescriptions(){
        for(Integer userId: userIDs){
            updateAboutWarehouseOfId(userId);
            updateAboutStrongboxOfId(userId);
            updateAboutFaithPointOfId(userId);
            updateAboutFaithTrackofId(userId);
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
    protected void updateAboutFaithTrackofId(Integer userId){
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
            case ACTIVATE_LEADER_CONTEXT_SELECTED:
                LeaderActionContext emptyActivateLeaderContext = new LeaderActionContext();
                emptyActivateLeaderContext.setPlayerCard(new HashSet<>(game.getPersonalBoard(userID).getInactiveLeaderCards()));
                emptyActivateLeaderContext.setLastStep(CHOOSE_ACTION);
                cvEvent = new CVEvent(ACTIVATE_LEADER_FILL_CONTEXT, emptyActivateLeaderContext);
                userIDtoVirtualViews.get(userID).update(cvEvent);
                break;
            case ACTIVATE_LEADER_CONTEXT_FILLED:
                LeaderActionContext activateLeaderContext = (LeaderActionContext) vcEvent.getEventPayload(LeaderActionContext.class);
                handleActivateLeaderAction(userID, activateLeaderContext);
                break;
            case TAKE_RES_ACTION_ENDED:
                TakeResActionContext takeResContextTwo = (TakeResActionContext) vcEvent.getEventPayload(TakeResActionContext.class);
                int faithPointsToAdd = takeResContextTwo.getFaithPoints();
                if (faithPointsToAdd > 0){
                    game.getPersonalBoard(userID).increaseFaitPoint(faithPointsToAdd);
                    updateAboutFaithPointOfId(userID);
                }
                int discardedRes = takeResContextTwo.getDiscardedRes();
                if (discardedRes > 0){
                    userIDs.remove(userID);
                    for(Integer anOtherUserId: userIDs){
                        game.getPersonalBoard(anOtherUserId).increaseFaitPoint(discardedRes);
                        updateAboutFaithPointOfId(anOtherUserId);
                    }
                    userIDs.add(userID);
                }
                CVEvent cvEventTwo = new CVEvent(SELECT_MINOR_ACTION);
                userIDtoVirtualViews.get(userID).update(cvEventTwo);
                break;
            case BUY_DEVCARD_ACTION_ENDED:
                // there is nothing to do when buy dev card ended
                CVEvent cvEventFour = new CVEvent(SELECT_MINOR_ACTION);
                userIDtoVirtualViews.get(userID).update(cvEventFour);
                break;
            case ACTIVATE_PROD_ACTION_ENDED:
                CVEvent cvEventFive = new CVEvent(SELECT_MINOR_ACTION);
                userIDtoVirtualViews.get(userID).update(cvEventFive);
                break;
            case ACTIVATE_LEADER_ACTION_ENDED:
                CVEvent cvEventSix = new CVEvent(SELECT_MINOR_ACTION);
                userIDtoVirtualViews.get(userID).update(cvEventSix);
                break;
                //TODO Omer maybe it is better in the second way, they do the same works
        /*
                case BUY_DEVCARD_ACTION_ENDED:

                case ACTIVATE_PROD_ACTION_ENDED:
                // there is nothing to do when buy dev card ended
                CVEvent cvEventFour = new CVEvent(SELECT_MINOR_ACTION);
                userIDtoVirtualViews.get(userID).update(cvEventFour);
                break;
        */

            case END_TURN:
                TurnManager.goToNextTurn();
                Integer nextUserID = TurnManager.getCurrentPlayerID();
                CVEvent cvEventThree = new CVEvent(SELECT_ALL_ACTION);
                userIDtoVirtualViews.get(nextUserID).update(cvEventThree);
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
        int discarded = game.getPersonalBoard(userID).clearShelf(place);
        context.addDiscardedRes(discarded);
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
        int discarded = game.getPersonalBoard(userID).swapShelves(places);
        context.addDiscardedRes(discarded);
        context.setLastStep(CHOOSE_SHELVES);
        updateAboutWarehouseOfId(userID);
    }
    /**
     * method that handle the insertion of resources inside the shelf and update the
     * @param userID player id
     * @param context  is the context of take res action. it is filled in both view and controller side with info needed to complete the action
     */
    private void handlePutResourcesChosen(Integer userID, TakeResActionContext context){
        // todo omer do not put resources of same type to two different shelves
        Map<Shelf.shelfPlace, Resources.ResType> map = context.getShelfPlaceResTypeMap();
        Map<Shelf.shelfPlace, Boolean> shelfToResult = new HashMap<>();
        boolean result;
        for (Map.Entry<Shelf.shelfPlace, Resources.ResType> entry : map.entrySet()) {
            Resources resToPut = new Resources();
            resToPut.add(entry.getValue(), context.getResources().getNumberOfType(entry.getValue()));
            int discardedSameTypeRes = game.getPersonalBoard(userID).putToWarehouse(entry.getKey(), resToPut);
            result = (discardedSameTypeRes >= 0);
            if (result) {
                context.addDiscardedRes(discardedSameTypeRes);
            }
            shelfToResult.put(entry.getKey(), result);
        }
        context.setPutResultMap(shelfToResult);
        context.removeResourcesPutToShelf();
        context.setLastStep(CHOOSE_SHELVES); //choose shelves is correct, I did it this way intentionally
        updateAboutWarehouseOfId(userID);
    }

    protected void endTurn(Integer userId){
        //TODO to implement all checks

        TurnManager.registerResponse(userId);
        TurnManager.goToNextTurn();
        Integer currentUserID = TurnManager.getCurrentPlayerID();
        CVEvent beginTurnEvent = new CVEvent(CVEvent.EventType.SELECT_ALL_ACTION);
        userIDtoVirtualViews.get(currentUserID).update(beginTurnEvent);

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
        updateAboutLeaderCardsOfId(userID); //todo add extra slot representation on CLI
        updateAboutDevSlotOfId(userID);
        context.setLastStep(COST_PAID_DEVCARD_PUT);
        if(game.getPersonalBoard(userID).getOwnedCard().size()== 7) {
            triggerTheEndGame(userID);
        }
    }
    //handle the activation of production
    private void handleActivateDevCardAction(Integer userID, ActivateProdAlternativeContext context){
        switch (context.getLastStep()){
            case DEVLSLOTS_CHOOSEN_FOR_PROD:
                handleActivateDevSlotsProductionChosen(userID, context);
                break;
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

    private void setAvailableAddProdLeaders(Integer userID, ActivateProdAlternativeContext context){
        List<LeaderCard> leaderCardList = new ArrayList<>();
        List<LeaderCard> addProdLeaderList = new ArrayList<>();
        leaderCardList.addAll(game.getPersonalBoard(userID).getActiveLeaderCards());
        for(LeaderCard leaderCard : leaderCardList){
            if (leaderCard.getAbility().getAbilityType() == SpecialAbility.AbilityType.ADDPROD){
                addProdLeaderList.add(leaderCard);
            }
        }
        if (addProdLeaderList.isEmpty()){
            context.setAddProdOptionAvailable(false);
            return;
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
            Map<LeaderCard,Resources.ResType> leaderToResMap = context.getAddProdLeaderResMap();
            for (Map.Entry<LeaderCard, Resources.ResType> entry : leaderToResMap.entrySet()) {
                Resources.ResType type = entry.getKey().getAbility().getResType();
                totalCost.add(type, 1);
                type = entry.getValue();
                totalProduction.add(type,1);
            }
        }
        Resources totalResources = game.getPersonalBoard(userID).getTotalResources();
        boolean enoughRes = totalResources.includes(totalCost);
        if(!enoughRes){
            context.setLastStep(NOT_ENOUGH_RES_ON_PERSONAL_BOARD);
            return;
        } else {
            payCostFromPersonalBoard(userID, totalCost);
            Resources faithRes = totalProduction.splitThisType(Resources.ResType.FAITH);
            game.getPersonalBoard(userID).increaseFaitPoint(faithRes.sumOfValues());
            game.getPersonalBoard(userID).addToStrongBox(totalProduction);
            updateAboutWarehouseOfId(userID);
            updateAboutStrongboxOfId(userID);
            updateAboutFaithPointOfId(userID);
            updateAboutLeaderCardsOfId(userID); //todo add extra slot representation on CLI
            context.setLastStep(PRODUCTION_DONE);
        }
    }

    private void payCostFromPersonalBoard(Integer userID, Resources totalCost){
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
        // automatic payment from warehouse
        Resources warehouseRes = game.getPersonalBoard(userID).getWarehouseResources();
        for(Resources.ResType type: totalCost.getResTypes()){
            Resources oneTypeRes = totalCost.cloneThisType(type);
            if(warehouseRes.includes(oneTypeRes)){
                game.getPersonalBoard(userID).subtractFromWarehouse(oneTypeRes);
                totalCost.subtract(oneTypeRes);
            } else {
                Resources paidPartFromWarehouse = warehouseRes.cloneThisType(type);
                game.getPersonalBoard(userID).subtractFromStrongbox(paidPartFromWarehouse);
                totalCost.subtract(paidPartFromWarehouse);
            }
        }
        // automatic payment from extraslot
        if (!totalCost.isEmpty()){
            //this means there was extra resources used in the extra slot
            game.getPersonalBoard(userID).subtractFromExtraSlot(totalCost);
        }
        // maybe assert total cost = 0 here
    }
    /**
     * method that handle the leader cards action like discard or activation leader cards.
     * @param userID player id
     * @param context is the leader card context. it is filled in both view and controller side with info needed to complete the action
     */
    private void handleActivateLeaderAction(Integer userID, LeaderActionContext context){
        switch (context.getLastStep()){
            case DISCARD_LEADER_CARD:
                handleDiscardLeaderChosen(userID, context);
                context.setLastStep(END_LEADER_ACTION);
                break;
            case LEADER_CARD_ACTIVATED_CHOSEN:
                handleActivationLeaderChosen(userID, context);
                context.setLastStep(END_LEADER_ACTION);
                break;
            case BOTH_ACTIONS:
                handleDiscardLeaderChosen(userID, context);
                handleActivationLeaderChosen(userID, context);
                context.setLastStep(END_LEADER_ACTION);
                break;
            case LEADER_CARD_NOT_ACTIVATED_CHOSEN:
                context.setLastStep(END_LEADER_ACTION);
                break;
        }

        updateAboutLeaderCardsOfId(userID);
        CVEvent cvEvent = new CVEvent(ACTIVATE_LEADER_FILL_CONTEXT, context);
        userIDtoVirtualViews.get(userID).update(cvEvent);
    }
    /**
     * method that handle the discard of a leader card.
     * @param userID player id
     * @param context is the leader card context. it is filled in both view and controller side with info needed to complete the action
     */
    private void  handleDiscardLeaderChosen(Integer userID, LeaderActionContext context) {
       game.getPersonalBoard(userID).changePlayerCard(context.discardedPlayerCard());
       game.getPersonalBoard(userID).increaseFaitPoint(context.discardedPlayerCard().size());
    }
    /**
     * method that handle the activation of leader cards
     * @param userID player id
     * @param context is the leader card context. it is filled in both view and controller side with info needed to complete the action
     */
    private void  handleActivationLeaderChosen(Integer userID, LeaderActionContext context){
       checkLeaderActivationAction(userID,context);
       int i=0;
       while(i<context.getActiveLeaderCard().size()){
           System.out.println(context.getActiveLeaderCard().get(i));
       }
       game.getPersonalBoard(userID).setActiveLeaderCards(new HashSet<>(context.getActiveLeaderCard()));
       game.getPersonalBoard(userID).changePlayerCard(context.getActiveLeaderCard());

    }
    /**
     * methods that check if the requirements of leader cards  are satisfied for activation
     * @param userID player id
     * @param context is the leader card context. it is filled in both view and controller side with info needed to complete the action
     */
    private void checkLeaderActivationAction (Integer userID, LeaderActionContext context){
        int discConvert;
        int firstCount = 0;
        int secondCount = 0;
        List<LeaderCard> activeLeaderCards= new ArrayList<>();
        int i = 0;
        int j = 0;
        while (j < context.getLeadersToActivate().size()) {
            if (context.getLeadersToActivate().get(j).getAbility().getAbilityType() == SpecialAbility.AbilityType.ADDPROD) {
                while (i < context.getOwnedCards().size()) {
                    if (context.getLeadersToActivate().get(j).getRequirement().getColor(0).equals(game.getPersonalBoard(userID).getOwnedCard().get(i).getColor()) &&
                            context.getOwnedCards().get(i).getLevel() == 2) {
                        activeLeaderCards.add(context.getLeadersToActivate().get(j));
                    }
                    i++;
                }
            }
            if (context.getLeadersToActivate().get(j).getAbility().getAbilityType() == SpecialAbility.AbilityType.EXTRASLOT) {
                if ((game.getPersonalBoard(userID).getTotalResources().getNumberOfType(context.getLeadersToActivate().get(j).getRequirement().getResource().getOnlyType()) == 5)) {
                    activeLeaderCards.add(context.getLeadersToActivate().get(j));
                }
            } else {
                if (context.getLeadersToActivate().get(j).getAbility().getAbilityType() == SpecialAbility.AbilityType.CONVERTWHITE) {
                    discConvert = 1;
                } else {
                    discConvert = 2;
                }
                while (i < game.getPersonalBoard(userID).getOwnedCard().size()) {
                    if (context.getLeadersToActivate().get(j).getRequirement().getColor(0).equals(game.getPersonalBoard(userID).getOwnedCard().get(i).getColor()) && game.getPersonalBoard(userID).getOwnedCard().get(i).getLevel() == 1)
                        firstCount++;
                    if (context.getLeadersToActivate().get(j).getRequirement().getColor(1).equals(game.getPersonalBoard(userID).getOwnedCard().get(i).getColor()) && game.getPersonalBoard(userID).getOwnedCard().get(i).getLevel() == 1)
                        secondCount++;
                    i++;
                }
                if (firstCount >= 1 && secondCount >= 1 && discConvert == 1)
                    activeLeaderCards.add(context.getLeadersToActivate().get(j));

                if (firstCount >= 2 && secondCount >= 1 && discConvert == 2)
                    activeLeaderCards.add(context.getLeadersToActivate().get(j));

            }
            j++;
        }
       context.setActiveLeaderCard(new HashSet<>(activeLeaderCards));
    }
    /**
     * method that compute the total victory point of the player
     * @param userID player id
     */

    private void computeVictoryPoint(Integer userID) {

        int respoint;
        game.getPersonalBoard(userID).countVictoryPoints(game.getPersonalBoard(userID).getTurnPopeFavorTile());
        respoint =game.getPersonalBoard(userID).getTotalResources().sumOfValues();
        respoint = respoint/5;

        game.getPersonalBoard(userID).countVictoryPoints(respoint);
    }
    /**
     * method that trigger the event of the end of the game
     * @param userID player id
     */
    private void  triggerTheEndGame(Integer userID) {

        computeVictoryPoint(userID);

        CVEvent cvEvent = new CVEvent(END_GAME);
        userIDtoVirtualViews.get(userID).update(cvEvent);

    }


    public void handleGameMessage(Integer userID, Message msg) {
        userIDtoVirtualViews.get(userID).handleGameMessage(msg);

    }

    public void takeVaticanReports(PersonalBoard.PopeArea area){
        Map<PersonalBoard.PopeArea, Boolean> map;
        for(Integer userID: userIDs){
            game.getPersonalBoard(userID).giveVaticanReport(area);
            updateAboutFaithTrackofId(userID);
        }
    }
}
