package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.repository.source.local.IngredientLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.RecipeIngredientLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.RecipePortionsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.ProductLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.RecipeCourseLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.RecipeDurationLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.RecipeIdentityLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.RecipeLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.TKMDatabase;
import com.example.peter.thekitchenmenu.data.repository.source.local.FavoriteProductsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.IngredientRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.ProductRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.RecipeDurationRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.FavoriteProductsRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.RecipeCourseRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.RecipeIdentityRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.RecipeIngredientRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.RecipeRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.RecipePortionsRemoteDataSource;

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

    public static RepositoryRecipeMetaData provideRecipeDataSource(@Nonnull Context context) {

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryRecipeMetaData.getInstance(
                RecipeRemoteDataSource.getInstance(),
                RecipeLocalDataSource.getInstance(
                        new AppExecutors(),
                        database.recipeEntityDao())
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