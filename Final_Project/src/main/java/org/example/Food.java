package org.example;

import java.io.Serializable;

public class Food extends Grocery implements Serializable {
    private String category;

    public Food(String name, int spaceRequired, String expiryDate, String category) {
        super(name, spaceRequired, expiryDate);
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String getInformation() {
        return super.getInformation() + ", also the category is " + category;
    }
}
