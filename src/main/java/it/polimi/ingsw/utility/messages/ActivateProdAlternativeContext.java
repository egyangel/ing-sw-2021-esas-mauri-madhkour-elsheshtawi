package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * class that acts as a form between server and client for the activation of production action
 * Between each step, server and client stores related headers (fields) of the class
 * Server reads related fields and checks, and modifies model accordingly, then fills more headers and sends
 * it back to client if necessary. Client reads this class, shows the information to user, and fill the related fields
 * with user's choices before sending it back
 * */
public class ActivateProdAlternativeContext {
    public enum ActionStep{
        // from client to server
        DEVLSLOTS_CHOOSEN_FOR_PROD,
        // from server to client
        CHOOSE_DEV_SLOTS_FOR_PROD,
        PRODUCTION_DONE,
        NOT_ENOUGH_RES_ON_PERSONAL_BOARD
    }
    private ActivateProdAlternativeContext.ActionStep lastStep;
    private Map<DevSlot.slotPlace, DevCard> slotToCardMap = new HashMap<>();
    private List<DevCard> selectedDevCards = new ArrayList<>();
    private List<LeaderCard> addProdLeaderList = new ArrayList<>();
    private Resources leaderCosts = null;
    private Resources leaderProds = null;
    private boolean addProdOptionAvailable = false;
    private boolean addProdOptionSelected = false;
    private boolean basicProdOptionSelected = false;
    private Resources basicProdLHS;
    private Resources basicProdRHS;
    private boolean error = false;

    public ActivateProdAlternativeContext.ActionStep getLastStep(){
        return lastStep;
    }
    public void setLastStep(ActivateProdAlternativeContext.ActionStep step){
        lastStep = step;
    }
    public void setSlotMap(Map<DevSlot.slotPlace, DevCard> map){
        for (Map.Entry<DevSlot.slotPlace, DevCard> entry : map.entrySet()) {
            this.slotToCardMap.put(entry.getKey(), entry.getValue());
        }
    }

    public Map<DevSlot.slotPlace, DevCard> getSlotMap(){
        return slotToCardMap;
    }


    public boolean getAddProdOptionAvailable() {
        return addProdOptionAvailable;
    }

    public void setAddProdOptionAvailable(boolean addProdOption) {
        this.addProdOptionAvailable = addProdOption;
    }


    public void setAddProdLeaders(List<LeaderCard> addProdLeaders) {
        this.addProdLeaderList = addProdLeaders;
    }

    public List<LeaderCard> getAddProdLeaderList(){
        return this.addProdLeaderList;
    }

    public boolean getBasicProdOptionSelected() {
        return basicProdOptionSelected;
    }

    public void setBasicProdOptionSelected(boolean basicProdOption) {
        this.basicProdOptionSelected = basicProdOption;
    }

    public Resources getBasicProdLHS() {
        return basicProdLHS;
    }

    public void setBasicProdLHS(Resources basicProdLHS) {
        this.basicProdLHS = basicProdLHS;
    }

    public Resources getBasicProdRHS() {
        return basicProdRHS;
    }

    public void setBasicProdRHS(Resources basicProdRHS) {
        this.basicProdRHS = basicProdRHS;
    }

    public void setSelectedDevCards(List<DevCard> selectedCards){
        selectedDevCards.addAll(selectedCards);
    }

    public List<DevCard> getSelectedDevCards(){
        return selectedDevCards;
    }

    public boolean getAddProdOptionSelected() {
        return addProdOptionSelected;
    }

    public void setAddProdOptionSelected(boolean addProdOptionSelected) {
        this.addProdOptionSelected = addProdOptionSelected;
    }

    public void addLeaderCost(Resources.ResType resType){
        if(this.leaderCosts == null) {
            this.leaderCosts = new Resources();
        }
        this.leaderCosts.add(resType,1);
    }

    public void addLeaderProd(Resources.ResType resType){
        if(this.leaderProds == null) {
            this.leaderProds = new Resources();
        }
        this.leaderProds.add(resType,1);
        this.leaderProds.add(Resources.ResType.FAITH,1);
    }

    public Resources getLeaderCosts() {
        return leaderCosts;
    }

    public Resources getLeaderProds() {
        return leaderProds;
    }
}
