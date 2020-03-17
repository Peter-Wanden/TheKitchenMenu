package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeportions.RecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeportions.RecipePortionsRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeportions.RecipePortionsResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipePortionsEntity;

import org.junit.*;
import org.mockito.*;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeportions.RecipePortions.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipePortionsTest {

    private static final String TAG = "tkm-" + RecipePortionsTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    private final RecipePortionsEntity NEW_EMPTY =
            TestDataRecipePortionsEntity.getNewValidEmpty();
    private final RecipePortionsEntity INVALID_NEW_TOO_HIGH_SITTINGS_TOO_HIGH_SERVINGS =
            TestDataRecipePortionsEntity.getNewInvalidTooHighServingsInvalidTooHighSittings();
    private final RecipePortionsEntity INVALID_NEW_TOO_HIGH_SERVINGS_VALID_SITTINGS =
            TestDataRecipePortionsEntity.getNewInvalidTooHighServingsValidSittings();
    private final RecipePortionsEntity INVALID_NEW_VALID_SERVINGS_TOO_HIGH_SITTINGS =
            TestDataRecipePortionsEntity.getNewValidServingsInvalidTooHighSittings();
    private final RecipePortionsEntity VALID_NEW =
            TestDataRecipePortionsEntity.getNewValidServingsValidSittings();
    private final RecipePortionsEntity VALID_EXISTING =
            TestDataRecipePortionsEntity.getExistingValidNinePortions();
    private final RecipePortionsEntity VALID_EXISTING_UPDATED_SERVINGS =
            TestDataRecipePortionsEntity.getExistingValidUpdatedServings();
    private final RecipePortionsEntity VALID_EXISTING_UPDATED_SITTINGS =
            TestDataRecipePortionsEntity.getExistingValidUpdatedSittings();

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    private UseCaseHandler handler;
    @Mock
    RepositoryRecipePortions repoPortionsMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipePortionsEntity>> repoPortionsCallback;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    UniqueIdProvider idProviderMock;

    private RecipePortionsResponse portionsOnSuccessResponse;
    private RecipePortionsResponse portionsOnErrorResponse;

    public static final int MAX_SERVINGS = TestDataRecipePortionsEntity.getMaxServings();
    public static final int MAX_SITTINGS = TestDataRecipePortionsEntity.getMaxSittings();
    // endregion helper fields ---------------------------------------------------------------------

    private RecipePortions SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        handler = new UseCaseHandler(new UseCaseSchedulerMock());
        SUT = givenUseCase();
    }

    private RecipePortions givenUseCase() {
        return new RecipePortions(
                repoPortionsMock,
                idProviderMock,
                timeProviderMock,
                MAX_SERVINGS,
                MAX_SITTINGS
        );
    }

    @Test
    public void newId_defaultModel_newDefaultModelReturned() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        whenTimeProviderReturn(NEW_EMPTY.getCreateDate());

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setId(NEW_EMPTY.getRecipeId()).
                build();
        RecipePortionsResponse.Model expectedResponseModel = getResponseModelFromEntity(NEW_EMPTY);
        // Act
        handler.execute(SUT, request, getCallback());
        simulateNothingReturnedFromDatabase();
        // Assert
        assertEquals(expectedResponseModel, portionsOnErrorResponse.getModel());
    }

    @Test
    public void newId_defaultModel_resultDATA_UNAVAILABLE() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        whenTimeProviderReturn(NEW_EMPTY.getCreateDate());

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setId(NEW_EMPTY.getRecipeId()).
                build();
        // Act
        handler.execute(SUT, request, getCallback());
        simulateNothingReturnedFromDatabase();
        // Assert
        assertEquals(RecipeStateCalculator.ComponentState.INVALID_UNCHANGED,
                portionsOnErrorResponse.getMetadata().getState());
        assertTrue(portionsOnErrorResponse.getMetadata().
                getFailReasons().
                contains(CommonFailReason.DATA_UNAVAILABLE));
    }

    @Test
    public void newId_invalidServingsInvalidSittings_resultINVALID_CHANGED() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        whenTimeProviderReturn(NEW_EMPTY.getCreateDate());

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setId(NEW_EMPTY.getRecipeId()).
                build();

        handler.execute(SUT, request, getCallback());
        simulateNothingReturnedFromDatabase();
        // Act
        RecipePortionsRequest.Model invalidModel = getRequestModelFromEntity(
                INVALID_NEW_TOO_HIGH_SITTINGS_TOO_HIGH_SERVINGS);

        RecipePortionsRequest invalidRequest = new RecipePortionsRequest.Builder().
                setId(NEW_EMPTY.getRecipeId()).
                setModel(invalidModel).
                build();

        handler.execute(SUT, invalidRequest, getCallback());
        // Assert
        assertEquals(RecipeStateCalculator.ComponentState.INVALID_CHANGED,
                portionsOnErrorResponse.getMetadata().getState());
        assertEquals(2, portionsOnErrorResponse.getMetadata().getFailReasons().size());
        assertTrue(portionsOnErrorResponse.getMetadata().
                getFailReasons().
                contains(FailReason.SERVINGS_TOO_HIGH));
        assertTrue(portionsOnErrorResponse.
                getMetadata().
                getFailReasons().contains(FailReason.SITTINGS_TOO_HIGH));
    }

    @Test
    public void newId_invalidServingsValidSittings_resultINVALID_CHANGED() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        whenTimeProviderReturn(NEW_EMPTY.getCreateDate());

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setId(NEW_EMPTY.getRecipeId()).
                build();

        handler.execute(SUT, request, getCallback());
        simulateNothingReturnedFromDatabase();
        // Act
        RecipePortionsRequest.Model invalidModel = getRequestModelFromEntity(
                INVALID_NEW_TOO_HIGH_SERVINGS_VALID_SITTINGS);
        RecipePortionsRequest invalidRequest = new RecipePortionsRequest.Builder().
                setId(NEW_EMPTY.getRecipeId()).
                setModel(invalidModel).
                build();
        handler.execute(SUT, invalidRequest, getCallback());
        // Assert
        assertEquals(1, portionsOnErrorResponse.getMetadata().
                getFailReasons().
                size());
        assertTrue(portionsOnErrorResponse.getMetadata().
                getFailReasons().
                contains(FailReason.SERVINGS_TOO_HIGH));
        assertEquals(RecipeStateCalculator.ComponentState.INVALID_CHANGED,
                portionsOnErrorResponse.getMetadata().getState());
    }

    @Test
    public void newId_validServingsInvalidSittings_resultINVALID_CHANGED() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        whenTimeProviderReturn(NEW_EMPTY.getCreateDate());

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setId(NEW_EMPTY.getRecipeId()).
                build();

        handler.execute(SUT, request, getCallback());
        simulateNothingReturnedFromDatabase();
        // Act
        RecipePortionsRequest.Model invalidModel = getRequestModelFromEntity(
                INVALID_NEW_VALID_SERVINGS_TOO_HIGH_SITTINGS);
        RecipePortionsRequest invalidRequest = new RecipePortionsRequest.Builder().
                setId(NEW_EMPTY.getRecipeId()).
                setModel(invalidModel).
                build();
        handler.execute(SUT, invalidRequest, getCallback());
        // Assert
        assertEquals(RecipeStateCalculator.ComponentState.INVALID_CHANGED,
                portionsOnErrorResponse.getMetadata().getState());
        assertEquals(1, portionsOnErrorResponse.getMetadata().
                getFailReasons().
                size());
        assertTrue(portionsOnErrorResponse.getMetadata().
                getFailReasons().
                contains(FailReason.SITTINGS_TOO_HIGH));
    }

    @Test
    public void newId_validServingsValidSittings_resultVALID_CHANGED() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        whenTimeProviderReturn(NEW_EMPTY.getCreateDate());

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setId(NEW_EMPTY.getRecipeId()).build();

        handler.execute(SUT, request, getCallback());
        simulateNothingReturnedFromDatabase();
        // Act
        RecipePortionsRequest.Model validModel = getRequestModelFromEntity(VALID_NEW);
        RecipePortionsRequest validRequest = new RecipePortionsRequest.Builder().
                setId(NEW_EMPTY.getRecipeId()).
                setModel(validModel).
                build();
        handler.execute(SUT, validRequest, getCallback());
        // Assert
        assertEquals(RecipeStateCalculator.ComponentState.VALID_CHANGED,
                portionsOnSuccessResponse.getMetadata().getState());
        assertEquals(1, portionsOnSuccessResponse.getMetadata().
                getFailReasons().
                size());
        assertTrue(portionsOnSuccessResponse.getMetadata().
                getFailReasons().
                contains(CommonFailReason.NONE));
    }

    @Test
    public void newId_validSittingsValidServings_saved() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        whenTimeProviderReturn(NEW_EMPTY.getCreateDate());

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setId(NEW_EMPTY.getRecipeId()).
                build();

        handler.execute(SUT, request, getCallback());
        simulateNothingReturnedFromDatabase();
        // Act
        RecipePortionsRequest.Model validModel = getRequestModelFromEntity(VALID_NEW);
        System.out.println(TAG + validModel);

        RecipePortionsRequest validRequest = new RecipePortionsRequest.Builder().
                setId(NEW_EMPTY.getRecipeId()).
                setModel(validModel).
                build();
        handler.execute(SUT, validRequest, getCallback());
        // Assert
        verify(repoPortionsMock).save(eq(VALID_NEW));
        assertEquals(1, portionsOnSuccessResponse.getMetadata().
                getFailReasons().
                size());
        assertTrue(portionsOnSuccessResponse.getMetadata().
                getFailReasons().
                contains(CommonFailReason.NONE));
    }

    @Test
    public void existingId_validRecipeId_resultVALID_UNCHANGED() {
        // Arrange
        String recipeId = VALID_EXISTING.getRecipeId();
        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setId(recipeId).
                build();
        // Act
        handler.execute(SUT, request, getCallback());
        simulateValidExistingReturnedFromDatabase(recipeId);

        RecipePortionsResponse.Model expectedModel = getResponseModelFromEntity(VALID_EXISTING);
        // Assert
        assertEquals(expectedModel, portionsOnSuccessResponse.getModel());
        assertEquals(RecipeStateCalculator.ComponentState.VALID_UNCHANGED,
                portionsOnSuccessResponse.
                        getMetadata().
                        getState());
        assertEquals(1, portionsOnSuccessResponse.getMetadata().
                getFailReasons().
                size());
        assertTrue(portionsOnSuccessResponse.getMetadata().
                getFailReasons().
                contains(CommonFailReason.NONE));
    }

    @Test
    public void existingId_invalidUpdatedServings_invalidValueNotSaved() {
        // Arrange
        String recipeId = VALID_EXISTING.getRecipeId();
        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setId(recipeId).
                build();
        handler.execute(SUT, request, getCallback());
        simulateValidExistingReturnedFromDatabase(recipeId);
        // Act
        RecipePortionsRequest.Model invalidModel = RecipePortionsRequest.Model.Builder.
                basedOnPortionsResponseModel(portionsOnSuccessResponse.getModel()).
                setServings(INVALID_NEW_TOO_HIGH_SITTINGS_TOO_HIGH_SERVINGS.getServings()).
                build();
        RecipePortionsRequest invalidRequest = new RecipePortionsRequest.Builder().
                setId(recipeId).
                setModel(invalidModel).build();
        handler.execute(SUT, invalidRequest, getCallback());
        // Assert
        verifyNoMoreInteractions(repoPortionsMock);
    }

    @Test
    public void existingId_validUpdatedServings_saved() {
        // Arrange
        String recipeId = VALID_EXISTING_UPDATED_SERVINGS.getRecipeId();
        whenTimeProviderReturn(VALID_EXISTING_UPDATED_SERVINGS.getLastUpdate());
        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setId(recipeId).
                build();
        handler.execute(SUT, request, getCallback());
        simulateValidExistingReturnedFromDatabase(recipeId);
        // Act
        RecipePortionsRequest.Model validModel = RecipePortionsRequest.Model.Builder.
                basedOnPortionsResponseModel(portionsOnSuccessResponse.getModel()).
                setServings(VALID_EXISTING_UPDATED_SERVINGS.getServings()).
                build();
        RecipePortionsRequest validRequest = new RecipePortionsRequest.Builder().
                setId(recipeId).
                setModel(validModel).
                build();
        handler.execute(SUT, validRequest, getCallback());
        // Assert
        verify(repoPortionsMock).save(eq(VALID_EXISTING_UPDATED_SERVINGS));
    }

    @Test
    public void existingId_invalidUpdatedSittings_invalidValueNotSaved() {
        String recipeId = VALID_EXISTING.getRecipeId();
        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setId(recipeId).
                build();
        handler.execute(SUT, request, getCallback());
        simulateValidExistingReturnedFromDatabase(recipeId);
        // Act
        RecipePortionsRequest.Model invalidModel = RecipePortionsRequest.Model.Builder.
                basedOnPortionsResponseModel(portionsOnSuccessResponse.getModel()).
                setSittings(INVALID_NEW_TOO_HIGH_SITTINGS_TOO_HIGH_SERVINGS.getSittings()).
                build();
        RecipePortionsRequest invalidRequest = new RecipePortionsRequest.Builder().
                setId(recipeId).
                setModel(invalidModel).
                build();
        handler.execute(SUT, invalidRequest, getCallback());
        // Assert
        verifyNoMoreInteractions(repoPortionsMock);
        assertTrue(portionsOnErrorResponse.getMetadata().
                getFailReasons().
                contains(FailReason.SITTINGS_TOO_HIGH));
    }

    @Test
    public void existingId_validUpdatedSittings_saved() {
        // Arrange
        String recipeId = VALID_EXISTING_UPDATED_SITTINGS.getRecipeId();
        whenTimeProviderReturn(VALID_EXISTING_UPDATED_SITTINGS.getLastUpdate());
        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setId(recipeId).
                build();
        handler.execute(SUT, request, getCallback());
        simulateValidExistingReturnedFromDatabase(recipeId);
        // Act
        RecipePortionsRequest.Model validModel = RecipePortionsRequest.Model.Builder.
                basedOnPortionsResponseModel(portionsOnSuccessResponse.getModel()).
                setSittings(VALID_EXISTING_UPDATED_SITTINGS.getSittings()).
                build();
        RecipePortionsRequest validRequest = new RecipePortionsRequest.Builder().
                setId(recipeId).
                setModel(validModel).
                build();
        handler.execute(SUT, validRequest, getCallback());
        // Assert
        verify(repoPortionsMock).save(eq(VALID_EXISTING_UPDATED_SITTINGS));
        assertEquals(1, portionsOnSuccessResponse.getMetadata().
                getFailReasons().
                size());
        assertTrue(portionsOnSuccessResponse.getMetadata().
                getFailReasons().
                contains(CommonFailReason.NONE));
    }

    // region helper methods -----------------------------------------------------------------------
    private void whenIdProviderReturn(String id) {
        when(idProviderMock.getUId()).thenReturn(id);
    }

    private void whenTimeProviderReturn(long time) {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
    }

    private void simulateNothingReturnedFromDatabase() {
        verify(repoPortionsMock).getPortionsForRecipe(eq(NEW_EMPTY.getRecipeId()),
                repoPortionsCallback.capture());
        repoPortionsCallback.getValue().onDataNotAvailable();
    }

    private void simulateValidExistingReturnedFromDatabase(String recipeId) {
        assertEquals(VALID_EXISTING.getRecipeId(), recipeId);
        verify(repoPortionsMock).getPortionsForRecipe(eq(recipeId), repoPortionsCallback.capture());
        repoPortionsCallback.getValue().onEntityLoaded(VALID_EXISTING);
    }

    private UseCase.Callback<RecipePortionsResponse> getCallback() {
        return new UseCase.Callback<RecipePortionsResponse>() {

            @Override
            public void onSuccess(RecipePortionsResponse response) {
                portionsOnSuccessResponse = response;
            }

            @Override
            public void onError(RecipePortionsResponse response) {
                portionsOnErrorResponse = response;
            }
        };
    }

    private RecipePortionsResponse.Model getResponseModelFromEntity(RecipePortionsEntity entity) {
        return new RecipePortionsResponse.Model.Builder().
                setServings(entity.getServings()).
                setSittings(entity.getSittings()).
                setPortions(entity.getServings() * entity.getSittings()).
                build();
    }

    private RecipePortionsRequest.Model getRequestModelFromEntity(RecipePortionsEntity entity) {
        return new RecipePortionsRequest.Model.Builder().
                setServings(entity.getServings()).
                setSittings(entity.getSittings()).
                build();
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}