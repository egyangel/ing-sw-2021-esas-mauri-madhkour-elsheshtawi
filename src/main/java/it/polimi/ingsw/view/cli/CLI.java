package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.utility.InputConsumer;
import it.polimi.ingsw.utility.messages.*;
import it.polimi.ingsw.view.IView;

import java.io.PrintWriter;
import java.util.*;

public class CLI implements IView, Publisher<VCEvent>, Listener<Event> {
    private final Client client;
    private final PrintWriter out;
    private final Scanner in;
//    private Map<String, Runnable> displayTransitionMap = new HashMap<>();
    private Map<String, Runnable> displayNameMap = new HashMap<>();
    private Queue<Runnable> displayTransitionQueue = new ArrayDeque<>();
    private boolean shouldTerminateClient;
    private boolean stopIdle;
    private String generalmsg;

    public CLI(Client client) {
        this.client = client;
        this.out = new PrintWriter(System.out, true);
        this.in = new Scanner(System.in);
        this.shouldTerminateClient = false;
    }

    @Override
    public void startDisplay() {
        displayNameMap.put("displayGreet", this::displayGreet);
        displayNameMap.put("displaySetup", this::displaySetup);
        displayNameMap.put("displayIdle", this::displayIdle);
        displayNameMap.put("displayFirstLogin", this::displayFirstLogin);
        displayNameMap.put("displayLogin", this::displayLogin);
        displayNameMap.put("displayGeneralMsg", this::displayGeneralMsg);
        addNextDisplay("displayGreet");
        addNextDisplay("displaySetup");
        startDisplayTransition();
    }

    private void startDisplayTransition() {
        boolean stop;
        synchronized (this) {
            stop = shouldTerminateClient;
        }
        while(!stop){
            if (displayTransitionQueue.peek() == null){
                displayNameMap.get("displayIdle").run();
            } else {
                displayTransitionQueue.poll().run();
            }
            synchronized (this) {
                stop = shouldTerminateClient;
            }
        }
    }

    public synchronized void addNextDisplay(String displayName){
        if (displayTransitionQueue.peek() == null)
            stopDisplayIdle();
        displayTransitionQueue.add(displayNameMap.get(displayName));
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
        out.println("Connecting to server...");
        client.connectToServer(ip,portNumber);
    }

    @Override
    public void displayFirstLogin() {
        out.println("Choose a username:");
        String username = InputConsumer.getUserName(in, out);
        out.println("Choose number of players you would like to play with:");
        Integer numberOfPlayers = InputConsumer.getNumberOfPlayers(in, out);

        Message loginmsg = new Message(Message.MsgType.REQUEST_FIRST_LOGIN, username);
        client.sendToServer(loginmsg);
    }

    @Override
    public void displayLogin() {
        out.println("Choose a username:");
        String username = InputConsumer.getUserName(in, out);
        out.println("Choose number of players you would like to play with:");
        Message loginmsg = new Message(Message.MsgType.REQUEST_LOGIN, username);
        client.sendToServer(loginmsg);
    }

    @Override
    public void update(Event event) {

    }

    @Override
    public void subscribe(Listener<VCEvent> listener) {

    }

    @Override
    public void unsubscribe(Listener<VCEvent> listener) {

    }

    @Override
    public void publish(VCEvent event) {
        client.sendToServer(new Message(Message.MsgType.VC_EVENT));
    }

    @Override
    public synchronized void displayIdle(){
        try {
            this.wait(1000);
        } catch (InterruptedException e) {}
        String idleSymbols = "✞⨎⌬☺⌺";
        String backSpace = "\b";
        StringBuilder idleSymbolBar = new StringBuilder();
        int symbolIndex = 0;
        boolean appendtoRight = true;
        int lastBarSize = 0;
        out.print("Please wait... ");
        out.flush();

        while(!shouldStopDisplayIdle()){
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
            for (int i = 0; i< lastBarSize; i++) {
                out.print(backSpace);
            }
        }

        stopIdle = false;
        for (int i = 0; i< lastBarSize+15; i++)
            out.print(backSpace);
        out.flush();
    }

    @Override
    public synchronized boolean shouldStopDisplayIdle(){
        return stopIdle;
    }

    @Override
    public synchronized void stopDisplayIdle() {
        stopIdle = true;
        notifyAll();
    }

    @Override
    public synchronized void displayGeneralMsg(){
        out.println(generalmsg);
    }

    @Override
    public void setGeneralMsg(String msg) {
        generalmsg = msg;
    }

    // METHODS THAT WON'T BE USED
    @Override
    public synchronized void displayLobby(){
        out.println("Waiting users in the lobby are:");
        for(String username: client.getUserIDtoOtherUserNames().values())
            out.println(username);
    }

}
