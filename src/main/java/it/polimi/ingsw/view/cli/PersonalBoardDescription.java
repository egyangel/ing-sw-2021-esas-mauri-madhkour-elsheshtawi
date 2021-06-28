package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.PersonalBoard;

import java.util.HashMap;
import java.util.Map;

public class PersonalBoardDescription {
    private String warehouseDescription;
    private String strongboxDescription;
    private String devSlotsDescription;
    private String faithTrackDescription;
    private String activeLeaderCardsDescription;
    private String inactiveLeaderCardsDescription;
    private int faithPoints;
    private Map<PersonalBoard.PopeArea, Boolean> tileMap = new HashMap<>();

    public String getWarehouseDescription() {
        return warehouseDescription;
    }

    public void setWarehouseDescription(String warehouseDescription) {
        this.warehouseDescription = warehouseDescription;
    }

    public String getStrongboxDescription() {
        return strongboxDescription;
    }

    public void setStrongboxDescription(String strongboxDescription) {
        this.strongboxDescription = strongboxDescription;
    }

    public String getDevSlotsDescription() {
        return devSlotsDescription;
    }

    public void setDevSlotsDescription(String devSlotsDescription) {
        this.devSlotsDescription = devSlotsDescription;
    }

    public String getFaithTrackDescription() {
        return faithTrackDescription;
    }

    public void setFaithTrackDescription(String faithTrackDescription) {
        this.faithTrackDescription = faithTrackDescription;
    }

    public int getFaithPoints() {
        return faithPoints;
    }

    public void setFaithPoints(int faithPoints) {
        this.faithPoints = faithPoints;
    }

    public Map<PersonalBoard.PopeArea, Boolean> getTileMap() {
        return tileMap;
    }

    public void setTileMap(Map<PersonalBoard.PopeArea, Boolean> tileMap) {
        this.tileMap = tileMap;
    }

    public String getActiveLeaderCardsDescription() {
        return activeLeaderCardsDescription;
    }

    public void setActiveLeaderCardsDescription(String activeLeaderCardsDescription) {
        this.activeLeaderCardsDescription = activeLeaderCardsDescription;
    }

    public String getInactiveLeaderCardsDescription() {
        return inactiveLeaderCardsDescription;
    }

    public void setInactiveLeaderCardsDescription(String inactiveLeaderCardsDescription) {
        this.inactiveLeaderCardsDescription = inactiveLeaderCardsDescription;
    }
}
