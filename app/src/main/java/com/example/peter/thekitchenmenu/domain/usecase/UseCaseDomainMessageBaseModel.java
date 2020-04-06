package com.example.peter.thekitchenmenu.domain.usecase;

import javax.annotation.Nonnull;

public abstract class UseCaseDomainMessageBaseModel<DM extends UseCaseDomainModel>
        extends UseCaseDomainMessageBase {

    protected DM model;

    public DM getModel() {
        return model;
    }

    @Nonnull
    @Override
    public String toString() {
        return "UseCaseDomainMessageBaseModel{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", model=" + model +
                '}';
    }

    public static abstract class UseCaseMessageBuilderModel
            <SELF extends UseCaseMessageBuilderModel<SELF, M, DM>, // SELF - The builder class
                    M extends UseCaseDomainMessageBaseModel<DM>, // The class being built
                    DM extends UseCaseDomainModel> // The domain model
            extends UseCaseMessageBuilder<SELF, M> {

        public SELF setModel(DM model) {
            message.model = model;
            return self();
        }
    }
}
