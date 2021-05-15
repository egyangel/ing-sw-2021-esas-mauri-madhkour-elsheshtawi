
package it.polimi.ingsw.model;



import it.polimi.ingsw.model.enumclasses.AbilityType;
import it.polimi.ingsw.model.specialability.*;

import java.util.ArrayList;
import java.util.List;

public class LeaderCard {

    private List<Requirements> requirements=new ArrayList<>();
    private int points;
    private SpecialAbility ability;


    public LeaderCard(List<Requirements> requierment, Integer vicPoints, SpecialAbility specialAbility ){
        this.requirements.addAll(requierment);
        this.points = vicPoints;
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
    public int getVictoryPoints() { return points; }
    public  List<Requirements> getRequirements() { return this.requirements; }
    public  void getRequirementse() {

         for (int i = 0; i < this.requirements.size(); i++) {
            System.out.println(this.requirements.get(i).getReq());
        }
    }

//    @Override
//    public String toString(){
//        return "Color: " + color + " Level: " + level + " RHS: " + RHS + " LHS: " + LHS + " Cost: " + cost + " Victory Points: " + victoryPoints;
//    }
}