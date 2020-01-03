package com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;

import java.util.Objects;

public class ConversionFactorStatusResponse implements UseCaseInteractor.Response {
    @NonNull
    private ConversionFactorStatus.Result result;

    public ConversionFactorStatusResponse(
            @NonNull ConversionFactorStatus.Result result) {
        this.result = result;
    }

    @NonNull
    public ConversionFactorStatus.Result getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversionFactorStatusResponse that = (ConversionFactorStatusResponse) o;
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
