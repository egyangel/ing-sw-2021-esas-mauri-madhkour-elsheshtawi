package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShelfTest {
    private  List<Resources.ResType> values= new ArrayList<>() ;
    private  List<Resources.ResType> values1= new ArrayList<>() ;
    private  Shelf[] warehouse= new Shelf[3];
    private   List<Resources.ResType> draw= new ArrayList<>(Arrays.asList(Resources.ResType.STONE,Resources.ResType.COIN,Resources.ResType.SHIELD,Resources.ResType.SERVANT));


    @Test
    void putResource() {

        warehouse[0] = new Shelf(Shelf.shelfPlace.TOP);
        warehouse[1] = new Shelf(Shelf.shelfPlace.MIDDLE);
        warehouse[2] = new Shelf(Shelf.shelfPlace.BOTTOM);

        values.add(draw.get(0));
        values.add(draw.get(0));
        values1.add(draw.get(1));
        values1.add(draw.get(1));
        values1.add(draw.get(1));

        int returAdd = warehouse[0].putResource(values);

        assertNotEquals(0,returAdd);
        assertEquals(0,warehouse[1].putResource(values));


        values.clear();
        values1.clear();
    }

    @Test
    void swapShelf() {
        warehouse[0] = new Shelf(Shelf.shelfPlace.TOP);
        warehouse[1] = new Shelf(Shelf.shelfPlace.MIDDLE);
        warehouse[2] = new Shelf(Shelf.shelfPlace.BOTTOM);

        values.add(draw.get(0));
        values.add(draw.get(0));
        values1.add(draw.get(1));
        values1.add(draw.get(1));


        warehouse[1].putResource(values);
        warehouse[2].putResource(values1);

//        assertTrue(warehouse[2].swapShelf(warehouse[1]));
        assertEquals(0,warehouse[2].swapShelf(warehouse[1]));

        values.clear();
        values1.clear();
        warehouse[2].clearShelf();
        warehouse[1].clearShelf();
        values.add(draw.get(0));
        values.add(draw.get(0));
        values1.add(draw.get(1));
        values1.add(draw.get(1));
        values1.add(draw.get(1));
        warehouse[1].putResource(values);
        warehouse[2].putResource(values1);

//        assertFalse(warehouse[2].swapShelf(warehouse[1]));
        assertEquals(1,warehouse[2].swapShelf(warehouse[1]));

        values.clear();
        values1.clear();

    }

    @Test
    void isEmpty() {
        warehouse[0] = new Shelf(Shelf.shelfPlace.TOP);
        warehouse[1] = new Shelf(Shelf.shelfPlace.MIDDLE);
        warehouse[2] = new Shelf(Shelf.shelfPlace.BOTTOM);
        assertTrue(warehouse[0].isEmpty());
        assertTrue(warehouse[1].isEmpty());
        assertTrue(warehouse[2].isEmpty());
        values.add(draw.get(0));
        values.add(draw.get(0));
        values1.add(draw.get(1));
        values1.add(draw.get(1));
        values1.add(draw.get(1));
        warehouse[1].putResource(values);
        warehouse[2].putResource(values1);

        //assertFalse(warehouse[0].isEmpty());
        assertFalse(warehouse[1].isEmpty());
        //assertFalse(warehouse[2].isEmpty());
        values.clear();
        values1.clear();

    }

    @Test
    void isFull() {

        warehouse[0] = new Shelf(Shelf.shelfPlace.TOP);
        warehouse[1] = new Shelf(Shelf.shelfPlace.MIDDLE);
        warehouse[2] = new Shelf(Shelf.shelfPlace.BOTTOM);
        assertFalse(warehouse[0].isFull());
        assertFalse(warehouse[1].isFull());
        assertFalse(warehouse[2].isFull());

        values.add(draw.get(0));
        values.add(draw.get(0));
        values1.add(draw.get(1));
        values1.add(draw.get(1));
        values1.add(draw.get(1));
        warehouse[1].putResource(values);
        warehouse[2].putResource(values1);


        assertTrue(warehouse[1].isFull());
        assertTrue(warehouse[2].isFull());
        values.clear();
        values1.clear();

    }

    @Test
    void shelfSize() {
        warehouse[0] = new Shelf(Shelf.shelfPlace.TOP);
        warehouse[1] = new Shelf(Shelf.shelfPlace.MIDDLE);
        warehouse[2] = new Shelf(Shelf.shelfPlace.BOTTOM);

        assertEquals(1,warehouse[0].shelfSize());
        assertEquals(2,warehouse[1].shelfSize());
        assertEquals(3,warehouse[2].shelfSize());
        values.clear();
        values1.clear();
    }

    @Test
    void getShelfResType() {
        warehouse[0] = new Shelf(Shelf.shelfPlace.TOP);
        warehouse[1] = new Shelf(Shelf.shelfPlace.MIDDLE);
        warehouse[2] = new Shelf(Shelf.shelfPlace.BOTTOM);

        values.add(draw.get(0));
        values.add(draw.get(0));
        values1.add(draw.get(1));
        values1.add(draw.get(1));
        values1.add(draw.get(1));

        warehouse[1].putResource(values);
        warehouse[2].putResource(values1);

        //assertEquals(1,warehouse[0].ShelfSize());
        assertEquals(Resources.ResType.STONE,warehouse[1].getShelfResType());
        assertEquals(Resources.ResType.COIN,warehouse[2].getShelfResType());
        values.clear();
        values1.clear();
    }



}