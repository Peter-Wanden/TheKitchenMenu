package com.example.peter.thekitchenmenu.domain.usecase.recipemediator;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateModel;
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

import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeState.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class RecipeTest {

    private static final String TAG = "tkm-" + RecipeTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    private static final RecipeEntity INVALID_NEW =
            TestDataRecipeEntity.getNewInvalid();
    private static final RecipeIdentityEntity INVALID_NEW_EMPTY =
            TestDataRecipeIdentityEntity.getInvalidNewEmpty();

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
    ArgumentCaptor<RecipeStateModel> stateModelCaptor;

    private RecipeIdentityResponse onIdentitySuccessResponse;
    private RecipeIdentityResponse onIdentityErrorResponse;

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
        return new Recipe(recipeIdentity);
    }

    @Test
    public void newId_mediatorIssuesIdToAllColleagues() {
        // Arrange
        String recipeId = INVALID_NEW.getId();
        // Act
        SUT.registerClientListener(recipeClientListener1);
        SUT.startColleaguesAndNotify(recipeId);
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
    }

    @Test
    public void newId_mediatorIssuesIdToAllColleagues_allColleaguesReportCorrectValuesToMediator() {
        // Arrange
        String recipeId = INVALID_NEW.getId();
        // Act
        SUT.registerClientListener(recipeClientListener1);
        SUT.startColleaguesAndNotify(recipeId);
        // Assert database call, return nothing
        verifyIdentityDatabaseCalledWithNewIdAndReturnDataNotAvailable();
        // Assert verify listener called with correct values
        verify(recipeClientListener1).recipeStateChanged(stateModelCaptor.capture());
        HashMap<ComponentName, ComponentState> componentStates =
                new LinkedHashMap<>(stateModelCaptor.getValue().getComponentStates());
        assertEquals(ComponentState.DATA_UNAVAILABLE, componentStates.get(ComponentName.IDENTITY));
    }

    @Test
    public void newId_colleagueIssuedNewId_mediatorIssuesNewIdToAllColleagues() {
        // Arrange first transaction from client, to mediator, to colleague
        String recipeIdIssuedByMediator = INVALID_NEW.getId();
        // Act
        SUT.registerClientListener(recipeClientListener1);
        SUT.startColleaguesAndNotify(recipeIdIssuedByMediator);
        // Assert first transaction responses
        verifyIdentityDatabaseCalledWithNewIdAndReturnDataNotAvailable();
        verify(recipeClientListener1).recipeStateChanged(stateModelCaptor.capture());
        assertEquals(ComponentState.DATA_UNAVAILABLE,
                stateModelCaptor.getValue().getComponentStates().get(ComponentName.IDENTITY));

        // Arrange second transaction from RecipeIdentity colleague, to mediator, to client
        String recipeIdIssuedByColleague = VALID_EXISTING_RECIPE.getId();
        RecipeIdentityRequest request = RecipeIdentityRequest.Builder.getDefault().
                setRecipeId(recipeIdIssuedByColleague).build();
        RecipeIdentity identity = SUT.getIdentity();
        // Act
        handler.execute(identity, request, getIdentityUseCaseCallBack());
        verifyIdentityDatabaseCalledWithValidExistingCompleteIdAndReturnValues();
        // Assert second transaction responses
        verify(recipeClientListener1).recipeStateChanged(stateModelCaptor.capture());
        assertEquals(ComponentState.VALID_UNCHANGED,
                stateModelCaptor.getValue().getComponentStates().get(ComponentName.IDENTITY));
        System.out.println(stateModelCaptor.getAllValues());

        // todo, test correct listeners called
        // todo, add another client and see if their model database is called with correct id
        // todo, test RecipeMediator functionality from within RecipeIdentityTest

    }

    // newId_invalidColleagueValuesUpdated_mediatorNotifiesRecipeStateINVALID_CHANGED
    // newId_allRequiredColleaguesMadeValid_mediatorNotifiesClientVALID_CHANGED
    // newId_allColleaguesMadeValid_mediatorNotifiesClientCOMPLETE

    @Test
    public void existingId_validData_correctDataRequested() {
        // Arrange
        String recipeId = VALID_EXISTING_RECIPE.getId();
        // Act
        SUT.startColleaguesAndNotify(recipeId);
        // Assert
        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE_IDENTITY);
    }

    @Test
    public void existingId_validData_clientListenersNotified() {
        // Arrange
        String recipeId = VALID_EXISTING_RECIPE.getId();
        // Act
        SUT.registerClientListener(recipeClientListener1);
        SUT.registerClientListener(recipeStateListener2);
        SUT.startColleaguesAndNotify(recipeId);
        // Assert database called and return data
        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE_IDENTITY);
        // Assert listeners called
        verify(recipeClientListener1).recipeStateChanged(stateModelCaptor.capture());
        verify(recipeStateListener2).recipeStateChanged(stateModelCaptor.capture());
    }

    @Test
    public void existingId_validData_onlyRegisteredListenersNotified() {
        // Arrange
        String recipeId = VALID_EXISTING_RECIPE.getId();
        // Act
        SUT.registerClientListener(recipeClientListener1);
        SUT.registerClientListener(recipeStateListener2);
        SUT.unRegisterClientListener(recipeStateListener2);
        SUT.startColleaguesAndNotify(recipeId);
        // Assert database called and return data
        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE_IDENTITY);
        // Assert listeners called
        verify(recipeClientListener1).recipeStateChanged(any(RecipeStateModel.class));
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
        SUT.startColleaguesAndNotify(recipeId);
        // Assert database called and return data
        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE_IDENTITY);
        // Assert listeners called
        verify(recipeClientListener1).recipeStateChanged(stateModelCaptor.capture());
        verify(recipeStateListener2).recipeStateChanged(stateModelCaptor.capture());
        // Assert recipe component status
        ComponentState identityComponentState1 = stateModelCaptor.getAllValues().
                get(0).
                getComponentStates().
                get(ComponentName.IDENTITY);
        assertEquals(ComponentState.VALID_UNCHANGED, identityComponentState1);

        verify(recipeStateListener2).recipeStateChanged(stateModelCaptor.capture());
        ComponentState identityComponentState2 = stateModelCaptor.getAllValues().
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
        SUT.startColleaguesAndNotify(recipeId);
        // Assert database called and return data
        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE_IDENTITY);
        // Assert listeners called
        verify(recipeClientListener1).recipeStateChanged(stateModelCaptor.capture());
        verify(recipeStateListener2).recipeStateChanged(stateModelCaptor.capture());
        // Assert recipe state
        State recipeState = stateModelCaptor.getValue().getRecipeState();
        assertEquals(State.VALID_UNCHANGED, recipeState);
    }

    // existingId_validDataUpdatedWithValidValues_recipeStateVALID_CHANGED

    // existingId_invalidDataInOneComponent_recipeStateINVALID_UNCHANGED

    // newIdAndCloneToId_validData_mediatorIssuesCloneRequestToAllColleagues
    // anotherExistingId_sentDirectlyToRecipeComponent_mediatorUpdatesAllOtherComponentsWithNewId

    // region helper methods -----------------------------------------------------------------------
    private void verifyIdentityDatabaseCalledWithNewIdAndReturnDataNotAvailable() {
        String recipeId = INVALID_NEW_EMPTY.getId();
        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onDataNotAvailable();
    }

    private UseCase.Callback<RecipeIdentityResponse> getIdentityUseCaseCallBack() {
        return new UseCase.Callback<RecipeIdentityResponse>() {

            @Override
            public void onSuccess(RecipeIdentityResponse response) {
                onIdentitySuccessResponse = response;

            }

            @Override
            public void onError(RecipeIdentityResponse response) {
                onIdentityErrorResponse = response;
            }
        };
    }

    private void verifyIdentityDatabaseCalledWithValidExistingCompleteIdAndReturnValues() {
        verify(repoIdentityMock).getById(eq(VALID_EXISTING_COMPLETE_IDENTITY.getId()),
                repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE_IDENTITY);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class RecipeClient implements Recipe.Listener {

        @Override
        public void recipeStateChanged(RecipeStateModel stateModel) {

        }
    }
    // endregion helper classes --------------------------------------------------------------------


}