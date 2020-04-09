package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetAllDomainModelsCallback;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.primitive.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.primitive.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.primitive.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationPersistenceModel;

import java.util.List;

import javax.annotation.Nonnull;

public class DurationLocalGetAdapter {

    @Nonnull
    private final RecipeDurationLocalDataSource durationLocalDataSource;

    public DurationLocalGetAdapter(@Nonnull RecipeDurationLocalDataSource durationLocalDataSource) {
        this.durationLocalDataSource = durationLocalDataSource;
    }

    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeDurationPersistenceModel> c) {
        durationLocalDataSource.getByDataId(
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
        durationLocalDataSource.getAllByDomainId(
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
        durationLocalDataSource.getAll(
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
}
