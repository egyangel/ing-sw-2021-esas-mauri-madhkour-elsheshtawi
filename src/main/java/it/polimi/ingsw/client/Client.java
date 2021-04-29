package it.polimi.ingsw.client;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

//CLient class
public class Client {
    public static void main(String[] args)  {

        //trying to estabilish a connection with the server
        try(Socket socket = new Socket("localhost",1010)){
            //read from the server
            BufferedReader serverToClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //reading from the input
            Scanner scan = new Scanner(System.in);
            String keyBoard = null;
            //write to server
            PrintWriter clientToServer = new PrintWriter(socket.getOutputStream(), true);


            while(!"exit".equals(keyBoard)) {
                //read from input
                keyBoard=scan.nextLine();

                clientToServer.println(keyBoard);
                clientToServer.flush();

                System.out.println("Server replied " + serverToClient.readLine());

            }

            scan.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
