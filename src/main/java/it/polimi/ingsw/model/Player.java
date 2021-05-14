package it.polimi.ingsw.model;
import it.polimi.ingsw.model.enumclasses.ResType;

public class Player {
    private PersonalBoard personalBoard;
    private boolean inkwell;

    //    DEBUG methods
    public void giveInitialStrongboxResources(int stone, int shield, int servant, int coin, int faith){
        this.personalBoard.setStrongbox(new Resources(stone, shield, servant, coin, faith));
    }
}

