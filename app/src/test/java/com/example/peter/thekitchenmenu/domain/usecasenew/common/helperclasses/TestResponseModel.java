package com.example.peter.thekitchenmenu.domain.usecasenew.common.helperclasses;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;

import javax.annotation.Nonnull;

public final class TestResponseModel
        extends
        BaseDomainModel
        implements
        DomainModel.UseCaseResponseModel {

    private String responseModelString = "responseModelString";

    private TestResponseModel() {
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
            BaseDomainModelBuilder<Builder, TestResponseModel> {

        public Builder() {
            super(new TestResponseModel());
        }

        @Override
        public Builder basedOnModel(TestResponseModel model) {
            domainModel.responseModelString = model.getResponseModelString();
            return self();
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
