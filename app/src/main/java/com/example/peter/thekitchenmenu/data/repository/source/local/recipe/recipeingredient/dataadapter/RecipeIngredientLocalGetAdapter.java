package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetAllDomainModelsCallback;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.datasource.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.datasource.RecipeIngredientLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredientPersistenceModel;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeIngredientLocalGetAdapter {

    @Nonnull
    private final RecipeIngredientLocalDataSource dataSource;
    @Nonnull
    private final RecipeIngredientModelConverterParent converter;

    public RecipeIngredientLocalGetAdapter(@Nonnull RecipeIngredientLocalDataSource dataSource) {
        this.dataSource = dataSource;
        converter = new RecipeIngredientModelConverterParent();
    }

    public void getByDataId(
            @Nonnull String dataId,
            GetDomainModelCallback<RecipeIngredientPersistenceModel> callback) {
        dataSource.getByDataId(
                dataId,
                new GetPrimitiveCallback<RecipeIngredientEntity>() {
                    @Override
                    public void onEntityLoaded(RecipeIngredientEntity entity) {
                        callback.onDomainModelLoaded(converter.convertToModel(entity));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDomainModelUnavailable();
                    }
                }
        );
    }

    public void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceModel> callback) {
        dataSource.getAllByDomainId(
                domainId,
                new GetAllPrimitiveCallback<RecipeIngredientEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                        callback.onAllDomainModelsLoaded(converter.convertToModels(entities));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDomainModelsUnavailable();
                    }
                }
        );
    }

    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeIngredientPersistenceModel> callback) {
        getAllByDomainId(
                domainId,
                new GetAllDomainModelsCallback<RecipeIngredientPersistenceModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeIngredientPersistenceModel> models) {
                        callback.onDomainModelLoaded(filterForActiveModel(models));
                    }

                    @Override
                    public void onDomainModelsUnavailable() {
                        callback.onDomainModelUnavailable();
                    }
                }
        );
    }

    private RecipeIngredientPersistenceModel filterForActiveModel(
            List<RecipeIngredientPersistenceModel> models) {
        long lastUpdated = 0;
        RecipeIngredientPersistenceModel activeModel = new RecipeIngredientPersistenceModel.Builder().
                getDefault().
                build();

        for (RecipeIngredientPersistenceModel currentModel : models) {
            if (currentModel.getLastUpdate() > lastUpdated) {
                activeModel = currentModel;
            }
        }
        return activeModel;
    }

    public void getAllByRecipeId(
            @Nonnull String recipeId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceModel> callback) {
        dataSource.getAllByRecipeId(
                recipeId,
                new GetAllPrimitiveCallback<RecipeIngredientEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                        callback.onAllDomainModelsLoaded(converter.convertToModels(entities));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDomainModelsUnavailable();
                    }
                }
        );
    }

    public void getAllByProductId(
            @Nonnull String productId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceModel> callback) {
        dataSource.getAllByProductId(
                productId,
                new GetAllPrimitiveCallback<RecipeIngredientEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                        callback.onAllDomainModelsLoaded(converter.convertToModels(entities));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDomainModelsUnavailable();
                    }
                }
        );
    }

    public void getAllByIngredientId(
            @Nonnull String ingredientId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceModel> callback) {
        dataSource.getAllByIngredientId(
                ingredientId,
                new GetAllPrimitiveCallback<RecipeIngredientEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                        callback.onAllDomainModelsLoaded(converter.convertToModels(entities));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDomainModelsUnavailable();
                    }
                }
        );
    }

    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceModel> callback) {
        dataSource.getAll(
                new GetAllPrimitiveCallback<RecipeIngredientEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                        callback.onAllDomainModelsLoaded(converter.convertToModels(entities));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDomainModelsUnavailable();
                    }
                }
        );
    }
}
