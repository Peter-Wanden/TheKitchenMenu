package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.FavoriteProductEntity;
import com.google.firebase.database.annotations.NotNull;

import java.util.List;

public interface FavoriteProductsDataSource extends DataSource {

    void getFavoriteProductByProductId(@NonNull String productId,
                                       @NonNull GetItemCallback callback);
}
