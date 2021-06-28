package it.polimi.ingsw.utility;

import it.polimi.ingsw.model.DevCard;
import it.polimi.ingsw.model.DevSlot;
import it.polimi.ingsw.model.Resources;
import it.polimi.ingsw.model.Shelf;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static it.polimi.ingsw.network.server.Server.SERVER_MIN_PORT;
import static it.polimi.ingsw.network.server.Server.SERVER_MAX_PORT;

public class InputConsumer {

    public static String getIP(Scanner scanner, PrintWriter out) {
        String ip;
        ip = scanner.nextLine();
        while (!isValidIp(ip)) {
            out.println("This is not a valid IPv4 address. Please try again:");
            ip = scanner.nextLine();
        }
        return ip;
    }

    public static int getPortNumber(Scanner scanner, PrintWriter out) {
        int portNumber;
        portNumber = scanner.nextInt();
        while (!isValidPort(portNumber)) {
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

    private static boolean isValidPort(int portNumber) {
        return (portNumber >= SERVER_MIN_PORT && portNumber <= SERVER_MAX_PORT);
    }

    public static String getUserName(Scanner scanner, PrintWriter out) {
        String username;
        username = scanner.nextLine();
        while (!isValidUsername(username)) {
            out.println("Username must begin with letter and can be max 10 characters. Please try again:");
            username = scanner.nextLine();
        }
        return username;
    }

    private static boolean isValidUsername(String name) {
        return (Character.isLetter(name.charAt(0))) && (name.length() <= 10);
    }

    public static String getStartOrCancel(Scanner scanner, PrintWriter out) {
        String input;
        input = scanner.nextLine().toLowerCase();
        while (!input.equals("start") && !input.equals("exit")) {
            out.println("Invalid input, please enter 'start' or 'exit'");
            input = scanner.nextLine().toLowerCase();
        }
        return input;
    }

    public static Integer getNumberOfPlayers(Scanner scanner, PrintWriter out) {
        Integer input;
        input = Integer.parseInt(scanner.nextLine());
        while (input < 1 || input > 4) {
            out.println("Invalid number of players, please enter a number between 1 and 4:");
            input = Integer.parseInt(scanner.nextLine());
        }
        return input;
    }

    public static Integer getANumberBetween(Scanner scanner, PrintWriter out, int min, int max) {
        Integer input = -1;
        try {
            input = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
        }
        while (input < min || input > max) {
            out.println("Invalid input, please enter a number between " + min + " and " + max);
            try {
                input = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                out.println("Invalid input, please enter a number between " + min + " and " + max);
            }
        }
        return input;
    }

    public static Resources.ResType getResourceType(Scanner in, PrintWriter out) {
        Resources.ResType resType;
        String input;
        out.println("Enter a resource type name: [COIN] [STONE] [SERVANT] [SHIELD]");
        input = in.nextLine().toUpperCase();
        while (!((input.equals("COIN")) || (input.equals("STONE")) || (input.equals("SERVANT")) || (input.equals("SHIELD")))) {
            out.println("Invalid input.");
            out.println("Enter a resource type name: [COIN] [STONE] [SERVANT] [SHIELD]");
            input = in.nextLine().toUpperCase();
        }
        resType = Resources.ResType.valueOf(input);
        return resType;
    }

    public static Resources.ResType getAllResourceType(Scanner in, PrintWriter out) {
        Resources.ResType resType;
        String input;
        out.println("Enter a resource type name: [COIN] [STONE] [SERVANT] [SHIELD] [FAITH]");
        input = in.nextLine().toUpperCase();
        while (!((input.equals("COIN")) || (input.equals("STONE")) || (input.equals("SERVANT")) || (input.equals("SHIELD")) || (input.equals("FAITH")))) {
            out.println("Invalid input.");
            out.println("Enter a resource type name: [COIN] [STONE] [SERVANT] [SHIELD] [FAITH]");
            input = in.nextLine().toUpperCase();
        }
        resType = Resources.ResType.valueOf(input);
        return resType;
    }

    public static Resources.ResType getATypeAmongSet(Scanner in, PrintWriter out, List<Resources.ResType> resTypeList) {
        List<String> resTypeStringList = new ArrayList<>();
        String string = "Please enter one of the options: ";
        for (Resources.ResType resType : resTypeList) {
            string += "[" + resType.toString() + "] ";
            resTypeStringList.add(resType.toString());
        }
        out.println(string);
        String input = in.nextLine().toUpperCase();
        while (!(resTypeStringList.contains(input))) {
            out.println("Invalid input.");
            out.println(string);
            input = in.nextLine().toUpperCase();
        }
        return Resources.ResType.valueOf(input);
    }

    public static String getMarketRowColumnIndex(Scanner in, PrintWriter out) {
        out.println("[c-1] [c-2] [c-3] [c-4] [r-1] [r-2] [r-3]");
        out.println("Enter column/row and index as shown above, indexes start at left column and top row:");
        String input;
        input = in.nextLine();
        try {
            char firstLetter = input.charAt(0);
            char midLetter = input.charAt(1);
            int number = Integer.parseInt(String.valueOf(input.charAt(2)));
            while (!((firstLetter == 'c' || firstLetter == 'r') && (midLetter == '-') && (number > 0 && number < 5) && (!input.equals("r-4")))) {
                out.println("Invalid input.");
                out.println("Enter one of the inputs as shown: [c-1] [c-2] [c-3] [c-4] [r-1] [r-2] [r-3]");
                input = in.nextLine();
                firstLetter = input.charAt(0);
                midLetter = input.charAt(1);
                number = Integer.parseInt(String.valueOf(input.charAt(2)));
            }
            return input;
        } catch (Exception $e) {
            out.println("Invalid input. (tip: showd be composer of 3 character C or R + '-' + the number of the column or the row. EX: C-1)");
            out.println("Enter one of the inputs as shown: [c-1] [c-2] [c-3] [c-4] [r-1] [r-2] [r-3]");
            return getMarketRowColumnIndex(in, out);
        }


    }

    public static boolean getYesOrNo(Scanner in, PrintWriter out) {
        out.println("Please enter 'yes' or 'no':");
        String input = in.nextLine();
        while (!(input.equals("yes") || (input.equals("no")))) {
            out.println("Invalid input.");
            out.println("Please enter 'yes' or 'no':");
            input = in.nextLine();
        }
        if (input.equals("yes")) return true;
        else return false;
    }

    public static Shelf.shelfPlace getShelfPlace(Scanner in, PrintWriter out) {
        out.println("Please enter one of the options: [top] [middle] [bottom]");
        String input = in.nextLine();
        while (!(input.equals("top") || (input.equals("middle")) || (input.equals("bottom")))) {
            out.println("Invalid input.");
            out.println("Please enter 'top' or 'middle' or 'bottom':");
            input = in.nextLine();
        }
        return Shelf.shelfPlace.valueOf(input.toUpperCase());
    }

    public static String getColorAndLevel(Scanner in, PrintWriter out) {
        out.println("Enter the color and level of the development card you want to buy:");
        out.println("Example inputs: [blue-1] [green-2] [yellow-3] [purple-1]");
        String input;
        input = in.nextLine().toUpperCase();
        String[] parts = input.split("-");
        String color = parts[0];
        String level = parts[1];
        int number = Integer.parseInt(level);
        while (!(DevCard.CardColor.contains(color) && (number >= 1) && (number <= 3))) {
            out.println("Invalid input, please enter an input as shown in examples without brackets");
            out.println("Example inputs: [blue-1] [green-2] [yellow-3] [purple-1]");
            input = in.nextLine().toUpperCase();
            parts = input.split("-");
            color = parts[0];
            level = parts[1];
            number = Integer.parseInt(level);
        }
        return input;
    }

    public static DevSlot.slotPlace getSlotPlace(Scanner in, PrintWriter out, List<DevSlot.slotPlace> placeList) {
        String placeString = placeList.stream().map(Object::toString).collect(Collectors.joining(" "));
        out.println("Please enter one of the options: " + placeString);
        String input = in.nextLine().toUpperCase();
        List<String> slotAsStrings = new ArrayList<>();
        slotAsStrings.add((DevSlot.slotPlace.LEFT.name()));
        slotAsStrings.add((DevSlot.slotPlace.CENTER.name()));
        slotAsStrings.add((DevSlot.slotPlace.RIGHT.name()));
        while(!slotAsStrings.contains(input) && !placeList.contains(DevSlot.slotPlace.valueOf(input))) {
            out.println("Invalid input.");
            out.println("Please enter one of the options: " + placeString);
            input = in.nextLine().toUpperCase();
        }
        return DevSlot.slotPlace.valueOf(input);
    }
}
