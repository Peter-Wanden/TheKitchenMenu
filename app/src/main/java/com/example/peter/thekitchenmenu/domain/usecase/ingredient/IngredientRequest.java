package com.example.peter.thekitchenmenu.domain.usecase.ingredient;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class IngredientRequest implements UseCase.Request {
    @Nonnull
    private final IngredientModel model;

    public IngredientRequest(@Nonnull IngredientModel model) {
        this.model = model;
    }

    @Nonnull
    public IngredientModel getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientRequest request = (IngredientRequest) o;
        return model.equals(request.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(model);
    }

    @Override
    public String toString() {
        return "Request{" +
                "model=" + model +
                '}';
    }
}
