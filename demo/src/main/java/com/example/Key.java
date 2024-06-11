package com.example;



public class Key extends Item {
    private int keyId;

    public Key(double x, double y, int keyId) {
        super(x,y);

        this.keyId = keyId;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public void interact(Player player) {
        if(!isInteracted){
            player.setInventory(this);
            isInteracted = true;
        }

    }



    public int getKeyId() {
        return keyId;
    }
}
