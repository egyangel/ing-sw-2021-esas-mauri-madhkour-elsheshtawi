package it.polimi.ingsw.server.networkmesseges;

import java.io.Serializable;
import java.util.UUID;

public abstract class BaseMessage implements Serializable {
    UUID id = UUID.randomUUID();

    /**
     * unique id for the commands over network
     * allow match the recived commands and the response
     * @return id
     */
    public UUID getCommandId(){
        return id;
    }
}
