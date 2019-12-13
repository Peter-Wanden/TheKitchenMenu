package com.example.peter.thekitchenmenu.ui.detail.ingredient;

import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.UseCaseTextValidator;
import com.example.peter.thekitchenmenu.testdata.TestDataIngredientEntity;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

import org.junit.*;
import org.mockito.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class IngredientEditorViewModelTest {

    // region constants ----------------------------------------------------------------------------
    private IngredientEntity NEW =
            TestDataIngredientEntity.getNew();
    private IngredientEntity NEW_INVALID_NAME =
            TestDataIngredientEntity.getNewInvalidName();
    private IngredientEntity NEW_VALID_NAME =
            TestDataIngredientEntity.getNewValidName();
    private IngredientEntity NEW_VALID_NAME_INVALID_DESCRIPTION =
            TestDataIngredientEntity.getNewValidNameInvalidDescription();
    private IngredientEntity NEW_INVALID_NAME_VALID_DESCRIPTION =
            TestDataIngredientEntity.getNewInvalidNameValidDescription();
    private IngredientEntity NEW_VALID_NAME_VALID_DESCRIPTION =
            TestDataIngredientEntity.getNewValidNameValidDescription();
    private IngredientEntity VALID_EXISTING_COMPLETE =
            TestDataIngredientEntity.getExistingValidNameValidDescriptionNoConversionFactor();
    private IngredientEntity VALID_EXISTING_INVALID_NAME_UPDATE =
            TestDataIngredientEntity.getExistingUpdatedWithInvalidName();
    private IngredientEntity VALID_EXISTING_VALID_NAME_UPDATE =
            TestDataIngredientEntity.getExistingUpdatedWithValidName();
    private IngredientEntity VALID_EXISTING_INVALID_DESCRIPTION_UPDATE =
            TestDataIngredientEntity.getExistingUpdatedWithInvalidDescription();
    private IngredientEntity VALID_EXISTING_VALID_DESCRIPTION_UPDATE =
            TestDataIngredientEntity.getExistingUpdatedWithValidDescription();
    private IngredientEntity VALID_EXISTING_FROM_ANOTHER_USER =
            TestDataIngredientEntity.getExistingValidNameValidDescriptionFromAnotherUser();

    private static final String DUPLICATE_ERROR_MESSAGE = "DUPLICATE ERROR MESSAGE";
    private static final String TEXT_TOO_SHORT_ERROR_MESSAGE = "TEXT_TOO_SHORT_ERROR_MESSAGE";
    private static final String TEXT_TOO_LONG_ERROR_MESSAGE = "TEXT_TOO_LONG_ERROR_MESSAGE";
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
//    @Mock
//    Resources resourcesMock;
    private ResourcesMock resourcesMock;
    @Mock
    RepositoryIngredient repoMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<IngredientEntity>> repoCallbackCaptor;
    @Mock
    UniqueIdProvider idProviderMock;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    Observer<Integer> integerObserverMock;
    @Mock
    Observer<Boolean> useButtonVisibilityObserverMock;
    @Mock
    AddEditIngredientNavigator navigatorMock;
    @Mock
    IngredientDuplicateChecker duplicateCheckerMock;
    @Captor
    ArgumentCaptor<IngredientDuplicateChecker.DuplicateCallback> duplicateCallbackArgumentCaptor;
    private int[] textLengthValues = {3, 70, 0, 500};
    // endregion helper fields ---------------------------------------------------------------------

    private IngredientEditorViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
//        setupResources();
        resourcesMock = new ResourcesMock();

        SUT = givenViewModel();

        SUT.setNavigator(navigatorMock);
        observeUseButtonLiveData();
    }

    private IngredientEditorViewModel givenViewModel() {
        UseCaseHandler handler = new UseCaseHandler(new UseCaseSchedulerMock());

        UseCaseTextValidator textValidator = new UseCaseTextValidator(
                textLengthValues[0],
                textLengthValues[1],
                textLengthValues[2],
                textLengthValues[3]
        );

        return new IngredientEditorViewModel(
                resourcesMock,
                repoMock,
                handler,
                textValidator,
                idProviderMock,
                timeProviderMock,
                duplicateCheckerMock
        );
    }

    @Test
    public void startNewIngredientId_nothingSetToObservers() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(NEW.getId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(NEW.getCreateDate());
        // Act
        SUT.start();
        // Assert
        assertEquals(NEW.getName(), SUT.nameObservable.get());
        assertEquals(NEW.getDescription(), SUT.descriptionObservable.get());
    }

    @Test
    public void startNewIngredientId_nothingSaved() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(NEW.getId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(NEW.getCreateDate());
        // Act
        SUT.start();
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void startNewIngredientId_activityTitleSetToAddIngredient() {
        // Arrange
        // Act
        SUT.start();
        // Assert
        verify(navigatorMock).setActivityTitle(eq(R.string.activity_title_add_new_ingredient));
    }

    @Test
    public void startNewIngredientId_useButtonNotShown() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(NEW.getId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(NEW.getCreateDate());
        // Act
        SUT.start();
        // Assert
        verify(useButtonVisibilityObserverMock, times((2))).onChanged(false);
    }

    @Test
    public void startNewIngredientId_invalidName_nameErrorMessageSetToObserver() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(NEW_INVALID_NAME.getId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(NEW_INVALID_NAME.getCreateDate());
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_INVALID_NAME.getName());
        // Assert
//        assertEquals(, SUT.nameErrorMessageObservable.get());
    }

    @Test
    public void startNewIngredientId_invalidName_useButtonNotShown() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(NEW_INVALID_NAME.getId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                NEW_INVALID_NAME.getCreateDate());
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
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME.getLastUpdate());
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
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME.getLastUpdate());
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
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME.getLastUpdate());
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_VALID_NAME.getName());
        whenDuplicateNameCheckForNewIngredientReturnNoneFound();
        // Assert
        SUT.useButtonPressed();
        verify(repoMock).save(eq(NEW_VALID_NAME));
    }

    @Test
    public void startNewIngredientId_validNameDuplicate_duplicateNameErrorSetToObservable() {
        // Arrange
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
        whenIdProviderGetIdReturnNewEntityId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
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
        whenIdProviderGetIdReturnNewEntityId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
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
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME_INVALID_DESCRIPTION.getLastUpdate());

        String description = getStringOfExactLength(textLengthValues[3]);
        description += "a";
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_VALID_NAME_INVALID_DESCRIPTION.getName());
        SUT.descriptionObservable.set(description);
        // Assert
        assertEquals(TEXT_TOO_LONG_ERROR_MESSAGE, SUT.descriptionErrorMessageObservable.get());
    }

    @Test
    public void startNewIngredientId_validNameInvalidDescription_useButtonNotShown() {
        // Arrange
        whenIdProviderGetIdReturnNewEntityId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME.getLastUpdate());
        int maxDescriptionLength = textLengthValues[3];
        String invalidDescription = getStringOfExactLength(maxDescriptionLength);
        invalidDescription = lengthenStringByOneCharacter(invalidDescription);
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_VALID_NAME_INVALID_DESCRIPTION.getName());
        SUT.descriptionObservable.set(invalidDescription);
        // Assert
        verify(useButtonVisibilityObserverMock, times((3))).onChanged(false);
    }

    @Test
    public void startNewIngredientId_invalidNameValidDescription_useButtonNotShown() {
        // Arrange
        whenIdProviderGetIdReturnNewEntityId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                NEW.getCreateDate(), NEW.getLastUpdate());

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
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME_VALID_DESCRIPTION.getLastUpdate());

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
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME_VALID_DESCRIPTION.getLastUpdate());

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
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME_VALID_DESCRIPTION.getLastUpdate());
        // Act
        SUT.start();
        SUT.nameObservable.set(NEW_VALID_NAME_VALID_DESCRIPTION.getName());
        SUT.descriptionObservable.set(NEW_VALID_NAME_VALID_DESCRIPTION.getDescription());
        // Assert
        SUT.useButtonPressed();
        verify(repoMock).save(eq(NEW_VALID_NAME_VALID_DESCRIPTION));
    }

    // startExistingId_nameUpdatedToNameInUseThenBackToOriginal_duplicateErrorNotShown
    // startExistingId_nameUpdatedToNameInUseThenBackToOriginal_useButtonNotShown
    // startExistingId_descriptionUpdated_useButtonShown
    // startExistingId_descriptionUpdated_saved

    @Test
    public void startExistingId_validNameValidDescription_activityTitleSetToEdit() {
        // Arrange
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        verify(navigatorMock).setActivityTitle(eq(R.string.activity_title_edit_ingredient));
    }

    @Test
    public void startExistingId_validNameValidDescription_nameSetToObserver() {
        // Arrange

        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(VALID_EXISTING_COMPLETE.getName(), SUT.nameObservable.get());
    }

    @Test
    public void startExistingId_validNameValidDescription_descriptionSetToObserver() {
        // Arrange

        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(VALID_EXISTING_COMPLETE.getDescription(), SUT.descriptionObservable.get());
    }

    @Test
    public void startExistingId_validNameValidDescription_noErrorMessagesSet() {
        // Arrange

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

        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        verify(useButtonVisibilityObserverMock, times((2))).onChanged(eq(false));
    }

    @Test
    public void startExistingId_validNameValidDescription_duplicateErrorNotShown() {
        // Arrange


        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertNull(SUT.nameErrorMessageObservable.get());
    }

    @Test
    public void startExistingId_validNameValidDescriptionNameUpdatedWithInvalidValue_useButtonNotShown() {
        // Arrange


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
        int shortTextMinLength = textLengthValues[0];
        String nameTooShort = getStringOfExactLength(shortTextMinLength);
        nameTooShort = shortenStringByOneCharacter(nameTooShort);
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.nameObservable.set(nameTooShort);
        // Assert
        assertEquals(TEXT_TOO_SHORT_ERROR_MESSAGE, SUT.nameErrorMessageObservable.get());
    }

    @Test
    public void startExistingId_validNameValidDescriptionNameUpdatedWithValidValue_useButtonShown() {
        // Arrange
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
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                VALID_EXISTING_VALID_NAME_UPDATE.getLastUpdate());
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.nameObservable.set(VALID_EXISTING_VALID_NAME_UPDATE.getName());
        whenDuplicateNameCheckForNewIngredientReturnNoneFound();
        // Assert
        verify(useButtonVisibilityObserverMock, times((2))).onChanged(eq(true));
        SUT.useButtonPressed();
        verify(repoMock).save(eq(VALID_EXISTING_VALID_NAME_UPDATE));
    }

    @Test
    public void startExistingId_validNameValidDescriptionNameUpdatedWithValidValue_nameErrorMessageNull() {
        // Arrange


        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
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
        // Arrange
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
        String description = getStringOfExactLength(textLengthValues[3]);
        description = lengthenStringByOneCharacter(description);
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.descriptionObservable.set(description);
        // Assert
        verify(useButtonVisibilityObserverMock, times((3))).onChanged(false);
    }

    @Test
    public void startExistingId_validNameValidDescriptionDescriptionUpdatedWithInvalidValue_descriptionErrorMessageShown() {
        // Arrange
        int maxLengthOfDescriptionString = textLengthValues[3];
        String description = getStringOfExactLength(maxLengthOfDescriptionString);
        description = lengthenStringByOneCharacter(description);

        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.descriptionObservable.set(description);
        // Assert
        assertEquals(TEXT_TOO_LONG_ERROR_MESSAGE, SUT.descriptionErrorMessageObservable.get());
    }

    @Test
    public void startExistingId_validNameValidDescriptionDescriptionUpdatedWithValidValue_useButtonShown() {
        // Arrange


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
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                VALID_EXISTING_VALID_DESCRIPTION_UPDATE.getLastUpdate());
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.descriptionObservable.set(VALID_EXISTING_VALID_DESCRIPTION_UPDATE.getDescription());
        verify(useButtonVisibilityObserverMock).onChanged(eq(true));
        SUT.useButtonPressed();
        // Assert
        verify(repoMock).save(eq(VALID_EXISTING_VALID_DESCRIPTION_UPDATE));
    }

    @Test
    public void startExistingId_validNameValidDescriptionDescriptionUpdatedWithValidValue_descriptionErrorMessageNull() {
        // Arrange


        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
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


        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                VALID_EXISTING_VALID_DESCRIPTION_UPDATE.getLastUpdate());
        // Act
        SUT.start(VALID_EXISTING_FROM_ANOTHER_USER.getId());
        simulateGetValidExistingFromAnotherUserFromDatabase();
        // Assert
        verify(navigatorMock).finishActivity(null);
    }

    // region helper methods -----------------------------------------------------------------------
    private void observeUseButtonLiveData() {
        SUT.showUseButtonLiveData.observeForever(useButtonVisibilityObserverMock);
    }

    private void whenIdProviderGetIdReturnNewEntityId() {
        when(idProviderMock.getUId()).thenReturn(NEW.getId());
    }

    private void simulateGetValidExistingCompleteFromDatabase() {
        verify(repoMock).getById(eq(VALID_EXISTING_COMPLETE.getId()), repoCallbackCaptor.capture());
        repoCallbackCaptor.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE);
    }

    private void simulateGetValidExistingFromAnotherUserFromDatabase() {
        verify(repoMock).getById(eq(VALID_EXISTING_FROM_ANOTHER_USER.getId()),
                repoCallbackCaptor.capture());
        repoCallbackCaptor.getValue().onEntityLoaded(VALID_EXISTING_FROM_ANOTHER_USER);
    }

    private void whenDuplicateNameCheckForNewIngredientReturnNoneFound() {
        verify(duplicateCheckerMock).checkForDuplicatesAndNotify(
                anyString(),
                anyString(),
                duplicateCallbackArgumentCaptor.capture());
        duplicateCallbackArgumentCaptor.getValue().duplicateCheckResult(
                IngredientDuplicateChecker.NO_DUPLICATE_FOUND);
    }

    private String getStringOfExactLength(int length) {
        StringBuilder builder = new StringBuilder();
        String a="a";
        for (int i=0; i<length; i++) {
            builder.append(a);
        }
        return builder.toString();
    }

    private String lengthenStringByOneCharacter(String stringToLengthen) {
        return stringToLengthen += "a";
    }

    private String shortenStringByOneCharacter(String stringToShorten) {
        return stringToShorten.substring(0, stringToShorten.length() -1);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private class ResourcesMock extends Resources {
        public ResourcesMock(){
            super(null, null, null);
        }

        @NonNull
        @Override
        public String getString(int id) throws NotFoundException {
            if (id == R.string.ingredient_name_duplicate_error_message) {
                return DUPLICATE_ERROR_MESSAGE;

            }
            return "";
        }

        @NonNull
        @Override
        public String getString(int id, Object... formatArgs) throws NotFoundException {
            if (id == R.string.input_error_text_too_short) {
                return TEXT_TOO_SHORT_ERROR_MESSAGE;

            } else if (id == R.string.input_error_text_too_long) {
                return TEXT_TOO_LONG_ERROR_MESSAGE;
            }
            return "";
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}