package it.polimi.ingsw.network.client;

import it.polimi.ingsw.utility.JsonConverter;
import it.polimi.ingsw.utility.MsgPrinterToCLI;
import it.polimi.ingsw.utility.messages.*;
import it.polimi.ingsw.view.IView;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.gui.GUI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerHandler implements Runnable, Listener<VCEvent>, Publisher<Event> {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Client client;
    private CLI viewCLI;
    private GUI viewGUI;
    private Integer userID = 0;
    private List<Listener<Event>> listenerList = new ArrayList<>();

    public ServerHandler(Socket socket, Client client) {
        this.socket = socket;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Can't connect to server at " + socket.getInetAddress());
            return;
        }
        //TODO DEBUG CONNECTION
//        System.out.println("\nConnected to server at " + socket.getInetAddress());
        if (client.getView() instanceof CLI) {
            viewCLI = (CLI) client.getView();
            viewCLI.subscribe(this);
            this.subscribe(viewCLI);
        } else if(client.getView() instanceof GUI){
            viewGUI = (GUI) client.getView();
            this.subscribe(viewGUI);
        } else{
            System.out.println("View is not created properly");
        }
        try {
            handleConnection();
        } catch (IOException e) {
            System.out.println("Server connection dropped at " + socket.getInetAddress());
        }
    }
    //TODO MESSAGE PRINTER FOR DEBUG
    public void sendMessage(Message msg){
        msg.setUserID(userID);
//        MsgPrinterToCLI.printMessage(MsgPrinterToCLI.MsgDirection.OUTGOINGfromCLIENT, msg);
        try {
            oos.writeObject(msg);
            oos.flush();
            oos.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleConnection() throws IOException{
        while(true) {
            try {
                Message msg = (Message)ois.readObject();
                Message.MsgType msgType = msg.getMsgtype();
                //TODO MESSAGE PRINTER FOR DEBUG
//                MsgPrinterToCLI.printMessage(MsgPrinterToCLI.MsgDirection.INCOMINGtoCLIENT, msg);
                if(msgType == Message.MsgType.HEARTBEAT){
                    // do heartbeat thing
                } else if(msgType == Message.MsgType.CV_EVENT){
                    CVEvent cvEvent = (CVEvent) JsonConverter.fromMsgToObject(msg, CVEvent.class);
                    publish(cvEvent);
                } else if(msgType == Message.MsgType.MV_EVENT){
                    MVEvent mvEvent = (MVEvent) JsonConverter.fromMsgToObject(msg, MVEvent.class);
                    publish(mvEvent);
                } else if(msgType == Message.MsgType.VC_EVENT) {
                    System.out.println("Unexpected client to client message");
                } else {
                    client.handleSetUpMessage(msg);
                }
            } catch (ClassNotFoundException | ClassCastException e) {
                System.out.println("Unidentified message from server");
            }
        }
    }

    @Override
    public void update(VCEvent event) {
        sendMessage(new Message(Message.MsgType.VC_EVENT, event));
    }

    @Override
    public void subscribe(Listener<Event> listener) {
        listenerList.add(listener);
    }

    @Override
    public void unsubscribe(Listener<Event> listener) {
        listenerList.remove(listener);
    }

    @Override
    public void publish(Event event) {
        for(Listener<Event> listener : listenerList){
            listener.update(event);
        }
    }

    public void setUserId(Integer userID) {
        this.userID = userID;
    }
}
