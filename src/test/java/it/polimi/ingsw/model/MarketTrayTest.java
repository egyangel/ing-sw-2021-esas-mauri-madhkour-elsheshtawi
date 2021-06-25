package it.polimi.ingsw.model;



import it.polimi.ingsw.model.enumclasses.MarbleColor;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;


public class MarketTrayTest {
    MarketTray marketTest;
    List<MarbleColor> resources;
    List<MarbleColor> marbleColors;
    @BeforeEach
    void setUp() {
        marketTest = new MarketTray();
        resources = new ArrayList<>();
        marbleColors = new ArrayList<>(Arrays.asList( MarbleColor.WHITE, MarbleColor.BLUE, MarbleColor.GREY, MarbleColor.YELLOW, MarbleColor.PURPLE, MarbleColor.RED));;

    }
    @AfterEach
    void tearDown() {
        marketTest =null;
        resources.clear();
        marbleColors.clear();

    }
    @Test
    //@Disabled("Not implemented yet")
    @DisplayName("selects which row to choose from the market tray")
    public void selectRow() {


        for(int i=1;i<4;i++) {
            resources = marketTest.selectRow(i);
            assertNotNull(resources);
            assertEquals(false, resources.isEmpty());
            assertEquals(4, resources.size());
            for(int j = 0;j<4;j++){
                MarbleColor marble = resources.get(j);
                assertEquals(true, marbleColors.contains(marble));
                
            }
        }
        int k=-1;
        assertNull(marketTest.selectRow(k));


    }

    @Test
    public void selectColumn() {
        for(int i=1;i<5;i++) {
            resources = marketTest.selectColumn(i);
            assertNotNull(resources);
            assertEquals(false, resources.isEmpty());
            assertEquals(3, resources.size());
            for(int j = 0;j<3;j++){
                MarbleColor marble = resources.get(j);
                assertEquals(true, marbleColors.contains(marble));

            }
        }
        int k=-1;
        assertNull(marketTest.selectColumn(k));
        assertNull(marketTest.selectColumn(5));

    }

}