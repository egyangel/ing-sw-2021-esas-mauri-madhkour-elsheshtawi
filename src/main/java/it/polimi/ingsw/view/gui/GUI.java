package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.network.client.Client;
import it.polimi.ingsw.view.IView;

public class GUI implements IView {
    public GUI(Client client) {
    }

    @Override
    public void displayGreet() {

    }

    @Override
    public void displaySetup() {

    }

    @Override
    public void displayLogin() {

    }

    @Override
    public void displayIdle() {

    }

    @Override
    public boolean shouldStopDisplayIdle() {
        return false;
    }

    @Override
    public void stopDisplayIdle() {

    }

    @Override
    public void transitionToDisplay(String displayName) {

    }

    public void startDisplay() {

    }
}
