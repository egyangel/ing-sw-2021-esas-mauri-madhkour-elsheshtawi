package it.polimi.ingsw.utility.messages;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
    private UUID userID;
    private MsgType msgtype;
    private String jsonContent;


}
