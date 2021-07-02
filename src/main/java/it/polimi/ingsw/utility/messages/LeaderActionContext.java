package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.model.*;
import java.util.ArrayList;
import java.util.List;

/**
 * class that represent the leader action context. It the object that is sent between view and controller.
 * The player fill the context with the action
 * related to leader card action and send it to the controller that check and activate/discard leader card
 *
 * */
public class LeaderActionContext {
    private List<LeaderCard> inactiveCards = new ArrayList<>();
    private List<LeaderCard> activeCards = new ArrayList<>();
    private List<Integer> selectedInactiveCardIndexes = new ArrayList<>();
    private List<Integer> selectedActiveCardIndexes = new ArrayList<>();

    public List<LeaderCard> getInactiveCards() {
        return inactiveCards;
    }

    public void setInactiveCards(List<LeaderCard> inactiveCards) {
        this.inactiveCards = inactiveCards;
    }

    public List<LeaderCard> getActiveCards() {
        return activeCards;
    }

    public void setActiveCards(List<LeaderCard> activeCards) {
        this.activeCards = activeCards;
    }

    public List<Integer> getSelectedInactiveCardIndexes() {
        return selectedInactiveCardIndexes;
    }

    public void setSelectedInactiveCardIndexes(List<Integer> selectedInactiveCardIndexes) {
        this.selectedInactiveCardIndexes = selectedInactiveCardIndexes;
    }

    public List<Integer> getSelectedActiveCardIndexes() {
        return selectedActiveCardIndexes;
    }

    public void setSelectedActiveCardIndexes(List<Integer> selectedActiveCardIndexes) {
        this.selectedActiveCardIndexes = selectedActiveCardIndexes;
    }
}