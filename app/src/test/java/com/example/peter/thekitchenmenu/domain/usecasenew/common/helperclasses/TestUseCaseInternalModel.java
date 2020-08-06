package com.example.peter.thekitchenmenu.domain.usecasenew.common.helperclasses;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;

import javax.annotation.Nonnull;

public final class TestUseCaseInternalModel
        extends
        BaseDomainModel
        implements
        DomainModel.UseCaseModel {

    private String useCaseModelString;

    private TestUseCaseInternalModel() {}

    public String getUseCaseModelString() {
        return useCaseModelString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestUseCaseInternalModel)) return false;

        TestUseCaseInternalModel that = (TestUseCaseInternalModel) o;

        return useCaseModelString.equals(that.useCaseModelString);
    }

    @Override
    public int hashCode() {
        return useCaseModelString.hashCode();
    }

    @Nonnull
    @Override
    public String toString() {
        return "TestUseCaseDomainModel{" +
                "useCaseModelString='" + useCaseModelString + '\'' +
                '}';
    }

    public static class Builder
            extends
            BaseDomainModelBuilder<Builder, TestUseCaseInternalModel> {

        public Builder() {
            super(new TestUseCaseInternalModel());
        }

        public Builder setUseCaseModelString(String useCaseModelString) {
            domainModel.useCaseModelString = useCaseModelString;
            return self();
        }

        @Override
        public Builder getDefault() {
            domainModel.useCaseModelString = "";
            return self();
        }

        @Override
        public Builder basedOnModel(TestUseCaseInternalModel model) {
            domainModel.useCaseModelString = model.useCaseModelString;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
