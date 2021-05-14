package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DevCardDeck {
     private List<DevCard> stackOfDevCards = new ArrayList<>();

     public DevCardDeck(){
     }

     public void putCard(DevCard card){
         stackOfDevCards.add(card);
     }

     public void shuffleDeck(){
         Collections.shuffle(stackOfDevCards);
     }

     public DevCard peekTopCard(){
         return stackOfDevCards.get(0);
     }


}
