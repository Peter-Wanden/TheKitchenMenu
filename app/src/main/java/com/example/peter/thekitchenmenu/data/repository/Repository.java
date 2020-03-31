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
    public void getAll(@Nonnull GetAllDomainModelsCallback<T> callback) {

        if (cache != null && cacheIsDirty) {
            callback.onAllLoaded(new ArrayList<>(cache.values()));
            return;
        }
        if (cacheIsDirty)
            getItemsFromRemoteDataSource(callback);
        else {
            localDataSource.getAll(new GetAllDomainModelsCallback<T>() {
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

    private void getItemsFromRemoteDataSource(@Nonnull final GetAllDomainModelsCallback<T> callback) {
        remoteDataSource.getAll(new GetAllDomainModelsCallback<T>() {
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
    public void getByDataId(@Nonnull String dataId, @Nonnull GetDomainModelCallback<T> callback) {

        T cachedModel = getModelWithId(dataId);

        if (cachedModel != null) {
            callback.onModelLoaded(cachedModel);
            return;
        }
        localDataSource.getByDataId(dataId, new GetDomainModelCallback<T>() {
            @Override
            public void onModelLoaded(T model) {
                if (cache == null)
                    cache = new LinkedHashMap<>();

                cache.put(model.getDataId(), model);
                callback.onModelLoaded(model);
            }

            @Override
            public void onModelUnavailable() {
                remoteDataSource.getByDataId(dataId, new GetDomainModelCallback<T>() {
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

    public void deleteByDataId(@Nonnull String dataId) {
        remoteDataSource.deleteById(dataId);
        localDataSource.deleteById(dataId);
        cache.remove(dataId);
    }
}