package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumclasses.GameMode;

import java.util.List;
import java.util.Stack;

//Trying to implement attributes/methods of DevDeck class into this to delete DevDeck

public class Game {

    private List<Player> players;
    private List<PersonalBoard> boards;
    private MarketTray market; //or can it be MarketTray marketTray??
    private Resources resourceSupply; //or can it be Resources resource??
    private GameMode gameMode;
    private List<Stack<DevCard>> devCardDecks;

    public void addPlayer(Player player, int playerID, String nickname){
        {
            for (int i=0; i<4; i++){
                System.out.println("Do you wish to add a player?");
                this.players.add(new Player());
                System.out.println("Digit the ID: "+playerID);
                System.out.println("Digit the nickname: "+nickname);
            }


        }


    }

    public void addBoard(PersonalBoard board){
        {
            this.boards.add(new PersonalBoard());
        }


    }

    public void editBoard(PersonalBoard board){

    }

}


