package com.example.peter.thekitchenmenu.domain.usecase;

import android.app.Application;
import android.content.res.Resources;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.repository.DatabaseInjection;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipe;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus.ConversionFactorStatus;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.Ingredient;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequestAbstract;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipelist.RecipeList;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipeingredientlist.RecipeIngredientList;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipemacro.RecipeMacro;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions.RecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientDuplicateChecker;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.domain.usecase.recipeingredientcalculator.IngredientCalculator;

public class UseCaseFactory {

    private static final String TAG = "tkm-" + UseCaseFactory.class.getSimpleName() + ": ";

    private static volatile UseCaseFactory INSTANCE;

    private final Application application;
    private final RepositoryRecipe recipeRepository;
    private final RepositoryRecipePortions recipePortionsRepository;
    private final RepositoryRecipeIngredient recipeIngredientRepository;
    private final RepositoryIngredient ingredientRepository;
    private final RepositoryRecipeIdentity recipeIdentityRepository;
    private final RepositoryRecipeDuration recipeDurationRepository;
    private final RepositoryRecipeCourse recipeCourseRepository;

    private UseCaseFactory(Application application,
                           RepositoryRecipe recipeRepository,
                           RepositoryRecipePortions recipePortionsRepository,
                           RepositoryRecipeIngredient recipeIngredientRepository,
                           RepositoryIngredient ingredientRepository,
                           RepositoryRecipeIdentity recipeIdentityRepository,
                           RepositoryRecipeDuration recipeDurationRepository,
                           RepositoryRecipeCourse recipeCourseRepository) {
        this.application = application;
        this.recipeRepository = recipeRepository;
        this.recipePortionsRepository = recipePortionsRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.ingredientRepository = ingredientRepository;
        this.recipeIdentityRepository = recipeIdentityRepository;
        this.recipeDurationRepository = recipeDurationRepository;
        this.recipeCourseRepository = recipeCourseRepository;
    }

    public static UseCaseFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (UseCaseFactory.class) {
                if (INSTANCE == null)
                    INSTANCE = new UseCaseFactory(
                            application,
                            DatabaseInjection.provideRecipeDataSource(
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
                            ),
                            DatabaseInjection.provideRecipeIdentityDataSource(
                                    application.getApplicationContext()
                            ),
                            DatabaseInjection.provideRecipeDurationDataSource(
                                    application.getApplicationContext()
                            ),
                            DatabaseInjection.provideRecipeCourseDataSource(
                                    application.getApplicationContext()
                            )
                    );
            }
        }
        return INSTANCE;
    }

    public IngredientCalculator provideIngredientCalculator() {
        return new IngredientCalculator(
                recipePortionsRepository,
                recipeIngredientRepository,
                ingredientRepository,
                new UniqueIdProvider(),
                new TimeProvider()
        );
    }

    public ConversionFactorStatus provideConversionFactorStatus() {
        return new ConversionFactorStatus(
                ingredientRepository
        );
    }

    public RecipeIngredientList provideRecipeIngredientList() {
        return new RecipeIngredientList(
                recipeIngredientRepository,
                ingredientRepository,
                recipePortionsRepository
        );
    }

    public RecipeList provideRecipeIdentityAndDurationList() {
        return new RecipeList(
                this,
                UseCaseHandler.getInstance());
    }

    public RecipeCourse provideRecipeCourse() {
        return new RecipeCourse(
                recipeCourseRepository,
                new UniqueIdProvider(),
                new TimeProvider());
    }

    public TextValidator provideTextValidator() {
        Resources resources = application.getResources();

        return new TextValidator.Builder().
                setShortTextMinLength(
                        resources.getInteger(R.integer.input_validation_short_text_min_length)).
                setShortTextMaxLength(
                        resources.getInteger(R.integer.input_validation_short_text_max_length)).
                setLongTextMinLength(
                        resources.getInteger(R.integer.input_validation_long_text_min_length)).
                setLongTextMaxLength(
                        resources.getInteger(R.integer.input_validation_long_text_max_length)).
                build();
    }

    public Ingredient provideIngredient() {
        return new Ingredient(
                ingredientRepository,
                new UniqueIdProvider(),
                new TimeProvider(),
                new IngredientDuplicateChecker(ingredientRepository)
        );
    }

    private Recipe provideRecipe() {
        return new Recipe(
                recipeRepository,
                new TimeProvider()
        );
    }

    public RecipePortions provideRecipePortions() {
        Resources resources = application.getResources();
        return new RecipePortions(
                recipePortionsRepository,
                new UniqueIdProvider(),
                new TimeProvider(),
                resources.getInteger(R.integer.recipe_max_servings),
                resources.getInteger(R.integer.recipe_max_sittings)
        );
    }

    private RecipeDuration provideRecipeDuration() {
        Resources resources = application.getResources();
        return new RecipeDuration(
                recipeDurationRepository,
                new TimeProvider(),
                resources.getInteger(R.integer.recipe_max_prep_time_in_minutes),
                resources.getInteger(R.integer.recipe_max_cook_time_in_minutes)
        );
    }

    public RecipeIdentity provideRecipeIdentity() {
        return new RecipeIdentity(
                recipeIdentityRepository,
                new TimeProvider(),
                UseCaseHandler.getInstance(),
                provideTextValidator());
    }

    public <Q extends RecipeRequestAbstract, P extends UseCase.Response> RecipeMacro<Q, P> provideRecipeMacro() {
        return new RecipeMacro<>(
                UseCaseHandler.getInstance(),
                provideRecipeStateCalculator(),
                provideRecipe(),
                provideRecipeIdentity(),
                provideRecipeCourse(),
                provideRecipeDuration(),
                provideRecipePortions());
    }

    private RecipeStateCalculator provideRecipeStateCalculator() {
        return new RecipeStateCalculator();
    }
}
