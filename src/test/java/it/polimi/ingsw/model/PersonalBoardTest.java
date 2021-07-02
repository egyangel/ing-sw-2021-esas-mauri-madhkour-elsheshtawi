package it.polimi.ingsw.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PersonalBoardTest {
    private Integer userID;
    private Game game;
    private int victoryPoints = 0;
    private DevSlot[] devSlots = new DevSlot[3];
    private Shelf[] warehouse = new Shelf[3];
    private Resources strongbox;

    private int faithPoints = 0;
    private List<LeaderCard> inactiveLeaderCards;
    private List<LeaderCard> activeLeaderCards;
    private Map<PersonalBoard.PopeArea, Boolean> popeAreaMap;
    private List<DevCard> ownedDevCards ;
    PersonalBoard personalBoard;

    @BeforeEach
    void setUp() {
        game= new Game();
        personalBoard = new PersonalBoard(1);
        this.userID = 1;
        devSlots[0] = new DevSlot(DevSlot.slotPlace.LEFT);
        devSlots[1] = new DevSlot(DevSlot.slotPlace.CENTER);
        devSlots[2] = new DevSlot(DevSlot.slotPlace.RIGHT);
       /* warehouse[0] = new Shelf(Shelf.shelfPlace.TOP);
        warehouse[1] = new Shelf(Shelf.shelfPlace.MIDDLE);
        warehouse[2] = new Shelf(Shelf.shelfPlace.BOTTOM);*/
        strongbox = new Resources();
        ownedDevCards = new ArrayList<>();
        popeAreaMap = new HashMap<>();
        inactiveLeaderCards = new ArrayList<>();
        activeLeaderCards = new ArrayList<>();

    }

    @AfterEach
    void tearDown() {
        this.userID = 0;
        devSlots[0].clear();
        devSlots[1].clear();
        devSlots[2].clear();
        warehouse[0] = null;
        warehouse[1] = null;
        warehouse[2] = null;
        strongbox.clear();
        popeAreaMap.clear();
        inactiveLeaderCards = null;
        activeLeaderCards = null;
        personalBoard = null;
        ownedDevCards.clear();

    }

    @Test
    void putToWarehouseWithoutCheck() {
    }

    @Test
    void putToWarehouse() {
        Resources res = new Resources(Resources.ResType.STONE,5);
        Resources res2 = new Resources(0,2,3,4);
        assertEquals(2,personalBoard.putToWarehouse(Shelf.shelfPlace.BOTTOM,res));
        assertEquals(-1,personalBoard.putToWarehouse(Shelf.shelfPlace.BOTTOM,res2));
    }

    @Test
    void getOwnedCards() {
        Resources  Lhs = new Resources(Resources.ResType.STONE,2);
        Resources Rhs= new Resources(Resources.ResType.SERVANT,1);
        Resources cost= new Resources(Resources.ResType.STONE,2);
        DevCard d1= new DevCard(1, DevCard.CardColor.BLUE,Lhs,Rhs, cost,10);
        assertEquals(0,personalBoard.getOwnedCards().size());
        personalBoard.putDevCardOnSlot(d1,DevSlot.slotPlace.LEFT);
        assertEquals(1,personalBoard.getOwnedCards().size());
        assertEquals(d1,personalBoard.getOwnedCards().get(0));

    }

    @Test
    void countVictoryPoints() {
        assertEquals(0,personalBoard.getVictoryPoints());
        personalBoard.countVictoryPoints(10);
        assertEquals(10,personalBoard.getVictoryPoints());
    }

    @Test
    void getVictoryPoints() {
        assertEquals(0,personalBoard.getVictoryPoints());
        personalBoard.countVictoryPoints(10);
        assertEquals(10,personalBoard.getVictoryPoints());
    }

    @Test
    void getPopeAreaMap() {
        assertNotNull(personalBoard.getPopeAreaMap());
        assertEquals(3,personalBoard.getPopeAreaMap().size());
    }


    @Test
    void giveVaticanReport() {
        personalBoard.setFaitPoint(7);
        assertFalse(personalBoard.getPopeAreaMap().get(PersonalBoard.PopeArea.FIRST));
        personalBoard.giveVaticanReport(PersonalBoard.PopeArea.FIRST);
        assertTrue(personalBoard.getPopeAreaMap().get(PersonalBoard.PopeArea.FIRST));

    }

    @Test
    void getTurnPopeFavorTile() {
        personalBoard.setFaitPoint(7);
       // assertEquals(0,personalBoard.getTurnPopeFavorTile());
        personalBoard.giveVaticanReport(PersonalBoard.PopeArea.FIRST);
        assertTrue(personalBoard.getPopeAreaMap().get(PersonalBoard.PopeArea.FIRST));
       // assertEquals(2,personalBoard.getTurnPopeFavorTile());
    }


    @Test
    void getInactiveLeaderCards() {
        Requirement requirement = new Requirement(Requirement.reqType.TWOCARD, DevCard.CardColor.YELLOW, DevCard.CardColor.GREEN);
        int victoryPoint=5;
        SpecialAbility ability = new SpecialAbility(SpecialAbility.AbilityType.DISCOUNT,Resources.ResType.SERVANT);
        Requirement requirement1 = new Requirement(Requirement.reqType.THREECARD, DevCard.CardColor.GREEN, DevCard.CardColor.PURPLE );
        int victoryPoint1=4;
        SpecialAbility ability1 = new SpecialAbility(SpecialAbility.AbilityType.CONVERTWHITE,Resources.ResType.SERVANT);

        LeaderCard card = new LeaderCard(requirement1,victoryPoint1,ability1);
        LeaderCard card2 = new LeaderCard(requirement,victoryPoint,ability);
        List<LeaderCard> list = new ArrayList<>();
        list.add(card);
        list.add(card2);
        personalBoard.setInactiveLeaderCards(list);
        assertFalse(personalBoard.getInactiveLeaderCards().isEmpty());
        assertEquals(2,personalBoard.getInactiveLeaderCards().size());
        assertEquals(card,personalBoard.getInactiveLeaderCards().get(0));

    }

    @Test
    void getActiveLeaderCards() {
        Requirement requirement = new Requirement(Requirement.reqType.TWOCARD, DevCard.CardColor.YELLOW, DevCard.CardColor.GREEN);
        int victoryPoint=5;
        SpecialAbility ability = new SpecialAbility(SpecialAbility.AbilityType.DISCOUNT,Resources.ResType.SERVANT);
        Requirement requirement1 = new Requirement(Requirement.reqType.THREECARD, DevCard.CardColor.GREEN, DevCard.CardColor.PURPLE );
        int victoryPoint1=4;
        SpecialAbility ability1 = new SpecialAbility(SpecialAbility.AbilityType.CONVERTWHITE,Resources.ResType.SERVANT);

        LeaderCard card = new LeaderCard(requirement1,victoryPoint1,ability1);
        LeaderCard card2 = new LeaderCard(requirement,victoryPoint,ability);
        List<LeaderCard> list = new ArrayList<>();
        list.add(card);
        list.add(card2);
        personalBoard.setActiveLeaderCards(list);
        assertFalse(personalBoard.getActiveLeaderCards().isEmpty());
        assertEquals(2,personalBoard.getActiveLeaderCards().size());
        assertEquals(card,personalBoard.getActiveLeaderCards().get(0));

    }

    @Test
    void setInactiveLeaderCards() {
        Requirement requirement = new Requirement(Requirement.reqType.TWOCARD, DevCard.CardColor.YELLOW, DevCard.CardColor.GREEN);
        int victoryPoint=5;
        SpecialAbility ability = new SpecialAbility(SpecialAbility.AbilityType.DISCOUNT,Resources.ResType.SERVANT);
        Requirement requirement1 = new Requirement(Requirement.reqType.THREECARD, DevCard.CardColor.GREEN, DevCard.CardColor.PURPLE );
        int victoryPoint1=4;
        SpecialAbility ability1 = new SpecialAbility(SpecialAbility.AbilityType.CONVERTWHITE,Resources.ResType.SERVANT);

        LeaderCard card = new LeaderCard(requirement1,victoryPoint1,ability1);
        LeaderCard card2 = new LeaderCard(requirement,victoryPoint,ability);
        List<LeaderCard> list = new ArrayList<>();
        list.add(card);
        list.add(card2);
        assertTrue(personalBoard.getInactiveLeaderCards().isEmpty());
        personalBoard.setInactiveLeaderCards(list);
        assertFalse(personalBoard.getInactiveLeaderCards().isEmpty());
        assertEquals(2,personalBoard.getInactiveLeaderCards().size());
        assertEquals(card,personalBoard.getInactiveLeaderCards().get(0));
    }

    @Test
    void setActiveLeaderCards() {
        Requirement requirement = new Requirement(Requirement.reqType.TWOCARD, DevCard.CardColor.YELLOW, DevCard.CardColor.GREEN);
        int victoryPoint=5;
        SpecialAbility ability = new SpecialAbility(SpecialAbility.AbilityType.DISCOUNT,Resources.ResType.SERVANT);
        Requirement requirement1 = new Requirement(Requirement.reqType.THREECARD, DevCard.CardColor.GREEN, DevCard.CardColor.PURPLE );
        int victoryPoint1=4;
        SpecialAbility ability1 = new SpecialAbility(SpecialAbility.AbilityType.CONVERTWHITE,Resources.ResType.SERVANT);

        LeaderCard card = new LeaderCard(requirement1,victoryPoint1,ability1);
        LeaderCard card2 = new LeaderCard(requirement,victoryPoint,ability);
        List<LeaderCard> list = new ArrayList<>();
        list.add(card);
        list.add(card2);
        assertTrue(personalBoard.getActiveLeaderCards().isEmpty());
        personalBoard.setActiveLeaderCards(list);
        assertFalse(personalBoard.getActiveLeaderCards().isEmpty());
        assertEquals(2,personalBoard.getActiveLeaderCards().size());
        assertEquals(card,personalBoard.getActiveLeaderCards().get(0));
    }

    @Test
    void getTotalResources() {
        Resources  warehouse = new Resources(Resources.ResType.STONE,2);
        Resources strong= new Resources(Resources.ResType.SERVANT,15);
        assertTrue(personalBoard.getTotalResources().isEmpty());
        personalBoard.setStrongbox(strong);
        personalBoard.putToWarehouse(Shelf.shelfPlace.MIDDLE,warehouse);
        Resources temp= new Resources();
        temp.add(warehouse);
        temp.add(strong);
        assertFalse(personalBoard.getTotalResources().isEmpty());
        assertEquals(temp.sumOfValues(),personalBoard.getTotalResources().sumOfValues());
        assertEquals(temp.getResTypes(),personalBoard.getTotalResources().getResTypes());
    }

    @Test
    void getStrongboxResources() {

        Resources strong= new Resources(Resources.ResType.STONE,15);
        personalBoard.setStrongbox(strong);

        assertFalse(personalBoard.getTotalResources().isEmpty());
        assertEquals(15,personalBoard.getTotalResources().sumOfValues());
        assertEquals(Resources.ResType.STONE,personalBoard.getStrongboxResources().getResTypes().get(0));
        strong.add(Resources.ResType.SERVANT,8);
        personalBoard.setStrongbox(strong);
        assertEquals(23,personalBoard.getTotalResources().sumOfValues());

    }

    @Test
    void getWarehouseResources() {
        Resources  warehouse = new Resources(Resources.ResType.STONE,2);
        Resources  warehouse2 = new Resources(Resources.ResType.SERVANT,3);
        personalBoard.putToWarehouse(Shelf.shelfPlace.MIDDLE,warehouse);
        assertEquals(2,personalBoard.getWarehouseResources().sumOfValues());
        assertEquals(Resources.ResType.STONE,personalBoard.getWarehouseResources().getResTypes().get(0));
        personalBoard.putToWarehouse(Shelf.shelfPlace.BOTTOM,warehouse2);
        assertEquals(5,personalBoard.getWarehouseResources().sumOfValues());

    }


    @Test
    void isThereEnoughRes() {
        Requirement requirement = new Requirement(Requirement.reqType.TWOCARD, DevCard.CardColor.YELLOW, DevCard.CardColor.GREEN);
        int victoryPoint=5;
        SpecialAbility ability = new SpecialAbility(SpecialAbility.AbilityType.DISCOUNT,Resources.ResType.SERVANT);

        LeaderCard card2 = new LeaderCard(requirement,victoryPoint,ability);
        List<LeaderCard> list = new ArrayList<>();
        list.add(card2);

        Resources  warehouse = new Resources(Resources.ResType.STONE,2);
        Resources strong= new Resources(Resources.ResType.SERVANT,15);
        personalBoard.setStrongbox(strong);
        personalBoard.putToWarehouse(Shelf.shelfPlace.MIDDLE,warehouse);

        Resources Lhs = new Resources(Resources.ResType.STONE, 2);
        Resources Rhs = new Resources(Resources.ResType.SERVANT, 1);
        Resources cost = new Resources(Resources.ResType.STONE, 5);
        DevCard d1 = new DevCard(1, DevCard.CardColor.BLUE, Lhs, Rhs, cost, 10);
        assertFalse(personalBoard.isThereEnoughRes(d1));
        personalBoard.setStrongbox(new Resources(Resources.ResType.STONE,4));
        assertTrue(personalBoard.isThereEnoughRes(d1));


    }

    @Test
    void isCardSuitableForSlots() {
        Resources Lhs = new Resources(Resources.ResType.STONE, 2);
        Resources Rhs = new Resources(Resources.ResType.SERVANT, 1);
        Resources cost = new Resources(Resources.ResType.STONE, 5);
        DevCard d1 = new DevCard(1, DevCard.CardColor.BLUE, Lhs, Rhs, cost, 10);
        DevCard d2 = new DevCard(1, DevCard.CardColor.GREEN, Lhs, Rhs, cost, 10);
        DevCard d3 = new DevCard(2, DevCard.CardColor.BLUE, Lhs, Rhs, cost, 10);

        assertTrue(personalBoard.isCardSuitableForSlots(d1));
        personalBoard.putDevCardOnSlot(d1,DevSlot.slotPlace.LEFT);
        personalBoard.putDevCardOnSlot(d1,DevSlot.slotPlace.CENTER);
        personalBoard.putDevCardOnSlot(d1,DevSlot.slotPlace.RIGHT);
        assertFalse(personalBoard.isCardSuitableForSlots(d2));
        assertTrue(personalBoard.isCardSuitableForSlots(d3));

    }

    @Test
    void getSuitablePlaces() {
        Resources Lhs = new Resources(Resources.ResType.STONE, 2);
        Resources Rhs = new Resources(Resources.ResType.SERVANT, 1);
        Resources cost = new Resources(Resources.ResType.STONE, 5);
        DevCard d1 = new DevCard(1, DevCard.CardColor.BLUE, Lhs, Rhs, cost, 10);
        List<DevSlot.slotPlace> places = new ArrayList<>();
        places.add(DevSlot.slotPlace.LEFT);
        places.add(DevSlot.slotPlace.CENTER);
        places.add(DevSlot.slotPlace.RIGHT);
        assertEquals(places,personalBoard.getSuitablePlaces(d1));
        personalBoard.putDevCardOnSlot(d1,DevSlot.slotPlace.RIGHT);

        assertNotEquals(places,personalBoard.getSuitablePlaces(d1));
        places.remove(2);
        assertEquals(places,personalBoard.getSuitablePlaces(d1));
    }

    @Test
    void putDevCardOnSlot() {
        Resources Lhs = new Resources(Resources.ResType.STONE, 2);
        Resources Rhs = new Resources(Resources.ResType.SERVANT, 1);
        Resources cost = new Resources(Resources.ResType.STONE, 5);
        DevCard d1 = new DevCard(1, DevCard.CardColor.BLUE, Lhs, Rhs, cost, 10);
        assertNull(personalBoard.getDevCardOnSlot(DevSlot.slotPlace.LEFT));
        personalBoard.putDevCardOnSlot(d1,DevSlot.slotPlace.LEFT);
        assertEquals(d1,personalBoard.getDevCardOnSlot(DevSlot.slotPlace.LEFT));
    }

    @Test
    void getDevCardOnSlot() {
        Resources Lhs = new Resources(Resources.ResType.STONE, 2);
        Resources Rhs = new Resources(Resources.ResType.SERVANT, 1);
        Resources cost = new Resources(Resources.ResType.STONE, 5);
        DevCard d1 = new DevCard(1, DevCard.CardColor.BLUE, Lhs, Rhs, cost, 10);
        assertNull(personalBoard.getDevCardOnSlot(DevSlot.slotPlace.LEFT));
        personalBoard.putDevCardOnSlot(d1,DevSlot.slotPlace.LEFT);
        assertEquals(d1,personalBoard.getDevCardOnSlot(DevSlot.slotPlace.LEFT));
    }

    @Test
    void clearShelf() {
        Resources  warehouse = new Resources(Resources.ResType.STONE,2);
        Resources  warehouse2 = new Resources(Resources.ResType.SERVANT,3);
        personalBoard.putToWarehouse(Shelf.shelfPlace.MIDDLE,warehouse);
        personalBoard.putToWarehouse(Shelf.shelfPlace.BOTTOM,warehouse2);
        assertEquals(5,personalBoard.getWarehouseResources().sumOfValues());
        personalBoard.clearShelf(Shelf.shelfPlace.MIDDLE);
        assertEquals(3,personalBoard.getWarehouseResources().sumOfValues());

    }

    @Test
    void swapShelves() {
        Shelf.shelfPlace[] places = new Shelf.shelfPlace[2];
        places[0] = Shelf.shelfPlace.MIDDLE;
        places[1] = Shelf.shelfPlace.BOTTOM;
        Resources  warehouse = new Resources(Resources.ResType.STONE,2);
        Resources  warehouse2 = new Resources(Resources.ResType.SERVANT,3);
        assertEquals(0,personalBoard.swapShelves(places).sumOfValues());

        personalBoard.putToWarehouse(Shelf.shelfPlace.MIDDLE,warehouse);
        personalBoard.putToWarehouse(Shelf.shelfPlace.BOTTOM,warehouse2);

        
        assertEquals(1,personalBoard.swapShelves(places).sumOfValues());
    }

    @Test
    void getShelves() {
        assertFalse(personalBoard.getShelves().isEmpty());
        assertEquals(3, personalBoard.getShelves().size());
    }

    @Test
    void getDevSlots() {
        assertFalse(personalBoard.getDevSlots().isEmpty());
        assertEquals(3, personalBoard.getShelves().size());

    }

    @Test
    void subtractFromWarehouse() {
        Resources resToSub = new Resources();
        Resources  warehouse = new Resources(Resources.ResType.STONE,2);
        Resources  warehouse2 = new Resources(Resources.ResType.SERVANT,3);
        personalBoard.putToWarehouse(Shelf.shelfPlace.MIDDLE,warehouse);
        personalBoard.putToWarehouse(Shelf.shelfPlace.BOTTOM,warehouse2);
        assertEquals(5,personalBoard.getWarehouseResources().sumOfValues());
        resToSub.add(Resources.ResType.SERVANT,1);
        personalBoard.subtractFromWarehouse(resToSub);
        assertEquals(4,personalBoard.getWarehouseResources().sumOfValues());
        assertEquals(2,personalBoard.getWarehouseResources().getNumberOfType(Resources.ResType.SERVANT));
        resToSub.clear();
        resToSub.add(Resources.ResType.STONE,2);
        personalBoard.subtractFromWarehouse(resToSub);
        assertEquals(2,personalBoard.getWarehouseResources().sumOfValues());
        assertEquals(0,personalBoard.getWarehouseResources().getNumberOfType(Resources.ResType.STONE));

    }

    @Test
    void addToStrongBox() {
        Resources  strongb = new Resources(Resources.ResType.STONE,10);
        Resources  strongb2 = new Resources(Resources.ResType.SERVANT,5);
        personalBoard.addToStrongBox(strongb);

        assertEquals(10,personalBoard.getStrongboxResources().sumOfValues());
        personalBoard.addToStrongBox(strongb2);

        assertEquals(15,personalBoard.getStrongboxResources().sumOfValues());
        assertEquals(10,personalBoard.getStrongboxResources().getNumberOfType(Resources.ResType.STONE));
        assertEquals(5,personalBoard.getStrongboxResources().getNumberOfType(Resources.ResType.SERVANT));

    }

    @Test
    void subtractFromStrongbox() {
        Resources resToSub = new Resources();
        Resources  strongb = new Resources(Resources.ResType.STONE,10);
        Resources  strongb2 = new Resources(Resources.ResType.SERVANT,5);
        personalBoard.addToStrongBox(strongb);
        personalBoard.addToStrongBox(strongb2);

        assertEquals(15,personalBoard.getStrongboxResources().sumOfValues());
        resToSub.add(Resources.ResType.SERVANT,2);
        resToSub.add(Resources.ResType.STONE,5);
        personalBoard.subtractFromStrongbox(resToSub);

        assertEquals(8,personalBoard.getStrongboxResources().sumOfValues());

        assertEquals(3,personalBoard.getStrongboxResources().getNumberOfType(Resources.ResType.SERVANT));
        assertEquals(5,personalBoard.getStrongboxResources().getNumberOfType(Resources.ResType.STONE));
    }

    @Test
    void subtractFromExtraSlot() {
        Resources resAddHolder = new Resources(Resources.ResType.SERVANT,2);
        Requirement requirement1 = new Requirement(Requirement.reqType.THREECARD, DevCard.CardColor.GREEN, DevCard.CardColor.PURPLE );
        int victoryPoint1=4;
        SpecialAbility ability1 = new SpecialAbility(SpecialAbility.AbilityType.EXTRASLOT,Resources.ResType.SERVANT);
        ability1.addToHolder(resAddHolder);
        LeaderCard card = new LeaderCard(requirement1,victoryPoint1,ability1);

        List<LeaderCard> list = new ArrayList<>();
        list.add(card);
        Resources  resToSub = new Resources(Resources.ResType.STONE,2);
        
        personalBoard.subtractFromExtraSlot(resToSub);

        assertEquals(0,personalBoard.getExtraSlotResources().sumOfValues());
        resToSub.add(Resources.ResType.SERVANT,2);

        personalBoard.subtractFromExtraSlot(resToSub);
        assertEquals(0,personalBoard.getExtraSlotResources().sumOfValues());

    }

    @Test
    void setStrongbox() {

        Resources  strongb = new Resources(Resources.ResType.STONE,10);
        strongb.add(Resources.ResType.SERVANT,5);
        assertEquals(0,personalBoard.getStrongboxResources().sumOfValues());
        personalBoard.setStrongbox(strongb);

        assertEquals(15,personalBoard.getStrongboxResources().sumOfValues());
    }

    @Test
    void getVP() {
    }
}