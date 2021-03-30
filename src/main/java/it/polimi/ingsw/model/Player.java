package it.polimi.ingsw.model;
import java.util.*;

public class Player {
    private int playerId;
    private String nickname;
    private int victoryPoints;
    private PersonalBoard personalBoard;
    private boolean inkwell;

    public Player(){
        personalBoard=new PersonalBoard();
        Scanner input = new Scanner(System.in);

        System.out.print("inserts nickname: ");
        nickname= input.nextLine();
        System.out.println();

        System.out.print("inserts playerId: ");
        playerId= input.nextInt();
        System.out.println();

        victoryPoints=0;
        inkwell=false;
    }

    public void setInkwell(boolean inkwell){
        this.inkwell=inkwell;
    }
    public boolean hasInkwell() {
        return inkwell;
    }
    public void countVictoryPoints(int point){
        victoryPoints+=point;


    }
    public int getVictoryPoints(){
        return victoryPoints;
    }
}
