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

    void displayLobby();

    boolean shouldStopDisplayIdle();

    void stopDisplayIdle();

    void displayGeneralMsg();

    void setGeneralMsg(String string);

    void addNextDisplay(String displayName);

    void displayCreateGame();
}
