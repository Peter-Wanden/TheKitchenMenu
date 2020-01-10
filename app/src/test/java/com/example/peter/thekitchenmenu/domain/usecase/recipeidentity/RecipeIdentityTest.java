package com.example.peter.thekitchenmenu.domain.usecase.recipeidentity;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.UseCase;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeIdentityEntity;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;

import org.junit.*;
import org.mockito.*;

import static com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentity.DO_NOT_CLONE;
import static com.example.peter.thekitchenmenu.domain.usecase.recipestate.RecipeState.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

public class RecipeIdentityTest {

    // region constants ----------------------------------------------------------------------------
    private static final RecipeIdentityEntity INVALID_NEW_EMPTY =
            TestDataRecipeIdentityEntity.getInvalidNewEmpty();
    private static final RecipeIdentityEntity INVALID_NEW_TITLE_INVALID =
            TestDataRecipeIdentityEntity.getInvalidNewTitleUpdatedWithInvalidValue();
    private static final RecipeIdentityEntity INVALID_NEW_TITLE_INVALID_DESCRIPTION_VALID =
            TestDataRecipeIdentityEntity.getInvalidNewTitleInvalidDescriptionValid();
    private static final RecipeIdentityEntity VALID_NEW_TITLE_VALID =
            TestDataRecipeIdentityEntity.getValidNewTitleUpdatedWithValidValue();
    private static final RecipeIdentityEntity VALID_NEW_COMPLETE =
            TestDataRecipeIdentityEntity.getValidNewComplete();
    private static final RecipeIdentityEntity INVALID_EXISTING_INCOMPLETE_INVALID_TITLE =
            TestDataRecipeIdentityEntity.getInvalidExistingIncomplete();

    private static final RecipeIdentityEntity VALID_EXISTING_COMPLETE =
            TestDataRecipeIdentityEntity.getValidExistingComplete();
    private static final RecipeIdentityEntity VALID_FROM_ANOTHER_USER =
            TestDataRecipeIdentityEntity.getValidCompleteFromAnotherUser();
    private static final RecipeIdentityEntity INVALID_FROM_ANOTHER_USER =
            TestDataRecipeIdentityEntity.getInvalidCompleteFromAnotherUser();
    private static final RecipeIdentityEntity VALID_NEW_CLONED =
            TestDataRecipeIdentityEntity.getValidNewCloned();
    private static final RecipeIdentityEntity INVALID_NEW_CLONED =
            TestDataRecipeIdentityEntity.getInvalidNewCloned();
    private static final RecipeIdentityEntity VALID_CLONED_DESCRIPTION_UPDATED =
            TestDataRecipeIdentityEntity.getValidNewClonedDescriptionUpdatedComplete();

    private static final String INVALID_TITLE = "";
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    private UseCaseHandler handler;
    @Mock
    RepositoryRecipeIdentity repoMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeIdentityEntity>> repoCallback;
    @Mock
    TimeProvider timeProviderMock;
    private RecipeIdentityResponse actualResponse;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeIdentity SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        handler = new UseCaseHandler(new UseCaseSchedulerMock());
        SUT = givenUseCase();
    }

    private RecipeIdentity givenUseCase() {
        return new RecipeIdentity(repoMock, timeProviderMock);
    }

    @Test
    public void newId_DATA_UNAVAILABLE() {
        // Arrange
        // Act
        givenNewEmptyModelSimulateNothingReturnedFromDatabase();
        // Assert
        assertEquals(ComponentState.DATA_UNAVAILABLE, actualResponse.getState());
    }

    // newId_validTitleInvalidDescription_stateINVALID_CHANGED
    // newId_validTitleInvalidDescription_failReasonsINVALID_DESCRIPTION

    @Test
    public void newId_invalidTitleValidDescription_stateINVALID_CHANGED() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(INVALID_NEW_EMPTY.getCreateDate());

        givenNewEmptyModelSimulateNothingReturnedFromDatabase();

        RecipeIdentityModel modelValidDescription = new RecipeIdentityModel.
                Builder().
                setId(INVALID_NEW_EMPTY.getId()).
                setTitle(INVALID_TITLE).
                setDescription("VALID_DESCRIPTION").
                setCreateDate(INVALID_NEW_EMPTY.getCreateDate()).
                setLastUpdate(INVALID_NEW_EMPTY.getCreateDate()).
                build();

        // Act
        handler.execute(
                SUT,
                getRequest(INVALID_NEW_EMPTY.getId(), DO_NOT_CLONE, modelValidDescription),
                getCallback());

        // Assert
        assertEquals(ComponentState.INVALID_CHANGED, actualResponse.getState());
    }

    // newId_invalidTitleValidDescription_failReasonsINVALID_TITLE

    @Test
    public void newId_validTitleNoDescription_valuesPersisted() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_TITLE_VALID.getCreateDate());

        givenNewEmptyModelSimulateNothingReturnedFromDatabase();

        RecipeIdentityModel validTitleModel = new RecipeIdentityModel.Builder().
                setId(actualResponse.getModel().getId()).
                setTitle(VALID_NEW_TITLE_VALID.getTitle()).
                setDescription(actualResponse.getModel().getDescription()).
                setCreateDate(actualResponse.getModel().getCreateDate()).
                setLastUpdate(actualResponse.getModel().getLastUpdate()).
                build();

        // Act
        handler.execute(
                SUT,
                getRequest(INVALID_NEW_EMPTY.getId(), DO_NOT_CLONE, validTitleModel),
                getCallback()
        );

        // Assert
        verify(repoMock).save(eq(VALID_NEW_TITLE_VALID));
        assertEquals(ComponentState.VALID_CHANGED, actualResponse.getState());
    }

    // newId_validTitleValidDescription_stateVALID_CHANGED
    // newId_validTitleValidDescription_failReasonsNONE

    @Test
    public void newId_validTitleValidDescription_valuesPersisted() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_COMPLETE.getCreateDate());
        givenNewEmptyModelSimulateNothingReturnedFromDatabase();

        RecipeIdentityModel validTitleAndDescription = new RecipeIdentityModel.
                Builder().
                setId(INVALID_NEW_EMPTY.getId()).
                setTitle(VALID_NEW_COMPLETE.getTitle()).
                setDescription(VALID_NEW_COMPLETE.getDescription()).
                setCreateDate(VALID_NEW_COMPLETE.getCreateDate()).
                setLastUpdate(VALID_NEW_COMPLETE.getLastUpdate()).
                build();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                setRecipeId(INVALID_NEW_EMPTY.getId()).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(validTitleAndDescription).
                build();
        // Act
        handler.execute(SUT, request, getCallback());

        // Assert
        verify(repoMock).save(VALID_NEW_COMPLETE);
        assertEquals(ComponentState.VALID_CHANGED, actualResponse.getState());
    }

    @Test
    public void existingId_existingValidValuesLoaded_VALID_UNCHANGED() {
        // Arrange
        RecipeIdentityRequest request = getRequest(
                VALID_EXISTING_COMPLETE.getId(), DO_NOT_CLONE, getDefaultModel());
        // Act
        handler.execute(SUT, request, getCallback());

        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(ComponentState.VALID_UNCHANGED, actualResponse.getState());
    }

    @Test
    public void existingId_invalidTitle_INVALID_UNCHANGED() {
        // Arrange

        // Act

        // Assert
    }

    @Test
    public void existingIdCloneToId_persistenceCalledWithExistingRecipeId() {
        // Arrange
        RecipeIdentityRequest request = getRequest(
                VALID_FROM_ANOTHER_USER.getId(), VALID_NEW_CLONED.getId(), getDefaultModel());

        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_CLONED.getCreateDate());
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        simulateGetValidFromAnotherUserFromDatabase();
        verify(repoMock).save(eq(VALID_NEW_CLONED));
        assertEquals(ComponentState.VALID_UNCHANGED, actualResponse.getState());
    }

    @Test
    public void existingIdCloneToId_descriptionChangedAfterClone_savedToNewEntity() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                VALID_CLONED_DESCRIPTION_UPDATED.getCreateDate());

        RecipeIdentityRequest requestClone = getRequest(
                VALID_FROM_ANOTHER_USER.getId(),
                VALID_CLONED_DESCRIPTION_UPDATED.getId(),
                getDefaultModel());

        // Act
        handler.execute(SUT, requestClone, getCallback());
        simulateGetValidFromAnotherUserFromDatabase();
        // Assert
        verify(repoMock).save(VALID_NEW_CLONED);

        // Arrange
        RecipeIdentityRequest requestUpdateDescription = getRequest(
                actualResponse.getModel().getId(),
                DO_NOT_CLONE,
                new RecipeIdentityModel(
                        actualResponse.getModel().getId(),
                        actualResponse.getModel().getTitle(),
                        VALID_CLONED_DESCRIPTION_UPDATED.getDescription(),
                        actualResponse.getModel().getCreateDate(),
                        actualResponse.getModel().getLastUpdate()));
        // Act
        handler.execute(SUT, requestUpdateDescription, getCallback());
        // Assert
        verify(repoMock).save(VALID_CLONED_DESCRIPTION_UPDATED);
        assertEquals(ComponentState.VALID_CHANGED, actualResponse.getState());
    }

    // region helper methods -----------------------------------------------------------------------
    private void givenNewEmptyModelSimulateNothingReturnedFromDatabase() {
        RecipeIdentityModel model = RecipeIdentityModel.Builder.getDefault().
                setId(INVALID_NEW_EMPTY.getId()).
                build();

        handler.execute(
                SUT,
                getRequest(INVALID_NEW_EMPTY.getId(), DO_NOT_CLONE, model),
                getCallback());
        simulateNothingReturnedFromDatabase();
    }

    private RecipeIdentityModel getDefaultModel() {
        return RecipeIdentityModel.Builder.getDefault().build();
    }

    private RecipeIdentityRequest getRequest(String recipeId,
                                              String cloneToRecipeId,
                                              RecipeIdentityModel model) {
        return new RecipeIdentityRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(cloneToRecipeId).
                setModel(model).
                build();
    }

    private UseCase.Callback<RecipeIdentityResponse> getCallback() {
        return new UseCase.Callback<RecipeIdentityResponse>() {

            @Override
            public void onSuccess(RecipeIdentityResponse response) {
                RecipeIdentityTest.this.actualResponse = response;

            }

            @Override
            public void onError(RecipeIdentityResponse response) {
                RecipeIdentityTest.this.actualResponse = response;
            }
        };
    }

    private void simulateNothingReturnedFromDatabase() {
        verify(repoMock).getById(eq(INVALID_NEW_EMPTY.getId()),
                repoCallback.capture());
        repoCallback.getValue().onDataNotAvailable();
    }

    private void simulateGetValidNewTitleValidFromDatabase() {
        verify(repoMock).getById(eq(VALID_NEW_TITLE_VALID.getId()),
                repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(VALID_NEW_TITLE_VALID);
    }

    private void simulateGetValidExistingCompleteFromDatabase() {
        verify(repoMock).getById(eq(VALID_EXISTING_COMPLETE.getId()),
                repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE);
    }

    private void simulateGetValidFromAnotherUserFromDatabase() {
        verify(repoMock).getById(eq(VALID_FROM_ANOTHER_USER.getId()), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(VALID_FROM_ANOTHER_USER);
    }


    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}