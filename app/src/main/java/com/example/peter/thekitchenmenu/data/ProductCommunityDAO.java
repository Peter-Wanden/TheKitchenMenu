package com.example.peter.thekitchenmenu.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.model.ProductCommunity;

import java.util.List;

@Dao
public interface ProductCommunityDAO {

    // Lists all community products
    @Query("SELECT * " +
            "FROM "+
            Constants.TABLE_PRODUCT_COMM +
            " ORDER BY " +
            Constants.TABLE_PRODUCT_COMM_DESCRIPTION)
    LiveData<List<ProductCommunity>> listAllProductCommunity();

    // Retrieves a single product
    @Query("SELECT * " +
           "FROM " +
            Constants.TABLE_PRODUCT_COMM +
            " WHERE _id = :id")
    LiveData<ProductCommunity> loadProductCommunityById(int id);

    @Insert
    void insertProductCommunity(ProductCommunity productCommunity);

    @Insert
    void insertProductsCommunity(List<ProductCommunity> productsCommunity);

    @Update
    void updateProductCommunity(ProductCommunity productCommunity);

    @Delete
    void deleteProductCommunity(ProductCommunity productCommunity);

    @Query("DELETE FROM " + Constants.TABLE_PRODUCT_COMM)
    void deleteAllProductCommunity();

    // Checks to see if a product exists
    @Query("SELECT * FROM " +
            Constants.TABLE_PRODUCT_COMM +
            " WHERE " +
            Constants.PRODUCT_MY_FB_REFERENCE_KEY + " = :fbProductReferenceKey")
    ProductCommunity findByProductReferenceKey(String fbProductReferenceKey);
}
