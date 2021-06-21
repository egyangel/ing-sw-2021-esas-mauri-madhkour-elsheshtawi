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

    public void addDevCard(DevCard devcard){
        // code to check devcard constraints...
        // instead of add(), I (omer) used push() to focus on the fact that it is a stack and not a list
        this.devCards.add(devcard);
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
        else return this.devCards.getFirst();
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
        devCards.addFirst(card); //same as push, level constraints are checked in controller in a previous step
    }

    public String describeDevSlot(){
        StringBuilder sb = new StringBuilder();
        sb.append(place.name() + " SLOT:  ");
        Iterator<DevCard> iterator = devCards.descendingIterator();
        if (isEmpty()) {
            sb.append("Empty");
        } else {
            DevCard card = iterator.next();
            sb.append("Top Card:   " + card.describeDevCard());
            while(iterator.hasNext()){
                card = iterator.next();
                sb.append("\n\t\t\tBelow Card: " + card.describeDevCard());
            }
        }
        return sb.toString();
    }

//    public static void main(String[] args){
//        DevSlot[] slots = new DevSlot[3];
//        slots[0] = new DevSlot(slotPlace.LEFT);
//        slots[1] = new DevSlot(slotPlace.CENTER);
//        slots[2] = new DevSlot(slotPlace.RIGHT);
//
//        Game game = new Game();
//        game.createDevCardDecks();
//        DevCard devCard1 = game.peekTopDevCard(DevCard.CardColor.BLUE, 1);
//        DevCard devCard2 = game.peekTopDevCard(DevCard.CardColor.BLUE, 2);
//        DevCard devCard3 = game.peekTopDevCard(DevCard.CardColor.BLUE, 3);
//        DevCard devCard11 = game.peekTopDevCard(DevCard.CardColor.GREEN, 1);
//        slots[0].addDevCard(devCard1);
//        slots[0].addDevCard(devCard2);
//        slots[0].addDevCard(devCard3);
//        slots[1].addDevCard(devCard1);
////        slots[0].addDevCard(devCard1);
//
//        StringBuilder sb = new StringBuilder();
//        for(int i=0; i<3; i++){
//            sb.append(slots[i].describeDevSlot());
//            sb.append("\n");
//        }
//        System.out.println(sb.toString());
//    }

}
