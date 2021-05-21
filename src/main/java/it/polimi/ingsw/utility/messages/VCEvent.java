package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.utility.JsonConverter;

// events after interactions of user
public class VCEvent extends Event{
    public enum eventType {
        LEADER_CARDS_CHOOSEN,
        INIT_RES_CHOOSEN,
        ROW_COLUMN_INDEX_CHOOSEN,
        WHITE_MARBLES_CONVERTED_IF_NECESSARY,
        SWAP_SHELF_INDEX_CHOOSEN,
        DISCARD_SHELF_CHOOSEN,
        LEVEL_COLOR_DEVCARD_CHOOSEN
    }
    private eventType eventType;

    public VCEvent(eventType eventType, Object object){
        super(object);
        this.jsonContent = JsonConverter.toJson(object);
    }

    public eventType getEventType() {
        return eventType;
    }
}
