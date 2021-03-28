package it.polimi.ingsw.model.enumclasses;



public enum MarbleColor {
    WHITE,BLUE,GREY,YELLOW,PURPLE,RED;
  //  private EnumMap<MarbleColor,ResType> type;


    public String getColor() {
        return (this.name());
    }

    public ResType ResourceType(){


        if(this.equals(BLUE))
            return ResType.SHIELD;

        if(this.equals(GREY))
            return ResType.STONE;

        if(this.equals(YELLOW))
            return ResType.COIN;

        if(this.equals(PURPLE))
            return ResType.SERVANT;

        if(this.equals(RED))
            return ResType.FAITH;

        return ResType.NOTHING;
    }
/*
    private void set() {
        type.put(WHITE,ResType.NOTHING);
        type.put(BLUE,ResType.SHIELD);
        type.put(GREY,ResType.STONE);
        type.put(YELLOW,ResType.COIN);
        type.put(PURPLE,ResType.SERVANT);
        type.put(RED,ResType.FAITH);
    }
   */
}
