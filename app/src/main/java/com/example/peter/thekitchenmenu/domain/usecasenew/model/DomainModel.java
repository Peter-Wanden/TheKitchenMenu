package com.example.peter.thekitchenmenu.domain.usecasenew.model;

import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import javax.annotation.Nonnull;

/**
 * Tagging interface for all domain data model classes
 */
public interface DomainModel {

    /**
     * Business entity domain model
     */
    interface BusinessEntityModel {
    }

    /**
     * Use case domain model
     */
    interface UseCaseModel {
    }

    /**
     * All domain models that are to be persisted implement this interface
     */
    interface PersistenceModel {

        String getDataId();

        String getDomainId();

        long getCreateDate();

        long getLastUpdate();
    }

    /**
     * Request domain model
     */
    interface UseCaseRequestModel {
    }

    /**
     * Response domain model
     */
    interface UseCaseResponseModel {
    }

    abstract class Converter<
            USE_CASE_MODEL extends UseCaseModel,
            PERSISTENCE_MODEL extends PersistenceModel,
            REQUEST_MODEL extends UseCaseRequestModel,
            RESPONSE_MODEL extends UseCaseResponseModel> {

        @Nonnull
        protected final TimeProvider timeProvider;

        @Nonnull
        protected final UniqueIdProvider idProvider;

        public Converter(@Nonnull TimeProvider timeProvider,
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
    }
}
