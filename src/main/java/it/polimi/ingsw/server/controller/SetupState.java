package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PersonalBoard;
import it.polimi.ingsw.model.Player;

// the second state, to initialize objects, creating boards, cards and shuffling them etc (setup + player's setup in pdf)
public class SetupState implements GameState{
    Game game;

    public SetupState(Game game){
        this.game = game;
    }

    @Override
    public void enter() {
        createBoards();
        createDevCards();
    }

    private void createBoards(){
        for(Player player: game.getPlayers()){
            game.addNewBoardFor(player);
        }
    }

    private void createDevCards(){
    }
}
