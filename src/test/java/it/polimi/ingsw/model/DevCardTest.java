package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DevCardTest {
    private DevSlot slotL = new DevSlot(DevSlot.slotPlace.LEFT);
    private Resources Lhs = new Resources(Resources.ResType.STONE,2);
    private Resources Rhs= new Resources(Resources.ResType.SERVANT,1);
    private Resources cost= new Resources(Resources.ResType.STONE,2);

    private DevCard testCard = new DevCard(1, DevCard.CardColor.BLUE,Lhs,Rhs, cost,10);
    private DevCard testCard2 = new DevCard(2, DevCard.CardColor.YELLOW,Lhs,Rhs, cost,5);
    private DevCard testCard3 = new DevCard(3, DevCard.CardColor.GREEN,Lhs,Rhs, cost,8);


    @Test
    void getLevel() {
    }

    @Test
    void getVictoryPoints() {
    }

    @Test
    void getColor() {
    }

    @Test
    void getLHS() {
    }

    @Test
    void getRHS() {
    }

    @Test
    void getCost() {
    }

    @Test
    void produce() {
    }
}