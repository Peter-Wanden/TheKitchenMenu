package com.example.peter.thekitchenmenu.ui.detail.ingredient;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.testdata.IngredientEntityTestData;
import com.example.peter.thekitchenmenu.testdata.TextValidationData;
import com.example.peter.thekitchenmenu.utils.TextValidationHandler;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

import org.junit.*;
import org.mockito.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class IngredientViewModelTest {

    // region constants ----------------------------------------------------------------------------
    private IngredientEntity NEW =
            IngredientEntityTestData.getNew();
    private IngredientEntity NEW_INVALID_NAME =
            IngredientEntityTestData.getNewInvalidName();
    private IngredientEntity NEW_VALID_NAME =
            IngredientEntityTestData.getNewValidName();
    private IngredientEntity NEW_VALID_NAME_INVALID_DESCRIPTION =
            IngredientEntityTestData.getNewValidNameInvalidDescription();
    private IngredientEntity NEW_INVALID_NAME_VALID_DESCRIPTION =
            IngredientEntityTestData.getNewInvalidNameValidDescription();
    private IngredientEntity NEW_VALID_NAME_VALID_DESCRIPTION =
            IngredientEntityTestData.getNewValidNameValidDescription();
    private IngredientEntity VALID_EXISTING_COMPLETE =
            IngredientEntityTestData.getExistingValidNameValidDescription();
    private IngredientEntity VALID_EXISTING_INVALID_NAME_UPDATE =
            IngredientEntityTestData.getExistingUpdatedWithInvalidName();
    private IngredientEntity VALID_EXISTING_VALID_NAME_UPDATE =
            IngredientEntityTestData.getExistingUpdatedWithValidName();
    private IngredientEntity VALID_EXISTING_INVALID_DESCRIPTION_UPDATE =
            IngredientEntityTestData.getExistingUpdatedWithInvalidDescription();
    private IngredientEntity VALID_EXISTING_VALID_DESCRIPTION_UPDATE =
            IngredientEntityTestData.getExistingUpdatedWithValidDescription();
    private IngredientEntity VALID_EXISTING_FROM_ANOTHER_USER =
            IngredientEntityTestData.getExistingValidNameValidDescriptionFromAnotherUser();
    private static final String DUPLICATE_ERROR_MESSAGE = "DUPLICATE ERROR MESSAGE";
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Mock
    Resources resourcesMock;
    @Mock
    DataSource<IngredientEntity> dataSourceMock;
    @Mock
    TextValidationHandler textValidationHandlerMock;
    @Mock
    UniqueIdProvider uniqueIdProviderMock;
    @Mock
    TimeProvider timeProviderMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<IngredientEntity>> getEntityCallbackArgumentCaptor;
    @Mock
    Observer<Integer> integerObserverMock;
    @Mock
    Observer<Boolean> booleanObserverMock;
    @Mock
    AddEditIngredientNavigator addEditIngredientNavigatorMock;
    @Mock
    IngredientDuplicateChecker duplicateCheckerMock;
    @Captor
    ArgumentCaptor<IngredientDuplicateChecker.DuplicateCallback> duplicateCallbackArgumentCaptor;
    // endregion helper fields ---------------------------------------------------------------------

    private IngredientViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupResources();

        SUT = new IngredientViewModel(
                resourcesMock,
                dataSourceMock,
                textValidationHandlerMock,
                uniqueIdProviderMock,
                timeProviderMock,
                duplicateCheckerMock);

        SUT.setNavigator(addEditIngredientNavigatorMock);
    }

    @Test
    public void startNewIngredientId_nothingSetToObservers() {
        // Arrange
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW.getId());
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(NEW.getCreateDate());
        // Act
        SUT.start();
        // Assert
        assertEquals(NEW.getName(), SUT.nameObservable.get());
        assertEquals(NEW.getDescription(), SUT.descriptionObservable.get());
    }

    @Test
    public void startNewIngredientId_nothingSaved() {
        // Arrange
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW.getId());
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(NEW.getCreateDate());
        // Act
        SUT.start();
        // Assert
        verifyNoMoreInteractions(dataSourceMock);
    }

    @Test
    public void startNewIngredientId_activityTitleSetToAddIngredient() {
        // Arrange
        SUT.getSetActivityTitleEvent().observeForever(integerObserverMock);
        // Act
        SUT.start();
        // Assert
        verify(integerObserverMock).onChanged(R.string.activity_title_add_new_ingredient);
    }

    @Test
    public void startNewIngredientId_doneButtonNotShown() {
        // Arrange
        SUT.showDoneButtonLiveData.observeForever(booleanObserverMock);
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW.getId());
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(NEW.getCreateDate());
        // Act
        SUT.start();
        // Assert
        verify(booleanObserverMock, times(2)).onChanged(false);
    }

    @Test
    public void startNewIngredientId_invalidName_nameErrorMessageSetToObserver() {
        // Arrange
        when(uniqueIdProviderMock.getUId()).thenReturn(
                NEW_INVALID_NAME.getId());
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                NEW_INVALID_NAME.getCreateDate());
        whenShortTextValidationReturnErrorMessage();
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_INVALID_NAME.getName());
        // Assert
        assertEquals(TextValidationData.SHORT_TEXT_VALIDATION_ERROR,
                SUT.nameErrorMessageObservable.get());
    }

    @Test
    public void startNewIngredientId_invalidName_doneButtonNotShown() {
        // Arrange
        when(uniqueIdProviderMock.getUId()).thenReturn(
                NEW_INVALID_NAME.getId());
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                NEW_INVALID_NAME.getCreateDate());
        whenShortTextValidationReturnErrorMessage();
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_INVALID_NAME.getName());
        // Assert
        assertFalse(SUT.showDoneButtonLiveData.getValue());
    }

    @Test
    public void startNewIngredientId_invalidName_nothingSaved() {
        // Arrange
        when(uniqueIdProviderMock.getUId()).thenReturn(
                NEW_INVALID_NAME.getId());
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                NEW_INVALID_NAME.getCreateDate());
        whenShortTextValidationReturnErrorMessage();
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_INVALID_NAME.getName());
        // Assert
        verifyNoMoreInteractions(dataSourceMock);
    }

    @Test
    public void startNewIngredientId_validName_nameErrorMessageNull() {
        // Arrange
        whenIdProviderGetIdReturnNewEntityId();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME.getLastUpdate());
        whenShortTextValidationReturnValidated();
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_VALID_NAME.getName());
        // Assert
        assertNull(SUT.nameErrorMessageObservable.get());
    }

    @Test
    public void startNewIngredientId_validName_ingredientSaved() {
        // Arrange
        whenIdProviderGetIdReturnNewEntityId();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME.getLastUpdate());
        whenShortTextValidationReturnValidated();
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_VALID_NAME.getName());
        whenDuplicateNameCheckForNewIngredientReturnFalse();
        // Assert
        verify(dataSourceMock).save(eq(NEW_VALID_NAME));
    }

    @Test
    public void startNewIngredientId_validName_doneButtonShown() {
        // Arrange
        whenIdProviderGetIdReturnNewEntityId();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME.getLastUpdate());
        whenShortTextValidationReturnValidated();
        SUT.showDoneButtonLiveData.observeForever(booleanObserverMock);
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_VALID_NAME.getName());
        whenDuplicateNameCheckForNewIngredientReturnFalse();
        // Assert
        verify(booleanObserverMock).onChanged(eq(true));
    }

    @Test
    public void startNewIngredientId_validNameAlreadyUsed_nameInUseErrorMessageSetToObservable() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenIdProviderGetIdReturnNewEntityId();
        // Act
        SUT.start();
        SUT.nameObservable.set(VALID_EXISTING_COMPLETE.getName());
        // Assert
        verify(duplicateCheckerMock).checkForDuplicateAndNotify(
                eq(VALID_EXISTING_COMPLETE.getName()),
                duplicateCallbackArgumentCaptor.capture());
        duplicateCallbackArgumentCaptor.getValue().duplicateCheckResult(true);

        assertEquals(DUPLICATE_ERROR_MESSAGE, SUT.nameErrorMessageObservable.get());
    }

    @Test
    public void startNewIngredientId_validNameAlreadyUsed_ingredientNotSaved() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenIdProviderGetIdReturnNewEntityId();
        // Act
        SUT.start();
        SUT.nameObservable.set(VALID_EXISTING_COMPLETE.getName());
        // Assert
        verify(duplicateCheckerMock).checkForDuplicateAndNotify(
                eq(VALID_EXISTING_COMPLETE.getName()),
                duplicateCallbackArgumentCaptor.capture());
        duplicateCallbackArgumentCaptor.getValue().duplicateCheckResult(true);

        verifyNoMoreInteractions(dataSourceMock);
    }

    @Test
    public void startNewIngredientId_validNameAlreadyUsed_doneButtonNotShown() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenIdProviderGetIdReturnNewEntityId();
        // Act
        SUT.start();
        SUT.nameObservable.set(VALID_EXISTING_COMPLETE.getName());
        // Assert
        verify(duplicateCheckerMock).checkForDuplicateAndNotify(
                eq(VALID_EXISTING_COMPLETE.getName()),
                duplicateCallbackArgumentCaptor.capture());
        duplicateCallbackArgumentCaptor.getValue().duplicateCheckResult(true);

        assertFalse(SUT.showDoneButtonLiveData.getValue());
    }

    @Test
    public void startNewIngredientId_validNameNotInUse_nameInUseErrorNull() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenIdProviderGetIdReturnNewEntityId();
        // Act
        SUT.start();
        SUT.nameObservable.set(VALID_EXISTING_VALID_NAME_UPDATE.getName());
        whenDuplicateNameCheckForNewIngredientReturnFalse();
        // Assert
        assertNull(SUT.nameErrorMessageObservable.get());
    }

    @Test
    public void startNewIngredientId_validNameNotInUse_saved() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenIdProviderGetIdReturnNewEntityId();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                NEW_VALID_NAME.getCreateDate(), NEW_VALID_NAME.getLastUpdate());
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_VALID_NAME.getName());
        whenDuplicateNameCheckForNewIngredientReturnFalse();
        // Assert
        verify(dataSourceMock).save(NEW_VALID_NAME);
    }

    @Test
    public void startNewIngredientId_validNameNotInUse_doneButtonShown() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenIdProviderGetIdReturnNewEntityId();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                NEW_VALID_NAME.getCreateDate(), NEW_VALID_NAME.getLastUpdate());
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_VALID_NAME.getName());
        whenDuplicateNameCheckForNewIngredientReturnFalse();
        // Assert
        assertTrue(SUT.showDoneButtonLiveData.getValue());
    }

    @Test
    public void startNewIngredientId_validNameNotInUseNameInUseBackToLastSavedName_nameInUseErrorNull() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenIdProviderGetIdReturnNewEntityId();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                NEW_VALID_NAME.getCreateDate(), NEW_VALID_NAME.getLastUpdate());
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_VALID_NAME.getName());
        verify(duplicateCheckerMock).checkForDuplicateAndNotify(
                eq(NEW_VALID_NAME.getName()), duplicateCallbackArgumentCaptor.capture());
        duplicateCallbackArgumentCaptor.getValue().duplicateCheckResult(false);

        SUT.nameObservable.set(VALID_EXISTING_COMPLETE.getName());
        verify(duplicateCheckerMock).checkForDuplicateAndNotify(
                eq(VALID_EXISTING_COMPLETE.getName()), duplicateCallbackArgumentCaptor.capture());
        duplicateCallbackArgumentCaptor.getValue().duplicateCheckResult(true);

        SUT.nameObservable.set(NEW_VALID_NAME.getName());
        verifyNoMoreInteractions(duplicateCheckerMock);
        assertNull(SUT.nameErrorMessageObservable.get());
    }

    @Test
    public void startNewIngredientId_validNameNotInUseNameInUseBackToLastSavedName_doneButtonShown() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenIdProviderGetIdReturnNewEntityId();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                NEW_VALID_NAME.getCreateDate(), NEW_VALID_NAME.getLastUpdate());
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_VALID_NAME.getName());
        verify(duplicateCheckerMock).checkForDuplicateAndNotify(
                eq(NEW_VALID_NAME.getName()), duplicateCallbackArgumentCaptor.capture());
        duplicateCallbackArgumentCaptor.getValue().duplicateCheckResult(false);

        SUT.nameObservable.set(VALID_EXISTING_COMPLETE.getName());
        verify(duplicateCheckerMock).checkForDuplicateAndNotify(
                eq(VALID_EXISTING_COMPLETE.getName()), duplicateCallbackArgumentCaptor.capture());
        duplicateCallbackArgumentCaptor.getValue().duplicateCheckResult(true);

        SUT.nameObservable.set(NEW_VALID_NAME.getName());
        verifyNoMoreInteractions(duplicateCheckerMock);
        // Assert
        assertTrue(SUT.showDoneButtonLiveData.getValue());
    }

    @Test
    public void startNewIngredientId_validNameInvalidDescription_descriptionErrorMessageSet() {
        // Arrange
        whenIdProviderGetIdReturnNewEntityId();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME_INVALID_DESCRIPTION.getLastUpdate());
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnErrorMessage();
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_VALID_NAME_INVALID_DESCRIPTION.getName());
        SUT.descriptionObservable.set(NEW_VALID_NAME_INVALID_DESCRIPTION.getDescription());
        // Assert
        assertEquals(TextValidationData.LONG_TEXT_VALIDATION_ERROR,
                SUT.descriptionErrorMessageObservable.get());
    }

    @Test
    public void startNewIngredientId_validNameInvalidDescription_descriptionNotSaved() {
        // Arrange
        whenIdProviderGetIdReturnNewEntityId();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME.getLastUpdate());
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnErrorMessage();
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_VALID_NAME_INVALID_DESCRIPTION.getName());
        whenDuplicateNameCheckForNewIngredientReturnFalse();
        SUT.descriptionObservable.set(NEW_VALID_NAME_INVALID_DESCRIPTION.getDescription());
        // Assert
        verify(dataSourceMock).save(eq(NEW_VALID_NAME));
    }

    @Test
    public void startNewIngredientId_validNameInvalidDescription_doneButtonNotShown() {
        // Arrange
        whenIdProviderGetIdReturnNewEntityId();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME.getLastUpdate());
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnErrorMessage();
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_VALID_NAME_INVALID_DESCRIPTION.getName());
        SUT.descriptionObservable.set(NEW_VALID_NAME_INVALID_DESCRIPTION.getDescription());
        // Assert
        assertFalse(SUT.showDoneButtonLiveData.getValue());
    }

    @Test
    public void startNewIngredientId_invalidNameValidDescription_ingredientNotSaved() {
        // Arrange
        whenIdProviderGetIdReturnNewEntityId();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                NEW.getCreateDate(), NEW.getLastUpdate());
        whenShortTextValidationReturnErrorMessage();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_INVALID_NAME_VALID_DESCRIPTION.getName());
        SUT.descriptionObservable.set(NEW_INVALID_NAME_VALID_DESCRIPTION.getDescription());
        // Assert
        verifyNoMoreInteractions(dataSourceMock);
    }

    @Test
    public void startNewIngredientId_validNameValidDescription_errorMessageObservablesNull() {
        // Arrange
        whenIdProviderGetIdReturnNewEntityId();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME_VALID_DESCRIPTION.getLastUpdate());
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_VALID_NAME_VALID_DESCRIPTION.getName());
        SUT.descriptionObservable.set(NEW_VALID_NAME_VALID_DESCRIPTION.getDescription());
        // Assert
        assertNull(SUT.nameErrorMessageObservable.get());
        assertNull(SUT.descriptionErrorMessageObservable.get());
    }

    @Test
    public void startNewIngredientId_validNameValidDescription_ingredientSaved() {
        // Arrange
        whenIdProviderGetIdReturnNewEntityId();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME_VALID_DESCRIPTION.getLastUpdate());
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_VALID_NAME_VALID_DESCRIPTION.getName());
        SUT.descriptionObservable.set(NEW_VALID_NAME_VALID_DESCRIPTION.getDescription());
        // Assert
        verify(dataSourceMock).save(eq(NEW_VALID_NAME_VALID_DESCRIPTION));
    }

    @Test
    public void startNewIngredientId_validNameValidDescription_doneButtonShown() {
        // Arrange
        whenIdProviderGetIdReturnNewEntityId();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME_VALID_DESCRIPTION.getLastUpdate());
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        SUT.showDoneButtonLiveData.observeForever(booleanObserverMock);
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_VALID_NAME_VALID_DESCRIPTION.getName());
        whenDuplicateNameCheckForNewIngredientReturnFalse();
        SUT.descriptionObservable.set(NEW_VALID_NAME_VALID_DESCRIPTION.getDescription());
        // Assert
        verify(booleanObserverMock, times(2)).onChanged(eq(true));
    }

    // startExistingId_nameUpdatedToNameInUseThenBackToOriginal_duplicateErrorNotShown
    // startExistingId_nameUpdatedToNameInUseThenBackToOriginal_doneButtonNotShown
    // startExistingId_descriptionUpdated_doneButtonShown
    // startExistingId_descriptionUpdated_saved

    @Test
    public void startExistingId_validNameValidDescription_activityTitleSetToEdit() {
        // Arrange
        SUT.getSetActivityTitleEvent().observeForever(integerObserverMock);
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        verify(integerObserverMock).onChanged(eq(R.string.activity_title_edit_ingredient));
    }

    @Test
    public void startExistingId_validNameValidDescription_nameSetToObserver() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(VALID_EXISTING_COMPLETE.getName(), SUT.nameObservable.get());
    }

    @Test
    public void startExistingId_validNameValidDescription_descriptionSetToObserver() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(VALID_EXISTING_COMPLETE.getDescription(), SUT.descriptionObservable.get());
    }

    @Test
    public void startExistingId_validNameValidDescription_noErrorMessagesSet() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertNull(SUT.nameErrorMessageObservable.get());
        assertNull(SUT.descriptionErrorMessageObservable.get());
    }

    @Test
    public void startExistingId_validNameValidDescription_doneButtonNotShown() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertFalse(SUT.showDoneButtonLiveData.getValue());
    }

    @Test
    public void startExistingId_validNameValidDescription_duplicateErrorNotShown() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertNull(SUT.nameErrorMessageObservable.get());
    }

    @Test
    public void startExistingId_validNameValidDescription_notSaved() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        verifyNoMoreInteractions(dataSourceMock);
    }

    @Test
    public void startExistingId_validNameValidDescriptionNameUpdatedWithInvalidValue_doneButtonNotShown() {
        // Arrange
        whenShortTextValidationReturnErrorMessage();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.nameObservable.set(VALID_EXISTING_INVALID_NAME_UPDATE.getName());
        // Assert
        assertFalse(SUT.showDoneButtonLiveData.getValue());
    }

    @Test
    public void startExistingId_validNameValidDescriptionNameUpdatedWithInvalidValue_notSaved() {
        // Arrange
        whenShortTextValidationReturnErrorMessage();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.nameObservable.set(VALID_EXISTING_INVALID_NAME_UPDATE.getName());
        // Assert
        verifyNoMoreInteractions(dataSourceMock);
    }

    @Test
    public void startExistingId_validNameValidDescriptionNameUpdatedWithDuplicateValue_doneButtonNotShown() {
        // Arrange
        whenShortTextValidationReturnErrorMessage();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();

        SUT.nameObservable.set(VALID_EXISTING_VALID_NAME_UPDATE.getName());
        verify(duplicateCheckerMock).checkForDuplicateAndNotify(
                eq(VALID_EXISTING_VALID_NAME_UPDATE.getName()),
                duplicateCallbackArgumentCaptor.capture());
        duplicateCallbackArgumentCaptor.getValue().duplicateCheckResult(true);

        // Assert
        assertFalse(SUT.showDoneButtonLiveData.getValue());
    }

    @Test
    public void startExistingId_validNameValidDescriptionNameUpdatedWithDuplicateValue_duplicateErrorShown() {
        // Arrange
        // Act
        // Assert
    }

    @Test
    public void startExistingId_validNameValidDescriptionNameUpdatedWithDuplicateValue_notSaved() {
        // Arrange
        // Act
        // Assert
    }

    @Test
    public void startExistingId_validNameValidDescriptionNameUpdatedWithInvalidValue_nameErrorMessageSet() {
        // Arrange
        whenShortTextValidationReturnErrorMessage();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.nameObservable.set(VALID_EXISTING_INVALID_NAME_UPDATE.getName());
        // Assert
        assertEquals(TextValidationData.SHORT_TEXT_VALIDATION_ERROR,
                SUT.nameErrorMessageObservable.get());
    }

    @Test
    public void startExistingId_validNameValidDescriptionNameUpdatedWithValidValue_doneButtonShown() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.nameObservable.set(VALID_EXISTING_VALID_NAME_UPDATE.getName());
        whenDuplicateNameCheckForNewIngredientReturnFalse();
        // Assert
        assertTrue(SUT.showDoneButtonLiveData.getValue());
    }

    @Test
    public void startExistingId_validNameValidDescriptionNameUpdatedWithValidValue_saved() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                VALID_EXISTING_VALID_NAME_UPDATE.getLastUpdate());
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.nameObservable.set(VALID_EXISTING_VALID_NAME_UPDATE.getName());
        whenDuplicateNameCheckForNewIngredientReturnFalse();
        // Assert
        verify(dataSourceMock).save(eq(VALID_EXISTING_VALID_NAME_UPDATE));
    }

    @Test
    public void startExistingId_validNameValidDescriptionNameUpdatedWithValidValue_nameErrorMessageNull() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                VALID_EXISTING_VALID_NAME_UPDATE.getLastUpdate());
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.nameObservable.set(VALID_EXISTING_VALID_NAME_UPDATE.getName());
        // Assert
        assertNull(SUT.nameErrorMessageObservable.get());
    }

    @Test
    public void startExistingId_validNameValidDescriptionDescriptionUpdatedWithInvalidValue_doneButtonNotShown() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnErrorMessage();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.descriptionObservable.set(VALID_EXISTING_INVALID_DESCRIPTION_UPDATE.getName());
        // Assert
        assertFalse(SUT.showDoneButtonLiveData.getValue());
    }

    @Test
    public void startExistingId_validNameValidDescriptionDescriptionUpdatedWithInvalidValue_notSaved() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnErrorMessage();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.descriptionObservable.set(VALID_EXISTING_INVALID_DESCRIPTION_UPDATE.getName());
        // Assert
        verifyNoMoreInteractions(dataSourceMock);
    }

    @Test
    public void startExistingId_validNameValidDescriptionDescriptionUpdatedWithInvalidValue_descriptionErrorMessageShown() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnErrorMessage();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.descriptionObservable.set(VALID_EXISTING_INVALID_DESCRIPTION_UPDATE.getName());
        // Assert
        assertEquals(TextValidationData.LONG_TEXT_VALIDATION_ERROR,
                SUT.descriptionErrorMessageObservable.get());
    }

    @Test
    public void startExistingId_validNameValidDescriptionDescriptionUpdatedWithValidValue_doneButtonShown() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        SUT.showDoneButtonLiveData.observeForever(booleanObserverMock);
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.descriptionObservable.set(VALID_EXISTING_VALID_DESCRIPTION_UPDATE.getName());
        // Assert
        verify(booleanObserverMock).onChanged(true);
    }

    @Test
    public void startExistingId_validNameValidDescriptionDescriptionUpdatedWithValidValue_saved() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        SUT.showDoneButtonLiveData.observeForever(booleanObserverMock);
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                VALID_EXISTING_VALID_DESCRIPTION_UPDATE.getLastUpdate());
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.descriptionObservable.set(VALID_EXISTING_VALID_DESCRIPTION_UPDATE.getDescription());
        // Assert
        verify(dataSourceMock).save(eq(VALID_EXISTING_VALID_DESCRIPTION_UPDATE));
    }

    @Test
    public void startExistingId_validNameValidDescriptionDescriptionUpdatedWithValidValue_descriptionErrorMessageNull() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        SUT.showDoneButtonLiveData.observeForever(booleanObserverMock);
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                VALID_EXISTING_VALID_DESCRIPTION_UPDATE.getLastUpdate());
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.descriptionObservable.set(VALID_EXISTING_VALID_DESCRIPTION_UPDATE.getDescription());
        // Assert
        assertNull(SUT.descriptionErrorMessageObservable.get());
    }

    @Test
    public void startExistingId_validNameValidDescriptionFromAnotherUser_callFinishImmediately() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                VALID_EXISTING_VALID_DESCRIPTION_UPDATE.getLastUpdate());
        // Act
        SUT.start(VALID_EXISTING_FROM_ANOTHER_USER.getId());
        simulateGetValidExistingFromAnotherUserFromDatabase();
        // Assert
        verify(addEditIngredientNavigatorMock).finishActivity();
    }

    // region helper methods -----------------------------------------------------------------------
    private void whenShortTextValidationReturnErrorMessage() {
        when(textValidationHandlerMock.validateShortText(anyObject(), anyString())).
                thenReturn(TextValidationData.SHORT_TEXT_VALIDATION_ERROR);
    }

    private void whenShortTextValidationReturnValidated() {
        when(textValidationHandlerMock.validateShortText(anyObject(), anyString())).
                thenReturn(TextValidationHandler.VALIDATED);
    }

    private void whenLongTextValidationReturnErrorMessage() {
        when(textValidationHandlerMock.validateLongText(anyObject(), anyString())).
                thenReturn(TextValidationData.LONG_TEXT_VALIDATION_ERROR);
    }

    private void whenLongTextValidationReturnValidated() {
        when(textValidationHandlerMock.validateLongText(anyObject(), anyString())).
                thenReturn(TextValidationHandler.VALIDATED);
    }

    private void whenIdProviderGetIdReturnNewEntityId() {
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW.getId());
    }

    private void simulateGetValidExistingCompleteFromDatabase() {
        verify(dataSourceMock).getById(eq(
                VALID_EXISTING_COMPLETE.getId()),
                getEntityCallbackArgumentCaptor.capture());

        getEntityCallbackArgumentCaptor.getValue().onEntityLoaded(
                VALID_EXISTING_COMPLETE);
    }

    private void simulateGetValidExistingFromAnotherUserFromDatabase() {
        verify(dataSourceMock).getById(eq(
                VALID_EXISTING_FROM_ANOTHER_USER.getId()),
                getEntityCallbackArgumentCaptor.capture());

        getEntityCallbackArgumentCaptor.getValue().onEntityLoaded(
                VALID_EXISTING_FROM_ANOTHER_USER);
    }

    private void whenDuplicateNameCheckForNewIngredientReturnFalse() {
        verify(duplicateCheckerMock).checkForDuplicateAndNotify(
                anyString(), duplicateCallbackArgumentCaptor.capture());
        duplicateCallbackArgumentCaptor.getValue().duplicateCheckResult(false);
    }

    private void whenDuplicateNameCheckForNewIngredientReturnTrue() {
        verify(duplicateCheckerMock).checkForDuplicateAndNotify(
                anyString(), duplicateCallbackArgumentCaptor.capture());
        duplicateCallbackArgumentCaptor.getValue().duplicateCheckResult(true);
    }

    private void setupResources() {
        when(resourcesMock.getString(R.string.ingredient_name_duplicate_error_message)).
                thenReturn(DUPLICATE_ERROR_MESSAGE);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}