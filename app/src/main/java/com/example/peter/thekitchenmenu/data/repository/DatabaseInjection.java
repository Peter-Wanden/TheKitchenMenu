package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.metadata.RecipeMetadataLocalDeleteAllAdapter;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.metadata.DeleteByDataIdAdapter;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.metadata.DeleteByDomainIdAdapter;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.metadata.GetAllActiveAdapter;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.metadata.GetActiveByDomainIdAdapter;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.metadata.SaveAdapter;
import com.example.peter.thekitchenmenu.data.repository.ingredient.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.product.RepositoryFavoriteProduct;
import com.example.peter.thekitchenmenu.data.repository.product.RepositoryProduct;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource.IngredientLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.componentstate.RecipeComponentStateLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.recipe.metadata.RecipeMetadataLocalGetByDataIdAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.failreason.RecipeFailReasonsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.ingredient.RecipeIngredientLocalDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntityLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.RecipePortionsLocalDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.local.product.datasource.ProductLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.RecipeCourseLocalDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.RecipeDurationLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.RecipeIdentityLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.RecipeMetadataLocalDomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.local.TKMDatabase;
import com.example.peter.thekitchenmenu.data.repository.source.local.product.datasource.FavoriteProductsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.IngredientRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.ProductRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipeDurationRemoteDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.remote.FavoriteProductsRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipeCourseRemoteDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipeIdentityRemoteDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipeIngredientRemoteDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipeMetadataRemoteDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipePortionsRemoteDataAccess;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import javax.annotation.Nonnull;

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
        return RepositoryRecipeMetadata.getInstance(
                RecipeMetadataRemoteDataAccess.getInstance(),
                RecipeMetadataLocalDomainDataAccess.getInstance(
                        provideRecipeMetadataGetByDataIdAdapter(context),
                        provideGetLatestByDomainIdAdapter(context),
                        provideGetAllLatestAdapter(context),
                        provideSaveAdapter(context),
                        provideDeleteByDataIdAdapter(context),
                        provideDeleteByDomainIdAdapter(context),
                        provideDeleteAllAdapter(context)
                )
        );
    }

    private static RecipeMetadataLocalDeleteAllAdapter provideDeleteAllAdapter(@Nonnull Context context) {
        return new RecipeMetadataLocalDeleteAllAdapter(
                provideRecipeMetaDataParentLocalDataSource(context),
                provideRecipeComponentStateLocalDataSource(context),
                provideRecipeFailReasonsLocalDataSource(context)
        );
    }

    private static DeleteByDomainIdAdapter provideDeleteByDomainIdAdapter(
            @Nonnull Context context) {
        return new DeleteByDomainIdAdapter(
                provideRecipeMetaDataParentLocalDataSource(context),
                provideDeleteByDataIdAdapter(context)
        );
    }

    private static DeleteByDataIdAdapter provideDeleteByDataIdAdapter(
            @Nonnull Context context) {
        return new DeleteByDataIdAdapter(
                provideRecipeMetaDataParentLocalDataSource(context),
                provideRecipeComponentStateLocalDataSource(context),
                provideRecipeFailReasonsLocalDataSource(context)
        );
    }

    private static SaveAdapter provideSaveAdapter(Context context) {
        return new SaveAdapter(
                provideRecipeMetaDataParentLocalDataSource(context),
                provideRecipeComponentStateLocalDataSource(context),
                provideRecipeFailReasonsLocalDataSource(context),
                new UniqueIdProvider()
        );
    }

    private static GetAllActiveAdapter provideGetAllLatestAdapter(
            @Nonnull Context context) {
        return new GetAllActiveAdapter(
                provideRecipeMetaDataParentLocalDataSource(context),
                provideGetLatestByDomainIdAdapter(context)
        );
    }

    private static GetActiveByDomainIdAdapter provideGetLatestByDomainIdAdapter(
            @Nonnull Context context) {
        return new GetActiveByDomainIdAdapter(
                provideRecipeMetaDataParentLocalDataSource(context),
                provideRecipeMetadataGetByDataIdAdapter(context)
        );
    }

    private static RecipeMetadataLocalGetByDataIdAdapter provideRecipeMetadataGetByDataIdAdapter(
            @Nonnull Context context) {
        return new RecipeMetadataLocalGetByDataIdAdapter(
                provideRecipeMetaDataParentLocalDataSource(context),
                provideRecipeComponentStateLocalDataSource(context),
                provideRecipeFailReasonsLocalDataSource(context)
        );
    }

    private static RecipeMetadataParentEntityLocalDataSource provideRecipeMetaDataParentLocalDataSource(
            @Nonnull Context context) {

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RecipeMetadataParentEntityLocalDataSource.getInstance(
                new AppExecutors(),
                database.recipeMetadataParentEntityDao()
        );
    }

    private static RecipeComponentStateLocalDataSource provideRecipeComponentStateLocalDataSource(
            @Nonnull Context context) {

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RecipeComponentStateLocalDataSource.getInstance(
                new AppExecutors(),
                database.recipeComponentStateEntityDao()
        );
    }

    private static RecipeFailReasonsLocalDataSource provideRecipeFailReasonsLocalDataSource(
            @Nonnull Context context) {

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RecipeFailReasonsLocalDataSource.getInstance(
                new AppExecutors(),
                database.recipeFailReasonDao()
        );
    }

    public static RepositoryRecipeCourse provideRecipeCourseDataSource(
            @Nonnull Context context) {

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryRecipeCourse.getInstance(
                RecipeCourseRemoteDataAccess.getInstance(),
                RecipeCourseLocalDataAccess.getInstance(
                        new AppExecutors(),
                        database.recipeCourseEntityDao())
        );
    }

    public static RepositoryRecipeIdentity provideRecipeIdentityDataSource(
            @Nonnull Context context) {

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryRecipeIdentity.getInstance(
                RecipeIdentityRemoteDataAccess.getInstance(),
                RecipeIdentityLocalDataSource.getInstance(
                        new AppExecutors(),
                        database.recipeIdentityEntityDao())
        );
    }

    public static RepositoryRecipeDuration provideRecipeDurationDataSource(
            @Nonnull Context context) {

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryRecipeDuration.getInstance(
                RecipeDurationRemoteDataAccess.getInstance(),
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
                RecipePortionsRemoteDataAccess.getInstance(),
                RecipePortionsLocalDataAccess.getInstance(
                        new AppExecutors(),
                        database.recipePortionsEntityDao())
        );
    }

    public static RepositoryRecipeIngredient provideRecipeIngredientDataSource(
            @Nonnull Context context) {

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryRecipeIngredient.getInstance(
                RecipeIngredientRemoteDataAccess.getInstance(),
                RecipeIngredientLocalDataAccess.getInstance(
                        new AppExecutors(),
                        database.recipeIngredientEntityDao())
        );
    }
}