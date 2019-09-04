package com.example.peter.thekitchenmenu.ui;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.peter.thekitchenmenu.data.repository.DatabaseInjection;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipe;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeCourseSelectorViewModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidator;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.RecipeCatalogViewModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeEditorViewModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeIdentityViewModel;
import com.example.peter.thekitchenmenu.utils.ParseIntegerFromObservableHandler;
import com.example.peter.thekitchenmenu.utils.TextValidationHandler;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

/**
 * A creator is used to inject the product ID into the ViewModel
 */
public class ViewModelFactoryRecipe extends ViewModelProvider.NewInstanceFactory {

    private static final String TAG = "tkm-FactoryRecipeVM";

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactoryRecipe INSTANCE;
    private final Application application;
    private final RepositoryRecipe recipeRepository;
    private final RepositoryRecipeCourse recipeCourseRepository;
    private final RepositoryRecipeIdentity recipeIdentityRepository;

    private ViewModelFactoryRecipe(Application application,
                                   RepositoryRecipe recipeRepository,
                                   RepositoryRecipeCourse recipeCourseRepository,
                                   RepositoryRecipeIdentity recipeIdentityRepository) {
        this.application = application;
        this.recipeRepository = recipeRepository;
        this.recipeCourseRepository = recipeCourseRepository;
        this.recipeIdentityRepository = recipeIdentityRepository;
    }

    public static ViewModelFactoryRecipe getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactoryRecipe.class) {
                if (INSTANCE == null)
                    INSTANCE = new ViewModelFactoryRecipe(
                            application,
                            DatabaseInjection.provideRecipesDataSource(
                                    application.getApplicationContext()
                            ),
                            DatabaseInjection.provideRecipeCourseDataSource(
                                    application.getApplicationContext()
                            ),
                            DatabaseInjection.provideRecipeIdentityDataSource(
                                    application.getApplicationContext()
                            )
                    );
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
            return (T) new RecipeCatalogViewModel(
                    application,
                    recipeRepository);

        } else if (modelClass.isAssignableFrom(RecipeEditorViewModel.class)) {
            //noinspection unchecked
            return (T) new RecipeEditorViewModel(
                    new TimeProvider(),
                    recipeRepository,
                    new UniqueIdProvider(),
                    application.getResources(),
                    new RecipeValidator());

        } else if (modelClass.isAssignableFrom(RecipeIdentityViewModel.class)) {
            //noinspection unchecked
            return (T) new RecipeIdentityViewModel(
                    recipeIdentityRepository,
                    new TimeProvider(),
                    new UniqueIdProvider(),
                    application.getResources(),
                    new TextValidationHandler(),
                    new ParseIntegerFromObservableHandler());

        } else if (modelClass.isAssignableFrom(RecipeCourseSelectorViewModel.class)) {
            //noinspection unchecked
            return (T) new RecipeCourseSelectorViewModel(
                    recipeCourseRepository,
                    new UniqueIdProvider());
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
