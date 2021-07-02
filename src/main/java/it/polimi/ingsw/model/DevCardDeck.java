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

    public int getStackLegnth(){
        return this.stackOfDevCards.size();
    }
    public void putCard(DevCard card) {
        stackOfDevCards.add(card);
    }

    public void shuffleDeck() {
        Collections.shuffle(stackOfDevCards);
    }

    public DevCard peekTopCard() {
        if (stackOfDevCards.isEmpty()) return null;
        else return stackOfDevCards.get(0);
    }
    //todo mohamed ask why this method
    // Mohamed>  To Delete the bottom card for soloMode
    public DevCard discardBottomCard() {
        if (stackOfDevCards.isEmpty()) return null;
        DevCard card = stackOfDevCards.get(stackOfDevCards.size() - 1);
        stackOfDevCards.remove(stackOfDevCards.size() - 1);
         return card;
    }

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
