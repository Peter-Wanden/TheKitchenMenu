package com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage;

import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;

import java.util.Objects;

/**
 * Base class for all request and response messages which operate on domain data.
 */
public abstract class UseCaseMessageModelBase<DOMAIN_MODEL extends BaseDomainModel>
        implements
        UseCaseBase.Message {

    protected DOMAIN_MODEL model;

    public DOMAIN_MODEL getDomainModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UseCaseMessageModelBase)) return false;
        UseCaseMessageModelBase<?> that = (UseCaseMessageModelBase<?>) o;
        return Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model);
    }

    public static abstract class MessageModelBuilder
            <SELF extends MessageModelBuilder<SELF, MESSAGE, DOMAIN_MODEL>,
                    MESSAGE extends UseCaseMessageModelBase<DOMAIN_MODEL>,
                    DOMAIN_MODEL extends BaseDomainModel> {

        protected MESSAGE message;

        public SELF setDomainModel(DOMAIN_MODEL model) {
            message.model = model;
            return self();
        }

        protected SELF self() {
            // noinspection unchecked
            return (SELF) this;
        }

        public abstract SELF getDefault();

        public MESSAGE build() {
            return message;
        }
    }
}
