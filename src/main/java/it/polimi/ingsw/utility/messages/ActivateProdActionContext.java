package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.List;


public class ActivateProdActionContext {
    public enum ActionStep{
        // from client to server
        DEV_SLOTS_CHOOSEN,
        PAY_PRODUCTION_FROM_WHERE_CHOSEN,
        // from server to client
        CHOOSE_DEV_SLOTS,
        CHOOSE_PRODUCTION_COST_FROM_WHERE,
        NOT_ENOUGH_RES_FOR_PRODUCTION,

        COST_PAID
    }
    private int numberOfCardActivated = 0;
    private Resources leaderRHS=  new Resources();
    private List<DevSlot>  slotChosen = new ArrayList<>();
    private List<DevSlot>  slotAvailable = new ArrayList<>();
    private List<DevCard>  selectedCard = new ArrayList<>();
    private DevCard  baseProductionCard;

    private Resources leaderLHS = new Resources();
    private Resources defautProdLHS = new Resources();
    private Resources devProdLHS = new Resources();
    private Resources totalRightCost = new Resources();
    private boolean warehouseSelectedForLeader = false;
    private boolean warehouseSelectedForDefault = false;
    private boolean warehouseSelectedForDevslots = false;
    private Boolean baseProdPower = false ;
    private Boolean activationLeaderCardProduction= false;

    private ActionStep lastStep;

    public ActionStep getLastStep(){
        return lastStep;
    }

    public void setLastStep(ActionStep step){
        lastStep = step;
    }

    public void setBaseProductionCard (DevCard baseProductionCard) {
        this.baseProductionCard = baseProductionCard;
    }
    public DevCard getBaseProductionCard() { return baseProductionCard;}
    public void setBaseProdPower(boolean answer){
        this.baseProdPower = answer;
    }
    public  boolean getBaseProdPower(){
        return this.baseProdPower ;
    }
    public void setFromWhereToPayForDefault(boolean warehouseSelectedForDefault){ this.warehouseSelectedForDefault = warehouseSelectedForDefault; }
    public boolean getFromWhereToPayForDefault(){
        return this.warehouseSelectedForDefault ;
    }


    public void setSlotAvailable(List<DevSlot> slotAvailable){ this.slotAvailable.addAll(slotAvailable); }
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

    public void setFromWhereToPayForLeader(boolean warehouseSelected){ this.warehouseSelectedForLeader = warehouseSelected; }
    public boolean getFromWhereToPayForLeader(){ return this.warehouseSelectedForLeader ; }

    public void setNumberOfActiveLeaderProduction (int numberOfCardActivated) { this.numberOfCardActivated = numberOfCardActivated; }
    public int getNumberOfActiveLeaderProduction() { return numberOfCardActivated; }

    public void setActivationLeaderCardProduction(boolean activationLeaderCardAfter){ this.activationLeaderCardProduction = activationLeaderCardAfter; }
    public boolean getActivationLeaderCardProduction(){
        return this.activationLeaderCardProduction ;
    }


    public void resetActivationProduction() {
        this.slotChosen= null;
        this.leaderRHS.clear();
        this.devProdLHS.clear();
        this.selectedCard = null;
        this.defautProdLHS.clear();
        this.baseProdPower = false;
        this.totalRightCost.clear();
        this.baseProductionCard = null;
        this.warehouseSelectedForLeader = false ;
        this.warehouseSelectedForDefault = false;
        this.warehouseSelectedForDevslots = false ;

    }


    public void setRhlLeaderCard(Resources RHS) {
        this.leaderRHS.add(RHS);
    }
    public Resources getRhlLeaderCard() {
        return leaderRHS;
    }

    public void setLhlLeaderCard(Resources LHS) { this.leaderLHS.add(LHS); }
    public Resources getLhlLeaderCard() { return leaderLHS; }

    public void setTotalRightCost(Resources totalRightCost) { this.totalRightCost.add(totalRightCost);  }
    public Resources getTotalRightCost() {
        return this.totalRightCost;
    }
    public void resetTotalRightCost() {
        this.totalRightCost.clear();
    }




}
