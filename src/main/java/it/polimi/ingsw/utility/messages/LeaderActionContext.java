package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.model.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LeaderActionContext {

    public enum ActionStep{
        // from client to server
        LEADER_CARD_ACTIVATED_CHOOSEN,
        LEADER_CARD_NOT_ACTIVATED_CHOOSEN,
        DISCARD_LEADER_CARD,
        BOTH_ACTIONS,

        // from server to client
        CHOOSE_ACTION,
        END_LEADER_ACTION
    }
    private Set<LeaderCard> playerCard = new HashSet<>();
    private Set<LeaderCard> discardedCard = new HashSet<>();
    private Set<LeaderCard> activeLeaderCard = new HashSet<>();
    private Boolean activationLeaderCardBefore= false;
    private Resources totalResources= new Resources() ;
    private ActionStep lastStep;
    private Set<DevCard> ownedCards= new HashSet<>();

    public void setLastStep(ActionStep step){
        lastStep = step;
    }
    public ActionStep getLastStep(){
        return lastStep;
    }

    public void setPlayerCard(Set<LeaderCard> playerCard){
        this.playerCard.addAll(playerCard);
    }
    public List<LeaderCard> getPlayerCard(){  return new ArrayList<>(this.playerCard); }

    public void setDiscardedPlayerCard(Set<LeaderCard> discardedCard){
        this.discardedCard.addAll(discardedCard);
    }
    public List<LeaderCard> discardedPlayerCard(){ return new ArrayList<>(this.discardedCard); }

    public void changePlayerCard(Set<LeaderCard> discardCard){
        int i = 0;
        while(i < discardCard.size()){
            playerCard.removeAll(discardCard);
            i++;
        }
    }

    public void setActiveLeaderCard(Set<LeaderCard> activeLeaderCard){
        this.activeLeaderCard.addAll(activeLeaderCard);
    }

    public List<DevCard> getOwnedCards() {
        return  new ArrayList<>(ownedCards);
    }

    public List<LeaderCard> getActiveLeaderCard(){ return new ArrayList<>(this.activeLeaderCard); }

    public void setActivationLeaderCardBefore(boolean activationLeaderCardBefore){ this.activationLeaderCardBefore = activationLeaderCardBefore; }
    public boolean getActivationLeaderCardBefore(){
        return this.activationLeaderCardBefore ;
    }
    public void resetActivationLeaderCardBefore(){ this.activationLeaderCardBefore   = false ; }

    public void setOwnedCard( List<DevCard> myCards) {
        ownedCards.addAll(myCards);
    }
    public  List<DevCard> getOwnedCard() {
        return new ArrayList<>(this.ownedCards);
    }
   // public void setLeaderProd(List<LeaderCard> producerCard) { this.producerCard.addAll(producerCard); }

    public void setTotalResources(Resources totalResources) {
        this.totalResources = totalResources;
    }
    public Resources getTotalResources() {
        return this.totalResources;
    }


}