package com.example.peter.thekitchenmenu.data.repository.source.remote.recipe;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course.RecipeCourseUseCasePersistenceModel;

import javax.annotation.Nonnull;

public class RepositoryRecipeCourseRemote
        implements
        DomainDataAccess<RecipeCourseUseCasePersistenceModel> {

    private static RepositoryRecipeCourseRemote INSTANCE;

    public static RepositoryRecipeCourseRemote getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RepositoryRecipeCourseRemote();
        return INSTANCE;
    }

    @Override
    public void getAll(
            @Nonnull GetAllDomainModelsCallback<RecipeCourseUseCasePersistenceModel> callback) {
        callback.onDomainModelsUnavailable();
    }

    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<RecipeCourseUseCasePersistenceModel> callback) {
        callback.onPersistenceModelUnavailable();
    }

    @Override
    public void getByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<RecipeCourseUseCasePersistenceModel> callback) {
        callback.onPersistenceModelUnavailable();
    }

    @Override
    public void save(@Nonnull RecipeCourseUseCasePersistenceModel model) {

    }

    @Override
    public void refreshData() {

    }

    @Override
    public void deleteByDataId(String dataId) {

    }

    @Override
    public void deleteByDomainId(@Nonnull String domainId) {

    }

    @Override
    public void deleteAll() {

    }
}
