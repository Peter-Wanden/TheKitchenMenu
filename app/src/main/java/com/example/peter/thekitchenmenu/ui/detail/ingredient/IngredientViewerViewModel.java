package com.example.peter.thekitchenmenu.ui.detail.ingredient;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;

public class IngredientViewerViewModel extends ViewModel {

    private RepositoryIngredient repositoryIngredient;

    public final ObservableField<String> ingredientName = new ObservableField<>();

    public IngredientViewerViewModel(RepositoryIngredient repositoryIngredient) {
        this.repositoryIngredient = repositoryIngredient;
    }

    public void start(String ingredientId) {
        getIngredient(ingredientId);
    }

    private void getIngredient(String ingredientId) {
        repositoryIngredient.getById(
                ingredientId,
                new DataSource.GetEntityCallback<IngredientEntity>() {
            @Override
            public void onEntityLoaded(IngredientEntity ingredient) {
                setIngredientToDisplay(ingredient);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void setIngredientToDisplay(IngredientEntity ingredient) {
        ingredientName.set(ingredient.getName());
    }
}
