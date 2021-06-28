package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.utility.messages.*;
import it.polimi.ingsw.view.IView;

public class GUI implements IView, Listener<Event>{
    private Client client;

    public GUI(Client client) {
    }

    public GUI() {
    }
//the first gui have the role of greet and start dislay
    public void displayGreet() {
        client = new Client();
       // this.client.setGui(this);
        new FirstGui();
    }

    public void displaySetup() {
        new ConnectionToServerGui();

    }

    public void displayLogin() {
            new LoginGui();
    }

    public void displayIdle() {

    }

    public void displayLobby() {

    }

    public boolean shouldStopDisplayIdle() {
        return false;
    }

    public void stopDisplayIdle() {

    }

    public void displayGeneralMsg() {

    }

    public void setGeneralMsg(String string) {

    }

    public void addNextDisplay(String displayA) {

    }

    public void displayFirstLogin() {

    }

    public void startDisplay() {

    }

    public void update(Event event) {

    }

    public void subscribe(Listener<VCEvent> listener) {

    }

    public void unsubscribe(Listener<VCEvent> listener) {

    }

    public void publish(VCEvent event) {

    }
}
