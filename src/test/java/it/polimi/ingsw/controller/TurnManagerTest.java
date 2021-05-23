package it.polimi.ingsw.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TurnManagerTest {
    private static Integer currentPlayerIndex;
    private static List<Integer> turnOrderUserID;
    private static Map<Integer, Boolean> userIDtoCheck;
    private Integer userID;
    private TurnManager t;

    @BeforeEach
    void setUp() {
        currentPlayerIndex = 0;
        userIDtoCheck = new HashMap<>();
        userID = 10;
        t= new TurnManager();


    }

    @AfterEach
    void tearDown() {
        currentPlayerIndex = null;
        userIDtoCheck.clear();
        userID = null;
        t=null;
    }

    @Test
    void putUserID() {
        t.putUserID(userID);
        assertEquals(userID,t.getIndexOfUserID(userID));



    }

    @Test
    void registerResponse() {
    }

    @Test
    void hasAllClientsResponded() {
    }

    @Test
    void assignTurnOrder() {
    }

    @Test
    void getNextPlayerID() {
    }

    @Test
    void getCurrentPlayerID() {
    }

    @Test
    void getCurrentPlayerIndex() {
    }

    @Test
    void getIndexOfUserID() {
    }

    @Test
    void goToNextTurn() {
    }
}