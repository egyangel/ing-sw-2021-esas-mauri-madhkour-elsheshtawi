package it.polimi.ingsw;

import it.polimi.ingsw.model.MarketTray;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumclasses.ResType;

public class App 
{
    public static void main( String[] args )
    {
//        MarketTray test = new MarketTray();
//        System.out.println("Initial MarketTray Position:, rows and columns start at 1");
//        test.MarketTrayDraw();
//
//        System.out.println("After 2nd row selection");
//        test.selectRow(2);
//        test.MarketTrayDraw();
//        test.ResourceDraw();
//
//        System.out.println("After 3rd column selection");
//        test.selectColumn(3);
//        test.MarketTrayDraw();
//        test.ResourceDraw();

        Player playerOne = new Player();
        playerOne.giveInitialStrongboxResources(1,2,1,2,0);
        playerOne.printStrongBox();
        playerOne.useDefProd(ResType.COIN, ResType.SERVANT, ResType.SHIELD);
        playerOne.printStrongBox();
    }
}

