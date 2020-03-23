package com.example.peter.thekitchenmenu.domain.usecase.recipe.component;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeDataModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequestAbstract;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;

import java.util.Objects;

/**
 * The base class for a recipe component request. Component requests should always be routed
 * through a {@link Recipe}.
 * All recipe component requests require a recipe ID and a data structure (model) of the business
 * data relating to the request. See classes extending this abstract class for details of the
 * the data models and the implementation pattern for the builders.
 * @param <M> the business data model
 */
public abstract class RecipeComponentRequest<M extends RecipeDataModel> extends RecipeRequestAbstract {

    protected M model;

    public M getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeComponentRequest<?> that = (RecipeComponentRequest<?>) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, model);
    }

    /**
     * Base class for a {@link RecipeComponentRequest} builder.
     * @param <SELF> the builder class extending this abstract builder.
     * @param <R> the request class extending the {@link RecipeComponentRequest} for the builder.
     * @param <M> the the {@link RecipeDataModel} for the {@link RecipeComponentRequest}.
     */
    public static abstract class RecipeRequestBuilder
                    <SELF extends RecipeRequestBuilder<SELF, R, M>,
                    R extends RecipeComponentRequest<M>,
                    M extends RecipeDataModel> {

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

        @SuppressWarnings("unchecked")
        protected SELF self() {
            return (SELF) this;
        }
    }
}
