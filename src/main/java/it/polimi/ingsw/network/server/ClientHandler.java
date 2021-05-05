package it.polimi.ingsw.network.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private String id;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;


    public ClientHandler(String id, Socket socket) {
        this.id = id;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // these are the channels which client handlers write/read message objects into/from
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Can't connect to client  " + id + " at " + socket.getInetAddress());
            return;
        }
        System.out.println("Connected to client  " + id + " at " + socket.getInetAddress());

        try {
            handleConnection();
        } catch (IOException e) {
            System.out.println("Client " + id + " dropped");
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
