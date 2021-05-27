package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;


public class Shelf {
    public enum shelfPlace{
        TOP(0), MIDDLE(1), BOTTOM(2);
        private int indexInWarehouse;
        public int getIndexInWarehouse(){
            return indexInWarehouse;
        }
        private shelfPlace(int index){
            this.indexInWarehouse = index;
        }
    }

    private final int maxSize;
    private final shelfPlace place;
    private List<Resources.ResType> resources;


    public Shelf(shelfPlace place) {
        this.place = place;
        if (place == shelfPlace.TOP) this.maxSize = 1;
        else if (place == shelfPlace.MIDDLE) this.maxSize = 2;
        else this.maxSize = 3;
        resources = new ArrayList<>();
    }

    public void putResource(Resources.ResType resType, int size){
        List<Resources.ResType> list = new ArrayList<>();
        for (int i = 0; i < size; i++){
            list.add(resType);
        }
        putResource(list);
    }

    //adding multiple elements each time
    public Integer putResource(List<Resources.ResType> resources) {

        int i=0;
            if (this.isEmpty() && this.shelfSize()>= resources.size()) {
                this.resources.addAll(resources);
                return 0;
            }else
                if (this.isFull()) {

                    return resources.size();
                }else {
                    if (!this.isEmpty()) {
                        if (!this.resources.get(0).equals(resources.get(0))) {

                            return resources.size();
                        }
                    }
                    while (this.resources.size() < maxSize) {
                        this.resources.add(resources.get(i));
                        i++;
                    }
                    return resources.size() - i;
                }
    }
    // method to be used in the game to return false if cannot put it
    public boolean putResource(Resources res){
        if (!res.isThisOneType() || (res.sumOfValues() + this.getNumberOfElements() > shelfSize())) return false;
        this.putResource(res.getOnlyType(), res.sumOfValues());
        return true;
    }

    // modification of below method that returns discarded res number
    public int swapShelf(Shelf otherShelf){
        int thisMaxSize = this.maxSize;
        int otherMaxSize = otherShelf.maxSize;
        int thisSize = this.resources.size();
        int otherSize = otherShelf.resources.size();
        int discarded = 0;
        if (thisSize > otherMaxSize){
            discarded = thisSize - otherSize;
        }
        if (otherSize > thisMaxSize){
            discarded = otherSize - thisMaxSize;
        }
        List<Resources.ResType> resTypeList = new ArrayList<>();
        resTypeList.addAll(otherShelf.shelf());
        otherShelf.clearShelf();
        otherShelf.putResource(this.shelf());
        this.clearShelf();
        this.putResource(resTypeList);
        return discarded;
    }

//    public boolean swapShelf(Shelf otherShelf){
//        if ( otherShelf.getNumberOfElements() > this.maxSize || this.getNumberOfElements() > otherShelf.shelfSize() )
//            return false;
//        else{
//            List<Resources.ResType> resTypeList = new ArrayList<>();
//            resTypeList.addAll(otherShelf.shelf());
//            otherShelf.clearShelf();
//            otherShelf.putResource(this.shelf());
//            this.clearShelf();
//            this.putResource(resTypeList);
//            return true;
//        }
//    }

    public boolean isEmpty(){
        return resources.isEmpty();
    }

    public boolean isFull(){
        return resources.size() == maxSize;
    }
    private List<Resources.ResType> shelf(){
        return resources;
    }
    public int getNumberOfElements(){
        return resources.size();
    }
    public int shelfSize(){
        return maxSize;
    }
    public Resources.ResType getShelfResType(){
        return this.resources.get(0);
    }
    public int clearShelf(){
        int size = resources.size();
        this.resources.clear();
        return size;
    }
    public String describeShelf(){
        String string = this.getNumberOfElements()+" of "+ this.getShelfResType();
        return string;
    }
    public boolean removeOneFromShelf(){
        return this.resources.remove(this.resources.get(0));
    }

    public void removeFromShelf(int number){
        while (number > 0){
            removeOneFromShelf();
            number--;
        }
    }

    public Resources getResource(){
        if (isEmpty()) return new Resources();
        else return new Resources(resources.get(0), resources.size());
    }
}
