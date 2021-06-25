package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;

import java.util.*;

public class TurnManager {
    private static Integer currentPlayerIndex = 0;
    private static List<Integer> turnOrderUserID;
    private static Map<Integer, Boolean> userIDtoCheck = new HashMap<>();
    private static Map<Integer, Boolean> userIDtoMajorAction = new HashMap<>();
    private static Game game;
    private static boolean endTriggeredAlready = false;

    public static void putUserID(Integer userID) {
        userIDtoCheck.put(userID, false);
    }

    public static void registerResponse(Integer userID) {
        if (!userIDtoCheck.get(userID)) {
            userIDtoCheck.replace(userID, true);
        } else
            System.out.println("Turn related register problem occured!");
    }

    public static void registerMajorActionDone(Integer userID) {
        userIDtoMajorAction.replace(userID, true);
    }

    public static boolean isMajorActionDone(Integer userID) {
        return userIDtoMajorAction.get(userID);
    }

    public static boolean hasAllClientsResponded() {
        for (Boolean hasEnded : userIDtoCheck.values()) {
            if (!hasEnded) return false;
        }
        userIDtoCheck.replaceAll((key, value) -> false);
        return true;
    }

    public static void assignTurnOrder() {
        turnOrderUserID = new ArrayList<>(userIDtoCheck.keySet());
        for (Integer userID : turnOrderUserID) {
            userIDtoMajorAction.put(userID, false);
        }
        Collections.shuffle(turnOrderUserID);
    }

    public static Integer getInkwellUserID() {
        return turnOrderUserID.get(0);
    }

    public static Integer getOrderOfUserID(Integer userID) {
        return turnOrderUserID.indexOf(userID) + 1;
    }

    public static Integer goToNextTurn() {
        int currentPlayerID = turnOrderUserID.get(currentPlayerIndex);
        userIDtoMajorAction.replace(currentPlayerID, false);
        currentPlayerIndex = (currentPlayerIndex + 1) % turnOrderUserID.size();
        return turnOrderUserID.get(currentPlayerIndex);
    }

    public static void setGame(Game game1) {
        game = game1;
    }

    public static boolean checkIfEndTriggered(Integer userID) {
        return endTriggeredAlready = game.IsEndTriggered(userID);

    }

    public static boolean checkIfEndOfGame(Integer userID) {
        return endTriggeredAlready;
    }

    public static int getRemainingNumberOfTurns() {
        return turnOrderUserID.size() - currentPlayerIndex - 1;
    }


    // TODO CONTINUE

    /**
     * method that trigger the event of the end of the game
     *
     * @param userID player id
     */
    private static void triggerTheEndGame(Integer userID) {
        // if the end is triggered after the player with inkwell played
        if (currentPlayerIndex == 0) {

        }
    }
}
