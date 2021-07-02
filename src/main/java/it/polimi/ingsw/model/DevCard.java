package it.polimi.ingsw.model;

/**
 * Development Card class
 *
 * */
public class DevCard {

    /**
     * Enumeration class that represent the card's color
     * */
    public enum CardColor{
        GREEN("\u001B[32m"),
        BLUE("\u001B[34m"),
        YELLOW("\u001B[33m"),
        PURPLE("\u001B[35m");

        public static boolean contains(String string) {
            for (CardColor color : CardColor.values()) {
                if (color.name().equals(string)) {
                    return true;
                }
            }
            return false;
        }
        static final String RESET = "\u001B[0m";
        private String ansiCode;
        CardColor(String ansiCode)
        {
            this.ansiCode = ansiCode;
        }

        public String getAnsiCode(){
            return ansiCode;
        }

        @Override
        public String toString()
        {
            return this.name();
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

    public String describeDevCard(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(describeLevelAndColor());
        stringBuilder.append(", ");
        stringBuilder.append("Cost: " + cost.describeResource());
        stringBuilder.append(", ");
        stringBuilder.append("Prod: " + LHS.describeResource() + " —> " + RHS.describeResource());
        stringBuilder.append(", ");
        stringBuilder.append("VP: " + victoryPoints);
        stringBuilder.append(", ");
        stringBuilder.append("Level:" + this.getLevel());
        return stringBuilder.toString();
    }

    private String describeLevelAndColor(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Flag: ");
        switch (level){
            case 1:
                stringBuilder.append(color.getAnsiCode() + "[" + "\u2680" + "]" + DevCard.CardColor.RESET);
                break;
            case 2:
                stringBuilder.append(color.getAnsiCode() + "[" + "\u2681" + "]" + DevCard.CardColor.RESET);
                break;
            case 3:
                stringBuilder.append(color.getAnsiCode() + "[" + "\u2682" + "]" + DevCard.CardColor.RESET);
                break;
        }
        return stringBuilder.toString();
    }
}
