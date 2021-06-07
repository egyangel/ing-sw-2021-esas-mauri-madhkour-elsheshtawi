package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivateProdActionContext {

    private boolean hasError;
    private List<LeaderCard> producerCard = new ArrayList<>();
    private List<Resources> leaderRHS= new ArrayList<>();
    private List<DevSlot>  slotChosen = new ArrayList<>();
    private List<DevSlot>  slotAvailable = new ArrayList<>();
    private List<DevCard>  selectedCard = new ArrayList<>();
    private DevCard  baseProductionCard;
    private Resources payment;
    private boolean warehouseSelectedForLeader = false;
    private boolean warehouseSelectedForDefault = false;
    private boolean warehouseSelectedForDevslots = false;
    private Boolean baseProdPower = false ;
    private int numberOfCardActivated;
    private ActionStep lastStep;
    private List<DevSlot.slotPlace> placeList = new ArrayList<>();
    //flag for leader card
    public enum ActionStep{
        // from client to server
        LEADER_CARD_CHOOSEN,
        DEV_SLOTS_CHOOSEN,
        LEADER_CARD_NOT_CHOOSEN,
        PAY_PRODUCTION_FROM_WHERE_CHOSEN,

        // from server to client

        CHECK_ACTIVE_LEADER_PRODOCTION,
        CHOOSE_DEV_SLOTS,
        EMPTY_DEV_SLOTS_ERROR,
        CHOOSE_PAY_COST_FOR_PRODUCTION_FROM_WHERE,
        NOT_ENOUGH_RES_FOR_PRODUCTION_IN_WAREHOUSE,
        NOT_ENOUGH_RES_FOR_PRODUCTION_IN_STRONGBOX,
        CHOOSE_LEADER_TO_PRODUCE,
        CHECK_RES_FROM_SHELF,
        COST_PAID
    }

    public ActionStep getLastStep(){
        return lastStep;
    }

    public void setLastStep(ActionStep step){
        lastStep = step;
    }

    public void setBaseProductionCard (DevCard baseProductionCard) {
        this.baseProductionCard = baseProductionCard;
    }
    public void setBaseProdPower(boolean answer){
        this.baseProdPower = answer;
    }
    public  boolean getBaseProdPower(){
        return this.baseProdPower ;
    }
    public DevCard getBaseProductionCard() { return baseProductionCard; }
    public void setFromWhereToPayForDefault(boolean warehouseSelectedForDefault){ this.warehouseSelectedForDefault = warehouseSelectedForDefault; }
    public boolean getFromWhereToPayForDefault(){
        return this.warehouseSelectedForDefault ;
    }

    public void setSlotAvailable(List<DevSlot> slotAvailabl){ this.slotAvailable.addAll(slotAvailabl); }
    public List<DevSlot> getSlotAvailable(){ return this.slotAvailable; }
    public void setSlots(List<DevSlot> slotChosen){ this.slotChosen.addAll(slotChosen); }
    public List<DevSlot> getSlots(){ return this.slotChosen;  }
    public void setFromWhereToPayForDevslots(boolean warehouseSelectedForDevslots){ this.warehouseSelectedForDevslots = warehouseSelectedForDevslots; }
    public boolean getFromWhereToPayForDevslots(){
        return this.warehouseSelectedForDevslots ;
    }
    public void setSelectedCard( List<DevCard> selectedCard) {
        this.selectedCard.addAll(selectedCard);
    }
    public List<DevCard> getSelectedCard() { return selectedCard; }


    public void setErrorTrue(){
        this.hasError = true;
    }

    //All Methods below handle the activation of leader Card Production
    public void setRhlLeaderCard(List<Resources> RHS) {
        this.leaderRHS.addAll(RHS);
    }
    public List<Resources> getRhlLeaderCard() {
        return leaderRHS;
    }
    public void setLeaderProd(List<LeaderCard> producerCard) { this.producerCard.addAll(producerCard); }
    public List<LeaderCard> getProducerCard() { return producerCard; }
    public int getNumberOfActiveLeaderProduction() { return numberOfCardActivated; }
    public void setNumberOfActiveLeaderProduction (int numberOfCardActivated) { this.numberOfCardActivated = numberOfCardActivated; }
    public void setFromWhereToPayForLeader(boolean warehouseSelected){ this.warehouseSelectedForLeader = warehouseSelected; }
    public boolean getFromWhereToPayForLeader(){ return this.warehouseSelectedForLeader ; }


}
