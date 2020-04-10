package com.example.peter.thekitchenmenu.ui.detail.ingredient;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.ingredient.RepositoryIngredient;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientPersistenceModel;

public class IngredientViewerViewModel
        extends ViewModel
        implements DomainDataAccess.GetDomainModelCallback<IngredientPersistenceModel> {

    private RepositoryIngredient repositoryIngredient;

    public final ObservableField<String> ingredientName = new ObservableField<>();

    public IngredientViewerViewModel(RepositoryIngredient repositoryIngredient) {
        this.repositoryIngredient = repositoryIngredient;
    }

    public void start(String ingredientId) {
        getIngredient(ingredientId);
    }

    private void getIngredient(String ingredientId) {
        repositoryIngredient.getByDataId(ingredientId, this);
    }

    @Override
    public void onModelLoaded(IngredientPersistenceModel model) {
        setIngredientToDisplay(model);
    }

    @Override
    public void onModelUnavailable() {

    }

    private void setIngredientToDisplay(IngredientPersistenceModel model) {
        ingredientName.set(model.getName());
    }
}
