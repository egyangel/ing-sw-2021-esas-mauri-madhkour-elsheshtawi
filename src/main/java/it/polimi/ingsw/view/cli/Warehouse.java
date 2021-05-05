package it.polimi.ingsw.view.cli;


import it.polimi.ingsw.model.Resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Warehouse {

    static ArrayList<Integer> Position=new ArrayList<Integer>();
    public static void main(String[] args) {

        char [][] warehouse =  {{' ',' ',' ','[',' ',']',' ',' ',' '},
                                {' ',' ','[',' ',']','[',' ',']',' ',' '},
                                {' ','[',' ',']','[',' ',']','[',' ',']',' '}
        };
        while (true){
        Resources sym;
        Scanner pos= new Scanner(System.in);
        System.out.println("Where do you want to place your resources? (1-6)");
        int position=pos.nextInt();
        while (Position.contains(position)){
            System.out.println("position taken. enter correct position");
            position = pos.nextInt();
        }
        Scanner fig= new Scanner(System.in); //fig=sym(?)
        System.out.println("which resources do you want to add?(inputs:a=✞(faith)\" +\n" +
                "                \",b=⚇(servant),\" +\n" +
                "                \"c=⛨(shield), d=⨎(coin),e=⬠(stone)\"");
        // input  for the resources...int or Resources type??
        System.out.println(position);
        placeResource(warehouse,position);
        checkWarehouseRows();
        shoWarehouse (warehouse);}


    }

    public static void placeResource(char[][] warehouse, int position){

        char sym=' ';


        //Resources sym.add(warehouse);
        //adding a resource in warehouse..don't know how to do the proper binding from Resources.java

        switch (position){
            case 1:
                warehouse[0][4]=sym;
                break;
            case 2:
                warehouse[1][3]=sym;
                break;
            case 3:
                warehouse[1][6]=sym;
                break;
            case 4:
                warehouse[2][2]=sym;
                break;
            case 5:
                warehouse[2][5]=sym;
                break;
            case 6:
                warehouse[2][8]=sym;
                break;
            default:
                break;
        }
    }

    public static String checkWarehouseRows(){
        List middleRow= Arrays.asList(2,3);
        List bottomRow= Arrays.asList(4,5,6);

        List<List> correct= new ArrayList<List>();

        correct.add(middleRow);
        correct.add(bottomRow);

        for (List a:correct){
            if (Position.containsAll(a)){
                return "same Resource on row. correct";
            }

        }
        return "";
    }

    public static void shoWarehouse(char [][] warehouse){
        for (char[] row : warehouse){
            for (char col:row){
                System.out.println(col);
            }
            System.out.println();
        } }
}
