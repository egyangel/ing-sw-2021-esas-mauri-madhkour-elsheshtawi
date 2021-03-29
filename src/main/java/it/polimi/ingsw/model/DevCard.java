package it.polimi.ingsw.model;

import it.polimi.ingsw.model.EnumClasses.DevCardColor;

public class DevCard {

    private int level;
    private DevCardColor color;
    private Resources LHS;
    private Resources RHS;
    private Resources cost;
    private int victoryPoints;

    public int getLevel() {
        return level;
    }

    public Resources getRHS() {
        return RHS;
    }

    public Resources getLHS() {
        return LHS;
    }

    public DevCardColor getDevCardColor() {
        return color;
    }

    public Resources getCost() {
        return cost;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    public DevCard (int level, DevCardColor color, Resources LHS, Resources RHS, Resources cost, int victoryPoints) {
        this.color=color;
        this.level=level;
        this.LHS=LHS;
        this.RHS=RHS;
        this.cost=cost;
        this.victoryPoints=victoryPoints;
    }




    public Resources produce(){
        if (Resources>=LHS) LHS=RHS;
        else return 0;


    }







}
