package it.polimi.ingsw.model.enumclasses;

import it.polimi.ingsw.model.Resources;

public enum MarbleColor {
    WHITE("\033[1;37m"),
    BLUE("\u001B[34m"),
    GREY("\u001B[0;30m"),
    YELLOW("\u001B[33m"),
    PURPLE("\u001B[35m"),
    RED("\u001B[31m");

    public String getColor() {
        return (this.name());
    }

    private String ansiCode;
    public static final String RESET = "\u001B[0m";
    public String getAnsiCode() {
        return ansiCode;
    }

    MarbleColor(String ansiCode)
    {
        this.ansiCode = ansiCode;
    }

    public MarbleColor getValue(){
        return this;
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