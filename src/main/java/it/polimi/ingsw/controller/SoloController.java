package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.VirtualView;
import it.polimi.ingsw.utility.messages.*;

import java.util.*;

/**
 *  SoloController class , Extend controller and implement solo mode logic.
 * */
public class SoloController extends Controller {
    private Integer crossTokenPoints = 0;
    private Map<Integer, ActionToken> actionTokens = new HashMap<>();
    private Integer userID = 0;
    private Boolean alreadyStarted = false;
    private List<Integer> actionTokensOrder;
    private Integer currentActionTokenIndex= 0;

    public SoloController(Game game, Server server) {
        super(game, server);
    }


    /**
     * method that create the game in soloMode
     *@param userIDtoNameMap it is the map of all user that are in the game

     */

    @Override
    public void createMatch(Map<Integer, String> userIDtoNameMap) {
        userIDtoUsernames.putAll(userIDtoNameMap);

        for (Integer userID : userIDtoUsernames.keySet()) {
            this.userID = userID;
            VirtualView virtualView = new VirtualView(userID, server.getClientHandler(userID));
            virtualView.subscribe(this);
            game.subscribe(userID, virtualView);
            userIDtoVirtualViews.put(userID, virtualView);
            TurnManager.putUserID(userID);
        }
        TurnManager.setGame(game);
        game.createGameObjects();


        this.actionTokens.put(0,new ActionToken("Discard 2 Green Development Card", DevCard.CardColor.GREEN , ActionToken.SoloActionTokenType.DISCARD_DEV_CARD
        ));
        this.actionTokens.put(1,new ActionToken("Discard 2 Blue Development Card", DevCard.CardColor.BLUE, ActionToken.SoloActionTokenType.DISCARD_DEV_CARD
        ));
        this.actionTokens.put(2,new ActionToken("Discard 2 Yellow Development Card", DevCard.CardColor.YELLOW, ActionToken.SoloActionTokenType.DISCARD_DEV_CARD
        ));
        this.actionTokens.put(3,new ActionToken("Discard 2 Purple Development Card", DevCard.CardColor.PURPLE, ActionToken.SoloActionTokenType.DISCARD_DEV_CARD));
        this.actionTokens.put(4,
                new ActionToken("Move Black Cross forward by 2", null, ActionToken.SoloActionTokenType.MOVE_CROSS_TOKEN_TWO));
    this.actionTokens.put(5,
                new ActionToken("Move the Black Cross forward by 1 ,shuffle all Solo Action tokens and create new stack.", null, ActionToken.SoloActionTokenType.MOVE_CROSS_TOKEN_ONE_SHELF));
        actionTokensOrder = new ArrayList<>(actionTokens.keySet());
        Collections.shuffle(actionTokensOrder);
    }
    /**
     * Shuffle the solo action tokens
     */
    private void shuffleActionTokenArray() {
        Collections.shuffle(actionTokensOrder);
        currentActionTokenIndex = 0;
    }
    /**
     * increase faith points after eachTurn
     */
    /*@Override
    /*protected void InitFatihPoints(Integer userID, Integer userTurn) {
        game.getPersonalBoard(userID).increaseFaitPoint(1);
    }*/
    /**
     * send turn order
     */
    @Override
    protected void sendTurnOrderAssign() {
        TurnManager.assignTurnOrder();
        for (Map.Entry<Integer, VirtualView> entry : userIDtoVirtualViews.entrySet()) {
            Integer userTurn = TurnManager.getOrderOfUserID(entry.getKey());

            TurnManager.registerResponse(entry.getKey());   // first player doesnt choose init res, its lack of response counts as a response

            CVEvent turnAssignEvent = new CVEvent(CVEvent.EventType.ASSIGN_TURN_ORDER, userTurn);
            entry.getValue().update(turnAssignEvent);
        }
        //todo Omer: I updated mv event about warehouse
        updateAboutWarehouseOfId(userID);
        beginMatch();
    }

    @Override
    protected void beginMatch() {
        if (alreadyStarted)
            return;
        alreadyStarted = true;
        super.beginMatch();

    }
    /**
     * handle the end of the turn of soloMode and do the soloTokenActions
     * @param userId id of the user
     */
    @Override
    protected void handleEndTurn(Integer userId) {


        ActionToken actionToken = actionTokens.get(currentActionTokenIndex);
        performAction(actionToken);
        currentActionTokenIndex = (currentActionTokenIndex + 1) % actionTokens.size();
        TurnManager.registerResponse(userId);
        super.handleEndTurn(userId);
    }

    private void performAction(ActionToken actionToken) {
        switch (actionToken.getType()){
            case DISCARD_DEV_CARD:
                this.game.discardLowerCard(2);
                super.updateAboutWarehouseOfId(userID);
                break;
            case MOVE_CROSS_TOKEN_TWO:
                crossTokenPoints = crossTokenPoints + 2;
                updateCrossTokenPoints();
                break;
            case MOVE_CROSS_TOKEN_ONE_SHELF:
                crossTokenPoints = crossTokenPoints + 1;
                updateCrossTokenPoints();
                shuffleActionTokenArray();
                break;
        }
    }

    /**
     * send black cross faith point to the player on change
     */
    public void updateCrossTokenPoints(){
        MVEvent crossTokenUpdate = new MVEvent(this.userID, MVEvent.EventType.BLACKCROSS_FAITHPOINT_UPDATE, crossTokenPoints);
        game.updateAllAboutChange(crossTokenUpdate);
    }

}
