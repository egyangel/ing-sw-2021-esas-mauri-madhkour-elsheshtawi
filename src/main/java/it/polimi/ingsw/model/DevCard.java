package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumclasses.DevCardColor;

public class DevCard {

    private final int level;
    private final DevCardColor color;
    private final Resources LHS;
    private final Resources RHS;
    private final Resources cost;
    private final int victoryPoints;

    public DevCard (int level, final DevCardColor color, Resources LHS, Resources RHS, Resources cost, int victoryPoints) {
        this.color=color;
        this.level=level;
        this.LHS=LHS;
        this.RHS=RHS;
        this.cost=cost;
        this.victoryPoints=victoryPoints;
    }

    public int getLevel() {
        return this.level;
    }
    public int getVictoryPoints() {
        return this.victoryPoints;
    }
    public DevCardColor getColor() {
        return this.color;
    }
    public Resources getLHS() {
        return this.LHS;
    }
    public Resources getRHS() {
        return this.RHS;
    }
    public Resources getCost() {
        return this.cost;
    }

    public String toString() {
        return color +"_"+level+"_"+ RHS+"_"+LHS+"_"+"_"+cost+"_"+victoryPoints;
    }

    public Resources produce(){
        return null;
    }







}
