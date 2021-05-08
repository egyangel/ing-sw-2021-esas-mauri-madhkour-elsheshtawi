package it.polimi.ingsw.network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Integer userID;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;


    public ClientHandler(Integer userID, Socket socket) {
        this.userID = userID;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // these are the channels which client handlers write/read message objects into/from
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
            // handle messages
        }
    }
}
