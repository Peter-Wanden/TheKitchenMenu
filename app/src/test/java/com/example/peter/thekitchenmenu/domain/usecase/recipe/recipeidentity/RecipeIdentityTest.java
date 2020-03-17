package com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeidentity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeidentity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeIdentityEntity;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;

import org.junit.*;
import org.mockito.*;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateCalculator.*;
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

//    private static final RecipeIdentityEntity INVALID_FROM_ANOTHER_USER_TITLE_TOO_LONG_DESCRIPTION_TOO_LONG =
//            TestDataRecipeIdentityEntity.getInvalidFromAnotherUser();
//    private static final RecipeIdentityEntity VALID_AFTER_INVALID_FROM_ANOTHER_USER =
//            TestDataRecipeIdentityEntity.getValidAfterInvalidClonedData();
//    private static final RecipeIdentityEntity VALID_COMPLETE_FROM_ANOTHER_USER =
//            TestDataRecipeIdentityEntity.getValidCompleteFromAnotherUser();
//    private static final RecipeIdentityEntity VALID_COMPLETE_AFTER_CLONED =
//            TestDataRecipeIdentityEntity.getValidCompleteAfterCloned();
//    private static final RecipeIdentityEntity VALID_COMPLETE_AFTER_CLONE_DESCRIPTION_UPDATED =
//            TestDataRecipeIdentityEntity.getValidClonedDescriptionUpdated();

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
    public void newId_emptyModel_stateINVALID_UNCHANGED_recipeStateListenerUpdated() {
        // Arrange
        // Act
        verifyIdentityDatabaseCalledWithNewIdAndReturnDataNotAvailable();
        // Assert
        assertEquals(ComponentState.INVALID_UNCHANGED, onErrorResponse.getMetadata().getState());
        assertTrue(onErrorResponse.getMetadata().
                getFailReasons().
                contains(CommonFailReason.DATA_UNAVAILABLE));
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
        verifyIdentityDatabaseCalledWithNewIdAndReturnDataNotAvailable();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, model),
                getCallback());
        // Assert
        assertEquals(ComponentState.INVALID_CHANGED, onErrorResponse.getMetadata().getState());
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
        verifyIdentityDatabaseCalledWithNewIdAndReturnDataNotAvailable();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, model),
                getCallback());
        // Assert
        assertTrue(onErrorResponse.
                getMetadata().
                getFailReasons().
                contains(RecipeIdentity.FailReason.TITLE_TOO_LONG));
        assertTrue(onErrorResponse.
                getMetadata().
                getFailReasons().
                contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG));
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
        verifyIdentityDatabaseCalledWithNewIdAndReturnDataNotAvailable();
        handler.execute(
                SUT,
                getRequest(recipeId, model),
                getCallback());

        // Assert
        assertEquals(ComponentState.INVALID_CHANGED, onErrorResponse.getMetadata().getState());
        System.out.println(TAG + onErrorResponse);

        assertTrue(onErrorResponse.
                getMetadata().
                getFailReasons().
                contains(RecipeIdentity.FailReason.TITLE_TOO_SHORT));
        assertEquals(invalidTitle, onErrorResponse.getModel().getTitle());
        assertEquals(validDescription, onErrorResponse.getModel().getDescription());
    }

    @Test
    public void newId_titleTooShortDescriptionDefault_stateINVALID_CHANGED() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        String invalidTitle = INVALID_NEW_TITLE_TOO_SHORT_DEFAULT_DESCRIPTION.getTitle();
        String description = INVALID_NEW_TITLE_TOO_SHORT_DEFAULT_DESCRIPTION.getDescription();

        verifyIdentityDatabaseCalledWithNewIdAndReturnDataNotAvailable();

        RecipeIdentityRequest.Model model = RecipeIdentityRequest.Model.Builder.
                basedOnIdentityResponseModel(onErrorResponse.getModel()).
                setTitle(invalidTitle).
                setDescription(description).
                build();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, model),
                getCallback());

        // Assert
        assertEquals(ComponentState.INVALID_CHANGED, onErrorResponse.getMetadata().getState());
        assertTrue(onErrorResponse.
                getMetadata().
                getFailReasons().
                contains(RecipeIdentity.FailReason.TITLE_TOO_SHORT));
    }

    @Test
    public void newId_titleValidDescriptionDefault_valuesPersisted() {
        // Arrange
        whenTimeProviderReturnTime(VALID_NEW_TITLE_VALID_DEFAULT_DESCRIPTION.getCreateDate());
        verifyIdentityDatabaseCalledWithNewIdAndReturnDataNotAvailable();

        String recipeId = VALID_NEW_TITLE_VALID_DEFAULT_DESCRIPTION.getId();
        String validTitle = VALID_NEW_TITLE_VALID_DEFAULT_DESCRIPTION.getTitle();

        RecipeIdentityRequest.Model validTitleModel = new RecipeIdentityRequest.Model.Builder().
                setTitle(validTitle).
                setDescription(onErrorResponse.getModel().getDescription()).
                build();

        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, validTitleModel),
                getCallback()
        );

        // Assert
        verify(repoIdentityMock).save(eq(VALID_NEW_TITLE_VALID_DEFAULT_DESCRIPTION));
        assertEquals(ComponentState.VALID_CHANGED, onSuccessResponse.getMetadata().getState());
    }

    @Test
    public void newId_titleValidDescriptionDefault_stateVALID_CHANGED() {
        // Arrange
        whenTimeProviderReturnTime(VALID_NEW_TITLE_VALID_DEFAULT_DESCRIPTION.getCreateDate());
        verifyIdentityDatabaseCalledWithNewIdAndReturnDataNotAvailable();

        String recipeId = VALID_NEW_TITLE_VALID_DEFAULT_DESCRIPTION.getId();
        String validTitle = VALID_NEW_TITLE_VALID_DEFAULT_DESCRIPTION.getTitle();

        RecipeIdentityRequest.Model model = RecipeIdentityRequest.Model.Builder.
                basedOnIdentityResponseModel(onErrorResponse.getModel()).
                setTitle(validTitle).
                build();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, model),
                getCallback());
        // Assert
        assertEquals(
                RecipeStateCalculator.ComponentState.VALID_CHANGED,
                onSuccessResponse.getMetadata().getState());
        assertTrue(
                onSuccessResponse.getMetadata().getFailReasons().contains(CommonFailReason.NONE));
    }

    @Test
    public void newId_titleValidDescriptionValid_valuesPersisted() {
        // Arrange
        whenTimeProviderReturnTime(VALID_NEW_COMPLETE.getCreateDate());
        verifyIdentityDatabaseCalledWithNewIdAndReturnDataNotAvailable();

        RecipeIdentityRequest.Model requestModel = new RecipeIdentityRequest.Model.Builder().
                setTitle(VALID_NEW_COMPLETE.getTitle()).
                setDescription(VALID_NEW_COMPLETE.getDescription()).
                build();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                setId(INVALID_NEW_EMPTY.getId()).
                setModel(requestModel).
                build();
        // Act
        handler.execute(SUT, request, getCallback());

        // Assert
        verify(repoIdentityMock).save(VALID_NEW_COMPLETE);
        assertEquals(ComponentState.VALID_CHANGED, onSuccessResponse.getMetadata().getState());
    }

    @Test
    public void newId_titleValidThenDescriptionValid_stateVALID_CHANGED() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        ArgumentCaptor<RecipeIdentityEntity> identityEntity = ArgumentCaptor.forClass(RecipeIdentityEntity.class);
        whenTimeProviderReturnTime(VALID_NEW_COMPLETE.getCreateDate());
        // RecipeRequestAbstract/Response 1
        verifyIdentityDatabaseCalledWithNewIdAndReturnDataNotAvailable();

        // RecipeRequestAbstract/Response 2
        String validTitle = VALID_NEW_COMPLETE.getTitle();
        RecipeIdentityRequest.Model validTitleModel = RecipeIdentityRequest.Model.Builder.
                basedOnIdentityResponseModel(onErrorResponse.getModel()).
                setTitle(validTitle).
                build();
        handler.execute(
                SUT,
                getRequest(recipeId, validTitleModel),
                getCallback());

        // RecipeRequestAbstract/Response 3
        String validDescription = VALID_NEW_COMPLETE.getDescription();
        RecipeIdentityRequest.Model validTitleDescriptionModel = RecipeIdentityRequest.Model.Builder.
                basedOnIdentityResponseModel(onSuccessResponse.getModel()).
                setDescription(validDescription).
                build();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, validTitleDescriptionModel),
                getCallback());
        // Assert save
        verify(repoIdentityMock, times((2))).save(identityEntity.capture());
        assertEquals(VALID_NEW_COMPLETE, identityEntity.getValue());
        // Assert values
        assertEquals(validTitle, onSuccessResponse.getModel().getTitle());
        assertEquals(validDescription, onSuccessResponse.getModel().getDescription());
        // Assert state
        assertEquals(
                RecipeStateCalculator.ComponentState.VALID_CHANGED,
                onSuccessResponse.getMetadata().getState());
        assertEquals(1, onSuccessResponse.getMetadata().getFailReasons().size());
        assertTrue(onSuccessResponse.
                getMetadata().
                getFailReasons().
                contains(CommonFailReason.NONE));
    }

    @Test
    public void existingId_titleValidDescriptionValid_VALID_UNCHANGED() {
        // Arrange
        RecipeIdentityRequest request = getRequest(
                VALID_EXISTING_COMPLETE.getId(),
                getDefaultModel());
        // Act
        handler.execute(SUT, request, getCallback());

        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(ComponentState.VALID_UNCHANGED, onSuccessResponse.getMetadata().getState());
    }

    @Test
    public void existingId_titleTooShortDescriptionDefault_stateINVALID_UNCHANGED() {
        // Arrange
        String recipeId = INVALID_EXISTING_TITLE_TOO_SHORT_DEFAULT_DESCRIPTION.getId();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, getDefaultModel()),
                getCallback());
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(INVALID_EXISTING_TITLE_TOO_SHORT_DEFAULT_DESCRIPTION);
        // Assert state
        assertEquals(RecipeStateCalculator.ComponentState.INVALID_UNCHANGED,
                onErrorResponse.getMetadata().getState());
    }

    @Test
    public void existingId_titleTooShortDescriptionDefault_failReasonsTITLE_TOO_SHORT() {
        // Arrange
        String recipeId = INVALID_EXISTING_TITLE_TOO_SHORT_DEFAULT_DESCRIPTION.getId();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, getDefaultModel()),
                getCallback());
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(INVALID_EXISTING_TITLE_TOO_SHORT_DEFAULT_DESCRIPTION);
        // Assert fail reasons
        assertEquals(1, onErrorResponse.getMetadata().getFailReasons().size());
        assertTrue(onErrorResponse.
                getMetadata().
                getFailReasons().
                contains(RecipeIdentity.FailReason.TITLE_TOO_SHORT));
    }

    @Test
    public void existingId_titleTooLongDescriptionDefault_stateINVALID_UNCHANGED() {
        // Arrange
        String recipeId = INVALID_EXISTING_TITLE_TOO_LONG_DEFAULT_DESCRIPTION.getId();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, getDefaultModel()),
                getCallback());
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(INVALID_EXISTING_TITLE_TOO_LONG_DEFAULT_DESCRIPTION);
        // assert state
        assertEquals(
                RecipeStateCalculator.ComponentState.INVALID_UNCHANGED,
                onErrorResponse.getMetadata().getState());
    }

    @Test
    public void existingId_titleValidDescriptionTooLong_stateINVALID_UNCHANGED() {
        // Arrange
        String recipeId = INVALID_EXISTING_TITLE_VALID_DESCRIPTION_TOO_LONG.getId();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, getDefaultModel()),
                getCallback()
        );
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(INVALID_EXISTING_TITLE_VALID_DESCRIPTION_TOO_LONG);

        assertEquals(
                RecipeStateCalculator.ComponentState.INVALID_UNCHANGED,
                onErrorResponse.getMetadata().getState());
        assertEquals(1, onErrorResponse.getMetadata().getFailReasons().size());
        assertTrue(onErrorResponse.
                getMetadata().
                getFailReasons().
                contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG));
    }

    @Test
    public void existingId_titleValidDescriptionTooLong_failReasonsDESCRIPTION_TOO_LONG() {
        // Arrange
        String recipeId = INVALID_EXISTING_TITLE_VALID_DESCRIPTION_TOO_LONG.getId();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, getDefaultModel()),
                getCallback()
        );
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(INVALID_EXISTING_TITLE_VALID_DESCRIPTION_TOO_LONG);

        assertEquals(1, onErrorResponse.getMetadata().getFailReasons().size());
        assertTrue(onErrorResponse.
                getMetadata().
                getFailReasons().
                contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG));
    }

    @Test
    public void existingId_titleTooShortDescriptionTooLong_stateINVALID_UNCHANGED() {
        // Arrange
        String recipeId = INVALID_EXISTING_TITLE_TOO_SHORT_DESCRIPTION_TOO_LONG.getId();
        // Act
        handler.execute(
                SUT,
                getRequest(recipeId, getDefaultModel()),
                getCallback());
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(INVALID_EXISTING_TITLE_TOO_SHORT_DESCRIPTION_TOO_LONG);

        assertEquals(
                RecipeStateCalculator.ComponentState.INVALID_UNCHANGED,
                onErrorResponse.getMetadata().getState());
    }

    @Test
    public void existingId_titleTooShortDescriptionTooLong_failReasonsTITLE_TOO_SHORT_DESCRIPTION_TOO_LONG() {
        // Arrange
        String recipeId = INVALID_EXISTING_TITLE_TOO_SHORT_DESCRIPTION_TOO_LONG.getId();
        // Act
        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                setId(recipeId).
                setModel(getDefaultModel()).
                build();

        handler.execute(SUT, request, getCallback());
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(INVALID_EXISTING_TITLE_TOO_SHORT_DESCRIPTION_TOO_LONG);

        assertEquals(2, onErrorResponse.getMetadata().getFailReasons().size());
        assertTrue(onErrorResponse.
                getMetadata().
                getFailReasons().
                contains(RecipeIdentity.FailReason.TITLE_TOO_SHORT));
        assertTrue(onErrorResponse.
                getMetadata().
                getFailReasons().
                contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG));
    }

    @Test
    public void existingId_titleTooLongDescriptionTooLong_stateINVALID_UNCHANGED() {
        // Arrange
        String recipeId = INVALID_EXISTING_TITLE_TOO_LONG_DESCRIPTION_TOO_LONG.getId();
        // Act
        handler.execute(SUT, getRequest(recipeId, getDefaultModel()), getCallback());
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(INVALID_EXISTING_TITLE_TOO_LONG_DESCRIPTION_TOO_LONG);
        // Assert state
        assertEquals(
                RecipeStateCalculator.ComponentState.INVALID_UNCHANGED,
                onErrorResponse.getMetadata().getState());
    }

    @Test
    public void existingId_titleTooLongDescriptionTooLong_failReasonsTITLE_TOO_LONG_DESCRIPTION_TOO_LONG() {
        // Arrange
        String recipeId = INVALID_EXISTING_TITLE_TOO_LONG_DESCRIPTION_TOO_LONG.getId();
        // Act
        handler.execute(SUT, getRequest(recipeId, getDefaultModel()), getCallback());
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(INVALID_EXISTING_TITLE_TOO_LONG_DESCRIPTION_TOO_LONG);
        // Assert fail reasons
        assertEquals(2, onErrorResponse.getMetadata().getFailReasons().size());
        assertTrue(onErrorResponse.
                getMetadata().
                getFailReasons().
                contains(RecipeIdentity.FailReason.TITLE_TOO_LONG));
        assertTrue(onErrorResponse.
                getMetadata().
                getFailReasons().
                contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG));
    }

    @Test
    public void existingId_titleValidDescriptionDefault_stateVAILD_UNCHANGED() {
        // Arrange
        String recipeId = VALID_EXISTING_TITLE_VALID_DESCRIPTION_DEFAULT.getId();
        // Act
        handler.execute(SUT, getRequest(recipeId, getDefaultModel()), getCallback());
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(VALID_EXISTING_TITLE_VALID_DESCRIPTION_DEFAULT);
        // Assert state
        assertEquals(
                RecipeStateCalculator.ComponentState.VALID_UNCHANGED,
                onSuccessResponse.getMetadata().getState());
    }

    @Test
    public void existingId_titleValidDescriptionDefault_failReasonsNONE() {
        // Arrange
        String recipeId = VALID_EXISTING_TITLE_VALID_DESCRIPTION_DEFAULT.getId();
        // Act
        handler.execute(SUT, getRequest(recipeId, getDefaultModel()), getCallback());
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(VALID_EXISTING_TITLE_VALID_DESCRIPTION_DEFAULT);
        // Assert fail reasons
        assertEquals(1, onSuccessResponse.getMetadata().getFailReasons().size());
        assertTrue(onSuccessResponse.
                getMetadata().
                getFailReasons().
                contains(CommonFailReason.NONE));
    }

    @Test
    public void existingId_titleValidDescriptionValid_stateVALID_UNCHANGED() {
        // Arrange
        String recipeId = VALID_EXISTING_TITLE_VALID_DESCRIPTION_VALID.getId();
        // Act
        handler.execute(SUT, getRequest(recipeId, getDefaultModel()), getCallback());

        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(VALID_EXISTING_TITLE_VALID_DESCRIPTION_VALID);
        // Assert state
        assertEquals(
                RecipeStateCalculator.ComponentState.VALID_UNCHANGED,
                onSuccessResponse.getMetadata().getState());
    }

    @Test
    public void existingId_titleValidDescriptionValid_failReasonsNONE() {
        // Arrange
        String recipeId = VALID_EXISTING_TITLE_VALID_DESCRIPTION_VALID.getId();
        // Act
        handler.execute(SUT, getRequest(recipeId, getDefaultModel()), getCallback());

        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(VALID_EXISTING_TITLE_VALID_DESCRIPTION_VALID);
        // Assert values
        // Assert state
        assertEquals(1, onSuccessResponse.getMetadata().getFailReasons().size());
        assertTrue(onSuccessResponse.
                getMetadata().
                getFailReasons().
                contains(CommonFailReason.NONE));
    }

    @Test
    public void existingId_titleTooLongDescriptionDefault_failReasonsTITLE_TOO_LONG() {
        // Arrange
        String recipeId = INVALID_EXISTING_TITLE_TOO_LONG_DEFAULT_DESCRIPTION.getId();
        // Act
        handler.execute(SUT, getRequest(recipeId, getDefaultModel()), getCallback());
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoCallback.capture());
        repoCallback.getValue().onEntityLoaded(INVALID_EXISTING_TITLE_TOO_LONG_DEFAULT_DESCRIPTION);
        // Assert fail reasons
        assertEquals(1, onErrorResponse.getMetadata().getFailReasons().size());
        assertTrue(onErrorResponse.
                getMetadata().
                getFailReasons().
                contains(RecipeIdentity.FailReason.TITLE_TOO_LONG));
    }

    // region helper methods -----------------------------------------------------------------------
    private void verifyIdentityDatabaseCalledWithNewIdAndReturnDataNotAvailable() {
        handler.execute(
                SUT,
                getRequest(INVALID_NEW_EMPTY.getId(), getDefaultModel()),
                getCallback());
        simulateNothingReturnedFromDatabase();
    }

    private RecipeIdentityRequest.Model getDefaultModel() {
        return new RecipeIdentityRequest.Model.Builder().getDefault().build();
    }

    private RecipeIdentityRequest getRequest(String recipeId,
                                             RecipeIdentityRequest.Model model) {
        return new RecipeIdentityRequest.Builder().
                setId(recipeId).
                setModel(model).
                build();
    }

    private UseCase.Callback<RecipeIdentityResponse> getCallback() {
        return new UseCase.Callback<RecipeIdentityResponse>() {

            @Override
            public void onSuccess(RecipeIdentityResponse response) {
                onSuccessResponse = response;
            }

            @Override
            public void onError(RecipeIdentityResponse response) {
                onErrorResponse = response;
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