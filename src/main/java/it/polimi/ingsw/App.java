package it.polimi.ingsw;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enumclasses.CliColors;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


public class App {

    

    public static void main(String[] args) throws IOException {
        /*int j=0;
        List<LeaderCard> prodLeaderCard = new ArrayList<>();
        List<DevCard> Card = new ArrayList<>();
        List<DevSlot>  slotAvailable = new ArrayList<>();
        List<DevSlot.slotPlace> placeList = new ArrayList<>( Arrays.asList(DevSlot.slotPlace.LEFT,DevSlot.slotPlace.CENTER,DevSlot.slotPlace.RIGHT));
        List<DevSlot> l=new ArrayList<>();
        Resources lhs = new Resources(Resources.ResType.COIN,2);
        Resources rhs = new Resources(Resources.ResType.SERVANT,2);
        Resources cost = new Resources(Resources.ResType.SHIELD,2);
        /////int level, CardColor color, Resources LHS, Resources RHS, Resources cost, int victoryPoints
        for(int i=0;i<3; i++){
           Card.add(new DevCard(1, DevCard.CardColor.BLUE,lhs,rhs,cost,5));
        }
        for(int i=0;i<3; i++) {
            l.add(new DevSlot(placeList.get(i)));

        }
        for(int i=0;i<1; i++) {

            l.get(i).putDevCard(Card.get(i));
        }
        while (j < 3 ) {
            //DevSlot temp= new DevSlot (placeList.get(j));
            System.out.println( l.get(j).getTopDevCard());
            j++;
        }*/
        //  Game game = new Game();
        /* Lorenzo
        //DevCardScreen(1,2);
         //better with a for loop
        System.out.println(CliColors "--------------");
        System.out.println("");
        //System.out.println(padder("|", 13) + "|"); not working properly..should create spaces based on the parameter n
        System.out.println("|            !");
        System.out.println("");

         *//*
        List<DevSlot.slotPlace> placeList = new ArrayList<>( Arrays.asList(DevSlot.slotPlace.LEFT,DevSlot.slotPlace.CENTER,DevSlot.slotPlace.RIGHT));
        String placeString = placeList.stream().map(Object::toString).collect(Collectors.joining(" "));

        System.out.println("Please enter one of the options: " + placeString);

        Scanner in = new Scanner(System.in);
        String input = in.nextLine().toUpperCase();
        while (!placeList.contains(DevSlot.slotPlace.getByName(input))){
            System.out.println("Invalid input.");
            System.out.println("Please enter one of the options: " + placeString);
            input = in.nextLine().toUpperCase();
        }*/
        //TODO  test of getDevSlotIndexs in Inputconsumer Class

        Scanner in = new Scanner(System.in);
        List<DevSlot.slotPlace>  slotAvailable = new ArrayList<>();
        int i = 0;
        String input;
        List<DevSlot> slotChoosen = new ArrayList<>();

        List<DevSlot.slotPlace> placeList = new ArrayList<>( Arrays.asList(DevSlot.slotPlace.LEFT,DevSlot.slotPlace.CENTER,DevSlot.slotPlace.RIGHT));
        String placeString = placeList.stream().map(Object::toString).collect(Collectors.joining(" "));

        System.out.println("How many slots do you want to activate?  : ");
        System.out.println("Example inputs: [1] [2] [3]");

        for(int u=0;u < 2; u++){
            slotAvailable.add(placeList.get(u));
        }
        for(int u=0;u < 2; u++){
            System.out.println( slotAvailable.get(u));
        }
        int numberOfSlots = Integer.parseInt(in.nextLine());
        while (numberOfSlots > 3 || numberOfSlots < 1){
            System.out.println("Invalid input.");
            System.out.println("Please enter number between 1 and 3");

            numberOfSlots = Integer.parseInt(in.nextLine());
        }

        System.out.println("Please enter the options: " + placeString);


         while(i < numberOfSlots){
            input = in.nextLine().toUpperCase();
            if (placeList.contains(DevSlot.slotPlace.getByName(input)) && !slotChoosen.contains(DevSlot.slotPlace.getByName(input))&& slotAvailable.contains(DevSlot.slotPlace.getByName(input)) ){
                slotChoosen.add(new DevSlot(DevSlot.slotPlace.getByName(input)));
                i ++;
            }else{
                System.out.println("Invalid input.");
                System.out.println("Please enter the options: " + placeString);
            }

        }




//        game.printSpecialAbilities();
//        Requirement aReq = new Requirement(Requirement.reqType.TWOCARD, DevCard.CardColor.BLUE, DevCard.CardColor.GREEN);
//        System.out.println(JsonConverter.toJson(aReq));

//        SpecialAbility aAbi = new SpecialAbility(SpecialAbility.AbilityType.EXSTRASLOT, Resources.ResType.COIN);
//        System.out.println(JsonConverter.toJson(aAbi));

//        LeaderCard aCard = new LeaderCard(aReq, 10, aAbi);
//        System.out.println(JsonConverter.toJson(aCard));

//

   /*     MarketTray test = new MarketTray();
        System.out.println("Initial MarketTray Position:, rows and columns start at 1");
        test.MarketTrayDraw();

        System.out.println("After 2nd row selection");
        test.selectRow(2);
        test.MarketTrayDraw();
//        test.ResourceDraw();

        System.out.println("After 3rd column selection");
        test.selectColumn(3);
        test.MarketTrayDraw();

    */
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




