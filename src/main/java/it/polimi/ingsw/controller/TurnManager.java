package it.polimi.ingsw.controller;

import java.util.*;

public class TurnManager {
    private static Integer currentPlayerIndex = 0;
    private static List<Integer> turnOrderUserID;
    private static Map<Integer, Boolean> userIDtoCheck = new HashMap<>();

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

    public static Integer getCurrentPlayerID() {
        return turnOrderUserID.get(currentPlayerIndex);
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
}
