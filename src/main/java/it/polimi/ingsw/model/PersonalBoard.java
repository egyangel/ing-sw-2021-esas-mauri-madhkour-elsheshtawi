package it.polimi.ingsw.model;

import java.util.*;

/**
 * PersonalBoard  class , it is the abstraction of the Player PersonalBoard inside the game
 * It contains all the methods to interact with resources/card
 * @author
 * */
public class PersonalBoard {



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
    private List<LeaderCard> inactiveLeaderCards;
    private List<LeaderCard> activeLeaderCards;
    private Map<PopeArea, Boolean> popeAreaMap;
    private int blackCrossToken;

    private List<DevCard> ownedDevCards = new ArrayList<>();

    /**
     * Constructor that instantiate the devSlot,warehouse , strongbox /leader card and all the necessary var for the player
     * @param userID the id of the player associated to the board
     * */

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
        inactiveLeaderCards = new ArrayList<>();
        activeLeaderCards = new ArrayList<>();
        popeAreaMap.put(PopeArea.FIRST, false);
        popeAreaMap.put(PopeArea.SECOND, false);
        popeAreaMap.put(PopeArea.THIRD, false);
    }

    /**
     * Constructor that instantiate the solo mode game
     * @param userID the id of the player associated to the board
     * @param solo boolean attribute for solo mode game
     * */
    public PersonalBoard(Integer userID, boolean solo) {
        this(userID);
        if (solo) {
            this.blackCrossToken = 0;


        }
    }
    public int getBlackCrossToken() {
        return blackCrossToken;
    }

    public void setBlackCrossToken(int blackCrossToken) {
        this.blackCrossToken = blackCrossToken;
    }

    //     for now, this considers strongbox only, the code will need to be improved.
//     in case there is not enough resource, some error/exception must be included,
//     or assertions before execution/during testing
    public void putToWarehouseWithoutCheck(Resources res) {
        for (Resources.ResType resType : res.getResTypes()) {
            putFromTop(resType, res.getNumberOfType(resType));
        }
    }

    /**
     * method that put the resources that the player achieve in the action in the warehouse
     * @param place represent where the player put the res
     * @param res  represent the resources that has to be put inside
     * @return  the number of res that exceeded the shelf size
     * */
    public int putToWarehouse(Shelf.shelfPlace place, Resources res) {
        return warehouse[place.getIndexInWarehouse()].putResource(res);
    }

    /**
     * method that put the resources that the player achieve in the action in the warehouse
     * it check start from the top which shelf can be used
     * @param resType represent the type of the resource
     * @param size the number of that res
     * @return  false you cannot put any res because shelves is full
     */
    private boolean putFromTop(Resources.ResType resType, int size) {
        if (checkShelfBySizeAndType(Shelf.shelfPlace.TOP, size, resType)) {
            warehouse[0].putResource(resType, size);
        } else if (checkShelfBySizeAndType(Shelf.shelfPlace.MIDDLE, size, resType)) {
            warehouse[1].putResource(resType, size);
        } else if (checkShelfBySizeAndType(Shelf.shelfPlace.BOTTOM, size, resType)) {
            warehouse[2].putResource(resType, size);
        } else return false;
        return true;
    }

    /**
     * method that check if you can put the res in the shelf
     * @param place represent where the player put the res
     * @param size the number of that res
     * @param type represent the type of the resource
     * @return  false you cannot put any res because shelves is full/or different type
     */
    private boolean checkShelfBySizeAndType(Shelf.shelfPlace place, int size, Resources.ResType type){
        if (!checkEnoughSize(place, size)) return false;
        else {
            if (checkEmptyShelf(place)) return true;
            else return checkSameType(place, type);
        }
    }

    /**
     * method that check if you can put the res in the shelf
     * @param place represent where the player put the res
     * @param size the number of that res
     * @return  false you cannot put any res because shelves is full
     */
    private boolean checkEnoughSize(Shelf.shelfPlace place, int size) {
        int index = place.getIndexInWarehouse();
        return ((warehouse[index].getNumberOfElements()) + size <= warehouse[index].getShelfSize());
    }

    /**
     * method that check if you can put the res in the shelf
     * @param place represent where the player put the res
     * @param type represent the type of the resource
     * @return  false you cannot put  res because shelves has different type
     */
    public boolean checkSameType(Shelf.shelfPlace place, Resources.ResType type) {
        int index = place.getIndexInWarehouse();
        try {
            return (warehouse[index].getShelfResType() == type);
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * method that check if the shelf is empty
     * @param place represent where the player put the res
     * @return  false shelf is not empty
     */
    public boolean checkEmptyShelf(Shelf.shelfPlace place) {
        return warehouse[place.getIndexInWarehouse()].isEmpty();
    }

    /**
     * method that get the list of Development card owned by the player
     * @return list of player's Development Card
     */
    public  List<DevCard> getOwnedCards() {
        return this.ownedDevCards;
    }

    public void countVictoryPoints(int victoryPoints) {
        this.victoryPoints+=victoryPoints;
    }

    public  int getVictoryPoints() {
        return this.victoryPoints;
    }

    /**
     * method that increase the faith point and in case trigger a vatican report
     * @param toAdd number of faith point gained to add to the current faithpoint
     */
    public void increaseFaitPoint(int toAdd) {
        int oldFaithPoint = faithPoints;
        faithPoints += toAdd;
        if (faithPoints > 24) faithPoints = 24;
        if (oldFaithPoint < 8 && faithPoints>=8){
            game.triggerVaticanReport(PopeArea.FIRST);
        } else if (oldFaithPoint < 16 && faithPoints>=16){
            game.triggerVaticanReport(PopeArea.SECOND);
        } else if (oldFaithPoint < 24 && faithPoints == 24){
            game.triggerVaticanReport(PopeArea.THIRD);
        }
    }
    //methods only for testing
    public void setFaitPoint(int toAdd) {
        faithPoints += toAdd;
    }

    /**
     * method that give the pope area
     * @return  a map of the pope area and if they are true or not
     */
    public Map<PopeArea, Boolean> getPopeAreaMap(){
        return popeAreaMap;
    }

    /**
     * method that give the player's faith point
     * @return  the position on faith tracka
     */
    public int getFaithPoints(){
        return faithPoints;
    }

    /**
     * method that check the position on the faith track and when a vatican report
     * is active and the player is inside the window of report it turn on Pope’s Favor tile
     * @param area it is the current Pope area
     */
    public void giveVaticanReport(PopeArea area) {
        if (area == PopeArea.FIRST && faithPoints >= 5) {
            turnPopeFavorTile(PopeArea.FIRST);
        } else if (area == PopeArea.SECOND && faithPoints >= 12) {
            turnPopeFavorTile(PopeArea.SECOND);
        } else if (area == PopeArea.THIRD && faithPoints >= 19) {
            turnPopeFavorTile(PopeArea.THIRD);
        }
    }

    /**
     * method that set true the Pope’s Favor tile
     * @param area it is the current Pope area
     */
    private void turnPopeFavorTile(PopeArea area) {
        popeAreaMap.replace(area, Boolean.TRUE);
    }

    /**
     * method that get the list of inactive leaderCard owned by the player
     * @return list of player's inactive leaderCard
     */
    public List<LeaderCard> getInactiveLeaderCards() {
        return this.inactiveLeaderCards;
    }


    /**
     * method that get the list of active leaderCard owned by the player
     * @return list of player's active leaderCard
     */
    public List<LeaderCard> getActiveLeaderCards() {
        return this.activeLeaderCards;
    }


    //both methods are used in testing
    public void setInactiveLeaderCards(List<LeaderCard> inactiveLeaderCards) {
        this.inactiveLeaderCards = inactiveLeaderCards;
    }


    public void setActiveLeaderCards(List<LeaderCard> activeLeaderCards) {
        this.activeLeaderCards = activeLeaderCards;
    }

    /**
     * method that compute the total resources owned by the player
     * @return  total player's resources
     */
    public Resources getTotalResources() {
        Resources res = new Resources();
        res.add(getWarehouseResources());
        res.add(getStrongboxResources());
        res.add(getExtraSlotResources());
        return res;
    }

    /**
     * method that compute the resources owned by the player on Strongbox
     * @return  player's resources on Strongbox
     */
    public Resources getStrongboxResources() {
        Resources res = new Resources();
        res.add(strongbox);
        return res;
    }

    /**
     * method that compute the resources owned by the player on Warehouse
     * @return  player's resources on Warehouse
     */
    public Resources getWarehouseResources() {
        Resources res = new Resources();
        for (Shelf shelf : warehouse) {
            res.add(shelf.getResource());
        }
        return res;
    }

    /**
     * method that compute the resources owned by the player on leader card with ability ExtraSlot
     * @return  player's resources  on leader card with ability ExtraSlot
     */
    public Resources getExtraSlotResources(){
        Resources res = new Resources();
        for(LeaderCard card:activeLeaderCards){
            if(card.getAbility().getAbilityType() == SpecialAbility.AbilityType.EXTRASLOT){
                res.add(card.getAbility().getResourcesAtSlot());
            }
        }
        return res;
    }

    /**
     * method that check if there are enough resources to purchase the development card
     * @param card development card that have to be checked
     * @return  true if there are resource to purchase the card
     */
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

    /**
     * method that check if the card is suitable in one slot following the rule game
     * @param card development card that have to be inserted on top of one suitable slot
     * @return  true if there are a slot where the player can put the card
     */
    public boolean isCardSuitableForSlots(DevCard card) {
        int level = card.getLevel();
        for (DevSlot devSlot : devSlots) {
            if (level == devSlot.getLevelOfTopCard() + 1) return true;
        }
        return false;
    }

    /**
     * method that check if there are a slotPlace available
     * @param card development card that have to be inserted on top of one suitable slot
     * @return  list of suitable slots where the player can put the card
     */
    public List<DevSlot.slotPlace> getSuitablePlaces(DevCard card) {
        int level = card.getLevel();
        List<DevSlot.slotPlace> places = new ArrayList<>();
        for (DevSlot devSlot : devSlots) {
            if (level == devSlot.getLevelOfTopCard() + 1) places.add(devSlot.getPlace());
        }
        return places;
    }

    /**
     * method that put a card on the slot
     * @param card development card that have to be inserted on top of one suitable slot
     * @param place slot where the player choose to put the card
     */
    public void putDevCardOnSlot(DevCard card, DevSlot.slotPlace place) {
        int index = place.getIndexInBoard();
        devSlots[index].putDevCard(card);
        this.ownedDevCards.add(card);
    }

    /**
     * method that give the top card on a the slot
     * @param place slot chooses from the player
     * return the card that is on the top of that slotPlace
     * */
    public DevCard getDevCardOnSlot(DevSlot.slotPlace place) {
        int index = place.getIndexInBoard();
        if(devSlots[index].isEmpty()){
            return null;
        }
        return devSlots[index].getTopDevCard();
    }

    /**
     * method that discard a chosen shelf
     * @param place shelf chooses by the player
     * return the number of discarded resources.
     * */
    public int clearShelf(Shelf.shelfPlace place) {
        return warehouse[place.getIndexInWarehouse()].clearShelf();
    }

    /**
     * method that swap the resources inside two shelf
     * @param places list of the two shelf chosen by the player to swap
     * return the number of discarded resources.
     * */
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

    /**
     * method that remove resources from warehouse
     * @param res resources that have to be removed
     *
     * */
    public void subtractFromWarehouse(Resources res) {
        List<Resources.ResType> resTypeList = new ArrayList<>();

        resTypeList.addAll(res.getResTypes());
        for (int i = 0; i < 3; i++) {
            for (Resources.ResType resType : resTypeList) {
                if (warehouse[i].getShelfResType()!= null && warehouse[i].getShelfResType().equals(resType)) {
                    int number = res.getNumberOfType(resType);
                    warehouse[i].removeFromShelf(number);
                }
            }
        }
    }

    /**
     * method that add resources into strongbox
     * @param res resources that have to be added in the strongbox
     *
     * */
    public void addToStrongBox(Resources res) {
        this.strongbox.add(res);
    }

    /**
     * method that remove resources from strongbox
     * @param res resources that have to be removed
     *
     * */
    public void subtractFromStrongbox(Resources res) {
        this.strongbox.subtract(res);
    }

    /**
     * method that remove resources from  leader card with ability exstra slot
     * @param res resources that have to be removed
     * */
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

    /**
     * method that compute the total victory point of the player
     * @return the Victory point
     * */
    public int getVP(){
        int VP = 0;
        VP += getVPFromDevCards();
        VP += getVPfromFaithTrack();
        VP += getVPfromActiveLeaders();
        VP += getVPfromAllResources();
        return VP;
    }
    /**
     * method that compute the total victory point gained from the development card
     * @return the Victory point
     * */
    private int getVPFromDevCards(){
        int VP = 0;
        for(DevCard card: ownedDevCards){
            VP += card.getVictoryPoints();
        }
        return VP;
    }

    /**
     * method that compute the total victory point gained from  FaithTrack
     * @return the Victory point
     * */
    private int getVPfromFaithTrack(){
        int VP = 0;
        if ((0 <= faithPoints) && (faithPoints < 3))
            VP =  0;
        else if ((3 <= faithPoints) && (faithPoints < 6))
            VP = 1;
        else if ((6 <= faithPoints) && (faithPoints < 9))
            VP = 2;
        else if ((9 <= faithPoints) && (faithPoints < 12))
            VP = 4;
        else if ((12 <= faithPoints) && (faithPoints < 15))
            VP = 6;
        else if ((15 <= faithPoints) && (faithPoints < 18))
            VP = 9;
        else if ((18 <= faithPoints) && (faithPoints < 21))
            VP = 12;
        else if ((21 <= faithPoints) && (faithPoints < 24))
            VP = 16;
        else if ((24 <= faithPoints))
            VP = 20;

        if(popeAreaMap.get(PopeArea.FIRST)) VP += 2;
        if(popeAreaMap.get(PopeArea.SECOND)) VP += 3;
        if(popeAreaMap.get(PopeArea.THIRD)) VP += 4;

        return VP;
    }

    /**
     * method that compute the total victory point gained from  ActiveLeaders
     * @return the Victory point
     * */
    private int getVPfromActiveLeaders(){
        int VP = 0;
        for(LeaderCard card: activeLeaderCards){
            VP += card.getVictoryPoints();
        }
        return VP;
    }

    /**
     * method that compute the total victory point gained from all Resources owned by the player
     * @return the Victory point
     * */
    private int getVPfromAllResources(){
        Resources res = getTotalResources();
        int VP = 0;
        VP += Integer.valueOf(res.sumOfValues()/5);
        return VP;
    }

    public boolean isTypePutInAnotherShelf(Shelf.shelfPlace place, Resources.ResType resType){
        Integer index = place.getIndexInWarehouse();
        List<Integer> indexList = new ArrayList<>();
        indexList.add(0);
        indexList.add(1);
        indexList.add(2);
        indexList.remove(index);
        List<Resources.ResType> resTypeList = new ArrayList<>();
        for(Integer otherIndex: indexList){
            if(!warehouse[otherIndex].isEmpty()) resTypeList.add(warehouse[otherIndex].getShelfResType());
        }
        return resTypeList.contains(resType);
    }
}

