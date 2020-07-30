package com.example.peter.thekitchenmenu.domain.usecasenew.common.helperclasses;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseRequest;

public final class TestRequest
        extends
        UseCaseRequest<TestRequestModel> {

    public static class Builder
            extends
            UseCaseRequest.Builder<Builder, TestRequest, TestRequestModel> {

        public Builder() {
            super(new TestRequest());
        }

        @Override
        public Builder self() {
            return this;
        }
    }
}
