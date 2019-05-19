package com.example.peter.thekitchenmenu.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// See: https://developer.android.com/training/data-storage/room/relationships many to many
@Entity
public class ShoppingListItem {

    @PrimaryKey
    private int id;
    private String shoppingListName;

}
