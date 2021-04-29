package it.polimi.ingsw.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable{
    private ServerHandler serverHandler;
    final static int SOCKET_PORT = 3000;

    public static void main(String[] args){
        Client client = new Client();
        // note: this does not create a new thread
        client.run();
    }

    @Override
    public void run() {
        // getting localhost ip for now
        InetAddress ip = null;
        try {
            ip = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        Socket socket;
        try {
            socket = new Socket(ip, SOCKET_PORT);
        } catch (IOException e) {
            System.out.println("Server unreachable");
            return;
        }

        serverHandler = new ServerHandler(socket, this);
        Thread thread = new Thread(serverHandler);
        thread.start();

        //handle user-interaction...
        //...
    }
}
