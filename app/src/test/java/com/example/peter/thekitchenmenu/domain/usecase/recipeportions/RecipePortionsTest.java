package com.example.peter.thekitchenmenu.domain.usecase.recipeportions;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipePortionsEntity;

import org.junit.*;
import org.mockito.*;

import static com.example.peter.thekitchenmenu.domain.usecase.recipeportions.RecipePortions.DO_NOT_CLONE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipePortionsTest {

    // region constants ----------------------------------------------------------------------------
    private final RecipePortionsEntity NEW_EMPTY =
            TestDataRecipePortionsEntity.getNewValidEmpty();
    private final RecipePortionsEntity NEW_INVALID =
            TestDataRecipePortionsEntity.getNewInvalidServingsInvalidSittings();
    private final RecipePortionsEntity NEW_INVALID_SERVINGS_VALID_SITTINGS =
            TestDataRecipePortionsEntity.getNewInvalidServingsValidSittings();
    private final RecipePortionsEntity NEW_VALID_SERVINGS_INVALID_SITTINGS =
            TestDataRecipePortionsEntity.getNewValidServingsInvalidSittings();
    private final RecipePortionsEntity NEW_VALID =
            TestDataRecipePortionsEntity.getNewValidServingsValidSittings();
    private final RecipePortionsEntity EXISTING_VALID =
            TestDataRecipePortionsEntity.getExistingValidNinePortions();
    private final RecipePortionsEntity EXISTING_VALID_UPDATED_SERVINGS =
            TestDataRecipePortionsEntity.getExistingValidUpdatedServings();
    private final RecipePortionsEntity EXISTING_VALID_UPDATED_SITTINGS =
            TestDataRecipePortionsEntity.getExistingValidUpdatedSittings();
    private final RecipePortionsEntity EXISTING_VALID_CLONE =
            TestDataRecipePortionsEntity.getExistingValidClone();
    private final RecipePortionsEntity EXISTING_VALID_CLONE_UPDATED_SITTINGS_SERVINGS =
            TestDataRecipePortionsEntity.getExistingClonedUpdatedSittingsServings();
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    private UseCaseHandler handler;
    @Mock
    RepositoryRecipePortions repoMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipePortionsEntity>> repoCallback;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    UniqueIdProvider idProviderMock;
    private RecipePortionsResponse actualResponse;
    private int maxServings = TestDataRecipePortionsEntity.getMaxServings();
    private int maxSittings = TestDataRecipePortionsEntity.getMaxSittings();
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
                timeProviderMock, idProviderMock, repoMock, maxServings, maxSittings
        );
    }

    @Test
    public void newRecipeId_defaultModel_newDefaultModelReturned() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        whenTimeProviderReturn(NEW_EMPTY.getCreateDate());

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().setRecipeId(NEW_EMPTY.getRecipeId()).build();

        RecipePortionsModel expectedResponseModel = getModelFromEntity(NEW_EMPTY);
        // Act
        handler.execute(SUT, request, getCallback());
        simulateNothingReturnedFromDatabase();
        // Assert
        assertEquals(expectedResponseModel, actualResponse.getModel());
    }

    @Test
    public void newRecipeId_defaultModel_resultVALID_UNCHANGED() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        whenTimeProviderReturn(NEW_EMPTY.getCreateDate());

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().setRecipeId(NEW_EMPTY.getRecipeId()).build();
        // Act
        handler.execute(SUT, request, getCallback());
        simulateNothingReturnedFromDatabase();
        // Assert
        assertEquals(RecipePortions.Result.VALID_UNCHANGED, actualResponse.getResult());
    }

    @Test
    public void newRecipeId_invalidServingsInvalidSittings_resultINVALID_CHANGED() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        whenTimeProviderReturn(NEW_EMPTY.getCreateDate());

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().setRecipeId(NEW_EMPTY.getRecipeId()).build();

        handler.execute(SUT, request, getCallback());
        simulateNothingReturnedFromDatabase();
        // Act
        RecipePortionsModel invalidModel = getModelFromEntity(NEW_INVALID);
        RecipePortionsRequest invalidRequest = new RecipePortionsRequest.Builder().
                setRecipeId(NEW_EMPTY.getRecipeId()).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(invalidModel).
                build();

        handler.execute(SUT, invalidRequest, getCallback());
        // Assert
        assertEquals(RecipePortions.Result.INVALID_CHANGED, actualResponse.getResult());
    }

    @Test
    public void newRecipeId_invalidServingsValidSittings_resultINVALID_CHANGED() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        whenTimeProviderReturn(NEW_EMPTY.getCreateDate());

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().setRecipeId(NEW_EMPTY.getRecipeId()).build();

        handler.execute(SUT, request, getCallback());
        simulateNothingReturnedFromDatabase();
        // Act
        RecipePortionsModel invalidModel = getModelFromEntity(NEW_INVALID_SERVINGS_VALID_SITTINGS);
        RecipePortionsRequest invalidRequest = new RecipePortionsRequest.Builder().
                setRecipeId(NEW_EMPTY.getId()).
                setRecipeId(NEW_EMPTY.getRecipeId()).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(invalidModel).
                build();
        handler.execute(SUT, invalidRequest, getCallback());
        // Assert
        assertEquals(RecipePortions.Result.INVALID_CHANGED, actualResponse.getResult());
    }

    @Test
    public void newRecipeId_validServingsInvalidSittings_resultINVALID_CHANGED() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        whenTimeProviderReturn(NEW_EMPTY.getCreateDate());

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().setRecipeId(NEW_EMPTY.getRecipeId()).build();

        handler.execute(SUT, request, getCallback());
        simulateNothingReturnedFromDatabase();
        // Act
        RecipePortionsModel invalidModel = getModelFromEntity(NEW_VALID_SERVINGS_INVALID_SITTINGS);
        RecipePortionsRequest invalidRequest = new RecipePortionsRequest.Builder().
                setRecipeId(NEW_EMPTY.getId()).
                setRecipeId(NEW_EMPTY.getRecipeId()).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(invalidModel).
                build();
        handler.execute(SUT, invalidRequest, getCallback());
        // Assert
        assertEquals(RecipePortions.Result.INVALID_CHANGED, actualResponse.getResult());
    }

    @Test
    public void newRecipeId_validServingsValidSittings_resultVALID_CHANGED() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        whenTimeProviderReturn(NEW_EMPTY.getCreateDate());

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().setRecipeId(NEW_EMPTY.getRecipeId()).build();

        handler.execute(SUT, request, getCallback());
        simulateNothingReturnedFromDatabase();
        // Act
        RecipePortionsModel validModel = getModelFromEntity(NEW_VALID);
        RecipePortionsRequest validRequest = new RecipePortionsRequest.Builder().
                setRecipeId(NEW_EMPTY.getId()).
                setRecipeId(NEW_EMPTY.getRecipeId()).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(validModel).
                build();
        handler.execute(SUT, validRequest, getCallback());
        // Assert
        assertEquals(RecipePortions.Result.VALID_CHANGED, actualResponse.getResult());
    }

    @Test
    public void newRecipeId_validSittingsValidServings_saved() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        whenTimeProviderReturn(NEW_EMPTY.getCreateDate());

        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().setRecipeId(NEW_EMPTY.getRecipeId()).build();

        handler.execute(SUT, request, getCallback());
        simulateNothingReturnedFromDatabase();
        // Act
        RecipePortionsModel validModel = getModelFromEntity(NEW_VALID);
        RecipePortionsRequest validRequest = new RecipePortionsRequest.Builder().
                setRecipeId(NEW_EMPTY.getId()).
                setRecipeId(NEW_EMPTY.getRecipeId()).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(validModel).
                build();
        handler.execute(SUT, validRequest, getCallback());
        // Assert
        verify(repoMock).save(eq(NEW_VALID));
    }

    @Test
    public void existingRecipeId_validRecipeId_resultVALID_UNCHANGED() {
        // Arrange
        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setRecipeId(EXISTING_VALID.getRecipeId()).
                build();
        // Act
        handler.execute(SUT, request, getCallback());
        simulateExistingValidReturnedFromDatabase();
        RecipePortionsModel expectedModel = getModelFromEntity(EXISTING_VALID);
        // Assert
        assertEquals(expectedModel, actualResponse.getModel());
        assertEquals(RecipePortions.Result.VALID_UNCHANGED, actualResponse.getResult());
    }

    @Test
    public void existingRecipeId_invalidUpdatedServings_invalidValueNotSaved() {
        // Arrange
        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setRecipeId(EXISTING_VALID.getRecipeId()).
                build();
        handler.execute(SUT, request, getCallback());
        simulateExistingValidReturnedFromDatabase();
        // Act
        RecipePortionsModel invalidModel = RecipePortionsModel.Builder.
                basedOn(actualResponse.getModel()).
                setServings(NEW_INVALID.getServings()).
                build();
        RecipePortionsRequest invalidRequest = RecipePortionsRequest.Builder.
                basedOnRequest(request).setModel(invalidModel).build();
        handler.execute(SUT, invalidRequest, getCallback());
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void existingRecipeId_validUpdatedServings_saved() {
        // Arrange
        whenTimeProviderReturn(EXISTING_VALID_UPDATED_SERVINGS.getLastUpdate());
        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setRecipeId(EXISTING_VALID_UPDATED_SERVINGS.getRecipeId()).
                build();
        handler.execute(SUT, request, getCallback());
        simulateExistingValidReturnedFromDatabase();
        // Act
        RecipePortionsModel validModel = RecipePortionsModel.Builder.
                basedOn(actualResponse.getModel()).
                setServings(EXISTING_VALID_UPDATED_SERVINGS.getServings()).
                build();
        RecipePortionsRequest validRequest = RecipePortionsRequest.Builder.
                basedOnRequest(request).setModel(validModel).build();
        handler.execute(SUT, validRequest, getCallback());
        // Assert
        verify(repoMock).save(eq(EXISTING_VALID_UPDATED_SERVINGS));
    }

    @Test
    public void existingRecipeId_invalidUpdatedSittings_invalidValueNotSaved() {
        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setRecipeId(EXISTING_VALID.getRecipeId()).
                build();
        handler.execute(SUT, request, getCallback());
        simulateExistingValidReturnedFromDatabase();
        // Act
        RecipePortionsModel invalidModel = RecipePortionsModel.Builder.
                basedOn(actualResponse.getModel()).
                setSittings(NEW_INVALID.getSittings()).
                build();
        RecipePortionsRequest invalidRequest = RecipePortionsRequest.Builder.
                basedOnRequest(request).setModel(invalidModel).build();
        handler.execute(SUT, invalidRequest, getCallback());
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void existingRecipeId_validUpdatedSittings_saved() {
        // Arrange
        whenTimeProviderReturn(EXISTING_VALID_UPDATED_SITTINGS.getLastUpdate());
        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setRecipeId(EXISTING_VALID_UPDATED_SITTINGS.getRecipeId()).
                build();
        handler.execute(SUT, request, getCallback());
        simulateExistingValidReturnedFromDatabase();
        // Act
        RecipePortionsModel validModel = RecipePortionsModel.Builder.
                basedOn(actualResponse.getModel()).
                setSittings(EXISTING_VALID_UPDATED_SITTINGS.getSittings()).
                build();
        RecipePortionsRequest validRequest = RecipePortionsRequest.Builder.
                basedOnRequest(request).setModel(validModel).build();
        handler.execute(SUT, validRequest, getCallback());
        // Assert
        verify(repoMock).save(eq(EXISTING_VALID_UPDATED_SITTINGS));
    }

    @Test
    public void existingAndCloneToRecipeId_existingSavedWithCloneToRecipeId() {
        // Arrange
        whenIdProviderReturn(EXISTING_VALID_CLONE.getId());
        whenTimeProviderReturn(EXISTING_VALID_CLONE.getCreateDate());
        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setRecipeId(EXISTING_VALID.getRecipeId()).
                setCloneToRecipeId(EXISTING_VALID_CLONE.getRecipeId()).
                build();
        // Act
        handler.execute(SUT, request, getCallback());
        simulateExistingValidReturnedFromDatabase();
        // Assert
        verify(repoMock).save(EXISTING_VALID_CLONE);
    }

    @Test
    public void existingAndCloneToRecipeId_recipeModelStatusVALID_UNCHANGED() {
        // Arrange
        whenIdProviderReturn(EXISTING_VALID_CLONE.getId());
        whenTimeProviderReturn(EXISTING_VALID_CLONE.getCreateDate());
        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setRecipeId(EXISTING_VALID.getRecipeId()).
                setCloneToRecipeId(EXISTING_VALID_CLONE.getRecipeId()).
                build();
        // Act
        handler.execute(SUT, request, getCallback());
        simulateExistingValidReturnedFromDatabase();
        // Assert
        assertEquals(RecipePortions.Result.VALID_UNCHANGED, actualResponse.getResult());
    }

    @Test
    public void existingAndCloneToRecipeId_savedWithUpdatedSittingsServings() {
        whenIdProviderReturn(EXISTING_VALID_CLONE.getId());
        whenTimeProviderReturn(EXISTING_VALID_CLONE.getCreateDate());
        RecipePortionsRequest request = new RecipePortionsRequest.Builder().
                getDefault().
                setRecipeId(EXISTING_VALID.getRecipeId()).
                setCloneToRecipeId(EXISTING_VALID_CLONE.getRecipeId()).
                build();
        handler.execute(SUT, request, getCallback());
        simulateExistingValidReturnedFromDatabase();
        // Act
        RecipePortionsModel updatedServingsModel = RecipePortionsModel.Builder.
                basedOn(actualResponse.getModel()).
                setServings(EXISTING_VALID_CLONE_UPDATED_SITTINGS_SERVINGS.getServings()).
                build();
        RecipePortionsRequest updatedServingsRequest = new RecipePortionsRequest.
                Builder().
                setRecipeId(EXISTING_VALID_CLONE.getRecipeId()).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(updatedServingsModel).
                build();
        handler.execute(SUT, updatedServingsRequest, getCallback());

        RecipePortionsModel updatedSittingsModel = RecipePortionsModel.Builder.
                basedOn(actualResponse.getModel()).
                setSittings(EXISTING_VALID_CLONE_UPDATED_SITTINGS_SERVINGS.getSittings()).
                build();
        RecipePortionsRequest updatedSittingsRequest = RecipePortionsRequest.Builder.
                basedOnRequest(updatedServingsRequest).
                setModel(updatedSittingsModel).
                build();
        handler.execute(SUT, updatedSittingsRequest, getCallback());
        // Assert
        verify(repoMock).save(eq(EXISTING_VALID_CLONE_UPDATED_SITTINGS_SERVINGS));
    }

    // region helper methods -----------------------------------------------------------------------
    private void whenIdProviderReturn(String id) {
        when(idProviderMock.getUId()).thenReturn(id);
    }

    private void whenTimeProviderReturn(long time) {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
    }

    private void simulateNothingReturnedFromDatabase() {
        verify(repoMock).getPortionsForRecipe(eq(NEW_EMPTY.getRecipeId()), repoCallback.capture());
        repoCallback.getValue().onDataNotAvailable();
    }

    private void simulateExistingValidReturnedFromDatabase() {
        verify(repoMock).getPortionsForRecipe(eq(EXISTING_VALID.getRecipeId()), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(EXISTING_VALID);
    }

    private UseCaseInteractor.Callback<RecipePortionsResponse> getCallback() {
        return new UseCaseInteractor.Callback<RecipePortionsResponse>() {

            @Override
            public void onSuccess(RecipePortionsResponse response) {
                actualResponse = response;
            }

            @Override
            public void onError(RecipePortionsResponse response) {
                actualResponse = response;
            }
        };
    }

    private RecipePortionsModel getModelFromEntity(RecipePortionsEntity entity) {
        return new RecipePortionsModel.Builder().
                setId(entity.getId()).
                setRecipeId(entity.getRecipeId()).
                setServings(entity.getServings()).
                setSittings(entity.getSittings()).
                setCreateDate(entity.getCreateDate()).
                setLastUpdate(entity.getLastUpdate()).
                build();
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}