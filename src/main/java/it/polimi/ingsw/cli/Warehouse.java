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
            for (char c:row){
                System.out.println(c);
            }
            System.out.println();
        } }
}
