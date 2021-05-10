package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.utility.JsonConverter;

import java.io.Serializable;

public class Message implements Serializable {
    public enum Type {
        LOGIN,
        HEARTBEAT,
        TAKE_RES_ACTION,
        BUY_DEV_CARD_ACTION,
        MV_EVENT,
        VC_EVENT,
        DEBUG
    }

    private Integer userID;
    private Type msgtype;
    private String jsonContent;

    public Message(Integer userID, Type msgtype, String simpleString) {
        this.userID = userID;
        this.msgtype = msgtype;
        this.jsonContent = simpleString;
    }

    public Message(Integer userID, Type msgtype, Event event){
        this.userID = userID;
        this.msgtype = msgtype;
        this.jsonContent = JsonConverter.toJson(event);
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID){
        this.userID = userID;
    }

    public Type getMsgtype() {
        return msgtype;
    }

    public String getJsonContent() {
        return jsonContent;
    }
}
