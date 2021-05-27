package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.server.Server;

import java.util.HashMap;
import java.util.Map;

public class SoloController extends Controller {
    private HashMap<String,String> CrossToken; // CROSSTOKEN,Position


    public SoloController(Game game, Server server) {
        super(game, server);
    }

    @Override
    public void createMatch(Map<Integer, String> userIDtoNameMap) {
        super.createMatch(userIDtoNameMap);
        //TODO INIT SOLO GAME
        /*
       -- Setup the game as usual. Set yourself as the first player.
       -- Place the Black Cross token on the first space of your
       -- Faith Track (together with your Faith Marker).
       -- Shuffle the Solo Action tokens, create a stack and
       -- place it face-down on the table
         */
    }

    //TODO init the Cross Token into the first place
    public void placeCrossToken(String position) {

    }

    public void initSoloActionToken(){
        game.setSoloActionToken();
    }


}
