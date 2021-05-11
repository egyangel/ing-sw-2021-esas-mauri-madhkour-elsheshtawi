package it.polimi.ingsw.utility.messages;

// not a priority
public class MVEvent implements Event{
    public enum Type {
        WAREHOUSE_UPDATED,
        FAITH_TRACK_UPDATED,
        STRONGBOX_UPDATED,
        LEADER_CARD_ACTIVATED
    }
}
