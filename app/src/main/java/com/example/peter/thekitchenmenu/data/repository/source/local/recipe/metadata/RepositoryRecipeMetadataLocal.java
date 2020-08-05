package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter.RecipeMetadataLocalDeleteAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter.RecipeMetadataLocalGetAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter.RecipeMetadataLocalSaveAdapter;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.metadata.RecipeMetadataUseCasePersistenceModel;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Uses data adapters and filters to enable the flow of domain models objects to and from the
 * persistence local persistence framework.
 */
public class RepositoryRecipeMetadataLocal
        implements DomainDataAccess<RecipeMetadataUseCasePersistenceModel> {

    private static volatile RepositoryRecipeMetadataLocal INSTANCE;

    @Nonnull
    private final RecipeMetadataLocalGetAdapter getAdapter;
    @Nonnull
    private final RecipeMetadataLocalSaveAdapter saveAdapter;
    @Nonnull
    private final RecipeMetadataLocalDeleteAdapter deleteAdapter;

    private RepositoryRecipeMetadataLocal(
            @Nonnull RecipeMetadataLocalGetAdapter getAdapter,
            @Nonnull RecipeMetadataLocalSaveAdapter saveAdapter,
            @Nonnull RecipeMetadataLocalDeleteAdapter deleteAdapter) {

        this.getAdapter = getAdapter;
        this.saveAdapter = saveAdapter;
        this.deleteAdapter = deleteAdapter;
    }

    public static RepositoryRecipeMetadataLocal getInstance(
            @Nonnull RecipeMetadataLocalGetAdapter getAdapter,
            @Nonnull RecipeMetadataLocalSaveAdapter saveAdapter,
            @Nonnull RecipeMetadataLocalDeleteAdapter deleteAdapter) {

        if (INSTANCE == null) {
            synchronized (RepositoryRecipeMetadataLocal.class) {
                if (INSTANCE == null)
                    INSTANCE = new RepositoryRecipeMetadataLocal(
                            getAdapter,
                            saveAdapter,
                            deleteAdapter
                    );
            }
        }
        return INSTANCE;
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeMetadataUseCasePersistenceModel> callback) {
        getAdapter.getAllActive(
                new GetAllDomainModelsCallback<RecipeMetadataUseCasePersistenceModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeMetadataUseCasePersistenceModel> models) {
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
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeMetadataUseCasePersistenceModel> callback) {
        getAdapter.getByDataId(
                dataId,
                new GetDomainModelCallback<RecipeMetadataUseCasePersistenceModel>() {
                    @Override
                    public void onPersistenceModelLoaded(@Nonnull RecipeMetadataUseCasePersistenceModel model) {
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
    public void getByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeMetadataUseCasePersistenceModel> callback) {
        getAdapter.getActiveByDomainId(
                domainId,
                new GetDomainModelCallback<RecipeMetadataUseCasePersistenceModel>() {
                    @Override
                    public void onPersistenceModelLoaded(RecipeMetadataUseCasePersistenceModel model) {
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
    public void save(@Nonnull RecipeMetadataUseCasePersistenceModel persistenceModel) {
        saveAdapter.save(persistenceModel);
    }

    @Override
    public void refreshData() {
        // Not required because the {@link RepositoryRecipe} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    @Override
    public void deleteByDataId(String dataId) {
        deleteAdapter.deleteByDataId(dataId);
    }

    @Override
    public void deleteByDomainId(@Nonnull final String domainId) {
        deleteAdapter.deleteAllByDomainId(domainId);
    }

    @Override
    public void deleteAll() {
        deleteAdapter.deleteAll();
    }
}
