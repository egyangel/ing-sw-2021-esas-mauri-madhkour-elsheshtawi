package it.polimi.ingsw.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpecialAbilityTest {


    private Resources.ResType oneResType;
    private Resources resourceHolder;
    private SpecialAbility specialAbility;
    private SpecialAbility specialAbility1;
    private SpecialAbility specialAbility2;
    private SpecialAbility specialAbility3;
    @BeforeEach
    void setUp() {
        oneResType = Resources.ResType.SHIELD;
        resourceHolder = new Resources(Resources.ResType.SHIELD,2);
        specialAbility = new SpecialAbility(SpecialAbility.AbilityType.DISCOUNT,oneResType);
        specialAbility1 = new SpecialAbility(SpecialAbility.AbilityType.EXTRASLOT,oneResType);
        specialAbility2 = new SpecialAbility(SpecialAbility.AbilityType.CONVERTWHITE,oneResType);
        specialAbility3 = new SpecialAbility(SpecialAbility.AbilityType.ADDPROD,oneResType);
    }

    @AfterEach
    void tearDown() {
        oneResType = null;
        resourceHolder.clear();
        specialAbility = null;
        specialAbility1 = null;
        specialAbility2 = null;
        specialAbility3 = null;
    }

    @Test
    void testGetAbilityType() {
        assertNotNull(specialAbility.getAbilityType());
        assertEquals(SpecialAbility.AbilityType.DISCOUNT,specialAbility.getAbilityType());

    }

    @Test
    void testGetResType() {
        assertNotNull(specialAbility.getResType());
        assertEquals(Resources.ResType.SHIELD,specialAbility.getResType());

    }

    @Test
    void testAddToHolder() {
        assertTrue(specialAbility.getResourcesAtSlot().isEmpty());
        specialAbility.addToHolder(resourceHolder);
        assertFalse(specialAbility.getResourcesAtSlot().isEmpty());

    }

    @Test
    void testGetResourcesAtSlot() {
        assertNotNull(specialAbility.getResourcesAtSlot());
        specialAbility.addToHolder(resourceHolder);
        assertEquals(resourceHolder.getResTypes(),specialAbility.getResourcesAtSlot().getResTypes());
        assertEquals(resourceHolder.sumOfValues(),specialAbility.getResourcesAtSlot().sumOfValues());

    }

    @Test
    void testSubtractFromExtraSlot() {
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
    @Test
    void testToString() {
        specialAbility.toString();
        assertFalse(specialAbility.toString().isEmpty());



    }
    @Test
    void testDescribeSpecialAbility() {
        specialAbility.describeSpecialAbility();
        specialAbility1.describeSpecialAbility();
        specialAbility2.describeSpecialAbility();
        specialAbility3.describeSpecialAbility();
        assertFalse(specialAbility.describeSpecialAbility().isEmpty());
        assertFalse(specialAbility1.describeSpecialAbility().isEmpty());
        assertFalse(specialAbility2.describeSpecialAbility().isEmpty());
        assertFalse(specialAbility3.describeSpecialAbility().isEmpty());





    }
}