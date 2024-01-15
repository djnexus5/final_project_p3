package org.example;

import java.io.Serializable;
import java.time.LocalDate;

public class Grocery implements Expirable, Serializable {
    private String name;
    private int spaceRequired;
    private String expiryDate;

    public Grocery(String name, int spaceRequired, String expiryDate) {
        this.name = name;
        this.spaceRequired = spaceRequired;
        this.expiryDate = expiryDate;
    }

    public String getName() {
        return name;
    }

    public int getSpaceRequired() {
        return spaceRequired;
    }

    @Override
    public LocalDate getExpiryDate() {
        return LocalDate.parse((expiryDate));
    }

    public String getInformation() {
        return name + "," + spaceRequired + "," + expiryDate;
    }

    @Override
    public boolean isExpired() {
        return getExpiryDate().isBefore(LocalDate.now());
    }
}
