package it.polimi.ingsw.server.model;

import it.polimi.ingsw.server.model.enumclasses.ResType;

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

    public Resources(ResType resource, int number) {
        this.values = new HashMap<>();
        values.put(resource,number);
    }

    //    Over-loading constructor
    public Resources(int stone, int shield, int servant, int coin, int faith){
        this.values = new HashMap<>();
        this.values.put(ResType.STONE, stone);
        this.values.put(ResType.SHIELD, shield);
        this.values.put(ResType.SERVANT, servant);
        this.values.put(ResType.COIN, coin);
        this.values.put(ResType.FAITH, faith);
    }

    public void add(ResType type,Integer val){
        this.values.put(type, this.getNumberOfType(type) + val);
    }

    public void subtract(ResType type,Integer val){
        if (this.getNumberOfType(type) >= val){
            this.values.put(type, this.getNumberOfType(type) - val);
        }
    }

    public int sumOfValues(){
        int sum = 0;
        for (int i: values.values()){
            sum += i;
        }
        return sum;
    }

    public int getNumberOfType(ResType type){
        return values.getOrDefault(type, 0);
    }

    public boolean isThereType(ResType type){
        return values.containsKey(type);
    }

    public void clear(){
        this.values.clear();
    }

    @Override
    public String toString() {
        return values.toString();
    }
}