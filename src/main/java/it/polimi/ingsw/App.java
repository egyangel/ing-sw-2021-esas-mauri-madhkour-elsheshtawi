package it.polimi.ingsw;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.DevCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.LeaderCard;
import it.polimi.ingsw.model.Resources;
import it.polimi.ingsw.model.enumclasses.DevCardColor;
import it.polimi.ingsw.model.specialability.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import com.google.gson.*;
public class App {

    public static void main(String[] args) throws IOException {
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
        Game theGame = new Game();
        GameController theGameController = new GameController(theGame);

        // this will be surrounded by proper exceptions
        // theGameController.startGame();

        myFunction2();
        Resources devCardOneLHS = new Resources(0, 0, 0, 1, 0);
        Resources devCardOneRHS = new Resources(0, 0, 0, 0, 1);
        Resources devCardOneCost = new Resources(0, 2, 0, 0, 0);
        DevCard devcardOne = new DevCard(1, DevCardColor.BLUE, devCardOneLHS, devCardOneRHS, devCardOneCost, 1);
        serializeDevCard(devcardOne);
    }

    private static void serializeDevCard(DevCard devCardtoJson) {
        Gson gson = new Gson();
        String devCardString = gson.toJson(devCardtoJson);
        System.out.println(devCardString);
    }


    static void myFunction2() {
        List<LeaderCard> listOfCards = new ArrayList<>();
        LeaderCard[] extractedJson = new LeaderCard[0];

        try (FileReader reader = new FileReader("src/main/java/it/polimi/ingsw/resources/LeaderCards.json")) {
            
                Gson g = (new GsonBuilder()).registerTypeAdapterFactory(
                        RuntimeTypeAdapterFactory
                                .of(SpecialAbility.class, "type")
                                .registerSubtype(Discount.class, "Discount")
                                .registerSubtype(AdditionalProduction.class, "AdditionalProduction")
                                .registerSubtype(ConvertWhiteMarble.class, "ConvertWhiteMarble")
                                .registerSubtype(ExstraSlot.class, "ExstraSlot")
                ).create();
                        
                extractedJson = g.fromJson(reader, LeaderCard[].class);
            for (int i = 0; i < extractedJson.length; i++) {
                // System.out.println(extractedJson[i].getAbility().getType());
                listOfCards.add(new LeaderCard(extractedJson[i].getRequirements(), extractedJson[i].getVictoryPoints(), extractedJson[i].getAbility()));

            }

            } catch (FileNotFoundException e) {
             e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < listOfCards.size(); i++) {
            System.out.println(listOfCards.get(i).getAbility());
        }

}
        public static TypeAdapterFactory getUserTypeAdapter() {
            return RuntimeTypeAdapterFactory
                    .of(SpecialAbility.class, "type")
                    .registerSubtype(Discount.class, "Discount")
                    .registerSubtype(AdditionalProduction.class, "AdditionalProduction")
                    .registerSubtype(ConvertWhiteMarble.class, "ConvertWhiteMarble")
                    .registerSubtype(ExstraSlot.class, "ExstraSlot");
        }

        public static Gson getGsonWithTypeAdapters() {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapterFactory(getUserTypeAdapter());
            return builder.create();
        }

    }


