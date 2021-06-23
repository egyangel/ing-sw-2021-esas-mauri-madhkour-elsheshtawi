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
    private int numberOfElements;
    private Resources.ResType resType;


    public Shelf(shelfPlace place) {
        this.place = place;
        if (place == shelfPlace.TOP) this.maxSize = 1;
        else if (place == shelfPlace.MIDDLE) this.maxSize = 2;
        else this.maxSize = 3;
        numberOfElements = 0;
    }

    public int putResource(Resources.ResType resType, int size){
        if (size == 0) return 0;
        this.resType = resType;
        int discarded = 0;
        if (numberOfElements + size > maxSize){
            discarded = numberOfElements + size - maxSize;
            numberOfElements = maxSize;
        } else {
            numberOfElements += size;
        }
        return discarded;
    }

    //adding multiple elements each time
//    public Integer putResource(List<Resources.ResType> resources) {
//        int i=0;
//            if (this.isEmpty() && this.shelfSize()>= resources.size()) {
//                this.resources.addAll(resources);
//                return 0;
//            }else
//                if (this.isFull()) {
//                    return resources.size();
//                }else {
//                    if (!this.isEmpty()) {
//                        if (!this.resources.get(0).equals(resources.get(0))) {
//                            return resources.size();
//                        }
//                    }
//                    while (this.resources.size() < maxSize) {
//                        this.resources.add(resources.get(i));
//                        i++;
//                    }
//                    return resources.size() - i;
//                }
//    }
    // method to be used in the game to returns -1 if different type tried to be put
    public int putResource(Resources res){
        if (!res.isThisOneType()) return -1;
        else return this.putResource(res.getOnlyType(), res.sumOfValues());
    }

    // modification of below method that returns discarded res number
    public int swapShelf(Shelf otherShelf){
        int thisMaxSize = this.maxSize;
        int otherMaxSize = otherShelf.maxSize;
        int thisSize = this.numberOfElements;
        int otherSize = otherShelf.numberOfElements;
        int discarded;
        if (thisSize > otherMaxSize){
            discarded = thisSize - otherSize;
        } else if (otherSize > thisMaxSize){
            discarded = otherSize - thisMaxSize;
        } else {
            discarded = 0;
        }
        Resources.ResType tempType = otherShelf.resType;
        otherShelf.resType = this.resType;
        this.resType = tempType;
        this.numberOfElements = otherSize;
        otherShelf.numberOfElements = thisSize;
        return discarded;
    }

    public boolean isEmpty(){
        return (numberOfElements == 0);
    }

    public boolean isFull(){
        return numberOfElements == maxSize;
    }

    public int getNumberOfElements(){
        return numberOfElements;

    }
    public int getShelfSize(){
        return maxSize;
    }
    public Resources.ResType getShelfResType(){
        return this.resType;
    }
    public int clearShelf(){
        int temp = this.numberOfElements;
        this.numberOfElements = 0;
        return temp;
    }

    public String describeShelfFancy(){
        StringBuilder sb = new StringBuilder();
        switch(place){
            case TOP:
                sb.append("      \u2571 \u2572\n");
                if (this.isEmpty()){
                    sb.append("    \u2571  -  \u2572");
                }
                else {
                    sb.append("    \u2571 " + resType.getFirstAnsiPart() + resType.getSecondAnsiPart() + " \u2572");
                }
                sb.append("\n");
                break;
            case MIDDLE:
                sb.append("  \u2571");
                if (this.isEmpty()){
                    sb.append("  -   -  \u2572");
                }
                else if(numberOfElements == 1){
                    sb.append(" " + resType.getFirstAnsiPart() + resType.getSecondAnsiPart() + "  -  \u2572");
                } else if(numberOfElements == 2){
                    sb.append(" " + resType.getFirstAnsiPart() + resType.getSecondAnsiPart() + " " + resType.getFirstAnsiPart() + resType.getSecondAnsiPart() + " \u2572");
                }
                sb.append("\n");
                break;
            case BOTTOM:
                sb.append("\u2571");
                if (this.isEmpty()){
                    sb.append("  -   -   -  \u2572");
                }
                else if(numberOfElements == 1){
                    sb.append(" " + resType.getFirstAnsiPart() + resType.getSecondAnsiPart() + "  -  -   \u2572");
                } else if(numberOfElements == 2){
                    sb.append(" " + resType.getFirstAnsiPart() + resType.getSecondAnsiPart() + " " + resType.getFirstAnsiPart() + resType.getSecondAnsiPart() + "  -  \u2572");
                } else if(numberOfElements == 3){
                    sb.append(" " + resType.getFirstAnsiPart() + resType.getSecondAnsiPart() + " " + resType.getFirstAnsiPart() + resType.getSecondAnsiPart() + " " + resType.getFirstAnsiPart() + resType.getSecondAnsiPart() + " \u2572");
                }
                sb.append("\n");
                for(int i = 0; i<15; i++){
                    sb.append("\u2500");
                }
                break;
        }
        return sb.toString();
    }

//    public static void main(String[] args){
//        Shelf[] warehouse = new Shelf[3];
//        warehouse[0] = new Shelf(Shelf.shelfPlace.TOP);
//        warehouse[1] = new Shelf(Shelf.shelfPlace.MIDDLE);
//        warehouse[2] = new Shelf(Shelf.shelfPlace.BOTTOM);
//        warehouse[0].putResource(Resources.ResType.COIN, 1);
//        warehouse[1].putResource(Resources.ResType.SHIELD, 1);
//        warehouse[2].putResource(Resources.ResType.SERVANT, 3);
//        StringBuilder sb = new StringBuilder();
//        sb.append(warehouse[0].describeShelfFancy());
//        sb.append(warehouse[1].describeShelfFancy());
//        sb.append(warehouse[2].describeShelfFancy());
//        System.out.println(sb.toString());
//    }

    public void removeFromShelf(int number){
        this.numberOfElements -= number;
    }

    public Resources getResource(){
        if (isEmpty()) return new Resources();
        else return new Resources(resType, numberOfElements);
    }
}
