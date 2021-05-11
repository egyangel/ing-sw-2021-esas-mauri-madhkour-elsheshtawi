package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.enumclasses.GameMode;

import java.util.List;
import java.util.Stack;

// MODEL CLASS
public class Game {

    private List<Player> players;
    private List<PersonalBoard> boards;
    private MarketTray market; //or can it be MarketTray marketTray??
    private Resources resourceSupply; //or can it be Resources resource??
    private GameMode gameMode;
    private List<Stack<DevCard>> devCardDecks;

    public void addNewPlayer(String nickname) {
        players.add(new Player(nickname));
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addNewBoardFor(Player player) {
        this.boards.add(new PersonalBoard(player));
    }

    public void activate() {

        for (Player p : players) {
            //I can put all the 3 if statement in a huge single instruction ,but in this it is more readable
            if (p.getPersonalBoardl().getFaithpos() >= 8 && p.getPersonalBoardl().getFaithpos() <= 11 )
                p.getPersonalBoardl().check(1);
            if (p.getPersonalBoardl().getFaithpos() >= 16 && p.getPersonalBoardl().getFaithpos() <= 18)
                p.getPersonalBoardl().check(2);
            if(p.getPersonalBoardl().getFaithpos() == 24)
                p.getPersonalBoardl().check(3);


        }
    }

    public void setController(Controller controller) {
//        this.controller = controller;
    }
}
