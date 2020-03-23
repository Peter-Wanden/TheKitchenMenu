package com.example.peter.thekitchenmenu.domain.usecase.recipe;

/**
 * Base class for a recipe data model
 */
public abstract class RecipeDataModel {
    /**
     * Base class for an {@link RecipeDataModelBuilder}
     * Always override self() in extending classes to return 'this'.
     * The use of self in this manner will return the last class in the inheritance tree.
     * @param <SELF> the {@link RecipeDataModelBuilder} extending this base builder.
     * @param <M> the {@link RecipeDataModel} being built.
     */
    public static class RecipeDataModelBuilder<
            SELF extends RecipeDataModelBuilder<SELF, M>,
            M extends RecipeDataModel> {

        protected M model;

        protected SELF self() {
            // noinspection unchecked
            return (SELF) this;
        }

        public M build() {
            return model;
        }
    }
}
