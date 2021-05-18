package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ResourcesTest {
    private Map<Resources.ResType, Integer> values = new HashMap<>();
    private Resources res= new Resources();
    private List<Resources.ResType> draw= new ArrayList<>(Arrays.asList(Resources.ResType.STONE,Resources.ResType.COIN,Resources.ResType.SHIELD,Resources.ResType.SERVANT));

    @Test
    void testAdd() {
        int res1 = res.getNumberOfType(draw.get(0));
        int res2 = 5;

        res.add(draw.get(0),3);
        assertEquals(res1+3,res.getNumberOfType(draw.get(0)));
        res1 = res.getNumberOfType(draw.get(0));

        res.add(draw.get(0),res2);
        assertEquals(res1+res2,res.getNumberOfType(draw.get(0)));

        res.clear();

    }

    @Test
    void testSubtract() {
        int resBefore1,resBefore2;
        int resAfter1,resAfter2;

        res.add(draw.get(0),3);
        res.add(draw.get(1),5);

        resBefore1 = res.getNumberOfType(draw.get(0));
        resBefore2 = res.getNumberOfType(draw.get(1));

        res.subtract(draw.get(0),2);
        res.subtract(draw.get(1),5);

        resAfter1=res.getNumberOfType(draw.get(0));
        resAfter2=res.getNumberOfType(draw.get(1));

        assertNotEquals(resBefore1, resAfter1);
        assertFalse(res.isThereType(draw.get(1)));
        res.clear();
    }

    @Test
    void sumOfValues() {
        res.add(draw.get(0),3);
        res.add(draw.get(1),5);
        assertEquals(8,res.sumOfValues());
        res.clear();
    }

    @Test
    void getResTypes() {
        res.add(draw.get(0),3);
        res.add(draw.get(1),3);
        res.add(draw.get(2),1);
        res.add(draw.get(3),1);

        assertTrue(res.getResTypes().containsAll(draw));
        res.clear();

        res.add(draw.get(1),3);
        res.add(draw.get(3),1);
        assertFalse(res.getResTypes().containsAll(draw));


        res.clear();
    }

    @Test
    void getNumberOfType() {

        res.add(draw.get(0),3);
        assertEquals(3,res.getNumberOfType(draw.get(0)));
        res.clear();
    }

    @Test
    void isThereType() {
        res.add(draw.get(0),3);
        assertTrue(res.isThisTypeOnly(draw.get(0)));
        assertFalse(res.isThisTypeOnly(draw.get(1)));
    }

    @Test
    void clear() {
        res.add(draw.get(0),3);
        res.add(draw.get(1),3);
        res.add(draw.get(2),1);
        res.add(draw.get(3),1);

        assertTrue(res.getResTypes().containsAll(draw));
        res.clear();
        assertFalse(res.getResTypes().containsAll(draw));
    }

    @Test
    void isEmpty() {
        res.add(draw.get(0),3);
        res.add(draw.get(1),3);
        assertFalse(res.isEmpty());
        res.clear();
        assertTrue(res.isEmpty());
    }

    @Test
    void isThisTypeOnly() {
        res.add(draw.get(0),3);
        res.add(draw.get(1),3);
        assertFalse(res.isThisTypeOnly(draw.get(0)));
        res.clear();
        res.add(draw.get(0),3);
        assertTrue(res.isThisTypeOnly(draw.get(0)));
    }


    @Test
    void getOnlyType() {
        int i = 0;

        res.add(draw.get(0), 3);
        res.add(draw.get(1), 3);
        res.add(draw.get(2), 1);
        res.add(draw.get(3), 1);
        /*while(i<4){
            System.out.println(res.getOnlyType());
            i++;

        }
        Iterator<Resources.ResType> itr = res.keySet().iterator();
        while (itr.hasNext())
        {
            Resources.ResType key = itr.next();


        }*/

        /*while (i<res.getResTypes().size()) {

            assertEquals(draw.get(i),res.getOnlyType());
            i++;
        }*/
    }


}