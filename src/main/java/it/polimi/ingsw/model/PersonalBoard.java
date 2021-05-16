package it.polimi.ingsw.model;

import it.polimi.ingsw.network.server.VirtualView;

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
        devSlots[0] = new DevSlot(DevSlot.slotPlace.LEFT);
        devSlots[1] = new DevSlot(DevSlot.slotPlace.CENTER);
        devSlots[2] = new DevSlot(DevSlot.slotPlace.RIGHT);
        warehouse[0] = new Shelf(Shelf.shelfPlace.TOP);
        warehouse[1] = new Shelf(Shelf.shelfPlace.MIDDLE);
        warehouse[2] = new Shelf(Shelf.shelfPlace.BOTTOM);
        strongbox = new Resources();
        popeAreaMap = new HashMap<>();
        inactiveLeaderCards = new ArrayList<>();
        activeLeaderCards = new ArrayList<>();
        popeAreaMap.put(PopeArea.FIRST, false);
        popeAreaMap.put(PopeArea.SECOND, false);
        popeAreaMap.put(PopeArea.THIRD, false);
    }

//     for now, this considers strongbox only, the code will need to be improved.
//     in case there is not enough resource, some error/exception must be included,
//     or assertions before execution/during testing
    public void putToWarehouseWithoutCheck(Resources res){
        for(Resources.ResType resType: res.getResTypes()){
            putFromTop(resType, res.getNumberOfType(resType));
        }
    }

    public void putToWarehouse(Shelf.shelfPlace place, Resources res){
        if (!res.isThisOneType()) {
            System.out.println("Resource should be one type for this function");
        } else {
            switch (place){
                case TOP:
                    warehouse[0].PutResource(res.getOnlyType(), res.sumOfValues());
                    break;
                case MIDDLE:
                    warehouse[1].PutResource(res.getOnlyType(), res.sumOfValues());
                    break;
                case BOTTOM:
                    warehouse[3].PutResource(res.getOnlyType(), res.sumOfValues());
                    break;
            }
        }
    }

    private boolean putFromTop(Resources.ResType resType, int size){
        if (checkEnoughSize(0, size) && checkSameType(0, resType)){
            warehouse[0].PutResource(resType, size);
        } else if (checkEnoughSize(1, size) && checkSameType(1, resType)){
            warehouse[0].PutResource(resType, size);
        } else if (checkEnoughSize(2, size) && checkSameType(2, resType)){
            warehouse[0].PutResource(resType, size);
        } else return false;
        return true;
    }

    private boolean checkEnoughSize(int index, int size){
        return ((warehouse[index].GetNumberOfElements()) + size < warehouse[index].ShelfSize());
    }

    private boolean checkSameType(int index, Resources.ResType resType){
        return (warehouse[index].GetShelfResType() == resType);
    }

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

    public void increaseFaitPoint(int toAdd){
        faithPoints += toAdd;
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

    public void putSelectedLeaderCards(List<LeaderCard> selectedCards) {
        inactiveLeaderCards.addAll(selectedCards);
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
