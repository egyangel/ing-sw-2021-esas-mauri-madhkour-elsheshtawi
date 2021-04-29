package it.polimi.ingsw.server.model.specialability;

import it.polimi.ingsw.server.model.enumclasses.AbilityType;
import it.polimi.ingsw.server.model.enumclasses.ResType;

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


