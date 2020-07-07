package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetAllDomainModelsCallback;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationPersistenceDomainModel;

import java.util.List;

import javax.annotation.Nonnull;

public class DurationLocalGetAdapter {

    private static final String TAG = "tkm-" + DurationLocalGetAdapter.class.getSimpleName() + ": ";

    @Nonnull
    private final RecipeDurationLocalDataSource dataSource;
    @Nonnull
    private final DurationModelToDatabaseEntityConverterParent converter;

    public DurationLocalGetAdapter(@Nonnull RecipeDurationLocalDataSource dataSource) {
        this.dataSource = dataSource;
        converter = new DurationModelToDatabaseEntityConverterParent();
    }

    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeDurationPersistenceDomainModel> callback) {
        dataSource.getByDataId(
                dataId,
                new GetPrimitiveCallback<RecipeDurationEntity>() {
                    @Override
                    public void onEntityLoaded(RecipeDurationEntity entity) {
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
            @Nonnull GetAllDomainModelsCallback<RecipeDurationPersistenceDomainModel> callback) {
        dataSource.getAllByDomainId(
                domainId,
                new GetAllPrimitiveCallback<RecipeDurationEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeDurationEntity> entities) {
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
            @Nonnull GetDomainModelCallback<RecipeDurationPersistenceDomainModel> callback) {
        getAllByDomainId(
                domainId,
                new GetAllDomainModelsCallback<RecipeDurationPersistenceDomainModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeDurationPersistenceDomainModel> models) {
                        callback.onPersistenceModelLoaded(filterForActiveModel(models));
                    }

                    @Override
                    public void onDomainModelsUnavailable() {
                        callback.onPersistenceModelUnavailable();
                    }
                }
        );
    }

    private RecipeDurationPersistenceDomainModel filterForActiveModel(
            List<RecipeDurationPersistenceDomainModel> models) {
        long lastUpdated = 0;
        RecipeDurationPersistenceDomainModel activeModel =
                new RecipeDurationPersistenceDomainModel.Builder().
                        getDefault().
                        build();

        for (RecipeDurationPersistenceDomainModel m : models) {
            if (m.getLastUpdate() > lastUpdated) {
                activeModel = m;
            }
        }
        return activeModel;
    }

    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeDurationPersistenceDomainModel> callback) {
        dataSource.getAll(
                new GetAllPrimitiveCallback<RecipeDurationEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeDurationEntity> entities) {
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
