package it.polimi.ingsw.view.cli;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.enumclasses.MarbleColor;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.utility.InputConsumer;
import it.polimi.ingsw.utility.messages.*;
import it.polimi.ingsw.view.IView;
import static it.polimi.ingsw.utility.messages.TakeResActionContext.ActionStep.*;
import static it.polimi.ingsw.utility.messages.BuyDevCardActionContext.ActionStep.*;
import static it.polimi.ingsw.utility.messages.ActivateProdActionContext.ActionStep.*;
import static it.polimi.ingsw.utility.messages.CVEvent.EventType.*;
import static it.polimi.ingsw.utility.messages.VCEvent.EventType.*;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.*;

public class CLI implements IView, Publisher<VCEvent>, Listener<Event> {
    private final Client client;
    private final PrintWriter out;
    private final Scanner in;
    private Map<String, Runnable> displayNameMap = new HashMap<>();
    private Queue<Runnable> displayTransitionQueue = new ArrayDeque<>();
    private boolean shouldTerminateClient;
    private boolean stopIdle;
    private String generalmsg;
    private TakeResActionContext takeResContext;
    private BuyDevCardActionContext buyDevCardContext;
    private ActivateProdActionContext activateProdContext;
    private CVEvent initialCVevent;
    private List<Listener<VCEvent>> listenerList = new ArrayList<>();

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
        displayNameMap.put("displayActionSelection", this::displayAllActionSelection);
        //TODO add used methods at the end
//        displayNameMap.put("displayMarketTray", this::displayMarketTray);
//        displayNameMap.put("displayBuyDevCardAction", this::displayBuyDevCardAction);
//        displayNameMap.put("displayActivateProdAction", this::displayActivateProdAction);
//        displayNameMap.put("displayWarehouseAndStrongbox", this::displayWarehouseAndStrongbox);
//        displayNameMap.put("displayDevSlots", this::displayDevSlots);
//        displayNameMap.put("displayFaithTrack", this::displayFaithTrack);
//        displayNameMap.put("displayLeaderCards", this::displayLeaderCards);
//        displayNameMap.put("displayOtherPlayers", this::displayOtherPlayers);
//        displayNameMap.put("displayEndTurn", this::displayEndTurn);

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
        List<LeaderCard> fourLeaderCards = (List<LeaderCard>) initialCVevent.getEventPayload(type);
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
        VCEvent vcEvent = new VCEvent(LEADER_CARDS_CHOOSEN, twoLeaderCards);
        publish(vcEvent);
    }

    public void displayTurnAssign(){
        Integer turn = (Integer) initialCVevent.getEventPayload(Integer.class);
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
                VCEvent vcEvent = new VCEvent(INIT_RES_CHOOSEN, initResource);
                publish(vcEvent);
                break;
            case 3:
                out.println("You are the third player.");
                out.println("You will start with one faith point on your faith track.");
                out.println("You will have one initial resource of your choosing in the warehouse.");
                Resources.ResType initResTypeTwo = InputConsumer.getResourceType(in, out);
                Resources initResourceTwo = new Resources(initResTypeTwo, 1);
                VCEvent vcEventTwo = new VCEvent(INIT_RES_CHOOSEN, initResourceTwo);
                publish(vcEventTwo);
                break;
            case 4:
                out.println("You are the fourth player.");
                out.println("You will start with one faith point on your faith track.");
                out.println("You will have two initial resources of your choosing in the warehouse.");
                Resources.ResType initResTypeThree = InputConsumer.getResourceType(in, out);
                Resources initResourceThree = new Resources(initResTypeThree, 1);
                initResTypeThree = InputConsumer.getResourceType(in, out);
                initResourceThree.add(initResTypeThree, 1);
                VCEvent vcEventThree = new VCEvent(INIT_RES_CHOOSEN, initResourceThree);
                publish(vcEventThree);
                break;
        }
    }

    public void displayAllActionSelection(){
        out.println("It is your turn now!");
        out.println("Enter the index of the action you want to take:");
        out.println("[1] Take resource from market");
        out.println("[2] Buy one development card");
        out.println("[3] Activate the production");
        out.println("[4] View market tray");
        out.println("[5] View development cards available");
        out.println("[5] View and modify warehouse");
        out.println("[6] View strongbox");
        out.println("[6] View development slots");
        out.println("[7] View faith track");
        out.println("[8] View leader cards");
        out.println("[9] View other players");
        out.println("[0] End turn");
        int index = InputConsumer.getANumberBetween(in, out, 0, 9);
        switch (index){
            case 1:
                addNextDisplay("displayTakeResAction");
                break;
            case 2:
                addNextDisplay("displayBuyDevCardAction");
                break;
            case 3:
                addNextDisplay("displayActivateProdAction");
                break;
            case 4:
                addNextDisplay("displayMarketTray");
                break;
            case 5:
                addNextDisplay("displayWarehouseAndStrongbox");
                break;
            case 6:
                addNextDisplay("displayDevSlots");
                break;
            case 7:
                addNextDisplay("displayFaithTrack");
                break;
            case 8:
                addNextDisplay("displayLeaderCards");
                break;
            case 9:
                addNextDisplay("displayOtherPlayers");
                break;
            case 0:
                addNextDisplay("displayEndTurn");
                break;
        }
    }

    public void displayMinorActionSelection(){
        out.println("Do you want to execute any other action?");
        out.println("Enter the index of the action you want to take:");
        out.println("[1] View market tray");
        out.println("[2] View and modify warehouse");
        out.println("[3] View strongbox");
        out.println("[4] View development slots");
        out.println("[5] View faith track");
        out.println("[6] View leader cards");
        out.println("[7] View other players");
        out.println("[8] End Turn");
        int index = InputConsumer.getANumberBetween(in, out, 1, 8);
        switch (index){
            case 1:
                addNextDisplay("displayMarketTray");
                break;
            case 2:
                addNextDisplay("displayAskModifyWarehouse");
                break;
            case 3:
                addNextDisplay("displayStrongbox");
                break;
            case 4:
                addNextDisplay("displayDevSlots");
                break;
            case 5:
                addNextDisplay("displayFaithTrack");
                break;
            case 6:
                addNextDisplay("displayLeaderCards");
                break;
            case 7:
                addNextDisplay("displayOtherPlayers");
                break;
            case 8:
                addNextDisplay("displayEndTurn");
                break;
        }
    }

    //TODO after implementing all actions, don't forget to send updated personal board (for example take resource end result created
    // inside displayPutResourceTaken, or discarded resources for other's faith points, or if vatican report triggered)
    public void displayEndTurn(){}

    public void chooseRowColumnNumber(){
        String rowColumnNumber = InputConsumer.getMarketRowColumnIndex(in, out);
        char firstLetter = rowColumnNumber.charAt(0);
        int index = Integer.parseInt(String.valueOf(rowColumnNumber.charAt(2)));
        if (firstLetter == 'R') {
            takeResContext.chooseRow(true);
        } else if (firstLetter == 'C') {
            takeResContext.chooseRow(false);
        } else takeResContext.setErrorTrue();
        takeResContext.setIndex(index);
        takeResContext.setLastStep(ROW_COLUMN_CHOOSEN);
        VCEvent vcEvent = new VCEvent(TAKE_RES_CONTEXT_FILLED, takeResContext);
        publish(vcEvent);
    }

    public void chooseColorLevel(){
        String colorAndLevel = InputConsumer.getColorAndLevel(in, out);
        String[] parts = colorAndLevel.split("-");
        buyDevCardContext.setColor(DevCard.CardColor.valueOf(parts[0]));
        buyDevCardContext.setLevel(Integer.parseInt(parts[1]));
        VCEvent vcEvent = new VCEvent(BUY_DEVCARD_CONTEXT_FILLED, buyDevCardContext);
        publish(vcEvent);
    }

    public void displayMarketTray(){
        out.println(client.getMarketTrayDescription());
    }

    public void displayDevCardMatrix(){

    }

    public void displayDevSlots(){
        client.getPersonalBoard().printDevSlots();
    }

    public void displayFaithTrack(){
        out.println(client.getPersonalBoard().describeFaithTrack());
    }

    public void displayLeaderCards(){
        client.getPersonalBoard().printLeaderCards();
    }

    public void displayOtherPlayers(){

    }

    public void chooseWhiteConverters(){
        Resources.ResType firstResOption = takeResContext.getWhiteConverters().get(0).getAbility().getResType();
        Resources.ResType secondResOption = takeResContext.getWhiteConverters().get(1).getAbility().getResType();
        int whiteMarbles = takeResContext.getWhiteMarbleNumber();
        out.println("You have two active white marble converter leader cards, and received " + whiteMarbles + " white marble from market tray");
        out.println("You can convert the white marbles into [1]" + firstResOption.toString()  + " or [2]" + secondResOption.toString());
        while(whiteMarbles > 0){
            out.println("Enter the index of resource type into which you want to convert one white marble");
            int index = InputConsumer.getANumberBetween(in, out, 1, 2);
            if (index == 1) {
                takeResContext.addOneConvertedRes(firstResOption);
            } else
                takeResContext.addOneConvertedRes(secondResOption);
            whiteMarbles--;
            out.println("You now have " + whiteMarbles + " white marble to convert.");
        }
        takeResContext.setLastStep(RES_FROM_WHITE_ADDED_TO_CONTEXT);
        VCEvent vcEvent = new VCEvent(TAKE_RES_CONTEXT_FILLED, takeResContext);
        publish(vcEvent);
    }

    public void chooseShelvesToPut(){
        out.println("Your warehouse looks like:");
        displayWarehouse();
        Resources resources = takeResContext.getResources();
        out.println("You have " + resources.toString() + " that you can put to your warehouse.");
        out.println("Extra resources that you don't put will be discarded automatically");
        out.println("Select one of the options below:");
        out.println("[1] Clear shelf");
        out.println("[2] Swap shelves");
        out.println("[3] Select resource type and shelf to put that kind of resources");
        out.println("[4] End take resource action");
        int index = InputConsumer.getANumberBetween(in, out, 1, 3);
        if (index == 1){
            out.println("Select a shelf that you want to remove all resources from:");
            Shelf.shelfPlace place = InputConsumer.getShelfPlace(in, out);
            takeResContext.setShelf(place);
            takeResContext.setLastStep(CLEAR_SHELF_CHOOSEN);
        } else if (index == 2){
            out.println("Select two shelves that you want to swap, extra resources will be discarded automatically:");
            Shelf.shelfPlace firstPlace = InputConsumer.getShelfPlace(in, out);
            Shelf.shelfPlace secondPlace = InputConsumer.getShelfPlace(in, out);
            if(firstPlace.equals(secondPlace)){
                out.println("You cannot select the same shelf.");
                addNextDisplay("chooseShelvesToPut");
            }
            takeResContext.setShelves(firstPlace, secondPlace);
            takeResContext.setLastStep(SWAP_SHELVES_CHOOSEN);
        } else if (index == 3){
            List<Resources.ResType> resTypeList = new ArrayList<>();
            resTypeList.addAll(resources.getResTypes());
            Map<Shelf.shelfPlace, Resources.ResType> shelfToResMap = new HashMap<>();
            for(Shelf.shelfPlace place: Shelf.shelfPlace.values()){
                out.println("Which type of resource you want to put into " + place.toString() + " shelf?");
                Resources.ResType selectedType = InputConsumer.getATypeAmongSet(in, out, resTypeList);
                resTypeList.remove(selectedType);
                shelfToResMap.put(place, selectedType);
            }
            takeResContext.setShelftoResTypeMap(shelfToResMap);
            takeResContext.setLastStep(PUT_RESOURCES_CHOOSEN);
        } else {
            out.println("Ending take resource action...");
            VCEvent vcEvent = new VCEvent(TAKE_RES_ACTION_ENDED);
            publish(vcEvent);
            return;
        }
        VCEvent vcEvent = new VCEvent(TAKE_RES_CONTEXT_FILLED, takeResContext);
        publish(vcEvent);
    }

    public void displayStrongbox(){
        out.println(client.getPersonalBoard().describeStrongbox());
    }

    public void displayWarehouse(){
        out.println(client.getPersonalBoard().describeWarehouse());
    }

    public void displayPlaceDevCardSelected(){
        //TODO OMER continue refactor buy dev card action
//        DevCard selectedCard = (DevCard) cvEventToDisplay.getEventPayload(DevCard.class);
//        List<DevSlot.slotPlace> placeList = client.getPersonalBoard().getSuitablePlaces(selectedCard);
//        DevSlot.slotPlace selectedPlace = InputConsumer.getSlotPlace(in, out, placeList);
        displayWarehouse();
        displayStrongbox();
        Resources remainingCost = new Resources();
//        remainingCost.add(selectedCard.getCost());

         out.println("The cost of the card is: " + remainingCost.toString());
        for(Resources.ResType resType: remainingCost.getResTypes()){
            while(remainingCost.getNumberOfType(resType) > 0){
                out.println("Where do you want to spend 1 " + resType.toString() + " from?");
                boolean warehouse = InputConsumer.getWorS(in, out);
                if (warehouse) {
                    client.getPersonalBoard().spendOneFromWarehouse(resType);
                } else {
                    client.getPersonalBoard().spendOneFromStrongbox(resType);
                }
                remainingCost.subtract(resType,1);
            }
        }
//        client.getPersonalBoard().putDevCardOnSlot(selectedCard, selectedPlace);
        out.println("The card is successfully bought and placed where you selected.");
        // TODO send message to server at the end turn stage, update it about personal board change in client side
        addNextDisplay("displayMinorActions");

        //TODO Amor wrote this code,but i have to talk with omer
   /*     VCEvent vcEvent = new VCEvent(TAKE_RES_ACTION_ENDED);
        publish(vcEvent);
        return;*/
    }

    private void routeTakeResActionDisplay(){
        switch (takeResContext.getLastStep()){
            case CHOOSE_ROW_COLUMN:
                addNextDisplay("chooseRowColumnNumber");
                break;
            case CHOOSE_LEADER_TO_CONVERT_WHITE:
                addNextDisplay("chooseWhiteConverters");
                break;
            case CHOOSE_SHELVES:
                addNextDisplay("chooseShelvesToPut");
                break;
        }
    }

    private void routeBuyDevCardActionDisplay(){
        switch (buyDevCardContext.getLastStep()){
            case CHOOSE_COLOR_LEVEL:
                addNextDisplay("chooseColorLevel");
                break;
        }
    }

    private void routeActivateProdActionDisplay(){
        switch (activateProdContext.getLastStep()){
            case CHOOSE_DEV_SLOTS:
                addNextDisplay("chooseDevSlots");
        }
    }

    private void routeInitialActionsDisplay(){
        switch (initialCVevent.getEventType()) {
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
    }

    @Override
    public void update(Event event) {
        if (event instanceof CVEvent){
            CVEvent cvEvent = (CVEvent) event;
            CVEvent.EventType eventType = cvEvent.getEventType();
            if (eventType.equals(TAKE_RES_FILL_CONTEXT)){
                takeResContext = (TakeResActionContext) cvEvent.getEventPayload(TakeResActionContext.class);
                routeTakeResActionDisplay();
            } else if (eventType.equals(BUY_DEVCARD_FILL_CONTEXT)){
                buyDevCardContext = (BuyDevCardActionContext) cvEvent.getEventPayload(BuyDevCardActionContext.class);
                routeBuyDevCardActionDisplay();
            } else if (eventType.equals(ACTIVATE_PROD_FILL_CONTEXT)){
                activateProdContext = (ActivateProdActionContext) cvEvent.getEventPayload(ActivateProdActionContext.class);
                routeActivateProdActionDisplay();
            } else {
                initialCVevent = cvEvent;
                routeInitialActionsDisplay();
            }
        } else if (event instanceof MVEvent){
            MVEvent mvEvent = (MVEvent) event;
            switch (mvEvent.getEventType()) {
                case SWAPPED_SHELVES:
                    client.setPersonalBoard((PersonalBoard) mvEvent.getEventPayload(PersonalBoard.class));
                    out.println("Shelf swap successful!");
                    addNextDisplay("displayAskModifyWarehouse");
                    break;
                case DISCARDED_FROM_SHELF:
                    client.setPersonalBoard((PersonalBoard) mvEvent.getEventPayload(PersonalBoard.class));
                    out.println("Discard from shelf successful!");
                    addNextDisplay("displayAskModifyWarehouse");
                    break;
                case MOST_RECENT_MARKETTRAY_SENT:
                    client.setMarketTrayDescription((String) mvEvent.getEventPayload(String.class));
                    break;
                case MOST_RECENT_DEVCARDMATRIX_SENT:
                    client.setDevCardMatrixDescription((String) mvEvent.getEventPayload(String.class));
                    break;
                case OTHER_PERSONALBOARDS_SENT:
                    break;
            }
        } else {
            out.println("Unidentified MV or CV event");
        }
    }

    @Override
    public void subscribe(Listener<VCEvent> listener) {
        listenerList.add(listener);
    }

    @Override
    public void unsubscribe(Listener<VCEvent> listener) {
        listenerList.remove(listener);
    }

    @Override
    public void publish(VCEvent event) {
        for(Listener<VCEvent> listener: listenerList)
            listener.update(event);
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
        for(String username: client.getUserIDtoUserNames().values())
            out.println(username);
    }

}
