package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.utility.JsonConverter;

// selection by controller of which display is shown in view
public class CVEvent extends Event{
    public enum EventType {
        TAKE_RES_FILL_CONTEXT,
        BUY_DEVCARD_FILL_CONTEXT,
        ACTIVATE_PROD_FILL_CONTEXT,
        ACTIVATE_LEADER_FILL_CONTEXT,
        ACTIVATE_LEADER_DONE,
        DISCARD_LEADER_DONE,
        DISCARD_LEADER_FILL_CONTEXT,
        SELECT_MINOR_ACTION,
        CHOOSE_TWO_LEADER_CARD,
        ASSIGN_TURN_ORDER,
        SELECT_ALL_ACTION,
        END_GAME_TRIGGERED,
        END_RESULT,
        END_VP_COUNTED
    }
    private EventType eventType;

    public CVEvent(EventType eventType) {
        super();
        this.eventType = eventType;
    }

    public CVEvent(EventType eventType, Object object){
        super(object);
        this.eventType = eventType;
        this.jsonContent = JsonConverter.toJson(object);
    }

    public EventType getEventType() {
        return eventType;
    }
}
