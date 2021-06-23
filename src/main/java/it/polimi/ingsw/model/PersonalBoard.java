package it.polimi.ingsw.model;

import java.util.*;

public class PersonalBoard {
    private Object IllegalStateException;

    public enum PopeArea {
        FIRST,
        SECOND,
        THIRD
    }

    private Integer userID;
    private Game game;
    private int victoryPoints = 0;
    private DevSlot[] devSlots = new DevSlot[3];
    private Shelf[] warehouse = new Shelf[3];
    private Resources strongbox;

    private int faithPoints = 0;
    private Set<LeaderCard> inactiveLeaderCards;
    private Set<LeaderCard> activeLeaderCards;
    private Map<PopeArea, Boolean> popeAreaMap;

    private List<DevCard> ownedCards = new ArrayList<>();

    public PersonalBoard(Integer userID) {
        this.userID = userID;
        devSlots[0] = new DevSlot(DevSlot.slotPlace.LEFT);
        devSlots[1] = new DevSlot(DevSlot.slotPlace.CENTER);
        devSlots[2] = new DevSlot(DevSlot.slotPlace.RIGHT);
        warehouse[0] = new Shelf(Shelf.shelfPlace.TOP);
        warehouse[1] = new Shelf(Shelf.shelfPlace.MIDDLE);
        warehouse[2] = new Shelf(Shelf.shelfPlace.BOTTOM);
        strongbox = new Resources();
        popeAreaMap = new HashMap<>();
        inactiveLeaderCards = new HashSet<>();
        activeLeaderCards = new HashSet<>();
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

    public int putToWarehouse(Shelf.shelfPlace place, Resources res) {
        return warehouse[place.getIndexInWarehouse()].putResource(res);
    }

    private boolean putFromTop(Resources.ResType resType, int size) {
        if (checkEnoughSize(0, size) && (checkSameType(0, resType) || warehouse[0].isEmpty())) {
            warehouse[0].putResource(resType, size);
        } else if (checkEnoughSize(1, size) && checkSameType(1, resType) || warehouse[1].isEmpty()) {
            warehouse[1].putResource(resType, size);
        } else if (checkEnoughSize(2, size) && checkSameType(2, resType) || warehouse[2].isEmpty()) {
            warehouse[2].putResource(resType, size);
        } else return false;
        return true;
    }

    private boolean checkEnoughSize(int index, int size) {
        return ((warehouse[index].getNumberOfElements()) + size <= warehouse[index].shelfSize());
    }

    private boolean checkSameType(int index, Resources.ResType resType) {
        try {
            return (warehouse[index].getShelfResType() == resType);
        } catch (Exception e) {
            return true;
        }


    }
    public void setOwnedCard(DevCard myCards) {
        this.ownedCards.add(myCards);
    }
    public  List<DevCard> getOwnedCard() {
        return this.ownedCards;
    }

    public void countVictoryPoints(int victoryPoints) {
        this.victoryPoints+=victoryPoints;
    }

    public  int getVictoryPoints() {
        return this.victoryPoints;
    }

    public void increaseFaitPoint(int toAdd) {
        if (faithPoints < 8 && (faithPoints+toAdd)>=8){
            game.triggerVaticanReport(PopeArea.FIRST);
        } else if (faithPoints < 16 && (faithPoints+toAdd)>=16){
            game.triggerVaticanReport(PopeArea.SECOND);
        } else if (faithPoints < 24 && (faithPoints+toAdd)>=24){
            game.triggerVaticanReport(PopeArea.THIRD);
        }
        faithPoints += toAdd;
    }

    public Map<PopeArea, Boolean> getPopeAreaMap(){
        return popeAreaMap;
    }

    public int getFaithPoints(){
        return faithPoints;
    }

    public void giveVaticanReport(PopeArea area) {
        if (area == PopeArea.FIRST && faithPoints >= 5 && faithPoints <= 8) {
            turnPopeFavorTile(PopeArea.FIRST);
        } else if (area == PopeArea.SECOND && faithPoints >= 12 && faithPoints <= 16) {
            turnPopeFavorTile(PopeArea.SECOND);
        } else if (area == PopeArea.THIRD && faithPoints >= 19 && faithPoints <= 24) {
            turnPopeFavorTile(PopeArea.THIRD);
        }
    }

    private void turnPopeFavorTile(PopeArea area) {
        popeAreaMap.replace(area, Boolean.TRUE);
        // create a MV message inside Game
    }
    public int getTurnPopeFavorTile() {
        int temp=0;

        for (Map.Entry<PopeArea, Boolean> entry : popeAreaMap.entrySet()) {
            if (entry.getKey() == PopeArea.FIRST && entry.getValue()) temp += 2;
            if (entry.getKey() == PopeArea.SECOND && entry.getValue()) temp += 3;
            if (entry.getKey() == PopeArea.THIRD && entry.getValue()) temp += 4;
        }

        return temp;
    }

    public void putSelectedLeaderCards(List<LeaderCard> selectedCards) { inactiveLeaderCards.addAll(selectedCards); }
    public List<LeaderCard> getInactiveLeaderCards() { return new ArrayList<>(this.inactiveLeaderCards); }

    public void changePlayerCard(List<LeaderCard> Cards){
            inactiveLeaderCards.removeAll(Cards);
    }

    public void setActiveLeaderCards(Set<LeaderCard> activeLeaderCards) {
         this.activeLeaderCards.addAll(activeLeaderCards);
    }

    public List<LeaderCard> getActiveLeaderCards() {
        return new ArrayList<>(this.activeLeaderCards);
    }

    public Resources getTotalResources() {
        Resources res = new Resources();
        res.add(getWarehouseResources());
        res.add(getStrongboxResources());
        res.add(getExtraSlotResources());
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

    public Resources getExtraSlotResources(){
        Resources res = new Resources();
        for(LeaderCard card:activeLeaderCards){
            if(card.getAbility().getAbilityType() == SpecialAbility.AbilityType.EXTRASLOT){
                res.add(card.getAbility().getResourcesAtSlot());
            }
        }
        return res;
    }

    public boolean isThereEnoughRes(DevCard card) {
        Resources totalRes = getTotalResources();
        Resources cost = new Resources();
        cost.add(card.getCost());
        for(LeaderCard leaderCard: activeLeaderCards){
            if(leaderCard.getAbility().getAbilityType() == SpecialAbility.AbilityType.DISCOUNT){
                Resources.ResType discountType = leaderCard.getAbility().getResType();
                cost.subtract(discountType,1);
            }
        }
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

    public void putDevCardOnSlot(DevCard card, DevSlot.slotPlace place) {
        int index = place.getIndexInBoard();
        devSlots[index].putDevCard(card);
    }

    public DevCard getDevCardOnSlot(DevSlot.slotPlace place) {
        int index = place.getIndexInBoard();
        if(devSlots[index].isEmpty()){
            return null;
        }
        return devSlots[index].getTopDevCard();
    }

    public int clearShelf(Shelf.shelfPlace place) {
        return warehouse[place.getIndexInWarehouse()].clearShelf();
    }

    public int swapShelves(Shelf.shelfPlace[] places) {
        return warehouse[places[0].getIndexInWarehouse()].swapShelf(warehouse[places[1].getIndexInWarehouse()]);
    }

    public List<Shelf> getShelves(){
        List<Shelf> shelves = new ArrayList<>();
        shelves.add(warehouse[0]);
        shelves.add(warehouse[1]);
        shelves.add(warehouse[2]);
        return shelves;
    }

    public List<DevSlot> getDevSlots(){
        List<DevSlot> slots = new ArrayList<>();
        slots.add(devSlots[0]);
        slots.add(devSlots[1]);
        slots.add(devSlots[2]);
        return slots;
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
//todo Ask to omer if he think we can maintain both or what
    public Resources getRemainingCostFromWarehouse(Resources res) {
        List<Resources.ResType> resTypeList = new ArrayList<>();
        Resources remainingCost = new Resources();
        int numOfRemainingCost;
        resTypeList.addAll(res.getResTypes());
        for (int i = 0; i < 3; i++) {
            for (Resources.ResType resType : resTypeList) {
                if (warehouse[i].getShelfResType().equals(resType)) {
                    int number = res.getNumberOfType(resType);
                    if(warehouse[i].getNumberOfElements()< number) {
                        numOfRemainingCost = number - warehouse[i].getNumberOfElements();
                        remainingCost.add(resType, numOfRemainingCost);
                        number -= warehouse[i].getNumberOfElements();
                    }
                    warehouse[i].removeFromShelf(number);

                }
            }
        }
        return remainingCost;
    }

    public void addToStrongBox(Resources res) {
        this.strongbox.add(res);
    }

    public void subtractFromStrongbox(Resources res) {
        this.strongbox.subtract(res);
    }

    public void subtractFromExtraSlot(Resources res){
        for(LeaderCard card: activeLeaderCards){
            if(card.getAbility().getAbilityType() == SpecialAbility.AbilityType.EXTRASLOT){
                card.getAbility().subtractFromExtraSlot(res);
            }
        }
    }

    public void setStrongbox(Resources strongbox) {
        this.strongbox = strongbox;
    }

    public void setGame(Game game){
        this.game = game;
    }
}
