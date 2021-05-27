package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.model.LeaderCard;
import it.polimi.ingsw.model.Resources;
import it.polimi.ingsw.model.Shelf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TakeResActionContext {
    public enum ActionStep{
        // from client to server
        ROW_COLUMN_CHOSEN,
        RES_FROM_WHITE_ADDED_TO_CONTEXT,
        CLEAR_SHELF_CHOSEN,
        SWAP_SHELVES_CHOSEN,
        PUT_RESOURCES_CHOSEN,
        END_TAKE_RES_ACTION_CHOSEN,
        // from server to client
        CHOOSE_ROW_COLUMN,
        CHOOSE_LEADER_TO_CONVERT_WHITE,
        CHOOSE_SHELVES
    }
    private ActionStep lastStep;
    private boolean isRow;
    private int index;
    private boolean hasError;
    private int whiteMarbleNumber;
    private List<LeaderCard> whiteConverters;
    private Resources resources;
    private int discardedRes = 0;
    private int faithPoints;
    private Shelf.shelfPlace place;
    private Shelf.shelfPlace[] places;
    private Map<Shelf.shelfPlace, Resources.ResType> shelfPlaceResTypeMap = new HashMap<>();
    private Map<Shelf.shelfPlace, Boolean> shelfToResultMap = new HashMap<>();

    public ActionStep getLastStep(){
        return lastStep;
    }

    public void setLastStep(ActionStep step){
        lastStep = step;
    }

    public void chooseRow(boolean isRow){
        this.isRow = isRow;
    }

    public boolean isRow(){
        return isRow;
    }

    public void setErrorTrue(){
        this.hasError = true;
    }

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
}
