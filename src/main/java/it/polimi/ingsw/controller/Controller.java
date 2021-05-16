package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.LeaderCard;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.VirtualView;
import it.polimi.ingsw.utility.messages.*;

import java.util.ArrayList;
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
        }
    }

    public void startMatch(){
        game.shuffleLeaderCards();
        sendFourLeaderCards();
    }

    private void sendFourLeaderCards(){
        CVEvent leaderCardEvent;
        int calls = 0;
        for (VirtualView virtualView: userIDtoVirtualViews.values()) {
            leaderCardEvent = new CVEvent(CVEvent.EventType.CHOOSE_TWO_LEADER_CARD,game.getFourLeaderCard(calls));
            calls++;
            virtualView.update(leaderCardEvent);
        }
    }

    @Override
    //TODO omer can continue from here
    public void update(VCEvent event) {
        switch (event.getEventType()){
            case LEADER_CARDS_CHOOSEN:
                break;
        }
    }
}
