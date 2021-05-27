package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumclasses.SoloActionTokenType;

public class SoloActionToken {

    private SoloActionTokenType type;
    private int value;

    public SoloActionTokenType getType() {
        return type;
    }

    public void setType(SoloActionTokenType type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
