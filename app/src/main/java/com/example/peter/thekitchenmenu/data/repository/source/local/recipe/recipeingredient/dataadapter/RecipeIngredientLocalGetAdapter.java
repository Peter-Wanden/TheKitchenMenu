package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetAllDomainModelsCallback;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.datasource.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.datasource.RecipeIngredientLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredientPersistenceDomainModel;

import java.util.List;

import javax.annotation.Nonnull;

public class RecipeIngredientLocalGetAdapter {

    @Nonnull
    private final RecipeIngredientLocalDataSource dataSource;
    @Nonnull
    private final RecipeIngredientModelToDatabaseEntityConverterParent converter;

    public RecipeIngredientLocalGetAdapter(@Nonnull RecipeIngredientLocalDataSource dataSource) {
        this.dataSource = dataSource;
        converter = new RecipeIngredientModelToDatabaseEntityConverterParent();
    }

    public void getByDataId(
            @Nonnull String dataId,
            GetDomainModelCallback<RecipeIngredientPersistenceDomainModel> callback) {
        dataSource.getByDataId(
                dataId,
                new GetPrimitiveCallback<RecipeIngredientEntity>() {
                    @Override
                    public void onEntityLoaded(RecipeIngredientEntity entity) {
                        callback.onPersistenceModelLoaded(converter.convertParentEntityToDomainModel(entity));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onPersistenceModelUnavailable();
                    }
                }
        );
    }

    public void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceDomainModel> callback) {
        dataSource.getAllByDomainId(
                domainId,
                new GetAllPrimitiveCallback<RecipeIngredientEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                        callback.onAllDomainModelsLoaded(converter.convertParentEntitiesToDomainModels(entities));
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
            @Nonnull GetDomainModelCallback<RecipeIngredientPersistenceDomainModel> callback) {
        getAllByDomainId(
                domainId,
                new GetAllDomainModelsCallback<RecipeIngredientPersistenceDomainModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeIngredientPersistenceDomainModel> models) {
                        callback.onPersistenceModelLoaded(filterForActiveModel(models));
                    }

                    @Override
                    public void onDomainModelsUnavailable() {
                        callback.onPersistenceModelUnavailable();
                    }
                }
        );
    }

    private RecipeIngredientPersistenceDomainModel filterForActiveModel(
            List<RecipeIngredientPersistenceDomainModel> models) {
        long lastUpdated = 0;
        RecipeIngredientPersistenceDomainModel activeModel = new RecipeIngredientPersistenceDomainModel.Builder().
                getDefault().
                build();

        for (RecipeIngredientPersistenceDomainModel currentModel : models) {
            if (currentModel.getLastUpdate() > lastUpdated) {
                activeModel = currentModel;
            }
        }
        return activeModel;
    }

    public void getAllByRecipeId(
            @Nonnull String recipeId,
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceDomainModel> callback) {
        dataSource.getAllByRecipeId(
                recipeId,
                new GetAllPrimitiveCallback<RecipeIngredientEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                        callback.onAllDomainModelsLoaded(converter.convertParentEntitiesToDomainModels(entities));
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
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceDomainModel> callback) {
        dataSource.getAllByProductId(
                productId,
                new GetAllPrimitiveCallback<RecipeIngredientEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                        callback.onAllDomainModelsLoaded(converter.convertParentEntitiesToDomainModels(entities));
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
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceDomainModel> callback) {
        dataSource.getAllByIngredientId(
                ingredientId,
                new GetAllPrimitiveCallback<RecipeIngredientEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                        callback.onAllDomainModelsLoaded(converter.convertParentEntitiesToDomainModels(entities));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDomainModelsUnavailable();
                    }
                }
        );
    }

    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeIngredientPersistenceDomainModel> callback) {
        dataSource.getAll(
                new GetAllPrimitiveCallback<RecipeIngredientEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeIngredientEntity> entities) {
                        callback.onAllDomainModelsLoaded(converter.convertParentEntitiesToDomainModels(entities));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onDomainModelsUnavailable();
                    }
                }
        );
    }
}
