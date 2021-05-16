package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.utility.JsonConverter;

// events after update of model in server
public class MVEvent extends Event{
    public enum eventType {
        PERSONAL_BOARD_UPDATED
    }
    private eventType eventType;

    public MVEvent(eventType eventType, Object object){
        super(object);
        this.jsonContent = JsonConverter.toJson(object);
    }

    public eventType getEventType() {
        return eventType;
    }
}
