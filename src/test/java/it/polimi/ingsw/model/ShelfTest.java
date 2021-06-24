package it.polimi.ingsw.model;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShelfTest {
    private  Shelf[] warehouse= new Shelf[3];

    @Test
    void putResource() {
        warehouse[0] = new Shelf(Shelf.shelfPlace.TOP);
        warehouse[1] = new Shelf(Shelf.shelfPlace.MIDDLE);
        warehouse[2] = new Shelf(Shelf.shelfPlace.BOTTOM);

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

    @BeforeAll
    void buildWarehouse(){
        warehouse[0] = new Shelf(Shelf.shelfPlace.TOP);
        warehouse[1] = new Shelf(Shelf.shelfPlace.MIDDLE);
        warehouse[2] = new Shelf(Shelf.shelfPlace.BOTTOM);
    }

    @Test
    void swapShelf() {
        buildWarehouse();

        warehouse[1].putResource(Resources.ResType.SHIELD, 1);
        warehouse[2].putResource(Resources.ResType.COIN,1);

        assertEquals(0,warehouse[1].swapShelf(warehouse[2]));
        assertEquals(Resources.ResType.COIN, warehouse[1].getShelfResType());
        assertEquals(Resources.ResType.SHIELD, warehouse[2].getShelfResType());
        assertEquals(0,warehouse[1].swapShelf(warehouse[2]));

        warehouse[2].clearShelf();
        warehouse[2].putResource(Resources.ResType.STONE,3);

        assertEquals(2,warehouse[2].swapShelf(warehouse[1]));
        assertEquals(0,warehouse[1].swapShelf(warehouse[2]));


    }

    @Test
    void isEmpty() {
        buildWarehouse();

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
        buildWarehouse();

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
        buildWarehouse();

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
    void getShelfResType() {
        buildWarehouse();

        warehouse[1].putResource(Resources.ResType.SERVANT,2);
        warehouse[2].putResource(Resources.ResType.SHIELD,3);

        assertEquals(Resources.ResType.SERVANT,warehouse[1].getShelfResType());
        assertEquals(Resources.ResType.SHIELD,warehouse[2].getShelfResType());
    }



}