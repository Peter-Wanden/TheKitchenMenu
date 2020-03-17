package com.example.peter.thekitchenmenu.domain.usecase.recipe.component;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeDataModel;

import java.util.Objects;

public abstract class RecipeComponentResponse<
        T extends RecipeDataModel>
        implements UseCase.Response {

    protected String id;
    protected RecipeComponentMetadata metadata;
    protected T model;

    public String getId() {
        return id;
    }

    public RecipeComponentMetadata getMetadata() {
        return metadata;
    }

    public T getModel() {
        return model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeComponentResponse<?> that = (RecipeComponentResponse<?>) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(metadata, that.metadata) &&
                Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, metadata, model);
    }

    public static abstract class RecipeComponentResponseBuilder<
            SELF extends RecipeComponentResponseBuilder,
            R extends RecipeComponentResponse,
            T extends RecipeDataModel> {

        protected R response;

        public abstract SELF getDefault();

        public SELF setId(String id) {
            response.id = id;
            return self();
        }

        public SELF setMetadata(RecipeComponentMetadata metadata) {
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
