package it.polimi.ingsw.network.client;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utility.MsgPrinterToCLI;
import it.polimi.ingsw.utility.messages.Message;
import it.polimi.ingsw.view.IView;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.gui.GUI;

import javax.swing.*;

import static it.polimi.ingsw.network.server.Server.SERVER_MIN_PORT;
import static it.polimi.ingsw.network.server.Server.SERVER_MAX_PORT;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;

public class Client implements Runnable{
    private String username;
    private ServerHandler serverHandler;
    private IView view;
    public static final int MIN_PORT = SERVER_MIN_PORT;
    public static final int MAX_PORT = SERVER_MAX_PORT;

    private PersonalBoard personalBoard;

    public static void main(String[] args){
        Client client = new Client();

        if (args[0].equals("--CLI"))
            client.setView(new CLI(client));
        else if (args[0].equals("--GUI"))
            client.setView(new GUI(client));
        else System.exit(0);

        client.run();
    }

    private void setView(IView view) {
        this.view = view;
    }

    @Override
    public void run() {
        view.startDisplay();
    }

    public void connectToServer(String ip, int portNumber){
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

    public void sendToServer(Message msg){
        serverHandler.sendMessage(msg);
    }

    public void handleGameMessage(Message msg) {
    }

    public void handleSetUpMessage(Message msg){
        switch (msg.getMsgtype()) {
            case LOGIN:
                MsgPrinterToCLI.printMessage(MsgPrinterToCLI.MsgDirection.INCOMINGtoCLIENT, msg);
        }
    }

    public void handleMessage(Message msg) {
    }
}
