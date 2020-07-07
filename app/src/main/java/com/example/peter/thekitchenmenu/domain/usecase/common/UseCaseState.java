package com.example.peter.thekitchenmenu.domain.usecase.common;

import com.example.peter.thekitchenmenu.data.repository.Repository;
import com.example.peter.thekitchenmenu.domain.entity.BusinessEntity;
import com.example.peter.thekitchenmenu.domain.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.model.DomainModelConverter;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;

import java.util.ArrayList;
import java.util.List;

public abstract class UseCaseState<
        REPOSITORY extends Repository<PERSISTENCE_MODEL>,
        PERSISTENCE_MODEL extends DomainModel.PersistenceDomainModel,
        USE_CASE_MODEL extends DomainModel.UseCaseDomainModel,
        REQUEST_MODEL extends DomainModel.RequestDomainModel,
        RESPONSE_MODEL extends DomainModel.ResponseDomainModel>

        extends UseCaseData<
        REPOSITORY,
        PERSISTENCE_MODEL,
        USE_CASE_MODEL,
        REQUEST_MODEL,
        RESPONSE_MODEL> {

    protected List<FailReasons> failReasons = new ArrayList<>();
    protected List<BusinessEntity> entities = new ArrayList<>();

    public UseCaseState(DomainModelConverter<
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
