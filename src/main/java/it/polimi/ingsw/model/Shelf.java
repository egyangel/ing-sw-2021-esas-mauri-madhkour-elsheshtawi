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



    //adding multiple elements each time
    public Integer PutResource(List<ResType> resources) {


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

    public String SwapShelf(Shelf otherShelf){
      //  ResType temp = otherShelf.GetShelfResType();
        //int numOfResTemp = otherShelf.GetNumberOfElements();
            Shelf temp=otherShelf;

        if ( otherShelf.GetNumberOfElements() > this.maxSize || this.GetNumberOfElements() > otherShelf.ShelfSize() )
            return "Incorrect action";
        else{
            otherShelf.ClearShelf();

                otherShelf= this;

                this.ClearShelf();
                this.PutResource(temp.shelf());
            }

        return "Swap done";

    }

    public boolean isEmpty(){
        return resources.isEmpty();
    }

    public boolean isFull(){
        return resources.size() == maxSize;
    }
    private List<ResType> shelf(){
        return resources;
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
    public void GetShelfContent(){
        System.out.println("There are => "+this.GetNumberOfElements()+" of "+ this.GetShelfResType());
    }
}
