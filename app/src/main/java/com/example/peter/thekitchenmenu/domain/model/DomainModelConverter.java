package com.example.peter.thekitchenmenu.domain.model;

public interface DomainModelConverter<
        USE_CASE_MODEL extends DomainModel.UseCaseDomainModel,
        PERSISTENCE_MODEL extends DomainModel.PersistenceDomainModel,
        REQUEST_MODEL extends DomainModel.RequestDomainModel,
        RESPONSE_MODEL extends DomainModel.ResponseDomainModel> {

    USE_CASE_MODEL convertPersistenceToDomainModel(PERSISTENCE_MODEL model);

    USE_CASE_MODEL convertRequestModelToUseCaseModel(REQUEST_MODEL model);

    PERSISTENCE_MODEL convertPersistentModelToArchivedModel(long currentTime);
}
