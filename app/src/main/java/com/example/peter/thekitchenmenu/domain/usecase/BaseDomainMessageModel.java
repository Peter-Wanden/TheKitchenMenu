package com.example.peter.thekitchenmenu.domain.usecase;

public abstract class BaseDomainMessageModel<DM extends BaseDomainModel>
        extends BaseDomainMessage {

    protected DM model;

    public DM getModel() {
        return model;
    }

    public static abstract class UseCaseMessageBuilderModel
            <SELF extends UseCaseMessageBuilderModel<SELF, M, DM>, // SELF - The builder class
                    M extends BaseDomainMessageModel<DM>, // The class being built
                    DM extends BaseDomainModel> // The domain model
            extends UseCaseMessageBuilder<SELF, M> {

        public SELF setModel(DM model) {
            message.model = model;
            return self();
        }
    }
}
