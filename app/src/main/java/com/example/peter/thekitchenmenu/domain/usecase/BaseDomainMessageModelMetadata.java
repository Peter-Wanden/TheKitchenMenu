package com.example.peter.thekitchenmenu.domain.usecase;

import javax.annotation.Nonnull;

public abstract class BaseDomainMessageModelMetadata<
        DM extends BaseDomainModel>
        extends BaseDomainMessageModel<DM> {

    protected UseCaseMetadata metadata;

    public UseCaseMetadata getMetadata() {
        return metadata;
    }

    @Nonnull
    @Override
    public String toString() {
        return "UseCaseDomainMessageBaseModelMetadata{" +
                "metadata=" + metadata +
                ", model=" + model +
                ", dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                '}';
    }

    public static abstract class UseCaseMessageBuilderMetadata
            <SELF extends UseCaseMessageBuilderMetadata<SELF, M, DM>,
                    M extends BaseDomainMessageModelMetadata<DM>,
                    DM extends BaseDomainModel>
            extends UseCaseMessageBuilderModel<SELF, M, DM> {

        public SELF setMetadata(UseCaseMetadata metadata) {
            message.metadata = metadata;
            return self();
        }
    }
}
