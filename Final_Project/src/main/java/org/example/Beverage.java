package org.example;

import java.io.Serializable;

public class Beverage extends Grocery implements Serializable {
    private double volumeLeft;

    public Beverage(String name, int spaceRequired, String expiryDate) {
        super(name, spaceRequired, expiryDate);
        this.volumeLeft = 100;
    }

    public Beverage(String name, int spaceRequired, String expiryDate, double volumeLeft) {
        super(name, spaceRequired, expiryDate);
        this.volumeLeft = volumeLeft;
    }

    public double getVolumeLeft() {
        return volumeLeft;
    }

    @Override
    public String getInformation() {
        return super.getInformation() + ", volume left: " + volumeLeft;
    }
}
