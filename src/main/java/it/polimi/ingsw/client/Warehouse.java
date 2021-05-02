package it.polimi.ingsw.client;


import it.polimi.ingsw.server.model.Resources;
import it.polimi.ingsw.server.model.enumclasses.ResType;

import java.util.Scanner;

public class Warehouse {
    public static void main(String[] args) {

        char [][] warehouse =  {{' ',' ',' ','[',' ',']',' ',' ',' '},
                                {' ',' ','[',' ',']','[',' ',']',' ',' '},
                                {' ','[',' ',']','[',' ',']','[',' ',']',' '}
        };
        Resources sym;
        Scanner pos= new Scanner(System.in);
        Scanner fig= new Scanner(System.in); //fig=sym(?)
        System.out.println("Where do you want to place your resources? (1-6)");

        int position=pos.nextInt();

        System.out.println("which resources do you want to add?(inputs:a=✞(faith)\" +\n" +
                "                \",b=⚇(servant),\" +\n" +
                "                \"c=⛨(shield), d=⨎(coin),e=⬠(stone)\"");
        // input  for the resources
        System.out.println(position);
        placeResource(warehouse,position);
        shoWarehouse (warehouse);
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
        //to compile
        return null;
    }

    public static void shoWarehouse(char [][] warehouse){
        for (char[] row : warehouse){
            for (char col:row){
                System.out.println(col);
            }
            System.out.println();
        } }
}
