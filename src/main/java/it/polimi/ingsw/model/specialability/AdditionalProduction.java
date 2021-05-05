package it.polimi.ingsw.model.specialability;

import it.polimi.ingsw.model.enumclasses.AbilityType;

public class AdditionalProduction extends SpecialAbility{


    public AdditionalProduction(SpecialAbility ability) {
        effect = ability.getEffect();
        material = ability.getMaterial();
        number = ability.getNumber();

    }

    @Override
    public void activate() {

    }

    @Override
    public AbilityType  getEffect() {
        return AbilityType.ADDPROD;
    }
}
