package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;

import java.util.List;

public interface RecipeDataSource {

    interface LoadAllCallback {

        void onAllLoaded(List<RecipeEntity> recipeEntities);

        void onDataNotAvailable();
    }

    interface GetItemCallback {

        void onItemLoaded(RecipeEntity recipeEntity);

        void onDataNotAvailable();
    }

    void getAll(@NonNull LoadAllCallback callback);

    void getById(@NonNull String recipeId, @NonNull GetItemCallback callback);

    void save(@NonNull RecipeEntity recipeEntity);

    void refresh();

    void deleteAll();

    void deleteById(@NonNull String id);
}
