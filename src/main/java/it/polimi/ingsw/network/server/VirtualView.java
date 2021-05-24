package it.polimi.ingsw.network.server;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.LeaderCard;
import it.polimi.ingsw.network.client.ServerHandler;
import it.polimi.ingsw.utility.InputConsumer;
import it.polimi.ingsw.utility.JsonConverter;
import it.polimi.ingsw.utility.messages.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VirtualView implements Publisher<VCEvent>, Listener<Event> {
    // project can be implemented without PUBLISH - LISTEN but the tutors might want to see it implemented
    private Integer userID;
    private List<Listener<VCEvent>> listenerList = new ArrayList<>();
    private ClientHandler clientHandler;

    public VirtualView(Integer userID, ClientHandler clientHandler) {
        this.userID = userID;
        this.clientHandler = clientHandler;

    }

    public void handleGameMessage(Message msg){
        if (msg.getMsgtype() == Message.MsgType.VC_EVENT) {
            VCEvent vcEvent = (VCEvent) JsonConverter.fromMsgToObject(msg, VCEvent.class);
            vcEvent.setUserID(userID);
            publish(vcEvent);
        } else {
            System.out.println("Bad message in virtual view");
        }
    }

    @Override
    public void update(Event event) {
        Message msg = null;
        if (event instanceof CVEvent) {
            msg = new Message(Message.MsgType.CV_EVENT, JsonConverter.toJson(event));
        } else if  (event instanceof MVEvent) {
            msg = new Message(Message.MsgType.MV_EVENT, JsonConverter.toJson(event));
        } else
            System.out.println("Unidentified CV or MV event has been tried to sent to client inside server");
        if (msg != null) {
            clientHandler.sendMessage(msg);
        }
    }

    @Override
    public void subscribe(Listener<VCEvent> listener) {
        listenerList.add(listener);
    }

    @Override
    public void unsubscribe(Listener<VCEvent> listener) {
        listenerList.remove(listener);
    }

    @Override
    public void publish(VCEvent vcEvent) {
        for(Listener<VCEvent> listener : listenerList){
            listener.update(vcEvent);
        }
    }
}
