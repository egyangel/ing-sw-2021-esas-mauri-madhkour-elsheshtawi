package it.polimi.ingsw.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server1 {
    public static void main(String[] args) throws IOException {
        ServerSocket s1 = null;
        try {

            s1 = new ServerSocket(1010);
            s1.setReuseAddress(true);
            // running infinite loop for getting client request
            while (true) {

                // socket to receive incoming clients requests
                Socket client = s1.accept();

                // Displaying that new client is connectedto server
                System.out.println("New client connected" + client.getInetAddress().getHostAddress());

                // create a new thread object

                ClientHandler clientSock = new ClientHandler(client);
                clientSock.run();
                clientSock.init();

                //tread that handle each client separately
                new Thread(clientSock).start();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (s1 != null) {
                try {
                    s1.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
