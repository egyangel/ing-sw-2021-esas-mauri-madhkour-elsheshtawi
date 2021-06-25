package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enumclasses.SoloActionTokenType;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.VirtualView;
import it.polimi.ingsw.utility.messages.*;

import java.util.*;


public class SoloController extends Controller {
    private HashMap<String, String> crossToken; // CROSSTOKEN,Position
    private Integer crossTokenPoints = 0;
    private Map<Integer, ActionToken> actionTokens = new HashMap<Integer,ActionToken>();
    private Integer userID = 0;
    private Boolean alreadyStarted = false;
    private List<Integer> actionTokensOrder;
    private Integer currentActionTokenIndex= 0;

    public SoloController(Game game, Server server) {
        super(game, server);
    }

    @Override
    public void createMatch(Map<Integer, String> userIDtoNameMap) {
        userIDtoUsernames.putAll(userIDtoNameMap);

        for (Integer userID : userIDtoUsernames.keySet()) {
            this.userID = userID;
            game.addPlayer(userID);
            VirtualView virtualView = new VirtualView(userID, server.getClientHandler(userID));
            virtualView.subscribe(this);
            game.subscribe(userID, virtualView);
            userIDtoVirtualViews.put(userID, virtualView);
            TurnManager.putUserID(userID);
        }
        game.createGameObjects();

        //TODO INIT SOLO GAME
        /*

       -- Place the Black Cross token on the first space of your
       -- Faith Track (together with your Faith Marker).
       -- Shuffle the Solo Action tokens, create a stack and
       -- place it face-down on the table
         */

        // TODO test with actual 6 actions
        this.actionTokens.put(0,new ActionToken("Discard 2 Green Development Card", DevCard.CardColor.GREEN , SoloActionTokenType.DISCARD_DEV_CARD
        ));
        this.actionTokens.put(1,new ActionToken("Discard 2 Blue Development Card", DevCard.CardColor.BLUE, SoloActionTokenType.DISCARD_DEV_CARD
        ));
        this.actionTokens.put(2,new ActionToken("Discard 2 Yello Development Card", DevCard.CardColor.YELLOW, SoloActionTokenType.DISCARD_DEV_CARD
        ));
        this.actionTokens.put(3,new ActionToken("Discard 2 Purple Development Card", DevCard.CardColor.PURPLE, SoloActionTokenType.DISCARD_DEV_CARD));
        this.actionTokens.put(4,
                new ActionToken("Discard 2 Purple Development Card", null, SoloActionTokenType.MOVE_CROSS_TOKEN_TWO));
    this.actionTokens.put(5,
                new ActionToken("Discard 2 Purple Development Card", null, SoloActionTokenType.MOVE_CROSS_TOKEN_ONE_SHELF));
        actionTokensOrder = new ArrayList<Integer>(actionTokens.keySet());
        Collections.shuffle(actionTokensOrder);
    }





    private void shuffleActionTokenArray() {
        Collections.shuffle(actionTokensOrder);
        currentActionTokenIndex = 0;
    }

    /**
     * increase faith points after eachTurn
     */
    @Override
    protected void InitFatihPoints(Integer userID, Integer userTurn) {
        game.getPersonalBoard(userID).increaseFaitPoint(1);
    }




    @Override
    protected void sendTurnOrderAssign() {
        TurnManager.assignTurnOrder();
        for (Map.Entry<Integer, VirtualView> entry : userIDtoVirtualViews.entrySet()) {
            Integer userTurn = TurnManager.getOrderOfUserID(entry.getKey());

            TurnManager.registerResponse(entry.getKey());   // first player doesnt choose init res, its lack of response counts as a response

            // TODO for momo check if in solo mode the player get resources and faith points
            /*
            InitFatihPoints(entry.getKey(), userTurn);
            */

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

//    @Override
    protected void endTurn(Integer userId) {
        // TODO: to implement the end of the turn
        Integer currentIndex = actionTokensOrder.get(currentActionTokenIndex);
        ActionToken actionToken = actionTokens.get(currentActionTokenIndex);
        performAction(actionToken);
        currentActionTokenIndex = (currentActionTokenIndex + 1) % actionTokens.size();
        TurnManager.registerResponse(userId);
        TurnManager.goToNextTurn();
        // todo Note from Omer: I deleted the commented out methods in two line of code below, they are updated automatically now
//        game.sendMarketAndDevCardMatrixTo(currentUserID);
        //
        Integer currentUserID = TurnManager.getInkwellUserID();
        CVEvent beginTurnEvent = new CVEvent(CVEvent.EventType.SELECT_ALL_ACTION);
        userIDtoVirtualViews.get(currentUserID).update(beginTurnEvent);
    }

    private void performAction(ActionToken actionToken) {
        switch (actionToken.getType()){
            case DISCARD_DEV_CARD:
                this.game.discardLowerCard(actionToken.getColor(), 2);
                break;
            case MOVE_CROSS_TOKEN_TWO:
                crossTokenPoints = crossTokenPoints + 2;
                break;
            case MOVE_CROSS_TOKEN_ONE_SHELF:
                crossTokenPoints = crossTokenPoints + 1;
                shuffleActionTokenArray();
                break;
        }
    }

}
