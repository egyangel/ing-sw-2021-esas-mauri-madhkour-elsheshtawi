package it.polimi.ingsw.utility.messages;

import java.io.Serializable;
import java.util.UUID;

public abstract class BaseMessage implements Serializable {
    UUID id = UUID.randomUUID();

    /**
     * unique id for the messages over network
     * allow match the received messages and the response
     * @return id
     */
    public UUID getMessageId(){
        return id;
    }
}
