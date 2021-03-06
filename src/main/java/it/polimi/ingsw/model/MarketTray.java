package it.polimi.ingsw.model;


import it.polimi.ingsw.model.enumclasses.MarbleColor;

import java.util.*;
/**
 * Market Tray  ,it is the the representation of the market tray.
 * It simulate the market of the real game
 * @author
 * */
public class MarketTray {
    private final int row = 3;
    private final int column = 4;
    private MarbleColor OutMarble;
    private MarbleColor[] marbles = new MarbleColor[13];
    private MarbleColor[][] marketTray = new MarbleColor[3][4];
/**
 *Constructor create the 13 marble with the proper color

 */
    public MarketTray() {

        marbles[0] = MarbleColor.WHITE;
        marbles[1] = MarbleColor.WHITE;
        marbles[2] = MarbleColor.WHITE;
        marbles[3] = MarbleColor.WHITE;
        marbles[4] = MarbleColor.BLUE;
        marbles[5] = MarbleColor.BLUE;
        marbles[6] = MarbleColor.GREY;
        marbles[7] = MarbleColor.GREY;
        marbles[8] = MarbleColor.YELLOW;
        marbles[9] = MarbleColor.YELLOW;
        marbles[10] = MarbleColor.PURPLE;
        marbles[11] = MarbleColor.PURPLE;
        marbles[12] = MarbleColor.RED;

        MarblePositioning();
    }
/**
 * Method that handle the initial phase of the game.
 * Putting the marble on the tray(matrix) in a random way
 *
 * */
    //positioning the murbles in a random way at the beginning of the game
    private void MarblePositioning() {
        int k = 0;
        ArrayList<Integer> temp_position = new ArrayList<>();

        for (int i = 0; i < 13; i++) temp_position.add(i);
        Collections.shuffle(temp_position);

        OutMarble = marbles[temp_position.get(temp_position.size() - 1)];

        try {
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < column; j++) {

                    marketTray[i][j] = marbles[temp_position.get(k)];
                    k++;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("The push of the marbles into the tray brought an IndexOut");
        }
    }

    /**
     * Method that handle the selection of the row
     * It change the tray (matrix) configuration by shifting on the left the element in that row,
     * The first(lefter one) goes out
     * @param row is the row selected by the player
     * */
    public List<MarbleColor> selectRow(int row) {
        List<MarbleColor> resources = new ArrayList<>();
        MarbleColor temp;
        if (row > 0 && row < 4) {
            temp = marketTray[row - 1][0];
            for (int j = 0; j < column; j++) resources.add(marketTray[row - 1][j]);
            for (int j = 0; j < column - 1; j++) marketTray[row - 1][j] = marketTray[row - 1][j + 1];
            marketTray[row - 1][column - 1] = OutMarble;
            OutMarble = temp;
            return resources;
        } else
            return null;
    }

   /**
     * Method that handle the selection of the column
     * It change the tray (matrix) configuration by shifting the element in that column,
     * The first(TOp one) goes out
     * @param column is the column selected by the player
     * */
   public List<MarbleColor> selectColumn(int column) {
       List<MarbleColor> resources = new ArrayList<>();
       MarbleColor temp;
       if (column > 0 && column < 5) {
           temp = marketTray[0][column - 1];
           for (int i = 0; i < row; i++) resources.add(marketTray[i][column - 1]);
           for (int i = 0; i < row - 1; i++) marketTray[i][column - 1] = marketTray[i + 1][column - 1];
           marketTray[row - 1][column - 1] = OutMarble;
           OutMarble = temp;
           return resources;
       } else
           return null;
   }
    /**
     * method that describe the market tray, show how it look like during the game
     * */
    public String describeMarketTray() {
        StringBuilder sb = new StringBuilder();
        sb.append("\u2554");
        for(int i = 0; i<14; i++){
            sb.append("\u2550");
        }
        sb.append(OutMarble.getAnsiCode() + " " +"\u2b24" + " " + MarbleColor.RESET);
        sb.append("\n");
        for (int i = 0; i < row; i++) {
            sb.append("\u2551");
            for (int j = 0; j < column; j++) {
                sb.append(marketTray[i][j].getAnsiCode() + " " +"\u2b24" + " " + MarbleColor.RESET);
            }
            sb.append("\u2190" + "\n");
        }
        sb.append("  \u2191  \u2191  \u2191  \u2191");
        return sb.toString();
    }
}