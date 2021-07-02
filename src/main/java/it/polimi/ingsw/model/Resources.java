package it.polimi.ingsw.model;

import java.util.*;

/**
 * Resources  class , it is the abstraction of the resources inside the game
 * on the personal board of the player
 * */
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
    /**
     * method that add to the current res the res that is passed as argument
     * @param resourcesToBeAdded res that has to be added to the current resources
     * */
    public void add(Resources resourcesToBeAdded){
        for(ResType type: resourcesToBeAdded.getResTypes()){
            this.add(type, resourcesToBeAdded.getNumberOfType(type));
        }
    }
    /**
     * method that sub to the current res the res that is passed as argument
     * @param resourcesToBeSubtracted res that has to be deleted from the current resources
     * */
    public void subtract(Resources resourcesToBeSubtracted){
        for(ResType type: resourcesToBeSubtracted.getResTypes()){
            this.subtract(type, resourcesToBeSubtracted.getNumberOfType(type));
        }
    }
    /**
     * method that add to the current res a single res type with the amount of that res
     * @param type res type that has to be added to the current resources
     * @param val amount of the res that has to be added
     * */
    public void add(ResType type,Integer val){
        if (val <= 0) return;
        this.values.put(type, this.getNumberOfType(type) + val); //replaces by default if type exists before
    }
    /**
     * method that sub to the current res the res that is passed as argument
     *  @param type res type that has to be subtracted to the current resources
     *  @param val amount of the res that has to be subtracted
     * */

    public void subtract(ResType type,Integer val){
        if (this.getNumberOfType(type) > val){
            values.put(type, this.getNumberOfType(type) - val);
        } else {
            values.remove(type);
        }
    }
    /**
     * compute the total number of res inside the object, without count the res type
     * */
    public int sumOfValues(){
        int sum = 0;
        for (int i: values.values()){
            sum += i;
        }
        return sum;
    }

    /**
     * method that returns resource types as keys inside the map
     * @return list of ResType
     * */
    public List<ResType> getResTypes(){
        return new ArrayList<>(values.keySet());
    }

    /**
     * method that returns the number of a particular resource in the map (the value)
     * @param type Resource type of the query
     * @return number of resources as int
     * */
    public int getNumberOfType(ResType type){
        return values.getOrDefault(type, 0);
    }

    /**
     * method that checks in the map if the resource type exists as key
     * @param type Resource type of the query
     * @return boolean value of the result
     * */
    public boolean isThereType(ResType type){
        return values.containsKey(type);
    }

    public void clear(){
        this.values.clear();
    }

    public boolean isEmpty(){
        return values.isEmpty();
    }
    /**
     * check if inside this object there is only the param passed
     * @param type is the res type to check
     * */
    public boolean isThisTypeOnly(ResType type){
        if (isThereType(type) && (sumOfValues() == getNumberOfType(type)) )
            return true;
        else
            return false;
    }
    /**
     * check if inside this object there is only one res type

     * */
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
    /**
     * method that check if this is smaller or Equal to the other Resources
     * @param otherRes the resources that has to be compared with this
     * */
    // true if smallerOrEqual.compareTo(bigger), SUBSETOF
    public boolean smallerOrEqual(Resources otherRes){
        for(ResType resType: Resources.ResType.values()){
            if (this.getNumberOfType(resType) > otherRes.getNumberOfType(resType)){
                return false;
            }
        }
        return true;
    }

    /**
     * method that checks if this resource encompasses the parameter
     * eg, ((STONE, 2), (COIN, 1)).includes((COIN, 1)) returns true
     * @param Resources resource the be compared to
     * @return the result of the comparison
     * */
    public boolean includes(Resources otherRes){
        for(ResType resType: Resources.ResType.values()){
            if (otherRes.getNumberOfType(resType) > this.getNumberOfType(resType)){
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