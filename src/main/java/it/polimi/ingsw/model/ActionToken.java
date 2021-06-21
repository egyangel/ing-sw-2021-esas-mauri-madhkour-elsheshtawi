package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumclasses.SoloActionTokenType;

public class ActionToken {
    private String name;
    private DevCard.CardColor color;
    private SoloActionTokenType Type;

    public ActionToken(String name, DevCard.CardColor color, SoloActionTokenType type) {
        this.name = name;
        this.color = color;
        Type = type;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public DevCard.CardColor getColor() {
        return color;
    }

    public void setValue(DevCard.CardColor value) {
        this.color = value;
    }

    public SoloActionTokenType getType() {
        return Type;
    }

    public void setType(SoloActionTokenType type) {
        Type = type;
    }
}
