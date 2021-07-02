package it.polimi.ingsw.utility.messages;

import it.polimi.ingsw.model.DevCard;
import it.polimi.ingsw.model.DevSlot;
import it.polimi.ingsw.model.Resources;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
/**
 * class that acts as a form between server and client for the buy development card action
 * Between each step, server and client stores related headers (fields) of the class
 * Server reads related fields and checks, and modifies model accordingly, then fills more headers and sends
 * it back to client if necessary. Client reads this class, shows the information to user, and fill the related fields
 * with user's choices before sending it back
 * */
public class BuyDevCardActionContext {
    public enum ActionStep{
        // from client to server
        COLOR_LEVEL_CHOSEN,
        DEVSLOT_CHOSEN,
        // from server to client
        CHOOSE_COLOR_LEVEL,
        EMPTY_DEVCARD_DECK_ERROR,
        NOT_ENOUGH_RES_FOR_DEVCARD_ERROR,
        UNSUITABLE_FOR_DEVSLOTS_ERROR,
        CHOOSE_DEV_SLOT,
        COST_PAID_DEVCARD_PUT;
    }
    private ActionStep lastStep;
    private int level;
    private DevCard.CardColor color;
    private List<DevCard> ownedCards = new ArrayList<>();
    private DevCard selectedCard;
    private List<DevSlot.slotPlace> suitableSlots = new ArrayList<>();
    private DevSlot.slotPlace selectedSlot;
    private Resources remainingCost;
    private Resources payFromWarehouse;
    private Resources payFromStrongbox;
    private boolean isApplied = false;
    private Resources discountApplied;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public DevCard.CardColor getColor() {
        return color;
    }

    public void setColor(DevCard.CardColor color) {
        this.color = color;
    }

    public ActionStep getLastStep(){
        return lastStep;
    }

    public void setLastStep(ActionStep step){
        lastStep = step;
    }

    public void setSuitableSlots(List<DevSlot.slotPlace> list){
        suitableSlots.addAll(list);
    }

    public List<DevSlot.slotPlace> getSuitableSlots() {
        return suitableSlots;
    }

    public void setSelectedSlot(DevSlot.slotPlace place){
        this.selectedSlot = place;
    }

    public DevSlot.slotPlace getSelectedSlot() {
        return selectedSlot;
    }

    public DevCard getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(DevCard selectedCard) {
        this.selectedCard = selectedCard;
    }

    public Resources getRemainingCost() {
        return remainingCost;
    }

    public void setRemainingCost(Resources costOfCard) {
        this.remainingCost = new Resources();
        this.remainingCost.add(costOfCard);
    }

    public Resources getPayFromWarehouse() {
        return payFromWarehouse;
    }

    public void setPayFromWarehouse(Resources payFromWarehouse) {
        // not sure directly assigning will work for JSON, but it should
        this.payFromWarehouse = payFromWarehouse;
    }

    public Resources getPayFromStrongbox() {
        return payFromStrongbox;
    }

    public void setPayFromStrongbox(Resources payFromStrongbox) {
        this.payFromStrongbox = payFromStrongbox;
    }

    public boolean isError(){
        Set<ActionStep> errorSteps = EnumSet.of(ActionStep.EMPTY_DEVCARD_DECK_ERROR,
                                                ActionStep.NOT_ENOUGH_RES_FOR_DEVCARD_ERROR,
                                                ActionStep.UNSUITABLE_FOR_DEVSLOTS_ERROR);
        if (errorSteps.contains(lastStep)) return true;
        else return false;
    }

    public boolean isDiscountApplied(){
        return isApplied;
    }

    public void setTotalDiscount(Resources res){
        isApplied = true;
        discountApplied = res;
    }

    public Resources getTotalDiscount(){
        return discountApplied;
    }
}


