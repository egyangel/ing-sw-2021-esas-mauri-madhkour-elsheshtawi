package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.server.ClientHandler;
import it.polimi.ingsw.utility.MsgPrinterToCLI;
import it.polimi.ingsw.utility.messages.Message;
import it.polimi.ingsw.utility.messages.MsgType;
import it.polimi.ingsw.view.IView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

public class ServerHandler implements Runnable{
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Client client;
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
        client.transitionToView("displayLogin");
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
                if(isConnectionMessage(msg)){
                    handleConnectionMessage(msg);
                }else {
                    client.handleGameMessage(msg);
                }
            } catch (ClassNotFoundException | ClassCastException e) {
                System.out.println("Unidentified message from server");
            }
        }
    }

    private void handleConnectionMessage(Message msg){
        switch (msg.getMsgtype()) {
            case LOGIN:
                userID = msg.getUserID();
                client.displayMessage(msg);
                break;
            case HEARTBEAT:
                break;
        }
    }

    private boolean isConnectionMessage(Message msg){
        return Arrays.asList(MsgType.LOGIN, MsgType.HEARTBEAT).contains(msg.getMsgtype());
    }
}
