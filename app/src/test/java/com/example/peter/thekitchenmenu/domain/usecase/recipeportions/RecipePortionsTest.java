package com.example.peter.thekitchenmenu.domain.usecase.recipeportions;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipePortionsEntity;

import org.junit.*;
import org.mockito.*;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.Recipe.DO_NOT_CLONE;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipePortionsTest {

    // region constants ----------------------------------------------------------------------------
    private final RecipePortionsEntity NEW_EMPTY =
            TestDataRecipePortionsEntity.getNewValidEmpty();
    private final RecipePortionsEntity INVALID_NEW =
            TestDataRecipePortionsEntity.getNewInvalidServingsInvalidSittings();
    private final RecipePortionsEntity INVALID_NEW_SERVINGS_VALID_SITTINGS =
            TestDataRecipePortionsEntity.getNewInvalidServingsValidSittings();
    private final RecipePortionsEntity VALID_NEW_SERVINGS_INVALID_SITTINGS =
            TestDataRecipePortionsEntity.getNewValidServingsInvalidSittings();
    private final RecipePortionsEntity VALID_NEW =
            TestDataRecipePortionsEntity.getNewValidServingsValidSittings();
    private final RecipePortionsEntity VALID_EXISTING =
            TestDataRecipePortionsEntity.getExistingValidNinePortions();
    private final RecipePortionsEntity VALID_EXISTING_UPDATED_SERVINGS =
            TestDataRecipePortionsEntity.getExistingValidUpdatedServings();
    private final RecipePortionsEntity VALID_EXISTING_UPDATED_SITTINGS =
            TestDataRecipePortionsEntity.getExistingValidUpdatedSittings();
    private final RecipePortionsEntity VALID_EXISTING_CLONE =
            TestDataRecipePortionsEntity.getExistingValidClone();
    private final RecipePortionsEntity VALID_EXISTING_CLONE_UPDATED_SITTINGS_SERVINGS =
            TestDataRecipePortionsEntity.getExistingClonedUpdatedSittingsServings();
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
                repoPortionsMock,
                idProviderMock,
                timeProviderMock,
                maxServings,
                maxSittings
        );
    }

    @Test
    public void newId_defaultModel_newDefaultModelReturned() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        whenTimeProviderReturn(NEW_EMPTY.getCreateDate());

        RecipePortionsRequest request = RecipePortionsRequest.Builder.getDefault().
                setRecipeId(NEW_EMPTY.getRecipeId()).
                build();

        RecipePortionsModel expectedResponseModel = getModelFromEntity(NEW_EMPTY);
        // Act
        handler.execute(SUT, request, getCallback());
        simulateNothingReturnedFromDatabase();
        // Assert
        assertEquals(expectedResponseModel, actualResponse.getModel());
    }

    @Test
    public void newId_defaultModel_resultVALID_UNCHANGED() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        whenTimeProviderReturn(NEW_EMPTY.getCreateDate());

        RecipePortionsRequest request = RecipePortionsRequest.Builder.getDefault().
                setRecipeId(NEW_EMPTY.getRecipeId()).
                build();
        // Act
        handler.execute(SUT, request, getCallback());
        simulateNothingReturnedFromDatabase();
        // Assert
        assertEquals(ComponentState.VALID_UNCHANGED, actualResponse.getState());
    }

    @Test
    public void newId_invalidServingsInvalidSittings_resultINVALID_CHANGED() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        whenTimeProviderReturn(NEW_EMPTY.getCreateDate());

        RecipePortionsRequest request = RecipePortionsRequest.Builder.getDefault().
                setRecipeId(NEW_EMPTY.getRecipeId()).
                build();

        handler.execute(SUT, request, getCallback());
        simulateNothingReturnedFromDatabase();
        // Act
        RecipePortionsModel invalidModel = getModelFromEntity(INVALID_NEW);
        RecipePortionsRequest invalidRequest = new RecipePortionsRequest.Builder().
                setRecipeId(NEW_EMPTY.getRecipeId()).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(invalidModel).
                build();

        handler.execute(SUT, invalidRequest, getCallback());
        // Assert
        assertEquals(ComponentState.INVALID_CHANGED, actualResponse.getState());
    }

    @Test
    public void newId_invalidServingsValidSittings_resultINVALID_CHANGED() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        whenTimeProviderReturn(NEW_EMPTY.getCreateDate());

        RecipePortionsRequest request = RecipePortionsRequest.Builder.getDefault().
                setRecipeId(NEW_EMPTY.getRecipeId()).
                build();

        handler.execute(SUT, request, getCallback());
        simulateNothingReturnedFromDatabase();
        // Act
        RecipePortionsModel invalidModel = getModelFromEntity(INVALID_NEW_SERVINGS_VALID_SITTINGS);
        RecipePortionsRequest invalidRequest = new RecipePortionsRequest.Builder().
                setRecipeId(NEW_EMPTY.getId()).
                setRecipeId(NEW_EMPTY.getRecipeId()).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(invalidModel).
                build();
        handler.execute(SUT, invalidRequest, getCallback());
        // Assert
        assertEquals(ComponentState.INVALID_CHANGED, actualResponse.getState());
    }

    @Test
    public void newId_validServingsInvalidSittings_resultINVALID_CHANGED() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        whenTimeProviderReturn(NEW_EMPTY.getCreateDate());

        RecipePortionsRequest request = RecipePortionsRequest.Builder.getDefault().
                setRecipeId(NEW_EMPTY.getRecipeId()).
                build();

        handler.execute(SUT, request, getCallback());
        simulateNothingReturnedFromDatabase();
        // Act
        RecipePortionsModel invalidModel = getModelFromEntity(VALID_NEW_SERVINGS_INVALID_SITTINGS);
        RecipePortionsRequest invalidRequest = new RecipePortionsRequest.Builder().
                setRecipeId(NEW_EMPTY.getId()).
                setRecipeId(NEW_EMPTY.getRecipeId()).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(invalidModel).
                build();
        handler.execute(SUT, invalidRequest, getCallback());
        // Assert
        assertEquals(ComponentState.INVALID_CHANGED, actualResponse.getState());
    }

    @Test
    public void newId_validServingsValidSittings_resultVALID_CHANGED() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        whenTimeProviderReturn(NEW_EMPTY.getCreateDate());

        RecipePortionsRequest request = RecipePortionsRequest.Builder.getDefault().
                setRecipeId(NEW_EMPTY.getRecipeId()).build();

        handler.execute(SUT, request, getCallback());
        simulateNothingReturnedFromDatabase();
        // Act
        RecipePortionsModel validModel = getModelFromEntity(VALID_NEW);
        RecipePortionsRequest validRequest = new RecipePortionsRequest.Builder().
                setRecipeId(NEW_EMPTY.getId()).
                setRecipeId(NEW_EMPTY.getRecipeId()).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(validModel).
                build();
        handler.execute(SUT, validRequest, getCallback());
        // Assert
        assertEquals(ComponentState.VALID_CHANGED, actualResponse.getState());
    }

    @Test
    public void newId_validSittingsValidServings_saved() {
        // Arrange
        whenIdProviderReturn(NEW_EMPTY.getId());
        whenTimeProviderReturn(NEW_EMPTY.getCreateDate());

        RecipePortionsRequest request = RecipePortionsRequest.Builder.getDefault().
                setRecipeId(NEW_EMPTY.getRecipeId()).
                build();

        handler.execute(SUT, request, getCallback());
        simulateNothingReturnedFromDatabase();
        // Act
        RecipePortionsModel validModel = getModelFromEntity(VALID_NEW);
        RecipePortionsRequest validRequest = new RecipePortionsRequest.Builder().
                setRecipeId(NEW_EMPTY.getId()).
                setRecipeId(NEW_EMPTY.getRecipeId()).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(validModel).
                build();
        handler.execute(SUT, validRequest, getCallback());
        // Assert
        verify(repoPortionsMock).save(eq(VALID_NEW));
    }

    @Test
    public void existingId_validRecipeId_resultVALID_UNCHANGED() {
        // Arrange
        RecipePortionsRequest request = RecipePortionsRequest.Builder.getDefault().
                setRecipeId(VALID_EXISTING.getRecipeId()).
                build();
        // Act
        handler.execute(SUT, request, getCallback());
        simulateExistingValidReturnedFromDatabase();
        RecipePortionsModel expectedModel = getModelFromEntity(VALID_EXISTING);
        // Assert
        assertEquals(expectedModel, actualResponse.getModel());
        assertEquals(ComponentState.VALID_UNCHANGED, actualResponse.getState());
    }

    @Test
    public void existingId_invalidUpdatedServings_invalidValueNotSaved() {
        // Arrange
        RecipePortionsRequest request = RecipePortionsRequest.Builder.getDefault().
                setRecipeId(VALID_EXISTING.getRecipeId()).
                build();
        handler.execute(SUT, request, getCallback());
        simulateExistingValidReturnedFromDatabase();
        // Act
        RecipePortionsModel invalidModel = RecipePortionsModel.Builder.
                basedOn(actualResponse.getModel()).
                setServings(INVALID_NEW.getServings()).
                build();
        RecipePortionsRequest invalidRequest = RecipePortionsRequest.Builder.
                basedOnRequest(request).setModel(invalidModel).build();
        handler.execute(SUT, invalidRequest, getCallback());
        // Assert
        verifyNoMoreInteractions(repoPortionsMock);
    }

    @Test
    public void existingId_validUpdatedServings_saved() {
        // Arrange
        whenTimeProviderReturn(VALID_EXISTING_UPDATED_SERVINGS.getLastUpdate());
        RecipePortionsRequest request = RecipePortionsRequest.Builder.getDefault().
                setRecipeId(VALID_EXISTING_UPDATED_SERVINGS.getRecipeId()).
                build();
        handler.execute(SUT, request, getCallback());
        simulateExistingValidReturnedFromDatabase();
        // Act
        RecipePortionsModel validModel = RecipePortionsModel.Builder.
                basedOn(actualResponse.getModel()).
                setServings(VALID_EXISTING_UPDATED_SERVINGS.getServings()).
                build();
        RecipePortionsRequest validRequest = RecipePortionsRequest.Builder.
                basedOnRequest(request).setModel(validModel).build();
        handler.execute(SUT, validRequest, getCallback());
        // Assert
        verify(repoPortionsMock).save(eq(VALID_EXISTING_UPDATED_SERVINGS));
    }

    @Test
    public void existingId_invalidUpdatedSittings_invalidValueNotSaved() {
        RecipePortionsRequest request = RecipePortionsRequest.Builder.getDefault().
                setRecipeId(VALID_EXISTING.getRecipeId()).
                build();
        handler.execute(SUT, request, getCallback());
        simulateExistingValidReturnedFromDatabase();
        // Act
        RecipePortionsModel invalidModel = RecipePortionsModel.Builder.
                basedOn(actualResponse.getModel()).
                setSittings(INVALID_NEW.getSittings()).
                build();
        RecipePortionsRequest invalidRequest = RecipePortionsRequest.Builder.
                basedOnRequest(request).setModel(invalidModel).build();
        handler.execute(SUT, invalidRequest, getCallback());
        // Assert
        verifyNoMoreInteractions(repoPortionsMock);
    }

    @Test
    public void existingId_validUpdatedSittings_saved() {
        // Arrange
        whenTimeProviderReturn(VALID_EXISTING_UPDATED_SITTINGS.getLastUpdate());
        RecipePortionsRequest request = RecipePortionsRequest.Builder.getDefault().
                setRecipeId(VALID_EXISTING_UPDATED_SITTINGS.getRecipeId()).
                build();
        handler.execute(SUT, request, getCallback());
        simulateExistingValidReturnedFromDatabase();
        // Act
        RecipePortionsModel validModel = RecipePortionsModel.Builder.
                basedOn(actualResponse.getModel()).
                setSittings(VALID_EXISTING_UPDATED_SITTINGS.getSittings()).
                build();
        RecipePortionsRequest validRequest = RecipePortionsRequest.Builder.
                basedOnRequest(request).setModel(validModel).build();
        handler.execute(SUT, validRequest, getCallback());
        // Assert
        verify(repoPortionsMock).save(eq(VALID_EXISTING_UPDATED_SITTINGS));
    }

    @Test
    public void existingAndCloneToId_existingSavedWithCloneToRecipeId() {
        // Arrange
        whenIdProviderReturn(VALID_EXISTING_CLONE.getId());
        whenTimeProviderReturn(VALID_EXISTING_CLONE.getCreateDate());
        RecipePortionsRequest request = RecipePortionsRequest.Builder.getDefault().
                setRecipeId(VALID_EXISTING.getRecipeId()).
                setCloneToRecipeId(VALID_EXISTING_CLONE.getRecipeId()).
                build();
        // Act
        handler.execute(SUT, request, getCallback());
        simulateExistingValidReturnedFromDatabase();
        // Assert
        verify(repoPortionsMock).save(VALID_EXISTING_CLONE);
    }

    @Test
    public void existingAndCloneToId_recipeModelStatusVALID_UNCHANGED() {
        // Arrange
        whenIdProviderReturn(VALID_EXISTING_CLONE.getId());
        whenTimeProviderReturn(VALID_EXISTING_CLONE.getCreateDate());
        RecipePortionsRequest request = RecipePortionsRequest.Builder.getDefault().
                setRecipeId(VALID_EXISTING.getRecipeId()).
                setCloneToRecipeId(VALID_EXISTING_CLONE.getRecipeId()).
                build();
        // Act
        handler.execute(SUT, request, getCallback());
        simulateExistingValidReturnedFromDatabase();
        // Assert
        assertEquals(ComponentState.VALID_UNCHANGED, actualResponse.getState());
    }

    @Test
    public void existingAndCloneToId_savedWithUpdatedSittingsServings() {
        whenIdProviderReturn(VALID_EXISTING_CLONE.getId());
        whenTimeProviderReturn(VALID_EXISTING_CLONE.getCreateDate());
        RecipePortionsRequest request = RecipePortionsRequest.Builder.getDefault().
                setRecipeId(VALID_EXISTING.getRecipeId()).
                setCloneToRecipeId(VALID_EXISTING_CLONE.getRecipeId()).
                build();
        handler.execute(SUT, request, getCallback());
        simulateExistingValidReturnedFromDatabase();
        // Act
        RecipePortionsModel updatedServingsModel = RecipePortionsModel.Builder.
                basedOn(actualResponse.getModel()).
                setServings(VALID_EXISTING_CLONE_UPDATED_SITTINGS_SERVINGS.getServings()).
                build();
        RecipePortionsRequest updatedServingsRequest = new RecipePortionsRequest.
                Builder().
                setRecipeId(VALID_EXISTING_CLONE.getRecipeId()).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(updatedServingsModel).
                build();
        handler.execute(SUT, updatedServingsRequest, getCallback());

        RecipePortionsModel updatedSittingsModel = RecipePortionsModel.Builder.
                basedOn(actualResponse.getModel()).
                setSittings(VALID_EXISTING_CLONE_UPDATED_SITTINGS_SERVINGS.getSittings()).
                build();
        RecipePortionsRequest updatedSittingsRequest = RecipePortionsRequest.Builder.
                basedOnRequest(updatedServingsRequest).
                setModel(updatedSittingsModel).
                build();
        handler.execute(SUT, updatedSittingsRequest, getCallback());
        // Assert
        verify(repoPortionsMock).save(eq(VALID_EXISTING_CLONE_UPDATED_SITTINGS_SERVINGS));
    }

    // region helper methods -----------------------------------------------------------------------
    private void whenIdProviderReturn(String id) {
        when(idProviderMock.getUId()).thenReturn(id);
    }

    private void whenTimeProviderReturn(long time) {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
    }

    private void simulateNothingReturnedFromDatabase() {
        verify(repoPortionsMock).getPortionsForRecipe(eq(NEW_EMPTY.getRecipeId()), repoPortionsCallback.capture());
        repoPortionsCallback.getValue().onDataNotAvailable();
    }

    private void simulateExistingValidReturnedFromDatabase() {
        verify(repoPortionsMock).getPortionsForRecipe(eq(VALID_EXISTING.getRecipeId()),
                repoPortionsCallback.capture());
        repoPortionsCallback.getValue().onEntityLoaded(VALID_EXISTING);
    }

    private UseCase.Callback<RecipePortionsResponse> getCallback() {
        return new UseCase.Callback<RecipePortionsResponse>() {

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