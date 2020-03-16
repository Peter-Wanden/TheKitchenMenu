package com.example.peter.thekitchenmenu.domain.usecase.recipe;

public abstract class RecipeResponseModel {

    public static class RecipeResponseModelBuilder<
            SELF extends RecipeResponseModelBuilder<SELF, M>,
            M extends RecipeResponseModel> {

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
