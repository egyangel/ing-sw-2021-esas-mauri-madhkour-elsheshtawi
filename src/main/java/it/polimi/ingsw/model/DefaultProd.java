package it.polimi.ingsw.model;
/**
 * @deprecated class not more needed
 * */
public class DefaultProd {
    private Resources LHS;
    private Resources RHS;

    public DefaultProd(){
        this.LHS = new Resources();
        this.RHS = new Resources();
    }

    public void putTopLeftResource(Resources.ResType L1){
        this.LHS.add(L1, 1);
    }
    public Resources getLeftRes(){
         return LHS;
    }
    public Resources getRightRes(){
        return RHS;
    }

    public void putBottomLeftResource(Resources.ResType L2){
        this.LHS.add(L2, 1);
    }

    public void putRightResource(Resources.ResType R){
        this.RHS.add(R,1);
    }

    public void produce(){
        this.LHS.clear();
        this.RHS.clear();
    }
}
