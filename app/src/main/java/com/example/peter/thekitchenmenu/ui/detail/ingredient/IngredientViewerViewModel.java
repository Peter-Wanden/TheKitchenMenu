package com.example.peter.thekitchenmenu.ui.detail.ingredient;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.ingredient.RepositoryIngredient;

public class IngredientViewerViewModel
        extends ViewModel
        implements PrimitiveDataSource.GetEntityCallback<IngredientEntity> {

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
    public void onEntityLoaded(IngredientEntity entity) {
        setIngredientToDisplay(entity);
    }

    @Override
    public void onDataUnavailable() {

    }

    private void setIngredientToDisplay(IngredientEntity ingredient) {
        ingredientName.set(ingredient.getName());
    }
}
