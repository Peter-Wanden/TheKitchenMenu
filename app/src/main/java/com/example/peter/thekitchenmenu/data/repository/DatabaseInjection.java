package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.repository.ingredient.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.product.RepositoryFavoriteProduct;
import com.example.peter.thekitchenmenu.data.repository.product.RepositoryProduct;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeFailReasons;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeComponentState;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource.IngredientLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.datasource.RecipeComponentStateLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.datasource.RecipeIngredientLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.datasource.RecipePortionsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.product.datasource.ProductLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.datasource.RecipeCourseLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.datasource.RecipeDurationLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.datasource.RecipeIdentityLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.datasource.RecipeMetadataLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.TKMDatabase;
import com.example.peter.thekitchenmenu.data.repository.source.local.product.datasource.FavoriteProductsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.IngredientRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.ProductRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipeComponentStateRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipeDurationRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.FavoriteProductsRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipeCourseRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipeIdentityRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipeIngredientRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipeMetadataRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipePortionsRemoteDataSource;

import javax.annotation.Nonnull;

import static androidx.core.util.Preconditions.checkNotNull;

public class DatabaseInjection {

    public static RepositoryProduct provideProductDataSource(
            @Nonnull Context context) {

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryProduct.getInstance(
                ProductRemoteDataSource.getInstance(),
                ProductLocalDataSource.getInstance(
                        new AppExecutors(), database.productEntityDao())
        );
    }

    public static RepositoryFavoriteProduct provideFavoriteProductsDataSource(
            @Nonnull Context context) {

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryFavoriteProduct.getInstance(
                FavoriteProductsRemoteDataSource.getInstance(),
                FavoriteProductsLocalDataSource.getInstance(
                        new AppExecutors(),
                        database.favoriteProductEntityDao())
        );
    }

    public static RepositoryRecipeMetadata provideRecipeMetadataDataSource(
            @Nonnull Context context) {

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryRecipeMetadata.getInstance(
                RecipeMetadataRemoteDataSource.getInstance(),
                RecipeMetadataLocalDataSource.getInstance(
                        new AppExecutors(),
                        database.recipeEntityDao())
        );
    }

    public static RepositoryRecipeFailReasons provideRecipeFailReasonsDataSource(
            Context context) {

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryRecipeFailReasons(
                RecipeFailReasonsRemoteDataSource.getInstance(),
                RecipeFailReasonsLocalDataSource.getInstance(
                        new AppExecutors(),
                        database.recipeFailReasonDao())
        );
    }

    public static RepositoryRecipeComponentState provideRecipeComponentStateDataSource(
            @Nonnull Context context) {

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryRecipeComponentState.getInstance(
                RecipeComponentStateRemoteDataSource.getInstance(),
                RecipeComponentStateLocalDataSource.getInstance(
                        new AppExecutors(),
                        database.recipeComponentStateEntityDao()
                )
        );
    }

    public static RepositoryRecipeCourse provideRecipeCourseDataSource(
            @Nonnull Context context) {

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryRecipeCourse.getInstance(
                RecipeCourseRemoteDataSource.getInstance(),
                RecipeCourseLocalDataSource.getInstance(
                        new AppExecutors(),
                        database.recipeCourseEntityDao())
        );
    }

    public static RepositoryRecipeIdentity provideRecipeIdentityDataSource(
            @Nonnull Context context) {

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryRecipeIdentity.getInstance(
                RecipeIdentityRemoteDataSource.getInstance(),
                RecipeIdentityLocalDataSource.getInstance(
                        new AppExecutors(),
                        database.recipeIdentityEntityDao())
        );
    }

    public static RepositoryRecipeDuration provideRecipeDurationDataSource(
            @Nonnull Context context) {

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryRecipeDuration.getInstance(
                RecipeDurationRemoteDataSource.getInstance(),
                RecipeDurationLocalDataSource.getInstance(
                        new AppExecutors(),
                        database.recipeDurationEntityDao())
        );
    }

    public static RepositoryIngredient provideIngredientDataSource(@Nonnull Context context) {

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryIngredient.getInstance(
                IngredientRemoteDataSource.getInstance(),
                IngredientLocalDataSource.getInstance(
                        new AppExecutors(),
                        database.ingredientEntityDao())
        );
    }

    public static RepositoryRecipePortions provideRecipePortionsDataSource(
            @Nonnull Context context) {

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryRecipePortions.getInstance(
                RecipePortionsRemoteDataSource.getInstance(),
                RecipePortionsLocalDataSource.getInstance(
                        new AppExecutors(),
                        database.recipePortionsEntityDao())
        );
    }

    public static RepositoryRecipeIngredient provideRecipeIngredientDataSource(
            @Nonnull Context context) {

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryRecipeIngredient.getInstance(
                RecipeIngredientRemoteDataSource.getInstance(),
                RecipeIngredientLocalDataSource.getInstance(
                        new AppExecutors(),
                        database.recipeIngredientEntityDao())
        );
    }
}