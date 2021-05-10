package it.polimi.ingsw.utility.messages;

public class VCEvent implements Event{
    public enum Type {
        ASK_TAKE_RES_ROW_COLUMN,
        ASK_SOMETHING,
        CREATE_TAKE_RES_ACT,
        CREATE_SOMETHING
    }
}
