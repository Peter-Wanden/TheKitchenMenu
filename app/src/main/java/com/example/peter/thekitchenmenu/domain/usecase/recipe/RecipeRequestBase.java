package com.example.peter.thekitchenmenu.domain.usecase.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;

import java.util.Objects;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequestBase.*;

public abstract class RecipeRequestBase<M extends RecipeRequestModel> implements UseCase.Request {

    public interface RecipeRequestModel {
    }

    @Nonnull
    protected String id;
    @Nonnull
    protected M model;

    public RecipeRequestBase(@Nonnull String id,
                             @Nonnull M model) {
        this.id = id;
        this.model = model;
    }

    @Nonnull
    public String getId() {
        return id;
    }

    public abstract M getModel();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeRequestBase<?> that = (RecipeRequestBase<?>) o;
        return id.equals(that.id) &&
                model.equals(that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, model);
    }

    public static abstract class Builder<SELF extends Builder<SELF>> {
        protected String id;
        protected RecipeRequestModel model;

        public abstract SELF getDefault();

        public SELF setId(String id) {
            this.id = id;
            return self();
        }

        public SELF setModel(RecipeRequestModel model) {
            this.model = model;
            return self();
        }

        protected SELF self() {
            // noinspection unchecked
            return (SELF) this;
        }

        public abstract UseCase.Request build();
    }


}
