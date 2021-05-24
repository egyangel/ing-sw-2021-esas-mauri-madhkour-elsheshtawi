package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.utility.JsonConverter;

// selection by controller of which display is shown in view
public class CVEvent extends Event{
    public enum EventType {
        CHOOSE_TWO_LEADER_CARD,
        ASSIGN_TURN_ORDER,
        BEGIN_TURN,
        MARBLELIST_SENT,
        PUT_RESOURCES_TAKEN,
        INVALID_EDIT,
        EMPTY_DEVCARD_DECK,
        NOT_ENOUGH_RES_FOR_DEVCARD,
        UNSUITABLE_DEVCARD,
        SUITABLE_DEVCARD
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
