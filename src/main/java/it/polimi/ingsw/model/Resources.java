package it.polimi.ingsw.model;

import javax.naming.spi.ResolveResult;
import java.util.*;

public class Resources {
    public enum ResType {
        STONE("\u001B[37m[", "⌬]\u001B[0m"),
        SHIELD("\u001B[34m[", "⌺]\u001B[0m"),
        SERVANT("\u001B[35m[", "☺]\u001B[0m"),
        COIN("\u001B[33m[", "⨎]\u001B[0m"),
        FAITH("\u001B[31m[", "✞]\u001B[0m");

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
        private String firstAnsiPart;
        private String secondAnsiPart;

        public String getFirstAnsiPart() {
            return firstAnsiPart;
        }

        public String getSecondAnsiPart() {
            return secondAnsiPart;
        }

        ResType(String firstAnsiPart, String secondAnsiPart) {
            this.firstAnsiPart = firstAnsiPart;
            this.secondAnsiPart = secondAnsiPart;
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
        if (val <= 0) return;
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

    // true if smallerOrEqual.compareTo(bigger), SUBSETOF
    public boolean smallerOrEqual(Resources otherRes){
        for(ResType resType: Resources.ResType.values()){
            if (this.getNumberOfType(resType) > otherRes.getNumberOfType(resType)){
                return false;
            }
        }
        return true;
    }

    public boolean includes(Resources otherRes){
        for(ResType resType: Resources.ResType.values()){
            if (otherRes.getNumberOfType(resType) > this.getNumberOfType(resType)){
                return false;
            }
        }
        return true;
    }

//    public static void main(String[] args){
//        Resources x = new Resources();
//        x.add(ResType.STONE, 2);
//        x.add(ResType.COIN, 2);
//        Resources y = new Resources();
//        y.add(ResType.STONE, 2);
//        y.add(ResType.COIN, 1);
//        y.add(ResType.SHIELD, 1);
//        System.out.println(x.includes(y));
//    }

    @Override
    public String toString() {
        return values.toString();
    }

    public Resources() {}

    public Resources(ResType resource, int number) {
        if (number >=0) values.put(resource,number);
    }

    public String describeResource(){
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<ResType, Integer> entry : values.entrySet()) {
            stringBuilder.append(entry.getKey().firstAnsiPart + entry.getValue() + " " + entry.getKey().secondAnsiPart);
        }
        return stringBuilder.toString();
    }

    // DEBUG METHODS

    //    Over-loading constructor
    public Resources(int stone, int shield, int servant, int coin){
        this.values.put(ResType.STONE, stone);
        this.values.put(ResType.SHIELD, shield);
        this.values.put(ResType.SERVANT, servant);
        this.values.put(ResType.COIN, coin);
    }
}