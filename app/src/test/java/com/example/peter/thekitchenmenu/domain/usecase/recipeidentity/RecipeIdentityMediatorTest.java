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

    // region constants ----------------------------------------------------------------------------
    private static final RecipeIdentityEntity INVALID_NEW_EMPTY =
            TestDataRecipeIdentityEntity.getInvalidNewEmpty();
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

    private int shortTextMinLength = 3;
    private int shortTextMaxLength = 70;
    private int longTextMinLength = 0;
    private int longTextMaxLength = 500;
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
                setShortTextMinLength(shortTextMinLength).
                setShortTextMaxLength(shortTextMaxLength).
                setLongTextMinLength(longTextMinLength).
                setLongTextMaxLength(longTextMaxLength).
                build();

        return new RecipeIdentityMediator(
                handler,
                identity,
                textValidator
        );
    }

    @Test
    public void newRecipeId_emptyModel_stateDATA_UNAVAILABLE() {
        // Arrange
        // Act
        givenNewEmptyModelSimulateNothingReturnedFromDatabase();
        // Assert
        assertEquals(RecipeState.ComponentState.DATA_UNAVAILABLE, actualResponse.getState());
    }

    @Test
    public void newId_invalidTitleValidDescription_stateINVALID_CHANGED() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        givenNewEmptyModelSimulateNothingReturnedFromDatabase();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(INVALID_NEW_EMPTY.getCreateDate());

        String invalidTitle = new StringMaker().
                makeStringOfExactLength(shortTextMinLength).
                thenRemoveOneCharacter().
                build();

        String validDescription = new StringMaker().
                makeStringOfExactLength(longTextMaxLength).
                build();

        RecipeIdentityModel model = RecipeIdentityModel.Builder.
                basedOn(actualResponse.getModel()).
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
        assertTrue(actualResponse.getFailReasons().contains(RecipeIdentity.FailReason.DESCRIPTION_OK));
    }

    @Test
    public void newId_validTitleNoDescription_stateVALID_CHANGED() {
        // Arrange
        String recipeId = INVALID_NEW_EMPTY.getId();
        givenNewEmptyModelSimulateNothingReturnedFromDatabase();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(INVALID_NEW_EMPTY.getCreateDate());

        String validTitle = new StringMaker().makeStringOfExactLength(shortTextMaxLength).build();
        RecipeIdentityModel model = RecipeIdentityModel.Builder.
                basedOn(actualResponse.getModel()).
                setTitle(validTitle).
                build();
        // Act
        handler.execute(SUT, getRequest(recipeId, DO_NOT_CLONE, model), getCallback());
        // Assert
        assertEquals(RecipeState.ComponentState.VALID_CHANGED, actualResponse.getState());
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
                actualResponse = response;

            }

            @Override
            public void onError(RecipeIdentityResponse response) {
                actualResponse = response;
            }
        };
    }

    private void simulateNothingReturnedFromDatabase() {
        verify(repoIdentityMock).getById(eq(INVALID_NEW_EMPTY.getId()), repoCallback.capture());
        repoCallback.getValue().onDataNotAvailable();
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}
