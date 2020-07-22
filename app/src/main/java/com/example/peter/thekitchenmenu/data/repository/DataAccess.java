package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.Nullable;

import com.example.peter.thekitchenmenu.domain.model.DomainModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;


public abstract class DataAccess<T extends DomainModel.PersistenceModel>
        implements DomainDataAccess<T> {

    protected DomainDataAccess<T> remoteDomainDataAccess;
    protected DomainDataAccess<T> localDomainDataAccess;
    protected Map<String, T> cache;

    private boolean cacheIsDirty;

    @Override
    public void getAll(@Nonnull GetAllDomainModelsCallback<T> callback) {

        if (cache != null && cacheIsDirty) {
            callback.onAllDomainModelsLoaded(new ArrayList<>(cache.values()));
            return;
        }
        if (cacheIsDirty) {
            getItemsFromRemoteDataSource(callback);
        } else {
            localDomainDataAccess.getAll(new GetAllDomainModelsCallback<T>() {
                @Override
                public void onAllDomainModelsLoaded(List<T> models) {
                    refreshCache(models);
                    callback.onAllDomainModelsLoaded(new ArrayList<>(cache.values()));
                }

                @Override
                public void onDomainModelsUnavailable() {
                    getItemsFromRemoteDataSource(callback);
                }
            });
        }
    }

    @Override
    public void getByDataId(@Nonnull String dataId,
                            @Nonnull GetDomainModelCallback<T> callback) {

        T cachedModel = getFromCacheModelWithDataId(dataId);

        if (cachedModel != null) {
            callback.onPersistenceModelLoaded(cachedModel);
            return;
        }
        localDomainDataAccess.getByDataId(dataId, new GetDomainModelCallback<T>() {
            @Override
            public void onPersistenceModelLoaded(T model) {
                if (cache == null) {
                    cache = new LinkedHashMap<>();
                }
                cache.put(model.getDataId(), model);
                callback.onPersistenceModelLoaded(model);
            }

            @Override
            public void onPersistenceModelUnavailable() {
                remoteDomainDataAccess.getByDataId(dataId, new GetDomainModelCallback<T>() {
                    @Override
                    public void onPersistenceModelLoaded(T model) {
                        if (model == null) {
                            callback.onPersistenceModelUnavailable();
                            return;
                        }
                        if (cache == null) {
                            cache = new LinkedHashMap<>();
                        }
                        cache.put(model.getDataId(), model);
                        callback.onPersistenceModelLoaded(model);
                    }

                    @Override
                    public void onPersistenceModelUnavailable() {
                        callback.onPersistenceModelUnavailable();
                    }
                });
            }
        });
    }

    @Nullable
    private T getFromCacheModelWithDataId(String id) {
        if (cache == null || cache.isEmpty()) {
            return null;
        } else {
            return cache.get(id);
        }
    }

    @Override
    public void getActiveByDomainId(@Nonnull String domainId,
                                    @Nonnull GetDomainModelCallback<T> callback) {
        T cachedModel = getFromCacheModelWithDomainId(domainId);

        if (cachedModel != null) {
            callback.onPersistenceModelLoaded(cachedModel);
            return;
        }
        localDomainDataAccess.getActiveByDomainId(domainId, new GetDomainModelCallback<T>() {
            @Override
            public void onPersistenceModelLoaded(T model) {
                if (cache == null) {
                    cache = new LinkedHashMap<>();
                }
                cache.put(model.getDataId(), model);
                callback.onPersistenceModelLoaded(model);
            }

            @Override
            public void onPersistenceModelUnavailable() {
                remoteDomainDataAccess.getActiveByDomainId(
                        domainId,
                        new GetDomainModelCallback<T>() {
                    @Override
                    public void onPersistenceModelLoaded(T model) {
                        if (model == null) {
                            callback.onPersistenceModelUnavailable();
                            return;
                        }
                        if (cache == null) {
                            cache = new LinkedHashMap<>();
                        }
                        cache.put(model.getDataId(), model);
                        callback.onPersistenceModelLoaded(model);
                    }

                    @Override
                    public void onPersistenceModelUnavailable() {
                        callback.onPersistenceModelUnavailable();
                    }
                });
            }
        });
    }

    private T getFromCacheModelWithDomainId(String domainId) {
        T thisModel = null;
        if (cache == null || cache.isEmpty()) {
            return null;
        } else {
            for (T thatModel : cache.values()) {
                if (domainId.equals(thatModel.getDomainId())) {
                    thisModel = thatModel;
                }
            }
            return thisModel;
        }
    }

    @Override
    public void save(@Nonnull T model) {
        remoteDomainDataAccess.save(model);
        localDomainDataAccess.save(model);

        if (cache == null) {
            cache = new LinkedHashMap<>();
        }
        cache.put(model.getDataId(), model);
    }

    @Override
    public void refreshData() {
        cacheIsDirty = true;
    }


    @Override
    public void deleteByDataId(@Nonnull String dataId) {
        remoteDomainDataAccess.deleteByDataId(dataId);
        localDomainDataAccess.deleteByDataId(dataId);
        cache.remove(dataId);
    }

    @Override
    public void deleteAllByDomainId(@Nonnull String domainId) {
        remoteDomainDataAccess.deleteAllByDomainId(domainId);
        localDomainDataAccess.deleteAllByDomainId(domainId);

        Iterator<Map.Entry<String, T>> cacheIterator = cache.entrySet().iterator();

        while (cacheIterator.hasNext()) {
            Map.Entry<String, T> currentItem = cacheIterator.next();
            if (domainId.equals(currentItem.getValue().getDomainId())) {
                cacheIterator.remove();
            }
        }
    }

    @Override
    public void deleteAll() {
        remoteDomainDataAccess.deleteAll();
        localDomainDataAccess.deleteAll();

        if (cache == null) {
            cache = new LinkedHashMap<>();
        }
        cache.clear();
    }

    private void getItemsFromRemoteDataSource(@Nonnull final GetAllDomainModelsCallback<T> callback) {
        remoteDomainDataAccess.getAll(new GetAllDomainModelsCallback<T>() {
            @Override
            public void onAllDomainModelsLoaded(List<T> models) {
                refreshCache(models);
                refreshLocalDataSource(models);
                callback.onAllDomainModelsLoaded(new ArrayList<>(cache.values()));
            }

            @Override
            public void onDomainModelsUnavailable() {
                callback.onDomainModelsUnavailable();
            }
        });
    }

    private void refreshCache(List<T> models) {
        if (cache == null) {
            cache = new LinkedHashMap<>();
        }

        cache.clear();

        for (T model : models) {
            cache.put(model.getDataId(), model);
        }

        cacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<T> models) {
        localDomainDataAccess.deleteAll();

        for (T model : models) {
            localDomainDataAccess.save(model);
        }
    }
}