package com.example.peter.thekitchenmenu.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.peter.thekitchenmenu.data.primitivemodel.EntityModel;

import javax.annotation.Nonnull;

@Entity
public class ShoppingListItemEntity implements EntityModel {

    @PrimaryKey
    private String id;
    private String shoppingListName;

    @Nonnull
    @Override
    public String getDataId() {
        return id;
    }
}
