package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.utility.JsonConverter;

// events after interactions of user
public class VCEvent extends Event{
    public enum EventType {
        TAKE_RES_ACTION_SELECTED,
        BUY_DEVCARD_ACTION_SELECTED,
        ACTIVATE_PROD_ACTION_SELECTED,
        ACTIVATE_LEADER_CONTEXT_SELECTED,

        TAKE_RES_CONTEXT_FILLED,
        BUY_DEVCARD_CONTEXT_FILLED,
        ACTIVATE_PROD_CONTEXT_FILLED,
        ACTIVATE_LEADER_CONTEXT_FILLED,

        TAKE_RES_ACTION_ENDED,
        BUY_DEVCARD_ACTION_ENDED,
        ACTIVATE_PROD_ACTION_ENDED,
        ACTIVATE_LEADER_ACTION_ENDED,

        LEADER_CARDS_CHOOSEN,
        INIT_RES_CHOOSEN,
        LEVEL_COLOR_DEVCARD_CHOOSEN,

        END_TURN
    }
    private EventType eventType;

    public VCEvent(EventType eventType) {
        this.eventType = eventType;
    }

    public VCEvent(EventType eventType, Object object){
        super(object);
        this.eventType = eventType;
        this.jsonContent = JsonConverter.toJson(object);
    }

    public EventType getEventType() {
        return eventType;
    }
}
