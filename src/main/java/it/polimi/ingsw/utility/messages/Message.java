package it.polimi.ingsw.utility.messages;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
    private UUID userID;
    private MsgType msgtype;
    private String jsonContent;

    public Message(UUID userID, MsgType msgtype, String jsonContent) {
        this.userID = userID;
        this.msgtype = msgtype;
        this.jsonContent = jsonContent;
    }

    public void setUserID(UUID userID){
        this.userID = userID;
    }
}
