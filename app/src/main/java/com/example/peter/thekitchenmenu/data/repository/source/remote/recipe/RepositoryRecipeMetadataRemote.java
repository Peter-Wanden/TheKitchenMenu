package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.invoker.metadata.RecipeMacroMetadataUseCasePersistenceModel;

import javax.annotation.Nonnull;

public class RepositoryRecipeMetadataRemote
        implements DomainDataAccess<RecipeMacroMetadataUseCasePersistenceModel> {

    private static RepositoryRecipeMetadataRemote INSTANCE;

    public static RepositoryRecipeMetadataRemote getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RepositoryRecipeMetadataRemote();
        return INSTANCE;
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeMacroMetadataUseCasePersistenceModel> callback) {
        callback.onDomainModelsUnavailable();

    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeMacroMetadataUseCasePersistenceModel> callback) {
        callback.onPersistenceModelUnavailable();
    }

    @Override
    public void getByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeMacroMetadataUseCasePersistenceModel> callback) {
        callback.onPersistenceModelUnavailable();
    }

    @Override
    public void save(@Nonnull RecipeMacroMetadataUseCasePersistenceModel model) {

    }

    @Override
    public void refreshData() {

    }

    @Override
    public void deleteByDomainId(@Nonnull String domainId) {

    }

    @Override
    public void deleteByDataId(String dataId) {

    }

    @Override
    public void deleteAll() {

    }
}
