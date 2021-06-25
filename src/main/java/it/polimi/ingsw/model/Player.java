package it.polimi.ingsw.model;

/**
 *
 *
 * */
public class Player {
    private Integer userID;
    private boolean inkwell;
    private PersonalBoard personalBoard;

    //    DEBUG methods
    public void giveInitialStrongboxResources(int stone, int shield, int servant, int coin){
        this.personalBoard.setStrongbox(new Resources(stone, shield, servant, coin));
    }
}

