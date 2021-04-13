package it.polimi.ingsw.model;
import it.polimi.ingsw.model.enumclasses.ResType;

import java.sql.SQLOutput;
import java.util.*;

public class Player {
    private String nickname;
    private int victoryPoints;
    private PersonalBoard personalBoard;
    private boolean inkwell;

    public Player(String nickname) {
        this.nickname = nickname;
    }

    public void setInkwell(boolean inkwell){
        this.inkwell=inkwell;
    }
    public boolean hasInkwell() { return inkwell; }
    public void countVictoryPoints(int point){
        victoryPoints+=point;
    }
    public int getVictoryPoints(){
        return victoryPoints;
    }

    //    to simulate the environment, not to be used in actual game
    public void giveInitialStrongboxResources(int stone, int shield, int servant, int coin, int faith){ this.personalBoard.setStrongbox(new Resources(stone, shield, servant, coin, faith)); }

    public void useDefProd(ResType L1, ResType L2, ResType R){
        this.personalBoard.useDefProd(L1, L2, R);
    }

    public void printStrongBox(){
        Resources strongbox = this.personalBoard.getStrongBox();
        for(ResType type: ResType.values()) {
            System.out.println("There is " + strongbox.GetNumberOfType(type) + " " + type.toString());
        }
        System.out.println();
    }
}
