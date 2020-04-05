package com.example.peter.thekitchenmenu.domain.usecase;

public abstract class UseCaseDomainMessageBasePlusModelMetadata<
        DM extends UseCaseDomainModel>
        extends UseCaseDomainMessageBasePlusModel<DM> {

    protected UseCaseMetadata metadata;

    public UseCaseMetadata getMetadata() {
        return metadata;
    }

    public static abstract class UseCaseMessageBuilderWithMetadata
            <SELF extends UseCaseMessageBuilderWithMetadata<SELF, M, DM>,
                    M extends UseCaseDomainMessageBasePlusModelMetadata<DM>,
                    DM extends UseCaseDomainModel>
            extends UseCaseMessageBuilderWithModel<SELF, M, DM> {

        public SELF setMetadata(UseCaseMetadata metadata) {
            message.metadata = metadata;
            return self();
        }
    }
}
