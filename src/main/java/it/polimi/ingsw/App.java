package it.polimi.ingsw;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enumclasses.DevCardColor;
import it.polimi.ingsw.model.enumclasses.ResType;
import it.polimi.ingsw.model.specialability.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
/*
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

        MyFunc();
          Game theGame = new Game();
          GameController theGameController = new GameController(theGame);

         //this will be surrounded by proper exceptions
         //theGameController.startGame();

          DeserialLeaderCard();

          Resources devCardOneLHS = new Resources(0, 0, 0, 1, 0);
          Resources devCardOneRHS = new Resources(0, 0, 0, 0, 1);
          Resources devCardOneCost = new Resources(0, 2, 0, 0, 0);
          DevCard devcardOne = new DevCard(1, DevCardColor.BLUE, devCardOneLHS, devCardOneRHS, devCardOneCost, 1);
          serializeDevCard(devcardOne);

    }*/


    private static void serializeDevCard(DevCard devCardtoJson) {
        Gson gson = new Gson();
        String devCardString = gson.toJson(devCardtoJson);
        System.out.println(devCardString);
    }


    //Method that desirialize the leader card from the json Leader cards file and instantiate the array of the cards.
    private static void DeserialLeaderCard()
    {

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

                for (int i = 0; i < extractedJson.length; i++){
                    // System.out.println(extractedJson[i].getAbility().getType());
                    listOfCards.add(new LeaderCard(extractedJson[i].getRequirements(), extractedJson[i].getVictoryPoints(), extractedJson[i].getAbility()));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }

          /*  for (int i = 0; i < listOfCards.size(); i++) {
                //System.out.println(listOfCards.get(i).getAbility());
                System.out.println(listOfCards.get(i).getAbility().getEffect());
                for (int j = 0; j <  listOfCards.get(i).getRequirements().size(); j++) {
                    try{
                         System.out.println(listOfCards.get(i).getRequirements().get(j).getReq()+"  "+ listOfCards.get(i).getRequirements().get(j).getNumber());
                    }catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
                //listOfCards.get(i).getRequirementse();
                System.out.println();
            }*/

    }
}




