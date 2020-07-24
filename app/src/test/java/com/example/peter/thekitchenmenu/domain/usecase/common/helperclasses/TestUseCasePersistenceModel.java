package com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses;

import com.example.peter.thekitchenmenu.domain.model.BasePersistenceModel;

import javax.annotation.Nonnull;

public final class TestUseCasePersistenceModel
        extends
        BasePersistenceModel {

    private String persistenceModelString = "PersistenceModelString";

    private TestUseCasePersistenceModel() {
    }

    public String getPersistenceModelString() {
        return persistenceModelString;
    }

    @Nonnull
    @Override
    public String toString() {
        return "TestPersistenceModel{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", createDate=" + createDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    public static class Builder
            extends
            PersistenceModelBuilder<Builder, TestUseCasePersistenceModel> {

        public Builder() {
            persistenceModel = new TestUseCasePersistenceModel();
        }

        @Override
        public Builder basedOnModel(TestUseCasePersistenceModel model) {
            persistenceModel.dataId = model.getDataId();
            persistenceModel.domainId = model.getDomainId();
            persistenceModel.persistenceModelString = model.getPersistenceModelString();
            persistenceModel.createDate = model.getCreateDate();
            persistenceModel.lastUpdate = model.getLastUpdate();
            return self();
        }

        @Override
        public Builder getDefault() {
            persistenceModel.dataId = "";
            persistenceModel.domainId = "";
            persistenceModel.persistenceModelString = "";
            persistenceModel.createDate = 0;
            persistenceModel.lastUpdate = 0;
            return self();
        }

        public Builder setPersistenceModelString(String persistenceModelString) {
            persistenceModel.persistenceModelString = persistenceModelString;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
