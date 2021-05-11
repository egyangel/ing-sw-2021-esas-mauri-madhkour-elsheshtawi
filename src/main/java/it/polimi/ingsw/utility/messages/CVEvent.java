package it.polimi.ingsw.utility.messages;

public class CVEvent implements Event{
    public enum Type {
        ASK_WHICH_COLUMN_ROW,
        ASK_WHICH_DEV_CARD,
        ASK_WHICH_PROD_SLOTS,
        ASK_WHICH_LEADER_CARD_TO_ACTIVATE
    }
}
