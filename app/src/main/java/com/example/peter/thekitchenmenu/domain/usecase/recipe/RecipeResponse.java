package com.example.peter.thekitchenmenu.domain.usecase.recipe;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;

import java.util.Objects;

public abstract class RecipeResponse<
        T extends RecipeResponseModel>
        implements UseCase.Response {

    protected String id;
    protected RecipeResponseMetadata metadata;
    protected T model;

    public String getId() {
        return id;
    }

    public RecipeResponseMetadata getMetadata() {
        return metadata;
    }

    public T getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeResponse<?> that = (RecipeResponse<?>) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(metadata, that.metadata) &&
                Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, metadata, model);
    }

    public static abstract class RecipeResponseBuilder<
            SELF extends RecipeResponseBuilder,
            R extends RecipeResponse,
            T extends RecipeResponseModel> {

        protected R response;

        public abstract SELF getDefault();

        public SELF setId(String id) {
            response.id = id;
            return self();
        }

        public SELF setMetadata(RecipeResponseMetadata metadata) {
            response.metadata = metadata;
            return self();
        }

        public SELF setModel(T model) {
            response.model = model;
            return self();
        }

        public R build() {
            return response;
        }

        protected SELF self() {
            // noinspection unchecked
            return (SELF) this;
        }
    }
}
