package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetAllDomainModelsCallback;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.RecipePortionsLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsPersistenceModel;

import java.util.List;

import javax.annotation.Nonnull;

public class PortionsLocalGetAdapter {

    @Nonnull
    private final RecipePortionsLocalDataSource dataSource;
    @Nonnull
    private final PortionsModelConverterParent converter;

    public PortionsLocalGetAdapter(@Nonnull RecipePortionsLocalDataSource dataSource) {
        this.dataSource = dataSource;
        converter = new PortionsModelConverterParent();
    }

    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipePortionsPersistenceModel> callback) {
        dataSource.getByDataId(
                dataId,
                new GetPrimitiveCallback<RecipePortionsEntity>() {
                    @Override
                    public void onEntityLoaded(RecipePortionsEntity entity) {
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
            @Nonnull GetAllDomainModelsCallback<RecipePortionsPersistenceModel> callback) {
        dataSource.getAllByDomainId(
                domainId, new GetAllPrimitiveCallback<RecipePortionsEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipePortionsEntity> entities) {
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
            @Nonnull GetDomainModelCallback<RecipePortionsPersistenceModel> callback) {
        getAllByDomainId(
                domainId,
                new GetAllDomainModelsCallback<RecipePortionsPersistenceModel>() {
                    @Override
                    public void onAllLoaded(List<RecipePortionsPersistenceModel> models) {
                        callback.onModelLoaded(filterForActiveModel(models));
                    }

                    @Override
                    public void onModelsUnavailable() {
                        callback.onModelUnavailable();
                    }
                }
        );
    }

    private RecipePortionsPersistenceModel filterForActiveModel(
            List<RecipePortionsPersistenceModel> models) {
        long lasUpdated = 0;
        RecipePortionsPersistenceModel activeModel = new RecipePortionsPersistenceModel.Builder().
                getDefault().
                build();

        for (RecipePortionsPersistenceModel m : models) {
            if (m.getLastUpdate() > lasUpdated) {
                activeModel = m;
            }
        }
        return activeModel;
    }

    public void getAll(GetAllDomainModelsCallback<RecipePortionsPersistenceModel> callback) {
        dataSource.getAll(
                new GetAllPrimitiveCallback<RecipePortionsEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipePortionsEntity> entities) {
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
