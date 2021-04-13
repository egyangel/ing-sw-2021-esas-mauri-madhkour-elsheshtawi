package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumclasses.DevCardColor;
import it.polimi.ingsw.model.enumclasses.ResType;

import java.util.*;

public class Requirements {

    private String type;
    private String req1;
    private Integer number;
    private Integer level;

//return the type of the requirments : Resources or DevCard
    public String getType(){
        return req1;
    }
    //return this requirement
    public String getReq(){
        return req1;
    }
    //return the number needed for this requirment
    public Integer getNumber() {
        return this.number;
    }
    //return the level of the DevCard needed, null if it is a resources
    public Integer getLevel() { return level; }
}