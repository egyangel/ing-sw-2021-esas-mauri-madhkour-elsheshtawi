package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalBoard {
    public enum PopeArea{
        FIRST,
        SECOND,
        THIRD
    }
    private Integer userID;
    private Game game;
    private int victoryPoints;
    private DefaultProd defProd;
    private DevSlot[] devSlots = new DevSlot[3];
    private Shelf[] warehouse = new Shelf[3];
    private Resources strongbox;
    private int faithPoints = 0;
    private List<LeaderCard> inactiveLeaderCards;
    private List<LeaderCard> activeLeaderCards;
    private int vaticanReportCallCounter = 0;
    private Map<PopeArea,Boolean> popeAreaMap;

    public PersonalBoard(Integer userID){
        this.userID = userID;
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
        popeAreaMap = new HashMap<>();
        popeAreaMap.put(PopeArea.FIRST, false);
        popeAreaMap.put(PopeArea.SECOND, false);
        popeAreaMap.put(PopeArea.THIRD, false);
    }

//     for now, this considers strongbox only, the code will need to be improved.
//     in case there is not enough resource, some error/exception must be included,
//     or assertions before execution/during testing
    public void useDefProd(Resources.ResType L1, Resources.ResType L2, Resources.ResType R){
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

    public void increaseFaitPoint(){
        faithPoints++;
    }

    public void giveNextVaticanReport() {
        if (vaticanReportCallCounter == 0 && faithPoints >= 5 && faithPoints <= 8){
            turnPopeFavorTile(PopeArea.FIRST);
        } else if (vaticanReportCallCounter == 1 && faithPoints >= 12 && faithPoints <= 16) {
            turnPopeFavorTile(PopeArea.SECOND);
        } else if (vaticanReportCallCounter == 2 && faithPoints >= 19 && faithPoints <= 24){
            turnPopeFavorTile(PopeArea.THIRD);
        }
        vaticanReportCallCounter++;
    }

    private void turnPopeFavorTile(PopeArea area){
        popeAreaMap.replace(area, Boolean.TRUE);
        // create a MV message inside Game
    }

    // DEBUG methods
    public void setStrongbox(Resources strongbox) {
        this.strongbox = strongbox;
    }

    public void printStrongBox() {
        for (Resources.ResType type : Resources.ResType.values()) {
            System.out.println("There is " + strongbox.getNumberOfType(type) + " " + type.toString());
        }
        System.out.println();
    }
}
