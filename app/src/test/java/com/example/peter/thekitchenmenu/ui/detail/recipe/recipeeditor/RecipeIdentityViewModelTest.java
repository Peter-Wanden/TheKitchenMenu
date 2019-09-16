package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.testdata.RecipeIdentityEntityTestData;
import com.example.peter.thekitchenmenu.testdata.RecipeValidatorTestData;
import com.example.peter.thekitchenmenu.utils.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RecipeIdentityViewModelTest {

    // region constants ----------------------------------------------------------------------------
    private static final RecipeIdentityEntity INVALID_NEW_EMPTY =
            RecipeIdentityEntityTestData.getInvalidNewEmpty();
    private static final RecipeIdentityEntity INVALID_NEW_TITLE_INVALID =
            RecipeIdentityEntityTestData.getInvalidNewTitleUpdatedWithInvalidValue();
    private static final RecipeIdentityEntity INVALID_NEW_TITLE_INVALID_DESCRIPTION_VALID =
            RecipeIdentityEntityTestData.getInvalidNewTitleInvalidDescriptionValid();
    private static final RecipeIdentityEntity VALID_NEW_TITLE_VALID =
            RecipeIdentityEntityTestData.getValidNewTitleUpdatedWithValidValue();
    private static final RecipeIdentityEntity VALID_NEW_COMPLETE =
            RecipeIdentityEntityTestData.getValidNewComplete();
    private static final RecipeIdentityEntity INVALID_EXISTING_INCOMPLETE_INVALID_TITLE =
            RecipeIdentityEntityTestData.getInvalidExistingIncomplete();
    private static final RecipeIdentityEntity VALID_EXISTING_COMPLETE =
            RecipeIdentityEntityTestData.getValidExistingComplete();
    private static final RecipeIdentityEntity VALID_FROM_ANOTHER_USER =
            RecipeIdentityEntityTestData.getValidCompleteFromAnotherUser();
    private static final RecipeIdentityEntity INVALID_FROM_ANOTHER_USER =
            RecipeIdentityEntityTestData.getInvalidCompleteFromAnotherUser();
    private static final RecipeIdentityEntity VALID_NEW_CLONED =
            RecipeIdentityEntityTestData.getValidNewCloned();
    private static final RecipeIdentityEntity INVALID_NEW_CLONED =
            RecipeIdentityEntityTestData.getInvalidNewCloned();
    private static final RecipeIdentityEntity VALID_CLONED_DESCRIPTION_UPDATED =
            RecipeIdentityEntityTestData.getValidNewClonedDescriptionUpdatedComplete();

    private RecipeModelStatus UNCHANGED_INVALID =
            RecipeValidatorTestData.getIdentityModelStatusUnchangedInvalid();
    private RecipeModelStatus UNCHANGED_VALID =
            RecipeValidatorTestData.getIdentityModelStatusUnchangedValid();
    private RecipeModelStatus CHANGED_INVALID =
            RecipeValidatorTestData.getIdentityModelStatusChangedInvalid();
    private RecipeModelStatus CHANGED_VALID =
            RecipeValidatorTestData.getIdentityModelStatusChangedValid();

    private static final String ERROR_MESSAGE_TOO_LONG = "error_message_too_long";

    private static final String LONG_TEXT_VALIDATION_ERROR = "long_text_validation_error";
    private static final String SHORT_TEXT_VALIDATION_ERROR = "short_text_validation_error";

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Mock
    Resources resourcesMock;
    @Mock
    TextValidationHandler textValidationHandlerMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeIdentityEntity>>
            getEntityCallbackArgumentCaptor;
    @Mock
    DataSource<RecipeIdentityEntity> identityDataSourceMock;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    RecipeValidation.RecipeValidatorModelSubmission modelValidationSubmitterMock;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeIdentityViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupResourceMockReturnValues();

        SUT = new RecipeIdentityViewModel(
                identityDataSourceMock,
                timeProviderMock,
                resourcesMock,
                textValidationHandlerMock
        );

        SUT.setModelValidationSubmitter(modelValidationSubmitterMock);
    }

    @Test
    public void start_recipeId_databaseCalledWithRecipeId() {
        // Arrange
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        // Assert
        verify(identityDataSourceMock).getById(eq(INVALID_NEW_EMPTY.getId()),
                getEntityCallbackArgumentCaptor.capture());
    }

    @Test
    public void start_recipeIdWithNoRecipe_nothingSetToObservers() {
        // Arrange
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        // Assert
        assertEquals(INVALID_NEW_EMPTY.getTitle(), SUT.titleObservable.get());
        assertEquals(INVALID_NEW_EMPTY.getDescription(), SUT.descriptionObservable.get());
        assertNull(SUT.titleErrorMessage.get());
        assertNull(SUT.descriptionErrorMessage.get());
    }

    @Test
    public void start_newRecipeId_RecipeModelStatusUNCHANGED_INVALID() {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        // Assert
        verify(modelValidationSubmitterMock).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(UNCHANGED_INVALID, modelStatus);
    }

    @Test
    public void start_validExistingCompleteRecipeId_titleSetToObservable() {
        // Arrange
        whenTextValidationValidateTitleAndDescription();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(VALID_EXISTING_COMPLETE.getTitle(), SUT.titleObservable.get());
    }

    @Test
    public void start_validExistingCompleteRecipeId_descriptionSetToObservable() {
        // Arrange
        whenTextValidationValidateTitleAndDescription();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(VALID_EXISTING_COMPLETE.getDescription(), SUT.descriptionObservable.get());
    }

    @Test
    public void start_validExistingCompleteRecipeId_RecipeModelStatusIDENTITY_MODEL_UNCHANGED_VALID() {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        whenTextValidationValidateTitleAndDescription();
//        whenIntFromObserverMockReturnExistingValidCompleteRecipeTimes();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        // Assert
        simulateGetValidExistingCompleteFromDatabase();
        verify(modelValidationSubmitterMock).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(UNCHANGED_VALID, modelStatus);
    }





    @Test
    public void startWithCloned_existingAndNewRecipeIdSupplied_databaseCalledWithOldId() {
        // Arrange
        // Act
        SUT.startByCloningModel(
                VALID_FROM_ANOTHER_USER.getId(), INVALID_NEW_EMPTY.getId());
        // Assert
        verify(identityDataSourceMock).getById(eq(VALID_FROM_ANOTHER_USER.getId()), eq(SUT));
    }

    @Test
    public void startWithCloned_existingAndNewRecipeIdSupplied_existingSavedWithNewId() {
        // Arrange
        whenTimeProviderThenReturnClonedTimes();
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.startByCloningModel(
                VALID_FROM_ANOTHER_USER.getId(), INVALID_NEW_EMPTY.getId());
        simulateGetValidEntityFromAnotherUserFromDatabase();
        // Assert
        verify(identityDataSourceMock).save(eq(VALID_NEW_CLONED));
    }

    @Test
    public void startWithCloned_descriptionChanged_modelSavedWithUpdatedDescription() {
        // Arrange
        ArgumentCaptor<RecipeIdentityEntity> ac = ArgumentCaptor.forClass(RecipeIdentityEntity.class);
        whenTimeProviderThenReturnClonedTimes();
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.startByCloningModel(
                VALID_FROM_ANOTHER_USER.getId(), INVALID_NEW_EMPTY.getId());
        simulateGetValidEntityFromAnotherUserFromDatabase();
        SUT.descriptionObservable.set(VALID_CLONED_DESCRIPTION_UPDATED.getDescription());
        // Assert
        verify(identityDataSourceMock, times(2)).save(ac.capture());
        assertEquals(VALID_CLONED_DESCRIPTION_UPDATED, ac.getAllValues().get(1));
    }

    @Test
    public void startWithCloned_originalModelNotAvailable_newModelCreatedAndSaved() {
        // Arrange
        whenShortTextValidationReturnErrorMessage();
        whenLongTextValidationReturnValidated();
        whenTimeProviderThenReturnClonedTimes();
        // Act
        SUT.startByCloningModel(
                VALID_FROM_ANOTHER_USER.getId(), INVALID_NEW_EMPTY.getId());

        verify(identityDataSourceMock).getById(eq(VALID_FROM_ANOTHER_USER.getId()),
                getEntityCallbackArgumentCaptor.capture());
        getEntityCallbackArgumentCaptor.getValue().onDataNotAvailable();

        // Assert
        verify(identityDataSourceMock).save(eq(INVALID_NEW_EMPTY));
    }

    @Test
    public void observableTitleUpdatedWithInvalidValue_allOtherFieldsEmpty_errorMessageObservableUpdatedWithErrorMessage() {
        // Arrange
        whenShortTextValidationReturnErrorMessage();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(INVALID_NEW_TITLE_INVALID.getTitle());
        // Assert
        assertEquals(SUT.titleErrorMessage.get(), SHORT_TEXT_VALIDATION_ERROR);
    }

    @Test
    public void observableTitleUpdatedWithInvalidValue_allOtherFieldsValid_errorMessageObservableUpdatedWithErrorMessage() {
        // Arrange
        whenShortTextValidationReturnErrorMessage();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.titleObservable.set(INVALID_EXISTING_INCOMPLETE_INVALID_TITLE.getTitle());
        // Assert
        assertEquals(SUT.titleErrorMessage.get(), SHORT_TEXT_VALIDATION_ERROR);
    }

    @Test
    public void observableTitleUpdatedWithInvalidValue_allOtherFieldsEmpty_RecipeModelStatus_IDENTITY_MODEL_CHANGED_INVALID() {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        whenShortTextValidationReturnErrorMessage();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(INVALID_NEW_TITLE_INVALID.getTitle());
        // Assert
        verify(modelValidationSubmitterMock, times(2)).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(CHANGED_INVALID, modelStatus);
    }

    @Test
    public void observableTitleUpdatedWithValidValue_allOtherFieldsEmpty_errorMessageObservableUpdatedWithNull() {
        // Arrange
        whenShortTextValidationReturnValidated();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_NEW_TITLE_VALID.getTitle());
        // Assert
        assertNull(SUT.titleErrorMessage.get());
    }

    @Test
    public void observableTitleUpdatedWithValidValue_allOtherFieldsEmpty_entitySavedToDatabase() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenTimeProviderThenReturnNewEntityCreateDate();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_NEW_TITLE_VALID.getTitle());
        // Assert
        verify(identityDataSourceMock).save(eq(VALID_NEW_TITLE_VALID));
    }

    @Test
    public void observableTitleUpdatedWithValidValue_allOtherFieldsEmpty_RecipeModelStatus_IDENTITY_MODEL_CHANGED_VALID() {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        whenShortTextValidationReturnValidated();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_NEW_TITLE_VALID.getTitle());
        // Assert
        verify(modelValidationSubmitterMock, times(2)).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(CHANGED_VALID, modelStatus);
    }

    @Test
    public void observableDescriptionUpdatedWithInvalidValue_titleValid_errorMessageObservableUpdatedWithErrorMessage() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnErrorMessage();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_NEW_TITLE_VALID.getTitle());
        SUT.descriptionObservable.set("Doesn't matter what is here as returning an error message!");
        // Assert
        assertEquals(LONG_TEXT_VALIDATION_ERROR, SUT.descriptionErrorMessage.get());
    }

    @Test
    public void observableDescriptionUpdatedWithInvalidValue_titleValid_entityNotSavedToDatabase() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnErrorMessage();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_NEW_TITLE_VALID.getTitle());
        SUT.descriptionObservable.set("Doesn't matter what is here as returning an error message!");
        verify(identityDataSourceMock, times(2)).save(anyObject());
    }

    @Test
    public void observableDescriptionUpdatedWithInvalidValue_titleValid_RecipeModelStatus_IDENTITY_MODEL_CHANGED_INVALID() {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnErrorMessage();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_NEW_TITLE_VALID.getTitle());
        SUT.descriptionObservable.set("Doesn't matter what is here as returning an error message!");
        // Assert
        verify(modelValidationSubmitterMock, times(3)).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(CHANGED_INVALID, modelStatus);
    }

    @Test
    public void observableDescriptionUpdatedWithValidValue_titleValid_errorMessageObservableUpdatedWithNull() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_NEW_COMPLETE.getTitle());
        SUT.descriptionObservable.set(VALID_NEW_COMPLETE.getDescription());
        // Assert
        assertNull(SUT.titleErrorMessage.get());
        assertNull(SUT.descriptionErrorMessage.get());
    }

    @Test
    public void observableDescriptionUpdatedWithValidValue_titleValid_entitySavedToDatabase() {
        // Arrange
        ArgumentCaptor<RecipeIdentityEntity> ac = ArgumentCaptor.forClass(RecipeIdentityEntity.class);
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        whenTimeProviderThenReturnExistingCreateDate();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_NEW_COMPLETE.getTitle());
        SUT.descriptionObservable.set(VALID_NEW_COMPLETE.getDescription());
        // Assert
        verify(identityDataSourceMock, times(3)).save(ac.capture());
        List<RecipeIdentityEntity> identityEntities = ac.getAllValues();
        RecipeIdentityEntity identityEntity = identityEntities.get(2);
        assertEquals(VALID_NEW_COMPLETE.getTitle(), identityEntity.getTitle());
        assertEquals(VALID_NEW_COMPLETE.getDescription(), identityEntity.getDescription());
    }

    @Test
    public void observableDescriptionUpdatedWithValidValue_titleValid_RecipeModelStatus_IDENTITY_MODEL_CHANGED_VALID() {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_NEW_COMPLETE.getTitle());
        SUT.descriptionObservable.set(VALID_NEW_COMPLETE.getDescription());
        // Assert
        verify(modelValidationSubmitterMock, times(3)).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(CHANGED_VALID, modelStatus);
    }

    @Test
    public void observableDescriptionUpdatedWithValidValue_titleInvalid_emptyEntitySaved() {
        whenShortTextValidationReturnErrorMessage();
        whenLongTextValidationReturnValidated();
        whenTimeProviderThenReturnNewEntityCreateDate();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(INVALID_NEW_TITLE_INVALID_DESCRIPTION_VALID.getTitle());
        SUT.descriptionObservable.set(INVALID_NEW_TITLE_INVALID_DESCRIPTION_VALID.getDescription());
        // Assert
        verify(identityDataSourceMock).save(INVALID_NEW_EMPTY);
    }

    @Test // Simulates user input for a complete new recipe IdentityModel
    public void allObservablesUpdatedWithValidData_newRecipeIdSupplied_identityModelSavedToDatabase() {
        // Arrange
        ArgumentCaptor<RecipeIdentityEntity> ac = ArgumentCaptor.forClass(RecipeIdentityEntity.class);
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        whenTimeProviderThenReturnNewEntityCreateDate();
        // Act
        SUT.start(INVALID_NEW_EMPTY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_NEW_COMPLETE.getTitle());
        SUT.descriptionObservable.set(VALID_NEW_COMPLETE.getDescription());
        // Assert
        verify(identityDataSourceMock, times(3)).save(ac.capture());
        List<RecipeIdentityEntity> identityList = ac.getAllValues();
        RecipeIdentityEntity newCompleteIdentityEntity = identityList.get(2);

        assertEquals(VALID_NEW_COMPLETE, newCompleteIdentityEntity);
    }

    @Test(expected = RuntimeException.class)
    public void start_nullRecipe_runtimeExceptionThrown() {
        // Arrange
        // Act
        SUT.start(null);
        // Assert
        ArrayList list = new ArrayList();
        RuntimeException exception = (RuntimeException) list.get(0);
        assertEquals(exception.getMessage(), "Recipe id cannot be null");
    }

    // region helper methods -------------------------------------------------------------------
    private void simulateNothingReturnedFromDatabase() {
        verify(identityDataSourceMock).getById(eq(INVALID_NEW_EMPTY.getId()),
                getEntityCallbackArgumentCaptor.capture());
        getEntityCallbackArgumentCaptor.getValue().onDataNotAvailable();
    }

    private void simulateGetValidExistingCompleteFromDatabase() {
        verify(identityDataSourceMock).getById(eq(
                VALID_EXISTING_COMPLETE.getId()),
                getEntityCallbackArgumentCaptor.capture());

        getEntityCallbackArgumentCaptor.getValue().onEntityLoaded(
                VALID_EXISTING_COMPLETE);
    }

    private void simulateGetValidEntityFromAnotherUserFromDatabase() {
        verify(identityDataSourceMock).getById(eq(
                VALID_FROM_ANOTHER_USER.getId()),
                getEntityCallbackArgumentCaptor.capture());

        getEntityCallbackArgumentCaptor.getValue().onEntityLoaded(
                VALID_FROM_ANOTHER_USER);
    }

    private void whenTextValidationValidateTitleAndDescription() {
        when(textValidationHandlerMock.validateShortText(
                eq(resourcesMock),
                eq(VALID_EXISTING_COMPLETE.getTitle()))).
                thenReturn(TextValidationHandler.VALIDATED);

        when(textValidationHandlerMock.validateLongText(
                eq(resourcesMock),
                eq(VALID_EXISTING_COMPLETE.getDescription()))).
                thenReturn(TextValidationHandler.VALIDATED);
    }

    private void setupResourceMockReturnValues() {
        when(resourcesMock.getString(R.string.input_error_recipe_prep_time_too_long)).thenReturn(ERROR_MESSAGE_TOO_LONG);
        when(resourcesMock.getString(R.string.input_error_recipe_cook_time_too_long)).thenReturn(ERROR_MESSAGE_TOO_LONG);
    }

    private void whenShortTextValidationReturnValidated() {
        when(textValidationHandlerMock.validateShortText(anyObject(), anyString())).
                thenReturn(TextValidationHandler.VALIDATED);
    }

    private void whenShortTextValidationReturnErrorMessage() {
        when(textValidationHandlerMock.validateShortText(anyObject(), anyString())).
                thenReturn(SHORT_TEXT_VALIDATION_ERROR);
    }

    private void whenLongTextValidationReturnValidated() {
        when(textValidationHandlerMock.validateLongText(anyObject(), anyString())).
                thenReturn(TextValidationHandler.VALIDATED);
    }

    private void whenLongTextValidationReturnErrorMessage() {
        when(textValidationHandlerMock.validateLongText(anyObject(), anyString())).
                thenReturn(LONG_TEXT_VALIDATION_ERROR);
    }

//    private void whenIntFromObserver_thenReturn(int returnValue) {
//        when(intFromObservableMock.parseInt(anyObject(), anyObject(), anyInt())).thenReturn(
//                returnValue);
//    }

//    private void whenIntFromObserverMockReturnExistingValidCompleteRecipeTimes() {
//        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepHoursObservable), anyInt())).
//                thenReturn(VALID_EXISTING_COMPLETE.getPrepTime() / 60);
//        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepMinutesObservable), anyInt())).
//                thenReturn(VALID_EXISTING_COMPLETE.getPrepTime() % 60);
//        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookHoursObservable), anyInt())).
//                thenReturn(VALID_EXISTING_COMPLETE.getCookTime() / 60);
//        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookMinutesObservable), anyInt())).
//                thenReturn(VALID_EXISTING_COMPLETE.getCookTime() % 60);
//    }

//    private void whenIntFromObserverMockReturnMaxPrepAndCookTimes() {
//        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepHoursObservable), anyInt())).
//                thenReturn(0, MAX_PREP_TIME / 60);
//        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepMinutesObservable), anyInt())).
//                thenReturn(0, MAX_PREP_TIME % 60);
//        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookHoursObservable), anyInt())).
//                thenReturn(0, MAX_COOK_TIME / 60);
//        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookMinutesObservable), anyInt())).
//                thenReturn(0, MAX_COOK_TIME % 60);
//    }

//    private void whenIntFromObserverMockReturnMaxPrepAndCookTimesPlusOne() {
//        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepHoursObservable), anyInt())).
//                thenReturn(0, MAX_PREP_TIME / 60);
//        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepMinutesObservable), anyInt())).
//                thenReturn(0, MAX_PREP_TIME % 60 + 1);
//        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookHoursObservable), anyInt())).
//                thenReturn(0, MAX_COOK_TIME / 60);
//        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookMinutesObservable), anyInt())).
//                thenReturn(0, MAX_COOK_TIME % 60 + 1);
//    }

//    private void whenIntFromObserverMockReturnRecipeTimesFromAnotherUser() {
//        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookHoursObservable), anyInt())).
//                thenReturn(VALID_FROM_ANOTHER_USER.getCookTime() / 60);
//        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookMinutesObservable), anyInt())).
//                thenReturn(VALID_FROM_ANOTHER_USER.getCookTime() % 60);
//        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepHoursObservable), anyInt())).
//                thenReturn(VALID_FROM_ANOTHER_USER.getPrepTime() / 60);
//        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepMinutesObservable), anyInt())).
//                thenReturn(VALID_FROM_ANOTHER_USER.getPrepTime() % 60);
//    }

    private void whenTimeProviderThenReturnNewEntityCreateDate() {
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                INVALID_NEW_EMPTY.getCreateDate());
    }

    private void whenTimeProviderThenReturnExistingCreateDate() {
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                VALID_EXISTING_COMPLETE.getCreateDate());
    }

    private void whenTimeProviderThenReturnClonedTimes() {
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                VALID_NEW_CLONED.getLastUpdate());
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------

    // endregion helper classes --------------------------------------------------------------------

}