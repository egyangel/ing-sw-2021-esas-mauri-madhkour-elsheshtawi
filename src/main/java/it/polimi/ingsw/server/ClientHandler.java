package it.polimi.ingsw.server;

import it.polimi.ingsw.server.controller.GameController;
import it.polimi.ingsw.server.model.Game;

import java.io.*;
import java.net.Socket;

//this class handle the multi-client connection to the server
public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    public void run() {
        PrintWriter out = null;
        BufferedReader in = null;
        try {

            // get the outputstream of client
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // get the inputstream of client
            in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));

            String line;
            while ((line = in.readLine()) != null) {

                // writing the received message from client
                System.out.printf(" Sent from the client: %s\n", line);
                this.init();
                out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * init the client and connect it to the game
     */
    public void init() throws IOException {
        this.output = new ObjectOutputStream(this.clientSocket.getOutputStream());
        // ObjectInputStream  input = new ObjectInputStream(this.clientSocket.getInputStream());
        Game game = new Game();
        GameController gameController = new GameController(game);
        gameController.startGame();

    }

    public void sendMessage(String msg) throws IOException {
        output.writeChars(msg);
    }
}

