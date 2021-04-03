package it.polimi.ingsw;

import com.google.gson.Gson;
import it.polimi.ingsw.model.DevCard;
import it.polimi.ingsw.model.LeaderCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Resources;
import it.polimi.ingsw.model.enumclasses.DevCardColor;
import it.polimi.ingsw.model.enumclasses.ResType;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
// import org.*;

public class App {
    public static void main(String[] args ) throws IOException {
//        MarketTray test = new MarketTray();
//        System.out.println("Initial MarketTray Position:, rows and columns start at 1");
//        test.MarketTrayDraw();
//        System.out.println("After 2nd row selection");
//        test.selectRow(2);
//        test.MarketTrayDraw();
//        test.ResourceDraw();
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
        myFunction();
        Resources devCardOneLHS = new Resources(0,0,0,1,0);
        Resources devCardOneRHS = new Resources(0,0,0,0,1);
        Resources devCardOneCost = new Resources(0,2,0,0,0);
        DevCard devcardOne = new DevCard(1, DevCardColor.BLUE, devCardOneLHS, devCardOneRHS, devCardOneCost,1);
        serializeDevCard(devcardOne);
    }

    private static void serializeDevCard(DevCard devCardtoJson){
        Gson gson = new Gson();
        String devCardString = gson.toJson(devCardtoJson);
        System.out.println(devCardString);
    }
    static void myFunction() {

        List<LeaderCard> listOfCards = new ArrayList<>();
        try (FileReader reader = new FileReader("src/main/java/resouces/LeaderCard.json")) {



            LeaderCard[] extractedJson = new Gson().fromJson(reader, LeaderCard[].class);
            //  System.out.println(extractedJson.length);
            for (int i = 0; i < extractedJson.length; i++) {
                // System.out.println(extractedJson[i].getAbility().getType());
                listOfCards.add(new LeaderCard(extractedJson[i].getRequirements(), extractedJson[i].getVictoryPoints(), extractedJson[i].getAbility()));

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (LeaderCard listOfCard : listOfCards) {
            // System.out.println(extractedJson[i].getAbility().getType());

            System.out.println(listOfCard.getAbility());
        }
    }
}

