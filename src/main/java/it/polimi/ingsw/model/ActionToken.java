package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumclasses.SoloActionTokenType;

public class ActionToken {
    private String name;
    private String value;
    private SoloActionTokenType Type;

    public ActionToken(String name, String value, SoloActionTokenType type) {
        this.name = name;
        this.value = value;
        Type = type;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SoloActionTokenType getType() {
        return Type;
    }

    public void setType(SoloActionTokenType type) {
        Type = type;
    }
}
