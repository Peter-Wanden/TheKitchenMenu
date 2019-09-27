package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
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

import static androidx.core.util.Preconditions.checkNotNull;

public class DatabaseInjection {

    public static DataSource<ProductEntity> provideProductsDataSource(
            @NonNull Context context) {
        checkNotNull(context);

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryProduct.getInstance(
                ProductRemoteDataSource.getInstance(),
                ProductLocalDataSource.getInstance(
                        new AppExecutors(), database.productEntityDao()));
    }

    public static DataSourceFavoriteProducts provideFavoritesProductsDataSource(
            @NonNull Context context) {
        checkNotNull(context);

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryFavoriteProduct.getInstance(
                        FavoriteProductsRemoteDataSource.getInstance(),
                        FavoriteProductsLocalDataSource.getInstance(
                                new AppExecutors(),
                                database.favoriteProductEntityDao()));
    }

    public static RepositoryRecipe provideRecipesDataSource(
            @NonNull Context context) {
        checkNotNull(context);

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryRecipe.getInstance(
                RecipeRemoteDataSource.getInstance(),
                RecipeLocalDataSource.getInstance(new AppExecutors(), database.recipeEntityDao()));
    }

    public static RepositoryRecipeCourse provideRecipeCourseDataSource(
            @NonNull Context context) {
        checkNotNull(context);

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryRecipeCourse.getInstance(
                RecipeCourseRemoteDataSource.getInstance(),
                RecipeCourseLocalDataSource.getInstance(
                        new AppExecutors(),
                        database.recipeCourseEntityDao()));
    }

    public static RepositoryRecipeIdentity provideRecipeIdentityDataSource(
            @NonNull Context context) {
        checkNotNull(context);

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryRecipeIdentity.getInstance(
                RecipeIdentityRemoteDataSource.getInstance(),
                RecipeIdentityLocalDataSource.getInstance(
                        new AppExecutors(),
                        database.recipeIdentityEntityDao()));
    }

    public static RepositoryRecipeDuration provideRecipeDurationDataSource(
            @NonNull Context context) {
        checkNotNull(context);

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryRecipeDuration.getInstance(
                RecipeDurationRemoteDataSource.getInstance(),
                RecipeDurationLocalDataSource.getInstance(
                        new AppExecutors(),
                        database.recipeDurationEntityDao()));
    }

    public static RepositoryIngredient provideIngredientDataSource(@NonNull Context context) {
        checkNotNull(context);

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryIngredient.getInstance(
                IngredientRemoteDataSource.getInstance(),
                IngredientLocalDataSource.getInstance(
                        new AppExecutors(),
                        database.ingredientEntityDao()));
    }

    public static RepositoryRecipePortions provideRecipePortionsDataSource(
            @NonNull Context context) {
        checkNotNull(context);

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryRecipePortions.getInstance(
                RecipePortionsRemoteDataSource.getInstance(),
                RecipePortionsLocalDataSource.getInstance(
                        new AppExecutors(),
                        database.recipePortionsEntityDao()));
    }

    public static RepositoryRecipeIngredient provideRecipeIngredientDataSource(
            @NonNull Context context) {
        checkNotNull(context);

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryRecipeIngredient.getInstance(
                RecipeIngredientRemoteDataSource.getInstance(),
                RecipeIngredientLocalDataSource.getInstance(
                        new AppExecutors(),
                        database.recipeIngredientEntityDao()));
    }
}