package com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses;

import com.example.peter.thekitchenmenu.domain.model.DomainModel;

import javax.annotation.Nonnull;

public class TestUseCaseModel
        implements
        DomainModel.UseCaseModel {
    private String useCaseModelString = "useCaseModelString";

    public TestUseCaseModel() {
    }

    public TestUseCaseModel(String useCaseModelString) {
        this.useCaseModelString = useCaseModelString;
    }

    public String getUseCaseModelString() {
        return useCaseModelString;
    }

    @Nonnull
    @Override
    public String toString() {
        return "TestUseCaseDomainModel{" +
                "useCaseModelString='" + useCaseModelString + '\'' +
                '}';
    }
}
