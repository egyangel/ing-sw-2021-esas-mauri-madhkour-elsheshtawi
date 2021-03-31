package it.polimi.ingsw.model;

public class Shelf {
    private Resources resources;
    private final int maxSize;

    public Shelf(int maxSize){
        this.maxSize = maxSize;
        this.resources = new Resources();
    }

    public boolean isEmpty(){
        return resources.isEmpty();
    }

    public boolean isFull(){
        return resources.sumOfValues() == maxSize;
    }
}
