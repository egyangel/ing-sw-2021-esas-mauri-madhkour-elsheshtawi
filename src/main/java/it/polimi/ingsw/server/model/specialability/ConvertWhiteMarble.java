package it.polimi.ingsw.server.model.specialability;

import it.polimi.ingsw.server.model.enumclasses.AbilityType;
import it.polimi.ingsw.server.model.enumclasses.ResType;

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
