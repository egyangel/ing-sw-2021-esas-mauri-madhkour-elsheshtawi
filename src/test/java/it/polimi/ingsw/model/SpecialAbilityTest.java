package it.polimi.ingsw.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpecialAbilityTest {


    private Resources.ResType oneResType;
    private Resources resourceHolder;
    private SpecialAbility specialAbility;
    @BeforeEach
    void setUp() {
        oneResType = Resources.ResType.SHIELD;
        resourceHolder = new Resources(Resources.ResType.SHIELD,2);
        specialAbility= new SpecialAbility(SpecialAbility.AbilityType.DISCOUNT,oneResType);
    }

    @AfterEach
    void tearDown() {
        oneResType =null;
        resourceHolder.clear();
        specialAbility=null;
    }

    @Test
    void getAbilityType() {
        assertNotNull(specialAbility.getAbilityType());
        assertEquals(SpecialAbility.AbilityType.DISCOUNT,specialAbility.getAbilityType());

    }

    @Test
    void getResType() {
        assertNotNull(specialAbility.getResType());
        assertEquals(Resources.ResType.SHIELD,specialAbility.getResType());

    }

    @Test
    void addToHolder() {
        assertTrue(specialAbility.getResourcesAtSlot().isEmpty());
        specialAbility.addToHolder(resourceHolder);
        assertFalse(specialAbility.getResourcesAtSlot().isEmpty());

    }

    @Test
    void getResourcesAtSlot() {
        assertNotNull(specialAbility.getResourcesAtSlot());
        specialAbility.addToHolder(resourceHolder);
        assertEquals(resourceHolder.getResTypes(),specialAbility.getResourcesAtSlot().getResTypes());
        assertEquals(resourceHolder.sumOfValues(),specialAbility.getResourcesAtSlot().sumOfValues());

    }

    @Test
    void subtractFromExtraSlot() {
        Resources tempRes= new Resources(Resources.ResType.STONE,1);
        assertTrue(specialAbility.getResourcesAtSlot().isEmpty());
        specialAbility.addToHolder(resourceHolder);
        specialAbility.subtractFromExtraSlot(tempRes);
        assertEquals(2,specialAbility.getResourcesAtSlot().sumOfValues());

        tempRes.clear();
        tempRes= new Resources(Resources.ResType.SHIELD,1);
        specialAbility.subtractFromExtraSlot(tempRes);
        assertEquals(1,specialAbility.getResourcesAtSlot().sumOfValues());
    }
}