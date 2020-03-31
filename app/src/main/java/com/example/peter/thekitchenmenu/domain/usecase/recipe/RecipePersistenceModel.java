package com.example.peter.thekitchenmenu.domain.usecase.recipe;

import com.example.peter.thekitchenmenu.domain.model.PersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseDomainModel;

public abstract class RecipePersistenceModel
        extends UseCaseDomainModel
        implements PersistenceModel {

    protected String dataId;
    protected String recipeId;

    @Override
    public String getDataId() {
        return dataId;
    }

    @Override
    public String getDomainId() {
        return recipeId;
    }
}
