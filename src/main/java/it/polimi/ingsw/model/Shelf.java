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

    public void PutResource(Resources.ResType resType, int size){
        List<Resources.ResType> list = new ArrayList<>();
        for (int i = 0; i < size; i++){
            list.add(resType);
        }
        PutResource(list);
    }

    //adding multiple elements each time
    public Integer PutResource(List<Resources.ResType> resources) {


        int i=this.resources.size();

            if (this.isEmpty() && this.ShelfSize()>= resources.size())
                this.resources.addAll(resources);
            else
                if (this.isFull() || this.ShelfSize()< resources.size())
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

    public boolean SwapShelf(Shelf otherShelf){
        if ( otherShelf.GetNumberOfElements() > this.maxSize || this.GetNumberOfElements() > otherShelf.ShelfSize() )
            return false;
        else{
            List<Resources.ResType> resTypeList = new ArrayList<>();
            resTypeList.addAll(otherShelf.shelf());
            otherShelf.ClearShelf();
            otherShelf.PutResource(this.shelf());
            this.ClearShelf();
            this.PutResource(resTypeList);
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
    public int GetNumberOfElements(){
        return resources.size();
    }
    public int ShelfSize(){
        return maxSize;
    }
    public Resources.ResType GetShelfResType(){
        return this.resources.get(0);
    }
    public void ClearShelf(){
        this.resources.clear();
    }
    public String describeShelf(){
        String string = this.GetNumberOfElements()+" of "+ this.GetShelfResType();
        return string;
    }
}
