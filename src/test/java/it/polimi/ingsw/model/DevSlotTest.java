package it.polimi.ingsw.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DevSlotTest {
    private DevSlot slotL;
    Resources Lhs;
    Resources Rhs;
    Resources cost;

    DevCard testCard;
    DevCard testCard2;
    DevCard testCard3;
    @BeforeEach
    void setUp() {
        slotL = new DevSlot(DevSlot.slotPlace.LEFT);
        Lhs = new Resources(Resources.ResType.STONE,2);
        Rhs= new Resources(Resources.ResType.SERVANT,1);
        cost= new Resources(Resources.ResType.STONE,2);
        testCard = new DevCard(1, DevCard.CardColor.BLUE,Lhs,Rhs, cost,10);
        testCard2 = new DevCard(2, DevCard.CardColor.YELLOW,Lhs,Rhs, cost,5);
        testCard3 = new DevCard(3, DevCard.CardColor.GREEN,Lhs,Rhs, cost,8);

    }
    @AfterEach
    void tearDown() {
        slotL.clear();
        Lhs.clear();
        Rhs.clear();
        cost.clear();
        testCard = null;
        testCard2 =null;
        testCard3 = null;

    }
    @Test
    void testPutDevCard() {
        assertTrue(slotL.isEmpty());
        slotL.putDevCard(testCard);
        assertFalse(slotL.isEmpty());
        assertEquals(testCard,slotL.getTopDevCard());

    }

    @Test
    void testIsEmpty() {
        slotL.putDevCard(testCard);
        assertFalse(slotL.isEmpty());
        slotL.clear();
        assertTrue(slotL.isEmpty());

    }

    @Test
    void testIsFull() {
        slotL.putDevCard(testCard);
        assertFalse(slotL.isFull());
        slotL.putDevCard(testCard2);
        slotL.putDevCard(testCard3);

        assertTrue(slotL.isFull());


    }

    @Test
    void testGetTopDevCard() {
        slotL.putDevCard(testCard);
        assertEquals(testCard,slotL.getTopDevCard());
        slotL.putDevCard(testCard2);
        assertNotEquals(testCard,slotL.getTopDevCard());
        slotL.putDevCard(testCard3);
        assertEquals(testCard3,slotL.getTopDevCard());


    }
    @Test
    void testDescribeDevSlot(){

        assertTrue(slotL.describeDevSlot().equals(slotL.getPlace()+" SLOT: Empty"));

        slotL.putDevCard(testCard2);
        slotL.putDevCard(testCard3);
        assertFalse(slotL.describeDevSlot().equals(slotL.getPlace()+" SLOT: Empty"));


    }
}