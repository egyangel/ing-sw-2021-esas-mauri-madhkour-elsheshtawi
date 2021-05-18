package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DevSlotTest {
    private DevSlot slotL = new DevSlot(DevSlot.slotPlace.LEFT);
    Resources Lhs = new Resources(Resources.ResType.STONE,2);
    Resources Rhs= new Resources(Resources.ResType.SERVANT,1);
    Resources cost= new Resources(Resources.ResType.STONE,2);

    DevCard testCard = new DevCard(1, DevCard.CardColor.BLUE,Lhs,Rhs, cost,10);
    DevCard testCard2 = new DevCard(2, DevCard.CardColor.YELLOW,Lhs,Rhs, cost,5);
    DevCard testCard3 = new DevCard(3, DevCard.CardColor.GREEN,Lhs,Rhs, cost,8);

    @Test
    void addDevCard() {
        assertTrue(slotL.isEmpty());
        slotL.addDevCard(testCard);
        assertFalse(slotL.isEmpty());
        assertEquals(testCard,slotL.getTopDevCard());

    }

    @Test
    void isEmpty() {
        slotL.addDevCard(testCard);
        assertFalse(slotL.isEmpty());
        slotL.clear();
        assertTrue(slotL.isEmpty());

    }

    @Test
    void isFull() {
        slotL.addDevCard(testCard);
        assertFalse(slotL.isFull());
        slotL.addDevCard(testCard2);
        slotL.addDevCard(testCard3);

        assertTrue(slotL.isFull());


    }



    @Test
    void getTopDevCard() {
        slotL.addDevCard(testCard);
        assertEquals(testCard,slotL.getTopDevCard());
        slotL.addDevCard(testCard2);
        assertNotEquals(testCard,slotL.getTopDevCard());
        slotL.addDevCard(testCard3);
        assertEquals(testCard3,slotL.getTopDevCard());


    }
}