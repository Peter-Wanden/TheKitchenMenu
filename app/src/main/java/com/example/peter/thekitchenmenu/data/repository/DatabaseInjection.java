package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.DeleteAllAdapter;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.DeleteByDataIdAdapter;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.DeleteByDomainIdAdapter;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.GetAllLatestAdapter;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.GetLatestByDomainIdAdapter;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.SaveAdapter;
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
import com.example.peter.thekitchenmenu.data.repository.dataadapter.GetByDataIdAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.failreason.RecipeFailReasonsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.ingredient.RecipeIngredientLocalDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntityLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.RecipePortionsLocalDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.local.product.datasource.ProductLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.RecipeCourseLocalDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.RecipeDurationLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.RecipeIdentityLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.RecipeMetadataLocalDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.local.TKMDatabase;
import com.example.peter.thekitchenmenu.data.repository.source.local.product.datasource.FavoriteProductsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.IngredientRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.ProductRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipeComponentStateRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipeDurationRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.FavoriteProductsRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipeCourseRemoteDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipeIdentityRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipeIngredientRemoteDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipeMetadataRemoteDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipePortionsRemoteDataAccess;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeMetadata.getInstance;

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

        return getInstance(
                RecipeMetadataRemoteDataAccess.getInstance(),
                RecipeMetadataLocalDataAccess.getInstance(
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

    private static DeleteAllAdapter provideDeleteAllAdapter(@Nonnull Context context) {
        return new DeleteAllAdapter(
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

    private static GetAllLatestAdapter provideGetAllLatestAdapter(
            @Nonnull Context context) {
        return new GetAllLatestAdapter(
                provideRecipeMetaDataParentLocalDataSource(context),
                provideGetLatestByDomainIdAdapter(context)
        );
    }

    private static GetLatestByDomainIdAdapter provideGetLatestByDomainIdAdapter(
            @Nonnull Context context) {
        return new GetLatestByDomainIdAdapter(
                provideRecipeMetaDataParentLocalDataSource(context),
                provideRecipeMetadataGetByDataIdAdapter(context)
        );
    }

    private static GetByDataIdAdapter provideRecipeMetadataGetByDataIdAdapter(
            @Nonnull Context context) {
        return new GetByDataIdAdapter(
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