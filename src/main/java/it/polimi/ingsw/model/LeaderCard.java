package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumclasses.DevCardColor;
import it.polimi.ingsw.model.specialability.SpecialAbility;

import java.util.List;

public class LeaderCard {

    private List requirements;
    private int victoryPoints;
    private SpecialAbility specialAbility;
    private boolean ready;

    public boolean isReady(){
        return true;
    }


}
