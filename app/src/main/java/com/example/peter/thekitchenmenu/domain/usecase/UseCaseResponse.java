package com.example.peter.thekitchenmenu.domain.usecase;

/**
 * Base class for all recipe component responses
 * @param <DM> the recipe components domain data model
 */
public abstract class UseCaseResponse<
        DM extends UseCaseDomainModel>
        implements UseCase.Response {

    protected String id;
    protected UseCaseMetadata metadata;
    protected DM model;

    public String getId() {
        return id;
    }

    public UseCaseMetadata getMetadata() {
        return metadata;
    }

    public DM getModel() {
        return model;
    }

    /**
     * Base builder for {@link UseCaseResponse}'s
     * @param <SELF> the {@link UseCaseResponseBuilder} using this base class
     * @param <CR> the {@link UseCaseResponse} class being built
     * @param <DM> the {@link UseCaseDomainModel} for the {@link UseCaseResponse}
     */
    public static abstract class UseCaseResponseBuilder<
            SELF extends UseCaseResponseBuilder,
            CR extends UseCaseResponse,
            DM extends UseCaseDomainModel> {

        protected CR response;

        public abstract SELF getDefault();

        public SELF setId(String id) {
            response.id = id;
            return self();
        }

        public SELF setMetadata(UseCaseMetadata metadata) {
            response.metadata = metadata;
            return self();
        }

        public SELF setModel(DM model) {
            response.model = model;
            return self();
        }

        public CR build() {
            return response;
        }

        @SuppressWarnings("unchecked")
        protected SELF self() {
            return (SELF) this;
        }
    }
}
