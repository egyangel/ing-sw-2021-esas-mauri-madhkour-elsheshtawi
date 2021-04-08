package it.polimi.ingsw.model;

import it.polimi.ingsw.model.enumclasses.ResType;

import java.util.ArrayList;
import java.util.List;
/*
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
}*/


public class Shelf {

    private final int maxSize;
    private List<ResType> resources;


    public Shelf(int maxSize) {
        this.maxSize = maxSize;
        resources = new ArrayList<>();
    }

    public String PutResource(List<ResType> resources) {
        if( this.isEmpty() )
            this.resources.add(resources);
        else
            if( this.isFull() )
                return "Full shelf";
            else {
                if ( !this.resources.get(0).equals(resources) )
                {
                   return "Incorrect action";
                }else{
                    this.resources.add(resources);
                }
            }
            return"Correct Action";
    }

    public String SwapShelf(Shelf otherShelf){
        ResType temp = otherShelf.GetShelfResType();
        int numOfResTemp = otherShelf.GetNumberOfElements();
        if ( otherShelf.GetNumberOfElements() > this.maxSize || this.GetNumberOfElements() > otherShelf.ShelfSize() )
            return "Incorrect action";
        else{
            otherShelf.ClearShelf();
            for(int i = 0; i < this.GetNumberOfElements() ; i++ ){
                otherShelf.PutResource(this.GetShelfResType());
            }

            for(int i = 0; i < numOfResTemp ; i++ ){
                this.PutResource(temp);
            }
        }

        return "Swap done";

    }

    public boolean isEmpty(){
        return resources.isEmpty();
    }

    public boolean isFull(){
        return resources.size() == maxSize;
    }
    public int GetNumberOfElements(){
        return resources.size();
    }
    public int ShelfSize(){
        return maxSize;
    }
    public ResType GetShelfResType(){
        return this.resources.get(0);
    }
    public void ClearShelf(){
        this.resources.clear();
    }
}
