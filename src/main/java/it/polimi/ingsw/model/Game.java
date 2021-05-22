package it.polimi.ingsw.model;


import it.polimi.ingsw.network.server.VirtualView;
import it.polimi.ingsw.utility.JsonConverter;
import it.polimi.ingsw.utility.messages.Listener;
import it.polimi.ingsw.utility.messages.MVEvent;
import it.polimi.ingsw.utility.messages.Publisher;
import it.polimi.ingsw.utility.messages.VCEvent;

import java.util.*;

// MODEL CLASS
public class Game implements Publisher<MVEvent> {

    private int playersNumber;
    private List<Listener<MVEvent>> listenerList = new ArrayList<>();
    private Map<Integer,Player> userIDtoPlayers = new HashMap<>();
    private Map<Integer,PersonalBoard> userIDtoBoards = new HashMap<>();
    private Map<Integer,VirtualView> userIDtoVirtualView = new HashMap<>();
    private MarketTray market;
    private Resources resourceSupply;
    private List<LeaderCard> leaderCardList = new ArrayList<>();
    private DevCardDeck[][] devCardMatrix = new DevCardDeck[3][4];

    public void addPlayer(Integer userID) {
        userIDtoPlayers.put(userID, new Player());
        this.playersNumber++;
    }

    public void createGameObjects(){
        createBoardForEachPlayer();
        createDevCardDecks();
        createMarketTray();
        createLeaderCards();
    }

    private void createBoardForEachPlayer(){
        for(Map.Entry<Integer, Player> entry: userIDtoPlayers.entrySet()){
            userIDtoBoards.put(entry.getKey(), new PersonalBoard(entry.getKey()));
        }
    }

    private void createDevCardDecks(){
        List<DevCard> allDevCards = JsonConverter.deserializeDevCards();
        Iterator<DevCard> cardIterator = allDevCards.iterator();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 4; j++){
                DevCardDeck deck = new DevCardDeck();
                for(int k = 0; k < 4; k++){
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

    public PersonalBoard getPersonalBoard(Integer userID){
        return userIDtoBoards.get(userID);
    }

    public MarketTray getMarketTray(){
        return market;
    }

    public void shuffleLeaderCards() {
        Collections.shuffle(leaderCardList);
    }

    public List<LeaderCard> getFourLeaderCard(int counterOfCalls){
        List<LeaderCard> list = new ArrayList<>();
        list.addAll(leaderCardList.subList(counterOfCalls * 4, (counterOfCalls * 4) + 4));
        return list;
    }

    public List<Resources.ResType> getWhiteMarbleConverters(Integer userID){
        PersonalBoard personalBoard = userIDtoBoards.get(userID);
        List<LeaderCard> leaderList = new ArrayList<>();
        List<Resources.ResType> resTypeList = new ArrayList<>();
        leaderList.addAll(personalBoard.getActiveLeaderCards());
        for(LeaderCard card: leaderList){
            if (card.getAbility().getAbilityType() == SpecialAbility.AbilityType.CONVERTWHITE){
                resTypeList.add(card.getAbility().getResType());
            }
        }
        return resTypeList;
    }

    public void sendMarketAndDevCardMatrixTo(Integer userID){
        String marketTrayString = market.describeMarketTray();
        MVEvent marketUpdate = new MVEvent(MVEvent.EventType.MOST_RECENT_MARKETTRAY_SENT, marketTrayString);
        publish(userID, marketUpdate);
        String devCardMatrixString = describeDevCardMatrix();
        MVEvent devCardMatrixUpdate = new MVEvent(MVEvent.EventType.MOST_RECENT_DEVCARDMATRIX_SENT, devCardMatrixString);
        publish(userID, devCardMatrixUpdate);
    }

    //TODO FOR AMOR: return a single string that consists of top devcards in the 3x4 matrix
    // it would be best if 3x4 view of the matrix can be preserved
    private String describeDevCardMatrix(){
        return null;
    }

    @Override
    public void subscribe(Listener<MVEvent> listener) {
        listenerList.add(listener);
    }

    public void subscribe(Integer userID, VirtualView virtualView){
        userIDtoVirtualView.put(userID, virtualView);
    }

    public void publish(Integer userID, MVEvent event){
        userIDtoVirtualView.get(userID).update(event);
    }

    @Override
    public void unsubscribe(Listener<MVEvent> listener) {
        listenerList.remove(listener);
    }

    @Override
    public void publish(MVEvent event) {
        for(Listener<MVEvent> listener : listenerList){
            listener.update(event);
        }
    }

    public int getPlayersNumber() {
        return playersNumber;
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
