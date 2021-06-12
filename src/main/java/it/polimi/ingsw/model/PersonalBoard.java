package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonalBoard {
    public enum PopeArea {
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
    private Map<PopeArea, Boolean> popeAreaMap;

    public PersonalBoard(Integer userID) {
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

    public PersonalBoard(Integer userID, boolean solo) {
        this(userID);
        if (solo) {
            // TODO Place Black CROSS

        }
    }

    //     for now, this considers strongbox only, the code will need to be improved.
//     in case there is not enough resource, some error/exception must be included,
//     or assertions before execution/during testing
    public void putToWarehouseWithoutCheck(Resources res) {
        for (Resources.ResType resType : res.getResTypes()) {
            putFromTop(resType, res.getNumberOfType(resType));
        }
    }

    public boolean putToWarehouse(Shelf.shelfPlace place, Resources res) {
        return warehouse[place.getIndexInWarehouse()].putResource(res);
    }

    private boolean putFromTop(Resources.ResType resType, int size) {
        if (checkEnoughSize(0, size) && checkSameType(0, resType)) {
            warehouse[0].putResource(resType, size);
        } else if (checkEnoughSize(1, size) && checkSameType(1, resType)) {
            warehouse[0].putResource(resType, size);
        } else if (checkEnoughSize(2, size) && checkSameType(2, resType)) {
            warehouse[0].putResource(resType, size);
        } else return false;
        return true;
    }

    private boolean checkEnoughSize(int index, int size) {
        return ((warehouse[index].getNumberOfElements()) + size < warehouse[index].shelfSize());
    }

    private boolean checkSameType(int index, Resources.ResType resType) {
        try {
            return (warehouse[index].getShelfResType() == resType);
        } catch (Exception e) {
            return true;
        }


    }


    public void useDefProd(Resources.ResType L1, Resources.ResType L2, Resources.ResType R) {
        System.out.println("Trying Default Prod: " + L1.toString() + " + " + L2.toString() + " = " + R.toString() + "\n");
        if (L1 != L2) {
            if (this.strongbox.isThereType(L1) && this.strongbox.isThereType(L2)) {
                this.strongbox.subtract(L1, 1);
                this.strongbox.subtract(L2, 1);
                this.strongbox.add(R, 1);
            }
        } else {
            if (this.strongbox.getNumberOfType(L1) >= 2) {
                this.strongbox.subtract(L1, 2);
                this.strongbox.add(R, 1);
            }
        }
    }

    public void increaseFaitPoint(int toAdd) {
        faithPoints += toAdd;
    }

    public void giveNextVaticanReport() {
        if (vaticanReportCallCounter == 0 && faithPoints >= 5 && faithPoints <= 8) {
            turnPopeFavorTile(PopeArea.FIRST);
        } else if (vaticanReportCallCounter == 1 && faithPoints >= 12 && faithPoints <= 16) {
            turnPopeFavorTile(PopeArea.SECOND);
        } else if (vaticanReportCallCounter == 2 && faithPoints >= 19 && faithPoints <= 24) {
            turnPopeFavorTile(PopeArea.THIRD);
        }
        vaticanReportCallCounter++;
    }

    private void turnPopeFavorTile(PopeArea area) {
        popeAreaMap.replace(area, Boolean.TRUE);
        // create a MV message inside Game
    }

    public void putSelectedLeaderCards(List<LeaderCard> selectedCards) {
        inactiveLeaderCards.addAll(selectedCards);
    }

    public List<LeaderCard> getActiveLeaderCards() {
        return activeLeaderCards;
    }

    public Resources getTotalResources() {
        Resources res = new Resources();
        res.add(getWarehouseResources());
        res.add(getStrongboxResources());
        return res;
    }

    public Resources getStrongboxResources() {
        Resources res = new Resources();
        res.add(strongbox);
        return res;
    }

    public Resources getWarehouseResources() {
        Resources res = new Resources();
        for (Shelf shelf : warehouse) {
            res.add(shelf.getResource());
        }
        return res;
    }

    public boolean isThereEnoughRes(DevCard card) {
        Resources totalRes = getTotalResources();
        Resources cost = card.getCost();
        if (cost.smallerOrEqual(totalRes)) return true;
        else return false;
    }

    public boolean isCardSuitableForSlots(DevCard card) {
        int level = card.getLevel();
        for (DevSlot devSlot : devSlots) {
            if (level == devSlot.getLevelOfTopCard() + 1) return true;
        }
        return false;
    }

    public List<DevSlot.slotPlace> getSuitablePlaces(DevCard card) {
        int level = card.getLevel();
        List<DevSlot.slotPlace> places = new ArrayList<>();
        for (DevSlot devSlot : devSlots) {
            if (level == devSlot.getLevelOfTopCard() + 1) places.add(devSlot.getPlace());
        }
        return places;
    }

    public void spendOneFromWarehouse(Resources.ResType resType) {
        for (Shelf shelf : warehouse) {
            if (shelf.getShelfResType().equals(resType)) {
                shelf.removeOneFromShelf();
                return;
            }
        }
    }

    public void spendOneFromStrongbox(Resources.ResType resType) {
        strongbox.subtract(resType, 1);
    }

    public void putDevCardOnSlot(DevCard card, DevSlot.slotPlace place) {
        int index = place.getIndexInBoard();
        devSlots[index].putDevCard(card);
    }

    public DevCard getDevCardOnSlot(DevSlot place) {
        int index = place.getPlace().getIndexInBoard();
        if(devSlots[index].isEmpty()){
            return null;
        }
        return devSlots[index].getTopDevCard();
    }

    public int clearShelf(Shelf.shelfPlace place) {
        // TODO send MV event through game, that includes string representation of changed object

        return warehouse[place.getIndexInWarehouse()].clearShelf();
    }

    public int swapShelves(Shelf.shelfPlace[] places) {
        // TODO send MV event through game, that includes string representation of changed object
        return warehouse[places[0].getIndexInWarehouse()].swapShelf(warehouse[places[1].getIndexInWarehouse()]);
    }

    public void subtractFromWarehouse(Resources res) {
        List<Resources.ResType> resTypeList = new ArrayList<>();
        resTypeList.addAll(res.getResTypes());
        for (int i = 0; i < 3; i++) {
            for (Resources.ResType resType : resTypeList) {
                if (warehouse[i].getShelfResType().equals(resType)) {
                    int number = res.getNumberOfType(resType);
                    warehouse[i].removeFromShelf(number);
                }
            }
        }
    }

    public void putResInStrongBox(Resources res) {
        this.strongbox.add(res);
    }

    public void subtractFromStrongbox(Resources res) {
        this.strongbox.subtract(res);
    }


    // DEBUG methods
    public void setStrongbox(Resources strongbox) {
        this.strongbox = strongbox;
    }


    public String describeWarehouse() {
        String string = "Top Shelf: " + warehouse[0].describeShelf() + "\nMiddle Shelf: " + warehouse[1].describeShelf() + "\nBottom Shelf: " + warehouse[2].describeShelf();
        return string;
    }

    public String describeStrongbox() {
        String string = "Resources inside strong box: " + strongbox.toString();
        return string;
    }

    public String describeDevSlots() {
        String string = "";
        for (DevSlot slot : devSlots) {
            string += slot.describeDevSlot() + "\n";
        }
        return string.trim(); //removes one \n at the end
    }

    //TODO FOR AMOR: same for faith track, try to show special pope fields
    public String describeFaithTrack() {
        return null;
    }

    //TODO
    public String describeLeaderCards() {
        return null;
    }
    // NO USE METHODS for now
    //    public boolean swapShelves(List<Shelf.shelfPlace> list){
//        int firstIndex = list.get(0).ordinal();
//        int secondIndex = list.get(1).ordinal();
//        boolean result =  warehouse[firstIndex].swapShelf(warehouse[secondIndex]);
//        if (result){
//            MVEvent mvEvent = new MVEvent(MVEvent.EventType.SWAPPED_SHELVES, this);
//            game.publish(userID, mvEvent);
//        }
//        return result;
//    }

//    public boolean discardFromShelf(Shelf.shelfPlace place){
//        int index = place.ordinal();
//        boolean result =  warehouse[index].removeOneFromShelf();
//        if (result){
//            MVEvent mvEvent = new MVEvent(MVEvent.EventType.DISCARDED_FROM_SHELF, this);
//            game.publish(userID, mvEvent);
//        }
//        return result;
//    }
}
