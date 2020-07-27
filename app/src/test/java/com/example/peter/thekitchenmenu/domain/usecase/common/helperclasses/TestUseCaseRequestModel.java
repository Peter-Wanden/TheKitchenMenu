package com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses;

import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.model.DomainModelBuilder;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataIdMetadata;

import javax.annotation.Nonnull;

public final class TestUseCaseRequestModel
        extends
        BaseDomainModel
        implements
        DomainModel.UseCaseRequestModel {

    private String requestModelString;

    private TestUseCaseRequestModel() {
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

    public static final class Builder extends DomainModelBuilder<Builder, TestUseCaseRequestModel> {

        public Builder() {
            domainModel = new TestUseCaseRequestModel();
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
