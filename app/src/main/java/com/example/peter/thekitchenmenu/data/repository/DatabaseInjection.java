package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter.CourseLocalDeleteAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter.CourseLocalGetAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter.CourseLocalSaveAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter.CourseLocalUpdateAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.RepositoryRecipeDurationLocal;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter.DurationLocalDeleteAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter.DurationLocalGetAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter.DurationLocalSaveAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter.RecipeMetadataLocalDeleteAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter.RecipeMetadataLocalGetAllActiveAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter.RecipeMetadataLocalGetActiveByDomainIdAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter.RecipeMetadataLocalSaveAdapter;
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
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.RepositoryRecipeCourseLocal;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter.RecipeMetadataLocalGetByDataIdAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.RecipeIngredientLocalDomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.RecipePortionsLocalDomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.local.product.datasource.ProductLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.RepositoryRecipeMetadataLocal;
import com.example.peter.thekitchenmenu.data.repository.source.local.TKMDatabase;
import com.example.peter.thekitchenmenu.data.repository.source.local.product.datasource.FavoriteProductsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.IngredientRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.ProductRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RepositoryRecipeDurationRemote;
import com.example.peter.thekitchenmenu.data.repository.source.remote.FavoriteProductsRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RepositoryRecipeCourseRemote;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipeIdentityRemoteDomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipeIngredientRemoteDomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipeMetadataRemoteDomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RecipePortionsRemoteDomainDataAccess;
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
            @Nonnull Context c) {
        return RepositoryRecipeMetadata.getInstance(
                RecipeMetadataRemoteDomainDataAccess.getInstance(),
                RepositoryRecipeMetadataLocal.getInstance(
                        provideRecipeMetadataGetByDataIdAdapter(c),
                        provideGetLatestByDomainIdAdapter(c),
                        provideGetAllLatestAdapter(c),
                        provideSaveAdapter(c),
                        provideRecipeMetadataLocalDeleteAdapter(c)
                )
        );
    }

    private static RecipeMetadataLocalDeleteAdapter provideRecipeMetadataLocalDeleteAdapter(
            @Nonnull Context c) {
        return new RecipeMetadataLocalDeleteAdapter(
                provideRecipeMetaDataParentLocalDataSource(c),
                provideRecipeComponentStateLocalDataSource(c),
                provideRecipeFailReasonsLocalDataSource(c)
        );
    }

    private static RecipeMetadataLocalSaveAdapter provideSaveAdapter(Context c) {
        return new RecipeMetadataLocalSaveAdapter(
                provideRecipeMetaDataParentLocalDataSource(c),
                provideRecipeComponentStateLocalDataSource(c),
                provideRecipeFailReasonsLocalDataSource(c),
                new UniqueIdProvider()
        );
    }

    private static RecipeMetadataLocalGetAllActiveAdapter provideGetAllLatestAdapter(
            @Nonnull Context c) {
        return new RecipeMetadataLocalGetAllActiveAdapter(
                provideRecipeMetaDataParentLocalDataSource(c),
                provideGetLatestByDomainIdAdapter(c)
        );
    }

    private static RecipeMetadataLocalGetActiveByDomainIdAdapter provideGetLatestByDomainIdAdapter(
            @Nonnull Context c) {
        return new RecipeMetadataLocalGetActiveByDomainIdAdapter(
                provideRecipeMetaDataParentLocalDataSource(c),
                provideRecipeMetadataGetByDataIdAdapter(c)
        );
    }

    private static RecipeMetadataLocalGetByDataIdAdapter provideRecipeMetadataGetByDataIdAdapter(
            @Nonnull Context c) {
        return new RecipeMetadataLocalGetByDataIdAdapter(
                provideRecipeMetaDataParentLocalDataSource(c),
                provideRecipeComponentStateLocalDataSource(c),
                provideRecipeFailReasonsLocalDataSource(c)
        );
    }

    private static RecipeMetadataParentLocalDataSource provideRecipeMetaDataParentLocalDataSource(
            @Nonnull Context c) {

        TKMDatabase database = TKMDatabase.getInstance(c, new AppExecutors());

        return RecipeMetadataParentLocalDataSource.getInstance(
                new AppExecutors(),
                database.recipeMetadataParentEntityDao()
        );
    }

    private static RecipeComponentStateLocalDataSource provideRecipeComponentStateLocalDataSource(
            @Nonnull Context c) {

        TKMDatabase database = TKMDatabase.getInstance(c, new AppExecutors());

        return RecipeComponentStateLocalDataSource.getInstance(
                new AppExecutors(),
                database.recipeComponentStateEntityDao()
        );
    }

    private static RecipeFailReasonsLocalDataSource provideRecipeFailReasonsLocalDataSource(
            @Nonnull Context c) {

        TKMDatabase database = TKMDatabase.getInstance(c, new AppExecutors());

        return RecipeFailReasonsLocalDataSource.getInstance(
                new AppExecutors(),
                database.recipeFailReasonDao()
        );
    }

    public static RepositoryRecipeCourse provideRecipeCourseDataSource(
            @Nonnull Context c) {

        return RepositoryRecipeCourse.getInstance(
                RepositoryRecipeCourseRemote.getInstance(),
                RepositoryRecipeCourseLocal.getInstance(
                        provideCourseLocalGetAdapter(c),
                        provideCourseLocalUpdateAdapter(c),
                        provideCourseLocalSaveAdapter(c),
                        provideCourseLocalDeleteAdapter(c)
                )
        );
    }

    private static CourseLocalGetAdapter provideCourseLocalGetAdapter(
            @Nonnull Context c) {
        return new CourseLocalGetAdapter(provideRecipeCourseLocalDataSource(c));
    }

    private static CourseLocalUpdateAdapter provideCourseLocalUpdateAdapter(
            @Nonnull Context c) {
        return new CourseLocalUpdateAdapter(provideRecipeCourseLocalDataSource(c));
    }

    private static CourseLocalSaveAdapter provideCourseLocalSaveAdapter(
            @Nonnull Context c) {
        return new CourseLocalSaveAdapter(provideRecipeCourseLocalDataSource(c));
    }

    private static CourseLocalDeleteAdapter provideCourseLocalDeleteAdapter(
            @Nonnull Context c) {
        return new CourseLocalDeleteAdapter(provideRecipeCourseLocalDataSource(c));
    }

    private static RecipeCourseLocalDataSource provideRecipeCourseLocalDataSource(
            @Nonnull Context c) {

        TKMDatabase database = TKMDatabase.getInstance(c, new AppExecutors());

        return RecipeCourseLocalDataSource.getInstance(
                new AppExecutors(),
                database.recipeCourseEntityDao()
        );
    }

    public static RepositoryRecipeIdentity provideRecipeIdentityDataSource(
            @Nonnull Context context) {

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryRecipeIdentity.getInstance(
                RecipeIdentityRemoteDomainDataAccess.getInstance(),
                RecipeIdentityLocalDataSource.getInstance(
                        new AppExecutors(),
                        database.recipeIdentityEntityDao())
        );
    }

    public static RepositoryRecipeDuration provideRecipeDurationDataSource(
            @Nonnull Context c) {

        return RepositoryRecipeDuration.getInstance(
                RepositoryRecipeDurationRemote.getInstance(),
                RepositoryRecipeDurationLocal.getInstance(
                        provideRecipeDurationLocalGetAdapter(c),
                        provideRecipeDurationLocalSaveAdapter(c),
                        provideRecipeDurationLocalDeleteAdapter(c))
        );
    }

    private static DurationLocalGetAdapter provideRecipeDurationLocalGetAdapter(
            @Nonnull Context c) {
        return new DurationLocalGetAdapter(provideRecipeDurationLocalDataSource(c));
    }

    private static DurationLocalSaveAdapter provideRecipeDurationLocalSaveAdapter(
            @Nonnull Context c) {
        return new DurationLocalSaveAdapter(provideRecipeDurationLocalDataSource(c));
    }

    private static DurationLocalDeleteAdapter provideRecipeDurationLocalDeleteAdapter(
            @Nonnull Context c) {
        return new DurationLocalDeleteAdapter(provideRecipeDurationLocalDataSource(c));
    }

    private static RecipeDurationLocalDataSource provideRecipeDurationLocalDataSource(
            @Nonnull Context c) {
        TKMDatabase database = TKMDatabase.getInstance(c, new AppExecutors());

        return RecipeDurationLocalDataSource.getInstance(
                new AppExecutors(),
                database.recipeDurationEntityDao()
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
                RecipePortionsRemoteDomainDataAccess.getInstance(),
                RecipePortionsLocalDomainDataAccess.getInstance(
                        new AppExecutors(),
                        database.recipePortionsEntityDao())
        );
    }

    public static RepositoryRecipeIngredient provideRecipeIngredientDataSource(
            @Nonnull Context context) {

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return RepositoryRecipeIngredient.getInstance(
                RecipeIngredientRemoteDomainDataAccess.getInstance(),
                RecipeIngredientLocalDomainDataAccess.getInstance(
                        new AppExecutors(),
                        database.recipeIngredientEntityDao())
        );
    }
}