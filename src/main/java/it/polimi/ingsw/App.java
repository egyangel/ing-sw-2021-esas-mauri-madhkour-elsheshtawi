package it.polimi.ingsw;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enumclasses.*;
import it.polimi.ingsw.model.specialability.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import it.polimi.ingsw.utility.JsonConverter;


public class App {

    private static List<ResType> values= new ArrayList<>() ;
    private static List<ResType> values1= new ArrayList<>() ;
    private static Shelf[] warehouse= new Shelf[3];

    private static void MyFunc() {

        values.add(ResType.COIN);
        values.add(ResType.COIN);
        values1.add(ResType.STONE);
        values1.add(ResType.STONE);
        values1.add(ResType.STONE);

        warehouse[0] = new Shelf(1);
        warehouse[1] = new Shelf(2);
        warehouse[2] = new Shelf(3);

       System.out.println(warehouse[1].PutResource(values));
       System.out.println(warehouse[2].PutResource(values1));
       System.out.println(warehouse[1].SwapShelf(warehouse[2] ));

    }

    public static void main(String[] args) throws IOException {
        Game game = new Game();
//        game.printDevCardMatrix();
        game.printLeaderCards();
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
//
//        Player playerOne = new Player();
//        playerOne.giveInitialStrongboxResources(1, 2, 1, 2, 0);
//        playerOne.printStrongBox();
//        playerOne.useDefProd(ResType.COIN, ResType.SERVANT, ResType.SHIELD);
//        playerOne.printStrongBox();

//        MyFunc();
//          Game theGame = new Game();
//          GameController theGameController = new GameController(theGame);

//         this will be surrounded by proper exceptions
//         theGameController.startGame();


    }
}




