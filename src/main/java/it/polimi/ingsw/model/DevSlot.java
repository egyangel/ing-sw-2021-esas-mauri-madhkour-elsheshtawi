package it.polimi.ingsw.model;

import it.polimi.ingsw.model.DevCard;

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
        return this.devCards.peek();
    }
    //public
}
