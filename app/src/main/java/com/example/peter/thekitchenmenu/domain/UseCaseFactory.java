package com.example.peter.thekitchenmenu.domain;

import android.app.Application;
import android.content.res.Resources;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.repository.DatabaseInjection;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus.ConversionFactorStatus;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.Ingredient;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentityandduration.UseCaseRecipeIdentityAndDurationList;
import com.example.peter.thekitchenmenu.domain.usecase.recipecourse.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipeingredientlist.UseCaseRecipeIngredientListItems;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportions.UseCaseRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.UseCaseTextValidator;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientDuplicateChecker;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.domain.usecase.recipeingredientcalculator.UseCaseIngredientCalculator;

public class UseCaseFactory {

    private static volatile UseCaseFactory INSTANCE;
    private final Application application;
    private final RepositoryRecipePortions recipePortionsRepository;
    private final RepositoryRecipeIngredient recipeIngredientRepository;
    private final RepositoryIngredient ingredientRepository;
    private final RepositoryRecipeIdentity recipeIdentityRepository;
    private final RepositoryRecipeDuration recipeDurationRepository;
    private final RepositoryRecipeCourse recipeCourseRepository;

    private UseCaseFactory(Application application,
                           RepositoryRecipePortions recipePortionsRepository,
                           RepositoryRecipeIngredient recipeIngredientRepository,
                           RepositoryIngredient ingredientRepository,
                           RepositoryRecipeIdentity recipeIdentityRepository,
                           RepositoryRecipeDuration recipeDurationRepository,
                           RepositoryRecipeCourse recipeCourseRepository) {
        this.application = application;
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

    public UseCaseIngredientCalculator providePortionsUseCase() {
        return new UseCaseIngredientCalculator(
                recipePortionsRepository,
                recipeIngredientRepository,
                ingredientRepository,
                new UniqueIdProvider(),
                new TimeProvider()
        );
    }

    public ConversionFactorStatus provideConversionFactorUseCase() {
        return new ConversionFactorStatus(
                ingredientRepository
        );
    }

    public UseCaseRecipeIngredientListItems provideRecipeIngredientListUseCase() {
        return new UseCaseRecipeIngredientListItems(
                recipeIngredientRepository,
                ingredientRepository,
                recipePortionsRepository
        );
    }

    public UseCaseRecipeIdentityAndDurationList provideRecipeIdentityAndDurationListUseCase() {
        return new UseCaseRecipeIdentityAndDurationList(
                recipeIdentityRepository,
                recipeDurationRepository
        );
    }

    public RecipeIdentity provideRecipeIdentityUseCase() {
        return new RecipeIdentity(
                recipeIdentityRepository,
                new TimeProvider());
    }

    public RecipeCourse provideRecipeCourseUseCase() {
        return new RecipeCourse(
                recipeCourseRepository,
                new UniqueIdProvider(),
                new TimeProvider());
    }

    public UseCaseTextValidator provideTextValidatorUseCase() {
        Resources resources = application.getResources();

        return new UseCaseTextValidator.Builder().
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

    public Ingredient provideIngredientEditorUseCase() {
        return new Ingredient(
                ingredientRepository,
                new UniqueIdProvider(),
                new TimeProvider(),
                new IngredientDuplicateChecker(ingredientRepository)
                );
    }

    public UseCaseRecipePortions provideRecipePortionsUseCase() {
        Resources resources = application.getResources();
        return new UseCaseRecipePortions(
                new TimeProvider(),
                new UniqueIdProvider(),
                recipePortionsRepository,
                resources.getInteger(R.integer.recipe_max_servings),
                resources.getInteger(R.integer.recipe_max_sittings)
        );
    }

    public RecipeDuration provideRecipeDurationUseCase() {
        Resources resources = application.getResources();
        return new RecipeDuration(
                recipeDurationRepository,
                new TimeProvider(),
                resources.getInteger(R.integer.recipe_max_prep_time_in_minutes),
                resources.getInteger(R.integer.recipe_max_cook_time_in_minutes)
        );
    }
}
