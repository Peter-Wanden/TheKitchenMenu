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
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class IngredientEditorViewModelTest {

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
    Observer<Boolean> useButtonVisibilityObserverMock;
    @Mock
    AddEditIngredientNavigator addEditIngredientNavigatorMock;
    @Mock
    IngredientDuplicateChecker duplicateCheckerMock;
    @Captor
    ArgumentCaptor<IngredientDuplicateChecker.DuplicateCallback> duplicateCallbackArgumentCaptor;
    // endregion helper fields ---------------------------------------------------------------------

    private IngredientEditorViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupResources();

        SUT = new IngredientEditorViewModel(
                resourcesMock,
                dataSourceMock,
                textValidationHandlerMock,
                uniqueIdProviderMock,
                timeProviderMock,
                duplicateCheckerMock);

        SUT.setNavigator(addEditIngredientNavigatorMock);
        observeUseButtonLiveData();
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
    public void startNewIngredientId_useButtonNotShown() {
        // Arrange
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW.getId());
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(NEW.getCreateDate());
        // Act
        SUT.start();
        // Assert
        verify(useButtonVisibilityObserverMock, times((2))).onChanged(false);
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
    public void startNewIngredientId_invalidName_useButtonNotShown() {
        // Arrange
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW_INVALID_NAME.getId());
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                NEW_INVALID_NAME.getCreateDate());
        whenShortTextValidationReturnErrorMessage();
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_INVALID_NAME.getName());
        // Assert
        verify(useButtonVisibilityObserverMock, times((3))).onChanged(eq(false));
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
    public void startNewIngredientId_validName_useButtonShown() {
        // Arrange
        whenIdProviderGetIdReturnNewEntityId();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME.getLastUpdate());
        whenShortTextValidationReturnValidated();
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_VALID_NAME.getName());
        whenDuplicateNameCheckForNewIngredientReturnNoneFound();
        // Assert
        verify(useButtonVisibilityObserverMock, times((2))).onChanged(eq(true));
    }

    @Test
    public void startNewIngredientId_validName_saved() {
        // Arrange
        whenIdProviderGetIdReturnNewEntityId();
        whenShortTextValidationReturnValidated();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME.getLastUpdate());
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_VALID_NAME.getName());
        whenDuplicateNameCheckForNewIngredientReturnNoneFound();
        // Assert
        SUT.useButtonPressed();
        verify(dataSourceMock).save(eq(NEW_VALID_NAME));
    }

    @Test
    public void startNewIngredientId_validNameDuplicate_duplicateNameErrorSetToObservable() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenIdProviderGetIdReturnNewEntityId();
        // Act
        SUT.start();
        SUT.nameObservable.set(VALID_EXISTING_COMPLETE.getName());
        // Assert
        verify(duplicateCheckerMock).checkForDuplicatesAndNotify(
                eq(VALID_EXISTING_COMPLETE.getName()),
                eq(NEW.getId()),
                duplicateCallbackArgumentCaptor.capture());
        duplicateCallbackArgumentCaptor.getValue().duplicateCheckResult(
                VALID_EXISTING_COMPLETE.getId());

        assertEquals(DUPLICATE_ERROR_MESSAGE, SUT.nameErrorMessageObservable.get());
    }

    @Test
    public void startNewIngredientId_validNameDuplicate_useButtonNotShown() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenIdProviderGetIdReturnNewEntityId();
        // Act
        SUT.start();
        SUT.nameObservable.set(VALID_EXISTING_COMPLETE.getName());
        // Assert
        verify(duplicateCheckerMock).checkForDuplicatesAndNotify(
                eq(VALID_EXISTING_COMPLETE.getName()),
                eq(NEW.getId()),
                duplicateCallbackArgumentCaptor.capture());
        duplicateCallbackArgumentCaptor.getValue().duplicateCheckResult(
                VALID_EXISTING_COMPLETE.getId());

        verify(useButtonVisibilityObserverMock, times((3))).onChanged(eq(false));
    }

    @Test
    public void startNewIngredientId_validName_nameInUseErrorNull() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenIdProviderGetIdReturnNewEntityId();
        // Act
        SUT.start();
        SUT.nameObservable.set(VALID_EXISTING_VALID_NAME_UPDATE.getName());
        whenDuplicateNameCheckForNewIngredientReturnNoneFound();
        // Assert
        assertNull(SUT.nameErrorMessageObservable.get());
    }

    @Test
    public void startNewIngredientId_validNameDuplicateNameBackValidName_nameInUseErrorNull() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenIdProviderGetIdReturnNewEntityId();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                NEW_VALID_NAME.getCreateDate(), NEW_VALID_NAME.getLastUpdate());
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_VALID_NAME.getName());
        verify(duplicateCheckerMock).checkForDuplicatesAndNotify(
                eq(NEW_VALID_NAME.getName()), eq(NEW.getId()),
                duplicateCallbackArgumentCaptor.capture());
        duplicateCallbackArgumentCaptor.getValue().duplicateCheckResult(
                IngredientDuplicateChecker.NO_DUPLICATE_FOUND);

        SUT.nameObservable.set(VALID_EXISTING_COMPLETE.getName());
        verify(duplicateCheckerMock).checkForDuplicatesAndNotify(
                eq(VALID_EXISTING_COMPLETE.getName()),
                eq(NEW.getId()),
                duplicateCallbackArgumentCaptor.capture());
        duplicateCallbackArgumentCaptor.getValue().duplicateCheckResult(
                VALID_EXISTING_COMPLETE.getId());

        SUT.nameObservable.set(NEW_VALID_NAME.getName());
        assertNull(SUT.nameErrorMessageObservable.get());
    }

    @Test
    public void startNewIngredientId_validNameDuplicateNameBackValidName_useButtonVisible() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenIdProviderGetIdReturnNewEntityId();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                NEW_VALID_NAME.getCreateDate(), NEW_VALID_NAME.getLastUpdate());
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_VALID_NAME.getName());
        verify(duplicateCheckerMock).checkForDuplicatesAndNotify(
                eq(NEW_VALID_NAME.getName()),
                eq(NEW_VALID_NAME.getId()),
                duplicateCallbackArgumentCaptor.capture());
        duplicateCallbackArgumentCaptor.getValue().duplicateCheckResult(
                IngredientDuplicateChecker.NO_DUPLICATE_FOUND);

        SUT.nameObservable.set(VALID_EXISTING_COMPLETE.getName());
        verify(duplicateCheckerMock).checkForDuplicatesAndNotify(
                eq(VALID_EXISTING_COMPLETE.getName()), eq(NEW_VALID_NAME.getId()),
                duplicateCallbackArgumentCaptor.capture());
        duplicateCallbackArgumentCaptor.getValue().duplicateCheckResult(
                VALID_EXISTING_COMPLETE.getId());

        SUT.nameObservable.set(NEW_VALID_NAME.getName());
        // Assert
        verify(useButtonVisibilityObserverMock, times((4))).onChanged(eq(true));
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
    public void startNewIngredientId_validNameInvalidDescription_useButtonNotShown() {
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
        verify(useButtonVisibilityObserverMock, times((3))).onChanged(false);
    }

    @Test
    public void startNewIngredientId_invalidNameValidDescription_useButtonNotShown() {
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
        verify(useButtonVisibilityObserverMock, times((4))).onChanged(false);
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
    public void startNewIngredientId_validNameValidDescription_useButtonShown() {
        // Arrange
        whenIdProviderGetIdReturnNewEntityId();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME_VALID_DESCRIPTION.getLastUpdate());
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_VALID_NAME_VALID_DESCRIPTION.getName());
        whenDuplicateNameCheckForNewIngredientReturnNoneFound();
        SUT.descriptionObservable.set(NEW_VALID_NAME_VALID_DESCRIPTION.getDescription());
        // Assert
        verify(useButtonVisibilityObserverMock, times((3))).onChanged(eq(true));
    }

    @Test
    public void startNewIngredientId_validNameValidDescription_saved() {
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
        SUT.useButtonPressed();
        verify(dataSourceMock).save(eq(NEW_VALID_NAME_VALID_DESCRIPTION));
    }

    // startExistingId_nameUpdatedToNameInUseThenBackToOriginal_duplicateErrorNotShown
    // startExistingId_nameUpdatedToNameInUseThenBackToOriginal_useButtonNotShown
    // startExistingId_descriptionUpdated_useButtonShown
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
    public void startExistingId_validNameValidDescription_useButtonNotShown() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        verify(useButtonVisibilityObserverMock, times((2))).onChanged(eq(false));
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
    public void startExistingId_validNameValidDescriptionNameUpdatedWithInvalidValue_useButtonNotShown() {
        // Arrange
        whenShortTextValidationReturnErrorMessage();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.nameObservable.set(VALID_EXISTING_INVALID_NAME_UPDATE.getName());
        // Assert
        verify(useButtonVisibilityObserverMock, times((3))).onChanged(eq(false));
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
    public void startExistingId_validNameValidDescriptionNameUpdatedWithValidValue_useButtonShown() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.nameObservable.set(VALID_EXISTING_VALID_NAME_UPDATE.getName());
        whenDuplicateNameCheckForNewIngredientReturnNoneFound();
        // Assert
        verify(useButtonVisibilityObserverMock, times((2))).onChanged(false);
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
        whenDuplicateNameCheckForNewIngredientReturnNoneFound();
        // Assert
        verify(useButtonVisibilityObserverMock, times((2))).onChanged(eq(true));
        SUT.useButtonPressed();
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
    public void startExistingId_validNameValidDescriptionDuplicateName_useButtonNotShown() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.nameObservable.set(VALID_EXISTING_FROM_ANOTHER_USER.getName());

        verify(duplicateCheckerMock).checkForDuplicatesAndNotify(
                eq(VALID_EXISTING_FROM_ANOTHER_USER.getName()),
                eq(VALID_EXISTING_COMPLETE.getId()),
                duplicateCallbackArgumentCaptor.capture());
        duplicateCallbackArgumentCaptor.getValue().duplicateCheckResult(
                VALID_EXISTING_FROM_ANOTHER_USER.getId());
        // Assert
        verify(useButtonVisibilityObserverMock, times((3))).onChanged(false);
    }

    @Test
    public void startExistingId_validNameValidDescriptionDuplicateName_duplicateErrorShown() {
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.nameObservable.set(VALID_EXISTING_FROM_ANOTHER_USER.getName());

        verify(duplicateCheckerMock).checkForDuplicatesAndNotify(
                eq(VALID_EXISTING_FROM_ANOTHER_USER.getName()),
                eq(VALID_EXISTING_COMPLETE.getId()),
                duplicateCallbackArgumentCaptor.capture());
        duplicateCallbackArgumentCaptor.getValue().duplicateCheckResult(
                VALID_EXISTING_FROM_ANOTHER_USER.getId());
        // Assert
        assertEquals(DUPLICATE_ERROR_MESSAGE, SUT.nameErrorMessageObservable.get());
    }

    @Test
    public void startExistingId_validNameValidDescriptionDescriptionUpdatedWithInvalidValue_useButtonNotShown() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnErrorMessage();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.descriptionObservable.set(VALID_EXISTING_INVALID_DESCRIPTION_UPDATE.getName());
        // Assert
        verify(useButtonVisibilityObserverMock, times((3))).onChanged(false);
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
    public void startExistingId_validNameValidDescriptionDescriptionUpdatedWithValidValue_useButtonShown() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.descriptionObservable.set(VALID_EXISTING_VALID_DESCRIPTION_UPDATE.getName());
        // Assert
        verify(useButtonVisibilityObserverMock).onChanged(true);
    }

    @Test
    public void startExistingId_validNameValidDescriptionDescriptionUpdatedWithValidValue_saved() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                VALID_EXISTING_VALID_DESCRIPTION_UPDATE.getLastUpdate());
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.descriptionObservable.set(VALID_EXISTING_VALID_DESCRIPTION_UPDATE.getDescription());
        verify(useButtonVisibilityObserverMock).onChanged(eq(true));
        SUT.useButtonPressed();
        // Assert
//        verify(dataSourceMock).save(eq(VALID_EXISTING_VALID_DESCRIPTION_UPDATE));
    }

    @Test
    public void startExistingId_validNameValidDescriptionDescriptionUpdatedWithValidValue_descriptionErrorMessageNull() {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
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
        verify(addEditIngredientNavigatorMock).finishActivity(null);
    }

    // region helper methods -----------------------------------------------------------------------
    private void observeUseButtonLiveData() {
        SUT.showUseButtonLiveData.observeForever(useButtonVisibilityObserverMock);
    }

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

    private void whenDuplicateNameCheckForNewIngredientReturnNoneFound() {
        verify(duplicateCheckerMock).checkForDuplicatesAndNotify(
                anyString(),
                anyString(),
                duplicateCallbackArgumentCaptor.capture());
        duplicateCallbackArgumentCaptor.getValue().duplicateCheckResult(
                IngredientDuplicateChecker.NO_DUPLICATE_FOUND);
    }

    private void setupResources() {
        when(resourcesMock.getString(R.string.ingredient_name_duplicate_error_message)).
                thenReturn(DUPLICATE_ERROR_MESSAGE);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}