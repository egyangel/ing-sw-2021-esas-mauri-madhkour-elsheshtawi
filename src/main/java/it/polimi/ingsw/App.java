package it.polimi.ingsw;

import it.polimi.ingsw.model.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class App {



    public static void main(String[] args) throws IOException {
        Game game = new Game();

//        game.printSpecialAbilities();
//        Requirement aReq = new Requirement(Requirement.reqType.TWOCARD, DevCard.CardColor.BLUE, DevCard.CardColor.GREEN);
//        System.out.println(JsonConverter.toJson(aReq));

//        SpecialAbility aAbi = new SpecialAbility(SpecialAbility.AbilityType.EXSTRASLOT, Resources.ResType.COIN);
//        System.out.println(JsonConverter.toJson(aAbi));

//        LeaderCard aCard = new LeaderCard(aReq, 10, aAbi);
//        System.out.println(JsonConverter.toJson(aCard));

//

        MarketTray test = new MarketTray();
        System.out.println("Initial MarketTray Position:, rows and columns start at 1");
        test.MarketTrayDraw();

        System.out.println("After 2nd row selection");
        test.selectRow(2);
        test.MarketTrayDraw();
//        test.ResourceDraw();

        System.out.println("After 3rd column selection");
        test.selectColumn(3);
        test.MarketTrayDraw();
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




