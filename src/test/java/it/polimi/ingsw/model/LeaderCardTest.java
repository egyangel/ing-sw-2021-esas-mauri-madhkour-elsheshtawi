package it.polimi.ingsw.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeaderCardTest {
    private Requirement requirement;
    private int victoryPoint;
    private SpecialAbility ability;
    LeaderCard card;
    LeaderCard card2;
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
        card2= new LeaderCard(requirement1,victoryPoint1,ability1);
        card = new LeaderCard(requirement,victoryPoint,ability);
    }

    @AfterEach
    void tearDown() {
        requirement = null;
        ability = null;
        card = null;
    }

    @Test
    void getAbility() {
        assertEquals(ability,card.getAbility());
    }

    @Test
    void getVictoryPoints() {
        assertEquals(5,card.getVictoryPoints());
        assertNotEquals(-1,card.getVictoryPoints());
    }

    @Test
    void getRequirement() {
        assertEquals(requirement,card.getRequirement());
        assertNotEquals(card2.getRequirement(),card.getRequirement());
    }
}