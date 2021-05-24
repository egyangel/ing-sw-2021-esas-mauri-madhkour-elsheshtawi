package it.polimi.ingsw.network.client;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utility.messages.Message;
import it.polimi.ingsw.view.IView;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.gui.GUI;

import static it.polimi.ingsw.network.server.Server.SERVER_MIN_PORT;
import static it.polimi.ingsw.network.server.Server.SERVER_MAX_PORT;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class Client implements Runnable {
    private String username;

    private ServerHandler serverHandler;
    private IView view;
    public static final int MIN_PORT = SERVER_MIN_PORT;
    public static final int MAX_PORT = SERVER_MAX_PORT;

    private PersonalBoard personalBoard;
    private String marketTrayDescription;
    private String devCardMatrixDescription;
    private List<PersonalBoard> otherPersonalBoardImages;
    private Map<Integer, String> userIDtoUserNames;
    private Map<Integer, String> userIDtoOtherUserNames;
    private GUI gui;

    public Map<Integer, String> getUserIDtoUserNames() {
        return userIDtoUserNames;
    }

    public static void main(String[] args) {
        Client client = new Client();
//        checkIfCLI(args[0], client);
        client.setView(new CLI(client));
        client.run();
    }

    private void setView(IView view) {
        this.view = view;
    }

    @Override
    public void run() {
        view.startDisplay();
    }

    public void connectToServer(String ip, int portNumber) {
        Socket socket;
        try {
            socket = new Socket(ip, portNumber);
        } catch (IOException e) {
            System.out.println("Server socket unreachable");
            return;
        }
        serverHandler = new ServerHandler(socket, this);
        Thread thread = new Thread(serverHandler);
        thread.start();
    }

    public void sendToServer(Message msg) {
        serverHandler.sendMessage(msg);
    }

    public void handleSetUpMessage(Message msg) {
        switch (msg.getMsgtype()) {
            case DISPLAY_FIRST_LOGIN:
                serverHandler.setUserId(msg.getUserID());
                view.addNextDisplay("displayFirstLogin");
                break;
            case DISPLAY_LOGIN:
                serverHandler.setUserId(msg.getUserID());
                view.addNextDisplay("displayLogin");
                break;
            case FIRST_LOGIN_ACCEPTED:
                Type t = new TypeToken<Map<Integer, String>>() {
                }.getType();
                userIDtoUserNames = (Map<Integer, String>) msg.getObject(t);
                break;


            case LOGIN_ACCEPTED:
                view.setGeneralMsg("When other players connect the server, the game will start...");
                view.addNextDisplay("displayGeneralMsg");
                break;
            case START_MATCH:    // START_MATCH is the last setUp message from server to client
                Type type = new TypeToken<Map<Integer, String>>() {
                }.getType();
                userIDtoUserNames = (Map<Integer, String>) msg.getObject(type);
                break;
        }
    }

    public IView getView() {
        return view;
    }

    public PersonalBoard getPersonalBoard() {
        return personalBoard;
    }

    public void setPersonalBoard(PersonalBoard personalBoard) {
        this.personalBoard = personalBoard;
    }

    public String getMarketTrayDescription() {
        return marketTrayDescription;
    }

    public void setMarketTrayDescription(String marketTrayDescription) {
        this.marketTrayDescription = marketTrayDescription;
    }

    public String getDevCardMatrixDescription() {
        return devCardMatrixDescription;
    }

    public void setDevCardMatrixDescription(String devCardMatrixDescription) {
        this.devCardMatrixDescription = devCardMatrixDescription;
    }

    // METHODS THAT WON'T BE USED

    private static void checkIfCLI(String arg, Client client) {
        if (arg.equals("--CLI"))
            client.setView(new CLI(client));
        else if (arg.equals("--GUI"))
            client.setView(new GUI(client));
        else System.exit(0);
    }

    public void setGui(GUI g) {
        gui = g;
    }
}