package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import java.util.List;

public interface DataSource<T> {

    interface LoadAllCallback<T> {

        void onAllLoaded(List<T> entities);

        void onDataNotAvailable();
    }

    interface GetEntityCallback<T> {

        void onEntityLoaded(T object);

        void onDataNotAvailable();
    }

    void getAll(@NonNull LoadAllCallback<T> callback);

    void getById(@NonNull String id, @NonNull GetEntityCallback<T> callback);

    void save(@NonNull T object);

    void refreshData();

    void deleteAll();

    void deleteById(@NonNull String id);
}
