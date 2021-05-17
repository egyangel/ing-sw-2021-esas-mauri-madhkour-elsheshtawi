package it.polimi.ingsw.model;



import it.polimi.ingsw.model.enumclasses.MarbleColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class MarketTrayTest {
    MarketTray marketTest = new MarketTray();
    List<MarbleColor> resources = new ArrayList<>();
    List<MarbleColor> marbleColors = new ArrayList<>(Arrays.asList( MarbleColor.WHITE, MarbleColor.BLUE, MarbleColor.GREY, MarbleColor.YELLOW, MarbleColor.PURPLE, MarbleColor.RED));;

    @Test
    //@Disabled("Not implemented yet")
    @DisplayName("selects which row to choose from the market tray")
    public void selectRow() {

        /*each number in rowOfMarbles represent a Marble color in a row...
         (for example 0=white, 1=blue etc...)
        */List<Integer> rowOfMarbles=List.of(0,1,2,3);
        /*Assertions.assertAll(() -> assertEquals(row, 0)
        );*/


        for(int i=1;i<4;i++) {
            resources = marketTest.selectRow(i);
            //System.out.println(resources);
            assertEquals(false, resources.isEmpty());
            assertEquals(4, resources.size());
            for(int j = 0;j<4;j++){
                MarbleColor marble = resources.get(j);
                assertEquals(true, marbleColors.contains(marble));
                
            }
        }
   







    }

    @Test
    public void selectColumn() {
    }

    @Test
    public void marketTrayDraw() {
    }

    @Test
    public void resourceDraw() {
    }
}