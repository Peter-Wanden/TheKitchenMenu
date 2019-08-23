package com.example.peter.thekitchenmenu.ui;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.peter.thekitchenmenu.data.repository.DatabaseInjection;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipe;
import com.example.peter.thekitchenmenu.provider.TimeProvider;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.RecipeCatalogViewModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeEditorViewModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeIdentityViewModel;
import com.example.peter.thekitchenmenu.utils.ParseIntegerFromObservable;
import com.example.peter.thekitchenmenu.utils.TextValidationHandler;

/**
 * A creator is used to inject the product ID into the ViewModel
 */
public class ViewModelFactoryRecipe extends ViewModelProvider.NewInstanceFactory {

    private static final String TAG = "tkm-FactoryRecipeVM";

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactoryRecipe INSTANCE;
    private final Application application;
    private final RepositoryRecipe repository;

    private ViewModelFactoryRecipe(Application application, RepositoryRecipe repository) {
        this.application = application;
        this.repository = repository;
    }

    public static ViewModelFactoryRecipe getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactoryRecipe.class) {
                if (INSTANCE == null)
                    INSTANCE = new ViewModelFactoryRecipe(
                        application,
                        DatabaseInjection.provideRecipesRepository(
                                application.getApplicationContext()));
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        if (modelClass.isAssignableFrom(RecipeCatalogViewModel.class)) {
            //noinspection unchecked
            return (T) new RecipeCatalogViewModel(application, repository);

        } else if (modelClass.isAssignableFrom(RecipeEditorViewModel.class)) {
            //noinspection unchecked
            return (T) new RecipeEditorViewModel(application, new TimeProvider());

        } else if (modelClass.isAssignableFrom(RecipeIdentityViewModel.class)) {
            //noinspection unchecked
            return (T) new RecipeIdentityViewModel(
                    application.getResources(),
                    new TextValidationHandler(),
                    new ParseIntegerFromObservable());
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
