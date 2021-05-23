package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.DevCard;
import it.polimi.ingsw.model.LeaderCard;
import it.polimi.ingsw.model.PersonalBoard;

public class GameObjectPrinter {
    //TODO FOR LORENZO
    public static String printDevCard(DevCard card){
        //stringbuilder, for loop, string format etc
        //to substitute placeholder characters inside string with devcard values, it will look something like:
        //strinformatter("%d-16", card.getCost()) etc in necessary places
        return null;
    }

    // argument can be Shelf[]
    public static String printWarehouse(PersonalBoard board){
        return null;
    }

    // argument can be Resources, because stronbox is represented with a single resources object in personal board
    public static String printStrongbox(PersonalBoard board){
        return null;
    }

    public static String printDevSlots(PersonalBoard board){
        return null;
    }

    public static String printDevCard(LeaderCard card){
        return null;
    }
}
