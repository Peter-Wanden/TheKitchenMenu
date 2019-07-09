package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.peter.thekitchenmenu.data.entity.TkmEntity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static androidx.core.util.Preconditions.checkNotNull;

public abstract class Repository<T extends TkmEntity> implements DataSource<T> {

    private static final String TAG = "tkm-Repository";

//    public static Repository INSTANCE = null;

    DataSource remoteDataSource;
    DataSource localDataSource;
    private Map<String, T> entityCache;
    private boolean cacheIsDirty;

//    private Repository(@NonNull DataSource<T> remoteDataSource,
//                       @NonNull DataSource<T> localDataSource) {
//
//        this.remoteDataSource = checkNotNull(remoteDataSource);
//        this.localDataSource = checkNotNull(localDataSource);
//    }

//    public static <T extends TkmEntity> Repository<T> getInstance(
//            DataSource<T> remoteDataSource,
//            DataSource<T> localDataSource) {
//
//        if (INSTANCE == null)
//            INSTANCE = new Repository<>(remoteDataSource, localDataSource);
//        return INSTANCE;
//    }

    @Override
    public void getAll(@NonNull GetAllCallback<T> callback) {
        checkNotNull(callback);

        if (entityCache != null && cacheIsDirty) {
            callback.onAllLoaded(new ArrayList<>(entityCache.values()));
            return;
        }
        if (cacheIsDirty)
            getItemsFromRemoteDataSource(callback);
        else {
            //noinspection unchecked
            localDataSource.getAll(new GetAllCallback<T>() {
                @Override
                public void onAllLoaded(List<T> entities) {
                    refreshCache(entities);
                    callback.onAllLoaded(new ArrayList<>(entityCache.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getItemsFromRemoteDataSource(callback);
                }
            });
        }
    }
    private void getItemsFromRemoteDataSource(@NonNull final GetAllCallback<T> callback) {
        //noinspection unchecked
        remoteDataSource.getAll(new GetAllCallback<T>() {
            @Override
            public void onAllLoaded(List<T> entities) {
                refreshCache(entities);
                refreshLocalDataSource(entities);
                callback.onAllLoaded(new ArrayList<>(entityCache.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<T> entities) {
        if (entityCache == null)
            entityCache = new LinkedHashMap<>();

        entityCache.clear();

        for (T entity : entities)
            entityCache.put(entity.getId(), entity);

        cacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<T> entities) {
        localDataSource.deleteAll();

        for (T entity : entities)
            //noinspection unchecked
            localDataSource.save(entity);
    }

    @Override
    public void getById(@NonNull String id, @NonNull GetEntityCallback<T> callback) {
        checkNotNull(id);
        checkNotNull(callback);

        T cachedEntity = getEntityWithId(id);

        if (cachedEntity != null) {
            callback.onEntityLoaded(cachedEntity);
            return;
        }
        //noinspection unchecked
        localDataSource.getById(id, new GetEntityCallback<T>() {
            @Override
            public void onEntityLoaded(T entity) {
                if (entityCache == null)
                    entityCache = new LinkedHashMap<>();

                entityCache.put(entity.getId(), entity);
                callback.onEntityLoaded(entity);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Nullable
    private T getEntityWithId(String id) {
        checkNotNull(id);

        if (entityCache == null || entityCache.isEmpty())
            return null;
        else
            return entityCache.get(id);
    }

    @Override
    public void save(@NonNull T entity) {
        checkNotNull(entity);
        //noinspection unchecked
        remoteDataSource.save(entity);
        //noinspection unchecked
        localDataSource.save(entity);

        if (entityCache == null)
            entityCache = new LinkedHashMap<>();
        entityCache.put(entity.getId(), entity);
    }

    @Override
    public void refreshData() {
        cacheIsDirty = true;
    }

    @Override
    public void deleteAll() {
        remoteDataSource.deleteAll();
        localDataSource.deleteAll();

        if (entityCache == null)
            entityCache = new LinkedHashMap<>();
        entityCache.clear();
    }

    @Override
    public void deleteById(@NonNull String id) {
        remoteDataSource.deleteById(id);
        localDataSource.deleteById(id);
        entityCache.remove(id);
    }
}




























