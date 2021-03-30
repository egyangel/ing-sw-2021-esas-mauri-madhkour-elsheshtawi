package it.polimi.ingsw.model;

import it.polimi.ingsw.model.EnumClasses.DevCardColor;
import it.polimi.ingsw.model.SpecialAbility.SpecialAbility;

import java.util.List;

public class LeaderCard {

    private List requirements;
    private int victoryPoints;
    private SpecialAbility specialAbility;
    private boolean ready;
    private boolean ontable;

    public boolean isReady(){
        // honestly don't understand what this method is for. i assume that it's when you place the
        //leader card on the table it activates the leader card's special ability
        if (LeaderCard=ontable) activate();

    }


}
