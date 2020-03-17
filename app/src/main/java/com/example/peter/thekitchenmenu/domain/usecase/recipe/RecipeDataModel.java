package com.example.peter.thekitchenmenu.domain.usecase.recipe;

public abstract class RecipeDataModel {

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
