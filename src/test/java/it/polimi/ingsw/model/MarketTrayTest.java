package it.polimi.ingsw.model;



import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class MarketTrayTest {

    @Test
    @Disabled("Not implemented yet")
    @DisplayName("selects which row to choose from the market tray")
    public void selectRow(int row) {
        /*each number in rowOfMarbles represent a Marble color in a row...
        * (for example 0=white, 1=blue etc...) */
        List<Integer> rowOfMarbles=List.of(0,1,2,3);
        Assertions.assertAll(() -> assertEquals(row, 0)
        );




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