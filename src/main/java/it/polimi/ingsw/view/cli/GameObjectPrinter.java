package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.DevCard;
import it.polimi.ingsw.model.LeaderCard;
import it.polimi.ingsw.model.PersonalBoard;
import it.polimi.ingsw.model.enumclasses.CliColors;

import java.util.ArrayList;
import java.util.List;

public class GameObjectPrinter {
    //TODO FOR LORENZO
    //List<DevCard>= new ArrayList<>();
    public static String printDevCard(DevCard card){

        //stringbuilder, for loop, string format etc
        //to substitute placeholder characters inside string with devcard values, it will look something like:
        //strinformatter("%d-16", card.getCost()) etc in necessary places
        return null;
    }

    // argument can be Shelf[]
    public static String printWarehouse(PersonalBoard board){ return null;
    }

    // argument can be Resources, because stronbox is represented with a single resources object in personal board
    public static String printStrongbox(PersonalBoard board){
        return null;
    }

    public static String printDevSlots(PersonalBoard board){
        return null;
    }

    public static String printDevCard(LeaderCard card){
        return null;
    }

    // this is an example of the DevCard console display that can be applied to all the game objects inside the cli
    //(without the proper setters and getters)
    static void DevCardScreen(int frameSize, int cellSize){
        int DevCardSize=frameSize*cellSize;
        char c;
        for (int i=0; i<DevCardSize;i++){
            for (int k=0;k<DevCardSize;k++){
                if (i%DevCardSize==0||k%DevCardSize==0)
                    c='-';
                else
                    c=' ';
                System.out.println(c);
            }
            System.out.println();
        }
    }

    public static String padder(String text, int n ){
        return String.format("%-"+n+"text",text);
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder=new StringBuilder();


        return null;
    };



    CliColors blueCard=CliColors.blue;
    CliColors yellowCard=CliColors.yellow;
    CliColors greenCard=CliColors.green;
    CliColors purpleCard=CliColors.purpleBright;
}
