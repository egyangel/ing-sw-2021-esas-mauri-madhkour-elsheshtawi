package it.polimi.ingsw.model.enumclasses;

public enum DevCardColor {
    GREEN,BLUE,YELLOW,PURPLE;

    private static final DevCardColor[] colors = DevCardColor.values();

    public static DevCardColor getDevCardColor(int i){
        return DevCardColor.colors[i];
    }
}
