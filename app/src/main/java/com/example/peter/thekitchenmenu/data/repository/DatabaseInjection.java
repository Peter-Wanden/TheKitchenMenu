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
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.parent.RecipeCourseParentLocalDataSource;
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
import com.example.peter.thekitchenmenu.data.repository.ingredient.DataAccessIngredient;
import com.example.peter.thekitchenmenu.data.repository.product.DataAccessFavoriteProduct;
import com.example.peter.thekitchenmenu.data.repository.product.DataAccessProduct;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataAccessRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataAccessRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.recipe.RecipeIdentityUseCaseDataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataAccessRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataAccessRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataAccessRecipePortions;
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
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseItemLocalDataSource;
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

    public static DataAccessProduct provideProductDataSource(@Nonnull Context context) {

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return DataAccessProduct.getInstance(
                ProductRemoteDataSource.getInstance(),
                ProductLocalDataSource.getInstance(
                        new AppExecutors(), database.productEntityDao())
        );
    }

    public static DataAccessFavoriteProduct provideFavoriteProductsDataSource(
            @Nonnull Context context) {

        TKMDatabase database = TKMDatabase.getInstance(context, new AppExecutors());

        return DataAccessFavoriteProduct.getInstance(
                FavoriteProductsRemoteDataSource.getInstance(),
                FavoriteProductsLocalDataSource.getInstance(
                        new AppExecutors(),
                        database.favoriteProductEntityDao())
        );
    }

    public static DataAccessRecipeMetadata provideRecipeMetadataDataSource(@Nonnull Context c) {
        return DataAccessRecipeMetadata.getInstance(
                RepositoryRecipeMetadataRemote.getInstance(),
                RepositoryRecipeMetadataLocal.getInstance(
                        provideRecipeMetadataLocalGetAdapter(c),
                        provideRecipeMetadataLocalSaveAdapter(c),
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

    private static RecipeMetadataLocalSaveAdapter provideRecipeMetadataLocalSaveAdapter(
            @Nonnull Context c) {
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

    public static DataAccessRecipeCourse provideRecipeCourseDataSource(
            @Nonnull Context c) {
        return DataAccessRecipeCourse.getInstance(
                RepositoryRecipeCourseRemote.getInstance(),
                RepositoryRecipeCourseLocal.getInstance(
                        provideCourseLocalGetAdapter(c),
                        provideCourseLocalSaveAdapter(c),
                        provideCourseLocalDeleteAdapter(c)
                )
        );
    }

    private static CourseLocalGetAdapter provideCourseLocalGetAdapter(
            @Nonnull Context c) {
        return new CourseLocalGetAdapter(
                provideRecipeCourseParentLocalDataSource(c),
                provideRecipeCourseItemLocalDataSource(c));
    }

    private static CourseLocalSaveAdapter provideCourseLocalSaveAdapter(
            @Nonnull Context c) {
        return new CourseLocalSaveAdapter(
                provideRecipeCourseParentLocalDataSource(c),
                provideRecipeCourseItemLocalDataSource(c),
                new UniqueIdProvider());
    }

    private static CourseLocalDeleteAdapter provideCourseLocalDeleteAdapter(
            @Nonnull Context c) {
        return new CourseLocalDeleteAdapter(
                provideRecipeCourseParentLocalDataSource(c),
                provideRecipeCourseItemLocalDataSource(c));
    }

    private static RecipeCourseParentLocalDataSource provideRecipeCourseParentLocalDataSource(
            @Nonnull Context c) {

        TKMDatabase database = TKMDatabase.getInstance(c, new AppExecutors());

        return RecipeCourseParentLocalDataSource.getInstance(
                new AppExecutors(),
                database.recipeCourseParentEntityDao()
        );
    }

    private static RecipeCourseItemLocalDataSource provideRecipeCourseItemLocalDataSource(
            @Nonnull Context c) {

        TKMDatabase database = TKMDatabase.getInstance(c, new AppExecutors());

        return RecipeCourseItemLocalDataSource.getInstance(
                new AppExecutors(),
                database.recipeCourseEntityDao()
        );
    }

    public static RecipeIdentityUseCaseDataAccess provideRecipeIdentityDataSource(
            @Nonnull Context c) {

        return RecipeIdentityUseCaseDataAccess.getInstance(
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

    public static DataAccessRecipeDuration provideRecipeDurationDataSource(
            @Nonnull Context c) {

        return DataAccessRecipeDuration.getInstance(
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

    public static DataAccessRecipePortions provideRecipePortionsDataSource(
            @Nonnull Context c) {

        return DataAccessRecipePortions.getInstance(
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

    public static DataAccessIngredient provideIngredientDataSource(
            @Nonnull Context c) {
        return DataAccessIngredient.getInstance(
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

    public static DataAccessRecipeIngredient provideRecipeIngredientDataSource(
            @Nonnull Context c) {

        return DataAccessRecipeIngredient.getInstance(
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