package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.utility.JsonConverter;

import java.io.Serializable;

// 4 kinds of messages in total
public class Message implements Serializable {
    public enum Type {
        DISPLAY_LOGIN,
        REQUEST_LOGIN,
        LOGIN_ACCEPTED,
        DISPLAY_LOBBY,
        USER_JOINED_IN_LOBBY,
        USER_DROPPED_IN_LOBBY,
        VOTE_START,
        RESET_VOTE,
        START_MATCH,
        ASSIGN_TURN,

        BEGIN_TURN,

        HEARTBEAT,
        MV_EVENT, // if something changes, new personal board will be sent to view of player
        CV_EVENT, // from controller to view (alert warehouse full message)
        VC_EVENT, // from view to controller
        DEBUG
    }

    private Integer userID;
    private Type msgtype;
    private String jsonContent; //jsonized MV_Event or CV_EVENT or VC_EVENT

    public Message(Type msgtype){
        this.msgtype = msgtype;
    }

    public Message(Type msgtype, String jsonContent) {
        this.msgtype = msgtype;
        this.jsonContent = jsonContent;
    }

    public Message(Integer userID, Type msgtype, String simpleString) {
        this.userID = userID;
        this.msgtype = msgtype;
        this.jsonContent = simpleString;
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
