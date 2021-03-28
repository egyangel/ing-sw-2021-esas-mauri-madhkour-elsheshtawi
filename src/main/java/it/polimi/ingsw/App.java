package it.polimi.ingsw;

import it.polimi.ingsw.model.MarketTray;

/**
 * Hello world!
 *
 */
public class App 
{



    public static void main( String[] args )
    {
        MarketTray test = new MarketTray();
        test.MarketTrayDraw();
        test.selectRow(2);

        System.out.println("After selection");
        test.MarketTrayDraw();
        System.out.println("");
        test.ResourceDraw();

        System.out.println("Second rownd");
        test.MarketTrayDraw();
        test.selectColumn(3);

        System.out.println("After selection");
        test.MarketTrayDraw();
        System.out.println("");
        test.ResourceDraw();

    }


}

