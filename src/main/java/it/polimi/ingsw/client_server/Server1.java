package it.polimi.ingsw.client_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server1 {
   // public static void main(String[] args) throws IOException {
        ServerSocket s1 = new ServerSocket(9999);


    public Server1() throws IOException {

        while(true){
            //receive a string
            Socket s = null;
            try {
                s = s1.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Riceive the String
            InputStreamReader is = new InputStreamReader(s.getInputStream());
            BufferedReader in = new BufferedReader(is);
             try {
                System.out.println("il server riceve: " + in.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
