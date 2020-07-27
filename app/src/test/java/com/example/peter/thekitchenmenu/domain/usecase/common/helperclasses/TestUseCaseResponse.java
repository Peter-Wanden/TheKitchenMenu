package com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses;

import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataIdMetadata;

import javax.annotation.Nonnull;

public class TestUseCaseResponse
        extends
        UseCaseMessageModelDataIdMetadata<TestUseCaseResponseModel>
        implements
        UseCaseBase.Response {

    @Nonnull
    @Override
    public String toString() {
        return "TestUseCaseResponse{" +
                "metadata=" + metadata +
                ", dataId='" + dataId + '\'' +
                ", domainId='" + domainId + '\'' +
                ", model=" + model +
                '}';
    }

    public static class Builder
            extends
            MessageModelDataIdMetadataBuilder<
                    Builder,
                    TestUseCaseResponse,
                    TestUseCaseResponseModel> {

        public Builder() {
            message = new TestUseCaseResponse();
        }

        @Override
        public Builder getDefault() {
            message.dataId = "";
            message.domainId = "";
            message.model = new TestUseCaseResponseModel.Builder().getDefault().build();
            message.metadata = new UseCaseMetadataModel.Builder().getDefault().build();
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
