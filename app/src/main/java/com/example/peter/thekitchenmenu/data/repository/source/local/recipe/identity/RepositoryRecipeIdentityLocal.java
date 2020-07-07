package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.dataadapter.IdentityLocalDeleteAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.dataadapter.IdentityLocalGetAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.dataadapter.IdentityLocalSaveAdapter;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityPersistenceDomainModel;

import java.util.List;

import javax.annotation.Nonnull;

public class RepositoryRecipeIdentityLocal
        implements DomainDataAccess<RecipeIdentityPersistenceDomainModel> {

    private static volatile RepositoryRecipeIdentityLocal INSTANCE;

    @Nonnull
    private final IdentityLocalGetAdapter getAdapter;
    @Nonnull
    private final IdentityLocalSaveAdapter saveAdapter;
    @Nonnull
    private final IdentityLocalDeleteAdapter deleteAdapter;

    private RepositoryRecipeIdentityLocal(@Nonnull IdentityLocalGetAdapter getAdapter,
                                          @Nonnull IdentityLocalSaveAdapter saveAdapter,
                                          @Nonnull IdentityLocalDeleteAdapter deleteAdapter) {
        this.getAdapter = getAdapter;
        this.saveAdapter = saveAdapter;
        this.deleteAdapter = deleteAdapter;
    }

    public static RepositoryRecipeIdentityLocal getInstance(
            @Nonnull IdentityLocalGetAdapter getAdapter,
            @Nonnull IdentityLocalSaveAdapter saveAdapter,
            @Nonnull IdentityLocalDeleteAdapter deleteAdapter) {

        if (INSTANCE == null) {
            synchronized (RepositoryRecipeIdentityLocal.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RepositoryRecipeIdentityLocal(
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
            @Nonnull GetDomainModelCallback<RecipeIdentityPersistenceDomainModel> callback) {
        getAdapter.getByDataId(
                dataId,
                new GetDomainModelCallback<RecipeIdentityPersistenceDomainModel>() {
                    @Override
                    public void onPersistenceModelLoaded(RecipeIdentityPersistenceDomainModel model) {
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
            @Nonnull GetDomainModelCallback<RecipeIdentityPersistenceDomainModel> callback) {
        getAdapter.getActiveByDomainId(
                domainId,
                new GetDomainModelCallback<RecipeIdentityPersistenceDomainModel>() {
                    @Override
                    public void onPersistenceModelLoaded(RecipeIdentityPersistenceDomainModel model) {
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
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeIdentityPersistenceDomainModel> callback) {
        getAdapter.getAll(
                new GetAllDomainModelsCallback<RecipeIdentityPersistenceDomainModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeIdentityPersistenceDomainModel> models) {
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
    public void save(@Nonnull RecipeIdentityPersistenceDomainModel model) {
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
