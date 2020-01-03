package com.example.peter.thekitchenmenu.ui;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.peter.thekitchenmenu.data.repository.DatabaseInjection;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipe;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.domain.UseCaseFactory;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.ui.utils.unitofmeasure.MeasurementToSpannableConverter;
import com.example.peter.thekitchenmenu.ui.detail.common.MeasurementErrorMessageMaker;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeCourseEditorViewModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeDurationEditorViewModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeIdentityEditorViewModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredienteditor.RecipeIngredientMeasurementViewModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipePortionsEditorViewModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeValidator;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist.RecipeIngredientListItemViewModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist.RecipeNameAndPortionsViewModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist.RecipeIngredientListViewModel;
import com.example.peter.thekitchenmenu.ui.utils.NumberFormatter;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.RecipeCatalogViewModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeEditorViewModel;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

public class ViewModelFactoryRecipe extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactoryRecipe INSTANCE;
    private final Application application;
    private final UseCaseFactory useCaseFactory;
    private final UseCaseHandler useCaseHandler;
    private final RepositoryRecipe recipeRepository;

    private ViewModelFactoryRecipe(Application application,
                                   UseCaseFactory useCaseFactory,
                                   UseCaseHandler useCaseHandler,
                                   RepositoryRecipe recipeRepository) {
        this.application = application;
        this.useCaseFactory = useCaseFactory;
        this.useCaseHandler = useCaseHandler;
        this.recipeRepository = recipeRepository;
    }

    public static ViewModelFactoryRecipe getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactoryRecipe.class) {
                if (INSTANCE == null)
                    INSTANCE = new ViewModelFactoryRecipe(
                            application,
                            UseCaseFactory.getInstance(application),
                            UseCaseHandler.getInstance(),
                            DatabaseInjection.provideRecipesDataSource(
                                    application.getApplicationContext()
                            )
                    );
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        if (modelClass.isAssignableFrom(RecipeCatalogViewModel.class)) {
            //noinspection unchecked
            return (T) new RecipeCatalogViewModel(
                    useCaseHandler,
                    useCaseFactory.provideRecipeIdentityAndDurationListUseCase()
            );
        } else if (modelClass.isAssignableFrom(RecipeEditorViewModel.class)) {
            //noinspection unchecked
            return (T) new RecipeEditorViewModel(
                    new TimeProvider(),
                    recipeRepository,
                    new UniqueIdProvider(),
                    application.getResources(),
                    new RecipeValidator()
            );
        } else if (modelClass.isAssignableFrom(RecipeIdentityEditorViewModel.class)) {
            //noinspection unchecked
            return (T) new RecipeIdentityEditorViewModel(
                    useCaseHandler,
                    useCaseFactory.provideRecipeIdentityUseCase(),
                    useCaseFactory.provideTextValidatorUseCase(),
                    application.getResources()
            );
        } else if (modelClass.isAssignableFrom(RecipeCourseEditorViewModel.class)) {
            //noinspection unchecked
            return (T) new RecipeCourseEditorViewModel(
                    useCaseHandler,
                    useCaseFactory.provideRecipeCourseUseCase()
            );
        } else if (modelClass.isAssignableFrom(RecipeDurationEditorViewModel.class)) {
            // noinspection unchecked
            return (T) new RecipeDurationEditorViewModel(
                    useCaseHandler,
                    useCaseFactory.provideRecipeDurationUseCase(),
                    application.getResources()
            );
        } else if (modelClass.isAssignableFrom(RecipePortionsEditorViewModel.class)) {
            // noinspection unchecked
            return (T) new RecipePortionsEditorViewModel(
                    useCaseHandler,
                    useCaseFactory.provideRecipePortionsUseCase(),
                    application.getResources()
            );
        } else if (modelClass.isAssignableFrom(RecipeIngredientMeasurementViewModel.class)) {
            // noinspection unchecked
            return (T) new RecipeIngredientMeasurementViewModel(
                    useCaseHandler,
                    useCaseFactory.providePortionsUseCase(),
                    useCaseFactory.provideConversionFactorUseCase(),
                    application.getResources(),
                    new NumberFormatter(application.getResources()),
                    new MeasurementErrorMessageMaker(application.getResources(),
                            new NumberFormatter(application.getResources()))
            );
        } else if (modelClass.isAssignableFrom(RecipeNameAndPortionsViewModel.class)) {
            // noinspection unchecked
            return (T) new RecipeNameAndPortionsViewModel(
                    useCaseHandler,
                    useCaseFactory.provideRecipeIdentityUseCase(),
                    useCaseFactory.provideRecipePortionsUseCase()
            );
        } else if (modelClass.isAssignableFrom(RecipeIngredientListViewModel.class)) {
            // noinspection unchecked
            return (T) new RecipeIngredientListViewModel(
                    useCaseHandler,
                    useCaseFactory.provideRecipeIngredientListUseCase()
            );
        } else if (modelClass.isAssignableFrom(RecipeIngredientListItemViewModel.class)) {
            // noinspection unchecked
            return (T) new RecipeIngredientListItemViewModel(
                    new MeasurementToSpannableConverter(application.getResources())
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
