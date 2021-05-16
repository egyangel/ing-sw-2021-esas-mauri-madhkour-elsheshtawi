package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.utility.JsonConverter;

// selection by controller of which display is shown in view
public class CVEvent extends Event{
    public enum EventType {
        CHOOSE_TWO_LEADER_CARD,
        ASK_WHICH_COLUMN_ROW,
        ASK_WHICH_DEV_CARD,
        ASK_WHICH_PROD_SLOTS,
        ASK_WHICH_LEADER_CARD_TO_ACTIVATE
    }
    private EventType eventType;

    public CVEvent(EventType eventType, Object object){
        super(object);
        this.jsonContent = JsonConverter.toJson(object);
    }

    public EventType getEventType() {
        return eventType;
    }
}
