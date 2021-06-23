package it.polimi.ingsw.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DevCardDeckTest {

    private Resources Lhs;
    private Resources Rhs;
    private Resources cost;
    private Resources Rhs2;
    private Resources cost2;
    private DevCardDeck cardDeck;
    private DevCard testCard ;
    private DevCard testCard2 ;
    private DevCard testCard3 ;
    private DevCard testCard4 ;
    @BeforeEach
    void setUp() {
        Lhs = new Resources(Resources.ResType.STONE,2);
        Rhs = new Resources(Resources.ResType.SERVANT,1);
        cost = new Resources(Resources.ResType.STONE,2);

        Rhs2 = new Resources(Resources.ResType.SHIELD,5);
        cost2 = new Resources(Resources.ResType.COIN,2);

        cardDeck = new DevCardDeck(DevCard.CardColor.BLUE,1);

        testCard = new DevCard(1, DevCard.CardColor.BLUE,Lhs,Rhs, cost,10);
        testCard2 = new DevCard(2, DevCard.CardColor.YELLOW,Lhs,Rhs, cost,5);
        testCard3 = new DevCard(3, DevCard.CardColor.GREEN,Lhs,Rhs, cost,8);
        testCard4 = new DevCard(1, DevCard.CardColor.BLUE,Lhs,Rhs2, cost2,8);

    }

    @AfterEach
    void tearDown() {
        cardDeck  = null;

    }
    //should be in the reverse order the last one should be the top
    @Test
    void putCard() {
        cardDeck.putCard(testCard);
        assertEquals(testCard,cardDeck.peekTopCard());
        cardDeck.putCard(testCard4);

        assertEquals(testCard4,cardDeck.peekBottomCard());
    }

    @Test
    void peekTopCard() {
        cardDeck.putCard(testCard);
        cardDeck.putCard(testCard4);
        assertNotNull(cardDeck.peekTopCard());
        assertEquals(testCard,cardDeck.peekTopCard());

        assertNotEquals(testCard4,cardDeck.peekTopCard());

    }

    @Test
    void peekBottomCard() {
        cardDeck.putCard(testCard);
        cardDeck.putCard(testCard4);
        assertNotNull(cardDeck.peekBottomCard());
        assertEquals(testCard4,cardDeck.peekBottomCard());

        assertNotEquals(testCard,cardDeck.peekBottomCard());
    }

    @Test
    void discardBottomCard() {
        cardDeck.putCard(testCard);
        cardDeck.putCard(testCard4);
        cardDeck.putCard(testCard2);

        assertEquals(testCard2,cardDeck.discardBottomCard());
       // assertNotNull(cardDeck.discardBottomCard());
        assertEquals(testCard4,cardDeck.peekBottomCard());
        assertNotEquals(testCard2,cardDeck.peekBottomCard());
    }

    @Test
    void removeTopCard() {
        cardDeck.putCard(testCard);
        cardDeck.putCard(testCard4);
        cardDeck.putCard(testCard2);

        assertEquals(testCard2,cardDeck.discardBottomCard());
        // assertNotNull(cardDeck.discardBottomCard());
        assertEquals(testCard4,cardDeck.peekBottomCard());
        assertNotEquals(testCard2,cardDeck.peekBottomCard());
    }

    @Test
    void getColor() {
        assertEquals(DevCard.CardColor.BLUE,cardDeck.getColor());
        cardDeck = new DevCardDeck(DevCard.CardColor.GREEN,1);
        assertNotEquals(DevCard.CardColor.BLUE,cardDeck.getColor());
        assertEquals(DevCard.CardColor.GREEN,cardDeck.getColor());
    }

    @Test
    void getLevel() {
        assertEquals(1,cardDeck.getLevel());
        cardDeck = new DevCardDeck(DevCard.CardColor.BLUE,2);
        assertNotEquals(1,cardDeck.getLevel());
    }
}