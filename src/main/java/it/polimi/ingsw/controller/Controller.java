package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.server.VirtualView;
import it.polimi.ingsw.utility.messages.Listener;
import it.polimi.ingsw.utility.messages.VCEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller implements Listener<VCEvent> {
    // in case of multiple games, this should be list of games
    private Game game;
    private Map<Integer,VirtualView> userIDtoVirtualViews = new HashMap<>();

    public Controller(Game game) {
        this.game = game;
    }

    public void startGame(){
        // this brute style is just for initialization, there will be a loop for managing turns
        game.setNextGameState(new RegistrationState(game));
        game.enterNextState();
        game.setNextGameState(new SetupState(game));
        game.enterNextState();
        game.setNextGameState(new AdditionalSetupState(game));
    }

    public void addPlayer(Integer userID, String username, VirtualView virtualView) {
        game.addNewPlayer(username);
    }

    @Override
    public void update(VCEvent event) {
    }
}
