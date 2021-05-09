package it.polimi.ingsw.network.server;

import it.polimi.ingsw.utility.messages.Message;
import it.polimi.ingsw.utility.messages.MsgType;
import it.polimi.ingsw.utility.MsgPrinterToCLI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

public class ClientHandler implements Runnable{
    private Integer userID;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Server server;


    public ClientHandler(Integer userID, Socket socket, Server server) {
        this.userID = userID;
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Can't connect to client " + userID + " at " + socket.getInetAddress());
            return;
        }
        System.out.println("Connected to client " + userID + " at " + socket.getInetAddress());

        try {
            handleConnection();
        } catch (IOException e) {
            System.out.println("Client " + userID + " dropped");
        }

        try {
            oos.close();
            ois.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleConnection() throws IOException {
        while(true) {
            try {
                Message msg = (Message)ois.readObject();
                MsgPrinterToCLI.printMessage(MsgPrinterToCLI.MsgDirection.INCOMINGtoSERVER, msg);
                if(isConnectionMessage(msg)){
                    handleConnectionMessage(msg);
                }else {
                    server.handleGameMessage(msg);
                }
            } catch (ClassNotFoundException | ClassCastException e) {
                System.out.println("Unidentified message from client " + userID);
            }
        }
    }

    private boolean isConnectionMessage(Message msg){
        return Arrays.asList(MsgType.LOGIN, MsgType.HEARTBEAT).contains(msg.getMsgtype());
    }

    private void handleConnectionMessage(Message msg){
        Message msgToSend = null;
        switch (msg.getMsgtype()) {
            case LOGIN:
                msgToSend = new Message(userID, MsgType.LOGIN, "UserID assigned as " + userID);
                break;
            case HEARTBEAT:
                break;
            default:
                break;
        }
        if (msgToSend != null) {
            sendMessage(msgToSend);
        } else {
            System.out.println("Cannot process connection message properly");
        }
    }

    public void sendMessage(Message msg){
        MsgPrinterToCLI.printMessage(MsgPrinterToCLI.MsgDirection.OUTGOINGfromSERVER, msg);
        try {
            oos.writeObject((Object) msg);
        } catch (IOException e) {
            System.out.println("Cannot send message to client " + userID);
        }
    }
}
