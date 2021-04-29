
package it.polimi.ingsw.server.model;



import it.polimi.ingsw.server.model.enumclasses.AbilityType;
import it.polimi.ingsw.server.model.specialability.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LeaderCard {

    private List<Requirements> requirements=new ArrayList<>();
    private int points;
    private SpecialAbility ability;

    private boolean ready;


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
/*
    public boolean isReady(){
        return false; //under construction

    }
*/

}