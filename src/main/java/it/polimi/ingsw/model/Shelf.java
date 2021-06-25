package it.polimi.ingsw.model;


public class Shelf {
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

    public void removeFromShelf(int number){
        this.numberOfElements -= number;
    }

    public Resources getResource(){
        if (isEmpty()) return new Resources();
        else return new Resources(resType, numberOfElements);
    }
}
