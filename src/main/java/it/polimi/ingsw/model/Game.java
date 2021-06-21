package it.polimi.ingsw.model;


import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.network.server.VirtualView;
import it.polimi.ingsw.utility.JsonConverter;
import it.polimi.ingsw.utility.messages.Listener;
import it.polimi.ingsw.utility.messages.MVEvent;
import it.polimi.ingsw.utility.messages.Publisher;

import java.util.*;

// MODEL CLASS
public class Game implements Publisher<MVEvent> {

    private int playersNumber;
    private List<Listener<MVEvent>> listenerList = new ArrayList<>();
    private Map<Integer, Player> userIDtoPlayers = new HashMap<>();
    private Map<Integer, PersonalBoard> userIDtoBoards = new HashMap<>();
    private Map<Integer, VirtualView> userIDtoVirtualView = new HashMap<>();
    private MarketTray market;
    private Resources resourceSupply;
    private List<LeaderCard> leaderCardList = new ArrayList<>();
    private DevCardDeck[][] devCardMatrix = new DevCardDeck[3][4];
    private Controller controller;
    private Map<PersonalBoard.PopeArea, Boolean> popeAreaMapTrigger = new HashMap<>();
    private boolean soloMode;

    public void addPlayer(Integer userID) {
        userIDtoPlayers.put(userID, new Player());
        this.playersNumber++;
    }

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
        // todo I changed solo mode cheking condition, it was (this.soloMode = userIDtoPlayers.size() > 1;)
        this.soloMode = userIDtoPlayers.size() == 1;
        for (Map.Entry<Integer, Player> entry : userIDtoPlayers.entrySet()) {
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
        DevCard card = devCardMatrix[level-1][color.ordinal()].peekTopCard();
        return card;
    }

    public void removeTopDevCard(DevCard.CardColor color, int level) {
        devCardMatrix[level-1][color.ordinal()].removeTopCard();
    }

    public void sendMarketAndDevCardMatrixTo(Integer userID) {
        MVEvent marketUpdate = createMarketTrayMVEvent();
        publish(userID, marketUpdate);
        MVEvent devCardMatrixUpdate = createDevCardMVEvent();
        publish(userID, devCardMatrixUpdate);
    }

    public MVEvent createMarketTrayMVEvent(){
        MVEvent marketUpdate = new MVEvent(MVEvent.EventType.MARKET_TRAY_UPDATE, market);
        return marketUpdate;
    }

    public MVEvent createDevCardMVEvent(){
        List<DevCard> topDevCards = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                topDevCards.add(devCardMatrix[i][j].peekTopCard());
            }
        }
        MVEvent devCardMatrixUpdate = new MVEvent(MVEvent.EventType.DEVCARD_MATRIX_UPDATE, topDevCards);
        return devCardMatrixUpdate;
    }

    public String describeDevCardMatrix() {
        //Todo should show only the first card of each row ?
        StringBuilder stringBuilder = new StringBuilder();
        int order = 1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                stringBuilder.append(order + ") " + devCardMatrix[i][j].peekTopCard().describeDevCard());
                stringBuilder.append("\n");
                order++;
            }
        }
        return stringBuilder.toString();
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

    public int getPlayersNumber() {
        return playersNumber;
    }

    public boolean discardLowerCard(DevCard.CardColor cardColor, Integer numberOfCardsToDiscard) {
        List<DevCard> devCards = new ArrayList<DevCard>();
        HashMap<Integer, Integer> devCardsIndexs = new HashMap<>();
        for (int i = 0; i < this.devCardMatrix.length || devCards.size() == numberOfCardsToDiscard; i++) {

            for (int j = 0; j < this.devCardMatrix.length || devCards.size() == numberOfCardsToDiscard; j++) {
                DevCard card = devCardMatrix[j][j].peekBottomCard();
                if (card.getColor() == cardColor) {
                    devCards.add(card);
                    devCardsIndexs.put(i, j);

                }
            }
        }
        if (devCards.size() == numberOfCardsToDiscard) {
            for (Integer i = 0; i < devCardsIndexs.size(); i++) {
                Integer j = devCardsIndexs.get(i);
                this.devCardMatrix[i][j].discardButtomCard();
            }
            return true;

        }
        return false;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void triggerVaticanReport(PersonalBoard.PopeArea area){
        if (!popeAreaMapTrigger.get(area)){
            popeAreaMapTrigger.replace(area, true);
            this.controller.takeVaticanReports(area);
        }
    }

    // DEBUG METHODS

    /*
    private List<Requirement> createRequirements(){
        return JsonConverter.deserializeRequirements();
    }

    public void printDevCardMatrix(){
        createDevCardDecks();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 4; j++) {
                System.out.println(devCardMatrix[i][j].peekTopCard());
            }
        }
    }

    public void printLeaderCards(){
        createLeaderCards();
        for(LeaderCard card: leaderCardList){
            System.out.println(card);
        }

    }

    public void printRequirements() {
        List<Requirement> reqList = createRequirements();
        for(Requirement req: reqList){
            System.out.println(req);
        }
    }

    public void printSpecialAbilities() {
        List<SpecialAbility> abiList = createSpecialAbilities();
        for(SpecialAbility abi: abiList){
            System.out.println(abi);
        }
    }

    private List<SpecialAbility> createSpecialAbilities() {
        return JsonConverter.deserializeSpecialAbilities();
    }
     */
}
