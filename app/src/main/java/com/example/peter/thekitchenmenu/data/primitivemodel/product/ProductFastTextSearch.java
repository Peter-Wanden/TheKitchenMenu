package com.example.peter.thekitchenmenu.data.primitivemodel.product;

import androidx.room.Entity;
import androidx.room.Fts4;

import static com.example.peter.thekitchenmenu.data.primitivemodel.product.ProductFastTextSearch.TABLE_FTS_PRODUCT;

@Entity(tableName = TABLE_FTS_PRODUCT)
@Fts4(contentEntity = ProductEntity.class, prefix = {2,3,4})
public class ProductFastTextSearch {

    public static final String TAG = ProductFastTextSearch.class.getSimpleName();
    public static final String TABLE_FTS_PRODUCT = "fts_product";

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
