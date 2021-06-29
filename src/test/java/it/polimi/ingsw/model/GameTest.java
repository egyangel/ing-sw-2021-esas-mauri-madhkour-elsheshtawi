package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.server.VirtualView;
import it.polimi.ingsw.utility.messages.Listener;
import it.polimi.ingsw.utility.messages.MVEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    private List<Listener<MVEvent>> listenerList;
    private Map<Integer, PersonalBoard> userIDtoBoards ;
    private Map<Integer, VirtualView> userIDtoVirtualView;
    private MarketTray market;
    private List<LeaderCard> leaderCardList;
    private DevCardDeck[][] devCardMatrix;
    private Controller controller;
    private Map<PersonalBoard.PopeArea, Boolean> popeAreaMapTrigger;
    private boolean soloMode;

    @BeforeEach
    void setUp() {
        listenerList = new ArrayList<>();
        userIDtoBoards = new HashMap<>();
        userIDtoVirtualView = new HashMap<>();
        leaderCardList = new ArrayList<>();
        devCardMatrix = new DevCardDeck[3][4];
        popeAreaMapTrigger = new HashMap<>();

    }

    @AfterEach
    void tearDown() {
        listenerList = null;
        userIDtoBoards  = null;
        userIDtoVirtualView  = null;
        leaderCardList = null;
        devCardMatrix  = null;
        popeAreaMapTrigger = null;
    }

    @Test
    void createGameObjects() {
    }

    @Test
    void createDevCardDecks() {
    }

    @Test
    void getPersonalBoard() {
    }

    @Test
    void getMarketTray() {
    }

    @Test
    void shuffleLeaderCards() {
    }

    @Test
    void getFourLeaderCard() {
    }

    @Test
    void peekTopDevCard() {
    }

    @Test
    void removeTopDevCard() {
    }

    @Test
    void createDevCardMVEvent() {
    }

    @Test
    void subscribe() {
    }

    @Test
    void testSubscribe() {
    }

    @Test
    void publish() {
    }

    @Test
    void unsubscribe() {
    }

    @Test
    void testPublish() {
    }

    @Test
    void updateAllAboutChange() {
    }

    @Test
    void discardLowerCard() {
    }

    @Test
    void hasEmptySlot() {
    }

    @Test
    void setController() {
    }

    @Test
    void triggerVaticanReport() {
    }

    @Test
    void isEndTriggered() {
    }
}