package it.polimi.ingsw.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DevCardTest {

    private Resources Lhs;
    private Resources Rhs;
    private Resources cost;
    private DevCard testCard;
    private DevCard testDefaultProdCard;
    @BeforeEach
    void setUp() {
        Lhs = new Resources(Resources.ResType.STONE,2);
        Rhs= new Resources(Resources.ResType.SERVANT,1);
        cost= new Resources(Resources.ResType.STONE,2);
        testCard = new DevCard(1, DevCard.CardColor.BLUE,Lhs,Rhs, cost,10);
        testDefaultProdCard = new DevCard(Lhs,Rhs);

    }
    @AfterEach
    void tearDown() {
        Lhs.clear();
        Rhs.clear();
        cost.clear();
        testCard = null;
        testDefaultProdCard = null;

    }
    @Test
    void testGetLevel() {
        assertEquals(1,testCard.getLevel());
        assertNotEquals(4,testCard.getLevel());
        assertNotEquals(0,testCard.getLevel());
    }

    @Test
    void testGetVictoryPoints() {
        assertEquals(10,testCard.getVictoryPoints());
        assertNotEquals(-1,testCard.getVictoryPoints());
        assertNotEquals(0,testCard.getVictoryPoints());
    }

    @Test
    void testGetColor() {
        assertEquals(DevCard.CardColor.BLUE,testCard.getColor());
        assertNotEquals(testCard.getColor(), DevCard.CardColor.YELLOW);

    }

    @Test
    void testLHS() {
        Resources test = new Resources(Resources.ResType.STONE,0);
        assertNotEquals(null,testCard.getLHS());
        assertEquals(test.getResTypes(),testCard.getLHS().getResTypes());

    }

    @Test
    void testGetRHS() {
        Resources test = new Resources(Resources.ResType.SERVANT,0);
        assertNotEquals(null,testCard.getRHS());
        assertEquals(test.getResTypes(),testCard.getRHS().getResTypes());

    }

    @Test
    void testGetCost() {
        Resources test = new Resources(Resources.ResType.STONE,0);

        assertNotEquals(null,testCard.getCost());
        assertNotEquals(test.getResTypes(),testCard.getRHS().getResTypes());
    }
    @Test
    void testDescribeDevCard() {

        assertFalse(testCard.describeDevCard().isEmpty());
    }
    @Test
    void testDescribeLevelAndColor() {
        assertFalse(testCard.describeDevCard().isEmpty());
        testCard = new DevCard(2, DevCard.CardColor.BLUE,Lhs,Rhs, cost,10);

        assertFalse(testCard.describeDevCard().isEmpty());
        testCard = new DevCard(3, DevCard.CardColor.BLUE,Lhs,Rhs, cost,10);

        assertFalse(testCard.describeDevCard().isEmpty());
    }

}