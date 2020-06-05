package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient;

import com.example.peter.thekitchenmenu.data.repository.recipe.DomainDataAccessRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.dataadapter.RecipeIngredientLocalDeleteAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.dataadapter.RecipeIngredientLocalGetAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.dataadapter.RecipeIngredientLocalSaveAdapter;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredientPersistenceModel;

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
            @Nonnull GetDomainModelCallback<RecipeIngredientPersistenceModel> callback) {
        getAdapter.getByDataId(
                dataId,
                new GetDomainModelCallback<RecipeIngredientPersistenceModel>() {
                    @Override
                    public void onDomainModelLoaded(RecipeIngredientPersistenceModel model) {
                        callback.onDomainModelLoaded(model);
                    }

                    @Override
                    public void onDomainModelUnavailable() {
                        callback.onDomainModelUnavailable();
                    }
                });
    }

    @Override
    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeIngredientPersistenceModel> callback) {
        getAdapter.getActiveByDomainId(
                domainId,
                new GetDomainModelCallback<RecipeIngredientPersistenceModel>() {
                    @Override
                    public void onDomainModelLoaded(RecipeIngredientPersistenceModel model) {
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
    public void getAllByRecipeId(
            @Nonnull String recipeId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceModel> callback) {
        getAdapter.getAllByRecipeId(
                recipeId,
                new GetAllDomainModelsCallback<RecipeIngredientPersistenceModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeIngredientPersistenceModel> models) {
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
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceModel> callback) {
        getAdapter.getAllByProductId(
                productId,
                new GetAllDomainModelsCallback<RecipeIngredientPersistenceModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeIngredientPersistenceModel> models) {
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
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceModel> callback) {
        getAdapter.getAllByIngredientId(
                ingredientId,
                new GetAllDomainModelsCallback<RecipeIngredientPersistenceModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeIngredientPersistenceModel> models) {
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
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceModel> callback) {
        getAdapter.getAll(
                new GetAllDomainModelsCallback<RecipeIngredientPersistenceModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeIngredientPersistenceModel> models) {
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
    public void save(@Nonnull RecipeIngredientPersistenceModel model) {
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
