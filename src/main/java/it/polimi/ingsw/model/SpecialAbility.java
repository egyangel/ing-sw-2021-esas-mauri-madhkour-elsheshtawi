package it.polimi.ingsw.model;


public class SpecialAbility {
    public enum AbilityType {
        DISCOUNT,ADDPROD,EXSTRASLOT,CONVERTWHITE
    }

    private AbilityType abilityType;
    private Resources.ResType oneResType;
    private Resources resourceHolder = new Resources();

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

    public void addToHolder(Resources resourcesToBeAdded){
        if (resourcesToBeAdded.isThisOneType() && oneResType == resourcesToBeAdded.getOnlyType()){
            int numberToAdd = resourcesToBeAdded.sumOfValues();
            if (hasEnoughSpace(numberToAdd)){
                resourceHolder.add(resourcesToBeAdded);
            }
        }
    }

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
}
