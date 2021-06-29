package it.polimi.ingsw.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RequirementTest {
    private List<Requirement.reqType> reqtype = new ArrayList<>();
    private List<DevCard.CardColor> colorList = new ArrayList<>();
    private Resources resources;
    private Requirement r1;
    private Requirement r2;
    private Requirement r3;
    private Requirement r4;


    @BeforeEach
    void setUp() {
        this.reqtype.add(Requirement.reqType.TWOCARD);
        this.reqtype.add(Requirement.reqType.THREECARD);
        this.reqtype.add(Requirement.reqType.LEVELTWOCARD);
        this.reqtype.add(Requirement.reqType.RESOURCES);
        this.colorList.add(DevCard.CardColor.BLUE);
        this.colorList.add(DevCard.CardColor.GREEN);
        this.colorList.add(DevCard.CardColor.PURPLE);
        resources = new Resources();
        r1 = new Requirement(colorList.get(0)) ;
        r2 = new Requirement(resources) ;
        r3 = new Requirement(reqtype.get(0),colorList.get(1),colorList.get(2)) ;
        r4 = new Requirement(reqtype.get(1),colorList.get(1),colorList.get(2)) ;


    }

    @AfterEach
    void tearDown() {
        reqtype.clear();
        colorList.clear();
        r1 = null;
        r2 = null;
        r3 = null;
        r4 = null;
        resources.clear();

    }

    @Test
    void getType() {
        assertEquals(reqtype.get(0),r3.getType());
    }

    @Test
    void getColor() {
        assertEquals(colorList.get(0),r1.getColor(0));
    }

    @Test
    void getResource() {
        assertTrue(r2.getResource().isEmpty());
        resources.add(Resources.ResType.COIN,5);
        r2= new Requirement(resources);
        assertEquals(resources,r2.getResource());
    }

    @Test
    void testToString() {
        r1.toString();
        assertFalse(r1.toString().isEmpty());
    }
    @Test
    void testDescribeRequirement() {
        r1.describeRequirement();
        r2.describeRequirement();
        r3.describeRequirement();
        r4.describeRequirement();
        assertFalse(r1.toString().isEmpty());
        assertFalse(r2.toString().isEmpty());
        assertFalse(r3.toString().isEmpty());
        assertFalse(r4.toString().isEmpty());

    }
}