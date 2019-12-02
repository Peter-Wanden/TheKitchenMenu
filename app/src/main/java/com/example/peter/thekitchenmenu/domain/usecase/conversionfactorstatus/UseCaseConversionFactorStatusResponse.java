package com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.domain.UseCaseAbstract;

import java.util.Objects;

public class UseCaseConversionFactorStatusResponse implements UseCaseAbstract.Response {
    @NonNull
    private UseCaseConversionFactorStatus.UseCaseResult result;

    public UseCaseConversionFactorStatusResponse(
            @NonNull UseCaseConversionFactorStatus.UseCaseResult result) {
        this.result = result;
    }

    @NonNull
    public UseCaseConversionFactorStatus.UseCaseResult getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UseCaseConversionFactorStatusResponse that = (UseCaseConversionFactorStatusResponse) o;
        return result == that.result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(result);
    }

    @NonNull
    @Override
    public String toString() {
        return "Response{" +
                "result=" + result +
                '}';
    }
}
