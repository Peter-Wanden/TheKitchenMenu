package com.example.peter.thekitchenmenu.domain.usecase.recipeidentity;

import com.example.peter.thekitchenmenu.commonmocks.StringMaker;
import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.UseCase;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipestate.RecipeState;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeIdentityEntity;

import org.junit.*;
import org.mockito.*;

import static com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentity.DO_NOT_CLONE;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class RecipeIdentityMediatorTest {

    private static final String TAG = "tkm-" + RecipeIdentityMediatorTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    private static final RecipeIdentityEntity INVALID_NEW_EMPTY =
            TestDataRecipeIdentityEntity.getInvalidNewEmpty();
    private static final RecipeIdentityEntity VALID_EXISTING_COMPLETE =
            TestDataRecipeIdentityEntity.getValidExistingComplete();
    private static final RecipeIdentityEntity INVALID_NEW_TITLE_TOO_SHORT =
            TestDataRecipeIdentityEntity.getInvalidNewTitleTooShortDefaultDescription();
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    private UseCaseHandler handler;
    private RecipeIdentityResponse actualResponse;

    @Mock
    RepositoryRecipeIdentity repoIdentityMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeIdentityEntity>> repoCallback;
    @Mock
    TimeProvider timeProviderMock;

    private static final int SHORT_TEXT_MIN_LENGTH = 3;
    private static final int SHORT_TEXT_MAX_LENGTH = 70;
    private static final int LONG_TEXT_MIN_LENGTH = 0;
    private static final int LONG_TEXT_MAX_LENGTH = 500;

    public static final int TITLE_MIN_LENGTH = SHORT_TEXT_MIN_LENGTH;
    public static final int TITLE_MAX_LENGTH = SHORT_TEXT_MAX_LENGTH;
    public static final int DESCRIPTION_MIN_LENGTH = LONG_TEXT_MIN_LENGTH;
    public static final int DESCRIPTION_MAX_LENGTH = LONG_TEXT_MAX_LENGTH;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeIdentityMediator SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        handler = new UseCaseHandler(new UseCaseSchedulerMock());

        SUT = givenUseCase();
    }

    private RecipeIdentityMediator givenUseCase() {
        RecipeIdentity identity = new RecipeIdentity(
                repoIdentityMock,
                timeProviderMock
        );

        TextValidator textValidator = new TextValidator.Builder().
                setShortTextMinLength(SHORT_TEXT_MIN_LENGTH).
                setShortTextMaxLength(SHORT_TEXT_MAX_LENGTH).
                setLongTextMinLength(LONG_TEXT_MIN_LENGTH).
                setLongTextMaxLength(LONG_TEXT_MAX_LENGTH).
                build();

        return new RecipeIdentityMediator(
                handler,
                identity,
                textValidator
        );
    }

    @Test
    public void newId_emptyModel_stateDATA_UNAVAILABLE() {
        // Arrange
        // Act
        givenNewEmptyModelSimulateNothingReturnedFromDatabase();
        // Assert
        assertEquals(RecipeState.ComponentState.DATA_UNAVAILABLE, actualResponse.getState());
    }

    @Test
    public void newId_titleTooSHortValidDescription_stateINVALID_CHANGED() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        givenNewEmptyModelSimulateNothingReturnedFromDatabase();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(INVALID_NEW_EMPTY.getCreateDate());

        String invalidTitle = new StringMaker().
                makeStringOfExactLength(SHORT_TEXT_MIN_LENGTH).
                thenRemoveOneCharacter().
                build();

        String validDescription = new StringMaker().
                makeStringOfExactLength(LONG_TEXT_MAX_LENGTH).
                build();

        RecipeIdentityRequest.Model model = new RecipeIdentityRequest.Model.Builder().
                setTitle(invalidTitle).
                setDescription(validDescription).
                build();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, model),
                getCallback());

        // Assert
        assertEquals(RecipeState.ComponentState.DATA_UNAVAILABLE, actualResponse.getState());
        assertTrue(actualResponse.getFailReasons().contains(RecipeIdentity.FailReason.TITLE_TOO_SHORT));
        assertEquals(invalidTitle, actualResponse.getModel().getTitle());
        assertEquals(validDescription, actualResponse.getModel().getDescription());
    }

    @Test
    public void newId_invalidTitleNoDescription_stateINVALID_CHANGED() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        givenNewEmptyModelSimulateNothingReturnedFromDatabase();

        String invalidTitle = new StringMaker().
                makeStringOfExactLength(SHORT_TEXT_MIN_LENGTH).
                thenRemoveOneCharacter().
                build();

        RecipeIdentityRequest.Model model = RecipeIdentityRequest.Model.Builder.
                basedOn(actualResponse.getModel()).
                setTitle(invalidTitle).
                build();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, model),
                getCallback());

        // Assert
        assertEquals(RecipeState.ComponentState.DATA_UNAVAILABLE, actualResponse.getState());
        assertTrue(actualResponse.getFailReasons().contains(RecipeIdentity.FailReason.TITLE_TOO_SHORT));
    }

    @Test
    public void newId_validTitleNoDescription_stateVALID_CHANGED() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        whenTimeProviderReturnTime(INVALID_NEW_EMPTY.getCreateDate());
        givenNewEmptyModelSimulateNothingReturnedFromDatabase();

        String validTitle = new StringMaker().makeStringOfExactLength(SHORT_TEXT_MAX_LENGTH).build();
        RecipeIdentityRequest.Model model = RecipeIdentityRequest.Model.Builder.
                basedOn(actualResponse.getModel()).
                setTitle(validTitle).
                build();
        // Act
        handler.execute(SUT, getRequest(recipeId, DO_NOT_CLONE, model), getCallback());
        // Assert
        assertEquals(RecipeState.ComponentState.VALID_CHANGED, actualResponse.getState());
        assertTrue(actualResponse.getFailReasons().contains(RecipeIdentity.FailReason.NONE));
    }

    @Test
    public void newId_validTitleThenValidDescription_stateVALID_CHANGED() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        // Request/Response 1
        givenNewEmptyModelSimulateNothingReturnedFromDatabase();
        whenTimeProviderReturnTime(INVALID_NEW_EMPTY.getCreateDate());

        // Request/Response 2
        String validTitle = new StringMaker().makeStringOfExactLength(SHORT_TEXT_MAX_LENGTH).build();
        RecipeIdentityRequest.Model validTitleModel = RecipeIdentityRequest.Model.Builder.
                basedOn(actualResponse.getModel()).
                setTitle(validTitle).
                build();
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, validTitleModel),
                getCallback());

        // Request/Response 3
        String validDescription = new StringMaker().makeStringOfExactLength(LONG_TEXT_MAX_LENGTH).build();
        RecipeIdentityRequest.Model validTitleDescriptionModel = RecipeIdentityRequest.Model.Builder.
                basedOn(actualResponse.getModel()).
                setDescription(validDescription).
                build();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, validTitleDescriptionModel),
                getCallback());
        // Assert
        assertEquals(validTitle, actualResponse.getModel().getTitle());
        assertEquals(validDescription, actualResponse.getModel().getDescription());
        assertEquals(RecipeState.ComponentState.VALID_CHANGED, actualResponse.getState());
        assertEquals(1, actualResponse.getFailReasons().size());
        assertTrue(actualResponse.getFailReasons().contains(RecipeIdentity.FailReason.NONE));
    }

    @Test
    public void existingId_existingValidValuesLoaded_stateVALID_UNCHANGED() {
        // Arrange
        RecipeIdentityRequest request = getRequest(
                VALID_EXISTING_COMPLETE.getId(), DO_NOT_CLONE, getDefaultModel());
        // Act
        handler.execute(SUT, request, getCallback());

        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(RecipeState.ComponentState.VALID_UNCHANGED, actualResponse.getState());
        assertEquals(1, actualResponse.getFailReasons().size());
        assertTrue(actualResponse.getFailReasons().contains(RecipeIdentity.FailReason.NONE));
    }

    // existingId_titleTooShortNoDescription_stateINVALID_UNCHANGED
    // existingId_titleTooShortNoDescription_failReasonsTITLE_TOO_SHORT
    // existingId_titleTooLongNoDescription_stateINVALID_UNCHANGED
    // existingId_titleTooLongNoDescription_failReasonsTITLE_TOO_LONG

    // existingId_validTitleInvalidDescription_stateINVALID_UNCHANGED
    // existingId_validTitleInvalidDescription_failReasonsINVALID_DESCRIPTION
    // existingId_invalidTitleInvalidDescription_state_INVALID_UNCHANGED
    // existingId_invalidTitleInvalidDescription_failReasonsINVALID_TITLE_INVALID_DESCRIPTION
    // existingId_validTitleValidDescription_stateVAILD_UNCHANGED
    // existingId_validTitleValidDescription_failReasonsNONE


    // region helper methods -----------------------------------------------------------------------
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
                actualResponse = response;

            }

            @Override
            public void onError(RecipeIdentityResponse response) {
                actualResponse = response;
            }
        };
    }

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

    private void simulateNothingReturnedFromDatabase() {
        verify(repoIdentityMock).getById(eq(INVALID_NEW_EMPTY.getId()), repoCallback.capture());
        repoCallback.getValue().onDataNotAvailable();
    }

    private void simulateGetValidExistingCompleteFromDatabase() {
        verify(repoIdentityMock).getById(eq(VALID_EXISTING_COMPLETE.getId()), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE);
    }

    private void whenTimeProviderReturnTime(long time) {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}
