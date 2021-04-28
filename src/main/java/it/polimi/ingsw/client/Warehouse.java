package it.polimi.ingsw.cli;

public class Warehouse {
    public static void main(String[] args) {

        char [][] warehouse =  {{'*','*','*','[',' ',']','*','*','*'},
                                {'*','*','[',' ',']','[',' ',']','*','*'},
                                {'*','[',' ',']','[',' ',']','[',' ',']','*'}
        };

        shoWarehouse (warehouse);

    }
    public static void shoWarehouse(char [][] warehouse){
        for (char[] row : warehouse){
            for (char col:row){
                System.out.println(col);
            }
            System.out.println();
        } }
}
