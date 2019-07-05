package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;

import java.util.ArrayList;
import java.util.List;

public interface DataSource<T> {

    interface LoadAllCallback {

        <E> void onAllLoaded(List<E> entities);

        void onDataNotAvailable();
    }

    interface GetItemCallback<T> {

        void onItemLoaded(T Object);

        void onDataNotAvailable();
    }

    void getAll(@NonNull LoadAllCallback callback);

    void getById(@NonNull String id, @NonNull GetItemCallback callback);

    void save(@NonNull T object);

    void refreshData();

    void deleteAll();

    void deleteById(@NonNull String id);
}
