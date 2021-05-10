package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.utility.messages.Listener;

public interface Publisher<E>{
    void subscribe(Listener<E> listener);

    void unsubscribe(Listener<E> listener);

    void publish(E event);
}

