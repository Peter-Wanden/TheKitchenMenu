package com.example.peter.thekitchenmenu.domain.usecasenew.common.model;

import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import javax.annotation.Nonnull;

public abstract class DomainModelConverter<
        USE_CASE_MODEL extends DomainModel.UseCaseModel,
        PERSISTENCE_MODEL extends DomainModel.PersistenceModel,
        REQUEST_MODEL extends DomainModel.UseCaseRequestModel,
        RESPONSE_MODEL extends DomainModel.UseCaseResponseModel> {

    @Nonnull
    protected final TimeProvider timeProvider;

    @Nonnull
    protected final UniqueIdProvider idProvider;

    public DomainModelConverter(@Nonnull TimeProvider timeProvider,
                                @Nonnull UniqueIdProvider idProvider) {
        this.timeProvider = timeProvider;
        this.idProvider = idProvider;
    }

    public abstract USE_CASE_MODEL convertPersistenceToUseCaseModel(
            @Nonnull PERSISTENCE_MODEL persistenceModel);

    public abstract USE_CASE_MODEL convertRequestToUseCaseModel(
            @Nonnull REQUEST_MODEL requestModel);

    public abstract PERSISTENCE_MODEL convertUseCaseToPersistenceModel(
            @Nonnull String domainId,
            @Nonnull USE_CASE_MODEL useCaseModel);

    public abstract PERSISTENCE_MODEL createArchivedPersistenceModel(
            @Nonnull PERSISTENCE_MODEL persistenceModel);

    public abstract RESPONSE_MODEL convertUseCaseToResponseModel(
            @Nonnull USE_CASE_MODEL useCaseModel);

    public abstract PERSISTENCE_MODEL updatePersistenceModel(
            @Nonnull PERSISTENCE_MODEL persistenceModel,
            @Nonnull USE_CASE_MODEL useCaseModel);

    public abstract USE_CASE_MODEL getDefault();
}
