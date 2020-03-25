package com.example.peter.thekitchenmenu.domain.usecase.recipe;

import com.example.peter.thekitchenmenu.domain.model.PersistenceModel;

public abstract class RecipePersistenceModel
        extends RecipeDataModel
        implements PersistenceModel {

    protected String id;
    protected String recipeId;

    @Override
    public String getId() {
        return id;
    }

    public String getRecipeId() {
        return recipeId;
    }
}
