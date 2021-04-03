package it.polimi.ingsw;

import com.google.gson.Gson;
import it.polimi.ingsw.model.LeaderCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.enumclasses.ResType;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.*;

public class App
{
    public static void main(String[] args ) throws IOException {
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
        playerOne.giveInitialStrongboxResources(1, 2, 1, 2, 0);
        playerOne.printStrongBox();
        playerOne.useDefProd(ResType.COIN, ResType.SERVANT, ResType.SHIELD);
        playerOne.printStrongBox();

        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");
        System.out.println("");


        Gson bj = new Gson();
/*
        LeaderCard[] Lc = new LeaderCard[16];
        int i = 0;

        try (FileReader reader = new FileReader("src/main/java/resouces/Ability.txt")) {


            Scanner in = new Scanner(reader);

            List<String> token = new ArrayList<>();
            while (in.hasNextLine()) {

                String line = in.nextLine();
                Scanner t = new Scanner(line);
                while (t.hasNext()) {
                 token.add(t.next());

                }
                Lc[i] = new LeaderCard(token.get(0),token.get(1));
                i++;
                token.clear();
            }
        }
        catch(FileNotFoundException e){ e.printStackTrace(); }



       for(int j=0;j<16;j++)System.out.println(+j+1 +" Card ability is : "+Lc[j].getSpecialAbility()+"  VictoryPoint : "+Lc[j].getVictoryPoints());

    }*/
  }
}

