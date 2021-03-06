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
        FAITHPOINT_UPDATE,
        FAITHTRACK_UPDATE,
        ACTIVE_LEADER_CARD_UPDATE,
        INACTIVE_LEADER_CARD_UPDATE,
        BLACKCROSS_FAITHPOINT_UPDATE;
    }
    private EventType eventType;

    public MVEvent(Integer userIDofUpdatedBoard, EventType eventType, String updatedDescriptionOfObject) {
        this.userID = userIDofUpdatedBoard;
        this.eventType = eventType;
        this.jsonContent = updatedDescriptionOfObject;
    }

    public MVEvent(EventType eventType, Object object){
        super(object);
        this.eventType = eventType;
    }

    public MVEvent(Integer userIDofUpdatedBoard, EventType eventType, Object object){
        super(object);
        this.userID = userIDofUpdatedBoard;
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }
}
