package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.utility.MsgPrinterToCLI;
import it.polimi.ingsw.utility.messages.*;
import it.polimi.ingsw.view.IView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

public class ServerHandler implements Runnable, Listener<VCEvent>, Publisher<Event> {
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Client client;
    private IView view;
    private Integer userID;

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
        System.out.println("\nConnected to server at " + socket.getInetAddress());
        view.transitionToDisplay("displayLogin");
        try {
            handleConnection();
        } catch (IOException e) {
            System.out.println("Server connection dropped at " + socket.getInetAddress());
        }
    }

    public void sendMessage(Message msg){
        MsgPrinterToCLI.printMessage(MsgPrinterToCLI.MsgDirection.OUTGOINGfromCLIENT, msg);
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
                MsgPrinterToCLI.printMessage(MsgPrinterToCLI.MsgDirection.INCOMINGtoCLIENT, msg);
                if(msg.getMsgtype() == Message.Type.HEARTBEAT){
                    // do heartbeat thing
                }else {
                    client.handleMessage(msg);
                }
            } catch (ClassNotFoundException | ClassCastException e) {
                System.out.println("Unidentified message from server");
            }
        }
    }



    @Override
    public void update(VCEvent event) {

    }

    @Override
    public void subscribe(Listener<Event> listener) {

    }

    @Override
    public void unsubscribe(Listener<Event> listener) {

    }

    @Override
    public void publish(Event event) {

    }
}
