package com.example.peter.thekitchenmenu.domain.usecase;

import java.util.Objects;

/**
 * The base class for an {@link UseCaseRequestWithDomainModel}.
 * All requests require an ID and a data structure (model) of the domain data relating to the
 * request. See classes extending this abstract class for details of the data models and the
 * implementation pattern for the builders.
 * @param <DM> the business domain data model
 */
public abstract class UseCaseRequestWithDomainModel<DM extends UseCaseDomainModel>
        extends UseCaseDomainRequest {

    protected DM model;

    public DM getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UseCaseRequestWithDomainModel<?> that = (UseCaseRequestWithDomainModel<?>) o;
        return Objects.equals(dataId, that.dataId) &&
                Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataId, model);
    }

    /**
     * Base class for a {@link UseCaseRequestWithDomainModel} builder.
     * To use:
     *  - Provide a no args constructor the sets 'request = new YourRequestType()'
     *  - Override self() to return this
     *
     * @param <SELF> the builder class extending this abstract builder.
     * @param <R> the request class extending the {@link UseCaseRequestWithDomainModel} for the builder.
     * @param <DM> the the {@link UseCaseDomainModel} for the {@link UseCaseRequestWithDomainModel}.
     */
    public static abstract class UseCaseRequestBuilder
                    <SELF extends UseCaseRequestBuilder<SELF, R, DM>,
                    R extends UseCaseRequestWithDomainModel<DM>,
                    DM extends UseCaseDomainModel> {

        protected R request;

        public abstract SELF getDefault();

        public SELF setDataId(String dataId) {
            request.dataId = dataId;
            return self();
        }

        public SELF setDomainId(String domainId) {
            request.domainId = domainId;
            return self();
        }

        public SELF setModel(DM model) {
            request.model = model;
            return self();
        }

        public R build() {
            return request;
        }

        @SuppressWarnings("unchecked")
        protected SELF self() {
            return (SELF) this;
        }
    }
}
