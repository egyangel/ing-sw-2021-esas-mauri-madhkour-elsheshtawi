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
        warehouse[0] = new Shelf(Shelf.shelfPlace.TOP);
        warehouse[1] = new Shelf(Shelf.shelfPlace.MIDDLE);
        warehouse[2] = new Shelf(Shelf.shelfPlace.BOTTOM);
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
        DevCard d2= new DevCard(2, DevCard.CardColor.GREEN,Lhs,Rhs, cost,5);
        DevCard d3= new DevCard(3, DevCard.CardColor.YELLOW,Lhs,Rhs, cost,2);
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
    }

    @Test
    void getTurnPopeFavorTile() {
    }

    @Test
    void getInactiveLeaderCards() {
    }

    @Test
    void getActiveLeaderCards() {
    }

    @Test
    void setInactiveLeaderCards() {
    }

    @Test
    void setActiveLeaderCards() {
    }

    @Test
    void getTotalResources() {
    }

    @Test
    void getStrongboxResources() {
    }

    @Test
    void getWarehouseResources() {
    }

    @Test
    void getExtraSlotResources() {
    }

    @Test
    void isThereEnoughRes() {
    }

    @Test
    void isCardSuitableForSlots() {
    }

    @Test
    void getSuitablePlaces() {
    }

    @Test
    void putDevCardOnSlot() {
    }

    @Test
    void getDevCardOnSlot() {
    }

    @Test
    void clearShelf() {
    }

    @Test
    void swapShelves() {
    }

    @Test
    void getShelves() {
    }

    @Test
    void getDevSlots() {
    }

    @Test
    void subtractFromWarehouse() {
    }

    @Test
    void addToStrongBox() {
    }

    @Test
    void subtractFromStrongbox() {
    }

    @Test
    void subtractFromExtraSlot() {
    }

    @Test
    void setStrongbox() {
    }

    @Test
    void setGame() {
    }

    @Test
    void getVP() {
    }
}