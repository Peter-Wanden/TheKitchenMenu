package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.source.local.ProductLocalDataSource;
import com.example.peter.thekitchenmenu.data.source.local.RecipeLocalDataSource;
import com.example.peter.thekitchenmenu.data.source.local.TKMDatabase;
import com.example.peter.thekitchenmenu.data.source.local.FavoriteProductsLocalDataSource;
import com.example.peter.thekitchenmenu.data.source.remote.ProductRemoteDataSource;
import com.example.peter.thekitchenmenu.data.source.remote.FavoriteProductsRemoteDataSource;
import com.example.peter.thekitchenmenu.data.source.remote.RecipeRemoteDataSource;

import static androidx.core.util.Preconditions.checkNotNull;

public class DatabaseInjection {

    public static ProductRepository provideProductsRepository(@NonNull Context context) {
        checkNotNull(context);

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return ProductRepository.getInstance(
                ProductRemoteDataSource.getInstance(),
                ProductLocalDataSource.getInstance(
                        new AppExecutors(), database.productEntityDao()));
    }

    public static FavoriteProductsRepository provideFavoritesProductsRepository(
            @NonNull Context context) {
        checkNotNull(context);

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return FavoriteProductsRepository.getInstance(
                FavoriteProductsRemoteDataSource.getInstance(),
                FavoriteProductsLocalDataSource.getInstance(
                        new AppExecutors(), database.favoriteProductEntityDao()));
    }

    public static RecipeRepository provideRecipesRepository(@NonNull Context context) {
        checkNotNull(context);

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RecipeRepository.getInstance(
                RecipeRemoteDataSource.getInstance(),
                RecipeLocalDataSource.getInstance(new AppExecutors(), database.recipeEntityDao()));
    }
}