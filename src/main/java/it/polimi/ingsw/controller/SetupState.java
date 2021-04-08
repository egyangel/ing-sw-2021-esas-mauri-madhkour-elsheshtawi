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
        createDevCards();

        for(Player player: game.getPlayers()){
            createBoardFor(player);
        }
    }

    private void createBoardFor(Player player){
        game.addNewBoard(new PersonalBoard(player));
    }

    private void createDevCards(){
    }
}
