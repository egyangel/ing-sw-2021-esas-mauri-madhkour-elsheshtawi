package it.polimi.ingsw.model;

import java.util.*;
/**
 * Development Slot  , it is the class that handle the Development
 * slot on the personal board of the player
 * @author
 * */
public class DevSlot {
    public enum slotPlace{
        LEFT(0), CENTER(1), RIGHT(2);
        private int indexInBoard;
        public int getIndexInBoard(){
            return indexInBoard;
        }
        slotPlace(int index){
            this.indexInBoard = index;
        }
        public static slotPlace getByName(String input){
            for (slotPlace st : slotPlace.values()) {
                if (st.toString().equals(input)){
                    return st;
                }
            }
            return null;
        }
    }
    private Deque<DevCard> devCards;
    private final slotPlace place;

    public DevSlot(slotPlace place){
        this.place = place;
        devCards = new ArrayDeque<>();
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
        else return this.devCards.getLast();
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
        devCards.add(card);
    }

    public String describeDevSlot(){
        StringBuilder sb = new StringBuilder();
        sb.append(place.name() + " SLOT: ");
        Iterator<DevCard> iterator = devCards.descendingIterator();
        if (isEmpty()) {
            sb.append("Empty");
        } else {
            DevCard card = iterator.next();
            sb.append("Top Card: " + card.describeDevCard());
            while(iterator.hasNext()){
                card = iterator.next();
                sb.append("\n\t\t\tBelow Card: " + card.describeDevCard());
            }
        }
        return sb.toString();
    }
}
