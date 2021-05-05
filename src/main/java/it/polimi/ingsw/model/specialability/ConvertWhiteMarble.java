package it.polimi.ingsw.model.specialability;

import it.polimi.ingsw.model.enumclasses.AbilityType;

public class ConvertWhiteMarble extends SpecialAbility {
    public ConvertWhiteMarble(SpecialAbility ability) {
        effect = ability.getEffect();
        material = ability.getMaterial();
        number = ability.getNumber();

    }
    @Override
    public void activate() {

    }

    @Override
    public AbilityType  getEffect() {
        return AbilityType.CONVERTWHITE;
    }
}
