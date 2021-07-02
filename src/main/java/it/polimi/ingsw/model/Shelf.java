package it.polimi.ingsw.model;
/**
 * class that represent the shelf
 *
 * */

public class Shelf {
    /**
     * enumeration class that represent the the position of the shelf inside the warehouse
     *
     * */
    public enum shelfPlace{
        TOP(0), MIDDLE(1), BOTTOM(2);
        private int indexInWarehouse;
        public int getIndexInWarehouse(){
            return indexInWarehouse;
        }
        shelfPlace(int index){
            this.indexInWarehouse = index;
        }
    }

    private final int maxSize;
    private final shelfPlace place;
    private int numberOfElements;
    private Resources.ResType resType = null;

    /**
     * constructor that initialize the shelf based on its position inside the warehouse
     * @param place is the position of the shelf inside the warehouse
     *
     * */
    public Shelf(shelfPlace place) {
        this.place = place;
        if (place == shelfPlace.TOP) this.maxSize = 1;
        else if (place == shelfPlace.MIDDLE) this.maxSize = 2;
        else this.maxSize = 3;
        numberOfElements = 0;
    }
    /**
     * method that put res inside the shelf, return the number of extra res that it cannot be put inside the shelf
     * @param resType the type of the resource
     * @param size the quantity of re type that the player want to put inside the shelf
     *
     * */
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
    /**
     * method to be used in the game to returns -1 if different type tried to be put otherwise put res inside the shelf
     * @param res  the resource that should be added
     * */
    // method to be used in the game to returns -1 if different type tried to be put
    public int putResource(Resources res){
        if (!res.isThisOneType()) return -1;
        else return this.putResource(res.getOnlyType(), res.sumOfValues());
    }

    /**
     * method that handle the swap between two shelves w.r.t. the rule of the game and return the number of discarded extra res
     * @param otherShelf  the shelf that has to be swapped with this
     * */
    // modification of below method that returns discarded res number
    public Resources swapShelf(Shelf otherShelf){
        int thisMaxSize = this.maxSize;
        int otherMaxSize = otherShelf.maxSize;
        int thisSize = this.numberOfElements;
        int otherSize = otherShelf.numberOfElements;
        int discarded;
        Resources.ResType discardedType;
        if (thisSize > otherMaxSize){
            discarded = thisSize - otherSize;
            discardedType = this.resType;
        } else if (otherSize > thisMaxSize){
            discarded = otherSize - thisMaxSize;
            discardedType = otherShelf.resType;
        } else {
            discarded = 0;
            discardedType = Resources.ResType.STONE;
        }
        Resources.ResType tempType = otherShelf.resType;
        otherShelf.resType = this.resType;
        this.resType = tempType;
        this.numberOfElements = otherSize;
        otherShelf.numberOfElements = thisSize;
        return new Resources(discardedType, discarded);
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

    public void removeFromShelf(int number){
        this.numberOfElements -= number;
    }

    public Resources getResource(){
        if (isEmpty()) return new Resources();
        else return new Resources(resType, numberOfElements);
    }
}
