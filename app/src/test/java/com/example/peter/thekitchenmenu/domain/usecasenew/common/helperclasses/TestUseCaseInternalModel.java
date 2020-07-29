package com.example.peter.thekitchenmenu.domain.usecasenew.common.helperclasses;

import com.example.peter.thekitchenmenu.domain.usecasenew.model.DomainModel;

import javax.annotation.Nonnull;

public final class TestUseCaseInternalModel
        implements
        DomainModel.UseCaseModel {

    @Nonnull
    private String useCaseModelString;

    public TestUseCaseInternalModel(@Nonnull String useCaseModelString) {
        this.useCaseModelString = useCaseModelString;
    }

    @Nonnull
    public String getUseCaseModelString() {
        return useCaseModelString;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TestUseCaseInternalModel)) return false;

        TestUseCaseInternalModel that = (TestUseCaseInternalModel) o;

        return useCaseModelString.equals(that.useCaseModelString);
    }

    @Override
    public int hashCode() {
        return useCaseModelString.hashCode();
    }

    @Nonnull
    @Override
    public String toString() {
        return "TestUseCaseDomainModel{" +
                "useCaseModelString='" + useCaseModelString + '\'' +
                '}';
    }
}
