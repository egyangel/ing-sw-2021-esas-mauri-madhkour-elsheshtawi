package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.server.Server;
import it.polimi.ingsw.network.server.VirtualView;
import it.polimi.ingsw.utility.messages.Listener;
import it.polimi.ingsw.utility.messages.VCEvent;

import java.util.HashMap;
import java.util.Map;

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
            userIDtoVirtualViews.put(userID, new VirtualView(userID));
        }
    }

    @Override
    public void update(VCEvent event) {
    }
}
