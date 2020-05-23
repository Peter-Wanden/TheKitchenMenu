package com.example.peter.thekitchenmenu.domain.usecase;

import java.util.Objects;

public abstract class MessageModelDataIdMetadata<DOMAIN_MODEL extends BaseDomainModel>
        extends
        MessageModelDataId<DOMAIN_MODEL> {

    protected UseCaseMetadataModel metadata;

    public UseCaseMetadataModel getMetadata() {
        return metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageModelDataIdMetadata)) return false;
        if (!super.equals(o)) return false;
        MessageModelDataIdMetadata<?> that = (MessageModelDataIdMetadata<?>) o;
        return Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), metadata);
    }

    public static abstract class MessageModelDataIdMetadataBuilder
            <SELF extends MessageModelDataIdMetadataBuilder<SELF, MESSAGE, DATA_MODEL>,
                    MESSAGE extends MessageModelDataIdMetadata<DATA_MODEL>,
                    DATA_MODEL extends BaseDomainModel>
            extends MessageModelDataId.MessageModelDataIdBuilder<SELF, MESSAGE, DATA_MODEL> {

        public SELF setMetadata(UseCaseMetadataModel metadata) {
            message.metadata = metadata;
            return self();
        }
    }
}
