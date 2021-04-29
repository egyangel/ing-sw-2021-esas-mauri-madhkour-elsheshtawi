package it.polimi.ingsw.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private String id;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;


    public ClientHandler(String id, Socket socket) {
        this.id = id;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // these are the channels which client handlers write/read message objects into/from
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
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
            ois.close();
            oos.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleConnection() throws IOException {
        while(true) {
            try {
                // read messages
            } catch (Exception e) {}
        }
    }
}
