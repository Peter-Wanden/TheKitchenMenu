package com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses;

import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.model.DomainModelBuilder;

import javax.annotation.Nonnull;

public final class TestUseCaseResponseModel
        extends
        BaseDomainModel
        implements
        DomainModel.UseCaseResponseModel {

    private String responseModelString = "responseModelString";

    private TestUseCaseResponseModel() {
    }

    public String getResponseModelString() {
        return responseModelString;
    }

    @Nonnull
    @Override
    public String toString() {
        return "TestUseCaseResponseModel{" +
                "responseModelString='" + responseModelString + '\'' +
                '}';
    }

    public static class Builder
            extends
            DomainModelBuilder<Builder, TestUseCaseResponseModel> {

        public Builder() {
            domainModel = new TestUseCaseResponseModel();
        }

        @Override
        public Builder getDefault() {
            domainModel.responseModelString = "";
            return self();
        }

        public Builder setResponseModelString(String responseModelString) {
            domainModel.responseModelString = responseModelString;
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
