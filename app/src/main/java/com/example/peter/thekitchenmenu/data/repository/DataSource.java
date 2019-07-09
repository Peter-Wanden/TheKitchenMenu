package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.data.entity.TkmEntity;

import java.util.List;

public interface DataSource<T extends TkmEntity> {

    interface GetAllCallback<E extends TkmEntity> {

        void onAllLoaded(List<E> entities);

        void onDataNotAvailable();
    }

    interface GetEntityCallback<T> {

        void onEntityLoaded(T object);

        void onDataNotAvailable();
    }

    void getAll(@NonNull GetAllCallback<T> callback);

    void getById(@NonNull String id, @NonNull GetEntityCallback<T> callback);

    void save(@NonNull T object);

    void refreshData();

    void deleteAll();

    void deleteById(@NonNull String id);
}
