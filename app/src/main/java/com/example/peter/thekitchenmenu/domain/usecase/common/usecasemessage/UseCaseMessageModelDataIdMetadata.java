package com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.model.BaseDomainModel;

import java.util.Objects;

public abstract class UseCaseMessageModelDataIdMetadata<DOMAIN_MODEL extends BaseDomainModel>
        extends
        UseCaseMessageModelDataId<DOMAIN_MODEL> {

    protected UseCaseMetadataModel metadata;

    public UseCaseMetadataModel getMetadata() {
        return metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UseCaseMessageModelDataIdMetadata)) return false;
        if (!super.equals(o)) return false;
        UseCaseMessageModelDataIdMetadata<?> that = (UseCaseMessageModelDataIdMetadata<?>) o;
        return Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), metadata);
    }

    public static abstract class MessageModelDataIdMetadataBuilder
            <SELF extends MessageModelDataIdMetadataBuilder<SELF, MESSAGE, DATA_MODEL>,
                    MESSAGE extends UseCaseMessageModelDataIdMetadata<DATA_MODEL>,
                    DATA_MODEL extends BaseDomainModel>
            extends UseCaseMessageModelDataIdBuilder<SELF, MESSAGE, DATA_MODEL> {

        public SELF setMetadata(UseCaseMetadataModel metadata) {
            message.metadata = metadata;
            return self();
        }
    }
}
