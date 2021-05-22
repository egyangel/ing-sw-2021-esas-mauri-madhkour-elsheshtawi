package it.polimi.ingsw.utility;

import it.polimi.ingsw.model.DevCard;
import it.polimi.ingsw.model.Resources;
import it.polimi.ingsw.model.Shelf;

import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Pattern;
import static it.polimi.ingsw.network.server.Server.SERVER_MIN_PORT;
import static it.polimi.ingsw.network.server.Server.SERVER_MAX_PORT;

// TODO modify this class later as to be usable for both CLI and GUI (dont do println(), instead abstract into view.showError())
public class InputConsumer {

    public static String getIP(Scanner scanner, PrintWriter out){
        String ip;
        ip = scanner.nextLine();
        while(!isValidIp(ip)){
            out.println("This is not a valid IPv4 address. Please try again:");
            ip = scanner.nextLine();
        }
        return ip;
    }

    public static int getPortNumber(Scanner scanner, PrintWriter out){
        int portNumber;
        portNumber = scanner.nextInt();
        while(!isValidPort(portNumber)){
            out.println("This is not a valid port number. Please try again:");
            portNumber = scanner.nextInt();
        }
        return portNumber;
    }

    private static boolean isValidIp(String input) {
        Pattern p = Pattern.compile("^"
                + "(((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}" // Domain name
                + "|"
                + "localhost" // localhost
                + "|"
                + "((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?))$"); // Ip

        return p.matcher(input).matches();
    }

    private static boolean isValidPort(int portNumber){
        return (portNumber >= SERVER_MIN_PORT && portNumber <= SERVER_MAX_PORT);
    }

    public static String getUserName(Scanner scanner, PrintWriter out){
        String username;
        username = scanner.nextLine();
        while(!isValidUsername(username)){
            out.println("Username must begin with letter and can be max 10 characters. Please try again:");
            username = scanner.nextLine();
        }
        return username;
    }

    private static boolean isValidUsername(String name){
        return (Character.isLetter(name.charAt(0))) && (name.length() <= 10);
    }

    public static String getStartOrCancel(Scanner scanner, PrintWriter out){
        String input;
        input = scanner.nextLine().toLowerCase();
        while(!input.equals("start") && !input.equals("exit")){
            out.println("Invalid input, please enter 'start' or 'exit'");
            input = scanner.nextLine().toLowerCase();
        }
        return input;
    }

    public static Integer getNumberOfPlayers(Scanner scanner, PrintWriter out) {
        Integer input;
        input = Integer.parseInt(scanner.nextLine());
        while (input < 1 || input > 4 ) {
            out.println("Invalid number of players, please enter a number between 1 and 4:");
            input = Integer.parseInt(scanner.nextLine());
        }
        return input;
    }

    public static Integer getANumberBetween(Scanner scanner, PrintWriter out, int min, int max) {
        Integer input;
        input = Integer.parseInt(scanner.nextLine());
        while (input < min || input > max) {
            out.println("Invalid input, please enter a number between " + min + " and " + max);
            input = Integer.parseInt(scanner.nextLine());
        }
        return input;
    }

    public static Resources.ResType getResourceType(Scanner in, PrintWriter out) {
        Resources.ResType resType;
        String input;
        out.println("Enter a resource type name: [COIN] [STONE] [SERVANT] [SHIELD]");
        input = in.nextLine().toUpperCase();
        while (!((input.equals("COIN")) || (input.equals("STONE")) || (input.equals("SERVANT")) || (input.equals("SHIELD")))){
            out.println("Invalid input.");
            out.println("Enter a resource type name: [COIN] [STONE] [SERVANT] [SHIELD]");
            input = in.nextLine().toUpperCase();
        }
        resType = Resources.ResType.valueOf(input);
        return resType;
    }

    public static String getMarketRowColumnIndex(Scanner in, PrintWriter out) {
        out.println("[C-1] [C-2] [C-3] [C-4] [R-1] [R-2] [R-3]");
        out.println("Enter column/row and index as shown above, indexes start at left column and top row:");
        String input;
        input = in.nextLine().toUpperCase();
        char firstLetter = input.charAt(0);
        char midLetter = input.charAt(1);
        int number = Integer.parseInt(String.valueOf(input.charAt(2)));
        while (!((firstLetter == 'C' || firstLetter == 'R') && (midLetter == '-') && (number > 0 && number < 5) && (!input.equals("R-4")))){
            out.println("Invalid input.");
            out.println("Enter one of the inputs as shown: [C-1] [C-2] [C-3] [C-4] [R-1] [R-2] [R-3]");
            input = in.nextLine().toUpperCase();
            firstLetter = input.charAt(0);
            midLetter = input.charAt(1);
            number = Integer.parseInt(String.valueOf(input.charAt(2)));
        }
        return input;
    }

    public static boolean getYesOrNo(Scanner in, PrintWriter out){
        out.println("Please enter 'yes' or 'no':");
        String input = in.nextLine().toUpperCase();
        while (!(input.equals("YES") || (input.equals("NO")))){
            out.println("Invalid input.");
            out.println("Please enter 'yes' or 'no':");
            input = in.nextLine().toUpperCase();
        }
        if (input.equals("YES")) return true;
        else return false;
    }

    public static String getSwapDiscardNo(Scanner in, PrintWriter out){
        out.println("Please enter one of the options: [SWAP] [DISCARD] [NO]");
        String input = in.nextLine().toUpperCase();
        while (!(input.equals("SWAP") || (input.equals("DISCARD")) || (input.equals("NO")))){
            out.println("Invalid input.");
            out.println("Please enter 'SWAP' or 'DISCARD' or 'NO':");
            input = in.nextLine().toUpperCase();
        }
        return input;
    }

    public static Shelf.shelfPlace getShelfPlace(Scanner in, PrintWriter out) {
        out.println("Please enter one of the options: [TOP] [MIDDLE] [BOTTOM]");
        String input = in.nextLine().toUpperCase();
        while (!(input.equals("TOP") || (input.equals("MIDDLE")) || (input.equals("BOTTOM")))){
            out.println("Invalid input.");
            out.println("Please enter 'TOP' or 'MIDDLE' or 'BOTTOM':");
            input = in.nextLine().toUpperCase();
        }
        return Shelf.shelfPlace.valueOf(input);
    }

    public static String getShelfOrDiscard(Scanner in, PrintWriter out){
        out.println("Please enter one of the options: [SHELF] [DISCARD]");
        String input = in.nextLine().toUpperCase();
        while (!(input.equals("SHELF") || (input.equals("DISCARD")))){
            out.println("Invalid input.");
            out.println("Please enter 'SHELF' or 'DISCARD':");
            input = in.nextLine().toUpperCase();
        }
        return input;
    }

    public static String getColorAndLevel(Scanner in, PrintWriter out) {
        out.println("Enter the color and level of the development card you want to buy:");
        out.println("Example inputs: [BLUE-1] [GREEN-2] [YELLOW-3] [PURPLE-1]");
        String input;
        input = in.nextLine().toUpperCase();
        String[] parts = input.split("-");
        String color = parts[0];
        String level = parts[1];
        int number = Integer.parseInt(level);
        while(!(DevCard.CardColor.contains(color) && (number >= 1) && (number <= 3))){
            out.println("Invalid input, please enter an input as shown in examples without brackets");
            out.println("Example inputs: [BLUE-1] [GREEN-2] [YELLOW-3] [PURPLE-1]");
            parts = input.split("-");
            color = parts[0];
            level = parts[1];
            number = Integer.parseInt(level);
        }
        return input;
    }
}
