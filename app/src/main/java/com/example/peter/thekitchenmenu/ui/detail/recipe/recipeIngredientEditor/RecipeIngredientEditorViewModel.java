package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeIngredientEditor;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.ViewModel;

import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIngredientEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.utils.unitofmeasure.UnitOfMeasure;

import java.util.List;

public class RecipeIngredientEditorViewModel extends ViewModel {

    private RecipeIngredientEditorNavigator navigator;
    private RepositoryRecipeIdentity repositoryRecipeIdentity;
    private RepositoryIngredient repositoryIngredient;
    private RepositoryRecipePortions repositoryRecipePortions;
    private RepositoryRecipeIngredient repositoryRecipeIngredient;

    public final ObservableField<String> recipeTitleObservable = new ObservableField<>();
    public final ObservableField<String> servingsObservable = new ObservableField<>();
    public final ObservableField<String> sittingsObservable = new ObservableField<>();
    public final ObservableField<String> portionsObservable = new ObservableField<>();
    public final ObservableField<String> ingredientObservable = new ObservableField<>();
    public final ObservableField<MeasurementSubtype> subtypeObservable = new ObservableField<>();

    private String recipeId;
    private String ingredientId;
    private RecipeIngredientEntity ingredientEntity;
    private UnitOfMeasure unitOfMeasure;

    public RecipeIngredientEditorViewModel(RepositoryRecipeIdentity repositoryRecipeIdentity,
                                           RepositoryRecipePortions repositoryRecipePortions,
                                           RepositoryIngredient repositoryIngredient,
                                           RepositoryRecipeIngredient repositoryRecipeIngredient) {
        this.repositoryRecipeIdentity = repositoryRecipeIdentity;
        this.repositoryRecipePortions = repositoryRecipePortions;
        this.repositoryIngredient = repositoryIngredient;
        this.repositoryRecipeIngredient = repositoryRecipeIngredient;
    }

    public void setNavigator(RecipeIngredientEditorActivity navigator) {
        this.navigator = navigator;
        subtypeObservable.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                newUnitOfMeasureSelected();
            }
        });
    }

    void onActivityDestroyed() {
        navigator = null;
    }

    public void start(String recipeId, String ingredientId) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        setRecipeIdentity();
        setRecipePortions();
        setIngredient();
    }

    private void setRecipeIdentity() {
        repositoryRecipeIdentity.getById(
                recipeId,
                new DataSource.GetEntityCallback<RecipeIdentityEntity>() {
            @Override
            public void onEntityLoaded(RecipeIdentityEntity identityEntity) {
                recipeTitleObservable.set(identityEntity.getTitle());
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void setRecipePortions() {
        repositoryRecipePortions.getPortionsForRecipe(
                recipeId,
                new DataSource.GetEntityCallback<RecipePortionsEntity>() {
            @Override
            public void onEntityLoaded(RecipePortionsEntity portionsEntity) {
                int servings = portionsEntity.getServings();
                int sittings = portionsEntity.getSittings();
                servingsObservable.set(String.valueOf(servings));
                sittingsObservable.set(String.valueOf(sittings));
                portionsObservable.set(String.valueOf(servings * sittings));
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void setIngredient() {
        repositoryIngredient.getById(
                ingredientId,
                new DataSource.GetEntityCallback<IngredientEntity>() {
            @Override
            public void onEntityLoaded(IngredientEntity ingredientEntity) {
                ingredientObservable.set(ingredientEntity.getName());
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void setRecipeIngredient() {
        repositoryRecipeIngredient.getByRecipeId(
                recipeId,
                new DataSource.GetAllCallback<RecipeIngredientEntity>() {
            @Override
            public void onAllLoaded(List<RecipeIngredientEntity> recipeIngredientEntities) {
                for (RecipeIngredientEntity ingredientEntity : recipeIngredientEntities)
                    if (ingredientEntity.getIngredientId().equals(ingredientId))
                        displayRecipeIngredientMeasurement(ingredientEntity);
                    else
                        setupForNewRecipeIngredient();
            }

            @Override
            public void onDataNotAvailable() {
                setupForNewRecipeIngredient();
            }
        });
    }

    private void displayRecipeIngredientMeasurement(RecipeIngredientEntity ingredientEntity) {
        subtypeObservable.set(
                MeasurementSubtype.values()[ingredientEntity.getUnitOfMeasureSubtype()]);

    }

    private void setupForNewRecipeIngredient() {

    }

    private void newUnitOfMeasureSelected() {
        unitOfMeasure = subtypeObservable.get().getMeasurementClass();
    }
}
