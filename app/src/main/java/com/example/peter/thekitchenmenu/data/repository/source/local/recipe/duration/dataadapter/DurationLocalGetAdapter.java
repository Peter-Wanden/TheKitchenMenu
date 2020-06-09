package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetAllDomainModelsCallback;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationPersistenceModel;

import java.util.List;

import javax.annotation.Nonnull;

public class DurationLocalGetAdapter {

    private static final String TAG = "tkm-" + DurationLocalGetAdapter.class.getSimpleName() + ": ";

    @Nonnull
    private final RecipeDurationLocalDataSource dataSource;
    @Nonnull
    private final DurationModelConverterParent converter;

    public DurationLocalGetAdapter(@Nonnull RecipeDurationLocalDataSource dataSource) {
        this.dataSource = dataSource;
        converter = new DurationModelConverterParent();
    }

    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeDurationPersistenceModel> callback) {
        dataSource.getByDataId(
                dataId,
                new GetPrimitiveCallback<RecipeDurationEntity>() {
                    @Override
                    public void onEntityLoaded(RecipeDurationEntity entity) {
                        callback.onDomainModelLoaded(converter.convertToModelItem(entity));
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
            @Nonnull GetAllDomainModelsCallback<RecipeDurationPersistenceModel> callback) {
        dataSource.getAllByDomainId(
                domainId,
                new GetAllPrimitiveCallback<RecipeDurationEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeDurationEntity> entities) {
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
            @Nonnull GetDomainModelCallback<RecipeDurationPersistenceModel> callback) {
        getAllByDomainId(
                domainId,
                new GetAllDomainModelsCallback<RecipeDurationPersistenceModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeDurationPersistenceModel> models) {
                        callback.onDomainModelLoaded(filterForActiveModel(models));
                    }

                    @Override
                    public void onDomainModelsUnavailable() {
                        callback.onDomainModelUnavailable();
                    }
                }
        );
    }

    private RecipeDurationPersistenceModel filterForActiveModel(
            List<RecipeDurationPersistenceModel> models) {
        long lastUpdated = 0;
        RecipeDurationPersistenceModel activeModel =
                new RecipeDurationPersistenceModel.Builder().
                        getDefault().
                        build();

        for (RecipeDurationPersistenceModel m : models) {
            if (m.getLastUpdate() > lastUpdated) {
                activeModel = m;
            }
        }
        return activeModel;
    }

    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeDurationPersistenceModel> callback) {
        dataSource.getAll(
                new GetAllPrimitiveCallback<RecipeDurationEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeDurationEntity> entities) {
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
