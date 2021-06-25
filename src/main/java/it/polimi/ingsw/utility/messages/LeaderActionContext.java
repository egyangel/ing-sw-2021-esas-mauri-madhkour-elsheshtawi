package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.model.*;
import java.util.ArrayList;
import java.util.List;


public class LeaderActionContext {

    public enum ActionStep{
        // from client to server
        LEADER_CARD_ACTIVATED_CHOSEN,
        LEADER_CARD_NOT_ACTIVATED_CHOSEN,
        DISCARD_LEADER_CARD,

        // from server to client
        CHOOSE_ACTION,
        END_LEADER_ACTION
    }

    private List<LeaderCard> playerCard = new ArrayList<>();
    private List<Boolean> discardedCard = new ArrayList<>();
    private List<Boolean> leaderToActiveCards = new ArrayList<>();
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

        int j=Cards.size()-1;

        while (j >= 0) {
            if (Cards.get(j).equals(true)) {
                playerCard.remove(j);
            }
            j--;
        }
    }
    public void setActiveLeaderCard(List<Boolean> Cards){

        int j=0;

        while (j < Cards.size()) {
            if (Cards.get(j).equals(true)) {
                activeLeaderCard.add( playerCard.get(j));
            }
            j++;
        }
        //activeLeaderCard.addAll(Cards);
    }
    public List<LeaderCard> getActiveLeaderCard(){ return this.activeLeaderCard; }

    public void setLeadersToActivate(List<Boolean> leaderToActiveCards){
        this.leaderToActiveCards = leaderToActiveCards;
    }
    public List<Boolean> getLeadersToActivate(){ return this.leaderToActiveCards; }

    public List<DevCard> getOwnedCards() {
        return ownedCards;
    }


}