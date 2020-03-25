package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.Nullable;

import com.example.peter.thekitchenmenu.data.primitivemodel.PrimitiveModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;


public abstract class Repository<T extends PrimitiveModel> implements PrimitiveDataSource<T> {

    protected static Repository INSTANCE = null;
    protected PrimitiveDataSource<T> remoteDataSource;
    protected PrimitiveDataSource<T> localDataSource;
    protected Map<String, T> entityCache;

    private boolean cacheIsDirty;

    @Override
    public void getAll(@Nonnull GetAllCallback<T> callback) {

        if (entityCache != null && cacheIsDirty) {
            callback.onAllLoaded(new ArrayList<>(entityCache.values()));
            return;
        }
        if (cacheIsDirty)
            getItemsFromRemoteDataSource(callback);
        else {
            localDataSource.getAll(new GetAllCallback<T>() {
                @Override
                public void onAllLoaded(List<T> entities) {
                    refreshCache(entities);
                    callback.onAllLoaded(new ArrayList<>(entityCache.values()));
                }

                @Override
                public void onDataUnavailable() {
                    getItemsFromRemoteDataSource(callback);
                }
            });
        }
    }
    private void getItemsFromRemoteDataSource(@Nonnull final GetAllCallback<T> callback) {
        remoteDataSource.getAll(new GetAllCallback<T>() {
            @Override
            public void onAllLoaded(List<T> entities) {
                refreshCache(entities);
                refreshLocalDataSource(entities);
                callback.onAllLoaded(new ArrayList<>(entityCache.values()));
            }

            @Override
            public void onDataUnavailable() {
                callback.onDataUnavailable();
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
            localDataSource.save(entity);
    }

    @Override
    public void getById(@Nonnull String id, @Nonnull GetEntityCallback<T> callback) {

        T cachedEntity = getEntityWithId(id);

        if (cachedEntity != null) {
            callback.onEntityLoaded(cachedEntity);
            return;
        }
        localDataSource.getById(id, new GetEntityCallback<T>() {
            @Override
            public void onEntityLoaded(T entity) {
                if (entityCache == null)
                    entityCache = new LinkedHashMap<>();

                entityCache.put(entity.getId(), entity);
                callback.onEntityLoaded(entity);
            }

            @Override
            public void onDataUnavailable() {
                remoteDataSource.getById(id, new GetEntityCallback<T>() {
                    @Override
                    public void onEntityLoaded(T entity) {
                        if (entity == null) {
                            onDataUnavailable();
                            return;
                        }

                        if (entityCache == null)
                            entityCache = new LinkedHashMap<>();

                        entityCache.put(entity.getId(), entity);
                        callback.onEntityLoaded(entity);
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDataUnavailable();
                    }
                });
            }
        });
    }

    @Nullable
    private T getEntityWithId(String id) {

        if (entityCache == null || entityCache.isEmpty())
            return null;
        else
            return entityCache.get(id);
    }

    @Override
    public void save(@Nonnull T entity) {
        remoteDataSource.save(entity);
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
    public void deleteById(@Nonnull String id) {
        remoteDataSource.deleteById(id);
        localDataSource.deleteById(id);
        entityCache.remove(id);
    }
}