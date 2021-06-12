package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Requirement {
    public enum reqType{
        TWOCARD, RESOURCES, THREECARD, LEVELTWOCARD
    }

    private reqType reqtype;
    private List<DevCard.CardColor> colorList = new ArrayList<>();
    private Resources resources;

    public Requirement(reqType reqtype, DevCard.CardColor colorOne, DevCard.CardColor colorTwo){
        this.reqtype = reqtype;
        this.colorList.add(colorOne);
        this.colorList.add(colorTwo);
    }

    public Requirement(DevCard.CardColor color){
        this.reqtype = reqType.LEVELTWOCARD;
        this.colorList.add(color);
    }

    public Requirement(Resources resources){
        this.reqtype = reqType.RESOURCES;
        this.resources = resources;
    }

    public reqType getType(){
        return reqtype;
    }

    public DevCard.CardColor getColor(int index){
        return colorList.get(index);
    }


    public Resources getResource(){
        return resources;
    }

    @Override
    public String toString() {
        return "Requirement{" +
                "reqtype=" + reqtype +
                ", colorList=" + colorList +
                ", resources=" + resources +
                '}';
    }
}