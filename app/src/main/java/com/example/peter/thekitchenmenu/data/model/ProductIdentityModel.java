package com.example.peter.thekitchenmenu.data.model;

import androidx.annotation.NonNull;

public class ProductIdentityModel {

    private static final String TAG = "tkm - ProductIdentityModel";

    @NonNull
    private final String description;
    @NonNull
    private final String shoppingListItemName;
    private final int category;
    private final int shelfLife;

    public ProductIdentityModel(@NonNull String description, @NonNull String shoppingListItemName,
                                int category, int shelfLife) {
        this.description = description;
        this.shoppingListItemName = shoppingListItemName;
        this.category = category;
        this.shelfLife = shelfLife;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    @NonNull
    public String getShoppingListItemName() {
        return shoppingListItemName;
    }

    public int getCategory() {
        return category;
    }

    public int getShelfLife() {
        return shelfLife;
    }

}
