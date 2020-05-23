package com.example.peter.thekitchenmenu.domain.usecase;

import java.util.Objects;

public abstract class BaseDomainMessageModelMetadata<DM extends BaseDomainModel>
        extends
        BaseDomainMessageModel<DM> {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseDomainMessageModelMetadata)) return false;
        if (!super.equals(o)) return false;
        BaseDomainMessageModelMetadata<?> that = (BaseDomainMessageModelMetadata<?>) o;
        return Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), metadata);
    }

    protected UseCaseMetadata metadata;

    public UseCaseMetadata getMetadata() {
        return metadata;
    }

    public static abstract class UseCaseMessageMetadataBuilder
            <SELF extends UseCaseMessageMetadataBuilder<SELF, M, DM>,
                    M extends BaseDomainMessageModelMetadata<DM>,
                    DM extends BaseDomainModel>
            extends UseCaseMessageBuilderModel<SELF, M, DM> {

        public SELF setMetadata(UseCaseMetadata metadata) {
            message.metadata = metadata;
            return self();
        }
    }
}
