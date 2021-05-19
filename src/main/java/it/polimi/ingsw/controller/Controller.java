package it.polimi.ingsw.controller;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.LeaderCard;
import it.polimi.ingsw.model.Resources;
import it.polimi.ingsw.model.enumclasses.MarbleColor;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.VirtualView;
import it.polimi.ingsw.utility.messages.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// ALSO IMPLEMENTS Publisher<CVEvent> but ABSTRACT OUT LATER
public class Controller implements Listener<VCEvent> {

    private Server server;
    private Game game;
    private Map<Integer,String> userIDtoUsernames = new HashMap<>();
    private Map<Integer,VirtualView> userIDtoVirtualViews = new HashMap<>();

    public Controller(Game game, Server server) {
        this.game = game;
        this.server = server;
    }

    public void createMatch(Map<Integer, String> userIDtoNameMap) {
        userIDtoUsernames.putAll(userIDtoNameMap);
        game.createGameObjects();
        for(Integer userID: userIDtoUsernames.keySet()){
            game.addPlayer(userID);
            VirtualView virtualView = new VirtualView(userID, server.getClientHandler(userID));
            virtualView.subscribe(this);
            userIDtoVirtualViews.put(userID, virtualView);
            TurnManager.putUserID(userID);
        }
    }

    public void startMatch(){
        game.shuffleLeaderCards();
        sendFourLeaderCards();
    }

    private void sendFourLeaderCards(){
        int calls = 0;
        for (VirtualView virtualView: userIDtoVirtualViews.values()) {
            CVEvent leaderCardEvent = new CVEvent(CVEvent.EventType.CHOOSE_TWO_LEADER_CARD,game.getFourLeaderCard(calls));
            calls++;
            virtualView.update(leaderCardEvent);
        }
    }

    private void sendTurnOrderAssign(){
        TurnManager.assignTurnOrder();
        for(Map.Entry<Integer, VirtualView> entry: userIDtoVirtualViews.entrySet()){
            Integer userTurn = TurnManager.getIndexOfUserID(entry.getKey());
            InitFatihPoints(entry.getKey(), userTurn);
            CVEvent turnAssignEvent = new CVEvent(CVEvent.EventType.ASSIGN_TURN_ORDER,userTurn);
            entry.getValue().update(turnAssignEvent);
        }
    }

    private void InitFatihPoints(Integer userID, Integer userTurn){
        if (userTurn == 3 || userTurn == 4) {
            game.getPersonalBoard(userID).increaseFaitPoint(1);
        }
    }
    private void beginTurn(){
        Integer currentUserID = TurnManager.getCurrentPlayerID();
        CVEvent beginTurnEvent = new CVEvent(CVEvent.EventType.BEGIN_TURN);
        userIDtoVirtualViews.get(currentUserID).update(beginTurnEvent);
    }

    @Override
    public void update(VCEvent vcEvent) {
        Integer userID = vcEvent.getUserID();
        Resources resources;
        Resources resourcesWaitingToBePutIntoWarehouse;
        CVEvent cvEvent;
        switch (vcEvent.getEventType()) {
            case LEADER_CARDS_CHOOSEN:
                Type type = new TypeToken<List<LeaderCard>>() {
                }.getType();
                List<LeaderCard> selectedCards = (List<LeaderCard>) vcEvent.getEventPayload(type);
//                PRINT CARDS FOR DEBUG
//                for(LeaderCard card: selectedCards){
//                    System.out.println(card);
//                }
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
            case ROW_COLUMN_INDEX_CHOOSEN:
                String rowColumnNumber = (String) vcEvent.getEventPayload(String.class);
                List<MarbleColor> marbleList = null;
                char firstLetter = rowColumnNumber.charAt(0);
                int index = Integer.parseInt(String.valueOf(rowColumnNumber.charAt(0)));
                if (firstLetter == 'R') {
                    marbleList = game.getMarketTray().selectRowMarble(index);
                } else if (firstLetter == 'C') {
                    marbleList = game.getMarketTray().selectColumnMarble(index);
                } else System.out.println("Bad row/column and index came to server");
                cvEvent = new CVEvent(CVEvent.EventType.MARBLELIST_SENT, marbleList);
                userIDtoVirtualViews.get(userID).update(cvEvent);
                break;
            case WHITE_MARBLES_CONVERTED_IF_NECESSARY:
                resourcesWaitingToBePutIntoWarehouse = (Resources) vcEvent.getEventPayload(Resources.class);
                cvEvent = new CVEvent(CVEvent.EventType.ASK_SWAP_SHELVES);
                userIDtoVirtualViews.get(userID).update(cvEvent);
                break;
        }
    }
}
