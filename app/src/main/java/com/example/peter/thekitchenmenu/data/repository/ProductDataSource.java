package com.example.peter.thekitchenmenu.data.repository;

import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.google.firebase.database.annotations.NotNull;

public interface ProductDataSource {

    void saveProduct(@NotNull ProductEntity productEntity);
}
