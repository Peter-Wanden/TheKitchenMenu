package com.example.peter.thekitchenmenu.domain.usecasenew.common.helperclasses;

import com.example.peter.thekitchenmenu.domain.usecasenew.model.BaseDomainPersistenceModel;

import javax.annotation.Nonnull;

public final class TestUseCasePersistenceModel
        extends
        BaseDomainPersistenceModel {

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
            domainModel = new TestUseCasePersistenceModel();
        }

        @Override
        public Builder basedOnModel(TestUseCasePersistenceModel model) {
            domainModel.dataId = model.getDataId();
            domainModel.domainId = model.getDomainId();
            domainModel.persistenceModelString = model.getPersistenceModelString();
            domainModel.createDate = model.getCreateDate();
            domainModel.lastUpdate = model.getLastUpdate();
            return self();
        }

        @Override
        public Builder getDefault() {
            domainModel.dataId = "";
            domainModel.domainId = "";
            domainModel.persistenceModelString = "";
            domainModel.createDate = 0;
            domainModel.lastUpdate = 0;
            return self();
        }

        public Builder setPersistenceModelString(String persistenceModelString) {
            domainModel.persistenceModelString = persistenceModelString;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
