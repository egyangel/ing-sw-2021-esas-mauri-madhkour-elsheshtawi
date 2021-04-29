package it.polimi.ingsw.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerHandler implements Runnable{
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Client client;

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

        System.out.println("Connected to server at " + socket.getInetAddress());
        try {
            handleConnection();
        } catch (IOException e) {
            System.out.println("Server connection dropped at " + socket.getInetAddress());
        }
    }

    private void handleConnection() throws IOException{
        while(true) {
            try {
                // read messages
            } catch (Exception e) {}
        }
    }
}
