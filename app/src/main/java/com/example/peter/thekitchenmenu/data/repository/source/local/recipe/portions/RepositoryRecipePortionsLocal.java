package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions;

import com.example.peter.thekitchenmenu.data.repository.recipe.DomainDataAccessRecipePortions;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.dataadapter.PortionsLocalDeleteAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.dataadapter.PortionsLocalGetAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.dataadapter.PortionsLocalSaveAdapter;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsPersistenceModel;

import java.util.List;

import javax.annotation.Nonnull;

public class RepositoryRecipePortionsLocal
        implements DomainDataAccessRecipePortions {

    public static volatile RepositoryRecipePortionsLocal INSTANCE;

    @Nonnull
    private final PortionsLocalGetAdapter getAdapter;
    @Nonnull
    private final PortionsLocalSaveAdapter saveAdapter;
    @Nonnull
    private final PortionsLocalDeleteAdapter deleteAdapter;

    private RepositoryRecipePortionsLocal(@Nonnull PortionsLocalGetAdapter getAdapter,
                                          @Nonnull PortionsLocalSaveAdapter saveAdapter,
                                          @Nonnull PortionsLocalDeleteAdapter deleteAdapter) {
        this.getAdapter = getAdapter;
        this.saveAdapter = saveAdapter;
        this.deleteAdapter = deleteAdapter;
    }

    public static RepositoryRecipePortionsLocal getInstance(
            @Nonnull PortionsLocalGetAdapter getAdapter,
            @Nonnull PortionsLocalSaveAdapter saveAdapter,
            @Nonnull PortionsLocalDeleteAdapter deleteAdapter) {

        if (INSTANCE == null) {
            synchronized (RepositoryRecipePortionsLocal.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RepositoryRecipePortionsLocal(
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
            @Nonnull GetDomainModelCallback<RecipePortionsPersistenceModel> callback) {
        getAdapter.getByDataId(
                dataId,
                new GetDomainModelCallback<RecipePortionsPersistenceModel>() {
                    @Override
                    public void onDomainModelLoaded(RecipePortionsPersistenceModel model) {
                        callback.onDomainModelLoaded(model);
                    }

                    @Override
                    public void onDomainModelUnavailable() {
                        callback.onDomainModelUnavailable();
                    }
                }
        );
    }

    @Override
    public void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipePortionsPersistenceModel> callback) {
        getAdapter.getAllByDomainId(
                domainId,
                new GetAllDomainModelsCallback<RecipePortionsPersistenceModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipePortionsPersistenceModel> models) {
                        callback.onAllDomainModelsLoaded(models);
                    }

                    @Override
                    public void onDomainModelsUnavailable() {
                        callback.onDomainModelsUnavailable();
                    }
                });
    }

    @Override
    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipePortionsPersistenceModel> callback) {
        getAdapter.getActiveByDomainId(
                domainId, new GetDomainModelCallback<RecipePortionsPersistenceModel>() {
                    @Override
                    public void onDomainModelLoaded(RecipePortionsPersistenceModel model) {
                        callback.onDomainModelLoaded(model);
                    }

                    @Override
                    public void onDomainModelUnavailable() {
                        callback.onDomainModelUnavailable();
                    }
                });
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipePortionsPersistenceModel> callback) {
        getAdapter.getAll(
                new GetAllDomainModelsCallback<RecipePortionsPersistenceModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipePortionsPersistenceModel> models) {
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
    public void save(@Nonnull RecipePortionsPersistenceModel model) {
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
