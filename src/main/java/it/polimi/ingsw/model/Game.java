package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.enumclasses.GameMode;

import java.util.*;

// MODEL CLASS
public class Game {

    private Map<Integer,Player> userIDtoPlayers = new HashMap<>();
    private Map<Integer,PersonalBoard> userIDtoBoards = new HashMap<>();
    private MarketTray market; //or can it be MarketTray marketTray??
    private Resources resourceSupply; //or can it be Resources resource??
    private GameMode gameMode;
    private List<Deque<DevCard>> devCardDecks = new ArrayList<>();

    public void addPlayer(Integer userID) {
        userIDtoPlayers.put(userID, new Player());
    }

//    public void activate() {
//
//        for (Player p : players) {
//            //I can put all the 3 if statement in a huge single instruction ,but in this it is more readable
//            if (p.getPersonalBoardl().getFaithpos() >= 8 && p.getPersonalBoardl().getFaithpos() <= 11 )
//                p.getPersonalBoardl().check(1);
//            if (p.getPersonalBoardl().getFaithpos() >= 16 && p.getPersonalBoardl().getFaithpos() <= 18)
//                p.getPersonalBoardl().check(2);
//            if(p.getPersonalBoardl().getFaithpos() == 24)
//                p.getPersonalBoardl().check(3);
//
//
//        }
//    }

    public void setGameMode(GameMode gameMode){
        this.gameMode = gameMode;
    }

    public void initGameObjects(){
        for(Map.Entry<Integer, Player> entry: userIDtoPlayers.entrySet()){
            userIDtoBoards.put(entry.getKey(), new PersonalBoard(entry.getKey()));
        }
    }
}
