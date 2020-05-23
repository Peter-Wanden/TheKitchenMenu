package com.example.peter.thekitchenmenu.domain.usecase;

import java.util.Objects;

public abstract class BaseDomainMessageModel<DM extends BaseDomainModel>
        extends
        BaseDomainMessage {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseDomainMessageModel)) return false;
        if (!super.equals(o)) return false;
        BaseDomainMessageModel<?> that = (BaseDomainMessageModel<?>) o;
        return Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), model);
    }

    protected DM model;

    public DM getModel() {
        return model;
    }

    public static abstract class UseCaseMessageBuilderModel
            <SELF extends UseCaseMessageBuilderModel<SELF, M, DM>, // SELF - The builder class
                    M extends BaseDomainMessageModel<DM>, // M - The class being built
                    DM extends BaseDomainModel> // DM - The domain model
            extends UseCaseMessageBuilder<SELF, M> {

        public SELF setModel(DM model) {
            message.model = model;
            return self();
        }
    }
}
