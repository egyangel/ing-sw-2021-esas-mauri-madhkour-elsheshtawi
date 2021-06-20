package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.model.*;
import java.util.ArrayList;
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
    private List<LeaderCard> playerCard = new ArrayList<>();
    private List<LeaderCard> discardedCard = new ArrayList<>();
    private List<LeaderCard> activeLeaderCard = new ArrayList<>();
    private Boolean activationLeaderCardBefore= false;
    private Resources totalResources= new Resources() ;
    private ActionStep lastStep;
    private List<DevCard> ownedCards;

    public void setLastStep(ActionStep step){
        lastStep = step;
    }
    public ActionStep getLastStep(){
        return lastStep;
    }

    public void setPlayerCard(List<LeaderCard> playerCard){
        this.playerCard.addAll(playerCard);
    }
    public List<LeaderCard> getPlayerCard(){ return this.playerCard; }

    public void setDiscardedPlayerCard(List<LeaderCard> discardedCard){
        this.discardedCard.addAll(discardedCard);
    }
    public List<LeaderCard> discardedPlayerCard(){ return this.discardedCard; }

    public void changePlayerCard(List<LeaderCard> discardCard){
        int i = 0;
        while(i < discardCard.size()){
            playerCard.remove(this.playerCard.indexOf(discardCard.get(i)));
            i++;
        }
    }

    public void setActiveLeaderCard(List<LeaderCard> activeLeaderCard){
        this.activeLeaderCard.addAll(activeLeaderCard);
    }
    public List<LeaderCard> getActiveLeaderCard(){ return this.activeLeaderCard; }

    public void setActivationLeaderCardBefore(boolean activationLeaderCardBefore){ this.activationLeaderCardBefore = activationLeaderCardBefore; }
    public boolean getActivationLeaderCardBefore(){
        return this.activationLeaderCardBefore ;
    }
    public void resetActivationLeaderCardBefore(){ this.activationLeaderCardBefore   = false ; }

    public void setOwnedCard( List<DevCard> myCards) {
        ownedCards = new ArrayList<>(myCards);
    }
    public  List<DevCard> getOwnedCard() {
        return this.ownedCards;
    }
   // public void setLeaderProd(List<LeaderCard> producerCard) { this.producerCard.addAll(producerCard); }

    public void setTotalResources(Resources totalResources) {
        this.totalResources = totalResources;
    }
    public Resources getTotalResources() {
        return this.totalResources;
    }


}