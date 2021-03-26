package it.polimi.ingsw.model;


import it.polimi.ingsw.model.enumclasses.MarbleColor;

import java.util.*;

public class MarketTray {
    private final int row=3;
    private final int column=4;
    private MarbleColor OutMarble;
    private MarbleColor[] marbles =new MarbleColor[13];
    private MarbleColor[][] marketTray=new MarbleColor[3][4];


    public MarketTray(){

        marbles[0]  = MarbleColor.WHITE;
        marbles[1]  = MarbleColor.WHITE;
        marbles[2]  = MarbleColor.WHITE;
        marbles[3]  = MarbleColor.WHITE;
        marbles[4]  = MarbleColor.BLUE;
        marbles[5]  = MarbleColor.BLUE;
        marbles[6]  = MarbleColor.GREY;
        marbles[7]  = MarbleColor.GREY;
        marbles[8]  = MarbleColor.YELLOW;
        marbles[9]  = MarbleColor.YELLOW;
        marbles[10] = MarbleColor.PURPLE;
        marbles[11] = MarbleColor.PURPLE;
        marbles[12] = MarbleColor.RED;


        MarblePositioning();

    }
    //positioning the murbles in a random way at the beginning of the game
    private void MarblePositioning() {
        int k=0;
        ArrayList<Integer> temp_position = new ArrayList<>();

        for(int i=0;i<13;i++)temp_position.add(i);
        Collections.shuffle(temp_position);

        OutMarble=marbles[temp_position.get(temp_position.size()-1)];

        try
        {
              for(int i=0;i<row;i++){
                for(int j=0;j<column;j++){
                    marketTray[i][j]=marbles[temp_position.get(k)];
                    k++;
                    System.out.print(String.format("%-16s",marketTray[i][j].getColor()));
                  }
                System.out.println(" ");
            }
        }catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("The push of the marbles into the tray brought an IndexOut");
        }

    }

    public Resources selectRow(int row){

        return null;
    }
    public Resources selectColumn(int column){

        return null;
    }

}
