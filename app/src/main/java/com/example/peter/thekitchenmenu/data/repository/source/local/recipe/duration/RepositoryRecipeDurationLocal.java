package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationPersistenceModel;

import javax.annotation.Nonnull;

public class RepositoryRecipeDurationLocal
        implements DomainDataAccess<RecipeDurationPersistenceModel> {

    public static volatile RepositoryRecipeDurationLocal INSTANCE;



    @Override
    public void getAll(@Nonnull GetAllDomainModelsCallback<RecipeDurationPersistenceModel> callback) {

    }

    @Override
    public void getByDataId(@Nonnull String dataId, @Nonnull GetDomainModelCallback<RecipeDurationPersistenceModel> callback) {

    }

    @Override
    public void getActiveByDomainId(@Nonnull String domainId, @Nonnull GetDomainModelCallback<RecipeDurationPersistenceModel> callback) {

    }

    @Override
    public void save(@Nonnull RecipeDurationPersistenceModel model) {

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
