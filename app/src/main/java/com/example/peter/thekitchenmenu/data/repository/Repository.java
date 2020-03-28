package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.Nullable;

import com.example.peter.thekitchenmenu.domain.model.PersistenceModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;


public abstract class Repository<T extends PersistenceModel> implements DataSource<T> {

    protected static Repository INSTANCE = null;
    protected DataSource<T> remoteDataSource;
    protected DataSource<T> localDataSource;
    protected Map<String, T> cache;

    private boolean cacheIsDirty;

    @Override
    public void getAll(@Nonnull GetAllCallback<T> callback) {

        if (cache != null && cacheIsDirty) {
            callback.onAllLoaded(new ArrayList<>(cache.values()));
            return;
        }
        if (cacheIsDirty)
            getItemsFromRemoteDataSource(callback);
        else {
            localDataSource.getAll(new GetAllCallback<T>() {
                @Override
                public void onAllLoaded(List<T> models) {
                    refreshCache(models);
                    callback.onAllLoaded(new ArrayList<>(cache.values()));
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
            public void onAllLoaded(List<T> models) {
                refreshCache(models);
                refreshLocalDataSource(models);
                callback.onAllLoaded(new ArrayList<>(cache.values()));
            }

            @Override
            public void onDataUnavailable() {
                callback.onDataUnavailable();
            }
        });
    }

    private void refreshCache(List<T> models) {
        if (cache == null)
            cache = new LinkedHashMap<>();

        cache.clear();

        for (T model : models)
            cache.put(model.getDataId(), model);

        cacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<T> models) {
        localDataSource.deleteAll();

        for (T model : models)
            localDataSource.save(model);
    }

    @Override
    public void getById(@Nonnull String id, @Nonnull GetModelCallback<T> callback) {

        T cachedModel = getModelWithId(id);

        if (cachedModel != null) {
            callback.onModelLoaded(cachedModel);
            return;
        }
        localDataSource.getById(id, new GetModelCallback<T>() {
            @Override
            public void onModelLoaded(T model) {
                if (cache == null)
                    cache = new LinkedHashMap<>();

                cache.put(model.getDataId(), model);
                callback.onModelLoaded(model);
            }

            @Override
            public void onModelUnavailable() {
                remoteDataSource.getById(id, new GetModelCallback<T>() {
                    @Override
                    public void onModelLoaded(T model) {
                        if (model == null) {
                            callback.onModelUnavailable();
                            return;
                        }

                        if (cache == null)
                            cache = new LinkedHashMap<>();

                        cache.put(model.getDataId(), model);
                        callback.onModelLoaded(model);
                    }

                    @Override
                    public void onModelUnavailable() {
                        callback.onModelUnavailable();
                    }
                });
            }
        });
    }

    @Nullable
    private T getModelWithId(String id) {

        if (cache == null || cache.isEmpty())
            return null;
        else
            return cache.get(id);
    }

    @Override
    public void save(@Nonnull T model) {
        remoteDataSource.save(model);
        localDataSource.save(model);

        if (cache == null)
            cache = new LinkedHashMap<>();
        cache.put(model.getDataId(), model);
    }

    @Override
    public void refreshData() {
        cacheIsDirty = true;
    }

    @Override
    public void deleteAll() {
        remoteDataSource.deleteAll();
        localDataSource.deleteAll();

        if (cache == null)
            cache = new LinkedHashMap<>();
        cache.clear();
    }

    @Override
    public void deleteById(@Nonnull String id) {
        remoteDataSource.deleteById(id);
        localDataSource.deleteById(id);
        cache.remove(id);
    }
}