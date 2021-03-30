package it.polimi.ingsw.model;



import it.polimi.ingsw.model.enumclasses.AbilityType;
import it.polimi.ingsw.model.enumclasses.DevCardColor;

import java.util.List;

public class LeaderCard {

    private List<DevCardColor> requirements;
    private int victoryPoints;
    private AbilityType specialAbility; //private SpecialAbility ... (?)
    private boolean ready;

    public boolean isReady(){
        return false; //under construction

    }


}
