package com.example;



public class Key extends Position {
    private int keyId;

    public Key(double x, double y, int keyId) {
        super(x,y);

        this.keyId = keyId;
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
