package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.utility.JsonConverter;

import java.io.Serializable;
import java.lang.reflect.Type;

// 4 kinds of messages in total
public class Message implements Serializable {
    public enum MsgType {
        DISPLAY_CREATE_GAME,
        DISPLAY_LOGIN,
        REQUEST_FIRST_LOGIN,
        REQUEST_LOGIN,
        FIRST_LOGIN_ACCEPTED,
        LOGIN_ACCEPTED, //done until here
        START_MATCH,

        HEARTBEAT,
        MV_EVENT, // if something changes, new personal board will be sent to view of player
        CV_EVENT, // from controller to view (alert warehouse full message)
        VC_EVENT, // from view to controller
        DEBUG
    }

    private Integer userID;
    private MsgType msgtype;
    private String jsonContent; //jsonized MV_Event or CV_EVENT or VC_EVENT

    public Message(MsgType msgtype){
        this.msgtype = msgtype;
    }

    public Message(MsgType msgtype, Object object){
        this.msgtype = msgtype;
        this.jsonContent = JsonConverter.toJson(object);
    }

    public Message(MsgType msgtype, String string) {
        this.msgtype = msgtype;
        this.jsonContent = string;
    }

    public Message(MsgType msgtype, Integer integer){
        this.msgtype = msgtype;
        this.jsonContent = integer.toString();
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID){
        this.userID = userID;
    }

    public MsgType getMsgtype() {
        return msgtype;
    }

    public String getJsonContent() {
        return jsonContent;
    }

    // use these two
    public Object getObject(Class clazz){
        return JsonConverter.fromMsgToObject(this, clazz);
    }

    public Object getObject(Type type){
        return JsonConverter.fromMsgToObject(this, type);
    }
}
