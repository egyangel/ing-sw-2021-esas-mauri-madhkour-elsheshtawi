package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.model.DevCard;

import java.util.EnumSet;
import java.util.Set;

public class BuyDevCardActionContext {
    public enum ActionStep{
        // from client to server
        COLOR_LEVEL_CHOSEN,
        // from server to client
        CHOOSE_COLOR_LEVEL,
        EMPTY_DEVCARD_DECK_ERROR,
        NOT_ENOUGH_RES_FOR_DEVCARD_ERROR,
        UNSUITABLE_FOR_DEVSLOTS_ERROR,
        CHOOSE_DEV_SLOT;
    }
    private ActionStep lastStep;
    private int level;
    private DevCard.CardColor color;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public DevCard.CardColor getColor() {
        return color;
    }

    public void setColor(DevCard.CardColor color) {
        this.color = color;
    }

    public ActionStep getLastStep(){
        return lastStep;
    }

    public void setLastStep(ActionStep step){
        lastStep = step;
    }

    public boolean isError(){
        Set<ActionStep> errorSteps = EnumSet.of(ActionStep.EMPTY_DEVCARD_DECK_ERROR,
                                                ActionStep.NOT_ENOUGH_RES_FOR_DEVCARD_ERROR,
                                                ActionStep.UNSUITABLE_FOR_DEVSLOTS_ERROR);
        if (errorSteps.contains(lastStep)) return true;
        else return false;
    }
}


