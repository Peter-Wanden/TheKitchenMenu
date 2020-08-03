package com.example.peter.thekitchenmenu.domain.usecase.factory;

import android.app.Application;
import android.content.res.Resources;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.repository.DatabaseInjection;
import com.example.peter.thekitchenmenu.data.repository.ingredient.DataAccessIngredient;
import com.example.peter.thekitchenmenu.data.repository.recipe.RecipeCourseUseCaseDataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.RecipeDurationUseCaseDataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.RecipeIdentityUseCaseDataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataAccessRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataAccessRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataAccessRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus.ConversionFactorStatus;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.Ingredient;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientDuplicateChecker;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentName;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipecopy.RecipeCopy;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredient;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredientlist.RecipeIngredientList;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist.RecipeList;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.HashSet;
import java.util.Set;

public class UseCaseFactory {

    private static final String TAG = "tkm-" + UseCaseFactory.class.getSimpleName() + ": ";

    private static volatile UseCaseFactory INSTANCE;

    private final Application application;
    private final DataAccessRecipeMetadata recipeMetadataRepository;
    private final DataAccessRecipePortions recipePortionsRepository;
    private final DataAccessRecipeIngredient recipeIngredientRepository;
    private final DataAccessIngredient ingredientRepository;
    private final RecipeIdentityUseCaseDataAccess recipeIdentityUseCaseDataAccess;
    private final RecipeDurationUseCaseDataAccess recipeDurationRepository;
    private final RecipeCourseUseCaseDataAccess recipeCourseRepository;

    private UseCaseFactory(Application application,
                           DataAccessRecipeMetadata recipeMetadataRepository,
                           DataAccessRecipePortions recipePortionsRepository,
                           DataAccessRecipeIngredient recipeIngredientRepository,
                           DataAccessIngredient ingredientRepository,
                           RecipeIdentityUseCaseDataAccess recipeIdentityUseCaseDataAccess,
                           RecipeDurationUseCaseDataAccess recipeDurationRepository,
                           RecipeCourseUseCaseDataAccess recipeCourseRepository) {

        this.application = application;
        this.recipeMetadataRepository = recipeMetadataRepository;
        this.recipePortionsRepository = recipePortionsRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.ingredientRepository = ingredientRepository;
        this.recipeIdentityUseCaseDataAccess = recipeIdentityUseCaseDataAccess;
        this.recipeDurationRepository = recipeDurationRepository;
        this.recipeCourseRepository = recipeCourseRepository;
    }

    public static UseCaseFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (UseCaseFactory.class) {
                if (INSTANCE == null)
                    INSTANCE = new UseCaseFactory(
                            application,
                            DatabaseInjection.provideRecipeMetadataDataSource(
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

    public RecipeIngredient getIngredientCalculatorUseCase() {
        return new RecipeIngredient(
                recipePortionsRepository,
                recipeIngredientRepository,
                ingredientRepository,
                new UniqueIdProvider(),
                new TimeProvider()
        );
    }

    public ConversionFactorStatus getConversionFactorStatusUseCase() {
        return new ConversionFactorStatus(
                ingredientRepository
        );
    }

    public RecipeIngredientList getRecipeIngredientListUseCase() {
        return new RecipeIngredientList(
                recipeIngredientRepository,
                ingredientRepository,
                recipePortionsRepository
        );
    }

    public RecipeList getRecipeListUseCase() {
        return new RecipeList(
                getUseCaseHandler(),
                this,
                recipeMetadataRepository
        );
    }

    public RecipeCourse getRecipeCourseUseCase() {
        return new RecipeCourse(
                recipeCourseRepository,
                new UniqueIdProvider(),
                new TimeProvider()
        );
    }

    public TextValidator getTextValidatorUseCase() {
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

    public Ingredient getIngredientUseCase() {
        return new Ingredient(
                ingredientRepository,
                new UniqueIdProvider(),
                new TimeProvider(),
                new IngredientDuplicateChecker(ingredientRepository),
                getTextValidatorUseCase()
        );
    }

    private RecipeMetadata getRecipeMetadataUseCase() {
        final Set<RecipeMetadata.ComponentName> requiredComponents = new HashSet<>();
        requiredComponents.add(ComponentName.IDENTITY);
        requiredComponents.add(ComponentName.COURSE);
        requiredComponents.add(ComponentName.PORTIONS);

        final Set<RecipeMetadata.ComponentName> additionalComponents = new HashSet<>();
        additionalComponents.add(ComponentName.DURATION);

        return new RecipeMetadata(
                recipeMetadataRepository,
                new UniqueIdProvider(),
                new TimeProvider(),
                requiredComponents,
                additionalComponents
        );
    }

    public RecipePortions getRecipePortionsUseCase() {
        Resources resources = application.getResources();
        return new RecipePortions(
                recipePortionsRepository,
                new UniqueIdProvider(),
                new TimeProvider(),
                resources.getInteger(R.integer.recipe_max_servings),
                resources.getInteger(R.integer.recipe_max_sittings)
        );
    }

    private RecipeDuration getRecipeDurationUseCase() {
        Resources resources = application.getResources();
        return new RecipeDuration(
                recipeDurationRepository,
                new UniqueIdProvider(),
                new TimeProvider(),
                resources.getInteger(R.integer.recipe_max_prep_time_in_minutes),
                resources.getInteger(R.integer.recipe_max_cook_time_in_minutes)
        );
    }

    public RecipeIdentity getRecipeIdentityUseCase() {
        return new RecipeIdentity(
                recipeIdentityUseCaseDataAccess,
                new UniqueIdProvider(),
                new TimeProvider(),
                getTextValidatorUseCase());
    }

    public Recipe getRecipeUseCase() {
        return new Recipe(
                getUseCaseHandler(),
                getRecipeMetadataUseCase(),
                getRecipeIdentityUseCase(),
                getRecipeCourseUseCase(),
                getRecipeDurationUseCase(),
                getRecipePortionsUseCase()
        );
    }

    public RecipeCopy getRecipeMacroCloneUseCase() {
        return new RecipeCopy(
                getUseCaseHandler(),
                new UniqueIdProvider(),
                getRecipeUseCase(),
                getRecipeUseCase()
        );
    }

    public UseCaseHandler getUseCaseHandler() {
        return UseCaseHandler.getInstance();
    }
}
