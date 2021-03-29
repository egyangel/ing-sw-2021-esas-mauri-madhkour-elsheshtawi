package it.polimi.ingsw;

import it.polimi.ingsw.model.MarketTray;

public class App 
{
    public static void main( String[] args )
    {
        MarketTray test = new MarketTray();
        System.out.println("Initial MarketTray Position:, rows and columns start at 1");
        test.MarketTrayDraw();

        System.out.println("After 2nd row selection");
        test.selectRow(2);
        test.MarketTrayDraw();
        test.ResourceDraw();

        System.out.println("After 3rd column selection");
        test.selectColumn(3);
        test.MarketTrayDraw();
        test.ResourceDraw();
    }
}

