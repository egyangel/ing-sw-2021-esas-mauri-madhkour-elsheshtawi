package it.polimi.ingsw.model;


import it.polimi.ingsw.model.EnumClasses.MarbleColor;

import java.util.*;

public class MarketTray {
    private final int row=3;
    private final int column=4;
    private Marble[] marbles=new Marble[13];
    private Marble[][] marketTray=new Marble[3][4];


    public MarketTray(){
       /*
        marbles[0] = new Marble(MarbleColor.WHITE);
        marbles[1] = new Marble(MarbleColor.WHITE);
        marbles[2] = new Marble(MarbleColor.WHITE);
        marbles[3] = new Marble(MarbleColor.WHITE);
        marbles[4] = new Marble(MarbleColor.BLUE);
        marbles[5] = new Marble(MarbleColor.BLUE);
        marbles[6] = new Marble(MarbleColor.GREY);
        marbles[7] = new Marble(MarbleColor.GREY);
        marbles[8] = new Marble(MarbleColor.YELLOW);
        marbles[9] = new Marble(MarbleColor.YELLOW);
        marbles[10] = new Marble(MarbleColor.PURPLE);
        marbles[11] = new Marble(MarbleColor.PURPLE);
        marbles[12] = new Marble(MarbleColor.RED);
        */

        //initialization of the marbles
        for(int i=0;i<13;i++){
            if(i<4)marbles[i]=new Marble(MarbleColor.WHITE);
            if(i==4 || i==5)marbles[i]=new Marble(MarbleColor.BLUE);
            if(i==6 || i==7)marbles[i]=new Marble(MarbleColor.GREY);
            if(i==8 || i==9)marbles[i]=new Marble(MarbleColor.YELLOW);
            if(i==10 || i==11)marbles[i]=new Marble(MarbleColor.PURPLE);
            if(i==12)marbles[i]=new Marble(MarbleColor.RED);
        }
        MarblePositioning();

    }
    //positioning the murbles in a random way at the beginning of the game
    private void MarblePositioning() {
        int k=0;
        int out=-1;
        ArrayList<Integer> temp_position = new ArrayList<>();

        try
        {
            out=randomPos(temp_position);

            for(int i=0;i<row;i++){
                for(int j=0;j<column;j++){
                    marketTray[i][j]=marbles[temp_position.get(k)];
                    k++;
                    System.out.print(String.format("%-16s",marketTray[i][j].getColor()));
                  }
                System.out.println(" ");
            }
        }catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("The push of the marble into the tray brought an IndexOut");
        }

    }
    private static int randomPos(ArrayList<Integer> temp_position){
        int temp=-1;

        temp_position.add((int)((Math.random() * ((13) + 1)) + 0));

        while(temp_position.size()<13) {
            temp=(int)((Math.random() * ((13) + 1)) + 0);
            if (!temp_position.contains(temp))
                temp_position.add((temp));
        }return temp;
        /*for (Integer integer : temp_position) {

            System.out.println(integer);
        }*/
    }


    public Resources selectRow(int row){

        return null;
    }
    public Resources selectColumn(int column){

        return null;
    }

}
