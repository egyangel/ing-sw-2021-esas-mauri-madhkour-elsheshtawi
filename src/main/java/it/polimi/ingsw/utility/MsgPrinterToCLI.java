package it.polimi.ingsw.utility;

import it.polimi.ingsw.utility.messages.*;
/**
 * @deprecated
 **/
// this class is intended to be used for DEBUG purposes, not real case user interaction
public class MsgPrinterToCLI {
    public static void printMessage(MsgDirection direction, Message msg){
        System.out.println("\n[" + direction + "]");
        System.out.println("[USER ID:]"+ msg.getUserID() + "[MSG TYPE:]" + msg.getMsgtype() + "[MSG CONTENT IN BRACKETS]");
        System.out.println("[" + msg.getJsonContent() + "]");
    }

    public enum MsgDirection {
        INCOMINGtoSERVER,
        OUTGOINGfromSERVER,
        INCOMINGtoCLIENT,
        OUTGOINGfromCLIENT;
    }
}
