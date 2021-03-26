package it.polimi.ingsw.model.enumclasses;

public enum MarbleColor {
    WHITE,BLUE,GREY,YELLOW,PURPLE,RED;

    public String getColor() {
        return (this.name());
    }
}
