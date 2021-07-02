package it.polimi.ingsw.model;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class ShelfTest {
    private  Shelf[] warehouse= new Shelf[3];


    @BeforeEach
    void setUp() {
        warehouse[0] = new Shelf(Shelf.shelfPlace.TOP);
        warehouse[1] = new Shelf(Shelf.shelfPlace.MIDDLE);
        warehouse[2] = new Shelf(Shelf.shelfPlace.BOTTOM);
    }
    @AfterEach
    void tearDown() {
        warehouse  = null;

    }
    @Test
    void putResource() {


        assertEquals(0,warehouse[0].putResource(Resources.ResType.STONE,1));
        assertEquals(1,warehouse[0].putResource(Resources.ResType.STONE,1));
        assertEquals(1,warehouse[0].putResource(Resources.ResType.STONE,1));

        assertEquals(0,warehouse[1].putResource(Resources.ResType.COIN,1));
        assertEquals(0,warehouse[1].putResource(Resources.ResType.COIN,1));
        assertEquals(1,warehouse[1].putResource(Resources.ResType.COIN,1));
        assertEquals(1,warehouse[1].putResource(Resources.ResType.COIN,1));
        assertEquals(1,warehouse[1].putResource(Resources.ResType.COIN,1));

        assertEquals(0,warehouse[2].putResource(Resources.ResType.SHIELD,1));
        assertEquals(0,warehouse[2].putResource(Resources.ResType.SHIELD,2));
        assertEquals(1,warehouse[2].putResource(Resources.ResType.SHIELD,1));
        assertEquals(1,warehouse[2].putResource(Resources.ResType.SHIELD,1));
        assertEquals(1,warehouse[2].putResource(Resources.ResType.SHIELD,1));
    }



    @Test
    void swapShelf() {

        warehouse[1].putResource(Resources.ResType.SHIELD, 1);
        warehouse[2].putResource(Resources.ResType.COIN,1);

        assertEquals(0,warehouse[1].swapShelf(warehouse[2]).sumOfValues());
        assertEquals(Resources.ResType.COIN, warehouse[1].getShelfResType());
        assertEquals(Resources.ResType.SHIELD, warehouse[2].getShelfResType());
        assertEquals(0,warehouse[1].swapShelf(warehouse[2]).sumOfValues());

        warehouse[2].clearShelf();
        warehouse[2].putResource(Resources.ResType.STONE,3);

        assertEquals(2,warehouse[2].swapShelf(warehouse[1]).sumOfValues());
        assertEquals(0,warehouse[1].swapShelf(warehouse[2]).sumOfValues());


    }

    @Test
    void isEmpty() {

        assertTrue(warehouse[0].isEmpty());
        assertTrue(warehouse[1].isEmpty());
        assertTrue(warehouse[2].isEmpty());

        warehouse[1].putResource(Resources.ResType.COIN,1);
        warehouse[2].putResource(Resources.ResType.STONE,0);

        assertFalse(warehouse[1].isEmpty());
        assertTrue(warehouse[2].isEmpty());
        warehouse[1].clearShelf();
        warehouse[2].clearShelf();
        assertTrue(warehouse[1].isEmpty());
        assertTrue(warehouse[2].isEmpty());
    }

    @Test
    void isFull() {


        assertFalse(warehouse[0].isFull());
        assertFalse(warehouse[1].isFull());
        assertFalse(warehouse[2].isFull());

        warehouse[0].putResource(Resources.ResType.COIN,1);
        warehouse[1].putResource(Resources.ResType.SHIELD,1);
        warehouse[1].putResource(Resources.ResType.SHIELD,1);
        warehouse[2].putResource(Resources.ResType.STONE,0);

        assertTrue(warehouse[0].isFull());
        assertTrue(warehouse[1].isFull());
        assertFalse(warehouse[2].isFull());

        assertFalse(warehouse[0].isEmpty());
        warehouse[1].clearShelf();
        assertFalse(warehouse[1].isFull());
        assertTrue(warehouse[1].isEmpty());
    }

    @Test
    void numberOfElements() {


        warehouse[0].putResource(Resources.ResType.COIN,1);
        warehouse[1].putResource(Resources.ResType.STONE,1);
        warehouse[2].putResource(Resources.ResType.SHIELD,2);

        assertEquals(1,warehouse[0].getNumberOfElements());
        assertEquals(1,warehouse[1].getNumberOfElements());
        assertEquals(2,warehouse[2].getNumberOfElements());

        warehouse[1].swapShelf(warehouse[2]);
        assertEquals(2, warehouse[1].getNumberOfElements());
        assertEquals(1, warehouse[2].getNumberOfElements());

        assertEquals(1,warehouse[0].clearShelf());
        assertEquals(0,warehouse[0].clearShelf());

    }

    @Test
    void testGetShelfResType() {


        warehouse[1].putResource(Resources.ResType.SERVANT,2);
        warehouse[2].putResource(Resources.ResType.SHIELD,3);

        assertEquals(Resources.ResType.SERVANT,warehouse[1].getShelfResType());
        assertEquals(Resources.ResType.SHIELD,warehouse[2].getShelfResType());
    }
    @Test
    void testDescribeShelfFancy() {


        warehouse[1].putResource(Resources.ResType.SERVANT,2);
        warehouse[2].putResource(Resources.ResType.SHIELD,3);

        assertFalse(warehouse[0].describeShelfFancy().isEmpty());
        assertFalse(warehouse[1].describeShelfFancy().isEmpty());
        assertFalse(warehouse[2].describeShelfFancy().isEmpty());
    }



}