package com.example.peter.thekitchenmenu.domain.usecase.common;

import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.entity.BusinessEntity;
import com.example.peter.thekitchenmenu.domain.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;

import java.util.ArrayList;
import java.util.List;

public abstract class UseCaseState<
        REPOSITORY extends Repository<PERSISTENCE_MODEL>,
        PERSISTENCE_MODEL extends DomainModel.PersistenceModel,
        USE_CASE_MODEL extends DomainModel.UseCaseModel,
        REQUEST_MODEL extends DomainModel.RequestModel,
        RESPONSE_MODEL extends DomainModel.ResponseModel>

        extends UseCaseModel {

    protected List<FailReasons> failReasons = new ArrayList<>();
    protected List<BusinessEntity> entities = new ArrayList<>();

    public UseCaseState(DomainModel.ModelConverter<
                    USE_CASE_MODEL,
                    PERSISTENCE_MODEL,
                    REQUEST_MODEL,
                    RESPONSE_MODEL> modelConverter) {
        super(modelConverter);
    }

    @Override
    protected void initialiseUseCase() {
        failReasons.clear();

    }
}
