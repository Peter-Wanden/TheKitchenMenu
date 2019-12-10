package com.example.peter.thekitchenmenu.domain.usecase.recipeIdentity;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.UseCaseInteractor;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeIdentityEntity;
import com.example.peter.thekitchenmenu.utils.TimeProvider;

import org.junit.*;
import org.mockito.*;

import static com.example.peter.thekitchenmenu.domain.usecase.recipeIdentity.UseCaseRecipeIdentity.DO_NOT_CLONE;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

public class UseCaseRecipeIdentityTest {

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
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeIdentityEntity>> getEntityCallbackCaptor;
    @Mock
    TimeProvider timeProviderMock;
    private UseCaseRecipeIdentity.Response actualResponse;
    // endregion helper fields ---------------------------------------------------------------------

    private UseCaseRecipeIdentity SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        handler = new UseCaseHandler(new UseCaseSchedulerMock());
        SUT = givenUseCase();
    }

    private UseCaseRecipeIdentity givenUseCase() {
        return new UseCaseRecipeIdentity(repoMock, timeProviderMock);
    }

    @Test
    public void newRecipeId_response_NO_DATA_AVAILABLE() {
        // Arrange
        // Act
        givenNewEmptyModelSimulateNothingReturnedFromDatabase();
        // Assert
        assertEquals(UseCaseRecipeIdentity.Result.DATA_UNAVAILABLE, actualResponse.getResult());
    }

    @Test
    public void newRecipeId_invalidTitleValidDescription_responseInvalidChanged() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(INVALID_NEW_EMPTY.getCreateDate());

        givenNewEmptyModelSimulateNothingReturnedFromDatabase();

        UseCaseRecipeIdentity.Model modelValidDescription = new UseCaseRecipeIdentity.Model.
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
        assertEquals(UseCaseRecipeIdentity.Result.INVALID_CHANGED, actualResponse.getResult());
    }

    @Test
    public void newRecipeId_validTitle_valuesPersisted() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_TITLE_VALID.getCreateDate());

        givenNewEmptyModelSimulateNothingReturnedFromDatabase();

        UseCaseRecipeIdentity.Model validTitleModel = new UseCaseRecipeIdentity.Model.Builder().
                setId(actualResponse.getRecipeId()).
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
        assertEquals(UseCaseRecipeIdentity.Result.VALID_CHANGED, actualResponse.getResult());
    }

    @Test
    public void newRecipeId_validTitleValidDescription_valuesPersisted() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).
                thenReturn(VALID_NEW_COMPLETE.getCreateDate());
        givenNewEmptyModelSimulateNothingReturnedFromDatabase();

        UseCaseRecipeIdentity.Model validTitleAndDescription = new UseCaseRecipeIdentity.Model.
                Builder().
                setId(INVALID_NEW_EMPTY.getId()).
                setTitle(VALID_NEW_COMPLETE.getTitle()).
                setDescription(VALID_NEW_COMPLETE.getDescription()).
                setCreateDate(VALID_NEW_COMPLETE.getCreateDate()).
                setLastUpdate(VALID_NEW_COMPLETE.getLastUpdate()).
                build();

        UseCaseRecipeIdentity.Request request = new UseCaseRecipeIdentity.Request.Builder().
                setRecipeId(INVALID_NEW_EMPTY.getId()).
                setCloneToRecipeId(DO_NOT_CLONE).
                setModel(validTitleAndDescription).
                build();
        // Act
        handler.execute(SUT, request, getCallback());

        // Assert
        verify(repoMock).save(VALID_NEW_COMPLETE);
        assertEquals(UseCaseRecipeIdentity.Result.VALID_CHANGED, actualResponse.getResult());
    }

    @Test
    public void existingRecipeId_existingValidValuesLoaded_Result_VALID_UNCHANGED() {
        // Arrange
        UseCaseRecipeIdentity.Request request = getRequest(
                VALID_EXISTING_COMPLETE.getId(), DO_NOT_CLONE, getDefaultModel());
        // Act
        handler.execute(SUT, request, getCallback());

        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(UseCaseRecipeIdentity.Result.VALID_UNCHANGED, actualResponse.getResult());
    }

    @Test
    public void existingRecipeIdCloneToRecipeId_persistenceCalledWithExistingRecipeId() {
        // Arrange
        UseCaseRecipeIdentity.Request request = getRequest(
                VALID_FROM_ANOTHER_USER.getId(), VALID_NEW_CLONED.getId(), getDefaultModel());

        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_CLONED.getCreateDate());
        // Act
        handler.execute(SUT, request, getCallback());
        // Assert
        simulateGetValidFromAnotherUserFromDatabase();
        verify(repoMock).save(eq(VALID_NEW_CLONED));
        assertEquals(UseCaseRecipeIdentity.Result.VALID_UNCHANGED, actualResponse.getResult());
    }

    @Test
    public void existingRecipeIdCloneToRecipeId_descriptionChangedAfterClone_entityOnlyClonedOnce() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                VALID_CLONED_DESCRIPTION_UPDATED.getCreateDate());

        UseCaseRecipeIdentity.Request requestClone = getRequest(
                VALID_FROM_ANOTHER_USER.getId(),
                VALID_CLONED_DESCRIPTION_UPDATED.getId(),
                getDefaultModel());

        // Act
        handler.execute(SUT, requestClone, getCallback());
        simulateGetValidFromAnotherUserFromDatabase();
        // Assert
        verify(repoMock).save(VALID_NEW_CLONED);

        // Arrange
        UseCaseRecipeIdentity.Request requestUpdateDescription = getRequest(
                actualResponse.getRecipeId(),
                DO_NOT_CLONE,
                new UseCaseRecipeIdentity.Model(
                        actualResponse.getModel().getId(),
                        actualResponse.getModel().getTitle(),
                        VALID_CLONED_DESCRIPTION_UPDATED.getDescription(),
                        actualResponse.getModel().getCreateDate(),
                        actualResponse.getModel().getLastUpdate()));
        // Act
        handler.execute(SUT, requestUpdateDescription, getCallback());
        // Assert
        verify(repoMock).save(VALID_CLONED_DESCRIPTION_UPDATED);
        assertEquals(UseCaseRecipeIdentity.Result.VALID_CHANGED, actualResponse.getResult());
    }

    // region helper methods -----------------------------------------------------------------------
    private void givenNewEmptyModelSimulateNothingReturnedFromDatabase() {
        UseCaseRecipeIdentity.Model initialModel = new UseCaseRecipeIdentity.Model.Builder().
                getDefault().setId(INVALID_NEW_EMPTY.getId()).build();

        handler.execute(
                SUT,
                getRequest(INVALID_NEW_EMPTY.getId(), DO_NOT_CLONE, initialModel),
                getCallback());
        simulateNothingReturnedFromDatabase();
    }

    private UseCaseRecipeIdentity.Model getDefaultModel() {
        return new UseCaseRecipeIdentity.Model.Builder().getDefault().build();
    }

    private UseCaseRecipeIdentity.Request getRequest(String recipeId,
                                                     String cloneToRecipeId,
                                                     UseCaseRecipeIdentity.Model model) {
        return new UseCaseRecipeIdentity.Request(recipeId, cloneToRecipeId, model);
    }

    private UseCaseInteractor.Callback<UseCaseRecipeIdentity.Response> getCallback() {
        return new UseCaseInteractor.Callback<UseCaseRecipeIdentity.Response>() {

            @Override
            public void onSuccess(UseCaseRecipeIdentity.Response response) {
                UseCaseRecipeIdentityTest.this.actualResponse = response;

            }

            @Override
            public void onError(UseCaseRecipeIdentity.Response response) {
                UseCaseRecipeIdentityTest.this.actualResponse = response;
            }
        };
    }

    private void simulateNothingReturnedFromDatabase() {
        verify(repoMock).getById(eq(INVALID_NEW_EMPTY.getId()),
                getEntityCallbackCaptor.capture());
        getEntityCallbackCaptor.getValue().onDataNotAvailable();
    }

    private void simulateGetValidNewTitleValidFromDatabase() {
        verify(repoMock).getById(eq(VALID_NEW_TITLE_VALID.getId()),
                getEntityCallbackCaptor.capture());
        getEntityCallbackCaptor.getValue().onEntityLoaded(VALID_NEW_TITLE_VALID);
    }

    private void simulateGetValidExistingCompleteFromDatabase() {
        verify(repoMock).getById(eq(VALID_EXISTING_COMPLETE.getId()),
                getEntityCallbackCaptor.capture());
        getEntityCallbackCaptor.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE);
    }

    private void simulateGetValidFromAnotherUserFromDatabase() {
        verify(repoMock).getById(eq(VALID_FROM_ANOTHER_USER.getId()),
                getEntityCallbackCaptor.capture());

        getEntityCallbackCaptor.getValue().onEntityLoaded(VALID_FROM_ANOTHER_USER);
    }


    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}