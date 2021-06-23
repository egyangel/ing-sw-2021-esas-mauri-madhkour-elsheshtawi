package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DevCardTest {

    private Resources Lhs = new Resources(Resources.ResType.STONE,2);
    private Resources Rhs= new Resources(Resources.ResType.SERVANT,1);
    private Resources cost= new Resources(Resources.ResType.STONE,2);

    private DevCard testCard = new DevCard(1, DevCard.CardColor.BLUE,Lhs,Rhs, cost,10);


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
        assertNotEquals(testCard.getColor(), "WHITE");

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

        assertNotEquals(null,testCard.getCost());
        assertNotEquals(test.getResTypes(),testCard.getRHS().getResTypes());
    }

}