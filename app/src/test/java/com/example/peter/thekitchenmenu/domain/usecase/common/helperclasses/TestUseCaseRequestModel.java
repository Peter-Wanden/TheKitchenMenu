package com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses;

import com.example.peter.thekitchenmenu.domain.model.DomainModel;

import javax.annotation.Nonnull;

public class TestUseCaseRequestModel
        implements
        DomainModel.UseCaseRequestModel {
    private String requestModelString = "requestModelString";

    public TestUseCaseRequestModel() {
    }

    public TestUseCaseRequestModel(String requestModelString) {
        this.requestModelString = requestModelString;
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
}
