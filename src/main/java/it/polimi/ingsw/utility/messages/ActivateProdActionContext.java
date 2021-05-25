package it.polimi.ingsw.utility.messages;

public class ActivateProdActionContext {
    public enum ActionStep{
        // from client to server

        // from server to client
        CHOOSE_DEV_SLOTS
    }
    private ActionStep lastStep;

    public ActionStep getLastStep(){
        return lastStep;
    }

    public void setLastStep(ActionStep step){
        lastStep = step;
    }
}
