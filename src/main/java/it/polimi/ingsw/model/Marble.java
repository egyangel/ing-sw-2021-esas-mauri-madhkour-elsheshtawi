package it.polimi.ingsw.model;

import it.polimi.ingsw.model.EnumClasses.Direction;
import it.polimi.ingsw.model.EnumClasses.MarbleColor;

public class Marble {
    private MarbleColor color;

    public Marble(MarbleColor color){
        this.color=color;

    }
    public String getColor(){

        return color.name();
    }
    public void move(Direction MarbleDirection){

    }

}
