package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.ProductLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.LocalDataSourceRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.source.local.RecipeDurationLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.RecipeIdentityLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.RecipeLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.TKMDatabase;
import com.example.peter.thekitchenmenu.data.repository.source.local.LocalDataSourceFavoriteProducts;
import com.example.peter.thekitchenmenu.data.repository.source.remote.ProductRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.RecipeDurationRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.RemoteDataSourceFavoriteProducts;
import com.example.peter.thekitchenmenu.data.repository.source.remote.RemoteDataSourceRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.source.remote.RecipeIdentityRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.RecipeRemoteDataSource;

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
                        RemoteDataSourceFavoriteProducts.getInstance(),
                        LocalDataSourceFavoriteProducts.getInstance(
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
                RemoteDataSourceRecipeCourse.getInstance(),
                LocalDataSourceRecipeCourse.getInstance(
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
}