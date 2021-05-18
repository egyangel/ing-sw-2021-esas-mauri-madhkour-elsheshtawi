package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.utility.JsonConverter;

// events after interactions of user
public class VCEvent extends Event{
    public enum eventType {
        LEADER_CARDS_CHOOSEN,
        INIT_RES_CHOOSEN,
        TAKE_RES_ACTION,
        BUY_DEV_CARD_ACTION,
        ACTIVATE_PROD_ACTION,
        ARRANGE_WAREHOUSE,
        ACTIVATE_LEADERCARD
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
