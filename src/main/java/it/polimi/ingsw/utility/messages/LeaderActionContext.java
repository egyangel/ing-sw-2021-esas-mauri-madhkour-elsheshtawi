package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.model.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LeaderActionContext {

    public enum ActionStep{
        // from client to server
        LEADER_CARD_ACTIVATED_CHOSEN,
        LEADER_CARD_NOT_ACTIVATED_CHOSEN,
        DISCARD_LEADER_CARD,
        BOTH_ACTIONS,

        // from server to client
        CHOOSE_ACTION,
        END_LEADER_ACTION
    }

    private List<LeaderCard> playerCard = new ArrayList<>();
    private List<Boolean> discardedCard = new ArrayList<>();
    private List<LeaderCard> leaderToActiveCards = new ArrayList<>();
    private List<LeaderCard> activeLeaderCard = new ArrayList<>();
    private ActionStep lastStep;
    private List<DevCard> ownedCards= new ArrayList<>();

    public void setLastStep(ActionStep step){
        lastStep = step;
    }
    public ActionStep getLastStep(){
        return lastStep;
    }

    public void setPlayerCard(List<LeaderCard> playerCard){
        this.playerCard = playerCard;
    }
    public List<LeaderCard> getPlayerCard(){  return this.playerCard; }

    public void setDiscardedPlayerCard(List<Boolean> discardedCard){
        this.discardedCard = discardedCard;
    }
    public List<Boolean> getDiscardedPlayerCard(){ return (this.discardedCard); }

    public void changePlayerCard(List<Boolean> Cards) {
        int j = 0;
        int countDiscard = 0;
        while (j < Cards.size()) {
            if (Cards.get(j).equals(true))
                countDiscard++;


            if (countDiscard == 2)
                playerCard.clear();
            else
                playerCard.remove(j);
            j++;
        }
    }
    public void setActiveLeaderCard(List<LeaderCard> Cards){

            playerCard.addAll(Cards);
    }
    public List<LeaderCard> getActiveLeaderCard(){ return this.activeLeaderCard; }

    public void setLeadersToActivate(List<LeaderCard> leaderToActiveCards){
        this.leaderToActiveCards = leaderToActiveCards;
    }
    public List<LeaderCard> getLeadersToActivate(){ return this.leaderToActiveCards; }

    public List<DevCard> getOwnedCards() {
        return ownedCards;
    }


}