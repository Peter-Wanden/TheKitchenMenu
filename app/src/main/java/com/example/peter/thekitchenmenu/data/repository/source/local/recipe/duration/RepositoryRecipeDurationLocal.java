package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter.DurationLocalDeleteAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter.DurationLocalGetAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter.DurationLocalSaveAdapter;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.duration.RecipeDurationUseCasePersistenceModel;

import java.util.List;

import javax.annotation.Nonnull;

public class RepositoryRecipeDurationLocal
        implements DomainDataAccess<RecipeDurationUseCasePersistenceModel> {

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
            @Nonnull GetDomainModelCallback<RecipeDurationUseCasePersistenceModel> callback) {
        getAdapter.getByDataId(
                dataId,
                new GetDomainModelCallback<RecipeDurationUseCasePersistenceModel>() {
                    @Override
                    public void onPersistenceModelLoaded(RecipeDurationUseCasePersistenceModel model) {
                        callback.onPersistenceModelLoaded(model);
                    }

                    @Override
                    public void onPersistenceModelUnavailable() {
                        callback.onPersistenceModelUnavailable();
                    }
                }
        );
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeDurationUseCasePersistenceModel> callback) {
        getAdapter.getAll(
                new GetAllDomainModelsCallback<RecipeDurationUseCasePersistenceModel>() {
                    @Override
                    public void onAllDomainModelsLoaded(List<RecipeDurationUseCasePersistenceModel> models) {
                        callback.onAllDomainModelsLoaded(models);
                    }

                    @Override
                    public void onDomainModelsUnavailable() {
                        callback.onDomainModelsUnavailable();
                    }
                }
        );
    }

    @Override
    public void getByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeDurationUseCasePersistenceModel> callback) {
        getAdapter.getActiveByDomainId(
                domainId, new GetDomainModelCallback<RecipeDurationUseCasePersistenceModel>() {
                    @Override
                    public void onPersistenceModelLoaded(RecipeDurationUseCasePersistenceModel model) {
                        callback.onPersistenceModelLoaded(model);
                    }

                    @Override
                    public void onPersistenceModelUnavailable() {
                        callback.onPersistenceModelUnavailable();
                    }
                }
        );
    }

    @Override
    public void save(@Nonnull RecipeDurationUseCasePersistenceModel model) {
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
    public void deleteByDomainId(@Nonnull String domainId) {
        deleteAdapter.deleteAllByDomainId(domainId);
    }

    @Override
    public void deleteAll() {
        deleteAdapter.deleteAll();
    }
}
