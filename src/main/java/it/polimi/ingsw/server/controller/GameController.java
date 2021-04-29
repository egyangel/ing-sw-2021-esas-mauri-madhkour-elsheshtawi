package it.polimi.ingsw.controller;

import it.polimi.ingsw.server.model.Game;

public class GameController {
    Game game;

    public GameController(Game game) {
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
}
