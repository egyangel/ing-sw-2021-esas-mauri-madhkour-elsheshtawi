package it.polimi.ingsw.model.enumclasses;

//this is an enum of a color palette with ANSI code of all the game objects for the cli display

public enum CliColors {
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m");
    /*this ansi code is very important.
     Every time a custom color is set it remains permanently
     until this ansi is used and it returns to a default color
                                          (↓)                   */
    static final String ColorReset = "\u001B[0m";
    private String escape;


    CliColors(String escape)
    {
        this.escape = escape;
    }


    public String getEscape()
    {
        return escape;
    }


    @Override
    public String toString()
    {
        return escape;
    }


}
