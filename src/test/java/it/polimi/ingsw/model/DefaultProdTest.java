package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumclasses.MarbleColor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultProdTest {
    private  List<Resources.ResType> draw= new ArrayList<>(Arrays.asList(Resources.ResType.STONE,Resources.ResType.COIN,Resources.ResType.SHIELD,Resources.ResType.SERVANT));

    DefaultProd dP = new DefaultProd();

    @Test
    void putTopLeftResource() {
        dP.putTopLeftResource(draw.get(0));
        assertTrue(dP.getLeftRes().getResTypes().contains(draw.get(0)));
    }

    @Test
    void putBottomLeftResource() {
        dP.putTopLeftResource(draw.get(1));
        assertTrue(dP.getLeftRes().getResTypes().contains(draw.get(1)));
    }

    @Test
    void putRightResource() {
        dP.putRightResource(draw.get(2));
        assertTrue(dP.getRightRes().getResTypes().contains(draw.get(2)));
    }

    @Test
    void produce() {
        dP.putTopLeftResource(draw.get(0));
        dP.putTopLeftResource(draw.get(1));
        dP.putRightResource(draw.get(2));
        assertTrue(dP.getLeftRes().getResTypes().contains(draw.get(0)));
        assertTrue(dP.getLeftRes().getResTypes().contains(draw.get(1)));
        assertTrue(dP.getRightRes().getResTypes().contains(draw.get(2)));
        dP.produce();
        assertFalse(dP.getLeftRes().getResTypes().contains(draw.get(0)));
        assertFalse(dP.getLeftRes().getResTypes().contains(draw.get(1)));
        assertFalse(dP.getRightRes().getResTypes().contains(draw.get(2)));
    }
}