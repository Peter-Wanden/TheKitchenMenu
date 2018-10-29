package com.example.peter.thekitchenmenu.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.model.ProductMy;

import java.util.List;

@Dao
public interface ProductMyDAO {

    // List all of my products
    @Query("SELECT * FROM " + Constants.TABLE_PRODUCT_MY)
    LiveData<List<ProductMy>> listAllMyProducts();

    @Insert
    void insertProductMy(ProductMy productMy);

    @Insert
    void insertProductsMy(List<ProductMy> productMyList);

    @Update
    void updateProductMy(ProductMy productMy);

    @Delete
    void deleteProductMy(ProductMy productMy);

    @Query("DELETE FROM " + Constants.TABLE_PRODUCT_MY)
    void deleteAllMyProducts();

    // Checks to see if a product exists
    @Query("SELECT * FROM " +
            Constants.TABLE_PRODUCT_MY +
            " WHERE " +
            Constants.PRODUCT_MY_FB_REFERENCE_KEY + " = :fbProductReferenceKey")
    ProductMy findByFbRefKey(String fbProductReferenceKey);
}
