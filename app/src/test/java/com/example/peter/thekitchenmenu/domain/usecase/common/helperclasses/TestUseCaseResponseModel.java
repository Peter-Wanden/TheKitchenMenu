package com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses;

import com.example.peter.thekitchenmenu.domain.model.BaseDomainModel;
import com.example.peter.thekitchenmenu.domain.model.DomainModel;
import com.example.peter.thekitchenmenu.domain.model.DomainModelBuilder;

public class TestUseCaseResponseModel
        extends
        BaseDomainModel
        implements
        DomainModel.UseCaseResponseModel {

    private String responseModelString = "responseModelString";

    private TestUseCaseResponseModel() {
    }

    public static class Builder
            extends
            DomainModelBuilder<Builder, TestUseCaseResponseModel> {

        @Override
        public Builder getDefault() {
            domainModel.responseModelString = "";
            return self();
        }

        @Override
        protected Builder self() {
            return this;
        }
    }
}
