package it.polimi.ingsw.server.controller;

import it.polimi.ingsw.server.model.Game;

// the third state, for players to select 4 leader cards and return 2 and take 2 (additional setup in pdf)
public class AdditionalSetupState implements GameState {

    Game game;

    public AdditionalSetupState(Game game) {
        this.game = game;
    }

    @Override
    public void enter() {

    }
}
