package it.polimi.ingsw.network.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.utility.JsonConverter;
import it.polimi.ingsw.utility.messages.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server implements Runnable{
    public static final int SERVER_MIN_PORT = 3000;
    public static final int SERVER_MAX_PORT = 5000;
    private static final int MAX_NUM_OF_PLAYERS = 4;
    private static int numberOfUsers = 0;
    private Map<Integer, String> userIDtoWaitingUserNames = new HashMap<>();
    private Map<Integer,ClientHandler> userIDtoHandlers;
    private Controller controller;
    private Game game;

//    Implementation for multiple simultaneous games can be added later
//    Map<String, Map<Integer,String>> matchIDtoMatches = new HashMap<>();
    private Map<Integer,VirtualView> userIDtoVirtualViews = new HashMap<>();


    public static void main(String[] args) {
        Server server = new Server();
        server.run();
    }

    @Override
    public void run() {
        // one game and one controller per match
        game = new Game();
        controller = new Controller(game);
        game.setController(controller);
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
        ClientHandler handler = userIDtoHandlers.get(userID);
        List<ClientHandler> otherHandlers = new ArrayList<ClientHandler>();
        for(Integer otherUserID: userIDtoWaitingUserNames.keySet()){
            otherHandlers.add(userIDtoHandlers.get(otherUserID));
        }
        switch (incomingmsg.getMsgtype()) {
            case REQUEST_LOGIN:
                String username = incomingmsg.getJsonContent();
//                VirtualView virtualView = new VirtualView(userID);
//                virtualView.subscribe(controller);
                userIDtoWaitingUserNames.put(userID, username);
//                controller.addPlayer(userID, username, virtualView);
                respondmsg = new Message(Message.MsgType.LOGIN_ACCEPTED);
                handler.sendMessage(respondmsg);
                respondmsg = new Message(Message.MsgType.DISPLAY_LOBBY, JsonConverter.toJson(userIDtoWaitingUserNames));
                handler.sendMessage(respondmsg);
                for(ClientHandler otherHandler: otherHandlers){
                    respondmsg = new Message(Message.MsgType.USER_JOINED_IN_LOBBY, JsonConverter.toJson(userIDtoWaitingUserNames));
                    otherHandler.sendMessage(respondmsg);
                }
                break;
        }
    }
}