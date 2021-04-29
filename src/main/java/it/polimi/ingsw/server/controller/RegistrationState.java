package it.polimi.ingsw.controller;

import it.polimi.ingsw.server.model.Game;

import java.util.Scanner;

// the first state, for registering player, setting up connection with server(this later)
public class RegistrationState implements GameState {

    Game game;

    public RegistrationState(Game game) {
        this.game = game;
    }

    @Override
    public void enter() {
        addTwoPlayersCLI();
    }

    private void addTwoPlayersCLI() {
        // proper input management (name cannot be number only etc) will be done later
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter first player name:");
        String playerName = scanner.nextLine();
        game.addNewPlayer(playerName);
        System.out.println("Enter second player name:");
        playerName = scanner.nextLine();
        game.addNewPlayer(playerName);
    }
}
