package it.polimi.ingsw.network.server;

import it.polimi.ingsw.utility.messages.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server implements Runnable{
    public static final int SERVER_MIN_PORT = 3000;
    public static final int SERVER_MAX_PORT = 5000;
    private static final int MAX_NUM_OF_PLAYERS = 4;
    private static int numberOfUsers = 0;

//    Implementation for multiple simultaneous games can be added later
//    Map<String, Map<Integer,String>> matchIDtoMatches = new HashMap<>();
    Map<Integer,String> userIDtoUsernames = new HashMap<>();
    Map<Integer,ClientHandler> userIDtoHandlers = new HashMap<>();
    Map<Integer,VirtualView> userIDtoVirtualViews = new HashMap<>();

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter server port number:");
//        int portNumber = InputConsumer.getPortNumber(scanner);
        int portNumber = 3000; //for debug
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
                Integer userID = ++numberOfUsers;
                ClientHandler clientHandler = new ClientHandler(userID, socket, this);
                System.out.println("Adding to userID - client handler map...");
                userIDtoHandlers.put(userID, clientHandler);

                Thread thread = new Thread(clientHandler);
                thread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleMessage(Integer userID, Message msg){
        if (Arrays.asList(Message.Type.LOGIN).contains(msg.getMsgtype())){
            handleSetUpMessage(userID, msg);
        } else
            handleGameMessage(userID, msg);
    }

    private void handleSetUpMessage(Integer userID, Message msg) {
        Message msgToSend = null;
        switch (msg.getMsgtype()) {
            case LOGIN:
                msgToSend = new Message(userID, Message.Type.LOGIN, "UserID assigned as " + userID);
                break;
        }
        if(msgToSend != null){
            userIDtoHandlers.get(userID).sendMessage(msgToSend);
        }
    }

    private void handleGameMessage(Integer userID, Message msg){
        userIDtoVirtualViews.get(userID).handleGameMessage(msg);
    }
}