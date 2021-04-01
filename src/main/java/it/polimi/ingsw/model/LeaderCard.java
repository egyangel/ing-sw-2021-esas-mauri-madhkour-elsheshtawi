
package it.polimi.ingsw.model;



import it.polimi.ingsw.model.enumclasses.AbilityType;
import it.polimi.ingsw.model.enumclasses.DevCardColor;

import java.util.List;

public class LeaderCard {
// I am quite sure that we have to change the requirments
    private List<Object> requirements;
    private int victoryPoints;
    private AbilityType specialAbility; //private SpecialAbility ... (?)
    private boolean ready;

    public LeaderCard(String vicPoints,String specialAbility ){
        this.specialAbility = AbilityType.valueOf(specialAbility);
        this.victoryPoints = Integer.parseInt(vicPoints);
    }

    public AbilityType getSpecialAbility() { return specialAbility;  }
    public int getVictoryPoints() { return victoryPoints; }

    public boolean isReady(){
        return false; //under construction

    }


}