package it.polimi.ingsw.model;

import it.polimi.ingsw.model.DevCard;

import java.util.Deque;
import java.util.Stack;

public class DevSlot {
    public enum slotPlace{
        LEFT, CENTER, RIGHT
    }
    private Stack<DevCard> devCards;
    private final slotPlace place;

    public DevSlot(slotPlace place){
        this.place = place;
        devCards = new Stack<>();
    }

    public void addDevCard(DevCard devcard){
        // code to check devcard constraints...
        // instead of add(), I (omer) used push() to focus on the fact that it is a stack and not a list
        this.devCards.push(devcard);
    }

    public boolean isEmpty(){
        return this.devCards.isEmpty();
    }

    public boolean isFull(){
        return (this.devCards.size() == 3);
    }

    public void clear(){
        this.devCards.clear();
    }

    public DevCard getTopDevCard(){
        if (this.devCards.isEmpty()) return null;
        else return this.devCards.peek();
    }

    public int getLevelOfTopCard(){
        DevCard devCard = getTopDevCard();
        if (devCard == null) return 0;
        else return devCard.getLevel() ;
    }

    public DevSlot.slotPlace getPlace(){
        return this.place;
    }

    public void putDevCard(DevCard card) {
        devCards.push(card);
    }

}
