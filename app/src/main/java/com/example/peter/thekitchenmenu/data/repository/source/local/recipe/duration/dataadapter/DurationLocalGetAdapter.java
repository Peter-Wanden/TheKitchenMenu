package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetAllDomainModelsCallback;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.primitive.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.primitive.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationPersistenceModel;

import java.util.List;

import javax.annotation.Nonnull;

public class DurationLocalGetAdapter {

    @Nonnull
    private final RecipeDurationLocalDataSource localDataSource;

    public DurationLocalGetAdapter(@Nonnull RecipeDurationLocalDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeDurationPersistenceModel> c) {
        localDataSource.getByDataId(
                dataId,
                new GetPrimitiveCallback<RecipeDurationEntity>() {
                    @Override
                    public void onEntityLoaded(RecipeDurationEntity e) {
                        DurationConverter d = new DurationConverter();
                        c.onModelLoaded(d.convertToModel(e));
                    }

                    @Override
                    public void onDataUnavailable() {
                        c.onModelUnavailable();
                    }
                }
        );
    }

    public void getAllByDomainId(
            @Nonnull String domainId,
            @Nonnull GetAllDomainModelsCallback<RecipeDurationPersistenceModel> c) {
        localDataSource.getAllByDomainId(
                domainId,
                new GetAllPrimitiveCallback<RecipeDurationEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeDurationEntity> e) {
                        DurationConverter d = new DurationConverter();
                        c.onAllLoaded(d.convertToModels(e));
                    }

                    @Override
                    public void onDataUnavailable() {
                        c.onModelsUnavailable();
                    }
                }
        );
    }

    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeDurationPersistenceModel> c) {
        localDataSource.getAll(
                new GetAllPrimitiveCallback<RecipeDurationEntity>() {
                    @Override
                    public void onAllLoaded(List<RecipeDurationEntity> e) {
                        DurationConverter d = new DurationConverter();
                        c.onAllLoaded(d.convertToModels(e));
                    }

                    @Override
                    public void onDataUnavailable() {
                        c.onModelsUnavailable();
                    }
                }
        );
    }

    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeDurationPersistenceModel> c) {
        getAllByDomainId(
                domainId,
                new GetAllDomainModelsCallback<RecipeDurationPersistenceModel>() {
                    @Override
                    public void onAllLoaded(List<RecipeDurationPersistenceModel> models) {
                        int lastUpdated = 0;
                        RecipeDurationPersistenceModel activeModel =
                                new RecipeDurationPersistenceModel.Builder().
                                        getDefault().
                                        build();

                        for (RecipeDurationPersistenceModel m : models) {
                            if (m.getLastUpdate() > lastUpdated) {
                                activeModel = m;
                            }
                        }
                        c.onModelLoaded(activeModel);
                    }

                    @Override
                    public void onModelsUnavailable() {
                        c.onModelUnavailable();
                    }
                }
        );
    }
}
