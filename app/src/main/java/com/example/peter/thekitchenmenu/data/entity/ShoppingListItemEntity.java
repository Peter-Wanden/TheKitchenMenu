package com.example.peter.thekitchenmenu.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ShoppingListItemEntity implements TkmEntity {

    @PrimaryKey
    private String id;
    private String shoppingListName;

    @NonNull
    @Override
    public String getId() {
        return id;
    }
}
