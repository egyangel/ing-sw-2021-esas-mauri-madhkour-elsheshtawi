package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumclasses.CliColors;
/**
 * Development Card class
 * @author
 *
 * */
public class DevCard {

    public DevCard(CliColors[] values, int level, Resources lhs, Resources rhs, Resources cost, int victoryPoints) {
        this.level = level;
        LHS = lhs;
        RHS = rhs;
        this.cost = cost;
        this.victoryPoints = victoryPoints;
        color = null;
    }



    public static CliColors getFaith() {
        return CliColors.red;
    }
    public static CliColors getShield(){
        return CliColors.cyan;
    }
    public static CliColors getCoin(){
        return CliColors.gold;
    }
    public static CliColors getServant(){
        return CliColors.purple;
    }
    public static CliColors getStone(){
        return CliColors.grey;
    }
    public static CliColors getBlueCard(){
        return CliColors.blue;
    }
    public static CliColors getYellowcard(){
        return CliColors.yellow;
    }
    public static CliColors getGreenCard(){
        return CliColors.green;
    }
    public static CliColors getPurpleCard(){
        return CliColors.purpleBright;
    }
    private CliColors colors;
    private String face;

    /**
     * Enumeration class that represent the card's color
     * */
    public enum CardColor{
        GREEN,BLUE,YELLOW,PURPLE;

        public static boolean contains(String string) {
            for (CardColor color : CardColor.values()) {
                if (color.name().equals(string)) {
                    return true;
                }
            }
            return false;
        }
    }

    private final int level;
    private final CardColor color;
    private final Resources LHS;
    private final Resources RHS;
    private final Resources cost;
    private final int victoryPoints;

    /**
     * Constructor of a normal Development Card
     * @param color represent the color of the card
     * @param level represent the level of the card
     * @param cost represent the cost of the card
     * @param LHS represent the LHS(left hand side of production power) of the card
     * @param RHS represent the RHS(right hand side of production power of the card
     * @param victoryPoints represent the victoryPoints of the card
     * */
    public DevCard (int level, CardColor color, Resources LHS, Resources RHS, Resources cost, int victoryPoints) {
        this.color=color;
        this.level=level;
        this.LHS=LHS;
        this.RHS=RHS;
        this.cost=cost;
        this.victoryPoints=victoryPoints;

    }
    /**
     * Constructor of Default Production, I chose to think it as a Development
     * Card with only the two attribute
     * @param LHS represent the LHS(left hand side of production power) of the card
     * @param RHS represent the RHS(right hand side of production power of the card
     * */
    public DevCard ( Resources LHS, Resources RHS) {
        this.color = null;
        this.level = 0 ;
        this.cost  = null;
        this.victoryPoints = 0;
        this.LHS = LHS;
        this.RHS = RHS;

    }

    public CliColors getColors(){return colors;  }
    public void setColors(CliColors colors){this.colors=colors;}
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

        return this.colors+"|"+face+"|"+CliColors.colorReset;
        //return "Color: " + color + " Level: " + level + " RHS: " + RHS + " LHS: " + LHS + " Cost: " + cost + " VP: " + victoryPoints;
    }







}
