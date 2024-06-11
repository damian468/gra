package com.example;



public class Lever extends Item {
    private Door linkedDoor;

    public Lever(double x, double y, Door linkedDoor) {
        super(x, y);
        this.linkedDoor = linkedDoor;
    }

    public void activate() {
        linkedDoor.open();
    }
    public Door getLinkedDoor() {return linkedDoor;}
    @Override
    public void interact(Player player) {
        activate();
    }
}
