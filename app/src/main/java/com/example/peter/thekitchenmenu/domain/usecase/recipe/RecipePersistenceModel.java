package com.example.peter.thekitchenmenu.domain.usecase.recipe;

import com.example.peter.thekitchenmenu.domain.model.PersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainModel;

public abstract class RecipePersistenceModel
        extends UseCaseDomainModel
        implements PersistenceModel {

    protected String id;
    protected String recipeId;

    @Override
    public String getDataId() {
        return id;
    }


    public String getRecipeId() {
        return recipeId;
    }
}
