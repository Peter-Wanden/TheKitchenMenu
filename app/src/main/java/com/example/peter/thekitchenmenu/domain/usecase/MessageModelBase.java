package com.example.peter.thekitchenmenu.domain.usecase;

import java.util.Objects;

/**
 * Base class for all request and response messages which operate on domain data.
 */
public abstract class MessageModelBase<DOMAIN_MODEL extends BaseDomainModel>
        implements
        UseCaseBase.Message {

    protected DOMAIN_MODEL model;

    public DOMAIN_MODEL getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MessageModelBase)) return false;
        MessageModelBase<?> that = (MessageModelBase<?>) o;
        return Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model);
    }

    public static abstract class MessageModelBuilder
            <SELF extends MessageModelBuilder<SELF, MESSAGE, DOMAIN_MODEL>,
                    MESSAGE extends MessageModelBase<DOMAIN_MODEL>,
                    DOMAIN_MODEL extends BaseDomainModel> {

        protected MESSAGE message;

        public SELF setModel(DOMAIN_MODEL model) {
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
