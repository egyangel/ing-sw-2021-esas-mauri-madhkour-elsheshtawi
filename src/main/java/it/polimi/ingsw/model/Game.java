package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameState;
import it.polimi.ingsw.model.enumclasses.GameMode;

import java.util.List;
import java.util.Stack;

//Trying to implement attributes/methods of DevDeck class into this to delete DevDeck

public class Game {

    private GameState gameState;
    private List<Player> players;
    private List<PersonalBoard> boards;
    private MarketTray market; //or can it be MarketTray marketTray??
    private Resources resourceSupply; //or can it be Resources resource??
    private GameMode gameMode;
    private List<Stack<DevCard>> devCardDecks;

    public void addNewPlayer(String nickname){
        players.add(new Player(nickname));
    }

    public List<Player> getPlayers(){
        return players;
    }

    public void setNextGameState(GameState nextGameState){
        this.gameState = nextGameState;
    }

    public void enterNextState(){
        this.gameState.enter();
    }

    public void addNewBoard(PersonalBoard board){
        this.boards.add(board);
    }
}


