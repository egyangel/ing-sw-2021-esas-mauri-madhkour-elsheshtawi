package it.polimi.ingsw.view.cli;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.LeaderCard;
import it.polimi.ingsw.model.Resources;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.utility.InputConsumer;
import it.polimi.ingsw.utility.messages.*;
import it.polimi.ingsw.view.IView;

import java.io.PrintWriter;
import java.lang.reflect.Type;
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
    private CVEvent cvEventToDisplay;

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
        displayNameMap.put("displayFourLeaderCard", this::displayFourLeaderCard);
        displayNameMap.put("displayTurnAssign", this::displayTurnAssign);
        displayNameMap.put("displayActionSelection", this::displayActionSelection);

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
        Map<String, String> firstLoginMap = new HashMap<>();
        firstLoginMap.put("numberOfPlayers", numberOfPlayers.toString());
        firstLoginMap.put("username", username);
        Message loginmsg = new Message(Message.MsgType.REQUEST_FIRST_LOGIN, firstLoginMap);
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

    public void displayFourLeaderCard(){
        out.println("Here are the four leader card options...");
        Type type = new TypeToken<List<LeaderCard>>(){}.getType();
        List<LeaderCard> fourLeaderCards = (List<LeaderCard>) cvEventToDisplay.getEventPayload(type);
        for (int i = 0; i < fourLeaderCards.size(); i++){
            out.println(i);
            out.println(fourLeaderCards.get(i));
        }
        out.println("Enter the index of first leader card to keep:");
        Integer firstIndex = InputConsumer.getANumberBetween(in,out, 1, 4);
        out.println("Enter the index of second leader card to keep:");
        Integer secondIndex = InputConsumer.getANumberBetween(in,out, 1, 4);
        while (firstIndex.equals(secondIndex)) {
            out.println("Please enter a different index than first selection:");
            secondIndex = InputConsumer.getANumberBetween(in,out, 1, 4);
        }
        List<LeaderCard> twoLeaderCards = new ArrayList<>();
        twoLeaderCards.add(fourLeaderCards.get(firstIndex));
        twoLeaderCards.add(fourLeaderCards.get(secondIndex));
        VCEvent vcEvent = new VCEvent(VCEvent.eventType.LEADER_CARDS_CHOOSEN, twoLeaderCards);
        Message leadercardmsg = new Message(Message.MsgType.VC_EVENT, vcEvent);
        client.sendToServer(leadercardmsg);
    }

    public void displayTurnAssign(){
        Integer turn = (Integer) cvEventToDisplay.getEventPayload(Integer.class);
        switch (turn){
            case 1:
                out.println("You are the first player.");
                out.println("You have the inkwell but no initial resources or faith points.");
                // displayIdle automatically called
                break;
            case 2:
                out.println("You are the second player.");
                out.println("You will have one initial resource of your choosing in the warehouse.");
                Resources.ResType initResType = InputConsumer.getResourceType(in, out);
                Resources initResource = new Resources(initResType, 1);
                VCEvent vcEvent = new VCEvent(VCEvent.eventType.INIT_RES_CHOOSEN, initResource);
                Message initResMsg = new Message(Message.MsgType.VC_EVENT, vcEvent);
                client.sendToServer(initResMsg);
                break;
            case 3:
                out.println("You are the third player.");
                out.println("You will start with one faith point on your faith track.");
                out.println("You will have one initial resource of your choosing in the warehouse.");
                Resources.ResType initResTypeTwo = InputConsumer.getResourceType(in, out);
                Resources initResourceTwo = new Resources(initResTypeTwo, 1);
                VCEvent vcEventTwo = new VCEvent(VCEvent.eventType.INIT_RES_CHOOSEN, initResourceTwo);
                Message initResMsgTwo = new Message(Message.MsgType.VC_EVENT, vcEventTwo);
                client.sendToServer(initResMsgTwo);
                break;
            case 4:
                out.println("You are the fourth player.");
                out.println("You will start with one faith point on your faith track.");
                out.println("You will have two initial resources of your choosing in the warehouse.");
                Resources.ResType initResTypeThree = InputConsumer.getResourceType(in, out);
                Resources initResourceThree = new Resources(initResTypeThree, 1);
                initResTypeThree = InputConsumer.getResourceType(in, out);
                initResourceThree.add(initResTypeThree, 1);
                VCEvent vcEventThree = new VCEvent(VCEvent.eventType.INIT_RES_CHOOSEN, initResourceThree);
                Message initResMsgThree = new Message(Message.MsgType.VC_EVENT, vcEventThree);
                client.sendToServer(initResMsgThree);
                break;
        }
    }

    //TODO: omer will continue from here
    public void displayActionSelection(){
        out.println("It is your turn now!");
        out.println("Enter the index of the action you want to take:");
        out.println("[1] Take resource from market");
        out.println("[2] Buy one development card");
        out.println("[3] Activate the production");
        out.println("[4] View warehouse and strongbox");
        out.println("[5] View development slots");
        out.println("[6] View faith track");
        out.println("[7] View leader cards");
        out.println("[8] View other players");
        out.println("[9] End turn");
    }

    @Override
    public void update(Event event) {
        if (event instanceof CVEvent){
            cvEventToDisplay = (CVEvent) event;
            switch (cvEventToDisplay.getEventType()) {
                case CHOOSE_TWO_LEADER_CARD:
                    addNextDisplay("displayFourLeaderCard");
                    break;
                case ASSIGN_TURN_ORDER:
                    addNextDisplay("displayTurnAssign");
                    break;
                case BEGIN_TURN:
                    addNextDisplay("displayActionSelection");
                    break;
            }
        } else if (event instanceof MVEvent){

        } else {
            out.println("Unidentified MV or CV event");
        }
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
        out.print("Waiting for the other players... ");
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
