package com.example.peter.thekitchenmenu.domain.usecase.common.failreasons;

import com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus.ConversionFactorStatus;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.Ingredient;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipecopy.RecipeCopy;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeingredient.RecipeIngredient;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;

/**
 * All components that report metadata and that have fail reason states that are not covered in the
 * {@link CommonFailReason} enum, have their own FailReason enum that extends this base class.
 *
 * To enable 'flattening' to a primitive for persistent frameworks that require it, each FailReason
 * enum values are assigned the following integer number range.
 *
 * FailReasons 1000 (this)
 * {@link CommonFailReason} 50-99
 * {@link ConversionFactorStatus.FailReason} 100-149
 * {@link Ingredient.FailReason} 150-199
 * {@link RecipeCourse} 200-249 RESERVED (currently only uses CommonFailReasons)
 * {@link RecipeDuration.FailReason} 250-299
 * {@link RecipeIdentity.FailReason} 300-349
 * {@link RecipePortions.FailReason} 350-399
 * {@link RecipeMetadata.FailReason} 400-449
 * {@link RecipeIngredient.FailReason} 450-499
 * {@link TextValidator.FailReason} 500-549
 *
 * {@link RecipeCopy.FailReason} 600-649
 */

public interface FailReasons {

    int getId();
}
