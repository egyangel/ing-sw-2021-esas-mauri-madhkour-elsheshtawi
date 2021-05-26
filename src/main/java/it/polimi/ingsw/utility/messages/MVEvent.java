package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.utility.JsonConverter;

// events after update of model in server
public class MVEvent extends Event{
    public enum EventType {
        MARKET_TRAY_UPDATE,
        DEVCARD_MATRIX_UPDATE,
        WAREHOUSE_UPDATE,
        STRONGBOX_UPDATE,
        DEVSLOTS_UPDATE,
        FAITHPOINT_UPDATE
    }
    private EventType eventType;

    public MVEvent(Integer userIDofUpdatedBoard, EventType eventType, String updatedDescriptionOfObject) {
        this.userID = userIDofUpdatedBoard;
        this.eventType = eventType;
        this.jsonContent = updatedDescriptionOfObject;
    }

    public MVEvent(EventType eventType, Object object){
        super(object);
        this.jsonContent = JsonConverter.toJson(object);
    }

    public EventType getEventType() {
        return eventType;
    }
}
