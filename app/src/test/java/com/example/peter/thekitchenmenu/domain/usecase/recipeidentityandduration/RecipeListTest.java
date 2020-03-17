package com.example.peter.thekitchenmenu.domain.usecase.recipeidentityandduration;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipe;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseFactory;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipecourse.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeduration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeportions.RecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration.RecipeDurationTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist.RecipeList;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist.RecipeListRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipelist.RecipeListResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions.RecipePortionsTest;
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
    RepositoryRecipe repoRecipeMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeEntity>> repoRecipeCallback;
    @Mock
    UseCaseFactory useCaseFactoryMock;
    @Mock
    RepositoryRecipeIdentity repoIdentityMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeIdentityEntity>> repoIdentityCallback;
    @Captor
    ArgumentCaptor<DataSource.GetAllCallback<RecipeIdentityEntity>> repoIdentityALLCallback;
    @Mock
    RepositoryRecipeDuration repoDurationMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeDurationEntity>> repoDurationCallback;
    @Mock
    RepositoryRecipeCourse repoCourseMock;
    @Captor
    ArgumentCaptor<DataSource.GetAllCallback<RecipeCourseEntity>> repoCourseCallback;
    @Mock
    RepositoryRecipePortions repoPortionsMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipePortionsEntity>> repoPortionsCallback;
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

                        new RecipeMetadata(
                                timeProviderMock,
                                repoRecipeMock
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
        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onDataNotAvailable();
    }

    private void verifyCoursesDatabaseCalledWithIdAndReturnDataUnavailable(String recipeId) {
        verify(repoCourseMock).getCoursesForRecipe(eq(recipeId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onDataNotAvailable();
    }

    private void verifyDurationDatabaseCalledWithIdAndReturnDataUnavailable(String recipeId) {
        verify(repoDurationMock).getById(eq(recipeId), repoDurationCallback.capture());
        repoDurationCallback.getValue().onDataNotAvailable();
    }

    private void verifyPortionsDatabaseCalledWithIdAndReturnDataUnavailable(String recipeId) {
        verify(repoPortionsMock).getPortionsForRecipe(eq(recipeId), repoPortionsCallback.capture());
        repoPortionsCallback.getValue().onDataNotAvailable();
    }
    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}