package it.polimi.ingsw.utility.messages;

public interface Listener<E> {
    void update(E event);
}
