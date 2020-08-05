package com.example.peter.thekitchenmenu.data.repository;

import androidx.annotation.Nullable;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;


public abstract class DataAccess<PERSISTENCE_MODEL extends DomainModel.PersistenceModel>
        implements DomainDataAccess<PERSISTENCE_MODEL> {

    protected DomainDataAccess<PERSISTENCE_MODEL> remoteDomainDataAccess;
    protected DomainDataAccess<PERSISTENCE_MODEL> localDomainDataAccess;
    protected Map<String, PERSISTENCE_MODEL> cache;

    private boolean cacheIsDirty;

    @Override
    public void getAll(@Nonnull GetAllDomainModelsCallback<PERSISTENCE_MODEL> callback) {

        if (cache != null && cacheIsDirty) {
            callback.onAllDomainModelsLoaded(new ArrayList<>(cache.values()));
            return;
        }
        if (cacheIsDirty) {
            getItemsFromRemoteDataSource(callback);
        } else {
            localDomainDataAccess.getAll(new GetAllDomainModelsCallback<PERSISTENCE_MODEL>() {
                @Override
                public void onAllDomainModelsLoaded(List<PERSISTENCE_MODEL> models) {
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
                            @Nonnull GetDomainModelCallback<PERSISTENCE_MODEL> callback) {

        PERSISTENCE_MODEL cachedModel = getFromCacheModelWithDataId(dataId);

        if (cachedModel != null) {
            callback.onPersistenceModelLoaded(cachedModel);
            return;
        }
        localDomainDataAccess.getByDataId(
                dataId,
                new GetDomainModelCallback<PERSISTENCE_MODEL>() {
            @Override
            public void onPersistenceModelLoaded(PERSISTENCE_MODEL model) {
                if (cache == null) {
                    cache = new LinkedHashMap<>();
                }
                cache.put(model.getDataId(), model);
                callback.onPersistenceModelLoaded(model);
            }

            @Override
            public void onPersistenceModelUnavailable() {
                remoteDomainDataAccess.getByDataId(
                        dataId,
                        new GetDomainModelCallback<PERSISTENCE_MODEL>() {
                    @Override
                    public void onPersistenceModelLoaded(PERSISTENCE_MODEL model) {
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
    private PERSISTENCE_MODEL getFromCacheModelWithDataId(String id) {
        if (cache == null || cache.isEmpty()) {
            return null;
        } else {
            return cache.get(id);
        }
    }

    @Override
    public void getByDomainId(@Nonnull String domainId,
                              @Nonnull GetDomainModelCallback<PERSISTENCE_MODEL> callback) {
        PERSISTENCE_MODEL cachedModel = getFromCacheModelWithDomainId(domainId);

        if (cachedModel != null) {
            callback.onPersistenceModelLoaded(cachedModel);
            return;
        }
        localDomainDataAccess.getByDomainId(
                domainId,
                new GetDomainModelCallback<PERSISTENCE_MODEL>() {
            @Override
            public void onPersistenceModelLoaded(PERSISTENCE_MODEL model) {
                if (cache == null) {
                    cache = new LinkedHashMap<>();
                }
                cache.put(model.getDataId(), model);
                callback.onPersistenceModelLoaded(model);
            }

            @Override
            public void onPersistenceModelUnavailable() {
                remoteDomainDataAccess.getByDomainId(
                        domainId,
                        new GetDomainModelCallback<PERSISTENCE_MODEL>() {
                    @Override
                    public void onPersistenceModelLoaded(PERSISTENCE_MODEL model) {
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

    private PERSISTENCE_MODEL getFromCacheModelWithDomainId(String domainId) {
        PERSISTENCE_MODEL thisModel = null;
        if (cache == null || cache.isEmpty()) {
            return null;
        } else {
            for (PERSISTENCE_MODEL thatModel : cache.values()) {
                if (domainId.equals(thatModel.getDomainId())) {
                    thisModel = thatModel;
                }
            }
            return thisModel;
        }
    }

    @Override
    public void save(@Nonnull PERSISTENCE_MODEL model) {
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
    public void deleteByDomainId(@Nonnull String domainId) {
        remoteDomainDataAccess.deleteByDomainId(domainId);
        localDomainDataAccess.deleteByDomainId(domainId);

        Iterator<Map.Entry<String, PERSISTENCE_MODEL>> cacheIterator = cache.entrySet().iterator();

        while (cacheIterator.hasNext()) {
            Map.Entry<String, PERSISTENCE_MODEL> currentItem = cacheIterator.next();
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

    private void getItemsFromRemoteDataSource(
            @Nonnull final GetAllDomainModelsCallback<PERSISTENCE_MODEL> callback) {
        remoteDomainDataAccess.getAll(new GetAllDomainModelsCallback<PERSISTENCE_MODEL>() {
            @Override
            public void onAllDomainModelsLoaded(List<PERSISTENCE_MODEL> models) {
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

    private void refreshCache(List<PERSISTENCE_MODEL> models) {
        if (cache == null) {
            cache = new LinkedHashMap<>();
        }

        cache.clear();

        for (PERSISTENCE_MODEL model : models) {
            cache.put(model.getDataId(), model);
        }

        cacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<PERSISTENCE_MODEL> models) {
        localDomainDataAccess.deleteAll();

        for (PERSISTENCE_MODEL model : models) {
            localDomainDataAccess.save(model);
        }
    }
}