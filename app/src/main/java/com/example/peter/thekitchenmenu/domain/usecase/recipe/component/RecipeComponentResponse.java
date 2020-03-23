package com.example.peter.thekitchenmenu.domain.usecase.recipe.component;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeDataModel;

import java.util.Objects;

/**
 * Base class for all recipe component responses
 * @param <DM> the recipe component data model
 */
public abstract class RecipeComponentResponse<
        DM extends RecipeDataModel>
        implements UseCase.Response {

    protected String id;
    protected RecipeComponentMetadata metadata;
    protected DM model;

    public String getId() {
        return id;
    }

    public RecipeComponentMetadata getMetadata() {
        return metadata;
    }

    public DM getModel() {
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

    /**
     * Base builder for {@link RecipeComponentResponse}'s
     * @param <SELF> the {@link RecipeComponentResponseBuilder} using this base class
     * @param <CR> the {@link RecipeComponentResponse} class being built
     * @param <DM> the {@link RecipeDataModel} for the {@link RecipeComponentResponse}
     */
    public static abstract class RecipeComponentResponseBuilder<
            SELF extends RecipeComponentResponseBuilder,
            CR extends RecipeComponentResponse,
            DM extends RecipeDataModel> {

        protected CR response;

        public abstract SELF getDefault();

        public SELF setId(String id) {
            response.id = id;
            return self();
        }

        public SELF setMetadata(RecipeComponentMetadata metadata) {
            response.metadata = metadata;
            return self();
        }

        public SELF setModel(DM model) {
            response.model = model;
            return self();
        }

        public CR build() {
            return response;
        }

        @SuppressWarnings("unchecked")
        protected SELF self() {
            return (SELF) this;
        }
    }
}
