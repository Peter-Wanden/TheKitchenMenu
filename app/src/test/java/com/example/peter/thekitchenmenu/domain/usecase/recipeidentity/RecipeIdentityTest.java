package com.example.peter.thekitchenmenu.domain.usecase.recipeidentity;

import com.example.peter.thekitchenmenu.commonmocks.StringMaker;
import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorTest;
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
    private static final RecipeIdentityEntity VALID_NEW_TITLE_VALID =
            TestDataRecipeIdentityEntity.getValidNewTitleValidDescriptionDefault();
    private static final RecipeIdentityEntity VALID_NEW_COMPLETE =
            TestDataRecipeIdentityEntity.getValidNewComplete();

    private static final RecipeIdentityEntity VALID_EXISTING_COMPLETE =
            TestDataRecipeIdentityEntity.getValidExistingTitleValidDescriptionValid();
    private static final RecipeIdentityEntity VALID_FROM_ANOTHER_USER =
            TestDataRecipeIdentityEntity.getValidCompleteFromAnotherUser();
    private static final RecipeIdentityEntity VALID_NEW_CLONED =
            TestDataRecipeIdentityEntity.getValidCompleteAfterCloned();
    private static final RecipeIdentityEntity VALID_CLONED_DESCRIPTION_UPDATED =
            TestDataRecipeIdentityEntity.getValidClonedDescriptionUpdated();

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

    private RecipeIdentityResponse onSuccessResponse;
    private RecipeIdentityResponse onErrorResponse;

    private static final int SHORT_TEXT_MIN_LENGTH = 3;
    private static final int SHORT_TEXT_MAX_LENGTH = 70;
    private static final int LONG_TEXT_MIN_LENGTH = 0;
    private static final int LONG_TEXT_MAX_LENGTH = 500;

    public static final int TITLE_MIN_LENGTH = SHORT_TEXT_MIN_LENGTH;
    public static final int TITLE_MAX_LENGTH = SHORT_TEXT_MAX_LENGTH;
    public static final int DESCRIPTION_MIN_LENGTH = LONG_TEXT_MIN_LENGTH;
    public static final int DESCRIPTION_MAX_LENGTH = LONG_TEXT_MAX_LENGTH;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeIdentity SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        handler = new UseCaseHandler(new UseCaseSchedulerMock());
        SUT = givenUseCase();
    }

    private RecipeIdentity givenUseCase() {
        TextValidator textValidator = new TextValidator.Builder().
                setShortTextMinLength(SHORT_TEXT_MIN_LENGTH).
                setShortTextMaxLength(SHORT_TEXT_MAX_LENGTH).
                setLongTextMinLength(LONG_TEXT_MIN_LENGTH).
                setLongTextMaxLength(LONG_TEXT_MAX_LENGTH).
                build();

        return new RecipeIdentity(
                repoMock,
                timeProviderMock,
                handler,
                textValidator);
    }

    @Test
    public void newId_DATA_UNAVAILABLE() {
        // Arrange
        // Act
        givenNewEmptyModelSimulateNothingReturnedFromDatabase();
        // Assert
        assertEquals(ComponentState.DATA_UNAVAILABLE, onErrorResponse.getState());
    }

    @Test
    public void newId_invalidTitleValidDescription_stateINVALID_CHANGED() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(INVALID_NEW_EMPTY.getCreateDate());

        givenNewEmptyModelSimulateNothingReturnedFromDatabase();

        String validDescription = new StringMaker().
                makeStringOfExactLength(TextValidatorTest.LONG_TEXT_MAX_LENGTH).
                build();

        RecipeIdentityRequest.Model modelValidDescription = RecipeIdentityRequest.Model.Builder.
                basedOn(onSuccessResponse.getModel()).
                setTitle(INVALID_TITLE).
                setDescription(validDescription).
                build();

        // Act
        handler.execute(
                SUT,
                getRequest(INVALID_NEW_EMPTY.getId(), DO_NOT_CLONE, modelValidDescription),
                getCallback());

        // Assert
        assertEquals(ComponentState.INVALID_CHANGED, onSuccessResponse.getState());
    }

    @Test
    public void newId_validTitleNoDescription_valuesPersisted() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_TITLE_VALID.getCreateDate());

        givenNewEmptyModelSimulateNothingReturnedFromDatabase();

        RecipeIdentityRequest.Model validTitleModel = new RecipeIdentityRequest.Model.Builder().
                setTitle(VALID_NEW_TITLE_VALID.getTitle()).
                setDescription(onSuccessResponse.getModel().getDescription()).
                build();

        // Act
        handler.execute(
                SUT,
                getRequest(INVALID_NEW_EMPTY.getId(), DO_NOT_CLONE, validTitleModel),
                getCallback()
        );

        // Assert
        verify(repoMock).save(eq(VALID_NEW_TITLE_VALID));
        assertEquals(ComponentState.VALID_CHANGED, onSuccessResponse.getState());
    }

    @Test
    public void newId_validTitleValidDescription_valuesPersisted() {
        // Arrange
        whenTimeProviderReturnTime(VALID_NEW_COMPLETE.getCreateDate());
        givenNewEmptyModelSimulateNothingReturnedFromDatabase();

        RecipeIdentityRequest.Model requestModel = new RecipeIdentityRequest.Model.Builder().
                setTitle(VALID_NEW_COMPLETE.getTitle()).
                setDescription(VALID_NEW_COMPLETE.getDescription()).
                build();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                setRecipeId(INVALID_NEW_EMPTY.getId()).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(requestModel).
                build();
        // Act
        handler.execute(SUT, request, getCallback());

        // Assert
        verify(repoMock).save(VALID_NEW_COMPLETE);
        assertEquals(ComponentState.VALID_CHANGED, onSuccessResponse.getState());
    }

    @Test
    public void existingId_existingValidValuesLoaded_VALID_UNCHANGED() {
        // Arrange
        RecipeIdentityRequest request = getRequest(
                VALID_EXISTING_COMPLETE.getId(),
                DO_NOT_CLONE,
                getDefaultModel());
        // Act
        handler.execute(SUT, request, getCallback());

        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(ComponentState.VALID_UNCHANGED, onSuccessResponse.getState());
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
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_CLONED.getCreateDate());

        RecipeIdentityRequest request = getRequest(
                VALID_FROM_ANOTHER_USER.getId(),
                VALID_NEW_CLONED.getId(),
                getDefaultModel());
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        simulateGetValidFromAnotherUserFromDatabase();
        verify(repoMock).save(eq(VALID_NEW_CLONED));
        assertEquals(ComponentState.VALID_UNCHANGED, onSuccessResponse.getState());
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
                onSuccessResponse.getModel().getId(),
                DO_NOT_CLONE,
                new RecipeIdentityRequest.Model.Builder().
                        setTitle(onSuccessResponse.getModel().getTitle()).
                        setDescription(VALID_CLONED_DESCRIPTION_UPDATED.getDescription()).
                        build());
        // Act
        handler.execute(SUT, requestUpdateDescription, getCallback());
        // Assert
        verify(repoMock).save(VALID_CLONED_DESCRIPTION_UPDATED);
        assertEquals(ComponentState.VALID_CHANGED, onSuccessResponse.getState());
    }

    // region helper methods -----------------------------------------------------------------------
    private void givenNewEmptyModelSimulateNothingReturnedFromDatabase() {
        handler.execute(
                SUT,
                getRequest(INVALID_NEW_EMPTY.getId(), DO_NOT_CLONE, getDefaultModel()),
                getCallback());
        simulateNothingReturnedFromDatabase();
    }

    private RecipeIdentityRequest.Model getDefaultModel() {
        return RecipeIdentityRequest.Model.Builder.getDefault().build();
    }

    private RecipeIdentityRequest getRequest(String recipeId,
                                             String cloneToRecipeId,
                                             RecipeIdentityRequest.Model model) {
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
                RecipeIdentityTest.this.onSuccessResponse = response;

            }

            @Override
            public void onError(RecipeIdentityResponse response) {
                RecipeIdentityTest.this.onErrorResponse = response;
            }
        };
    }

    private void simulateNothingReturnedFromDatabase() {
        verify(repoMock).getById(eq(INVALID_NEW_EMPTY.getId()),
                repoCallback.capture());
        repoCallback.getValue().onDataNotAvailable();
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

    private void whenTimeProviderReturnTime(long time) {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}