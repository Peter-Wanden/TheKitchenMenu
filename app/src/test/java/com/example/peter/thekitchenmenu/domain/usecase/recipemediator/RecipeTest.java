package com.example.peter.thekitchenmenu.domain.usecase.recipemediator;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.RecipeResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityTest;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeDurationEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeIdentityEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipePortionsEntity;

import org.junit.*;
import org.mockito.*;

import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.Recipe.DO_NOT_CLONE;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipeTest {

    private static final String TAG = "tkm-" + RecipeTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    private static final RecipeEntity INVALID_NEW =
            TestDataRecipeEntity.getNewInvalid();

    private static final RecipeIdentityEntity IDENTITY_INVALID_NEW_EMPTY =
            TestDataRecipeIdentityEntity.getInvalidNewEmpty();
    private static final RecipeIdentityEntity IDENTITY_VALID_NEW_COMPLETE =
            TestDataRecipeIdentityEntity.getValidNewComplete();

    private static final RecipeEntity VALID_EXISTING_RECIPE =
            TestDataRecipeEntity.getValidExisting();
    private static final RecipeDurationEntity VALID_EXISTING_COMPLETE_DURATION =
            TestDataRecipeDurationEntity.getValidExistingComplete();
    private static final RecipeIdentityEntity VALID_EXISTING_COMPLETE_IDENTITY =
            TestDataRecipeIdentityEntity.getValidExistingTitleValidDescriptionValid();
    private static final RecipePortionsEntity VALID_EXISTING_PORTIONS =
            TestDataRecipePortionsEntity.getExistingValidNinePortions();

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RepositoryRecipeIdentity repoIdentityMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeIdentityEntity>> repoIdentityCallback;
    @Mock
    TimeProvider timeProviderMock;
    @Captor
    ArgumentCaptor<RecipeStateResponse> stateResponseCaptor;

    private RecipeIdentityResponse onIdentitySuccessResponse;
    private RecipeIdentityResponse onIdentityErrorResponse;
    private RecipeResponse onRecipeSuccessResponse;
    private RecipeResponse onRecipeErrorResponse;

    private RecipeIdentity recipeIdentity;
    private UseCaseHandler handler;

    @Mock
    private RecipeClient recipeClientListener1;
    @Mock
    private RecipeClient recipeStateListener2;
    // endregion helper fields ---------------------------------------------------------------------

    private Recipe SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();
    }

    private Recipe givenUseCase() {
        TextValidator textValidator = new TextValidator.Builder().
                setShortTextMinLength(RecipeIdentityTest.TITLE_MIN_LENGTH).
                setShortTextMaxLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                setLongTextMinLength(RecipeIdentityTest.DESCRIPTION_MIN_LENGTH).
                setLongTextMaxLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                build();

        UseCaseHandler handler = new UseCaseHandler(new UseCaseSchedulerMock());
        this.handler = handler;

        recipeIdentity = new RecipeIdentity(
                repoIdentityMock,
                timeProviderMock,
                handler,
                textValidator
        );

        RecipeStateCalculator stateCalculator = new RecipeStateCalculator();

        return new Recipe(handler, stateCalculator, recipeIdentity);
    }

    @Test
    public void recipeRequestNewId_invokerIssuesCommandToAllReceivers() {
        // Arrange
        String recipeId = INVALID_NEW.getId();
        // Act
        SUT.registerClientListener(recipeClientListener1);
        RecipeRequest request = new RecipeRequest(
                recipeId
        );
        handler.execute(SUT, request, getCallback());
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onDataNotAvailable();
        // Assert database call, return data not available
        verifyIdentityDatabaseCalledWithNewIdAndReturnDataUnavailable();
    }

    @Test
    public void recipeRequestNewId_invokerIssuesCommandToAllReceivers_listenersUpdatedWithCorrectRecipeStateValues() {
        // Arrange
        String recipeId = INVALID_NEW.getId();
        // Act
        SUT.registerClientListener(recipeClientListener1);
        RecipeRequest request = new RecipeRequest(
                recipeId
        );
        handler.execute(SUT, request, getCallback());
        verifyIdentityDatabaseCalledWithNewIdAndReturnDataUnavailable();

        // Assert recipe listener updated with correct recipe state model
        verify(recipeClientListener1).recipeStateChanged(stateResponseCaptor.capture());
        RecipeStateResponse recipeStateResponse = stateResponseCaptor.getValue();

        assertEquals(RecipeState.DATA_UNAVAILABLE, recipeStateResponse.getState());
        assertEquals(1, recipeStateResponse.getComponentStates().size());
        assertTrue(recipeStateResponse.getComponentStates().containsKey(ComponentName.IDENTITY));
        assertEquals(ComponentState.DATA_UNAVAILABLE, recipeStateResponse.getComponentStates().
                get(ComponentName.IDENTITY));
    }

    @Test
    public void recipeRequestNewId_invokerIssuesCommandToAllReceivers_responseINVALID_UNCHANGED_INVALID_MODELS() {
        // Arrange
        String recipeId = INVALID_NEW.getId();
        RecipeRequest request = new RecipeRequest(
                recipeId
        );
        // Act
        SUT.registerClientListener(recipeClientListener1);
        handler.execute(SUT, request, getCallback());
        verifyIdentityDatabaseCalledWithNewIdAndReturnDataUnavailable();

        // Assert request originator updated with recipe response
        assertEquals(RecipeState.DATA_UNAVAILABLE, onRecipeErrorResponse.getRecipeState());
        assertTrue(onRecipeErrorResponse.getFailReasons().contains(FailReason.MISSING_COMPONENTS));
    }

    @Test
    public void recipeRequestNewId_invokerIssuesCommandToAllReceivers_correctComponentResponses() {
        // Arrange
        String recipeId = INVALID_NEW.getId();
        // Act
        SUT.registerClientListener(recipeClientListener1);
        RecipeRequest request = new RecipeRequest(
                recipeId
        );
        handler.execute(SUT, request, getCallback());
        verifyIdentityDatabaseCalledWithNewIdAndReturnDataUnavailable();

        // Assert correct response identity component
        ComponentState identityState = onRecipeErrorResponse.getIdentityResponse().getState();
        assertEquals(ComponentState.DATA_UNAVAILABLE, identityState);

        List<FailReasons> identityFails = onRecipeErrorResponse.getIdentityResponse().getFailReasons();
        assertEquals(1, identityFails.size());
        assertTrue(identityFails.contains(RecipeIdentity.FailReason.DATA_UNAVAILABLE));
    }

    @Test
    public void identityRequestNewId_initialRequest_invokerIssuesCommandToAllReceivers() {
        // Arrange
        String recipeId = INVALID_NEW.getId();
        RecipeIdentityRequest identityRequest = RecipeIdentityRequest.Builder.getDefault().
                setRecipeId(recipeId).
                build();
        // Act
        SUT.registerClientListener(recipeClientListener1);
        handler.execute(SUT, identityRequest, getCallback());
        verifyIdentityDatabaseCalledWithNewIdAndReturnDataUnavailable();
        // Assert
    }

    @Test
    public void identityRequestNewId_titleValidDescriptionValid_identityStateVALID_CHANGED() {
        // Arrange
        String recipeId = IDENTITY_INVALID_NEW_EMPTY.getId();
        ArgumentCaptor<RecipeIdentityEntity> identityEntity = ArgumentCaptor.forClass(RecipeIdentityEntity.class);
        whenTimeProviderReturnTime(IDENTITY_VALID_NEW_COMPLETE.getCreateDate());
        // Request/Response 1
        RecipeIdentityRequest firstRequest = RecipeIdentityRequest.Builder.
                getDefault().
                setRecipeId(recipeId).
                build();
        SUT.registerClientListener(recipeClientListener1);
        handler.execute(SUT, firstRequest, getCallback());

        verifyIdentityDatabaseCalledWithNewIdAndReturnDataUnavailable();

        // Request/Response 2
        String validTitle = IDENTITY_VALID_NEW_COMPLETE.getTitle();
        RecipeIdentityRequest.Model validTitleModel = RecipeIdentityRequest.Model.Builder.
                basedOn(onIdentityErrorResponse.getModel()).
                setTitle(validTitle).
                build();

        RecipeIdentityRequest validTitleRequest = new RecipeIdentityRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(validTitleModel).
                build();

        handler.execute(SUT, validTitleRequest, getCallback());

        // Request/Response 3
        String validDescription = IDENTITY_VALID_NEW_COMPLETE.getDescription();
        RecipeIdentityRequest.Model validTitleDescriptionModel = RecipeIdentityRequest.Model.Builder.
                basedOn(onIdentitySuccessResponse.getModel()).
                setDescription(validDescription).
                build();
        RecipeIdentityRequest validDescriptionRequest = new RecipeIdentityRequest.Builder().
                setRecipeId(recipeId).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(validTitleDescriptionModel).
                build();
        // Act
        handler.execute(SUT, validDescriptionRequest, getCallback());
        // Assert save
        verify(repoIdentityMock, times((2))).save(identityEntity.capture());
        assertEquals(IDENTITY_VALID_NEW_COMPLETE, identityEntity.getValue());
        // Assert values
        assertEquals(validTitle, onIdentitySuccessResponse.getModel().getTitle());
        assertEquals(validDescription, onIdentitySuccessResponse.getModel().getDescription());
        // Assert state
        assertEquals(RecipeStateCalculator.ComponentState.VALID_CHANGED, onIdentitySuccessResponse.getState());
        assertEquals(1, onIdentitySuccessResponse.getFailReasons().size());
        assertTrue(onIdentitySuccessResponse.getFailReasons().contains(RecipeIdentity.FailReason.NONE));
    }

    // Check RecipeResponse as well as listener response

    // newId_invalidColleagueValuesUpdated_mediatorNotifiesRecipeStateINVALID_CHANGED
    // newId_allRequiredColleaguesMadeValid_mediatorNotifiesClientVALID_CHANGED
    // newId_allColleaguesMadeValid_mediatorNotifiesClientCOMPLETE

    @Test
    public void existingId_validData_correctDataRequested() {

    }

    @Test
    public void existingId_validData_clientListenersNotified() {

    }

    @Test
    public void existingId_validData_onlyRegisteredListenersNotified() {
        // Arrange
        String recipeId = VALID_EXISTING_RECIPE.getId();
        // Act
        SUT.registerClientListener(recipeClientListener1);
        SUT.registerClientListener(recipeStateListener2);
        SUT.unRegisterClientListener(recipeStateListener2);
        RecipeRequest request = new RecipeRequest(
                recipeId
        );
        handler.execute(SUT, request, getCallback());
        // Assert database called and return data
        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE_IDENTITY);
        // Assert listeners called
        verify(recipeClientListener1).recipeStateChanged(any(RecipeStateResponse.class));
        verifyNoMoreInteractions(recipeStateListener2);
    }

    // existingId_invalidData_recipeStateINVALID_UNCHANGED
    // existingId_validDataUpdatedWithInvalidData_recipeStateINVALID_CHANGED

    @Test
    public void existingId_validData_componentStateVALID_UNCHANGED() {
        // Arrange
        String recipeId = VALID_EXISTING_RECIPE.getId();
        // Act
        SUT.registerClientListener(recipeClientListener1);
        SUT.registerClientListener(recipeStateListener2);
        RecipeRequest request = new RecipeRequest(recipeId);
        handler.execute(SUT, request, getCallback());
        // Assert database called and return data
        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE_IDENTITY);
        // Assert listeners called
        verify(recipeClientListener1).recipeStateChanged(stateResponseCaptor.capture());
        verify(recipeStateListener2).recipeStateChanged(stateResponseCaptor.capture());
        // Assert recipe component status
        ComponentState identityComponentState1 = stateResponseCaptor.getAllValues().
                get(0).
                getComponentStates().
                get(ComponentName.IDENTITY);
        assertEquals(ComponentState.VALID_UNCHANGED, identityComponentState1);

        verify(recipeStateListener2).recipeStateChanged(stateResponseCaptor.capture());
        ComponentState identityComponentState2 = stateResponseCaptor.getAllValues().
                get(1).
                getComponentStates().
                get(ComponentName.IDENTITY);
        assertEquals(ComponentState.VALID_UNCHANGED, identityComponentState2);
    }

    @Test
    public void existingId_validData_recipeStateVALID_UNCHANGED() {
        // Arrange
        String recipeId = VALID_EXISTING_RECIPE.getId();
        // Act
        SUT.registerClientListener(recipeClientListener1);
        SUT.registerClientListener(recipeStateListener2);
        RecipeRequest request = new RecipeRequest(
                recipeId
        );
        handler.execute(SUT, request, getCallback());
        // Assert database called and return data
        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE_IDENTITY);
        // Assert listeners called
        verify(recipeClientListener1).recipeStateChanged(stateResponseCaptor.capture());
        verify(recipeStateListener2).recipeStateChanged(stateResponseCaptor.capture());
        // Assert recipe state
        RecipeState recipeState = stateResponseCaptor.getValue().getState();
        assertEquals(RecipeState.VALID_UNCHANGED, recipeState);
    }

    // existingId_validDataUpdatedWithValidValues_recipeStateVALID_CHANGED

    // existingId_invalidDataInOneComponent_recipeStateINVALID_UNCHANGED

    // newIdAndCloneToId_validData_mediatorIssuesCloneRequestToAllColleagues
    // anotherExistingId_sentDirectlyToRecipeComponent_mediatorUpdatesAllOtherComponentsWithNewId

    // region helper methods -----------------------------------------------------------------------
    private void verifyIdentityDatabaseCalledWithNewIdAndReturnDataUnavailable() {
        String recipeId = IDENTITY_INVALID_NEW_EMPTY.getId();
        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onDataNotAvailable();
    }

    private void verifyIdentityDatabaseCalledWithValidExistingCompleteIdAndReturnValues() {
        verify(repoIdentityMock).getById(eq(VALID_EXISTING_COMPLETE_IDENTITY.getId()),
                repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE_IDENTITY);
    }

    private UseCase.Callback<RecipeResponse> getCallback() {
        return new UseCase.Callback<RecipeResponse>() {

            @Override
            public void onSuccess(RecipeResponse response) {
                if (response != null) {
                    System.out.println(TAG + "recipeResponseOnSuccess: " + response);
                    onRecipeSuccessResponse = response;
                    onIdentitySuccessResponse = response.getIdentityResponse();
                }
            }

            @Override
            public void onError(RecipeResponse response) {
                if (response != null) {
                    System.out.println(TAG + "recipeResponseOnError: " + response);
                    onRecipeErrorResponse = response;

                    if (response.getIdentityResponse().getFailReasons().contains(FailReason.NONE)) {
                        onIdentitySuccessResponse = response.getIdentityResponse();
                    } else {
                        onIdentityErrorResponse = response.getIdentityResponse();
                    }
                }
            }
        };
    }

    private void whenTimeProviderReturnTime(long time) {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class RecipeClient implements Recipe.Listener {

        @Override
        public void recipeStateChanged(RecipeStateResponse stateResponse) {

        }
    }
    // endregion helper classes --------------------------------------------------------------------


}