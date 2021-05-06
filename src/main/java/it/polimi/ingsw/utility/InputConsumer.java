package it.polimi.ingsw.utility;

import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Pattern;
import static it.polimi.ingsw.network.server.Server.SERVER_MIN_PORT;
import static it.polimi.ingsw.network.server.Server.SERVER_MAX_PORT;

// TODO modify this class later as to be usable for both CLI and GUI (dont do println(), instead abstract into view.showError())
public class InputConsumer {

    public static String getIP(Scanner scanner){
        String ip;
        ip = scanner.nextLine();
        while(!isValidIp(ip)){
            System.out.println("This is not a valid IPv4 address. Please try again:");
            ip = scanner.nextLine();
        }
        return ip;
    }

    public static int getPortNumber(Scanner scanner){
        int portNumber;
        portNumber = scanner.nextInt();
        while(!isValidPort(portNumber)){
            System.out.println("This is not a valid port number. Please try again:");
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

    public static String getUserName(Scanner scanner){
        String username;
        username = scanner.nextLine();
        while(!isValidUsername(username)){
            System.out.println("Username must begin with letter and can be max 10 characters. Please try again:");
            username = scanner.nextLine();
        }
        return username;
    }

    private static boolean isValidUsername(String name){
        return (Character.isLetter(name.charAt(0))) && (name.length() <= 10);
    }
}
