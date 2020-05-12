package com.example.peter.thekitchenmenu.domain.usecase;

import android.app.Application;
import android.content.res.Resources;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.repository.DatabaseInjection;
import com.example.peter.thekitchenmenu.data.repository.ingredient.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus.ConversionFactorStatus;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.Ingredient;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientDuplicateChecker;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata;
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
    private final RepositoryRecipeMetadata recipeMetadataRepository;
    private final RepositoryRecipePortions recipePortionsRepository;
    private final RepositoryRecipeIngredient recipeIngredientRepository;
    private final RepositoryIngredient ingredientRepository;
    private final RepositoryRecipeIdentity recipeIdentityRepository;
    private final RepositoryRecipeDuration recipeDurationRepository;
    private final RepositoryRecipeCourse recipeCourseRepository;

    private UseCaseFactory(Application application,
                           RepositoryRecipeMetadata recipeMetadataRepository,
                           RepositoryRecipePortions recipePortionsRepository,
                           RepositoryRecipeIngredient recipeIngredientRepository,
                           RepositoryIngredient ingredientRepository,
                           RepositoryRecipeIdentity recipeIdentityRepository,
                           RepositoryRecipeDuration recipeDurationRepository,
                           RepositoryRecipeCourse recipeCourseRepository) {
        this.application = application;
        this.recipeMetadataRepository = recipeMetadataRepository;
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

    public RecipeIngredient provideIngredientCalculator() {
        return new RecipeIngredient(
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
                UseCaseHandler.getInstance()
        );
    }

    public RecipeCourse provideRecipeCourse() {
        return new RecipeCourse(
                recipeCourseRepository,
                new UniqueIdProvider(),
                new TimeProvider()
        );
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
                new IngredientDuplicateChecker(ingredientRepository),
                UseCaseHandler.getInstance(),
                provideTextValidator()
        );
    }

    private RecipeMetadata provideRecipeMetadata() {
        final Set<RecipeMetadata.ComponentName> requiredComponents = new HashSet<>();
        requiredComponents.add(RecipeMetadata.ComponentName.IDENTITY);
        requiredComponents.add(RecipeMetadata.ComponentName.COURSE);
        requiredComponents.add(RecipeMetadata.ComponentName.DURATION);
        requiredComponents.add(RecipeMetadata.ComponentName.PORTIONS);

        return new RecipeMetadata(
                recipeMetadataRepository,
                new UniqueIdProvider(),
                new TimeProvider(),
                requiredComponents
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
                new UniqueIdProvider(),
                resources.getInteger(R.integer.recipe_max_prep_time_in_minutes),
                resources.getInteger(R.integer.recipe_max_cook_time_in_minutes)
        );
    }

    public RecipeIdentity provideRecipeIdentity() {
        return new RecipeIdentity(
                recipeIdentityRepository,
                new UniqueIdProvider(),
                new TimeProvider(),
                UseCaseHandler.getInstance(),
                provideTextValidator());
    }

    public Recipe provideRecipeMacro() {
        return new Recipe(
                UseCaseHandler.getInstance(),
                provideRecipeMetadata(),
                provideRecipeIdentity(),
                provideRecipeCourse(),
                provideRecipeDuration(),
                provideRecipePortions()
        );
    }

    public RecipeCopy provideRecipeMacroClone() {
        return new RecipeCopy(
                UseCaseHandler.getInstance(),
                new UniqueIdProvider(),
                provideRecipeMacro(),
                provideRecipeMacro()
        );
    }
}
