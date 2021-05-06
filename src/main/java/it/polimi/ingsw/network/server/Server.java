package it.polimi.ingsw.network.server;

import it.polimi.ingsw.utility.InputConsumer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server implements Runnable{
    public static final int SERVER_MIN_PORT = 3000;
    public static final int SERVER_MAX_PORT = 5000;
    private static final int MAX_NUM_OF_PLAYERS = 4;
    private static int numberOfUsers = 0;

    Map<String, Map<Integer,String>> matchIDtoMatches = new HashMap<>();
    Map<Integer,String> userIDtoUsernames = new HashMap<>();
    Map<Integer,ClientHandler> userIDtoHandlers = new HashMap<>();

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter server port number:");
        int portNumber = InputConsumer.getPortNumber(scanner);
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            System.out.println("Can't open server socket");
            System.exit(1);
            return;
        }

        Socket socket;
        while (true) {
            try {
                socket = serverSocket.accept();
                System.out.println("New client request received : " + socket);

                System.out.println("Creating a new handler for this client...");
                Integer userID = numberOfUsers++;
                ClientHandler clientHandler = new ClientHandler(userID, socket);
                System.out.println("Adding to userID - client handler map...");
                userIDtoHandlers.put(userID, clientHandler);

                Thread thread = new Thread(clientHandler);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}