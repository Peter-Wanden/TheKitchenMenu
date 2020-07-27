package com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses;

import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.message.RequestWithId;

import javax.annotation.Nonnull;

public class TestUseCaseRequest
        extends
        RequestWithId<TestUseCaseRequestModel> {

    private TestUseCaseRequest() {
    }

    @Nonnull
    @Override
    public String toString() {
        return "TestRequest{" +
                "dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", model=" + model +
                '}';
    }

    public static class Builder
            extends
            IdBuilder<Builder, TestUseCaseRequest, TestUseCaseRequestModel> {

        public Builder() {
            message = new TestUseCaseRequest();
        }

        @Override
        public Builder getDefault() {
            message.dataId = NO_ID;
            message.domainId = NO_ID;
            message.model = new TestUseCaseRequestModel.Builder().
                    getDefault().
                    build();
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
