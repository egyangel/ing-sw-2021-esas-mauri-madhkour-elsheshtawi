package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;
/**
 * Class that represent the requirement of the leader cards
 * @author
 * */
public class Requirement {
    /**
     * enum that represent the type of requirement
     *   TWOCARD: for discount leader card
     *   RESOURCES: for extra leader card
     *   THREECARD: for white converter leader card
     *   LEVELTWOCARD: for additional production leader card
     * */
    public enum reqType{
        TWOCARD, RESOURCES, THREECARD, LEVELTWOCARD
    }

    private reqType reqtype;
    private List<DevCard.CardColor> colorList = new ArrayList<>();
    private Resources resources;
    /**
     * Constructor -> initialize the leader card with ability discount and  white converter
     * @param reqtype  type of requirement
     * @param colorOne the color of left req
     * @param colorTwo the color of right req
     * */
    public Requirement(reqType reqtype, DevCard.CardColor colorOne, DevCard.CardColor colorTwo){
        this.reqtype = reqtype;
        this.colorList.add(colorOne);
        this.colorList.add(colorTwo);
    }
    /**
     * Constructor -> initialize the leader card with ability additional production
     * @param color  color of the req

     * */
    public Requirement(DevCard.CardColor color){
        this.reqtype = reqType.LEVELTWOCARD;
        this.colorList.add(color);
    }
    /**
     * Constructor -> initialize the leader card with ability exstra slot
     * @param resources the type and number of resources

     * */
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

    public String describeRequirement(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Reqs: ");
        switch (reqtype){
            case TWOCARD:
                DevCard.CardColor firstColor = colorList.get(0);
                DevCard.CardColor secondColor = colorList.get(1);
                stringBuilder.append(firstColor.getAnsiCode() + "[" + "\u25a1" + "]" + DevCard.CardColor.RESET);
                stringBuilder.append(secondColor.getAnsiCode() + "[" + "\u25a1" + "]" + DevCard.CardColor.RESET);
                break;
            case RESOURCES:
                stringBuilder.append(resources.describeResource());
                break;
            case THREECARD:
                DevCard.CardColor colorOfTwoCard = colorList.get(0);
                DevCard.CardColor colorOfOneCard = colorList.get(1);
                stringBuilder.append(colorOfTwoCard.getAnsiCode() + "[" + "\u25a1" + "]" + DevCard.CardColor.RESET);
                stringBuilder.append(colorOfTwoCard.getAnsiCode() + "[" + "\u25a1" + "]" + DevCard.CardColor.RESET);
                stringBuilder.append(colorOfOneCard.getAnsiCode() + "[" + "\u25a1" + "]" + DevCard.CardColor.RESET);
                break;
            case LEVELTWOCARD:
                DevCard.CardColor colorOfOnlyCard = colorList.get(0);
                stringBuilder.append(colorOfOnlyCard.getAnsiCode() + "[" + "\u2681" + "]" + DevCard.CardColor.RESET);
                break;
        }
        return stringBuilder.toString();
    }
}