package it.polimi.ingsw.model.specialability;

import it.polimi.ingsw.model.enumclasses.AbilityType;

public class Discount extends SpecialAbility {
    public Discount(SpecialAbility ability) {
        effect = ability.getEffect();
        material = ability.getMaterial();
        number = ability.getNumber();

    }

    @Override
    public void activate() {

    }
    public AbilityType  getEffect() {
        return AbilityType.DISCOUNT;
    }
}


