package com.example.peter.thekitchenmenu.domain.usecase.recipe;

public abstract class RecipeRequestModel {

    public static class RecipeRequestModelBuilder<
            SELF extends RecipeRequestModelBuilder<SELF, M>,
            M extends RecipeRequestModel> {

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
