package it.polimi.ingsw.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {
    public static final int SOCKET_PORT = 3000;

    // Vector to store active clients
    static Vector<ClientHandler> clientHandlerVector = new Vector<>();

    // counter for clients (may not be needed but just not the forget)
    static int id = 0;

    public static void main(String[] args){
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(SOCKET_PORT);
        } catch (IOException e) {
            System.out.println("Can't open server socket");
            System.exit(1);
            return;
        }

        Socket socket;
        // don't wait for 5th player and so on to be added later
        while (true){
            try {
                socket = serverSocket.accept();
                System.out.println("New client request received : " + socket);

                System.out.println("Creating a new handler for this client...");
                ClientHandler clientHandler = new ClientHandler("client " + id, socket);

                System.out.println("Adding this client to active client list...");
                // add this client to active clients list
                clientHandlerVector.add(clientHandler);
                Thread thread = new Thread(clientHandler);
                thread.start();
                id++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}