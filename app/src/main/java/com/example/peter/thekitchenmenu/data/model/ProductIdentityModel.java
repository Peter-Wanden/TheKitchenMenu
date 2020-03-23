package com.example.peter.thekitchenmenu.data.model;

import javax.annotation.Nonnull;

public class ProductIdentityModel {

    private static final String TAG = "tkm - ProductIdentityModel";

    @Nonnull
    private final String description;
    @Nonnull
    private final String shoppingListItemName;
    private final int category;
    private final int shelfLife;

    public ProductIdentityModel(@Nonnull String description, @Nonnull String shoppingListItemName,
                                int category, int shelfLife) {
        this.description = description;
        this.shoppingListItemName = shoppingListItemName;
        this.category = category;
        this.shelfLife = shelfLife;
    }

    @Nonnull
    public String getDescription() {
        return description;
    }

    @Nonnull
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
