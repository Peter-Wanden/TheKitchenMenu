package com.example.peter.thekitchenmenu.data.repository.source.local.ingredient;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.dataadapter.IngredientLocalDeleteAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.dataadapter.IngredientLocalGetAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.dataadapter.IngredientLocalSaveAdapter;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientPersistenceModel;

import java.util.List;

import javax.annotation.Nonnull;

public class RepositoryIngredientLocal
        implements DomainDataAccess<IngredientPersistenceModel> {

    private static volatile RepositoryIngredientLocal INSTANCE;

    @Nonnull
    private final IngredientLocalGetAdapter getAdapter;
    @Nonnull
    private final IngredientLocalSaveAdapter saveAdapter;
    @Nonnull
    private final IngredientLocalDeleteAdapter deleteAdapter;

    private RepositoryIngredientLocal(@Nonnull IngredientLocalGetAdapter getAdapter,
                                     @Nonnull IngredientLocalSaveAdapter saveAdapter,
                                     @Nonnull IngredientLocalDeleteAdapter deleteAdapter) {
        this.getAdapter = getAdapter;
        this.saveAdapter = saveAdapter;
        this.deleteAdapter = deleteAdapter;
    }

    public static RepositoryIngredientLocal getInstance(
            @Nonnull IngredientLocalGetAdapter getAdapter,
            @Nonnull IngredientLocalSaveAdapter saveAdapter,
            @Nonnull IngredientLocalDeleteAdapter deleteAdapter) {
        if (INSTANCE == null) {
            synchronized (RepositoryIngredientLocal.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RepositoryIngredientLocal(
                            getAdapter,
                            saveAdapter,
                            deleteAdapter);
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<IngredientPersistenceModel> callback) {
        getAdapter.getByDataId(
                dataId,
                new GetDomainModelCallback<IngredientPersistenceModel>() {
                    @Override
                    public void onPersistenceModelLoaded(IngredientPersistenceModel model) {
                        callback.onPersistenceModelLoaded(model);
                    }

                    @Override
                    public void onPersistenceModelUnavailable() {
                        callback.onPersistenceModelUnavailable();
                    }
                }
        );
    }

    @Override
    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<IngredientPersistenceModel> callback) {
        getAdapter.getActiveByDomainId(
                domainId,
                new GetDomainModelCallback<IngredientPersistenceModel>() {
                    @Override
                    public void onPersistenceModelLoaded(IngredientPersistenceModel model) {
                        callback.onPersistenceModelLoaded(model);
                    }

                    @Override
                    public void onPersistenceModelUnavailable() {
                        callback.onPersistenceModelUnavailable();
                    }
                }
        );
    }

    @Override
    public void getAll(@Nonnull GetAllDomainModelsCallback<IngredientPersistenceModel> callback) {
        getAdapter.getAll(
                new GetAllDomainModelsCallback<IngredientPersistenceModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<IngredientPersistenceModel> models) {
                        callback.onAllDomainModelsLoaded(models);
                    }

                    @Override
                    public void onDomainModelsUnavailable() {
                        callback.onDomainModelsUnavailable();
                    }
                }
        );
    }

    @Override
    public void save(@Nonnull IngredientPersistenceModel model) {
        saveAdapter.save(model);
    }

    @Override
    public void refreshData() {

    }

    @Override
    public void deleteByDataId(String dataId) {
        deleteAdapter.deleteByDataId(dataId);
    }

    @Override
    public void deleteAllByDomainId(@Nonnull String domainId) {
        deleteAdapter.deleteAllByDomainId(domainId);
    }

    @Override
    public void deleteAll() {
        deleteAdapter.deleteAll();
    }
}
