package it.polimi.ingsw.network.server;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.utility.messages.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server implements Runnable{
    public static final int SERVER_MIN_PORT = 3000;
    public static final int SERVER_MAX_PORT = 5000;
    private static final int MAX_NUM_OF_PLAYERS = 4;
    private static int numberOfConnectedUsers = 0;
    private static int numberOfUsers = 0;
    private Map<Integer, String> userIDtoUserNames = new HashMap<>();
    private Map<Integer,ClientHandler> userIDtoHandlers;
    private Controller controller;
    private Game game;
    private Map<Integer,VirtualView> userIDtoVirtualViews = new HashMap<>();

    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }

    @Override
    public void run() {
        game = new Game();
        controller = new Controller(game, this);
        userIDtoHandlers = new HashMap<>();
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

                if (numberOfUsers < MAX_NUM_OF_PLAYERS) {
                    System.out.println("Creating a new handler for this client...");
                    Integer userID = ++numberOfUsers;
                    ClientHandler clientHandler = new ClientHandler(userID, socket, this);
                    System.out.println("Adding to userID - client handler map...");
                    userIDtoHandlers.put(userID, clientHandler);
                    Thread thread = new Thread(clientHandler);
                    thread.start();
                } else {
                    System.out.println("Refusing the new request...");
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject("The game has already started...");
                    try {
                        oos.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleMessage(Integer userID, Message msg){
        if (msg.getMsgtype() == Message.MsgType.VC_EVENT) {
            userIDtoVirtualViews.get(userID).handleGameMessage(msg);
        } else if (Arrays.asList(Message.MsgType.CV_EVENT, Message.MsgType.MV_EVENT).contains(msg.getMsgtype())){
            System.out.println("Unexpected server to server message");
        } else {
            handleSetUpMessage(userID,msg);
        }
    }

    private void handleSetUpMessage(Integer userID, Message incomingmsg) {
        Message respondmsg = null;
        ClientHandler senderHandler = userIDtoHandlers.get(userID);
        List<ClientHandler> otherHandlers = new ArrayList<>(userIDtoHandlers.values());
        otherHandlers.remove(senderHandler);
        switch (incomingmsg.getMsgtype()) {
            case REQUEST_FIRST_LOGIN:
                Type type = new TypeToken<Map<String, String>>(){}.getType();
                Map<String, String> firstLoginMap = (Map<String, String>) incomingmsg.getObject(type);
                numberOfUsers = Integer.parseInt(firstLoginMap.get("numberOfPlayers"));
                userIDtoUserNames.put(userID, firstLoginMap.get("username"));
                numberOfConnectedUsers++;
                respondmsg = new Message(Message.MsgType.FIRST_LOGIN_ACCEPTED);
                senderHandler.sendMessage(respondmsg);
            case REQUEST_LOGIN:
                String username = incomingmsg.getJsonContent();
                userIDtoUserNames.put(userID, username);
                numberOfConnectedUsers++;
                respondmsg = new Message(Message.MsgType.LOGIN_ACCEPTED);
                senderHandler.sendMessage(respondmsg);
                if(numberOfConnectedUsers == numberOfUsers){
                    for(ClientHandler handler: userIDtoHandlers.values()){
                        handler.sendMessage(new Message(Message.MsgType.START_MATCH));
                        controller.createMatch(userIDtoUserNames);
                    }
                }
                break;
        }
    }

    public boolean isFirstPlayerConnected() {
        if(numberOfUsers > 0) return true;
        else return false;
    }
}