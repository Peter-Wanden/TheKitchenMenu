package com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetAllDomainModelsCallback;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource.IngredientLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientPersistenceModel;

import java.util.List;

import javax.annotation.Nonnull;

public class IngredientLocalGetAdapter {

    @Nonnull
    private final IngredientLocalDataSource dataSource;
    @Nonnull
    private final IngredientLocalModelConverterParent converter;

    public IngredientLocalGetAdapter(@Nonnull IngredientLocalDataSource dataSource) {
        this.dataSource = dataSource;
        converter = new IngredientLocalModelConverterParent();
    }

    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<IngredientPersistenceModel> callback) {
        dataSource.getByDataId(
                dataId,
                new GetPrimitiveCallback<IngredientEntity>() {
                    @Override
                    public void onEntityLoaded(IngredientEntity entity) {
                        callback.onModelLoaded(converter.convertToModel(entity));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onModelUnavailable();
                    }
                }
        );
    }

    public void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<IngredientPersistenceModel> callback) {
        dataSource.getAllByDomainId(
                domainId,
                new GetAllPrimitiveCallback<IngredientEntity>() {
                    @Override
                    public void onAllLoaded(List<IngredientEntity> entities) {
                        callback.onAllLoaded(converter.convertToModels(entities));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onModelsUnavailable();
                    }
                }
        );
    }

    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<IngredientPersistenceModel> callback) {
        getAllByDomainId(
                domainId,
                new GetAllDomainModelsCallback<IngredientPersistenceModel>() {
                    @Override
                    public void onAllLoaded(List<IngredientPersistenceModel> models) {
                        callback.onModelLoaded(filterForActiveModel(models));
                    }

                    @Override
                    public void onModelsUnavailable() {
                        callback.onModelUnavailable();
                    }
                }
        );
    }

    private IngredientPersistenceModel filterForActiveModel(
            List<IngredientPersistenceModel> models) {
        long lastUpdated = 0;
        IngredientPersistenceModel activeModel = new IngredientPersistenceModel.Builder().
                getDefault().build();

        for (IngredientPersistenceModel m : models) {
            if (m.getLastUpdate() > lastUpdated) {
                activeModel = m;
                lastUpdated = m.getLastUpdate();
            }
        }
        return activeModel;
    }

    public void getAll(GetAllDomainModelsCallback<IngredientPersistenceModel> callback) {
        dataSource.getAll(
                new GetAllPrimitiveCallback<IngredientEntity>() {
                    @Override
                    public void onAllLoaded(List<IngredientEntity> entities) {
                        callback.onAllLoaded(converter.convertToModels(entities));
                    }

                    @Override
                    public void onDataUnavailable() {
                        callback.onModelsUnavailable();
                    }
                }
        );
    }
}
