package com.example.peter.thekitchenmenu.ui;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.peter.thekitchenmenu.data.repository.DatabaseInjection;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipe;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus.UseCaseConversionFactorStatus;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipeingredientlist.UseCaseRecipeIngredientList;
import com.example.peter.thekitchenmenu.ui.bindingadapters.unitofmeasure.MeasurementToStringFormatter;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.RecipeListDataInteractor;
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
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.RecipeCatalogViewModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeEditorViewModel;
import com.example.peter.thekitchenmenu.ui.utils.TextValidationHandler;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportioncalculator.UseCasePortionCalculator;

public class ViewModelFactoryRecipe extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactoryRecipe INSTANCE;
    private final Application application;
    private final RepositoryRecipe recipeRepository;
    private final RepositoryRecipeCourse recipeCourseRepository;
    private final RepositoryRecipeIdentity recipeIdentityRepository;
    private final RepositoryRecipeDuration recipeDurationRepository;
    private final RepositoryRecipePortions recipePortionsRepository;
    private final RepositoryRecipeIngredient recipeIngredientRepository;
    private final RepositoryIngredient ingredientRepository;

    private ViewModelFactoryRecipe(Application application,
                                   RepositoryRecipe recipeRepository,
                                   RepositoryRecipeCourse recipeCourseRepository,
                                   RepositoryRecipeIdentity recipeIdentityRepository,
                                   RepositoryRecipeDuration recipeDurationRepository,
                                   RepositoryRecipePortions recipePortionsRepository,
                                   RepositoryRecipeIngredient recipeIngredientRepository,
                                   RepositoryIngredient ingredientRepository) {
        this.application = application;
        this.recipeRepository = recipeRepository;
        this.recipeCourseRepository = recipeCourseRepository;
        this.recipeIdentityRepository = recipeIdentityRepository;
        this.recipeDurationRepository = recipeDurationRepository;
        this.recipePortionsRepository = recipePortionsRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.ingredientRepository = ingredientRepository;
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
                            ),
                            DatabaseInjection.provideRecipeDurationDataSource(
                                    application.getApplicationContext()
                            ),
                            DatabaseInjection.provideRecipePortionsDataSource(
                                    application.getApplicationContext()
                            ),
                            DatabaseInjection.provideRecipeIngredientDataSource(
                                    application.getApplicationContext()
                            ),
                            DatabaseInjection.provideIngredientDataSource(
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
                    new RecipeListDataInteractor(
                            recipeIdentityRepository,
                            recipeDurationRepository
                    )
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
                    recipeIdentityRepository,
                    new TimeProvider(),
                    application.getResources(),
                    new TextValidationHandler()
            );
        } else if (modelClass.isAssignableFrom(RecipeCourseEditorViewModel.class)) {
            //noinspection unchecked
            return (T) new RecipeCourseEditorViewModel(
                    recipeCourseRepository,
                    new UniqueIdProvider()
            );
        } else if (modelClass.isAssignableFrom(RecipeDurationEditorViewModel.class)) {
            // noinspection unchecked
            return (T) new RecipeDurationEditorViewModel(
                    recipeDurationRepository,
                    application.getResources(),
                    new TimeProvider()
            );
        } else if (modelClass.isAssignableFrom(RecipePortionsEditorViewModel.class)) {
            // noinspection unchecked
            return (T) new RecipePortionsEditorViewModel(
                    recipePortionsRepository,
                    new TimeProvider(),
                    new UniqueIdProvider(),
                    application.getResources()
            );
        } else if (modelClass.isAssignableFrom(RecipeIngredientMeasurementViewModel.class)) {
            // noinspection unchecked
            return (T) new RecipeIngredientMeasurementViewModel(
                    UseCaseHandler.getInstance(),
                    getPortionsUseCase(),
                    getConversionFactorStatusUseCase(),
                    application.getResources(),
                    new NumberFormatter(application.getResources()),
                    new MeasurementErrorMessageMaker(application.getResources(),
                            new NumberFormatter(application.getResources()))
            );
        } else if (modelClass.isAssignableFrom(RecipeNameAndPortionsViewModel.class)) {
            // noinspection unchecked
            return (T) new RecipeNameAndPortionsViewModel(
                    recipeIdentityRepository,
                    recipePortionsRepository
            );
        } else if (modelClass.isAssignableFrom(RecipeIngredientListViewModel.class)) {
            // noinspection unchecked
            return (T) new RecipeIngredientListViewModel(
                    UseCaseHandler.getInstance(),
                    getRecipeIngredientListUseCase()
            );
        } else if (modelClass.isAssignableFrom(RecipeIngredientListItemViewModel.class)) {
            // noinspection unchecked
            return (T) new RecipeIngredientListItemViewModel(
                    new MeasurementToStringFormatter(application.getResources())
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }

    private UseCasePortionCalculator getPortionsUseCase() {
        UseCaseFactory factory = UseCaseFactory.getInstance(application);
        return factory.providePortionsUseCase();
    }

    private UseCaseConversionFactorStatus getConversionFactorStatusUseCase() {
        UseCaseFactory factory = UseCaseFactory.getInstance(application);
        return factory.provideConversionFactorUseCase();
    }

    private UseCaseRecipeIngredientList getRecipeIngredientListUseCase() {
        UseCaseFactory factory = UseCaseFactory.getInstance(application);
        return factory.provideRecipeIngredientListUseCase();
    }
}
