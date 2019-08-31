package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.entity.ProductEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.ProductLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.RecipeCourseLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.RecipeLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.TKMDatabase;
import com.example.peter.thekitchenmenu.data.repository.source.local.FavoriteProductsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.ProductRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.FavoriteProductsRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.RecipeCourseRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.RecipeRemoteDataSource;

import static androidx.core.util.Preconditions.checkNotNull;

public class DatabaseInjection {

    public static DataSource<ProductEntity> provideProductsDataSource(@NonNull Context context) {
        checkNotNull(context);

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryProduct.getInstance(
                ProductRemoteDataSource.getInstance(),
                ProductLocalDataSource.getInstance(
                        new AppExecutors(), database.productEntityDao()));
    }

    public static FavoriteProductsDataSource provideFavoritesProductsDataSource(
            @NonNull Context context) {
        checkNotNull(context);

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryFavoriteProduct.getInstance(
                        FavoriteProductsRemoteDataSource.getInstance(),
                        FavoriteProductsLocalDataSource.getInstance(
                                new AppExecutors(),
                                database.favoriteProductEntityDao()));
    }

    public static RepositoryRecipe provideRecipesDataSource(@NonNull Context context) {
        checkNotNull(context);

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryRecipe.getInstance(
                RecipeRemoteDataSource.getInstance(),
                RecipeLocalDataSource.getInstance(new AppExecutors(), database.recipeEntityDao()));
    }

    public static RepositoryRecipeCourse provideRecipeCourseDataSource(@NonNull Context context) {
        checkNotNull(context);

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryRecipeCourse.getInstance(
                RecipeCourseRemoteDataSource.getInstance(),
                RecipeCourseLocalDataSource.getInstance(
                        new AppExecutors(),
                        database.recipeCourseEntityDao()));
    }
}