package it.polimi.ingsw.model;


import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.server.VirtualView;
import it.polimi.ingsw.utility.JsonConverter;
import it.polimi.ingsw.utility.messages.Listener;
import it.polimi.ingsw.utility.messages.MVEvent;
import it.polimi.ingsw.utility.messages.Publisher;

import java.util.*;
/**
 * Game class, it is the model class, it is the class that handle all the game logic
 * */
// MODEL CLASS
public class Game implements Publisher<MVEvent> {

    private List<Listener<MVEvent>> listenerList = new ArrayList<>();
    private Map<Integer, PersonalBoard> userIDtoBoards = new HashMap<>();
    private Map<Integer, VirtualView> userIDtoVirtualView = new HashMap<>();
    private MarketTray market;
    private Resources resourceSupply;
    private List<LeaderCard> leaderCardList = new ArrayList<>();
    private DevCardDeck[][] devCardMatrix = new DevCardDeck[3][4];
    private Controller controller;
    private Map<PersonalBoard.PopeArea, Boolean> popeAreaMapTrigger = new HashMap<>();
    private boolean soloMode;

    public void createGameObjects() {
        createBoardForEachPlayer();
        createDevCardDecks();
        createMarketTray();
        createLeaderCards();
        popeAreaMapTrigger.put(PersonalBoard.PopeArea.FIRST, false);
        popeAreaMapTrigger.put(PersonalBoard.PopeArea.SECOND, false);
        popeAreaMapTrigger.put(PersonalBoard.PopeArea.THIRD, false);
    }

    private void createBoardForEachPlayer() {
        this.soloMode = userIDtoVirtualView.size() == 1;
        for (Map.Entry<Integer, VirtualView> entry : userIDtoVirtualView.entrySet()) {
            PersonalBoard pb = new PersonalBoard(entry.getKey(), soloMode);
            pb.setGame(this);
            userIDtoBoards.put(entry.getKey(), pb);
        }
    }

    public void createDevCardDecks() {
        List<DevCard> allDevCards = JsonConverter.deserializeDevCards();
        Iterator<DevCard> cardIterator = allDevCards.iterator();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                // DevCardDeck constructor argument order(color, level), devcardmatrix is reverse, it can stay like this
                DevCardDeck deck = new DevCardDeck(DevCard.CardColor.values()[j], i + 1);
                for (int k = 0; k < 4; k++) {
                    deck.putCard(cardIterator.next());
                }
                deck.shuffleDeck();
                devCardMatrix[i][j] = deck;
            }
        }
    }

    private void createMarketTray() {
        this.market = new MarketTray();
    }

    private void createLeaderCards() {
        leaderCardList = JsonConverter.deserializeLeaderCards();
    }

    public PersonalBoard getPersonalBoard(Integer userID) {
        return userIDtoBoards.get(userID);
    }

    public MarketTray getMarketTray() {
        return market;
    }

    public void shuffleLeaderCards() {
        Collections.shuffle(leaderCardList);
    }

    public List<LeaderCard> getFourLeaderCard(int counterOfCalls) {
        List<LeaderCard> list = new ArrayList<>();
        list.addAll(leaderCardList.subList(counterOfCalls * 4, (counterOfCalls * 4) + 4));
        return list;
    }

    public DevCard peekTopDevCard(DevCard.CardColor color, int level) {
        DevCard card = devCardMatrix[level - 1][color.ordinal()].peekTopCard();
        return card;
    }

    public void removeTopDevCard(DevCard.CardColor color, int level) {
        devCardMatrix[level - 1][color.ordinal()].removeTopCard();
    }

    public MVEvent createDevCardMVEvent() {
        List<DevCard> topDevCards = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                topDevCards.add(devCardMatrix[i][j].peekTopCard());
            }
        }
        MVEvent devCardMatrixUpdate = new MVEvent(MVEvent.EventType.DEVCARD_MATRIX_UPDATE, topDevCards);
        return devCardMatrixUpdate;
    }

    @Override
    public void subscribe(Listener<MVEvent> listener) {
        listenerList.add(listener);
    }

    public void subscribe(Integer userID, VirtualView virtualView) {
        userIDtoVirtualView.put(userID, virtualView);
    }

    public void publish(Integer userID, MVEvent event) {
        userIDtoVirtualView.get(userID).update(event);
    }

    @Override
    public void unsubscribe(Listener<MVEvent> listener) {
        listenerList.remove(listener);
    }

    // this doesn't work, use publishToAll
    @Override
    public void publish(MVEvent event) {
        for (Listener<MVEvent> listener : listenerList) {
            listener.update(event);
        }
    }


    public void updateAllAboutChange(MVEvent event) {
        for (VirtualView virtualView : userIDtoVirtualView.values()) {
            virtualView.update(event);
        }
    }


    public boolean discardLowerCard(DevCard.CardColor cardColor, Integer numberOfCardsToDiscard) {
        List<DevCard> devCards = new ArrayList<>();

        for (int i = 0; i < this.devCardMatrix.length && devCards.size() != numberOfCardsToDiscard; i++) {

            for (int j = 0; j < this.devCardMatrix[i].length && devCards.size() != numberOfCardsToDiscard; j++) {
                DevCard deletedDevCard = this.devCardMatrix[i][j].discardBottomCard();
                if (deletedDevCard != null)
                    devCards.add(deletedDevCard);
            }
        }
        if (devCards.size() == numberOfCardsToDiscard){
            // TODO Show Discarded card ?
            return true;
        }
        // TODO: Trigger the end of the game
        return false;
    }

    public boolean hasEmptySlot() {
        // TODO to check, i don't understand if a complete column  like devCardMatrix[i] vs   devCardMatrix[i][j]

        for (int i = 0; i < this.devCardMatrix.length; i++) {

            for (int j = 0; j < this.devCardMatrix[i].length; j++) {
                if (this.devCardMatrix[i][j].getStackLength() == 0) {
                    return true;
                }
            }
        }
        return false;

    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void triggerVaticanReport(PersonalBoard.PopeArea area) {
        if (!popeAreaMapTrigger.get(area)) {
            popeAreaMapTrigger.replace(area, true);
            this.controller.takeVaticanReports(area);
        }
    }

    public boolean IsEndTriggered(int userID) {
        boolean endByFaithPoints = (getPersonalBoard(userID).getFaithPoints() == 24);
        boolean endByDevCard = (getPersonalBoard(userID).getOwnedCards().size() == 7);
        if (soloMode && getPersonalBoard(userID).getBlackCrossToken() == 24 && hasEmptySlot()) {
            return true;
        }
        if (endByDevCard || endByFaithPoints) {
            return true;
        }
        return false;
    }

}
