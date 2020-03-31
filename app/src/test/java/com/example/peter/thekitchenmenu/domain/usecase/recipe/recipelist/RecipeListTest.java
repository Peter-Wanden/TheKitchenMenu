package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist;

import com.example.peter.thekitchenmenu.commonmocks.RecipeComponents;
import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeComponentState;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseFactory;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipecourse.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeduration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeportions.RecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeduration.RecipeDurationTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeportions.RecipePortionsTest;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorTest;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeIdentityEntity;

import org.junit.*;
import org.mockito.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecipeListTest {

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RepositoryRecipeComponentState repoRecipeMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetEntityCallback<RecipeMetadataParentEntity>> repoRecipeCallback;
    @Mock
    UseCaseFactory useCaseFactoryMock;
    @Mock
    RepositoryRecipeIdentity repoIdentityMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetEntityCallback<RecipeIdentityEntity>> repoIdentityCallback;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetAllCallback<RecipeIdentityEntity>> repoIdentityALLCallback;
    @Mock
    RepositoryRecipeDuration repoDurationMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetEntityCallback<RecipeDurationEntity>> repoDurationCallback;
    @Mock
    RepositoryRecipeCourse repoCourseMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetAllCallback<RecipeCourseEntity>> repoCourseCallback;
    @Mock
    RepositoryRecipePortions repoPortionsMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetEntityCallback<RecipePortionsEntity>> repoPortionsCallback;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    UniqueIdProvider idProvideMock;

    private UseCaseHandler handler;

    private RecipeListResponse onSuccessResponse;
    private RecipeListResponse onErrorResponse;

    // endregion helper fields ---------------------------------------------------------------------

    private RecipeList SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();
    }

    private RecipeList givenUseCase() {
        handler = new UseCaseHandler(new UseCaseSchedulerMock());

        return new RecipeList(
                useCaseFactoryMock,
                handler);
    }

    @Test
    public void identityAndDurationRequest_getAllRecipes_recipesReturned() {
        // Arrange
        when(useCaseFactoryMock.provideRecipeMacro()).thenReturn(
                new Recipe(
                        handler,

                        new RecipeStateCalculator(),

                        new com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata(
                                timeProviderMock,
                                repoRecipeMock,
                                RecipeComponents.requiredComponents
                        ),
                        new RecipeIdentity(
                                repoIdentityMock,
                                timeProviderMock,
                                handler,
                                new TextValidator.Builder().
                                        setShortTextMinLength(
                                                TextValidatorTest.SHORT_TEXT_MIN_LENGTH).
                                        setShortTextMaxLength(
                                                TextValidatorTest.SHORT_TEXT_MAX_LENGTH).
                                        setLongTextMinLength(
                                                TextValidatorTest.LONG_TEXT_MIN_LENGTH).
                                        setLongTextMaxLength(
                                                TextValidatorTest.LONG_TEXT_MAX_LENGTH).
                                        build()
                        ),
                        new RecipeCourse(
                                repoCourseMock,
                                idProvideMock,
                                timeProviderMock
                        ),
                        new RecipeDuration(
                                repoDurationMock,
                                timeProviderMock,
                                RecipeDurationTest.MAX_PREP_TIME,
                                RecipeDurationTest.MAX_COOK_TIME
                        ),
                        new RecipePortions(
                                repoPortionsMock,
                                idProvideMock,
                                timeProviderMock,
                                RecipePortionsTest.MAX_SERVINGS,
                                RecipePortionsTest.MAX_SITTINGS
                        )
                )
        );

        RecipeListRequest request = new RecipeListRequest(
                RecipeList.RecipeFilter.ALL
        );
        // Act
        handler.execute(SUT, request, getCallback());

        verify(repoIdentityMock).getAll(repoIdentityALLCallback.capture());
        repoIdentityALLCallback.getValue().onAllLoaded(TestDataRecipeIdentityEntity.
                getValidIdentityEntities());

        // Assert

    }

    // region helper methods -----------------------------------------------------------------------
    private UseCase.Callback<RecipeListResponse> getCallback() {
        return new UseCase.Callback<RecipeListResponse>() {
            @Override
            public void onSuccess(RecipeListResponse response) {
                if (response != null) {
                    onSuccessResponse = response;
                }
            }

            @Override
            public void onError(RecipeListResponse response) {
                if (response != null) {
                    onErrorResponse = response;
                }
            }
        };
    }

    // endregion helper methods --------------------------------------------------------------------
    private void verifyIdentityDatabaseCalledWithIdAndReturnDataUnavailable(String recipeId) {
        verify(repoIdentityMock).getByDataId(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onDataUnavailable();
    }

    private void verifyCoursesDatabaseCalledWithIdAndReturnDataUnavailable(String recipeId) {
        verify(repoCourseMock).getAllByRecipeId(eq(recipeId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onDataUnavailable();
    }

    private void verifyDurationDatabaseCalledWithIdAndReturnDataUnavailable(String recipeId) {
        verify(repoDurationMock).getByDataId(eq(recipeId), repoDurationCallback.capture());
        repoDurationCallback.getValue().onDataUnavailable();
    }

    private void verifyPortionsDatabaseCalledWithIdAndReturnDataUnavailable(String recipeId) {
        verify(repoPortionsMock).getByRecipeId(eq(recipeId), repoPortionsCallback.capture());
        repoPortionsCallback.getValue().onDataUnavailable();
    }
    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}