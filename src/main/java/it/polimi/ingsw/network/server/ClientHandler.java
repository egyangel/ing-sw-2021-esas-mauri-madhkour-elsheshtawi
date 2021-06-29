package it.polimi.ingsw.network.server;

import it.polimi.ingsw.utility.messages.Message;
import it.polimi.ingsw.utility.MsgPrinterToCLI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


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
            if(server.isFirstPlayerSetupDone()){
                sendMessage(new Message(Message.MsgType.DISPLAY_LOGIN));
            } else {
                sendMessage(new Message(Message.MsgType.DISPLAY_CREATE_GAME));
            }

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
                //TODO MESSAGE PRINTER FOR DEBUG
//                MsgPrinterToCLI.printMessage(MsgPrinterToCLI.MsgDirection.INCOMINGtoSERVER, msg);
                if(msg.getMsgtype() == Message.MsgType.HEARTBEAT){
                    // do heartbeat thing
                }else {
                    server.handleMessage(userID, msg);
                }
            } catch (ClassNotFoundException | ClassCastException e) {
                System.out.println("Unidentified message into client " + userID);
            }
        }
    }

    public void sendMessage(Message msg){
        msg.setUserID(userID);
        //TODO MESSAGE PRINTER FOR DEBUG
//        MsgPrinterToCLI.printMessage(MsgPrinterToCLI.MsgDirection.OUTGOINGfromSERVER, msg);
        try {
            oos.writeObject((Object) msg);
        } catch (IOException e) {
            System.out.println("Cannot send message to client " + userID);
        }
    }
}
