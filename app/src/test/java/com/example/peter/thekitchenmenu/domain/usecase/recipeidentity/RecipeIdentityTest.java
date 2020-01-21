package com.example.peter.thekitchenmenu.domain.usecase.recipeidentity;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeState;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeIdentityEntity;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;

import org.junit.*;
import org.mockito.*;

import static com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentity.DO_NOT_CLONE;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeState.*;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

public class RecipeIdentityTest {

    private static final String TAG = "tkm-" + RecipeIdentityTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    private static final RecipeIdentityEntity INVALID_NEW_EMPTY =
            TestDataRecipeIdentityEntity.getInvalidNewEmpty();
    private static final RecipeIdentityEntity INVALID_NEW_TITLE_TOO_LONG_DESCRIPTION_TOO_LONG =
            TestDataRecipeIdentityEntity.getInvalidNewTitleTooLongDescriptionTooLong();
    private static final RecipeIdentityEntity INVALID_NEW_TITLE_TOO_SHORT_DEFAULT_DESCRIPTION =
            TestDataRecipeIdentityEntity.getInvalidNewTitleTooShortDescriptionDefault();
    private static final RecipeIdentityEntity INVALID_NEW_TITLE_TOO_SHORT_VALID_DESCRIPTION =
            TestDataRecipeIdentityEntity.getInvalidNewTitleTooShortDescriptionValid();

    private static final RecipeIdentityEntity VALID_NEW_TITLE_VALID_DEFAULT_DESCRIPTION =
            TestDataRecipeIdentityEntity.getValidNewTitleValidDescriptionDefault();
    private static final RecipeIdentityEntity VALID_NEW_COMPLETE =
            TestDataRecipeIdentityEntity.getValidNewComplete();

    private static final RecipeIdentityEntity INVALID_EXISTING_TITLE_TOO_SHORT_DEFAULT_DESCRIPTION =
            TestDataRecipeIdentityEntity.getInvalidExistingTitleTooShortDefaultDescription();
    private static final RecipeIdentityEntity INVALID_EXISTING_TITLE_TOO_LONG_DEFAULT_DESCRIPTION =
            TestDataRecipeIdentityEntity.getInvalidExistingTitleTooLongDefaultDescription();
    private static final RecipeIdentityEntity INVALID_EXISTING_TITLE_VALID_DESCRIPTION_TOO_LONG =
            TestDataRecipeIdentityEntity.getInvalidExistingTitleValidDescriptionTooLong();
    private static final RecipeIdentityEntity INVALID_EXISTING_TITLE_TOO_SHORT_DESCRIPTION_TOO_LONG =
            TestDataRecipeIdentityEntity.getInvalidExistingTitleTooShortDescriptionTooLong();
    private static final RecipeIdentityEntity INVALID_EXISTING_TITLE_TOO_LONG_DESCRIPTION_TOO_LONG =
            TestDataRecipeIdentityEntity.getInvalidExistingTitleTooLongDescriptionTooLong();
    private static final RecipeIdentityEntity VALID_EXISTING_TITLE_VALID_DESCRIPTION_DEFAULT =
            TestDataRecipeIdentityEntity.getValidExistingTitleValidDescriptionDefault();
    private static final RecipeIdentityEntity VALID_EXISTING_TITLE_VALID_DESCRIPTION_VALID =
            TestDataRecipeIdentityEntity.getValidExistingTitleValidDescriptionValid();

    private static final RecipeIdentityEntity VALID_EXISTING_COMPLETE =
            TestDataRecipeIdentityEntity.getValidExistingTitleValidDescriptionValid();

    private static final RecipeIdentityEntity INVALID_FROM_ANOTHER_USER_TITLE_TOO_LONG_DESCRIPTION_TOO_LONG =
            TestDataRecipeIdentityEntity.getInvalidFromAnotherUser();
    private static final RecipeIdentityEntity VALID_AFTER_INVALID_FROM_ANOTHER_USER =
            TestDataRecipeIdentityEntity.getValidAfterInvalidClonedData();
    private static final RecipeIdentityEntity VALID_COMPLETE_FROM_ANOTHER_USER =
            TestDataRecipeIdentityEntity.getValidCompleteFromAnotherUser();
    private static final RecipeIdentityEntity VALID_COMPLETE_AFTER_CLONED =
            TestDataRecipeIdentityEntity.getValidCompleteAfterCloned();
    private static final RecipeIdentityEntity VALID_COMPLETE_AFTER_CLONE_DESCRIPTION_UPDATED =
            TestDataRecipeIdentityEntity.getValidClonedDescriptionUpdated();

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RepositoryRecipeIdentity repoIdentityMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeIdentityEntity>> repoCallback;
    @Mock
    TimeProvider timeProviderMock;

    private UseCaseHandler handler;
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
                repoIdentityMock,
                timeProviderMock,
                handler,
                textValidator);
    }

    @Test
    public void newId_emptyModel_stateDATA_UNAVAILABLE() {
        // Arrange
        // Act
        givenNewEmptyModelSimulateNothingReturnedFromDatabase();
        // Assert
        assertEquals(ComponentState.DATA_UNAVAILABLE, onErrorResponse.getState());
    }

    @Test
    public void newId_titleTooLongDescriptionTooLong_stateINVALID_CHANGED() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        String titleTooLong = INVALID_NEW_TITLE_TOO_LONG_DESCRIPTION_TOO_LONG.getTitle();
        String descriptionTooLong = INVALID_NEW_TITLE_TOO_LONG_DESCRIPTION_TOO_LONG.getDescription();

        RecipeIdentityRequest.Model model = new RecipeIdentityRequest.Model.Builder().
                setTitle(titleTooLong).
                setDescription(descriptionTooLong).
                build();
        givenNewEmptyModelSimulateNothingReturnedFromDatabase();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, model),
                getCallback());
        // Assert
        assertEquals(ComponentState.INVALID_CHANGED, onErrorResponse.getState());
    }

    @Test
    public void newId_titleTooLongDescriptionTooLong_failReasonsTITLE_TOO_LONG_DESCRIPTION_TOO_LONG() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        String title = INVALID_NEW_TITLE_TOO_LONG_DESCRIPTION_TOO_LONG.getTitle();
        String description = INVALID_NEW_TITLE_TOO_LONG_DESCRIPTION_TOO_LONG.getDescription();

        RecipeIdentityRequest.Model model = new RecipeIdentityRequest.Model.Builder().
                setTitle(title).
                setDescription(description).
                build();
        givenNewEmptyModelSimulateNothingReturnedFromDatabase();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, model),
                getCallback());
        // Assert
        assertTrue(onErrorResponse.getFailReasons().contains(RecipeIdentity.FailReason.TITLE_TOO_LONG));
        assertTrue(onErrorResponse.getFailReasons().contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG));
    }

    @Test
    public void newId_titleTooShortDescriptionValid_stateINVALID_CHANGED() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        String invalidTitle = INVALID_NEW_TITLE_TOO_SHORT_VALID_DESCRIPTION.getTitle();
        String validDescription = INVALID_NEW_TITLE_TOO_SHORT_VALID_DESCRIPTION.getDescription();

        RecipeIdentityRequest.Model model = new RecipeIdentityRequest.Model.Builder().
                setTitle(invalidTitle).
                setDescription(validDescription).
                build();

        // Act
        givenNewEmptyModelSimulateNothingReturnedFromDatabase();
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, model),
                getCallback());

        // Assert
        assertEquals(ComponentState.INVALID_CHANGED, onErrorResponse.getState());
        System.out.println(TAG + onErrorResponse);

        assertTrue(onErrorResponse.getFailReasons().contains(RecipeIdentity.FailReason.TITLE_TOO_SHORT));
        assertEquals(invalidTitle, onErrorResponse.getModel().getTitle());
        assertEquals(validDescription, onErrorResponse.getModel().getDescription());
    }

    @Test
    public void newId_titleTooShortDescriptionDefault_stateINVALID_CHANGED() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        String invalidTitle = INVALID_NEW_TITLE_TOO_SHORT_DEFAULT_DESCRIPTION.getTitle();
        String description = INVALID_NEW_TITLE_TOO_SHORT_DEFAULT_DESCRIPTION.getDescription();

        givenNewEmptyModelSimulateNothingReturnedFromDatabase();

        RecipeIdentityRequest.Model model = RecipeIdentityRequest.Model.Builder.
                basedOn(onErrorResponse.getModel()).
                setTitle(invalidTitle).
                setDescription(description).
                build();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, model),
                getCallback());

        // Assert
        assertEquals(ComponentState.INVALID_CHANGED, onErrorResponse.getState());
        assertTrue(onErrorResponse.getFailReasons().contains(RecipeIdentity.FailReason.TITLE_TOO_SHORT));
    }

    @Test
    public void newId_titleValidDescriptionDefault_valuesPersisted() {
        // Arrange
        whenTimeProviderReturnTime(VALID_NEW_TITLE_VALID_DEFAULT_DESCRIPTION.getCreateDate());
        givenNewEmptyModelSimulateNothingReturnedFromDatabase();

        String recipeId = VALID_NEW_TITLE_VALID_DEFAULT_DESCRIPTION.getId();
        String validTitle = VALID_NEW_TITLE_VALID_DEFAULT_DESCRIPTION.getTitle();

        RecipeIdentityRequest.Model validTitleModel = new RecipeIdentityRequest.Model.Builder().
                setTitle(validTitle).
                setDescription(onErrorResponse.getModel().getDescription()).
                build();

        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, validTitleModel),
                getCallback()
        );

        // Assert
        verify(repoIdentityMock).save(eq(VALID_NEW_TITLE_VALID_DEFAULT_DESCRIPTION));
        assertEquals(ComponentState.VALID_CHANGED, onSuccessResponse.getState());
    }

    @Test
    public void newId_titleValidDescriptionDefault_stateVALID_CHANGED() {
        // Arrange
        whenTimeProviderReturnTime(VALID_NEW_TITLE_VALID_DEFAULT_DESCRIPTION.getCreateDate());
        givenNewEmptyModelSimulateNothingReturnedFromDatabase();

        String recipeId = VALID_NEW_TITLE_VALID_DEFAULT_DESCRIPTION.getId();
        String validTitle = VALID_NEW_TITLE_VALID_DEFAULT_DESCRIPTION.getTitle();

        RecipeIdentityRequest.Model model = RecipeIdentityRequest.Model.Builder.
                basedOn(onErrorResponse.getModel()).
                setTitle(validTitle).
                build();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, model),
                getCallback());
        // Assert
        assertEquals(RecipeState.ComponentState.VALID_CHANGED, onSuccessResponse.getState());
        assertTrue(onSuccessResponse.getFailReasons().contains(RecipeIdentity.FailReason.NONE));
    }

    @Test
    public void newId_titleValidDescriptionValid_valuesPersisted() {
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
        verify(repoIdentityMock).save(VALID_NEW_COMPLETE);
        assertEquals(ComponentState.VALID_CHANGED, onSuccessResponse.getState());
    }

    @Test
    public void newId_titleValidThenDescriptionValid_stateVALID_CHANGED() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        ArgumentCaptor<RecipeIdentityEntity> identityEntity = ArgumentCaptor.forClass(RecipeIdentityEntity.class);
        whenTimeProviderReturnTime(VALID_NEW_COMPLETE.getCreateDate());
        // Request/Response 1
        givenNewEmptyModelSimulateNothingReturnedFromDatabase();

        // Request/Response 2
        String validTitle = VALID_NEW_COMPLETE.getTitle();
        RecipeIdentityRequest.Model validTitleModel = RecipeIdentityRequest.Model.Builder.
                basedOn(onErrorResponse.getModel()).
                setTitle(validTitle).
                build();
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, validTitleModel),
                getCallback());

        // Request/Response 3
        String validDescription = VALID_NEW_COMPLETE.getDescription();
        RecipeIdentityRequest.Model validTitleDescriptionModel = RecipeIdentityRequest.Model.Builder.
                basedOn(onSuccessResponse.getModel()).
                setDescription(validDescription).
                build();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, validTitleDescriptionModel),
                getCallback());
        // Assert save
        verify(repoIdentityMock, times((2))).save(identityEntity.capture());
        assertEquals(VALID_NEW_COMPLETE, identityEntity.getValue());
        // Assert values
        assertEquals(validTitle, onSuccessResponse.getModel().getTitle());
        assertEquals(validDescription, onSuccessResponse.getModel().getDescription());
        // Assert state
        assertEquals(RecipeState.ComponentState.VALID_CHANGED, onSuccessResponse.getState());
        assertEquals(1, onSuccessResponse.getFailReasons().size());
        assertTrue(onSuccessResponse.getFailReasons().contains(RecipeIdentity.FailReason.NONE));
    }

    @Test
    public void existingId_titleValidDescriptionValid_VALID_UNCHANGED() {
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
    public void existingId_titleTooShortDescriptionDefault_stateINVALID_UNCHANGED() {
        // Arrange
        String recipeId = INVALID_EXISTING_TITLE_TOO_SHORT_DEFAULT_DESCRIPTION.getId();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, getDefaultModel()),
                getCallback());
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(INVALID_EXISTING_TITLE_TOO_SHORT_DEFAULT_DESCRIPTION);
        // Assert state
        assertEquals(RecipeState.ComponentState.INVALID_UNCHANGED, onErrorResponse.getState());
    }

    @Test
    public void existingId_titleTooShortDescriptionDefault_failReasonsTITLE_TOO_SHORT() {
        // Arrange
        String recipeId = INVALID_EXISTING_TITLE_TOO_SHORT_DEFAULT_DESCRIPTION.getId();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, getDefaultModel()),
                getCallback());
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(INVALID_EXISTING_TITLE_TOO_SHORT_DEFAULT_DESCRIPTION);
        // Assert fail reasons
        assertEquals(1, onErrorResponse.getFailReasons().size());
        assertTrue(onErrorResponse.getFailReasons().contains(RecipeIdentity.FailReason.TITLE_TOO_SHORT));
    }

    @Test
    public void existingId_titleTooLongDescriptionDefault_stateINVALID_UNCHANGED() {
        // Arrange
        String recipeId = INVALID_EXISTING_TITLE_TOO_LONG_DEFAULT_DESCRIPTION.getId();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, getDefaultModel()),
                getCallback());
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(INVALID_EXISTING_TITLE_TOO_LONG_DEFAULT_DESCRIPTION);
        // assert state
        assertEquals(RecipeState.ComponentState.INVALID_UNCHANGED, onErrorResponse.getState());
    }

    @Test
    public void existingId_titleValidDescriptionTooLong_stateINVALID_UNCHANGED() {
        // Arrange
        String recipeId = INVALID_EXISTING_TITLE_VALID_DESCRIPTION_TOO_LONG.getId();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, getDefaultModel()),
                getCallback()
        );
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(INVALID_EXISTING_TITLE_VALID_DESCRIPTION_TOO_LONG);

        assertEquals(RecipeState.ComponentState.INVALID_UNCHANGED, onErrorResponse.getState());
        assertEquals(1, onErrorResponse.getFailReasons().size());
        assertTrue(onErrorResponse.getFailReasons().contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG));
    }

    @Test
    public void existingId_titleValidDescriptionTooLong_failReasonsDESCRIPTION_TOO_LONG() {
        // Arrange
        String recipeId = INVALID_EXISTING_TITLE_VALID_DESCRIPTION_TOO_LONG.getId();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, getDefaultModel()),
                getCallback()
        );
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(INVALID_EXISTING_TITLE_VALID_DESCRIPTION_TOO_LONG);

        assertEquals(1, onErrorResponse.getFailReasons().size());
        assertTrue(onErrorResponse.getFailReasons().contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG));
    }

    @Test
    public void existingId_titleTooShortDescriptionTooLong_stateINVALID_UNCHANGED() {
        // Arrange
        String recipeId = INVALID_EXISTING_TITLE_TOO_SHORT_DESCRIPTION_TOO_LONG.getId();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, getDefaultModel()),
                getCallback());
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(INVALID_EXISTING_TITLE_TOO_SHORT_DESCRIPTION_TOO_LONG);

        assertEquals(RecipeState.ComponentState.INVALID_UNCHANGED, onErrorResponse.getState());
    }

    @Test
    public void existingId_titleTooShortDescriptionTooLong_failReasonsTITLE_TOO_SHORT_DESCRIPTION_TOO_LONG() {
        // Arrange
        String recipeId = INVALID_EXISTING_TITLE_TOO_SHORT_DESCRIPTION_TOO_LONG.getId();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, getDefaultModel()),
                getCallback());
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(INVALID_EXISTING_TITLE_TOO_SHORT_DESCRIPTION_TOO_LONG);

        assertEquals(2, onErrorResponse.getFailReasons().size());
        assertTrue(onErrorResponse.getFailReasons().contains(RecipeIdentity.FailReason.TITLE_TOO_SHORT));
        assertTrue(onErrorResponse.getFailReasons().contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG));
    }

    @Test
    public void existingId_titleTooLongDescriptionTooLong_stateINVALID_UNCHANGED() {
        // Arrange
        String recipeId = INVALID_EXISTING_TITLE_TOO_LONG_DESCRIPTION_TOO_LONG.getId();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, getDefaultModel()),
                getCallback());
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(INVALID_EXISTING_TITLE_TOO_LONG_DESCRIPTION_TOO_LONG);
        // Assert state
        assertEquals(RecipeState.ComponentState.INVALID_UNCHANGED, onErrorResponse.getState());
    }

    @Test
    public void existingId_titleTooLongDescriptionTooLong_failReasonsTITLE_TOO_LONG_DESCRIPTION_TOO_LONG() {
        // Arrange
        String recipeId = INVALID_EXISTING_TITLE_TOO_LONG_DESCRIPTION_TOO_LONG.getId();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, getDefaultModel()),
                getCallback());
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(INVALID_EXISTING_TITLE_TOO_LONG_DESCRIPTION_TOO_LONG);
        // Assert fail reasons
        assertEquals(2, onErrorResponse.getFailReasons().size());
        assertTrue(onErrorResponse.getFailReasons().contains(RecipeIdentity.FailReason.TITLE_TOO_LONG));
        assertTrue(onErrorResponse.getFailReasons().contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG));
    }

    @Test
    public void existingId_titleValidDescriptionDefault_stateVAILD_UNCHANGED() {
        // Arrange
        String recipeId = VALID_EXISTING_TITLE_VALID_DESCRIPTION_DEFAULT.getId();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, getDefaultModel()),
                getCallback());
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(VALID_EXISTING_TITLE_VALID_DESCRIPTION_DEFAULT);
        // Assert state
        assertEquals(RecipeState.ComponentState.VALID_UNCHANGED, onSuccessResponse.getState());
    }

    @Test
    public void existingId_titleValidDescriptionDefault_failReasonsNONE() {
        // Arrange
        String recipeId = VALID_EXISTING_TITLE_VALID_DESCRIPTION_DEFAULT.getId();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, getDefaultModel()),
                getCallback());
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(VALID_EXISTING_TITLE_VALID_DESCRIPTION_DEFAULT);
        // Assert fail reasons
        assertEquals(1, onSuccessResponse.getFailReasons().size());
        assertTrue(onSuccessResponse.getFailReasons().contains(RecipeIdentity.FailReason.NONE));
    }

    @Test
    public void existingId_titleValidDescriptionValid_stateVALID_UNCHANGED() {
        // Arrange
        String recipeId = VALID_EXISTING_TITLE_VALID_DESCRIPTION_VALID.getId();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, getDefaultModel()),
                getCallback());

        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(VALID_EXISTING_TITLE_VALID_DESCRIPTION_VALID);
        // Assert state
        assertEquals(RecipeState.ComponentState.VALID_UNCHANGED, onSuccessResponse.getState());
    }

    @Test
    public void existingId_titleValidDescriptionValid_failReasonsNONE() {
        // Arrange
        String recipeId = VALID_EXISTING_TITLE_VALID_DESCRIPTION_VALID.getId();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, getDefaultModel()),
                getCallback());

        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(VALID_EXISTING_TITLE_VALID_DESCRIPTION_VALID);
        // Assert values
        // Assert state
        assertEquals(1, onSuccessResponse.getFailReasons().size());
        assertTrue(onSuccessResponse.getFailReasons().contains(RecipeIdentity.FailReason.NONE));
    }

    @Test
    public void existingId_titleTooLongDescriptionDefault_failReasonsTITLE_TOO_LONG() {
        // Arrange
        String recipeId = INVALID_EXISTING_TITLE_TOO_LONG_DEFAULT_DESCRIPTION.getId();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, DO_NOT_CLONE, getDefaultModel()),
                getCallback());
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(INVALID_EXISTING_TITLE_TOO_LONG_DEFAULT_DESCRIPTION);
        // Assert fail reasons
        assertEquals(1, onErrorResponse.getFailReasons().size());
        assertTrue(onErrorResponse.getFailReasons().contains(RecipeIdentity.FailReason.TITLE_TOO_LONG));
    }

    @Test
    public void existingIdCloneToId_dataClonedAndSavedToCloneToId() {
        // Arrange
        whenTimeProviderReturnTime(VALID_NEW_COMPLETE.getCreateDate());
        String cloneFromRecipeId = VALID_COMPLETE_FROM_ANOTHER_USER.getId();
        String cloneToRecipeId = VALID_COMPLETE_AFTER_CLONED.getId();
        // Act
        handler.execute(
                SUT,
                getRequest(cloneFromRecipeId, cloneToRecipeId, getDefaultModel()),
                getCallback()
        );
        // Assert repo called with correct id
        verify(repoIdentityMock).getById(eq(cloneFromRecipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(VALID_COMPLETE_FROM_ANOTHER_USER);
        // Assert data cloned and saved with clone to id
        verify(repoIdentityMock).save(eq(VALID_COMPLETE_AFTER_CLONED));
    }

    @Test
    public void existingIdCloneToId_descriptionUpdatedAfterClone_updatesSavedToCloneToId() {
        // Arrange, 1st request, clone data
        whenTimeProviderReturnTime(VALID_NEW_COMPLETE.getCreateDate());
        String cloneFromRecipeId = VALID_COMPLETE_FROM_ANOTHER_USER.getId();
        String cloneToRecipeId = VALID_COMPLETE_AFTER_CLONED.getId();
        // Act
        handler.execute(
                SUT,
                getRequest(cloneFromRecipeId, cloneToRecipeId, getDefaultModel()),
                getCallback()
        );
        // Assert repo called with correct id
        verify(repoIdentityMock).getById(eq(cloneFromRecipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(VALID_COMPLETE_FROM_ANOTHER_USER);

        // Arrange, 2nd request, update description
        String updatedDescription = VALID_COMPLETE_AFTER_CLONE_DESCRIPTION_UPDATED.getDescription();
        RecipeIdentityRequest.Model modelWithUpdatedDescription = RecipeIdentityRequest.Model.Builder.
                basedOn(onSuccessResponse.getModel()).
                setDescription(updatedDescription).
                build();

        // Act
        handler.execute(
                SUT,
                getRequest(cloneToRecipeId, DO_NOT_CLONE, modelWithUpdatedDescription),
                getCallback()
        );
        // Assert
        verify(repoIdentityMock).save(eq(VALID_COMPLETE_AFTER_CLONE_DESCRIPTION_UPDATED));
    }

    @Test
    public void existingIdCloneToId_titleTooLongDescriptionTooLongInClone_failReasonTITLE_TOO_LONG_DESCRIPTION_TOO_LONG() {
        // Arrange
        // Arrange, 1st request, clone data
        whenTimeProviderReturnTime(VALID_NEW_COMPLETE.getCreateDate());
        String cloneFromRecipeId = INVALID_FROM_ANOTHER_USER_TITLE_TOO_LONG_DESCRIPTION_TOO_LONG.getId();
        String cloneToRecipeId = VALID_AFTER_INVALID_FROM_ANOTHER_USER.getId();
        // Act
        handler.execute(
                SUT,
                getRequest(cloneFromRecipeId, cloneToRecipeId, getDefaultModel()),
                getCallback()
        );
        // Assert
        verify(repoIdentityMock).getById(eq(cloneFromRecipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(INVALID_FROM_ANOTHER_USER_TITLE_TOO_LONG_DESCRIPTION_TOO_LONG);

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
        verify(repoIdentityMock).getById(eq(INVALID_NEW_EMPTY.getId()),
                repoCallback.capture());
        repoCallback.getValue().onDataNotAvailable();
    }

    private void simulateGetValidExistingCompleteFromDatabase() {
        verify(repoIdentityMock).getById(eq(VALID_EXISTING_COMPLETE.getId()),
                repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE);
    }

    private void whenTimeProviderReturnTime(long time) {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}