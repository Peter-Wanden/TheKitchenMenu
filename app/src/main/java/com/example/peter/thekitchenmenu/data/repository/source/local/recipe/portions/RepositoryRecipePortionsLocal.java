package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions;

import com.example.peter.thekitchenmenu.data.repository.recipe.DomainDataAccessRecipePortions;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.dataadapter.PortionsLocalDeleteAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.dataadapter.PortionsLocalGetAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.dataadapter.PortionsLocalSaveAdapter;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.portions.RecipePortionsUseCasePersistenceModel;

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
            @Nonnull GetDomainModelCallback<RecipePortionsUseCasePersistenceModel> callback) {
        getAdapter.getByDataId(
                dataId,
                new GetDomainModelCallback<RecipePortionsUseCasePersistenceModel>() {
                    @Override
                    public void onPersistenceModelLoaded(RecipePortionsUseCasePersistenceModel model) {
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
    public void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipePortionsUseCasePersistenceModel> callback) {
        getAdapter.getAllByDomainId(
                domainId,
                new GetAllDomainModelsCallback<RecipePortionsUseCasePersistenceModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipePortionsUseCasePersistenceModel> models) {
                        callback.onAllDomainModelsLoaded(models);
                    }

                    @Override
                    public void onDomainModelsUnavailable() {
                        callback.onDomainModelsUnavailable();
                    }
                });
    }

    @Override
    public void getByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipePortionsUseCasePersistenceModel> callback) {
        getAdapter.getActiveByDomainId(
                domainId, new GetDomainModelCallback<RecipePortionsUseCasePersistenceModel>() {
                    @Override
                    public void onPersistenceModelLoaded(RecipePortionsUseCasePersistenceModel model) {
                        callback.onPersistenceModelLoaded(model);
                    }

                    @Override
                    public void onPersistenceModelUnavailable() {
                        callback.onPersistenceModelUnavailable();
                    }
                });
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipePortionsUseCasePersistenceModel> callback) {
        getAdapter.getAll(
                new GetAllDomainModelsCallback<RecipePortionsUseCasePersistenceModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipePortionsUseCasePersistenceModel> models) {
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
    public void save(@Nonnull RecipePortionsUseCasePersistenceModel model) {
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
    public void deleteByDomainId(@Nonnull String domainId) {
        deleteAdapter.deleteAllByDomainId(domainId);
    }

    @Override
    public void deleteAll() {
        deleteAdapter.deleteAll();
    }
}
