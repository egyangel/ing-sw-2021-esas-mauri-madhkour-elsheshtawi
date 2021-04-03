
package it.polimi.ingsw.model;



import it.polimi.ingsw.model.enumclasses.AbilityType;
import it.polimi.ingsw.model.specialability.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LeaderCard {

    private List<Requirements> requirements;
    private int points;
   private SpecialAbility ability;


    private boolean ready;

    public LeaderCard(List<Requirements> requierment, Integer vicPoints, SpecialAbility specialAbility ){
        this.requirements = requierment;
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
/*
    public boolean isReady(){
        return false; //under construction

    }
*/

}
