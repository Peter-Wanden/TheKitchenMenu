package com.example.peter.thekitchenmenu.ui.detail.ingredient;

import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.ingredient.RepositoryIngredient;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientPersistenceModel;

public class IngredientViewerViewModel
        extends ViewModel
        implements DomainDataAccess.GetDomainModelCallback<IngredientPersistenceModel> {

    private RepositoryIngredient repositoryIngredient;

//    public final ObservableField<String> ingredientName = new ObservableField<>();

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
    public void onPersistenceModelLoaded(IngredientPersistenceModel model) {
        setIngredientToDisplay(model);
    }

    @Override
    public void onPersistenceModelUnavailable() {

    }

    private void setIngredientToDisplay(IngredientPersistenceModel model) {
//        ingredientName.set(model.getName());
    }
}
