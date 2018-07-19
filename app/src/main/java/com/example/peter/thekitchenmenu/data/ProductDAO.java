package com.example.peter.thekitchenmenu.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.peter.thekitchenmenu.model.Product;

import java.util.List;

@Dao
public interface ProductDAO {

    /* Query whole table */
    @Query("SELECT * FROM Products ORDER BY Category")
    LiveData<List<Product>> loadProducts();

    /* Query one row */
    @Query("SELECT * FROM Products WHERE id = :id")
    LiveData<Product> loadProductById(int id);

    @Insert
    void insertProduct(Product product);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateProduct(Product product);

    @Delete
    void deleteProduct(Product product);
}
