package it.polimi.ingsw.model;


import it.polimi.ingsw.utility.JsonConverter;

import java.util.*;

// MODEL CLASS
public class Game {

    private Map<Integer,Player> userIDtoPlayers = new HashMap<>();
    private Map<Integer,PersonalBoard> userIDtoBoards = new HashMap<>();
    private MarketTray market;
    private Resources resourceSupply;
    private List<LeaderCard> leaderCardList = new ArrayList<>();
    private DevCardDeck[][] devCardMatrix = new DevCardDeck[3][4];

    public void addPlayer(Integer userID) {
        userIDtoPlayers.put(userID, new Player());
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

    private List<Requirement> createRequirements(){
        return JsonConverter.deserializeRequirements();
    }

    // DEBUG METHODS
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
            System.out.println(card.getRequirement());
        }

    }

    public void printRequirements() {
        List<Requirement> reqList = createRequirements();
        for(Requirement req: reqList){
            System.out.println(req);
        }
    }
}
