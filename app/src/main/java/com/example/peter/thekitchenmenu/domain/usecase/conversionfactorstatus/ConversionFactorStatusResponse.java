package com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus;

import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class ConversionFactorStatusResponse implements UseCaseBase.Response {
    @Nonnull
    private ConversionFactorStatus.Result result;

    public ConversionFactorStatusResponse(
            @Nonnull ConversionFactorStatus.Result result) {
        this.result = result;
    }

    @Nonnull
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

    @Nonnull
    @Override
    public String toString() {
        return "Response{" +
                "result=" + result +
                '}';
    }
}
