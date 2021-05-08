package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.utility.InputConsumer;
import it.polimi.ingsw.utility.messages.Message;
import it.polimi.ingsw.utility.messages.MsgType;
import it.polimi.ingsw.view.IView;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CLI implements IView {
    private final Client client;
    private final PrintWriter out;
    private final Scanner in;
    private Map<String, Runnable> displayMap;
    private boolean shouldTerminateClient;
    private boolean stopInteraction;

    public CLI(Client client) {
        this.client = client;
        this.out = new PrintWriter(System.out, true);
        this.in = new Scanner(System.in);
        this.shouldTerminateClient = false;
    }

    @Override
    public void startDisplay() {
        startViewTransition();
    }

    public void startViewTransition(){
        displayMap = new HashMap<>();
        boolean stop;
        synchronized (this) {
            stop = shouldTerminateClient;
            displayMap.put("current", this::displayGreet);
            displayMap.put("next", null);
        }
        while(!stop){
            if (displayMap.get("current") == null) {
                displayMap.replace("current", this::displayIdle);
            }
            displayMap.get("current").run();
            synchronized (this) {
                stop = shouldTerminateClient;
                displayMap.replace("current", displayMap.get("next"));
                displayMap.replace("next", null);
            }
        }
    }

    @Override
    public synchronized void displayIdle(){
        String idleSymbols = "✞⨎⌬☺⌺";
        String backSpace = "\b";
        StringBuilder idleSymbolBar = new StringBuilder();
        int symbolIndex = 0;
        boolean appendtoRight = true;
        int lastBarSize;
        out.print("Please wait... ");

        while(!shouldStopInteraction()){
            out.print(idleSymbolBar);
            out.flush();
            lastBarSize =  idleSymbolBar.length();

            try {
                this.wait(400);
            } catch (InterruptedException e) {}
            if (appendtoRight) {
                idleSymbolBar.append(idleSymbols.charAt(symbolIndex));
                if (idleSymbolBar.length() == 6) {
                    appendtoRight = false;
                    symbolIndex = (symbolIndex + 1) % idleSymbols.length();
                }
            } else {
                if (idleSymbolBar.length() > 0)
                    idleSymbolBar.deleteCharAt(idleSymbolBar.length() - 1);
                else {
                    appendtoRight = true;
                    idleSymbolBar.append(idleSymbols.charAt(symbolIndex));
                }
            }
            for (int i = 0; i< lastBarSize; i++)
                out.print(backSpace);
        }


    }

    @Override
    public synchronized boolean shouldStopInteraction(){
        return stopInteraction;
    }

    @Override
    public synchronized void stopInteraction() {
        stopInteraction = true;
        notifyAll();
    }

    @Override
    public void displayGreet() {
        out.println("Welcome to Masters of Renaissance!");
    }

    @Override
    public void displaySetup() {
//        out.println("Enter IP address of the server:");
//        String ip = InputConsumer.getIP(in);
//        out.println("Enter port number of the server:");
//        int portNumber = InputConsumer.getPortNumber(in);
        String ip = "localhost";
        int portNumber = 3000; //for debug
        client.connectToServer(ip,portNumber);
    }

    @Override
    public void displayLogin() {
        out.println("Choose a username:");
        String username = InputConsumer.getUserName(in);
        Message loginmsg = new Message(0, MsgType.LOGIN, username);
        client.sendToServer(loginmsg);
    }
}
