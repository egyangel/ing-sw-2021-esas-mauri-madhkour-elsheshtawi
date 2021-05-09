package it.polimi.ingsw.view;

import it.polimi.ingsw.utility.messages.Message;

public interface IView {
    void startDisplay();
    // show name of game etc
    void displayGreet();
    // form for server connection data
    void displaySetup();

    void displayLogin();
    // user interaction shows error on CLI & GUI
    void displayIdle();

    boolean shouldStopDisplayIdle();

    void stopDisplayIdle();

    void displayGeneralMsg(Message msg);

    void transitionToDisplay(String displayName);
}
