package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.DevCard;
import it.polimi.ingsw.model.LeaderCard;
import it.polimi.ingsw.model.PersonalBoard;
import it.polimi.ingsw.model.enumclasses.CliColors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameObjectPrinter {
    //TODO FOR LORENZO
    List<DevCard> card= new ArrayList<>();
    public static String printDevCard(DevCard card){

        //stringbuilder, for loop, string format etc
        //to substitute placeholder characters inside string with devcard values, it will look something like:
        //strinformatter("%d-16", card.getCost()) etc in necessary places
        return null;
    }

    public GameObjectPrinter(int size){
        fill(size);
    }
    private void fill(int size){
        int nextColor =0;
        /*for (int i=0; i<size;i++){
            CliColors color;
            DevCard c=new DevCard(CliColors.values()[nextColor]);
            this.card.add(c);
            nextColor=(nextColor+1)%CliColors.values().length;
        }*/

    }
    public int getSize(){return card.size();}

    public DevCard Draw(){
        int count=card.size();
        if (count==0)
            return null;
        Random rand=new Random();
        int index= rand.nextInt(count);
        DevCard c =card.get(index);
        this.card.remove(c);
        return c;
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
        StringBuilder stringBuilder = new StringBuilder();
        int count= card.size();
        stringBuilder.append("# elements= "+ count+ "\n");
        int i=0;
        for (DevCard c:card){
            if (i!=0){
                if (i%10==0)
                    stringBuilder.append("\n");
                else stringBuilder.append("\n");
            }
            stringBuilder.append(c.toString());
            i++;
        }
        return stringBuilder.toString();

    }

    public void dump(){
        System.out.println(this);
    }



    CliColors blueCard=CliColors.blue;
    CliColors yellowCard=CliColors.yellow;
    CliColors greenCard=CliColors.green;
    CliColors purpleCard=CliColors.purpleBright;
}
