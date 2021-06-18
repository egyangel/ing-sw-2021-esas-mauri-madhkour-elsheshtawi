package it.polimi.ingsw.model;
/**
 * Leader Card class  ,it is the the representation of the leader card with
 * @author
 * */
public class LeaderCard {
    private Requirement requirement;
    private int victoryPoint;
    private SpecialAbility ability;
/**
 * Constructor set the param of the card object *
 *
 * */
    public LeaderCard(Requirement requirement, Integer vicPoints, SpecialAbility specialAbility ){
        this.requirement = requirement;
        this.victoryPoint = vicPoints;
        this.ability = specialAbility;

//        if(specialAbility. getEffect().equals(AbilityType.DISCOUNT))
//            this.ability = new Discount(specialAbility);
//        if(specialAbility. getEffect().equals(AbilityType.ADDPROD))
//            this.ability = new AdditionalProduction(specialAbility);
//        if(specialAbility. getEffect().equals(AbilityType.EXSTRASLOT))
//            this.ability = new ExstraSlot(specialAbility);
//        if(specialAbility. getEffect().equals(AbilityType.CONVERTWHITE))
//            this.ability = new ConvertWhiteMarble(specialAbility);

    }

    public SpecialAbility getAbility() { return this.ability;  }
    public int getVictoryPoints() { return victoryPoint; }
    public Requirement getRequirement() { return this.requirement; }

    @Override
    public String toString() {
        return "LeaderCard{" +
                "requirement=" + requirement +
                ", victoryPoint=" + victoryPoint +
                ", ability=" + ability +
                '}';
    }

    public String describeLeaderCard(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(requirement.describeRequirement());
        stringBuilder.append(", ");
        stringBuilder.append("VP: " + victoryPoint);
        stringBuilder.append(", ");
        stringBuilder.append(ability.describeSpecialAbility());
        return stringBuilder.toString();
    }
}