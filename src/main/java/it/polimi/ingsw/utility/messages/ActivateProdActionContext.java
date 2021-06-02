package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivateProdActionContext {
    private boolean isRow;
    private int index;
    private boolean hasError;
    private List<LeaderCard> producerCard;
    private Resources resources;
    private List<Resources> remainingCost;
    private List<DevSlot>  slotChosen;
    private List<DevCard>  selectedCard;
    private Resources payment;
    private Map<Shelf.shelfPlace, Resources.ResType> shelfPlaceResTypeMap = new HashMap<>();
    private Map<Shelf.shelfPlace, Boolean> shelfToResultMap = new HashMap<>();

    public enum ActionStep{
        // from client to server
        DEV_SLOTS_CHOOSEN,


        // from server to client
        CHOOSE_DEV_SLOTS,
        EMPTY_DEV_SLOTS_ERROR,
        NOT_ENOUGH_RES,
        CHOOSE_LEADER_TO_PRODUCE,
        CHECK_RES_FROM_SHELF,
        COST_PAID
    }
    private ActionStep lastStep;
    private List<DevSlot.slotPlace> placeList = new ArrayList<>();
    public void setIndexes(List<DevSlot.slotPlace> placeList){
        this.placeList.addAll(placeList);
    }

    public ActionStep getLastStep(){
        return lastStep;
    }

    public void setLastStep(ActionStep step){
        lastStep = step;
    }

   public void setSlots(List<DevSlot> slotChosen){
        this.slotChosen = slotChosen;
    }
    public List<DevSlot> getSlots(){
        return this.slotChosen;
    }

    public void setErrorTrue(){
        this.hasError = true;
    }
    public List<DevCard> getSelectedCard() {
        return selectedCard;
    }
    public void setSelectedCard( List<DevCard> selectedCard) {
        this.selectedCard.addAll(selectedCard);
    }
    public void setRemainingCost(List<Resources> costOfCard) {
               this.remainingCost.addAll(costOfCard);
    }
    public Resources getPayFromWarehouse() {
        return payment;
    }

    public void setPayFromWarehouse(Resources payFromWarehouse) {
        // not sure directly assigning will work for JSON, but it should
        this.payment = payFromWarehouse;
    }
/*
    public void setIndex(int index){
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public int getWhiteMarbleNumber() {
        return whiteMarbleNumber;
    }

    public void setWhiteMarbleNumber(int whiteMarbleNumber) {
        this.whiteMarbleNumber = whiteMarbleNumber;
    }

    public List<LeaderCard> getWhiteConverters() {
        return whiteConverters;
    }

    public void setWhiteConverters(List<LeaderCard> whiteConverters) {
        this.whiteConverters = new ArrayList<>();
        this.whiteConverters.addAll(whiteConverters);
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = new Resources();
        this.resources.add(resources);
    }

    public void addOneConvertedRes(Resources.ResType resType){
        this.resources.add(resType,1);
    }

    public void convertResIntoFaith(){
        this.faithPoints = this.resources.getNumberOfType(Resources.ResType.FAITH);
        this.resources.subtract(Resources.ResType.FAITH, faithPoints);
    }

    public void addDiscardedRes(int number){
        this.discardedRes += number;
    }

    public Shelf.shelfPlace getShelf() {
        return place;
    }

    public void setShelf(Shelf.shelfPlace place) {
        this.place = place;
    }

    public Shelf.shelfPlace[] getShelves() {
        return places;
    }

    public void setShelves(Shelf.shelfPlace firstPlace, Shelf.shelfPlace secondPlace) {
        this.places[0] = firstPlace;
        this.places[1] = secondPlace;
    }

    public void setShelftoResTypeMap(Map<Shelf.shelfPlace, Resources.ResType> map){
        for (Map.Entry<Shelf.shelfPlace, Resources.ResType> entry : map.entrySet()) {
            this.shelfPlaceResTypeMap.put(entry.getKey(), entry.getValue());
        }
    }

    public Map<Shelf.shelfPlace, Resources.ResType> getShelfPlaceResTypeMap() {
        return shelfPlaceResTypeMap;
    }

    public void setPutResultMap(Map<Shelf.shelfPlace, Boolean> map){
        for (Map.Entry<Shelf.shelfPlace, Boolean> entry : map.entrySet()) {
            this.shelfToResultMap.put(entry.getKey(), entry.getValue());
        }
    }

    public void removeResourcesPutToShelf(){
        for (Map.Entry<Shelf.shelfPlace, Boolean> entry : shelfToResultMap.entrySet()) {
            if (entry.getValue()){
                Resources.ResType resType = shelfPlaceResTypeMap.get(entry.getValue());
                this.resources.removeThisType(resType);
            }
        }
    }
*/
}
