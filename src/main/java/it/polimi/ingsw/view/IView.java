package it.polimi.ingsw.view;

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

    void transitionToDisplay(String displayName);
}
