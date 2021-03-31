package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumclasses.ResType;

import java.util.HashMap;
import java.util.Map;

public class Resources {
    private Map<ResType, Integer> values;

    public Resources() {
        this.values = new HashMap<>();
        for(ResType type: ResType.values()){
            this.values.put(type, 0);
        }
    }

    public void add(ResType res,Integer val){
        this.values.put(res, this.values.get(res) + val);
    }

    public void subtract(ResType res,Integer val){
        if (this.values.get(res) > val){
            this.values.put(res, this.values.get(res) - val);
        }
    }

    public boolean isEmpty(){
        int sum = 0;
        for (int i: values.values()){
            sum += i;
        }
        return sum == 0;
    }

    public int sumOfValues(){
        int sum = 0;
        for (int i: values.values()){
            sum += i;
        }
        return sum;
    }

    public int howManyOfType(ResType type){
        return values.get(type);
    }

    public boolean isThereType(ResType type){
        return values.get(type) >= 1;
    }

    public void resetToZero(){
        for(ResType type: ResType.values()){
            this.values.replace(type, 0);
        }
    }
}
