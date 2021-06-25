package it.polimi.ingsw.model;
/**
 * Class that represent the ability of the leader cards
 * @author
 * */

public class SpecialAbility {


    public enum AbilityType {
        DISCOUNT,ADDPROD,EXTRASLOT,CONVERTWHITE
    }

    private AbilityType abilityType;
    private Resources.ResType oneResType;
    private Resources resourceHolder = new Resources();
    /**
     * Constructor -> initialize the Res type of the card and the ability
     * @param abilityType the ability of the card
     * @param resType the type of resources of the card
     * */
    public SpecialAbility(AbilityType abilityType, Resources.ResType resType){
        this.abilityType = abilityType;
        this.oneResType = resType;
    }

    public AbilityType getAbilityType() {
        return abilityType;
    }

    public Resources.ResType getResType(){
        return oneResType;
    }
    /**
     * method that keep the resources in the leader card with extra slot ability
     * @param resourcesToBeAdded the res that has to be added on the card
     * */
    public void addToHolder(Resources resourcesToBeAdded){
        if (resourcesToBeAdded.isThisOneType() && oneResType == resourcesToBeAdded.getOnlyType()){
            int numberToAdd = resourcesToBeAdded.sumOfValues();
            if (hasEnoughSpace(numberToAdd)){
                resourceHolder.add(resourcesToBeAdded);
            }
        }
    }

    public Resources getResourcesAtSlot(){
        return resourceHolder;
    }
    /**
     * method that remove res from extra slot leader card during activation or buying action
     * @param res the res has to be subtracted
     * */
    public void subtractFromExtraSlot(Resources res){
        resourceHolder.subtract(res);
    }
    /**
     * method that check if there is space in the leader card with extra slot power
     * @param toBeAdded the res that has to be added on the card
     * */
    private boolean hasEnoughSpace(int toBeAdded){
        return ((resourceHolder.sumOfValues() + toBeAdded) <= 2);
    }

    @Override
    public String toString() {
        return "SpecialAbility{" +
                "abilityType=" + abilityType +
                ", oneResType=" + oneResType +
                ", resourceHolder=" + resourceHolder +
                '}';
    }

    public String describeSpecialAbility(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Ability: ");
        switch (abilityType){
            case ADDPROD:
                stringBuilder.append(oneResType.getFirstAnsiPart() + "1 " + oneResType.getSecondAnsiPart());
                stringBuilder.append(" —> [1 ?] + ");
                stringBuilder.append(Resources.ResType.FAITH.getFirstAnsiPart() + "1 " + Resources.ResType.FAITH.getSecondAnsiPart());
                break;
            case DISCOUNT:
                stringBuilder.append(oneResType.getFirstAnsiPart() + "-1 " + oneResType.getSecondAnsiPart());
                stringBuilder.append(" for dev card costs");
                break;
            case EXTRASLOT:
                stringBuilder.append("extra slot for " + oneResType.getFirstAnsiPart() + "2 " + oneResType.getSecondAnsiPart());
                break;
            case CONVERTWHITE:
                stringBuilder.append("\u26aa" + " = " + oneResType.getFirstAnsiPart() + "1 " + oneResType.getSecondAnsiPart());
                break;
        }
        return stringBuilder.toString();
    }
}
