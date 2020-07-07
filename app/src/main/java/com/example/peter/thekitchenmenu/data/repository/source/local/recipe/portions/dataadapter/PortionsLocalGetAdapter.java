package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetAllDomainModelsCallback;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.RecipePortionsLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsPersistenceDomainModel;

import java.util.List;

import javax.annotation.Nonnull;

public class PortionsLocalGetAdapter {

    @Nonnull
    private final RecipePortionsLocalDataSource dataSource;
    @Nonnull
    private final PortionsModelToDatabaseEntityConverterParent converter;

    public PortionsLocalGetAdapter(@Nonnull RecipePortionsLocalDataSource dataSource) {
        this.dataSource = dataSource;
        converter = new PortionsModelToDatabaseEntityConverterParent();
    }

    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipePortionsPersistenceDomainModel> callback) {
        dataSource.getByDataId(
                dataId,
                new GetPrimitiveCallback<RecipePortionsEntity>() {
                    @Override
                    public void onEntityLoaded(RecipePortionsEntity entity) {
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
            @Nonnull GetAllDomainModelsCallback<RecipePortionsPersistenceDomainModel> callback) {
        dataSource.getAllByDomainId(
                domainId, new GetAllPrimitiveCallback<RecipePortionsEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipePortionsEntity> entities) {
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
            @Nonnull GetDomainModelCallback<RecipePortionsPersistenceDomainModel> callback) {
        getAllByDomainId(
                domainId,
                new GetAllDomainModelsCallback<RecipePortionsPersistenceDomainModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipePortionsPersistenceDomainModel> models) {
                        callback.onPersistenceModelLoaded(filterForActiveModel(models));
                    }

                    @Override
                    public void onDomainModelsUnavailable() {
                        callback.onPersistenceModelUnavailable();
                    }
                }
        );
    }

    private RecipePortionsPersistenceDomainModel filterForActiveModel(
            List<RecipePortionsPersistenceDomainModel> models) {
        long lasUpdated = 0;
        RecipePortionsPersistenceDomainModel activeModel = new RecipePortionsPersistenceDomainModel.Builder().
                getDefault().
                build();

        for (RecipePortionsPersistenceDomainModel m : models) {
            if (m.getLastUpdate() > lasUpdated) {
                activeModel = m;
            }
        }
        return activeModel;
    }

    public void getAll(GetAllDomainModelsCallback<RecipePortionsPersistenceDomainModel> callback) {
        dataSource.getAll(
                new GetAllPrimitiveCallback<RecipePortionsEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipePortionsEntity> entities) {
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
