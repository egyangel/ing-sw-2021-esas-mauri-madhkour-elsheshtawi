package it.polimi.ingsw.model;


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
    private SoloActionToken[] soloActionToken = new SoloActionToken[6];
    private Resources resourceSupply;
    private List<LeaderCard> leaderCardList = new ArrayList<>();
    private DevCardDeck[][] devCardMatrix = new DevCardDeck[3][4];
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
    }

    private void createBoardForEachPlayer() {
        this.soloMode = userIDtoPlayers.size() > 1;
        for (Map.Entry<Integer, Player> entry : userIDtoPlayers.entrySet()) {
            userIDtoBoards.put(entry.getKey(), new PersonalBoard(entry.getKey(), soloMode));
        }
    }

    private void createDevCardDecks() {
        List<DevCard> allDevCards = JsonConverter.deserializeDevCards();
        Iterator<DevCard> cardIterator = allDevCards.iterator();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
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
        DevCard card = devCardMatrix[color.ordinal()][level].peekTopCard();
        return card;
    }

    public void removeTopDevCard(DevCard.CardColor color, int level) {
        devCardMatrix[color.ordinal()][level].removeTopCard();
    }

    public void sendMarketAndDevCardMatrixTo(Integer userID) {
//        String marketTrayString = market.describeMarketTray();
//        MVEvent marketUpdate = new MVEvent(MVEvent.EventType.MARKET_TRAY_UPDATE, marketTrayString);
        MVEvent marketUpdate = new MVEvent(MVEvent.EventType.MARKET_TRAY_UPDATE, market);
        publish(userID, marketUpdate);
        String devCardMatrixString = describeDevCardMatrix();
        MVEvent devCardMatrixUpdate = new MVEvent(MVEvent.EventType.DEVCARD_MATRIX_UPDATE, devCardMatrixString);
        publish(userID, devCardMatrixUpdate);
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


    /**
     * Init soloAction tokens
     */
    public void initSoloActionTokens() {

    }

    /**
     * shuffle and place solo action token
     */

    public void setSoloActionToken() {
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
