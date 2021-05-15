package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumclasses.AbilityType;
import it.polimi.ingsw.model.specialability.*;

import java.util.List;

public class LeaderCard {
    private Requirement requirement;
    private int victoryPoint;
    private SpecialAbility ability;

    public LeaderCard(Requirement requirement, Integer vicPoints, SpecialAbility specialAbility ){
        this.requirement = requirement;
        this.victoryPoint = vicPoints;
        this.ability = specialAbility;
        if(specialAbility. getEffect().equals(AbilityType.DISCOUNT))
            this.ability = new Discount(specialAbility);
        if(specialAbility. getEffect().equals(AbilityType.ADDPROD))
            this.ability = new AdditionalProduction(specialAbility);
        if(specialAbility. getEffect().equals(AbilityType.EXSTRASLOT))
            this.ability = new ExstraSlot(specialAbility);
        if(specialAbility. getEffect().equals(AbilityType.CONVERTWHITE))
            this.ability = new ConvertWhiteMarble(specialAbility);

    }

    public SpecialAbility getAbility() { return this.ability;  }
    public int getVictoryPoints() { return victoryPoint; }
    public Requirement getRequirement() { return this.requirement; }
//
//    @Override
//    public String toString(){
//        return "Color: " + color + " Level: " + level + " RHS: " + RHS + " LHS: " + LHS + " Cost: " + cost + " Victory Points: " + victoryPoints;
//    }
}