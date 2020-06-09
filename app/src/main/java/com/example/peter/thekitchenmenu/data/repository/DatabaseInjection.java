package com.example.peter.thekitchenmenu.data.repository;

import android.content.Context;

import com.example.peter.thekitchenmenu.app.AppExecutors;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.RepositoryIngredientLocal;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.dataadapter.IngredientLocalDeleteAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.dataadapter.IngredientLocalGetAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.dataadapter.IngredientLocalSaveAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter.CourseLocalDeleteAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter.CourseLocalGetAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter.CourseLocalSaveAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.dataadapter.CourseLocalUpdateAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.RepositoryRecipeDurationLocal;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter.DurationLocalDeleteAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter.DurationLocalGetAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.dataadapter.DurationLocalSaveAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.RepositoryRecipeIdentityLocal;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.dataadapter.IdentityLocalDeleteAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.dataadapter.IdentityLocalGetAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.dataadapter.IdentityLocalSaveAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter.RecipeMetadataLocalDeleteAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter.RecipeMetadataLocalGetAdapter;
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
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.RepositoryRecipePortionsLocal;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.dataadapter.PortionsLocalDeleteAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.dataadapter.PortionsLocalGetAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.dataadapter.PortionsLocalSaveAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.RepositoryRecipeIngredientLocal;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.dataadapter.RecipeIngredientLocalDeleteAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.dataadapter.RecipeIngredientLocalGetAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.dataadapter.RecipeIngredientLocalSaveAdapter;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.datasource.RecipeIngredientLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.RecipePortionsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.product.datasource.ProductLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.RepositoryRecipeMetadataLocal;
import com.example.peter.thekitchenmenu.data.repository.source.local.TKMDatabase;
import com.example.peter.thekitchenmenu.data.repository.source.local.product.datasource.FavoriteProductsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.RepositoryIngredientRemote;
import com.example.peter.thekitchenmenu.data.repository.source.remote.ProductRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RepositoryRecipeDurationRemote;
import com.example.peter.thekitchenmenu.data.repository.source.remote.FavoriteProductsRemoteDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RepositoryRecipeCourseRemote;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RepositoryRecipeIdentityRemote;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RepositoryRecipeIngredientRemote;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RepositoryRecipeMetadataRemote;
import com.example.peter.thekitchenmenu.data.repository.source.remote.recipe.RepositoryRecipePortionsRemote;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import javax.annotation.Nonnull;

public class DatabaseInjection {

    public static RepositoryProduct provideProductDataSource(@Nonnull Context context) {

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

    public static RepositoryRecipeMetadata provideRecipeMetadataDataSource(@Nonnull Context c) {
        return RepositoryRecipeMetadata.getInstance(
                RepositoryRecipeMetadataRemote.getInstance(),
                RepositoryRecipeMetadataLocal.getInstance(
                        provideRecipeMetadataLocalGetAdapter(c),
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

    private static RecipeMetadataLocalSaveAdapter provideSaveAdapter(@Nonnull Context c) {
        return new RecipeMetadataLocalSaveAdapter(
                provideRecipeMetaDataParentLocalDataSource(c),
                provideRecipeComponentStateLocalDataSource(c),
                provideRecipeFailReasonsLocalDataSource(c),
                new UniqueIdProvider()
        );
    }

    private static RecipeMetadataLocalGetAdapter provideRecipeMetadataLocalGetAdapter(
            @Nonnull Context c) {
        return new RecipeMetadataLocalGetAdapter(
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

    private static CourseLocalGetAdapter provideCourseLocalGetAdapter(@Nonnull Context c) {
        return new CourseLocalGetAdapter(provideRecipeCourseLocalDataSource(c));
    }

    private static CourseLocalUpdateAdapter provideCourseLocalUpdateAdapter(@Nonnull Context c) {
        return new CourseLocalUpdateAdapter(provideRecipeCourseLocalDataSource(c));
    }

    private static CourseLocalSaveAdapter provideCourseLocalSaveAdapter(@Nonnull Context c) {
        return new CourseLocalSaveAdapter(provideRecipeCourseLocalDataSource(c));
    }

    private static CourseLocalDeleteAdapter provideCourseLocalDeleteAdapter(@Nonnull Context c) {
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
            @Nonnull Context c) {

        return RepositoryRecipeIdentity.getInstance(
                RepositoryRecipeIdentityRemote.getInstance(),
                RepositoryRecipeIdentityLocal.getInstance(
                        provideRecipeIdentityLocalGetAdapter(c),
                        provideRecipeIdentitySaveAdapter(c),
                        provideRecipeIdentityDeleteAdapter(c))
        );
    }

    private static IdentityLocalGetAdapter provideRecipeIdentityLocalGetAdapter(
            @Nonnull Context c) {
        return new IdentityLocalGetAdapter(provideRecipeIdentityLocalDataSource(c));
    }

    private static IdentityLocalSaveAdapter provideRecipeIdentitySaveAdapter(
            @Nonnull Context c) {
        return new IdentityLocalSaveAdapter(provideRecipeIdentityLocalDataSource(c));
    }

    private static IdentityLocalDeleteAdapter provideRecipeIdentityDeleteAdapter(
            @Nonnull Context c) {
        return new IdentityLocalDeleteAdapter(provideRecipeIdentityLocalDataSource(c));
    }

    private static RecipeIdentityLocalDataSource provideRecipeIdentityLocalDataSource(
            @Nonnull Context c) {

        TKMDatabase database = TKMDatabase.getInstance(c, new AppExecutors());

        return RecipeIdentityLocalDataSource.getInstance(new AppExecutors(),
                database.recipeIdentityEntityDao()
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

    public static RepositoryRecipePortions provideRecipePortionsDataSource(
            @Nonnull Context c) {

        return RepositoryRecipePortions.getInstance(
                RepositoryRecipePortionsRemote.getInstance(),
                RepositoryRecipePortionsLocal.getInstance(
                        providePortionsLocalGetAdapter(c),
                        providePortionsLocalSaveAdapter(c),
                        providePortionsLocalDeleteAdapter(c)
                )
        );
    }

    private static PortionsLocalGetAdapter providePortionsLocalGetAdapter(
            @Nonnull Context c) {
        return new PortionsLocalGetAdapter(provideRecipePortionsLocalDataSource(c));
    }

    private static PortionsLocalSaveAdapter providePortionsLocalSaveAdapter(
            @Nonnull Context c) {
        return new PortionsLocalSaveAdapter(provideRecipePortionsLocalDataSource(c));
    }

    private static PortionsLocalDeleteAdapter providePortionsLocalDeleteAdapter(
            @Nonnull Context c) {
        return new PortionsLocalDeleteAdapter(provideRecipePortionsLocalDataSource(c));
    }

    private static RecipePortionsLocalDataSource provideRecipePortionsLocalDataSource(
            @Nonnull Context c) {
        TKMDatabase database = TKMDatabase.getInstance(c, new AppExecutors());
        return RecipePortionsLocalDataSource.getInstance(
                new AppExecutors(),
                database.recipePortionsEntityDao()
        );
    }

    public static RepositoryIngredient provideIngredientDataSource(
            @Nonnull Context c) {
        return RepositoryIngredient.getInstance(
                RepositoryIngredientRemote.getInstance(),
                RepositoryIngredientLocal.getInstance(
                        provideIngredientLocalGetAdapter(c),
                        provideIngredientLocalSaveAdapter(c),
                        provideIngredientLocalDeleteAdapter(c))
        );
    }

    private static IngredientLocalGetAdapter provideIngredientLocalGetAdapter(
            @Nonnull Context c) {
        return new IngredientLocalGetAdapter(provideIngredientLocalDataSource(c));
    }

    private static IngredientLocalSaveAdapter provideIngredientLocalSaveAdapter(
            @Nonnull Context c) {
        return new IngredientLocalSaveAdapter(provideIngredientLocalDataSource(c));
    }

    private static IngredientLocalDeleteAdapter provideIngredientLocalDeleteAdapter(
            @Nonnull Context c) {
        return new IngredientLocalDeleteAdapter(provideIngredientLocalDataSource(c));
    }

    private static IngredientLocalDataSource provideIngredientLocalDataSource(
            @Nonnull Context c) {

        TKMDatabase database = TKMDatabase.getInstance(c, new AppExecutors());

        return IngredientLocalDataSource.getInstance(
                new AppExecutors(),
                database.ingredientEntityDao()
        );
    }

    public static RepositoryRecipeIngredient provideRecipeIngredientDataSource(
            @Nonnull Context c) {

        return RepositoryRecipeIngredient.getInstance(
                RepositoryRecipeIngredientRemote.getInstance(),
                RepositoryRecipeIngredientLocal.getInstance(
                        provideRecipeIngredientLocalGetAdapter(c),
                        provideRecipeIngredientLocalSaveAdapter(c),
                        provideRecipeIngredientLocalDeleteAdapter(c)
                )
        );
    }

    private static RecipeIngredientLocalGetAdapter provideRecipeIngredientLocalGetAdapter(
            @Nonnull Context c) {
        return new RecipeIngredientLocalGetAdapter(provideRecipeIngredientLocalDataSource(c));
    }

    private static RecipeIngredientLocalSaveAdapter provideRecipeIngredientLocalSaveAdapter(
            @Nonnull Context c) {
        return new RecipeIngredientLocalSaveAdapter(provideRecipeIngredientLocalDataSource(c));
    }

    private static RecipeIngredientLocalDeleteAdapter provideRecipeIngredientLocalDeleteAdapter(
            @Nonnull Context c) {
        return new RecipeIngredientLocalDeleteAdapter(provideRecipeIngredientLocalDataSource(c));
    }

    private static RecipeIngredientLocalDataSource provideRecipeIngredientLocalDataSource(
            @Nonnull Context c) {
        TKMDatabase database = TKMDatabase.getInstance(c, new AppExecutors());
        return RecipeIngredientLocalDataSource.getInstance(
                new AppExecutors(),
                database.recipeIngredientEntityDao()
        );
    }
}