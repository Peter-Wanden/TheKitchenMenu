package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter.DurationLocalGetAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter.DurationLocalSaveAdapter;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationPersistenceModel;

import java.util.List;

import javax.annotation.Nonnull;

public class RepositoryRecipeDurationLocal
        implements DomainDataAccess<RecipeDurationPersistenceModel> {

    public static volatile RepositoryRecipeDurationLocal INSTANCE;

    @Nonnull
    private final DurationLocalGetAdapter getAdapter;
    @Nonnull
    private final DurationLocalSaveAdapter saveAdapter;

    public RepositoryRecipeDurationLocal(
            @Nonnull DurationLocalGetAdapter getAdapter,
            @Nonnull DurationLocalSaveAdapter saveAdapter) {
        this.getAdapter = getAdapter;
        this.saveAdapter = saveAdapter;
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeDurationPersistenceModel> c) {
        getAdapter.getByDataId(
                dataId,
                new GetDomainModelCallback<RecipeDurationPersistenceModel>() {
                    @Override
                    public void onModelLoaded(RecipeDurationPersistenceModel m) {
                        c.onModelLoaded(m);
                    }

                    @Override
                    public void onModelUnavailable() {
                        c.onModelUnavailable();
                    }
                }
        );
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeDurationPersistenceModel> c) {
        getAdapter.getAll(
                new GetAllDomainModelsCallback<RecipeDurationPersistenceModel>() {
                    @Override
                    public void onAllLoaded(List<RecipeDurationPersistenceModel> m) {
                        c.onAllLoaded(m);
                    }

                    @Override
                    public void onModelsUnavailable() {
                        c.onModelsUnavailable();
                    }
                }
        );
    }

    @Override
    public void getActiveByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeDurationPersistenceModel> c) {
        c.onModelUnavailable();
    }

    @Override
    public void save(@Nonnull RecipeDurationPersistenceModel model) {
        saveAdapter.save(model);
    }

    @Override
    public void refreshData() {

    }

    @Override
    public void deleteByDataId(String dataId) {

    }

    @Override
    public void deleteAllByDomainId(@Nonnull String domainId) {

    }

    @Override
    public void deleteAll() {

    }
}
