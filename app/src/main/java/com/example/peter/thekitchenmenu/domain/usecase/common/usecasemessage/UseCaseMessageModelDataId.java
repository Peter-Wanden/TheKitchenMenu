package com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage;

import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityRequest;

import java.util.Objects;

import javax.annotation.Nonnull;

public abstract class UseCaseMessageModelDataId
        <DOMAIN_MODEL extends BaseDomainModel>
        extends
        UseCaseMessageModelBase<DOMAIN_MODEL> {

    public static final String NO_ID = "";

    // The id for an instance of state of domain data as stored in the data layer.
    protected String dataId = NO_ID;
    // The id of the domain model, eg. recipeId, productId, ingredientId etc.
    protected String domainId = NO_ID;

    public String getDataId() {
        return dataId;
    }

    public String getDomainId() {
        return domainId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UseCaseMessageModelDataId)) return false;
        if (!super.equals(o)) return false;
        UseCaseMessageModelDataId<?> that = (UseCaseMessageModelDataId<?>) o;
        return Objects.equals(dataId, that.dataId) &&
                Objects.equals(domainId, that.domainId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), dataId, domainId);
    }

    protected static abstract class UseCaseMessageModelDataIdBuilder<
            SELF extends UseCaseMessageModelDataIdBuilder<SELF, MESSAGE, DOMAIN_MODEL>,
            MESSAGE extends UseCaseMessageModelDataId<DOMAIN_MODEL>,
            DOMAIN_MODEL extends BaseDomainModel>
            extends UseCaseMessageModelBase.MessageModelBuilder<SELF, MESSAGE, DOMAIN_MODEL> {

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
