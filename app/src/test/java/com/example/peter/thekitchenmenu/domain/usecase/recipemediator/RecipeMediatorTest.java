package com.example.peter.thekitchenmenu.domain.usecase.recipemediator;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipe;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDurationRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeDurationEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeIdentityEntity;

import org.junit.*;
import org.mockito.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class RecipeMediatorTest {

    // region constants ----------------------------------------------------------------------------
    public static final RecipeEntity VALID_EXISTING_RECIPE_ENTITY =
            TestDataRecipeEntity.getValidExisting();
    private static final RecipeDurationEntity VALID_EXISTING_COMPLETE_DURATION =
            TestDataRecipeDurationEntity.getValidExistingComplete();
    private static final RecipeIdentityEntity VALID_EXISTING_COMPLETE_IDENTITY =
            TestDataRecipeIdentityEntity.getValidExistingComplete();

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RepositoryRecipe repoRecipeMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeEntity>> repoRecipeCallback;
    @Mock
    RepositoryRecipeDuration repoDurationMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeDurationEntity>> repoDurationCallback;
    @Mock
    RepositoryRecipeIdentity repoIdentityMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeIdentityEntity>> repoIdentityCallback;
    @Mock
    TimeProvider timeProviderMock;
    private RecipeMediatorResponse actualResponse;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeMediator SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        SUT = givenUseCase();
    }

    private RecipeMediator givenUseCase() {
        UseCaseHandler handler = new UseCaseHandler(new UseCaseSchedulerMock());

        RecipeIdentity identity = new RecipeIdentity(
                repoIdentityMock,
                timeProviderMock
        );

        int MAX_PREP_TIME = 6000;
        int MAX_COOK_TIME = 6000;
        RecipeDuration duration = new RecipeDuration(
                repoDurationMock,
                timeProviderMock,
                MAX_PREP_TIME,
                MAX_COOK_TIME);

        return new RecipeMediator(handler, identity, duration);
    }

    @Test
    public void getAllRecipeData() {
        // Arrange
        String recipeId = VALID_EXISTING_RECIPE_ENTITY.getId();
        // Act
        SUT.getAllRecipeData(recipeId, getMediatorCallback());

        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE_IDENTITY);

        verify(repoDurationMock).getById(eq(recipeId), repoDurationCallback.capture());
        repoDurationCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE_DURATION);

        // Assert
        System.out.println(actualResponse);
    }

    @Test
    public void executeDurationRequest_durationDataReturned() {
        // Arrange
        String recipeId = VALID_EXISTING_COMPLETE_DURATION.getId();
        RecipeDurationRequest request = RecipeDurationRequest.Builder.getDefault().
                setRecipeId(recipeId).build();
        // Act
        SUT.executeDurationRequest(request, getMediatorCallback());
        verify(repoDurationMock).getById(eq(recipeId), repoDurationCallback.capture());
        repoDurationCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE_DURATION);
        // Assert
        System.out.println(actualResponse);
    }

    // region helper methods -----------------------------------------------------------------------
    private RecipeMediator.Callback<RecipeMediatorResponse> getMediatorCallback() {
        return new RecipeMediator.Callback<RecipeMediatorResponse>() {
            @Override
            public void onSuccess(RecipeMediatorResponse response) {
                actualResponse = response;
            }

            @Override
            public void onError(RecipeMediatorResponse response) {
                actualResponse = response;
            }
        };
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}