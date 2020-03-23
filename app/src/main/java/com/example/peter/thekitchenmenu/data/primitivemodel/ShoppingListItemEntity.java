package com.example.peter.thekitchenmenu.data.primitivemodel;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import javax.annotation.Nonnull;

@Entity
public class ShoppingListItemEntity implements PrimitiveModel {

    @PrimaryKey
    private String id;
    private String shoppingListName;

    @Nonnull
    @Override
    public String getId() {
        return id;
    }
}
