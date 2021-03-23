package it.polimi.ingsw.model;

import it.polimi.ingsw.model.EnumClasses.ResType;

import java.util.Map;


public class Resources {
    private Map<ResType, Integer> values;


    public Resources() {

    }
    public void add(ResType res,Integer val){
        values.putIfAbsent(res,val);
    }
    public void subtract(ResType res,Integer val){
        //int tempValue=0;
       // tempValue=(int)values.get(res).longValue()- val;


    }
}
