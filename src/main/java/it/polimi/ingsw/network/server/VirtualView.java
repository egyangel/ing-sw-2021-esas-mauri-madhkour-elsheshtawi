package it.polimi.ingsw.network.server;

import it.polimi.ingsw.utility.messages.*;

public class VirtualView implements Publisher<VCEvent>, Listener<Event> {
    private Integer userID;

    public void handleGameMessage(Message msg){
        switch (msg.getMsgtype()) {
            case TAKE_RES_ACTION:

                break;
            case BUY_DEV_CARD_ACTION:

                break;

            default:

                break;
        }
    }

    @Override
    public void update(Event event) {

    }

    @Override
    public void subscribe(Listener<VCEvent> listener) {

    }

    @Override
    public void unsubscribe(Listener<VCEvent> listener) {

    }

    @Override
    public void publish(VCEvent event) {

    }
}
