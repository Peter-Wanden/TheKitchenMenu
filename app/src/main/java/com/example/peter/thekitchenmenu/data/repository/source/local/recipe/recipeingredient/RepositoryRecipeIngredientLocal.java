package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient;

import com.example.peter.thekitchenmenu.data.repository.recipe.DomainDataAccessRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.dataadapter.RecipeIngredientLocalDeleteAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.dataadapter.RecipeIngredientLocalGetAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.dataadapter.RecipeIngredientLocalSaveAdapter;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredientPersistenceDomainModel;

import java.util.List;

import javax.annotation.Nonnull;

public class RepositoryRecipeIngredientLocal
        implements DomainDataAccessRecipeIngredient {

    private static volatile RepositoryRecipeIngredientLocal INSTANCE;

    @Nonnull
    private final RecipeIngredientLocalGetAdapter getAdapter;
    @Nonnull
    private final RecipeIngredientLocalSaveAdapter saveAdapter;
    @Nonnull
    private final RecipeIngredientLocalDeleteAdapter deleteAdapter;

    private RepositoryRecipeIngredientLocal(
            @Nonnull RecipeIngredientLocalGetAdapter getAdapter,
            @Nonnull RecipeIngredientLocalSaveAdapter saveAdapter,
            @Nonnull RecipeIngredientLocalDeleteAdapter deleteAdapter) {
        this.getAdapter = getAdapter;
        this.saveAdapter = saveAdapter;
        this.deleteAdapter = deleteAdapter;
    }

    public static RepositoryRecipeIngredientLocal getInstance(
            @Nonnull RecipeIngredientLocalGetAdapter getAdapter,
            @Nonnull RecipeIngredientLocalSaveAdapter saveAdapter,
            @Nonnull RecipeIngredientLocalDeleteAdapter deleteAdapter) {
        if (INSTANCE == null) {
            synchronized (RepositoryRecipeIngredientLocal.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RepositoryRecipeIngredientLocal(
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
            @Nonnull GetDomainModelCallback<RecipeIngredientPersistenceDomainModel> callback) {
        getAdapter.getByDataId(
                dataId,
                new GetDomainModelCallback<RecipeIngredientPersistenceDomainModel>() {
                    @Override
                    public void onPersistenceModelLoaded(RecipeIngredientPersistenceDomainModel model) {
                        callback.onPersistenceModelLoaded(model);
                    }

                    @Override
                    public void onPersistenceModelUnavailable() {
                        callback.onPersistenceModelUnavailable();
                    }
                });
    }

    @Override
    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeIngredientPersistenceDomainModel> callback) {
        getAdapter.getActiveByDomainId(
                domainId,
                new GetDomainModelCallback<RecipeIngredientPersistenceDomainModel>() {
                    @Override
                    public void onPersistenceModelLoaded(RecipeIngredientPersistenceDomainModel model) {
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
    public void getAllByRecipeId(
            @Nonnull String recipeId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceDomainModel> callback) {
        getAdapter.getAllByRecipeId(
                recipeId,
                new GetAllDomainModelsCallback<RecipeIngredientPersistenceDomainModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeIngredientPersistenceDomainModel> models) {
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
    public void getAllByProductId(
            @Nonnull String productId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceDomainModel> callback) {
        getAdapter.getAllByProductId(
                productId,
                new GetAllDomainModelsCallback<RecipeIngredientPersistenceDomainModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeIngredientPersistenceDomainModel> models) {
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
    public void getAllByIngredientId(
            @Nonnull String ingredientId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceDomainModel> callback) {
        getAdapter.getAllByIngredientId(
                ingredientId,
                new GetAllDomainModelsCallback<RecipeIngredientPersistenceDomainModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeIngredientPersistenceDomainModel> models) {
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
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceDomainModel> callback) {
        getAdapter.getAll(
                new GetAllDomainModelsCallback<RecipeIngredientPersistenceDomainModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeIngredientPersistenceDomainModel> models) {
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
    public void save(@Nonnull RecipeIngredientPersistenceDomainModel model) {
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
