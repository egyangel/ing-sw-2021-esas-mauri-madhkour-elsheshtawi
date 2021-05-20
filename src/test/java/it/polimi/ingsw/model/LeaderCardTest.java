package it.polimi.ingsw.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeaderCardTest {
    private Requirement requirement;
    private int victoryPoint;
    private SpecialAbility ability;
    LeaderCard lcard;
    LeaderCard lcard2;
    private Requirement requirement1;
    private int victoryPoint1;
    private SpecialAbility ability1;

    @BeforeEach
    void setUp() {
        requirement = new Requirement(Requirement.reqType.TWOCARD, DevCard.CardColor.YELLOW, DevCard.CardColor.GREEN);
        victoryPoint = 5 ;
        ability = new SpecialAbility(SpecialAbility.AbilityType.DISCOUNT,Resources.ResType.SERVANT);

        requirement1 = new Requirement(Requirement.reqType.THREECARD, DevCard.CardColor.GREEN, DevCard.CardColor.PURPLE );
        victoryPoint1 = 5 ;
        ability1 = new SpecialAbility(SpecialAbility.AbilityType.CONVERTWHITE,Resources.ResType.SERVANT);
        lcard2= new LeaderCard(requirement1,victoryPoint1,ability1);
        lcard = new LeaderCard(requirement,victoryPoint,ability);
    }

    @AfterEach
    void tearDown() {
        requirement = null;
        ability = null;
        lcard = null;
    }

    @Test
    void getAbility() {
        assertEquals(ability,lcard.getAbility());
    }

    @Test
    void getVictoryPoints() {
        assertEquals(5,lcard.getVictoryPoints());
        assertNotEquals(-1,lcard.getVictoryPoints());
    }

    @Test
    void getRequirement() {
        assertEquals(requirement,lcard.getRequirement());
        assertNotEquals(lcard2.getRequirement(),lcard.getRequirement());
    }
}