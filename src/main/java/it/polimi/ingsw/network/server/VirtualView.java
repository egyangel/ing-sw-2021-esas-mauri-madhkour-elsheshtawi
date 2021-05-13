package it.polimi.ingsw.network.server;

import it.polimi.ingsw.utility.JsonConverter;
import it.polimi.ingsw.utility.messages.*;

import java.util.ArrayList;
import java.util.List;

public class VirtualView implements Publisher<VCEvent>, Listener<Event> {
    // project can be implemented without PUBLISH - LISTEN but the tutors might want to see it implemented
    private Integer userID;
    private List<Listener<VCEvent>> listenerList = new ArrayList<>();

    public VirtualView(Integer userID) {
        this.userID = userID;
    }

    public void handleGameMessage(Message msg){
        if (msg.getMsgtype() == Message.MsgType.VC_EVENT) {
            VCEvent vcEvent = (VCEvent) JsonConverter.fromMsgToObject(msg, VCEvent.class);
            publish(vcEvent);
        } else {
            System.out.println("Bad message in virtual view");
        }
    }

    @Override
    public void update(Event event) {

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
