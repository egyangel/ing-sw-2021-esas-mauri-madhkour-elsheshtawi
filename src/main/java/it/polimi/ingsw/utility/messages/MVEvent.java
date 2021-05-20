package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.utility.JsonConverter;

// events after update of model in server
public class MVEvent extends Event{
    public enum EventType {
        SWAPPED_SHELVES,
        DISCARDED_FROM_SHELF
    }
    private EventType eventType;

    public MVEvent(EventType eventType, Object object){
        super(object);
        this.jsonContent = JsonConverter.toJson(object);
    }

    public EventType getEventType() {
        return eventType;
    }
}
