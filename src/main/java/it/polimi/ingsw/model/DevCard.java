package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumclasses.DevCardColor;

public class DevCard {


    private static int level;
    private final DevCardColor color;
    private final Resources LHS;
    private final Resources RHS;
    private final Resources cost;
    private static int victoryPoints;


    public static int getLevel() {
        return level;
    }
    public static int getVictoryPoints() {
        return victoryPoints;
    }
    public DevCardColor getColor() {
        return color;
    }
    public Resources getLHS() {
        return LHS;
    }
    public Resources getRHS() {
        return RHS;
    }
    public Resources getCost() {
        return cost;
    }

    public DevCard (int level, final DevCardColor color, Resources LHS, Resources RHS, Resources cost, int victoryPoints) {
        this.color=color;
        this.level=level;
        this.LHS=LHS;
        this.RHS=RHS;
        this.cost=cost;
        this.victoryPoints=victoryPoints;
    }


/*
*
*
* */



    public Resources produce(){
/*        if (Resources>=LHS) LHS=RHS;
        else return 0;


        */
        return null;

    }







}
