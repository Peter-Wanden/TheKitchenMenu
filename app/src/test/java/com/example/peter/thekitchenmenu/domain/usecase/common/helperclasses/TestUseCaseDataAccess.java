package com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;

import javax.annotation.Nonnull;

public class TestUseCaseDataAccess
        implements
        DomainDataAccess<TestUseCasePersistenceModel> {
    @Override
    public void getByDataId(
            @Nonnull String dataId,
            @Nonnull GetDomainModelCallback<TestUseCasePersistenceModel> callback) {

    }

    @Override
    public void getByDomainId(
            @Nonnull String domainId,
            @Nonnull GetDomainModelCallback<TestUseCasePersistenceModel> callback) {

    }

    @Override
    public void getAll(@Nonnull GetAllDomainModelsCallback<TestUseCasePersistenceModel> callback) {

    }

    @Override
    public void save(@Nonnull TestUseCasePersistenceModel model) {

    }

    @Override
    public void refreshData() {

    }

    @Override
    public void deleteByDataId(@Nonnull String dataId) {

    }

    @Override
    public void deleteAllByDomainId(@Nonnull String domainId) {

    }

    @Override
    public void deleteAll() {

    }
}
