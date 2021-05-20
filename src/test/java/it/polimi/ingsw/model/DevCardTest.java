package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DevCardTest {

    private DevSlot slotL = new DevSlot(DevSlot.slotPlace.LEFT);
    private Resources Lhs = new Resources(Resources.ResType.STONE,2);
    private Resources Rhs= new Resources(Resources.ResType.SERVANT,1);
    private Resources cost= new Resources(Resources.ResType.STONE,2);

    private DevCard testCard = new DevCard(1, DevCard.CardColor.BLUE,Lhs,Rhs, cost,10);
    private DevCard testCard2 = new DevCard(2, DevCard.CardColor.YELLOW,Lhs,Rhs, cost,5);
    private DevCard testCard3 = new DevCard(3, DevCard.CardColor.GREEN,Lhs,Rhs, cost,8);
    private


    @Test
    void getLevel() {
        assertEquals(1,testCard.getLevel());
        assertNotEquals(4,testCard.getLevel());
        assertNotEquals(0,testCard.getLevel());
    }

    @Test
    void getVictoryPoints() {
        assertEquals(10,testCard.getVictoryPoints());
        assertNotEquals(-1,testCard.getVictoryPoints());
        assertNotEquals(0,testCard.getVictoryPoints());
    }

    @Test
    void getColor() {
        assertEquals(DevCard.CardColor.BLUE,testCard.getColor());
        assertFalse(testCard.getColor().equals("WHITE"));

    }

    @Test
    void getLHS() {
        Resources test = new Resources(Resources.ResType.STONE,0);
        assertNotEquals(null,testCard.getLHS());
        assertEquals(test.getResTypes(),testCard.getLHS().getResTypes());

    }

    @Test
    void getRHS() {
        Resources test = new Resources(Resources.ResType.SERVANT,0);
        assertNotEquals(null,testCard.getRHS());
        assertEquals(test.getResTypes(),testCard.getRHS().getResTypes());

    }

    @Test
    void getCost() {
        Resources test = new Resources(Resources.ResType.STONE,0);
        Resources test2 = new Resources(Resources.ResType.STONE,2);
        assertNotEquals(null,testCard.getCost());
        assertNotEquals(test.getResTypes(),testCard.getRHS().getResTypes());
    }

}