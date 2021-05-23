package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DevCardDeck {
    private DevCard.CardColor color;
    private int level;
    private List<DevCard> stackOfDevCards = new ArrayList<>();

    public DevCardDeck(DevCard.CardColor color, int level) {
        this.color = color;
        this.level = level;
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
