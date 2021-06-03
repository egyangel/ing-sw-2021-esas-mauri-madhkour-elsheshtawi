package it.polimi.ingsw.model;

import javax.naming.spi.ResolveResult;
import java.util.*;

public class Resources {
    public enum ResType {
        STONE,SHIELD,SERVANT,COIN,FAITH;

        public static boolean contains(String string) {
            for (Resources.ResType resType : Resources.ResType.values()) {
                if (resType.name().equals(string)) {
                    return true;
                }
            }
            return false;

        }
        public static Resources.ResType getByName(String input){
            for (Resources.ResType st : Resources.ResType.values()) {
                if (st.toString().equals(input)){
                    return st;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return this.name();
        }
    }
    private Map<ResType, Integer> values = new HashMap<>();

    public void add(Resources resourcesToBeAdded){
        for(ResType type: resourcesToBeAdded.getResTypes()){
            this.add(type, resourcesToBeAdded.getNumberOfType(type));
        }
    }

    public void subtract(Resources resourcesToBeSubtracted){
        for(ResType type: resourcesToBeSubtracted.getResTypes()){
            this.subtract(type, resourcesToBeSubtracted.getNumberOfType(type));
        }
    }

    public void add(ResType type,Integer val){
        this.values.put(type, this.getNumberOfType(type) + val); //replaces by default if type exists before
    }

    public void subtract(ResType type,Integer val){
        if (this.getNumberOfType(type) > val){
            values.put(type, this.getNumberOfType(type) - val);
        } else if (this.getNumberOfType(type) == val){
            values.remove(type);
        }
    }

    public int sumOfValues(){
        int sum = 0;
        for (int i: values.values()){
            sum += i;
        }
        return sum;
    }

    public List<ResType> getResTypes(){
        return new ArrayList<>(values.keySet());
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

    public boolean isEmpty(){
        return values.isEmpty();
    }

    public boolean isThisTypeOnly(ResType type){
        if (isThereType(type) && (sumOfValues() == getNumberOfType(type)) )
            return true;
        else
            return false;
    }

    public boolean isThisOneType(){
        if (values.keySet().size() == 1)
            return true;
        else
            return false;
    }

    public Resources splitThisType(ResType resType){
        Resources resources = new Resources(resType, getNumberOfType(resType));
        values.remove(resType);
        return resources;
    }

    public void removeThisType(ResType resType){
        values.remove(resType);
    }

    public Resources cloneThisType(ResType resType){
        return new Resources(resType, getNumberOfType(resType));
    }

    public ResType getOnlyType(){
        if (values.keySet().size() == 1)
            return values.keySet().iterator().next();
        else return null;
    }

    // true if smallerOrEqual.compareTo(bigger)
    public boolean smallerOrEqual(Resources otherRes){
        for(ResType resType: this.getResTypes()){
            if (this.getNumberOfType(resType) > otherRes.getNumberOfType(resType)){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return values.toString();
    }

    public Resources() {}

    public Resources(ResType resource, int number) {
        values.put(resource,number);
    }

    // DEBUG METHODS

    //    Over-loading constructor
    public Resources(int stone, int shield, int servant, int coin, int faith){
        this.values = new HashMap<>();
        this.values.put(ResType.STONE, stone);
        this.values.put(ResType.SHIELD, shield);
        this.values.put(ResType.SERVANT, servant);
        this.values.put(ResType.COIN, coin);
        this.values.put(ResType.FAITH, faith);
    }
}