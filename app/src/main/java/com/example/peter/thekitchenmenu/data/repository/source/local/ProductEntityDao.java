package com.example.peter.thekitchenmenu.data.repository.source.local;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.peter.thekitchenmenu.data.primitivemodel.product.ProductEntity;

import java.util.List;

import static android.app.SearchManager.SUGGEST_COLUMN_INTENT_DATA;
import static android.app.SearchManager.SUGGEST_COLUMN_TEXT_1;
import static android.app.SearchManager.SUGGEST_COLUMN_TEXT_2;
import static com.example.peter.thekitchenmenu.data.primitivemodel.product.ProductEntity.DESCRIPTION;
import static com.example.peter.thekitchenmenu.data.primitivemodel.product.ProductEntity.ID;
import static com.example.peter.thekitchenmenu.data.primitivemodel.product.ProductEntity.SHOPPING_LIST_ITEM_NAME;
import static com.example.peter.thekitchenmenu.data.primitivemodel.product.ProductEntity.TABLE_PRODUCT;
import static com.example.peter.thekitchenmenu.data.primitivemodel.product.ProductFastTextSearch.TABLE_FTS_PRODUCT;

@Dao
public interface ProductEntityDao {

    @Query("SELECT * FROM " + TABLE_PRODUCT + " ORDER BY " + DESCRIPTION)
    List<ProductEntity> getAll();

    @Query("SELECT * FROM " + TABLE_PRODUCT + " WHERE id = :id")
    ProductEntity getById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProductEntity productEntity);

    @Update
    void update(ProductEntity productEntity);

    @Query("DELETE FROM " + TABLE_PRODUCT + " WHERE " + ID + " = :id")
    void deleteById(String id);

    @Query("DELETE FROM " + TABLE_PRODUCT)
    void deleteAll();

    @Query("SELECT " +
            TABLE_PRODUCT + "." + ID + " AS _id, " +
            TABLE_PRODUCT + "." + DESCRIPTION + " AS " + SUGGEST_COLUMN_TEXT_1 + ", " +
            TABLE_PRODUCT + "." + SHOPPING_LIST_ITEM_NAME + " AS " + SUGGEST_COLUMN_TEXT_2 + ", " +
            TABLE_PRODUCT + "." + ID + " AS " + SUGGEST_COLUMN_INTENT_DATA + " " + "FROM " +
            TABLE_PRODUCT + " JOIN " + TABLE_FTS_PRODUCT + " ON (" +
            TABLE_PRODUCT + "." + ID + " = " + TABLE_FTS_PRODUCT + ".rowid)" + " WHERE " +
            TABLE_FTS_PRODUCT + " MATCH :query")
    Cursor findProductsThatMatch(String query);
}
