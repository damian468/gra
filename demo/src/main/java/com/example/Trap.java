package com.example;



public class Trap extends Item  {
    private int damage;

    public Trap(double x, double y, int damage) {
        super(x, y);
        this.damage = damage;

    }

    public void trigger(Player player) {
        if (!isInteracted){
            player.reduceHealth(damage);
            isInteracted = true;
        }



    }

    @Override
    public void interact(Player player) {
        trigger(player);
    }
}
