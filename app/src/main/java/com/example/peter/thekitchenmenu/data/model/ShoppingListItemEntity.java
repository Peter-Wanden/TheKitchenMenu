package com.example.peter.thekitchenmenu.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;

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
