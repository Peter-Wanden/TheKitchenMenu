package com.example.peter.thekitchenmenu.domain.usecase.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;

import java.util.Objects;

public abstract class RecipeRequest<M extends RecipeRequestModel> implements UseCase.Request {

    protected String id;
    protected M model;

    public String getId() {
        return id;
    }

    public M getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeRequest<?> that = (RecipeRequest<?>) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, model);
    }

    public static abstract class RecipeRequestBuilder
                    <SELF extends RecipeRequestBuilder<SELF, R, M>,
                    R extends RecipeRequest<M>,
                    M extends RecipeRequestModel> {

        protected R request;

        public abstract SELF getDefault();

        public SELF setId(String id) {
            request.id = id;
            return self();
        }

        public SELF setModel(M model) {
            request.model = model;
            return self();
        }

        public R build() {
            return request;
        }

        protected SELF self() {
            // noinspection unchecked
            return (SELF) this;
        }
    }
}
