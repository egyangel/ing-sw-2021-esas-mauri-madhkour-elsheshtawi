package it.polimi.ingsw.server.model.specialability;

import it.polimi.ingsw.server.model.enumclasses.AbilityType;
import it.polimi.ingsw.server.model.enumclasses.ResType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/*
@interface JsonSubtype {
    Class<?> clazz();

    String name();
}

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)

@interface JsonType {
    String property();

    JsonSubtype[] subtypes();
}
@JsonType(
        property = "type",
        subtypes = {
                @JsonSubtype(clazz = Discount.class, name = "DISCOUNT"),
                @JsonSubtype(clazz = ConvertWhiteMarble.class, name = "CONVERTWHITE"),
                @JsonSubtype(clazz = ExstraSlot.class, name = "EXSTRASLOT"),
                @JsonSubtype(clazz = AdditionalProduction.class, name = "ADDPROD")
        }
)*/
public abstract class SpecialAbility {
    protected AbilityType effect;
    protected ResType material;
    protected int number;

    public void activate() {

    }

    public AbilityType getEffect() {
        return effect;
    }
    public ResType getMaterial() { return material; }
    public Integer getNumber() {
        return number;
    }
}
