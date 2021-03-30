package it.polimi.ingsw.model;

public class PersonalBoard {
    private DefaultProd defProd;
    private DevSlot[] devSlots = new DevSlot[3];

    public PersonalBoard(){
        defProd = new DefaultProd();
        // from left to right on personal board
        devSlots[0] = new DevSlot();
        devSlots[1] = new DevSlot();
        devSlots[3] = new DevSlot();
    }
}
