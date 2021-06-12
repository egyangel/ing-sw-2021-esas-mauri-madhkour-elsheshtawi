package it.polimi.ingsw.view.cli;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.utility.InputConsumer;
import it.polimi.ingsw.utility.messages.*;
import it.polimi.ingsw.view.IView;
import static it.polimi.ingsw.utility.messages.LeaderActionContext.ActionStep.*;
import static it.polimi.ingsw.utility.messages.ActivateProdActionContext.ActionStep.*;
import static it.polimi.ingsw.utility.messages.TakeResActionContext.ActionStep.*;
import static it.polimi.ingsw.utility.messages.BuyDevCardActionContext.ActionStep.*;
import static it.polimi.ingsw.utility.messages.CVEvent.EventType.*;
import static it.polimi.ingsw.utility.messages.VCEvent.EventType.*;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.*;
//todo Omer we have to talk about how to implement leader Action, i think it should be an automatic call
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
    private LeaderActionContext activateLeaderContext;
    private CVEvent initialCVevent;
    private List<Listener<VCEvent>> listenerList = new ArrayList<>();
    private String marketTrayDescription;
    private String devCardMatrixDescription;
    private Map<Integer, PersonalBoardDescription> userIDtoBoardDescriptions;
    private Map<Integer, String> userIDtoUsernames;
    private boolean majorActionDone;

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
        while (!stop) {
            if (displayTransitionQueue.peek() == null) {
                displayNameMap.get("displayIdle").run();
            } else {
                displayTransitionQueue.poll().run();
            }
            synchronized (this) {
                stop = shouldTerminateClient;
            }
        }
    }

    public synchronized void addNextDisplay(String displayName) {
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
        client.connectToServer(ip, portNumber);
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
        //TODO OMer here we may ask gor the number so that we know a priori the number of player and then change for an arbitrary number
        out.println("Choose number of players you would like to play with:");
        Message loginmsg = new Message(Message.MsgType.REQUEST_LOGIN, username);
        client.sendToServer(loginmsg);
    }
    private void routeInitialActionsDisplay() {
        switch (initialCVevent.getEventType()) {
            case CHOOSE_TWO_LEADER_CARD:
                addNextDisplay("displayFourLeaderCard");
                break;
            case ASSIGN_TURN_ORDER:
                addNextDisplay("displayTurnAssign");
                break;
            case SELECT_ALL_ACTION:
                majorActionDone = false;
                addNextDisplay("displayAllActionSelection");
                break;
        }
    }

    public void displayFourLeaderCard() {
        out.println("Here are the four leader card options...");
        Type type = new TypeToken<List<LeaderCard>>() {
        }.getType();
        List<LeaderCard> fourLeaderCards = (List<LeaderCard>) initialCVevent.getEventPayload(type);
        for (int i = 0; i < fourLeaderCards.size(); i++) {
            out.println(i);
            out.println(fourLeaderCards.get(i));
        }
        out.println("Enter the index of first leader card to keep:");
        Integer firstIndex = InputConsumer.getANumberBetween(in, out, 1, 4);
        out.println("Enter the index of second leader card to keep:");
        Integer secondIndex = InputConsumer.getANumberBetween(in, out, 1, 4);
        while (firstIndex.equals(secondIndex)) {
            out.println("Please enter a different index than first selection:");
            secondIndex = InputConsumer.getANumberBetween(in, out, 1, 4);
        }
        List<LeaderCard> twoLeaderCards = new ArrayList<>();
        twoLeaderCards.add(fourLeaderCards.get(firstIndex));
        twoLeaderCards.add(fourLeaderCards.get(secondIndex));
        VCEvent vcEvent = new VCEvent(LEADER_CARDS_CHOOSEN, twoLeaderCards);
        publish(vcEvent);
    }

    public void displayTurnAssign() {
        Integer turn = (Integer) initialCVevent.getEventPayload(Integer.class);
        switch (turn) {
            case 0: // SOLO PLAYER
                Resources.ResType initResTypeSolo = InputConsumer.getResourceType(in, out);
                Resources initResourceSolo = new Resources(initResTypeSolo, 1);
                VCEvent vcEventSolo = new VCEvent(INIT_RES_CHOOSEN, initResourceSolo);
                publish(vcEventSolo);
                break;
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

    public void displayAllActionSelection() {
        VCEvent vcEvent;
        out.println("It is your turn now!");
        out.println("Enter the index of the action you want to take:");
        out.println("[1] Take resource from market");
        out.println("[2] Buy one development card");
        out.println("[3] Activate the production");
        out.println("[4] View market tray");
        out.println("[5] View market tray");
        out.println("[6] View development card matrix");
        out.println("[7] View warehouse");
        out.println("[8] View strongbox");
        out.println("[9] View development slots");
        out.println("[10] View faith track");
        out.println("[11] View leader cards");
        // TODO maybe this option can be used for the users personal board too, so above display methods are not shown
        out.println("[12] View all personal boards");
        out.println("[13] End turn");
        int index = InputConsumer.getANumberBetween(in, out, 1, 11);
        switch (index) {
            case 1:
                vcEvent = new VCEvent(TAKE_RES_ACTION_SELECTED);
                publish(vcEvent);
                break;
            case 2:
                vcEvent = new VCEvent(BUY_DEVCARD_ACTION_SELECTED);
                publish(vcEvent);
                break;
            case 3:
                vcEvent = new VCEvent(ACTIVATE_PROD_ACTION_SELECTED);
                publish(vcEvent);
                break;
            case 4:
                vcEvent = new VCEvent(ACTIVATE_LEADER_CONTEXT_SELECTED);
                publish(vcEvent);
                break;
            case 5:
                addNextDisplay("displayMarketTray");
                break;
            case 6:
                addNextDisplay("displayDevCardMatrix");
                break;
            case 7:
                addNextDisplay("displayWarehouse");
                break;
            case 8:
                addNextDisplay("displayStrongbox");
                break;
            case 9:
                addNextDisplay("displayDevSlots");
                break;
            case 10:
                addNextDisplay("displayFaithTrack");
                break;
            case 11:
                addNextDisplay("displayLeaderCards");
                break;
            case 12:
                addNextDisplay("displayAllPersonalBoards");
        }
    }

    public void displayMinorActionSelection() {
        out.println("Do you want to execute any other action?");
        out.println("Enter the index of the action you want to take:");
        out.println("[1] View market tray");
        out.println("[2] View and modify warehouse");
        out.println("[3] View strongbox");
        out.println("[4] View development slots");
        out.println("[5] View faith track");
        out.println("[6] View leader cards");
        out.println("[7] View other players");
        //TODO omer why this action
        out.println("[8]ACTIVATE_PROD_ACTION_ENDED Turn");
        int index = InputConsumer.getANumberBetween(in, out, 1, 8);
        switch (index) {
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

    public void displayMarketTray(){
        out.println(marketTrayDescription);
        returnToCorrectActionSelection();
    }

    public void displayDevCardMatrix(){
        out.println(devCardMatrixDescription);
        returnToCorrectActionSelection();
    }

    public void displayWarehouse(){
        out.println(userIDtoBoardDescriptions.get(client.getUserID()).getWarehouseDescription());
        returnToCorrectActionSelection();
    }

    public void displayStrongbox(){
        out.println(userIDtoBoardDescriptions.get(client.getUserID()).getStrongboxDescription());
        returnToCorrectActionSelection();
    }

    public void displayDevSlots(){
        out.println(userIDtoBoardDescriptions.get(client.getUserID()).getDevSlotsDescription());
        returnToCorrectActionSelection();
    }

    public void displayFaithTrack(){
        out.println(userIDtoBoardDescriptions.get(client.getUserID()).getFaithTrackDescription());
        returnToCorrectActionSelection();
    }
    public void displayLeaderCards(){
        out.println(userIDtoBoardDescriptions.get(client.getUserID()).getLeaderCardsDescription());
        returnToCorrectActionSelection();
    }
    public void displayBuyDevActionEnd(){
        out.println("Ending buy development card action...");
        VCEvent vcEvent = new VCEvent(BUY_DEVCARD_ACTION_ENDED);
        publish(vcEvent);
    }
    public void displayActivationProdActionEnd(){
        out.println("Ending activation production phase...");

        VCEvent vcEvent = new VCEvent(ACTIVATE_PROD_ACTION_ENDED);
        publish(vcEvent);
    }

    public void displayAllPersonalBoards(){
        LinkedList<Integer> userIDs = new LinkedList<>();
        userIDs.addAll(userIDtoBoardDescriptions.keySet());
        userIDs.remove(client.getUserID());
        userIDs.addFirst(client.getUserID());
        int index = 0;
        int numberOfPlayers = userIDtoBoardDescriptions.keySet().size();
        out.println("Your personal board:");
        out.println(userIDtoBoardDescriptions.get(userIDs.get(index)));
        out.println("Enter [1] for next board, [2] for previous board, [3] to choose action:");
        int input = InputConsumer.getANumberBetween(in, out, 1,3);
        while (input == 1 || input == 2){
            if (input == 1) index = (index + 1) % numberOfPlayers;
            if (input == 2) index = (index - 1 + numberOfPlayers) % numberOfPlayers;
            Integer userIDtoDisplay = userIDs.get(index);
            String usernameToDisplay = userIDtoUsernames.get(userIDtoDisplay);
            PersonalBoardDescription boardToDisplay = userIDtoBoardDescriptions.get(userIDtoDisplay);
            out.println(usernameToDisplay + "'s personal board:");
            out.println(boardToDisplay);
            }
        returnToCorrectActionSelection();
    }

    private void returnToCorrectActionSelection(){
        if (majorActionDone) addNextDisplay("displayMinorActionSelection");
        else addNextDisplay("displayAllActionSelection");
    }
    //Handle the TakeResAction
    private void routeTakeResActionDisplay() {
        switch (takeResContext.getLastStep()) {
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
    public void chooseRowColumnNumber() {
        String rowColumnNumber = InputConsumer.getMarketRowColumnIndex(in, out);
        char firstLetter = rowColumnNumber.charAt(0);
        int index = Integer.parseInt(String.valueOf(rowColumnNumber.charAt(2)));
        if (firstLetter == 'R') {
            takeResContext.chooseRow(true);
        } else if (firstLetter == 'C') {
            takeResContext.chooseRow(false);
        } else takeResContext.setErrorTrue();
        takeResContext.setIndex(index);
        takeResContext.setLastStep(ROW_COLUMN_CHOSEN);
        VCEvent vcEvent = new VCEvent(TAKE_RES_CONTEXT_FILLED, takeResContext);
        publish(vcEvent);
    }
    public void chooseWhiteConverters() {
        Resources.ResType firstResOption = takeResContext.getWhiteConverters().get(0).getAbility().getResType();
        Resources.ResType secondResOption = takeResContext.getWhiteConverters().get(1).getAbility().getResType();
        int whiteMarbles = takeResContext.getWhiteMarbleNumber();
        out.println("You have two active white marble converter leader cards, and received " + whiteMarbles + " white marble from market tray");
        out.println("You can convert the white marbles into [1]" + firstResOption.toString() + " or [2]" + secondResOption.toString());
        while (whiteMarbles > 0) {
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
    public void chooseShelvesToPut() {
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
        if (index == 1) {
            out.println("Select a shelf that you want to remove all resources from:");
            Shelf.shelfPlace place = InputConsumer.getShelfPlace(in, out);
            takeResContext.setShelf(place);
            takeResContext.setLastStep(CLEAR_SHELF_CHOSEN);
        } else if (index == 2) {
            out.println("Select two shelves that you want to swap, extra resources will be discarded automatically:");
            Shelf.shelfPlace firstPlace = InputConsumer.getShelfPlace(in, out);
            Shelf.shelfPlace secondPlace = InputConsumer.getShelfPlace(in, out);
            if (firstPlace.equals(secondPlace)) {
                out.println("You cannot select the same shelf.");
                addNextDisplay("chooseShelvesToPut");
            }
            takeResContext.setShelves(firstPlace, secondPlace);
            takeResContext.setLastStep(SWAP_SHELVES_CHOSEN);
        } else if (index == 3) {
            List<Resources.ResType> resTypeList = new ArrayList<>();
            resTypeList.addAll(resources.getResTypes());
            Map<Shelf.shelfPlace, Resources.ResType> shelfToResMap = new HashMap<>();
            for (Shelf.shelfPlace place : Shelf.shelfPlace.values()) {
                out.println("Which type of resource you want to put into " + place.toString() + " shelf?");
                Resources.ResType selectedType = InputConsumer.getATypeAmongSet(in, out, resTypeList);
                resTypeList.remove(selectedType);
                shelfToResMap.put(place, selectedType);
            }
            takeResContext.setShelftoResTypeMap(shelfToResMap);
            takeResContext.setLastStep(PUT_RESOURCES_CHOSEN);
        } else {
            out.println("Ending take resource action...");
            VCEvent vcEvent = new VCEvent(TAKE_RES_ACTION_ENDED);
            publish(vcEvent);
            return;
        }
        VCEvent vcEvent = new VCEvent(TAKE_RES_CONTEXT_FILLED, takeResContext);
        publish(vcEvent);
    }
    //Handle the BuyDevCardAction
    private void routeBuyDevCardActionDisplay() {
        switch (buyDevCardContext.getLastStep()) {
            case CHOOSE_COLOR_LEVEL:
                addNextDisplay("chooseColorLevel");
                break;
            case EMPTY_DEVCARD_DECK_ERROR:
                setGeneralMsg("There is no available development card in that color and level.");
                addNextDisplay("displayGeneralMsg");
                addNextDisplay("chooseColorLevel");
                break;
            case NOT_ENOUGH_RES_FOR_DEVCARD_ERROR:
                setGeneralMsg("You don't have enough resources to buy that development card.");
                addNextDisplay("displayGeneralMsg");
                addNextDisplay("chooseColorLevel");
                break;
            case UNSUITABLE_FOR_DEVSLOTS_ERROR:
                setGeneralMsg("There are no suitable slots on your personal board for you to put the selected card on.");
                addNextDisplay("displayGeneralMsg");
                addNextDisplay("chooseColorLevel");
                break;
            case CHOOSE_DEV_SLOT:
                addNextDisplay("chooseDevSlotToPutDevCard");
                break;
            case CHOOSE_PAY_COST_FROM_WHERE:
                addNextDisplay("choosePayDevCardCostFromWhere");
                break;
            case NOT_ENOUGH_RES_IN_WAREHOUSE:
                setGeneralMsg("You selected more resources from warehouse than you can pay from there!");
                addNextDisplay("displayGeneralMsg");
                addNextDisplay("choosePayDevCardCostFromWhere");
                break;
            case NOT_ENOUGH_RES_IN_STRONGBOX:
                setGeneralMsg("You selected more resources from strongbox than you can pay from there!");
                addNextDisplay("displayGeneralMsg");
                addNextDisplay("choosePayDevCardCostFromWhere");
                break;
            case COST_PAID_DEVCARD_PUT:
                addNextDisplay("displayBuyDevActionEnd");
                break;
        }
    }
    public void chooseColorLevel(){
        String colorAndLevel = InputConsumer.getColorAndLevel(in, out);
        String[] parts = colorAndLevel.split("-");
        buyDevCardContext.setColor(DevCard.CardColor.valueOf(parts[0]));
        buyDevCardContext.setLevel(Integer.parseInt(parts[1]));
        buyDevCardContext.setLastStep(COLOR_LEVEL_CHOSEN);
        VCEvent vcEvent = new VCEvent(BUY_DEVCARD_CONTEXT_FILLED, buyDevCardContext);
        publish(vcEvent);
    }
    public void chooseDevSlotToPutDevCard(){
        List<DevSlot.slotPlace> suitableSlots = buyDevCardContext.getSuitableSlots();
        out.println("Select which development slot you want to put the selected card on.");
        DevSlot.slotPlace place = InputConsumer.getSlotPlace(in, out, suitableSlots);
        buyDevCardContext.setSelectedSlot(place);
        buyDevCardContext.setLastStep(DEVSLOT_CHOSEN);
        VCEvent vcEvent = new VCEvent(BUY_DEVCARD_CONTEXT_FILLED, buyDevCardContext);
        publish(vcEvent);

    }
    public void choosePayDevCardCostFromWhere(){
        out.println("Select warehouse or strongbox to pay the cost of the selected development card.");
        Resources remainingCost = buyDevCardContext.getRemainingCost();
        Resources payFromWarehouse = new Resources();
        Resources payFromStrongbox = new Resources();
        List<Resources.ResType> resTypeList = remainingCost.getResTypes(); //store list type in order to prevent modification of reamining cost while iterating it
        for(Resources.ResType resType: resTypeList){
            while(remainingCost.getNumberOfType(resType) > 0){
                out.println("From where do you want to pay 1 " + resType.toString());
                boolean warehouseSelected = InputConsumer.getWorS(in, out);
                if (warehouseSelected) payFromWarehouse.add(resType, 1);
                else payFromStrongbox.add(resType,1);
                remainingCost.subtract(resType,1);
            }
        }
        buyDevCardContext.setPayFromWarehouse(payFromWarehouse);
        buyDevCardContext.setPayFromStrongbox(payFromStrongbox);
        buyDevCardContext.setLastStep(PAY_FROM_WHERE_CHOSEN);
        VCEvent vcEvent = new VCEvent(BUY_DEVCARD_CONTEXT_FILLED, buyDevCardContext);
        publish(vcEvent);
    }

    //handle ActivateProdAction
    private void routeActivateProdActionDisplay() {
        switch (activateProdContext.getLastStep()) {
            case CHOOSE_DEV_SLOTS:
                addNextDisplay("chooseDevSlots");
                break;
            case EMPTY_DEV_SLOTS_ERROR:
                setGeneralMsg("There is no available development card in Slot");
                addNextDisplay("displayGeneralMsg");
                addNextDisplay("chooseDevSlots");
                break;
            case NOT_ENOUGH_RES_FOR_PRODUCTION_IN_WAREHOUSE:
                setGeneralMsg("You don't have enough resources in strongbox!");
                addNextDisplay("displayGeneralMsg");
                addNextDisplay("choosePayProductionCostFromWhere");
                break;
            case NOT_ENOUGH_RES_FOR_PRODUCTION_IN_STRONGBOX:
                setGeneralMsg("You don't have enough resources in strongbox !");
                addNextDisplay("displayGeneralMsg");
                addNextDisplay("choosePayProductionCostFromWhere");
                break;
            case COST_PAID:
                if(activateLeaderContext.getActivationLeaderCardBefore())
                    addNextDisplay("displayActivationProdActionEnd ");
                else{
                    VCEvent vcEvent = new VCEvent(ACTIVATE_LEADER_CONTEXT_SELECTED);
                    publish(vcEvent);
                }
                break;
        }
    }

    public void chooseDevSlots(){
        DevCard baseProd;
        Resources costLhsLeader = new Resources();
        int numberOfSlotAvailable = activateProdContext.getSlotAvailable().size(),j=0;
        List<DevSlot> slotAvailable =activateProdContext.getSlotAvailable();
        List<DevSlot> slotChosen = InputConsumer.getDevSlotIndexs(in, out,numberOfSlotAvailable,slotAvailable);

        int numberOfActiveProduceLeaderCard=0;
        while(j < activateLeaderContext.getActiveLeaderCard().size()) {
            if (activateLeaderContext.getActiveLeaderCard().get(j).getAbility().getAbilityType() == SpecialAbility.AbilityType.ADDPROD) {
                costLhsLeader.add(activateLeaderContext.getActiveLeaderCard().get(j).getAbility().getResType(),1);
                numberOfActiveProduceLeaderCard++;
            }j++;
        }
        activateProdContext.setLhlLeaderCard(costLhsLeader);
        if(numberOfActiveProduceLeaderCard != 0){
            out.println("Do want to use LeaderCard Production ability ? ");
            boolean leaderActivate = InputConsumer.getYesOrNo(in, out);
            if(leaderActivate){
                    activateProdContext.setActivationLeaderCardProduction(true);
                    activateProdContext.setNumberOfActiveLeaderProduction(numberOfActiveProduceLeaderCard);
                    chooseLeaderProdAction( numberOfActiveProduceLeaderCard);
            }
        }
        out.println("Do want to activate base production power ? ");
        boolean answer = InputConsumer.getYesOrNo(in,out);
        if(answer) {
            baseProd = InputConsumer.chooseBaseProdRes(in, out);
            activateProdContext.setBaseProdPower(true);
            activateProdContext.setBaseProductionCard(baseProd);
        }
        activateProdContext.setSlots(slotChosen);
        activateProdContext.setLastStep(DEV_SLOTS_CHOOSEN);
        VCEvent vcEvent = new VCEvent(ACTIVATE_PROD_CONTEXT_FILLED, activateProdContext);
        publish(vcEvent);
    }
    public void choosePayProductionCostFromWhere(){
        if(activateProdContext.getNumberOfActiveLeaderProduction()>0)
        {
            out.println("Select warehouse or strongbox to pay the left side for leader production.");
            boolean warehouseSelected = InputConsumer.getWorS(in, out);
            activateProdContext.setFromWhereToPayForLeader(warehouseSelected);
        }
        if(activateProdContext.getBaseProdPower()) {
            out.println("Select warehouse or strongbox to pay the left side for default production.");
            boolean warehouseSelectedForDefault = InputConsumer.getWorS(in, out);
            activateProdContext.setFromWhereToPayForDefault(warehouseSelectedForDefault);
        }
        if(activateProdContext.getSelectedCard().size() > 0 ){
            out.println("Select warehouse or strongbox to pay the left side of Development card for production.");
            boolean warehouseSelectedForDevslots = InputConsumer.getWorS(in, out);
            activateProdContext.setFromWhereToPayForDevslots(warehouseSelectedForDevslots);
        }
        activateProdContext.setLastStep(PAY_PRODUCTION_FROM_WHERE_CHOSEN);
        VCEvent vcEvent = new VCEvent(ACTIVATE_PROD_CONTEXT_FILLED, activateProdContext);
        publish(vcEvent);
    }
    public void chooseLeaderProdAction(int numberOfActiveProduceCard) {

        Resources RHS = new Resources();
        out.println("You have " + numberOfActiveProduceCard + " active produce leader cards ");
        out.println("How many do you want to activate?  ");
        int numOfCard = InputConsumer.getANumberBetween(in, out, 1, numberOfActiveProduceCard);
        RHS.add(InputConsumer.chooseRhsLeaderCard(in, out, numOfCard));
        activateProdContext.setRhlLeaderCard(RHS);

    }
    //handle ActivateLeaderAction
    private void routeActivateLeaderActionDisplay() {
        switch (activateLeaderContext.getLastStep()) {
            case CHOOSE_ACTION:
                addNextDisplay("chooseLeaderAction");
                break;
            case CHOOSE_DISCARD_A_LEADER:
                setGeneralMsg("You don't have enough resources in strongbox!");
                addNextDisplay("displayGeneralMsg");
                break;
            case POWER_ACTIVATED:


                break;
        }
    }
    public void chooseLeaderAction() {
        int j = 0;
        out.println("What Leader action do you want to make? ");
        out.println("[1]Discard,[2]Activation [3]both?  ");
        int numOfActionChoosen = InputConsumer.getANumberBetween(in, out, 1, 3);
        if(activateLeaderContext.getActiveLeaderCard().size()>0)
        switch(numOfActionChoosen) {
            case 1:
                chooseDiscardLeaderAction();
                break;
            case 2:
                chooseLeaderActivationAction();
                break;
            case 3:
                //todo ask to omer if this work as i thought it, i mean first
                // call the discard action and handle it in server side then
                // after changing the player card and faithtrack call the activation leader action with the right card
                chooseDiscardLeaderAction();
                chooseLeaderActivationAction();
                break;
        }
    }
    public void chooseDiscardLeaderAction() {
        int j = 0;
        out.println("Do want to Discard LeaderCard ? ");
        boolean discard = InputConsumer.getYesOrNo(in, out);
        int numOfDiscardableLeaderCard = (activateLeaderContext.getPlayerCard().size() - activateLeaderContext.getNumberOfActiveLeader());
        out.println("You can active  " + numOfDiscardableLeaderCard + "  leader cards ");
        if (discard) {
            if (numOfDiscardableLeaderCard > 0) {
                while (j < numOfDiscardableLeaderCard) {
                    out.println("You have " + numOfDiscardableLeaderCard + "leader cards ");
                    out.println("How many do you want to Discard[?  ");
                    out.println("[1]for the first,[2] for the second [3] for both?  ");

                    int numOfDiscardedCard = InputConsumer.getANumberBetween(in, out, 1, 3);

                    if(numOfDiscardableLeaderCard == 1 ) {
                        activateLeaderContext.setNumberOfDiscardLeader(numOfDiscardedCard);
                    }

                }
            }
        }
        activateLeaderContext.setLastStep(DISCARD_LEADER_CARD);
        VCEvent vcEvent = new VCEvent(ACTIVATE_PROD_CONTEXT_FILLED, activateProdContext);
        publish(vcEvent);
    }
    public void chooseLeaderActivationAction() {
        int j=0;
        List<LeaderCard> activeLeaderCard = new ArrayList<>();
        int numOfActivatableLeaderCard = (activateLeaderContext.getPlayerCard().size() - activateLeaderContext.getNumberOfActiveLeader());
        out.println("You can active  " + numOfActivatableLeaderCard + "  leader cards ");
        if (numOfActivatableLeaderCard > 0) {

            out.println("Do want to activate LeaderCard ? ");
            boolean leaderActivate = InputConsumer.getYesOrNo(in, out);
            activateLeaderContext.setActivationLeaderCard(leaderActivate);

            if (leaderActivate) {
                out.println("Your Leader Card:");
                while (j < numOfActivatableLeaderCard) {
                    out.println("Do you want to activate this Leader Card:" + "[" + (j + 1) + "] :" + activateLeaderContext.getPlayerCard().get(j));
                    if (InputConsumer.getYesOrNo(in, out)) {
                        if (checkLeaderActivationAction(activateLeaderContext.getPlayerCard().get(j)))
                            activeLeaderCard.add(activateLeaderContext.getPlayerCard().get(j));
                        else
                            out.println("You don't satisfy the requirement:");
                    }
                    j++;
                }
                if (activeLeaderCard.size() > 0) {
                    out.println("Do want to activate LeaderCard action before(yes) or after(no) normal action ? ");
                    boolean leaderActivationBefore = InputConsumer.getYesOrNo(in, out);
                    activateLeaderContext.setActivationLeaderCardBefore(leaderActivationBefore);
                    activateLeaderContext.setLastStep(LEADER_CARD_CHOOSEN);
                } else {
                    activateLeaderContext.setLastStep(LEADER_CARD_NOT_CHOOSEN);
                }
            }
        }
        activateLeaderContext.setActiveLeaderCard(activeLeaderCard);
        VCEvent vcEvent = new VCEvent(ACTIVATE_PROD_CONTEXT_FILLED, activateProdContext);
        publish(vcEvent);
    }
    private boolean checkLeaderActivationAction(LeaderCard leaderToCheck) {
        int numberOfSlotAvailable = activateProdContext.getSlotAvailable().size();
        List<DevSlot> slotAvailable = activateProdContext.getSlotAvailable();
        int count = 0;
        int i = 0;
        while (i < numberOfSlotAvailable) {
            if (activateProdContext.getSlotAvailable().get(i).getTopDevCard() != null) {
                if (leaderToCheck.getAbility().getAbilityType() == SpecialAbility.AbilityType.ADDPROD) {
                    if (leaderToCheck.getRequirement().getColor(0).equals(slotAvailable.get(i).getTopDevCard().getColor()) &&
                            slotAvailable.get(i).getTopDevCard().getLevel() == 2) {
                        return true;
                    }
                }
                if (leaderToCheck.getAbility().getAbilityType() == SpecialAbility.AbilityType.DISCOUNT) {
                    if (count == 2)
                        return true;
                    if (leaderToCheck.getRequirement().getColor(0).equals(activateProdContext.getSlotAvailable().get(i).getTopDevCard().getColor())
                            || leaderToCheck.getRequirement().getColor(1).equals(activateProdContext.getSlotAvailable().get(i).getTopDevCard().getColor())) {
                        count++;
                    }
                }
                if (leaderToCheck.getAbility().getAbilityType() == SpecialAbility.AbilityType.EXSTRASLOT) {
                    Resources totalRes=new Resources();
                    totalRes.add(activateLeaderContext.getTotalResources());
                    if ((totalRes.getResTypes().contains(leaderToCheck.getRequirement().getResource().getOnlyType()))
                            &&(totalRes.getNumberOfType(leaderToCheck.getRequirement().getResource().getOnlyType()) == 5)){
                        return true;
                    }
                }
                //todo ask to omer
                if (leaderToCheck.getAbility().getAbilityType() == SpecialAbility.AbilityType.CONVERTWHITE) {
                    if (leaderToCheck.getRequirement().getColor(0).equals(slotAvailable.get(i).getTopDevCard().getColor()) &&
                            slotAvailable.get(i).getTopDevCard().getLevel() == 2) {
                        return true;
                    }
                }
            }
            i++;
        }
        return false;
    }




    @Override
    public void update(Event event) {
        if (event instanceof CVEvent) {
            CVEvent cvEvent = (CVEvent) event;
            CVEvent.EventType eventType = cvEvent.getEventType();
            if (eventType.equals(SELECT_ALL_ACTION)) {
                majorActionDone = false;
                addNextDisplay("displayAllActionSelection");
            }
            else if (eventType.equals(TAKE_RES_FILL_CONTEXT)){
                takeResContext = (TakeResActionContext) cvEvent.getEventPayload(TakeResActionContext.class);
                routeTakeResActionDisplay();
            } else if (eventType.equals(BUY_DEVCARD_FILL_CONTEXT)) {
                buyDevCardContext = (BuyDevCardActionContext) cvEvent.getEventPayload(BuyDevCardActionContext.class);
                routeBuyDevCardActionDisplay();
            } else if (eventType.equals(ACTIVATE_PROD_FILL_CONTEXT)) {
                activateProdContext = (ActivateProdActionContext) cvEvent.getEventPayload(ActivateProdActionContext.class);
                routeActivateProdActionDisplay();
            }else if (eventType.equals(ACTIVATE_LEADER_FILL_CONTEXT)) {
                activateLeaderContext = (LeaderActionContext) cvEvent.getEventPayload(LeaderActionContext.class);
                routeActivateLeaderActionDisplay();
            } else if (eventType.equals(SELECT_MINOR_ACTION)){
                majorActionDone = true;
                addNextDisplay("displayMinorActionSelection");
            } else {
                initialCVevent = cvEvent;
                routeInitialActionsDisplay();
            }
        } else if (event instanceof MVEvent) {
            MVEvent mvEvent = (MVEvent) event;
            Integer userIDofUpdatedBoard = mvEvent.getUserID();
            switch (mvEvent.getEventType()) {
                case MARKET_TRAY_UPDATE:
                    marketTrayDescription = mvEvent.getJsonContent();
                    break;
                case DEVCARD_MATRIX_UPDATE:
                    devCardMatrixDescription = mvEvent.getJsonContent();
                    break;
                case WAREHOUSE_UPDATE:
                    userIDtoBoardDescriptions.get(userIDofUpdatedBoard).setWarehouseDescription(mvEvent.getJsonContent());
                    break;
                case STRONGBOX_UPDATE:
                    userIDtoBoardDescriptions.get(userIDofUpdatedBoard).setStrongboxDescription(mvEvent.getJsonContent());
                    break;
                case DEVSLOTS_UPDATE:
                    userIDtoBoardDescriptions.get(userIDofUpdatedBoard).setDevSlotsDescription(mvEvent.getJsonContent());
                    break;
                case FAITHPOINT_UPDATE:
                    userIDtoBoardDescriptions.get(userIDofUpdatedBoard).setFaithTrackDescription(mvEvent.getJsonContent());
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
        for (Listener<VCEvent> listener : listenerList)
            listener.update(event);
    }

    @Override
    public synchronized void displayIdle() {
        try {
            this.wait(1000);
        } catch (InterruptedException e) {
        }
        String idleSymbols = "✞⨎⌬☺⌺";
        String backSpace = "\b";
        StringBuilder idleSymbolBar = new StringBuilder();
        int symbolIndex = 0;
        boolean appendtoRight = true;
        int lastBarSize = 0;
        out.print("Waiting for the other players... ");
        out.flush();

        while (!shouldStopDisplayIdle()) {
            out.print(idleSymbolBar);
            out.flush();
            lastBarSize = idleSymbolBar.length();

            try {
                this.wait(400);
            } catch (InterruptedException e) {
            }
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
            for (int i = 0; i < lastBarSize; i++) {
                out.print(backSpace);
            }
        }

        stopIdle = false;
        for (int i = 0; i < lastBarSize + 15; i++)
            out.print(backSpace);
        out.flush();
    }

    @Override
    public synchronized boolean shouldStopDisplayIdle() {
        return stopIdle;
    }

    @Override
    public synchronized void stopDisplayIdle() {
        stopIdle = true;
        notifyAll();
    }

    @Override
    public synchronized void displayGeneralMsg() {
        out.println(generalmsg);
    }

    @Override
    public void setGeneralMsg(String msg) {
        generalmsg = msg;
    }

    public void setUserIDtoUsernames(Map<Integer, String> userIDtoUsernames) {
        this.userIDtoUsernames = userIDtoUsernames;
    }

    // METHODS THAT WON'T BE USED
    @Override
    public synchronized void displayLobby() {
        out.println("Waiting users in the lobby are:");
        for (String username : client.getUserIDtoUserNames().values())
            out.println(username);
    }

}
