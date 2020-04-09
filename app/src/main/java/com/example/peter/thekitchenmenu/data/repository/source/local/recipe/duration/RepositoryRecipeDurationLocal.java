package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter.DurationLocalDeleteAdapter;
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
    @Nonnull
    private final DurationLocalDeleteAdapter deleteAdapter;

    private RepositoryRecipeDurationLocal(
            @Nonnull DurationLocalGetAdapter getAdapter,
            @Nonnull DurationLocalSaveAdapter saveAdapter,
            @Nonnull DurationLocalDeleteAdapter deleteAdapter) {
        this.getAdapter = getAdapter;
        this.saveAdapter = saveAdapter;
        this.deleteAdapter = deleteAdapter;
    }

    public static RepositoryRecipeDurationLocal getInstance(
            @Nonnull DurationLocalGetAdapter getAdapter,
            @Nonnull DurationLocalSaveAdapter saveAdapter,
            @Nonnull DurationLocalDeleteAdapter deleteAdapter) {

        if (INSTANCE == null) {
            synchronized (RepositoryRecipeDurationLocal.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RepositoryRecipeDurationLocal(
                            getAdapter,
                            saveAdapter,
                            deleteAdapter
                    );
                }
            }
        }
        return INSTANCE;
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
        deleteAdapter.deleteByDataId(dataId);
    }

    @Override
    public void deleteAllByDomainId(@Nonnull String domainId) {
        deleteAdapter.deleteAllByDomainId(domainId);
    }

    @Override
    public void deleteAll() {
        deleteAdapter.deleteAll();
    }
}
