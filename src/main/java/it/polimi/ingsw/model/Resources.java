package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumclasses.ResType;

import java.util.HashMap;
import java.util.Map;

public class Resources {
    private Map<ResType, Integer> values;

    public Resources() {
        this.values = new HashMap<>();
        this.values.put(ResType.COIN, 0);
        this.values.put(ResType.SERVANT, 0);
        this.values.put(ResType.SHIELD, 0);
        this.values.put(ResType.STONE, 0);
        this.values.put(ResType.FAITH, 0);
    }

    public void add(ResType res,Integer val){
        this.values.put(res, this.values.get(res) + val);
    }

    public void subtract(ResType res,Integer val){
        if (this.values.get(res) > val){
            this.values.put(res, this.values.get(res) - val);
        }
    }
}
