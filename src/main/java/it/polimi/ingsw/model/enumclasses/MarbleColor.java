package it.polimi.ingsw.model.enumclasses;

import it.polimi.ingsw.model.Resources;

public enum MarbleColor {
    WHITE, BLUE, GREY, YELLOW, PURPLE, RED;

    public String getColor() {
        return (this.name());
    }

    public Resources.ResType getResourceType() {

        if (this.equals(BLUE))
            return Resources.ResType.SHIELD;

        if (this.equals(GREY))
            return Resources.ResType.STONE;

        if (this.equals(YELLOW))
            return Resources.ResType.COIN;

        if (this.equals(PURPLE))
            return Resources.ResType.SERVANT;

        if (this.equals(RED))
            return Resources.ResType.FAITH;

        return null;
    }
}