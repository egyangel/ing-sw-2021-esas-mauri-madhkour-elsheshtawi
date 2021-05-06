package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.utility.InputConsumer;
import it.polimi.ingsw.utility.messages.Message;
import it.polimi.ingsw.utility.messages.MsgType;
import it.polimi.ingsw.view.IView;

import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class CLI implements IView {
    private final Client client;
    private final PrintWriter out;
    private final Scanner in;

    public CLI(Client client) {
        this.client = client;
        this.out = new PrintWriter(System.out, true);
        this.in = new Scanner(System.in);
    }

    @Override
    public void startDisplay() {
        displayGreet();
        displaySetup();
        displayLogin();
    }

    @Override
    public void displayGreet() {
        out.println("Welcome to Masters of Renaissance!");
    }

    @Override
    public void displaySetup() {
        out.println("Enter IP address of the server:");
        String ip = InputConsumer.getIP(in);
        out.println("Enter port number of the server:");
        int portNumber = InputConsumer.getPortNumber(in);
        client.connectToServer(ip,portNumber);
    }

    @Override
    public void displayLogin() {
        out.println("Choose a username:");
        String username = InputConsumer.getUserName(in);
        Message loginmsg = new Message(null, MsgType.LOGIN, username);
        client.sendToServer(loginmsg);
    }
}
