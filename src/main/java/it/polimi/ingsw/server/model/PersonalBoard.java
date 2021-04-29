package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enumclasses.ResType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PersonalBoard {
    private Player player;
    private DefaultProd defProd;
    private DevSlot[] devSlots = new DevSlot[3];
    private Shelf[] warehouse = new Shelf[3];
    private Resources strongbox;
    private int faithPoints;//Maybe it is better call it faithMarker
    private List<LeaderCard> leadersCards;

    public PersonalBoard(Player player){
        player = player;
        defProd = new DefaultProd();
        // from left to right on personal board
        devSlots[0] = new DevSlot();
        devSlots[1] = new DevSlot();
        devSlots[2] = new DevSlot();
        // from top to bottom on warehouse
        warehouse[0] = new Shelf(1);
        warehouse[1] = new Shelf(2);
        warehouse[2] = new Shelf(3);
        strongbox = new Resources();
        faithPoints = 0;
    }
    public void setLeadersCards(List<LeaderCard> cardList){
        leadersCards = new ArrayList<>(cardList);
        //leadersCards  = cardList.stream().collect(Collectors.toList());
    }

    public void setStrongbox(Resources strongbox) {
        this.strongbox = strongbox;
    }

    public Resources getStrongBox(){
        return this.strongbox;
    }

//     for now, this considers strongbox only, the code will need to be improved.
//     in case there is not enough resource, some error/exception must be included,
//     or assertions before execution/during testing
    public void useDefProd(ResType L1, ResType L2, ResType R){
        System.out.println("Trying Default Prod: " + L1.toString() + " + " + L2.toString() + " = " + R.toString() + "\n");
        if (L1 != L2){
            if (this.strongbox.isThereType(L1) && this.strongbox.isThereType(L2)){
                this.strongbox.subtract(L1, 1);
                this.strongbox.subtract(L2, 1);
                this.strongbox.add(R, 1);
            }
        }
        else {
            if (this.strongbox.getNumberOfType(L1) >= 2){
                this.strongbox.subtract(L1, 2);
                this.strongbox.add(R, 1);
            }
        }
    }

    public void moveFaitMarker(){ faithPoints++; }
    public int getFaithpos(){ return faithPoints; }
    //   check if the player satisfy the pos to turn the Pope's favor
    public void check(int currentPopeFavor) {
        if (faithPoints >= 5 && faithPoints <= 11 && currentPopeFavor==1) player.countVictoryPoints(2);
        if (faithPoints >= 12 && faithPoints <= 18 && currentPopeFavor==2) player.countVictoryPoints(3);
        if (faithPoints >= 5 && faithPoints <= 11 && currentPopeFavor==3) player.countVictoryPoints(4);
    }

}
