package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.utility.messages.CVEvent;

import java.util.*;

public class TurnManager {
    private static Integer currentPlayerIndex = 0;
    private static List<Integer> turnOrderUserID;
    private static Map<Integer, Boolean> userIDtoCheck = new HashMap<>();
    private static Game game;
    private static boolean endTriggeredAlready = false;

    public static void putUserID(Integer userID){
        userIDtoCheck.put(userID, false);
    }

    public static void registerResponse(Integer userID){
        if (!userIDtoCheck.get(userID)){
            userIDtoCheck.replace(userID, true);
        } else
            System.out.println("Turn related register problem occured!");
    }

    public static boolean hasAllClientsResponded(){
        for (Boolean hasEnded: userIDtoCheck.values()){
            if (!hasEnded) return false;
        }
        userIDtoCheck.replaceAll((key, value) -> false);
        return true;
    }

    public static void assignTurnOrder() {
        turnOrderUserID = new ArrayList<>(userIDtoCheck.keySet());
        Collections.shuffle(turnOrderUserID);
    }

    public static Integer getNextPlayerID(){

        return turnOrderUserID.get(currentPlayerIndex+1);
    }

    public static Integer getCurrentPlayerID() {return turnOrderUserID.get(currentPlayerIndex);
    }

    public static Integer getCurrentPlayerIndex(){
        return currentPlayerIndex;
    }

    public static Integer getOrderOfUserID(Integer userID){
        return turnOrderUserID.indexOf(userID) + 1;
    }

    public static void goToNextTurn(){
        currentPlayerIndex = (currentPlayerIndex + 1) % turnOrderUserID.size();
    }

    public static void setGame(Game game1){
        game = game1;
    }

    public static boolean checkIfEndTriggered(Integer userID){
        //TODO convert to original end criteria
        boolean endByDevCard = (game.getPersonalBoard(userID).getOwnedCard().size()== 3);
        boolean endByFaithPoints = (game.getPersonalBoard(userID).getFaithPoints() == 24);
        if(endByDevCard || endByFaithPoints) {
            endTriggeredAlready = true;
            return true;
        }
        return false;
    }

    public static boolean checkIfEndOfGame(Integer userID){
        return endTriggeredAlready;
    }

    public static int getRemainingNumberOfTurns(){
        return turnOrderUserID.size() - currentPlayerIndex - 1;
    }


    // TODO CONTINUE
    /**
     * method that trigger the event of the end of the game
     * @param userID player id
     */
    private static void  triggerTheEndGame(Integer userID) {
        // if the end is triggered after the player with inkwell played
        if(currentPlayerIndex == 0) {

        }
    }
}
