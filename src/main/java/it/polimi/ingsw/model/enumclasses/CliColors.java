package it.polimi.ingsw.model.enumclasses;

//this is an enum of a color palette with ANSI code of all the game objects for the cli display

public enum CliColors {  //we can choose what to pick for game objects. i suggest  picking BG for card colors
    //                      and resources only text
    black("\u001b[30m"),
    red("\u001b[31m"), // for  sout("\u001b[31m ✞"); now becoming sout("red ✞"); for faith symbol
    green("\u001b[32m"), // for DevCardColor
    gold("\u001b[33m"), //for gold coin ⨎ (fiorino) : sout(\u001b[33m ⨎ \u001b[0m");
    blue("\u001b[34m"), //for DevCardColor
    purple("\u001b[35m"), //for DevCardColor
    cyan("\u001b[36m"),//for shield
    white("\u001b[37m"),
    grey("\u001b[30;1m"),  //for stone ⌬
    redBright("\u001b[31;1m"),//for faith
    greenBright("\u001b[32;1m"),
    yellow("\u001b[33;1m"),   //for DevCardColor
    blueBright ("\u001b[34;1m"),
    purpleBright("\u001b[35;1m"),
    cyanBright("\u001b[36;1m"),
    whiteBright("\u001b[37;1m"),

    //Background colors
    blackBG("\u001b[40m"),
    redBG("\u001b[41m"),//
    greenBG("\u001b[42m"),  //for DevCardColor
    goldBG("\u001b[43m"),
    blueBG("\u001b[44m"), //for DevCardColor
    purpleBG("\u001b[45m"), //for DevCardColor
    cyanBG("\u001b[46m"),
    whiteBG("\u001b[47m"),
    greyBG("\u001b[40;1m"),
    redBrightBG("\u001b[41;1m"),
    greenBrightBG("\u001b[42;1m"),
    yellowBG("\u001b[43;1m"),   //for DevCardColor
    blueBrightBG("\u001b[44;1m"),
    purpleBrightBG("\u001b[45;1m"),
    cyanBrightBG("\u001b[46;1m"),
    whiteBrightBG("\u001b[47;1m");


    /*this ansi code is very important.
     Every time a custom color is set it remains permanently
     until this ansi is used and it returns to a default color
                                          (↓)                   */
    static final String colorReset = "\u001B[0m";

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
