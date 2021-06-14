package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.model.*;
import java.util.ArrayList;
import java.util.List;

public class LeaderActionContext {

    public enum ActionStep{
        // from client to server
        LEADER_CARD_ACTIVATED_CHOOSEN,
        LEADER_CARD_NOT_ACTIVATED_CHOOSEN,
        DISCARD_LEADER_CARD,

        //inside client
        CHOOSE_ACTION,
        // from server to client
        POWER_ACTIVATED
    }
    private List<LeaderCard> playerCard = new ArrayList<>();
    private List<LeaderCard> activeLeaderCard = new ArrayList<>();
    private Boolean activationLeaderCardBefore= false;
    private Boolean activationLeaderCard= false;

    private Resources totalResources= new Resources() ;
    private boolean warehouseSelectedForLeader = false;
    private int numberOfCardActivated;
    private int numOfDiscardCard;
    private ActionStep lastStep;
    private List<DevSlot.slotPlace> placeList = new ArrayList<>();

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

    public int getNumberOfDiscardLeader() { return numOfDiscardCard; }
    public void setNumberOfDiscardLeader(int numOfDiscardCard) { this.numOfDiscardCard = numOfDiscardCard; }


    public void setActivationLeaderCardBefore(boolean activationLeaderCardBefore){ this.activationLeaderCardBefore = activationLeaderCardBefore; }
    public boolean getActivationLeaderCardBefore(){
        return this.activationLeaderCardBefore ;
    }
    public void resetActivationLeaderCardBefore(){ this.activationLeaderCardBefore   = false ; }


    //All Methods below handle the activation of leader Card Production
    public void setActivationLeaderCard(boolean activationLeaderCard){ this.activationLeaderCard = activationLeaderCard; }
    public boolean getActivationLeaderCard(){
        return this.activationLeaderCard ;
    }
    public void resetActivationLeaderCard(){ this.activationLeaderCard   = false ; }


   // public void setLeaderProd(List<LeaderCard> producerCard) { this.producerCard.addAll(producerCard); }


    public int getNumberOfActiveLeader() { return numberOfCardActivated; }
    public void setNumberOfActiveLeader (int numberOfCardActivated) { this.numberOfCardActivated = numberOfCardActivated; }


    public void setTotalResources(Resources totalResources) {
        this.totalResources = totalResources;
    }
    public Resources getTotalResources() {
        return this.totalResources;
    }


}