package com.example.peter.thekitchenmenu.domain.usecase.ingredient;

import com.example.peter.thekitchenmenu.domain.UseCaseCommand;

import java.util.Objects;

import javax.annotation.Nonnull;

public final class IngredientResponse implements UseCaseCommand.Response {
    @Nonnull
    private final Ingredient.Result result;
    @Nonnull
    private final IngredientModel model;

    public IngredientResponse(@Nonnull Ingredient.Result result, @Nonnull IngredientModel model) {
        this.result = result;
        this.model = model;
    }

    @Nonnull
    public Ingredient.Result getResult() {
        return result;
    }

    @Nonnull
    public IngredientModel getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientResponse response = (IngredientResponse) o;
        return result == response.result &&
                model.equals(response.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(result, model);
    }

    @Nonnull
    @Override
    public String toString() {
        return "Response{" +
                "result=" + result +
                ", IngredientModel=" + model +
                '}';
    }
}
