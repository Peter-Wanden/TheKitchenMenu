package com.example.peter.thekitchenmenu.ui;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseFactory;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeIngredientEditor.RecipeIngredientCalculatorViewModel;
import com.example.peter.thekitchenmenu.ui.utils.unitofmeasure.MeasurementToSpannableConverter;
import com.example.peter.thekitchenmenu.ui.detail.common.MeasurementErrorMessageMaker;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeCourseEditorViewModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeDurationEditorViewModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeIdentityEditorViewModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipePortionsEditorViewModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist.RecipeIngredientListItemViewModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist.RecipeNameAndPortionsViewModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeingredientlist.RecipeIngredientListViewModel;
import com.example.peter.thekitchenmenu.ui.utils.NumberFormatter;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.RecipeCatalogViewModel;
import com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.RecipeEditorViewModel;

import javax.annotation.Nonnull;

public class ViewModelFactoryRecipe extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactoryRecipe INSTANCE;
    @Nonnull
    private final Application application;
    @Nonnull
    private final UseCaseFactory useCaseFactory;
    @Nonnull
    private final UseCaseHandler useCaseHandler;
    private Recipe recipeMacro;

    private ViewModelFactoryRecipe(@Nonnull Application application,
                                   @Nonnull UseCaseFactory useCaseFactory,
                                   @Nonnull UseCaseHandler useCaseHandler) {
        this.application = application;
        this.useCaseFactory = useCaseFactory;
        this.useCaseHandler = useCaseHandler;
    }

    public static ViewModelFactoryRecipe getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactoryRecipe.class) {
                if (INSTANCE == null)
                    INSTANCE = new ViewModelFactoryRecipe(
                            application,
                            UseCaseFactory.getInstance(application),
                            UseCaseHandler.getInstance()
                    );
            }
        }
        return INSTANCE;
    }

    private ViewModelFactoryRecipe(@Nonnull Application application,
                                   @Nonnull Recipe recipeMacro,
                                   @Nonnull UseCaseFactory useCaseFactory,
                                   @Nonnull UseCaseHandler useCaseHandler) {
        this.application = application;
        this.recipeMacro = recipeMacro;
        this.useCaseFactory = useCaseFactory;
        this.useCaseHandler = useCaseHandler;
    }

    public static ViewModelFactoryRecipe getInstance(@Nonnull Application application,
                                                     @Nonnull Recipe recipeMacro) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactoryRecipe.class) {
                if (INSTANCE == null)
                    INSTANCE = new ViewModelFactoryRecipe(
                            application,
                            recipeMacro,
                            UseCaseFactory.getInstance(application),
                            UseCaseHandler.getInstance()
                    );
            }
        }
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {

        if (modelClass.isAssignableFrom(RecipeCatalogViewModel.class)) {
            return (T) new RecipeCatalogViewModel(
                    useCaseHandler,
                    useCaseFactory.provideRecipeIdentityAndDurationList()
            );
        } else if (modelClass.isAssignableFrom(RecipeEditorViewModel.class)) {
            return (T) new RecipeEditorViewModel(
                    useCaseHandler,
                    recipeMacro,
                    new UniqueIdProvider(),
                    application.getResources()
            );
        } else if (modelClass.isAssignableFrom(RecipeIdentityEditorViewModel.class)) {
            return (T) new RecipeIdentityEditorViewModel(
                    useCaseHandler,
                    recipeMacro,
                    application.getResources()
            );
        } else if (modelClass.isAssignableFrom(RecipeCourseEditorViewModel.class)) {
            return (T) new RecipeCourseEditorViewModel(
                    useCaseHandler,
                    recipeMacro
            );
        } else if (modelClass.isAssignableFrom(RecipeDurationEditorViewModel.class)) {
            return (T) new RecipeDurationEditorViewModel(
                    useCaseHandler,
                    recipeMacro,
                    application.getResources()
            );
        } else if (modelClass.isAssignableFrom(RecipePortionsEditorViewModel.class)) {
            return (T) new RecipePortionsEditorViewModel(
                    useCaseHandler,
                    recipeMacro,
                    application.getResources()
            );
        } else if (modelClass.isAssignableFrom(RecipeIngredientCalculatorViewModel.class)) {
            return (T) new RecipeIngredientCalculatorViewModel(
                    useCaseHandler,
                    useCaseFactory.provideIngredientCalculator(),
                    useCaseFactory.provideConversionFactorStatus(),
                    application.getResources(),
                    new NumberFormatter(application.getResources()),
                    new MeasurementErrorMessageMaker(application.getResources(),
                            new NumberFormatter(application.getResources()))
            );
        } else if (modelClass.isAssignableFrom(RecipeNameAndPortionsViewModel.class)) {
            return (T) new RecipeNameAndPortionsViewModel(
                    useCaseHandler,
                    useCaseFactory.provideRecipeIdentity(),
                    useCaseFactory.provideRecipePortions()
            );
        } else if (modelClass.isAssignableFrom(RecipeIngredientListViewModel.class)) {
            return (T) new RecipeIngredientListViewModel(
                    useCaseHandler,
                    useCaseFactory.provideRecipeIngredientList()
            );
        } else if (modelClass.isAssignableFrom(RecipeIngredientListItemViewModel.class)) {
            return (T) new RecipeIngredientListItemViewModel(
                    new MeasurementToSpannableConverter(application.getResources())
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
