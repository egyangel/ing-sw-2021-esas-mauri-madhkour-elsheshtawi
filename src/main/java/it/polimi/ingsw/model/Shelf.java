package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;


public class Shelf {
    public enum shelfPlace{
        TOP, MIDDLE, BOTTOM
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


        int i=this.resources.size();

            if (this.isEmpty() && this.shelfSize()>= resources.size())
                this.resources.addAll(resources);
            else
                if (this.isFull() || this.shelfSize()< resources.size())
                     return  resources.size();
                else
                {
                    if (!this.resources.get(0).equals(resources.get(0)))
                        return  resources.size();
                    else {
                        while (this.resources.size() < maxSize) {
                            this.resources.add(resources.get(i));
                            i++;
                        }
                        return this.resources.size() - resources.size();
                    }
                }
            return 0;
    }

    // method to be used in the game to return false if cannot put it
    public boolean putResource(Resources res){
        if (!res.isThisOneType() || (res.sumOfValues() + getNumberOfElements() > shelfSize())) return false;
        putResource(res.getOnlyType(), res.sumOfValues());
        return true;
    }


    public boolean swapShelf(Shelf otherShelf){
        if ( otherShelf.getNumberOfElements() > this.maxSize || this.getNumberOfElements() > otherShelf.shelfSize() )
            return false;
        else{
            List<Resources.ResType> resTypeList = new ArrayList<>();
            resTypeList.addAll(otherShelf.shelf());
            otherShelf.ClearShelf();
            otherShelf.putResource(this.shelf());
            this.ClearShelf();
            this.putResource(resTypeList);
            return true;
        }
    }

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
    public Resources.ResType GetShelfResType(){
        return this.resources.get(0);
    }
    public void ClearShelf(){
        this.resources.clear();
    }
    public String describeShelf(){
        String string = this.getNumberOfElements()+" of "+ this.GetShelfResType();
        return string;
    }

    public boolean removeOneFromShelf(){
        return this.resources.remove(this.resources.get(0));
    }
}
