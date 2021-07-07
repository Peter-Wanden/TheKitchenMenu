package com.example.peter.thekitchenmenu.domain.usecasenew.common.helperclasses;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.model.DomainModel;

import javax.annotation.Nonnull;

public final class TestRequestModel
        extends
        BaseDomainModel
        implements
        DomainModel.UseCaseRequestModel {

    private String requestModelString;

    private TestRequestModel() {
    }

    public String getRequestModelString() {
        return requestModelString;
    }

    @Nonnull
    @Override
    public String toString() {
        return "TestRequestModel{" +
                "requestModelString='" + requestModelString + '\'' +
                '}';
    }

    public static final class Builder
            extends
            BaseDomainModelBuilder<Builder, TestRequestModel> {

        public Builder() {
            super(new TestRequestModel());
        }

        @Override
        public Builder basedOnModel(TestRequestModel model) {
            domainModel.requestModelString = model.requestModelString;
            return self();
        }

        public Builder setRequestModelString(String requestModelString) {
            domainModel.requestModelString = requestModelString;
            return self();
        }

        @Override
        public Builder getDefault() {
            domainModel.requestModelString = "";
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
