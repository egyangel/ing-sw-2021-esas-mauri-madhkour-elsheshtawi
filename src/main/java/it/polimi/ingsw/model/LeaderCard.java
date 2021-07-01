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
 * Constructor set the param of the leader card object
 * @param requirement is the requirement for activation phase of a leader
 * @param vicPoints  the pint of the card
 * @param specialAbility the ability of the leader card
 * */
    public LeaderCard(Requirement requirement, Integer vicPoints, SpecialAbility specialAbility ){
        this.requirement = requirement;
        this.victoryPoint = vicPoints;
        this.ability = specialAbility;
    }

    public SpecialAbility getAbility() { return this.ability;  }
    public int getVictoryPoints() { return victoryPoint; }
    public Requirement getRequirement() { return this.requirement; }

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