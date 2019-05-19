package com.example.peter.thekitchenmenu.data.entity;

import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.FtsOptions;

import static com.example.peter.thekitchenmenu.data.entity.ProductFastTextSearch.TABLE_FTS_PRODUCT;

@Entity(tableName = TABLE_FTS_PRODUCT)
@Fts4(contentEntity = ProductEntity.class,
        tokenizer = FtsOptions.Tokenizer.UNICODE61,
        prefix = {2,3,4})
public class ProductFastTextSearch {

    public static final String TAG = ProductFastTextSearch.class.getSimpleName();
    public static final String TABLE_FTS_PRODUCT = "fts_prod_comm";

    private String description;
    private String shoppingListItemName;

    public ProductFastTextSearch(String description, String shoppingListItemName) {
        this.description = description;
        this.shoppingListItemName = shoppingListItemName;
    }

    public String getDescription() {
        return description;
    }

    public String getShoppingListItemName() {
        return shoppingListItemName;
    }
}
