package com.example.peter.thekitchenmenu.domain.usecase;

import java.util.Objects;

public abstract class MessageModelDataId<DOMAIN_MODEL extends BaseDomainModel>
        extends
        MessageModelBase<DOMAIN_MODEL> {

    // The id for an instance of state of domain data as stored in the data layer.
    protected String dataId;
    // The id of the domain model, eg. recipeId, productId, ingredientId etc.
    protected String domainId;

    public String getDataId() {
        return dataId;
    }

    public String getDomainId() {
        return domainId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageModelDataId)) return false;
        if (!super.equals(o)) return false;
        MessageModelDataId<?> that = (MessageModelDataId<?>) o;
        return Objects.equals(dataId, that.dataId) &&
                Objects.equals(domainId, that.domainId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dataId, domainId);
    }

    public static abstract class MessageModelDataIdBuilder
            <SELF extends MessageModelDataIdBuilder<SELF, MESSAGE, DOMAIN_MODEL>,
                    MESSAGE extends MessageModelDataId<DOMAIN_MODEL>,
                    DOMAIN_MODEL extends BaseDomainModel>
            extends MessageModelBase.MessageModelBuilder<SELF, MESSAGE, DOMAIN_MODEL> {

        public SELF setDataId(String dataId) {
            message.dataId = dataId;
            return self();
        }

        public SELF setDomainId(String domainId) {
            message.domainId = domainId;
            return self();
        }
    }
}