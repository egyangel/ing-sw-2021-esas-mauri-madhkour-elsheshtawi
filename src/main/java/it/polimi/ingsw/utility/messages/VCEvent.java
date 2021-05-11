package it.polimi.ingsw.utility.messages;

// events after interactions of user
public class VCEvent implements Event{
    public enum Type {
        TAKE_RES_ACTION,
        BUY_DEV_CARD_ACTION,
        ACTIVATE_PROD_ACTION,
        ARRANGE_WAREHOUSE,
        ACTIVATE_LEADERCARD
    }
}
