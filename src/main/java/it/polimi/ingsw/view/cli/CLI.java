package it.polimi.ingsw.view.cli;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.utility.InputConsumer;
import it.polimi.ingsw.utility.messages.*;
import it.polimi.ingsw.view.IView;

import static it.polimi.ingsw.utility.messages.ActivateProdAlternativeContext.ActionStep.*;
import static it.polimi.ingsw.utility.messages.TakeResActionContext.ActionStep.*;
import static it.polimi.ingsw.utility.messages.BuyDevCardActionContext.ActionStep.*;
import static it.polimi.ingsw.utility.messages.VCEvent.EventType.*;

import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Cli class , it is the client interface
 */

public class CLI implements IView, Publisher<VCEvent>, Listener<Event> {
    private final Client client;
    private final PrintWriter out;
    private final Scanner in;
    private Map<String, Runnable> displayNameMap = new HashMap<>();
    private Queue<Runnable> displayTransitionQueue = new ArrayDeque<>();
    private boolean shouldTerminateClient;
    private boolean stopIdle;
    private String generalMsg;
    private TakeResActionContext takeResContext;
    private BuyDevCardActionContext buyDevCardContext;
    private ActivateProdAlternativeContext activateProdContext;
    private LeaderActionContext leaderActionContext;
    private CVEvent generalCVEvent;
    private List<Listener<VCEvent>> listenerList = new ArrayList<>();
    private String marketTrayDescription;
    private String devCardMatrixDescription;
    private Map<Integer, PersonalBoardDescription> userIDtoBoardDescriptions = new HashMap<>();
    private Map<Integer, String> userIDtoUsernames = new HashMap<>();

    /**
     * Constructor of the class
     * initialize the in and out stream.
     * @param client player object
     *
     */
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
        displayNameMap.put("displayCreateGame", this::displayCreateGame);
        displayNameMap.put("displayLogin", this::displayLogin);
        displayNameMap.put("displayGeneralMsg", this::displayGeneralMsg);
        displayNameMap.put("displayFourLeaderCard", this::displayFourLeaderCard);
        displayNameMap.put("displayTurnAssign", this::displayTurnAssign);
        displayNameMap.put("displayAllActionSelection", this::displayAllActionSelection);
        displayNameMap.put("displayMinorActionSelection", this::displayMinorActionSelection);
        displayNameMap.put("chooseLeaderToActivate", this::chooseLeaderToActivate);
        displayNameMap.put("chooseLeaderToDiscard", this::chooseLeaderToDiscard);
        displayNameMap.put("chooseRowColumnNumber", this::chooseRowColumnNumber);
        displayNameMap.put("chooseShelvesToPut", this::chooseShelvesToPut);
        displayNameMap.put("chooseColorLevel", this::chooseColorLevel);
        displayNameMap.put("displayOwnPersonalBoard", this::displayOwnPersonalBoard);
        displayNameMap.put("displayMarketAndDevCards", this::displayMarketAndDevCards);
        displayNameMap.put("displayOtherPersonalBoards", this::displayOtherPersonalBoards);
        displayNameMap.put("displayEndTurn", this::displayEndTurn);
        displayNameMap.put("chooseDevSlotToPutDevCard", this::chooseDevSlotToPutDevCard);
        displayNameMap.put("chooseDevSlotsForProd", this::chooseDevSlotsForProd);
        displayNameMap.put("displayScoreBoard", this::displayScoreBoard);
        displayNameMap.put("chooseWhiteConverters", this::chooseWhiteConverters);

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
                // for debug display idle cancel, maybe for demo too
//                displayNameMap.get("displayIdle").run();
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
        out.println("Enter IP address of the server:");
        String ip = InputConsumer.getIP(in, out);
        out.println("Enter port number of the server:");
        int portNumber = InputConsumer.getPortNumber(in, out);
//        TODO IMPORTANT, ADD IP PUBLIC ADDRESS OF SERVER AND PORT TO client main() arguments
//        String ip = "localhost";
//        int portNumber = 30000; //for debug
        out.println("Connecting to server...");
        client.connectToServer(ip, portNumber);
    }

    @Override
    public void displayCreateGame() {
        out.println("Choose a username:");
        // TODO FOR DEBUG, DONT FORGET TO GO BACK TO MANUAL INPUT
//        String username = "omer";
        String username = InputConsumer.getUserName(in, out);
        out.println("Choose number of players you would like to play with:");
//        Integer numberOfPlayers = 2;
        Integer numberOfPlayers = InputConsumer.getNumberOfPlayers(in, out);
        Map<String, String> firstLoginMap = new HashMap<>();
        firstLoginMap.put("numberOfPlayers", numberOfPlayers.toString());
        firstLoginMap.put("username", username);
        Message loginMsg = new Message(Message.MsgType.REQUEST_FIRST_LOGIN, firstLoginMap);
        client.sendToServer(loginMsg);
    }

    @Override
    public void displayLogin() {
        out.println("Choose a username:");
        // TODO FOR DEBUG, DONT FORGET TO GO BACK TO MANUAL INPUT
//        String username = "John";
        String username = InputConsumer.getUserName(in, out);
        Message loginMsg = new Message(Message.MsgType.REQUEST_LOGIN, username);
        client.sendToServer(loginMsg);
    }

    public void initEmptyPersonalBoards(){
        for(Integer userID: userIDtoUsernames.keySet()){
            PersonalBoardDescription pbd = new PersonalBoardDescription();
            Map<PersonalBoard.PopeArea, Boolean> map = new HashMap<>();
            map.put(PersonalBoard.PopeArea.FIRST, false);
            map.put(PersonalBoard.PopeArea.SECOND, false);
            map.put(PersonalBoard.PopeArea.THIRD, false);
            pbd.setTileMap(map);
            pbd.setFaithPoints(0); //initial faith point will be given if necessary after turn assign
            userIDtoBoardDescriptions.put(userID, pbd);
        }
    }

    /**
     * method that handle the draw of the 4 leader cards and manage the two leader that the player wants to keep.
     * after the choice, an event from the View to the Controller (VCEvent) is published so that the controller
     * can know about the player's choice
     */
    public void displayFourLeaderCard() {
        out.println("Here are the four leader card options, select two of them:");
        Type type = new TypeToken<List<LeaderCard>>() {
        }.getType();
        List<LeaderCard> fourLeaderCards = (List<LeaderCard>) generalCVEvent.getEventPayload(type);
        for (int i = 0; i < fourLeaderCards.size(); i++) {
            out.println(i + 1 + ") " + fourLeaderCards.get(i).describeLeaderCard());
        }
        out.println("Enter the index of first leader card to keep:");
        // TODO FOR DEBUG, DONT FORGET TO GO BACK TO MANUAL INPUT
//        Integer firstIndex = 1;
        Integer firstIndex = InputConsumer.getANumberBetween(in, out, 1, 4);
        out.println("Enter the index of second leader card to keep:");
//        Integer secondIndex = 2;
        Integer secondIndex = InputConsumer.getANumberBetween(in, out, 1, 4);
        while (firstIndex.equals(secondIndex)) {
            out.println("Please enter a different index than first selection:");
            secondIndex = InputConsumer.getANumberBetween(in, out, 1, 4);
        }
        List<LeaderCard> twoLeaderCards = new ArrayList<>();
        twoLeaderCards.add(fourLeaderCards.get(firstIndex - 1));
        twoLeaderCards.add(fourLeaderCards.get(secondIndex - 1));
        VCEvent vcEvent = new VCEvent(LEADER_CARDS_CHOOSEN, twoLeaderCards);
        publish(vcEvent);
    }

    /**
     * method that handle the assign of the order of the players and give them based on the order the initial resources
     */
    public void displayTurnAssign() {
        Integer turn = (Integer) generalCVEvent.getEventPayload(Integer.class);
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
                break;
            case 2:
                out.println("You are the second player.");
                out.println("You will have one initial resource of your choosing in the warehouse.");
                // TODO FOR DEBUG, DONT FORGET TO GO BACK TO MANUAL INPUT
//                Resources.ResType initResType = Resources.ResType.COIN;
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

    /**
     * methods that handle the menu of the player's actions and based on the action chosen call respectively
     * the method that handle, except for the 3 normal actions and the leader card activation.
     * When the player chooses one of the 3 normal or the leader card
     * actions an event it created and sent so that the server to handle it
     */
    public void displayAllActionSelection() {
        VCEvent vcEvent;
        out.println("\nIt is your turn now!");
        out.println("[1] Take resource from market");
        out.println("[2] Buy one development card");
        out.println("[3] Activate the production");
        out.println("[4] View your personal board");
        out.println("[5] View market tray and development cards");
        out.println("[6] Activate a leader card");
        out.println("[7] Discard a leader card");
        out.println("[8] View other personal boards");
        out.println("[9] End turn");
        out.println("Enter the index of the action you want to take:");
        int index = InputConsumer.getANumberBetween(in, out, 1, 9);
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
                addNextDisplay("displayOwnPersonalBoard");
                addNextDisplay("displayAllActionSelection");
                break;
            case 5:
                addNextDisplay("displayMarketAndDevCards");
                addNextDisplay("displayAllActionSelection");
                break;
            case 6:
                vcEvent = new VCEvent(ACTIVATE_LEADER_SELECTED);
                publish(vcEvent);
                break;
            case 7:
                vcEvent = new VCEvent(DISCARD_LEADER_SELECTED);
                publish(vcEvent);
                break;
            case 8:
                addNextDisplay("displayOtherPersonalBoards");
                addNextDisplay("displayAllActionSelection");
                break;
            case 9:
                addNextDisplay("displayEndTurn");
                break;
        }
    }

    /**
     * method that handle a further menu of minor action, to allow the player to make more
     * action during the game. It is only a display method of the personal board
     */
    public void displayMinorActionSelection() {
        out.println("\nDo you want to execute any other action?");
        out.println("Enter the index of the action you want to take:");
        out.println("[1] View your personal board");
        out.println("[2] View market tray and development cards");
        out.println("[3] Activate a leader card");
        out.println("[4] Discard a leader card");
        out.println("[5] View other personal boards");
        out.println("[6] End turn");
        int index = InputConsumer.getANumberBetween(in, out, 1, 11);
        switch (index) {
            case 1:
                addNextDisplay("displayOwnPersonalBoard");
                addNextDisplay("displayMinorActionSelection");
                break;
            case 2:
                addNextDisplay("displayMarketAndDevCards");
                addNextDisplay("displayMinorActionSelection");
                break;
            case 3:
                VCEvent vcEvent = new VCEvent(ACTIVATE_LEADER_SELECTED);
                publish(vcEvent);
                break;
            case 4:
                VCEvent vcEventTwo = new VCEvent(DISCARD_LEADER_SELECTED);
                publish(vcEventTwo);
                break;
            case 5:
                addNextDisplay("displayOtherPersonalBoards");
                addNextDisplay("displayMinorActionSelection");
                break;
            case 6:
                addNextDisplay("displayEndTurn");
                break;
        }
    }

    public void displayOwnPersonalBoard(){
        out.println(userIDtoBoardDescriptions.get(client.getUserID()).describePersonalBoard());
    }

    public void displayMarketAndDevCards(){
        out.println("Market tray" + "\n" + marketTrayDescription + "\n");
        out.println("Development cards" + "\n" + devCardMatrixDescription);
    }

    public void displayWarehouse() {
        out.println(userIDtoBoardDescriptions.get(client.getUserID()).getWarehouseDescription());
    }

    public void displayEndTurn(){
        out.println("Ending turn...");
        VCEvent vcEvent = new VCEvent(END_TURN);
        publish(vcEvent);
    }

    public void displayOtherPersonalBoards() {
        LinkedList<Integer> userIDs = new LinkedList<>();
        userIDs.addAll(userIDtoBoardDescriptions.keySet());
        userIDs.remove(client.getUserID());
        int index = 0;
        int numberOfOtherPlayers = userIDtoBoardDescriptions.keySet().size() - 1;
        int input = 1;
        while (input == 1 || input == 2) {
            if (input == 1) index = (index + 1) % numberOfOtherPlayers;
            if (input == 2) index = (index - 1 + numberOfOtherPlayers) % numberOfOtherPlayers;
            Integer userIDtoDisplay = userIDs.get(index);
            String usernameToDisplay = userIDtoUsernames.get(userIDtoDisplay);
            PersonalBoardDescription boardToDisplay = userIDtoBoardDescriptions.get(userIDtoDisplay);
            out.println(usernameToDisplay + "'s personal board:");
            out.println(boardToDisplay.describePersonalBoard());
            out.println("Enter [1] for next board, [2] for previous board, [3] to choose action:");
            input = InputConsumer.getANumberBetween(in, out, 1, 3);
        }
    }

    /**
     * methods that handle the take resource action. It check the resources context based on the last step
     * (then next action that the player have to make )call the methods that:
     * -handle the draw from the market tray,
     * -handle the activation of the white converter leader card
     * -handle in which shelves the player put the resources
     */
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

    /**
     * methods that handle the draw from the market tray.
     * Ask to the player to choose a row or column and so that he can take the resources that correspond to that choice
     * then sent an event to the server so that , it take the resources from the market and change the market configuration
     * After the player fill the TAKE_RES_CONTEXT_FILLED it publish an VC(view to controller)
     * event that check and manage the transformation
     */
    public void chooseRowColumnNumber() {
        out.println(this.marketTrayDescription);
        String rowColumnNumber = InputConsumer.getMarketRowColumnIndex(in, out);
        char firstLetter = rowColumnNumber.charAt(0);
        int index = Integer.parseInt(String.valueOf(rowColumnNumber.charAt(2)));
        if (firstLetter == 'r') {
            takeResContext.chooseRow(true);
        } else if (firstLetter == 'c') {
            takeResContext.chooseRow(false);
        } else takeResContext.setErrorTrue();
        takeResContext.setIndex(index);
        takeResContext.setLastStep(ROW_COLUMN_CHOSEN);
        VCEvent vcEvent = new VCEvent(TAKE_RES_CONTEXT_FILLED, takeResContext);
        publish(vcEvent);
    }

    /**
     * methods that handle the activation of the white converter leader card
     * if the player draw white marbles ang has a white converter leader card active
     * he can convert that white marbles in some other resources based on the leader card ability.
     * After the player fill the takeResContext it publish an VC(view to controller)
     * event that check and manage the transformation
     */
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

    /**
     * methods that handle the phase where the player manage the resources and the shelves configuration,
     * the shelves manager
     */
    public void chooseShelvesToPut() {
        out.println("Your warehouse looks like:");
        displayWarehouse();
        Resources resources = takeResContext.getResources();
        String resourceString = " Nothing";
        if (resources != null)
            resourceString = resources.toString();
        List<LeaderCard> extraSlotLeaders = takeResContext.getExtraSlotLeaderList();
        out.println("You have " + resourceString + " that you can put to your warehouse.");
        out.println("Extra resources that you don't put will be discarded automatically");
        out.println("Select one of the options below:");
        out.println("[1] Clear shelf");
        out.println("[2] Swap shelves");
        out.println("[3] Select resource type and shelf to put that kind of resources");
        if(extraSlotLeaders.size() > 0){
            out.println("[4] Select leader card to put onto slot");
            out.println("[5] End take resource action");
        } else {
            out.println("[4] End take resource action");
        }
        int index;
        if (extraSlotLeaders.size() > 0)
            index = InputConsumer.getANumberBetween(in, out, 1, 5);
        else
            index = InputConsumer.getANumberBetween(in, out, 1, 4);
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
            List<Resources.ResType> resType = null;
            if (resources != null)
                resType = resources.getResTypes();
            if (resType != null)
                resTypeList.addAll(resources.getResTypes());
            Map<Shelf.shelfPlace, Resources.ResType> shelfToResMap = new HashMap<>();
            for (Shelf.shelfPlace place : Shelf.shelfPlace.values()) {
                if (resTypeList.isEmpty())
                    continue;
                out.println("Do you want to add a resource into " + place.toString() + " shelf?");
                boolean answer = InputConsumer.getYesOrNo(in, out);
                if (!answer) continue;
                if (resTypeList.isEmpty()) break;
                out.println("Which type of resource you want to put into " + place.toString() + " shelf?");
                Resources.ResType selectedType = InputConsumer.getATypeAmongSet(in, out, resTypeList);
                resTypeList.remove(selectedType);
                shelfToResMap.put(place, selectedType);
            }
            takeResContext.setShelfToResTypeMap(shelfToResMap);
            takeResContext.setLastStep(PUT_RESOURCES_CHOSEN);
        } else if (index == 4 && extraSlotLeaders.size() > 0){
            Map<Resources.ResType, Integer> resTypeIntegerMap = new HashMap<>();
            for (LeaderCard card: extraSlotLeaders){
                out.println(card.describeLeaderCard());
                out.println("How many number of " + card.getAbility().getResType().name() + " do you want to put onto this card?");
                int toAddToExtraSlot = InputConsumer.getANumberBetween(in, out, 0, 2);
                if (toAddToExtraSlot > takeResContext.getResources().getNumberOfType(card.getAbility().getResType())){
                    out.println("You don't have that much resources to put onto the leader card!");
                    addNextDisplay("chooseShelvesToPut");
                    return;
                }
                resTypeIntegerMap.put(card.getAbility().getResType(), toAddToExtraSlot);
            }
            takeResContext.setExtraSlotMap(resTypeIntegerMap);
            takeResContext.setLastStep(EXTRA_SLOT_CHOSEN);
        } else {
            takeResContext.addDiscardedRes(takeResContext.getResources().sumOfValues());
            out.println("Ending take resource action...");
            VCEvent vcEvent = new VCEvent(TAKE_RES_ACTION_ENDED, takeResContext);
            publish(vcEvent);
            return;
        }
        VCEvent vcEvent = new VCEvent(TAKE_RES_CONTEXT_FILLED, takeResContext);
        publish(vcEvent);
    }

    /**
     * methods that handle the dev card buying. Based on the CV event and last step of BuyCardContext
     * that has been set
     * in the server side this methods call the action that correspond to that event.
     */
    private void routeBuyDevCardActionDisplay() {
        switch (buyDevCardContext.getLastStep()) {
            case CHOOSE_COLOR_LEVEL:
                addNextDisplay("chooseColorLevel");
                break;
            case EMPTY_DEVCARD_DECK_ERROR:
                setGeneralMsg("There is no available development card in that color and level.");
                addNextDisplay("displayGeneralMsg");
                addNextDisplay("displayAllActionSelection");
                break;
            case NOT_ENOUGH_RES_FOR_DEVCARD_ERROR:
                setGeneralMsg("You don't have enough resources to buy that development card.");
                addNextDisplay("displayGeneralMsg");
                addNextDisplay("displayAllActionSelection");
                break;
            case UNSUITABLE_FOR_DEVSLOTS_ERROR:
                setGeneralMsg("There are no suitable slots on your personal board for you to put the selected card on.");
                addNextDisplay("displayGeneralMsg");
                addNextDisplay("displayAllActionSelection");
                break;
            case CHOOSE_DEV_SLOT:
                addNextDisplay("chooseDevSlotToPutDevCard");
                break;
            case COST_PAID_DEVCARD_PUT:
                setGeneralMsg("Your development slots now looks like:");
                addNextDisplay("displayGeneralMsg");
                addNextDisplay("displayMinorActionSelection");
                VCEvent vcEvent = new VCEvent(BUY_DEVCARD_ACTION_ENDED);
                publish(vcEvent);
        }
    }

    /**
     * methods that handle the choice of the color and level of the Development card.
     * After the player fill the buyDevCardContext context  it publish an VC(view to controller)
     * THen server check if the action it is correct based on the rule and the PLAYER personal board
     */
    public void chooseColorLevel() {
        String colorAndLevel = InputConsumer.getColorAndLevel(in, out);
        String[] parts = colorAndLevel.split("-");
        buyDevCardContext.setColor(DevCard.CardColor.valueOf(parts[0]));
        buyDevCardContext.setLevel(Integer.parseInt(parts[1]));
        buyDevCardContext.setLastStep(COLOR_LEVEL_CHOSEN);
        VCEvent vcEvent = new VCEvent(BUY_DEVCARD_CONTEXT_FILLED, buyDevCardContext);
        publish(vcEvent);
    }

    /**
     * methods that handle the choice of slot where to put put the Development Card.
     * After the player fill the buyDevCardContext context  it publish an VC(view to controller)
     * THen server check if the action it is correct based on the rule and the PLAYER personal board
     */
    public void chooseDevSlotToPutDevCard() {
        List<DevSlot.slotPlace> suitableSlots = buyDevCardContext.getSuitableSlots();
        out.println("Select which development slot you want to put the selected card on.");
        DevSlot.slotPlace place = InputConsumer.getSlotPlace(in, out, suitableSlots);
        buyDevCardContext.setSelectedSlot(place);
        buyDevCardContext.setLastStep(DEVSLOT_CHOSEN);
        VCEvent vcEvent = new VCEvent(BUY_DEVCARD_CONTEXT_FILLED, buyDevCardContext);
        publish(vcEvent);

    }

    /**
     * methods that handle the Activate Prod Action. Based on the CV event and last step of activateProdContext
     * that has been set
     * in the server side this methods call the action that correspond to that event.
     */
    private void routeActivateProdActionDisplay() {
        switch (activateProdContext.getLastStep()) {
            case CHOOSE_DEV_SLOTS_FOR_PROD:
                addNextDisplay("chooseDevSlotsForProd");
                break;
            case NOT_ENOUGH_RES_ON_PERSONAL_BOARD:
                setGeneralMsg("You do not have enough resources on your personal board!");
                addNextDisplay("displayGeneralMsg");
                addNextDisplay("displayAllActionSelection");
                break;
            case PRODUCTION_DONE:
                setGeneralMsg("The production is done!");
                VCEvent vcEvent = new VCEvent(ACTIVATE_PROD_ACTION_ENDED);
                publish(vcEvent);
                break;
        }
    }

    /**
     * methods that fill the activateProdContext context.
     * The player chooses:
     * -the dev cards slots,
     * -choose if activate the leader card production a if the player owned a card with that ability choose resources get
     * -choose to activate the default Production and that means that he has to choose the LHS and RHS
     * After the player fill the activateProdContext it publish an VC(view to controller)
     * event so that the server can check if everything is ok
     */
    public void chooseDevSlotsForProd() {
        Map<DevSlot.slotPlace, DevCard> slotToCardMap = activateProdContext.getSlotMap();
        List<DevSlot.slotPlace> slotList = new ArrayList<>(slotToCardMap.keySet());
        List<DevCard> selectedCards = new ArrayList<>();
        if(!slotList.isEmpty()) {
            out.println("You have the below options for development cards:");
            for (Map.Entry<DevSlot.slotPlace, DevCard> entry : slotToCardMap.entrySet()) {
                out.println(entry.getKey().name() + ": " + entry.getValue().describeDevCard());
            }
            for(DevSlot.slotPlace place:slotList){
                out.println("Do you want to activate " + place.name() + " slot?");
                boolean answer = InputConsumer.getYesOrNo(in, out);
                if (answer)
                    selectedCards.add(slotToCardMap.get(place));
            }
            activateProdContext.setSelectedDevCards(selectedCards);
        }
        out.println("Do you want to use basic production?");
        boolean basicProd = InputConsumer.getYesOrNo(in, out);
        if(basicProd){
            activateProdContext.setBasicProdOptionSelected(true);
            Resources basicProdCost = new Resources();
            out.println("Enter the first resource type to convert from:");
            Resources.ResType type = InputConsumer.getResourceType(in, out);
            basicProdCost.add(type,1);
            out.println("Enter the second resource type to convert from:");
            type = InputConsumer.getResourceType(in, out);
            basicProdCost.add(type,1);
            activateProdContext.setBasicProdLHS(basicProdCost);
            out.println("Enter the resource type to convert to:");
            type = InputConsumer.getAllResourceType(in, out);
            Resources outputres = new Resources();
            outputres.add(type, 1);
            activateProdContext.setBasicProdRHS(outputres);
        } else {
            activateProdContext.setBasicProdOptionSelected(false);
        }
        boolean addProdAnswer = false;
        if (activateProdContext.getAddProdOptionAvailable()) {
            List<LeaderCard> cardList = activateProdContext.getAddProdLeaderList();
            out.println("You have the below options for additional production from active leader cards:");
            for (LeaderCard card: cardList){
                out.println(card.describeLeaderCard());
            }
            out.println("Do you want to use additional production from leader cards?");
            addProdAnswer = InputConsumer.getYesOrNo(in, out);
            if (addProdAnswer) {
                activateProdContext.setAddProdOptionSelected(true);
                for(LeaderCard card: cardList){
                    out.println(card.describeLeaderCard());
                    out.println("Do you want to activate this card?");
                    boolean answer = InputConsumer.getYesOrNo(in, out);
                    if (answer) {
                        out.println("Enter a resource type to convert to:");
                        Resources.ResType resType1 = InputConsumer.getResourceType(in, out);
                        activateProdContext.addLeaderCost(card.getAbility().getResType());
                        activateProdContext.addLeaderProd(resType1);
                    }
                }
            } else {
                activateProdContext.setAddProdOptionSelected(false);
            };
        }
        if (slotList.isEmpty() && !basicProd && !addProdAnswer){
            addNextDisplay("displayAllActionSelection");
            return;
        }
        activateProdContext.setLastStep(DEVLSLOTS_CHOOSEN_FOR_PROD);
        VCEvent vcEvent = new VCEvent(ACTIVATE_PROD_CONTEXT_FILLED, activateProdContext);
        publish(vcEvent);
    }

    /**
     *  methods that handle the activation action of a leader card
     */
    public void chooseLeaderToActivate(){
        List<LeaderCard> cardList = leaderActionContext.getInactiveCards();
        List<Integer> cardIndexToActivateList = new ArrayList<>();
        for(int i = 0; i < cardList.size(); i++){
            out.println("Do you want to activate this leader card: " + cardList.get(i).describeLeaderCard());
            boolean answer = InputConsumer.getYesOrNo(in, out);
            if(answer)
                cardIndexToActivateList.add(i);
        }
        leaderActionContext.setSelectedInactiveCardIndexes(cardIndexToActivateList);
        VCEvent vcEvent = new VCEvent(ACTIVATE_LEADER_CONTEXT_FILLED, leaderActionContext);
        publish(vcEvent);
    }

    /**
     * methods that handle the discard action of a leader card
     *
     */
    public void chooseLeaderToDiscard(){
        List<LeaderCard> activeCardList = leaderActionContext.getActiveCards();
        List<LeaderCard> inactiveCardList = leaderActionContext.getInactiveCards();
        List<Integer> activeCardIndexToActivateList = new ArrayList<>();
        List<Integer> inactiveCardIndexToActivateList = new ArrayList<>();
        for(int i = 0; i < activeCardList.size(); i++){
            out.println("Do you want to discard this active leader card: " + activeCardList.get(i).describeLeaderCard());
            boolean answer = InputConsumer.getYesOrNo(in, out);
            if(answer)
                activeCardIndexToActivateList.add(i);
        }
        for(int i = 0; i < inactiveCardList.size(); i++){
            out.println("Do you want to discard this inactive leader card: " + inactiveCardList.get(i).describeLeaderCard());
            boolean answer = InputConsumer.getYesOrNo(in, out);
            if(answer)
                inactiveCardIndexToActivateList.add(i);
        }
        leaderActionContext.setSelectedActiveCardIndexes(activeCardIndexToActivateList);
        leaderActionContext.setSelectedInactiveCardIndexes(inactiveCardIndexToActivateList);
        VCEvent vcEvent = new VCEvent(DISCARD_LEADER_CONTEXT_FILLED, leaderActionContext);
        publish(vcEvent);
    }

    private void displayScoreBoard(){
        Type type = new TypeToken<Map<String,Integer>>() {}.getType();
        Map<String,Integer> scoreboard = (Map<String,Integer>) generalCVEvent.getEventPayload(type);
        out.println("The scoreboard is:");
        for(Map.Entry<String, Integer> entry: scoreboard.entrySet()){
            out.println("Username: "+ entry.getKey() + ", Victory Points: " + entry.getValue());
        }
        client.closeServerConnection();
    }

    @Override
    public void update(Event event) {
        if (event instanceof CVEvent) {
            CVEvent cvEvent = (CVEvent) event;
            handleCVEvent(cvEvent);
        } else if (event instanceof MVEvent) {
            MVEvent mvEvent = (MVEvent) event;
            handleMVEvent(mvEvent);
        } else {
            out.println("Unidentified MV or CV event");
        }
    }

    private void handleCVEvent(CVEvent cvEvent){
        switch (cvEvent.getEventType()) {
            case SELECT_ALL_ACTION:
                addNextDisplay("displayAllActionSelection");
                break;
            case TAKE_RES_FILL_CONTEXT:
                takeResContext = (TakeResActionContext) cvEvent.getEventPayload(TakeResActionContext.class);
                routeTakeResActionDisplay();
                break;
            case BUY_DEVCARD_FILL_CONTEXT:
                buyDevCardContext = (BuyDevCardActionContext) cvEvent.getEventPayload(BuyDevCardActionContext.class);
                routeBuyDevCardActionDisplay();
                break;
            case ACTIVATE_PROD_FILL_CONTEXT:
                activateProdContext = (ActivateProdAlternativeContext) cvEvent.getEventPayload(ActivateProdAlternativeContext.class);
                routeActivateProdActionDisplay();
                break;
            case ACTIVATE_LEADER_FILL_CONTEXT:
                leaderActionContext = (LeaderActionContext) cvEvent.getEventPayload(LeaderActionContext.class);
                addNextDisplay("chooseLeaderToActivate");
                break;
            case DISCARD_LEADER_FILL_CONTEXT:
                leaderActionContext = (LeaderActionContext) cvEvent.getEventPayload(LeaderActionContext.class);
                addNextDisplay("chooseLeaderToDiscard");
                break;
            case SELECT_MINOR_ACTION:
                addNextDisplay("displayMinorActionSelection");
                break;
            case CHOOSE_TWO_LEADER_CARD:
                generalCVEvent = cvEvent;
                addNextDisplay("displayFourLeaderCard");
                break;
            case ASSIGN_TURN_ORDER:
                generalCVEvent = cvEvent;
                addNextDisplay("displayTurnAssign");
                break;
            case END_GAME_TRIGGERED:
                generalMsg = "The end of the game is triggered, remaining players until the the player with inkwell will play their last turn!";
                addNextDisplay("displayGeneralMsg");
                break;
            case END_RESULT:
                generalMsg = (String) cvEvent.getEventPayload(String.class);
                addNextDisplay("displayGeneralMsg");
                break;
            case END_VP_COUNTED:
                generalCVEvent = cvEvent;
                addNextDisplay("displayScoreBoard");
                break;
        }
    }

    private void handleMVEvent(MVEvent mvEvent) {
        Integer userIDofUpdatedBoard = mvEvent.getUserID();
        switch (mvEvent.getEventType()) {
            case MARKET_TRAY_UPDATE:
                MarketTray marketTray = (MarketTray) mvEvent.getEventPayload(MarketTray.class);
                marketTrayDescription = marketTray.describeMarketTray();
                break;
            case DEVCARD_MATRIX_UPDATE:
                Type devCardListType = new TypeToken<List<DevCard>>() {
                }.getType();
                List<DevCard> topDevCards = (List<DevCard>) mvEvent.getEventPayload(devCardListType);
                devCardMatrixDescription = ObjectPrinter.printDevCardMatrixAsList(topDevCards);
                ;
                break;
            case WAREHOUSE_UPDATE:
                Type shelfListType = new TypeToken<List<Shelf>>() {
                }.getType();
                List<Shelf> shelves = (List<Shelf>) mvEvent.getEventPayload(shelfListType);
                String wareHouseDescription = ObjectPrinter.printWarehouse(shelves);
                userIDtoBoardDescriptions.get(userIDofUpdatedBoard).setWarehouseDescription(wareHouseDescription);
                break;
            case STRONGBOX_UPDATE:
                Resources strongboxRes = (Resources) mvEvent.getEventPayload(Resources.class);
                String strongboxDescription = ObjectPrinter.drawStrongBox(strongboxRes);
                userIDtoBoardDescriptions.get(userIDofUpdatedBoard).setStrongboxDescription(strongboxDescription);
                break;
            case DEVSLOTS_UPDATE:
                Type devSlotListType = new TypeToken<List<DevSlot>>() {
                }.getType();
                List<DevSlot> devSlots = (List<DevSlot>) mvEvent.getEventPayload(devSlotListType);
                String devSlotDescription = ObjectPrinter.printDevSlots(devSlots);
                userIDtoBoardDescriptions.get(userIDofUpdatedBoard).setDevSlotsDescription(devSlotDescription);
                break;
            case FAITHPOINT_UPDATE:
                Integer faithpoints = (Integer) mvEvent.getEventPayload(Integer.class);
                userIDtoBoardDescriptions.get(userIDofUpdatedBoard).setFaithPoints(faithpoints);
                Map<PersonalBoard.PopeArea, Boolean> tileMap = userIDtoBoardDescriptions.get(userIDofUpdatedBoard).getTileMap();
                String faithTrackDescription = ObjectPrinter.faithTrackPrinter(tileMap, faithpoints);
                userIDtoBoardDescriptions.get(userIDofUpdatedBoard).setFaithTrackDescription(faithTrackDescription);
                break;
            case FAITHTRACK_UPDATE:
                Type mapType = new TypeToken<Map<PersonalBoard.PopeArea, Boolean>>() {}.getType();
                Map<PersonalBoard.PopeArea, Boolean> tileMapTwo = (Map<PersonalBoard.PopeArea, Boolean>) mvEvent.getEventPayload(mapType);
                userIDtoBoardDescriptions.get(userIDofUpdatedBoard).setTileMap(tileMapTwo);
                int faithPointsTwo = userIDtoBoardDescriptions.get(userIDofUpdatedBoard).getFaithPoints();
                String faithTrackDescriptionTwo = ObjectPrinter.faithTrackPrinter(tileMapTwo, faithPointsTwo);
                userIDtoBoardDescriptions.get(userIDofUpdatedBoard).setFaithTrackDescription(faithTrackDescriptionTwo);
                break;
            case ACTIVE_LEADER_CARD_UPDATE:
                Type activeLeaderListType = new TypeToken<List<LeaderCard>>() {
                }.getType();
                List<LeaderCard> activeLeaders = (List<LeaderCard>) mvEvent.getEventPayload(activeLeaderListType);
                String activeLeaderDescription = ObjectPrinter.printLeaders(activeLeaders, true);
                userIDtoBoardDescriptions.get(userIDofUpdatedBoard).setActiveLeaderCardsDescription(activeLeaderDescription);
                break;
            case INACTIVE_LEADER_CARD_UPDATE:
                Type inactiveLeaderListType = new TypeToken<List<LeaderCard>>() {
                }.getType();
                List<LeaderCard> inactiveLeaders = (List<LeaderCard>) mvEvent.getEventPayload(inactiveLeaderListType);
                String inactiveLeaderDescription = ObjectPrinter.printLeaders(inactiveLeaders, false);
                userIDtoBoardDescriptions.get(userIDofUpdatedBoard).setInactiveLeaderCardsDescription(inactiveLeaderDescription);
                break;
        }
    }

        @Override
        public void subscribe (Listener < VCEvent > listener) {
            listenerList.add(listener);
        }

        @Override
        public void unsubscribe (Listener < VCEvent > listener) {
            listenerList.remove(listener);
        }

        @Override
        public void publish (VCEvent event){
            for (Listener<VCEvent> listener : listenerList)
                listener.update(event);
        }

        @Override
        public synchronized void displayIdle () {
            try {
            this.wait(2000);
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
        public synchronized boolean shouldStopDisplayIdle () {
            return stopIdle;
        }

        @Override
        public synchronized void stopDisplayIdle () {
            stopIdle = true;
            notifyAll();
        }

        @Override
        public synchronized void displayGeneralMsg () {
            out.println(generalMsg);
        }

        @Override
        public void setGeneralMsg (String msg){
            generalMsg = msg;
        }

        public void setUserIDtoUsernames (Map < Integer, String > userIDtoUsernames){
            this.userIDtoUsernames = userIDtoUsernames;
        }

        // METHODS THAT WON'T BE USED
        @Override
        public synchronized void displayLobby () {
            out.println("Waiting users in the lobby are:");
            for (String username : client.getUserIDtoUserNames().values())
                out.println(username);
        }
    }
