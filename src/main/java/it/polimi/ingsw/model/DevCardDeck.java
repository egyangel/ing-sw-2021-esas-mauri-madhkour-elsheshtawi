package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * Development Deck card class , it is the class that handle the Development deck
 *
 * */
public class DevCardDeck {
    private DevCard.CardColor color;
    private int level;
    private List<DevCard> stackOfDevCards = new ArrayList<>();

    /**
     * Constructor of the deck it set the color of that deck and the level
     * */
    public DevCardDeck(DevCard.CardColor color, int level) {
        this.color = color;
        this.level = level;
    }
    public int getStackLength(){
        return this.stackOfDevCards.size();
    }

    public void putCard(DevCard card) {
        stackOfDevCards.add(card);
    }

    /**
     * method that mixes the cards in the deck before the game starts
     * so that each game will start with different cards
     * */
    public void shuffleDeck() {
        Collections.shuffle(stackOfDevCards);
    }

    /**
     * method that returns but not removes top card in the deck
     * top is represented as the head of the list
     * @return top DevCard in the deck
     * */
    public DevCard peekTopCard() {
        if (stackOfDevCards.isEmpty()) return null;
        else return stackOfDevCards.get(0);
    }

    public DevCard discardBottomCard() {
        if (stackOfDevCards.isEmpty()) return null;
        DevCard card = stackOfDevCards.get(stackOfDevCards.size() - 1);
        stackOfDevCards.remove(stackOfDevCards.size() - 1);
         return card;
    }
    /**
     * method that returns and removes top card in the deck
     * top is represented as the head of the list
     * cards in the list move by one as a result
     * @return top DevCard in the deck
     * */
    public void removeTopCard(){
        stackOfDevCards.remove(0);
    }

    public DevCard.CardColor getColor() {
        return color;
    }

    public int getLevel() {
        return level;
    }

}
