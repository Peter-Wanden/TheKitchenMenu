package com.example.peter.thekitchenmenu.domain.usecase;

public abstract class UseCaseDomainMessageBasePlusModel<
        DM extends UseCaseDomainModel>
        extends UseCaseDomainMessageBase {

    protected DM model;

    public DM getModel() {
        return model;
    }

    public static abstract class UseCaseMessageBuilderWithModel
            <SELF extends UseCaseMessageBuilderWithModel<SELF, M, DM>,
                    M extends UseCaseDomainMessageBasePlusModel<DM>,
                    DM extends UseCaseDomainModel>
            extends UseCaseMessageBuilder<SELF, M> {

        public SELF setModel(DM model) {
            message.model = model;
            return self();
        }
    }
}
