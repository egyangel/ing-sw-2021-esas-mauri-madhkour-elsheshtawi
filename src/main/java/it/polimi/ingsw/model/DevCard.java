package it.polimi.ingsw.model;

public class DevCard {

    public enum CardColor{
        GREEN,BLUE,YELLOW,PURPLE;
    }

    private final int level;
    private final CardColor color;
    private final Resources LHS;
    private final Resources RHS;
    private final Resources cost;
    private final int victoryPoints;

    public DevCard (int level, CardColor color, Resources LHS, Resources RHS, Resources cost, int victoryPoints) {
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
    public CardColor getColor() {
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

    @Override
    public String toString(){
        return "Color: " + color + " Level: " + level + " RHS: " + RHS + " LHS: " + LHS + " Cost: " + cost + " Victory Points: " + victoryPoints;
    }
        // it is wrong, i have to implement.
    public Resources produce(){
        return null;
    }







}
