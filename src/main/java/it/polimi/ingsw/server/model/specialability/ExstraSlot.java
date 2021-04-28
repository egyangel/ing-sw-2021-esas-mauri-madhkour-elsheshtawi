package it.polimi.ingsw.model.specialability;

import it.polimi.ingsw.model.enumclasses.AbilityType;
import it.polimi.ingsw.model.enumclasses.ResType;

public class ExstraSlot extends SpecialAbility {


    public ExstraSlot(SpecialAbility ability) {
        effect = ability.getEffect();
        material = ability.getMaterial();
        number = ability.getNumber();

    }

    @Override
    public void activate() {

    }

    @Override
    public AbilityType  getEffect() {
        return AbilityType.EXSTRASLOT;
    }
}
