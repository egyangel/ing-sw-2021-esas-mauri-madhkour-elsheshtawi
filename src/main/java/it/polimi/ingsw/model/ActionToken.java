package it.polimi.ingsw.model;

public class ActionToken {
    public enum SoloActionTokenType {
        DISCARD_DEV_CARD, MOVE_CROSS_TOKEN_TWO, MOVE_CROSS_TOKEN_ONE_SHELF

    }
    private String name;
    private DevCard.CardColor color;
    private ActionToken.SoloActionTokenType Type;

    public ActionToken(String name, DevCard.CardColor color, ActionToken.SoloActionTokenType type) {
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

    public ActionToken.SoloActionTokenType getType() {
        return Type;
    }

    public void setType(ActionToken.SoloActionTokenType type) {
        Type = type;
    }
}
