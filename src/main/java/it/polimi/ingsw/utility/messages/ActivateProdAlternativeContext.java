package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<LeaderCard, Resources.ResType> addProdLeaderResMap = new HashMap<>();
    private List<DevCard> selectedDevCards = new ArrayList<>();
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

    public void setError(boolean error){
        this.error = error;
    }

    public boolean isError() {
        return error;
    }

    public boolean getAddProdOptionAvailable() {
        return addProdOptionAvailable;
    }

    public void setAddProdOptionAvailable(boolean addProdOption) {
        this.addProdOptionAvailable = addProdOption;
    }


    public void setAddProdLeaders(List<LeaderCard> addProdLeaders) {
        for(LeaderCard card: addProdLeaders){
            addProdLeaderResMap.put(card, null);
        }
    }

    public List<LeaderCard> getAddProdLeaderList(){
        return new ArrayList<>(addProdLeaderResMap.keySet());
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

    public void addLeaderToRes(LeaderCard card, Resources.ResType type){
        this.addProdLeaderResMap.replace(card, type);
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

    public Map<LeaderCard, Resources.ResType> getAddProdLeaderResMap() {
        return addProdLeaderResMap;
    }
}
