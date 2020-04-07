package com.example.peter.thekitchenmenu.ui.detail.ingredient;

import android.content.res.Resources;

import androidx.lifecycle.Observer;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.commonmocks.StringMaker;
import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.primitivemodel.ingredient.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.dataadapter.toprimitive.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.ingredient.RepositoryIngredient;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.Ingredient;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientDuplicateChecker;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.testdata.TestDataIngredientEntity;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.*;
import org.mockito.*;

import javax.annotation.Nonnull;

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
    private ResourcesMock resourcesMock;
    @Mock
    RepositoryIngredient repoMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetPrimitiveCallback<IngredientEntity>> repoCallbackCaptor;
    @Mock
    UniqueIdProvider idProviderMock;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    Observer<Boolean> useButtonVisibilityObserverMock;
    @Mock
    AddEditIngredientNavigator navigatorMock;
    @Mock
    IngredientDuplicateChecker duplicateCheckerMock;
    @Captor
    ArgumentCaptor<IngredientDuplicateChecker.DuplicateCallback> duplicateCallbackCaptor;
    @Captor
    ArgumentCaptor<IngredientEntity> ingredientEntityCaptor;
    private int shortTextMinLength = 3;
    private int shortTextMaxLength = 70;
    private int longTextMinLength = 0;
    private int longTextMaxLength = 500;
    // endregion helper fields ---------------------------------------------------------------------

    private IngredientEditorViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        resourcesMock = new ResourcesMock();

        SUT = givenViewModel();

        SUT.setNavigator(navigatorMock);
        observeUseButtonLiveData();
    }

    private IngredientEditorViewModel givenViewModel() {
        UseCaseHandler handler = new UseCaseHandler(new UseCaseSchedulerMock());

        TextValidator textValidator = new TextValidator.Builder().
                setShortTextMinLength(shortTextMinLength).
                setShortTextMaxLength(shortTextMaxLength).
                setLongTextMinLength(longTextMinLength).
                setLongTextMaxLength(longTextMaxLength).
                build();

        Ingredient ingredient = new Ingredient(
                repoMock,
                idProviderMock,
                timeProviderMock,
                duplicateCheckerMock);

        return new IngredientEditorViewModel(
                resourcesMock,
                handler,
                textValidator,
                ingredient
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
        assertEquals(NEW.getName(), SUT.getName());
        assertEquals(NEW.getDescription(), SUT.getDescription());
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
        SUT.setName(NEW_INVALID_NAME.getName());
        // Assert
        assertEquals(TEXT_TOO_SHORT_ERROR_MESSAGE, SUT.showNameError.get());
    }

    @Test
    public void startNewIngredientId_invalidName_useButtonNotShown() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(NEW_INVALID_NAME.getId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                NEW_INVALID_NAME.getCreateDate());
        // Act
        SUT.start();
        SUT.setName(NEW_INVALID_NAME.getName());
        // Assert
        verify(useButtonVisibilityObserverMock, times((3))).onChanged(eq(false));
    }

    @Test
    public void startNewIngredientId_validName_nameErrorMessageNull() {
        // Arrange
        whenIdProviderGetIdReturnNewId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME.getLastUpdate());
        // Act
        SUT.start();
        SUT.setName(NEW_VALID_NAME.getName());
        // Assert
        assertNull(SUT.showNameError.get());
    }

    @Test
    public void startNewIngredientId_validName_useButtonShown() {
        // Arrange
        whenIdProviderGetIdReturnNewId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME.getLastUpdate());
        // Act
        SUT.start();
        SUT.setName(NEW_VALID_NAME.getName());
        whenDuplicateNameCheckForNewIngredientReturnNoneFound();
        // Assert
        verify(useButtonVisibilityObserverMock).onChanged(eq(true));
    }

    @Test
    public void startNewIngredientId_validName_saved() {
        // Arrange
        whenIdProviderGetIdReturnNewId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME.getLastUpdate());
        // Act
        SUT.start();
        SUT.setName(NEW_VALID_NAME.getName());
        whenDuplicateNameCheckForNewIngredientReturnNoneFound();
        // Assert
        SUT.useButtonPressed();
        verify(repoMock).save(eq(NEW_VALID_NAME));
    }

    @Test
    public void startNewIngredientId_validNameDuplicate_duplicateNameErrorSetToObservable() {
        // Arrange
        whenIdProviderGetIdReturnNewId();
        // Act
        SUT.start();
        SUT.setName(VALID_EXISTING_COMPLETE.getName());
        // Assert
        verify(duplicateCheckerMock).checkForDuplicateAndNotify(
                eq(VALID_EXISTING_COMPLETE.getName()),
                eq(NEW.getId()),
                duplicateCallbackCaptor.capture());

        duplicateCallbackCaptor.getValue().duplicateCheckResult(VALID_EXISTING_COMPLETE.getId());

        assertEquals(DUPLICATE_ERROR_MESSAGE, SUT.showNameError.get());
    }

    @Test
    public void startNewIngredientId_validNameDuplicate_useButtonNotShown() {
        // Arrange
        whenIdProviderGetIdReturnNewId();
        // Act
        SUT.start();
        SUT.setName(VALID_EXISTING_COMPLETE.getName());
        // Assert
        verify(duplicateCheckerMock).checkForDuplicateAndNotify(
                eq(VALID_EXISTING_COMPLETE.getName()),
                eq(NEW.getId()),
                duplicateCallbackCaptor.capture());
        duplicateCallbackCaptor.getValue().duplicateCheckResult(
                VALID_EXISTING_COMPLETE.getId());

        verify(useButtonVisibilityObserverMock, times((3))).onChanged(eq(false));
    }

    @Test
    public void startNewIngredientId_validName_nameInUseErrorNull() {
        // Arrange
        whenIdProviderGetIdReturnNewId();
        // Act
        SUT.start();
        SUT.setName(VALID_EXISTING_VALID_NAME_UPDATE.getName());
        whenDuplicateNameCheckForNewIngredientReturnNoneFound();
        // Assert
        assertNull(SUT.showNameError.get());
    }

    @Test
    public void startNewIngredientId_validNameDuplicateNameBackValidName_nameInUseErrorNull() {
        // Arrange
        whenIdProviderGetIdReturnNewId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                NEW_VALID_NAME.getCreateDate(), NEW_VALID_NAME.getLastUpdate());
        // Act
        SUT.start();
        SUT.setName(NEW_VALID_NAME.getName());
        verify(duplicateCheckerMock).checkForDuplicateAndNotify(
                eq(NEW_VALID_NAME.getName()), eq(NEW.getId()),
                duplicateCallbackCaptor.capture());
        duplicateCallbackCaptor.getValue().duplicateCheckResult(
                IngredientDuplicateChecker.NO_DUPLICATE_FOUND);

        SUT.setName(VALID_EXISTING_COMPLETE.getName());
        verify(duplicateCheckerMock).checkForDuplicateAndNotify(
                eq(VALID_EXISTING_COMPLETE.getName()),
                eq(NEW.getId()),
                duplicateCallbackCaptor.capture());
        duplicateCallbackCaptor.getValue().duplicateCheckResult(
                VALID_EXISTING_COMPLETE.getId());

        SUT.setName(NEW_VALID_NAME.getName());
        assertNull(SUT.showNameError.get());
    }

    @Test
    public void startNewIngredientId_validNameDuplicateNameBackValidName_useButtonVisible() {
        // Arrange
        whenIdProviderGetIdReturnNewId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                NEW_VALID_NAME.getCreateDate(), NEW_VALID_NAME.getLastUpdate());
        // Act
        SUT.start();
        SUT.setName(NEW_VALID_NAME.getName());
        verify(duplicateCheckerMock).checkForDuplicateAndNotify(
                eq(NEW_VALID_NAME.getName()),
                eq(NEW_VALID_NAME.getId()),
                duplicateCallbackCaptor.capture());
        duplicateCallbackCaptor.getValue().duplicateCheckResult(
                IngredientDuplicateChecker.NO_DUPLICATE_FOUND);

        SUT.setName(VALID_EXISTING_COMPLETE.getName());
        verify(duplicateCheckerMock).checkForDuplicateAndNotify(
                eq(VALID_EXISTING_COMPLETE.getName()), eq(NEW_VALID_NAME.getId()),
                duplicateCallbackCaptor.capture());
        duplicateCallbackCaptor.getValue().duplicateCheckResult(
                VALID_EXISTING_COMPLETE.getId());

        SUT.setName(NEW_VALID_NAME.getName());
        // Assert
        verify(useButtonVisibilityObserverMock, times((1))).onChanged(eq(true));
    }

    @Test
    public void startNewIngredientId_validNameInvalidDescription_descriptionErrorMessageSet() {
        // Arrange
        whenIdProviderGetIdReturnNewId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME_INVALID_DESCRIPTION.getLastUpdate());

        String description = new StringMaker().
                makeStringOfExactLength(longTextMaxLength).
                thenAddOneCharacter().
                build();
        // Act
        SUT.start();
        SUT.setName(NEW_VALID_NAME_INVALID_DESCRIPTION.getName());
        SUT.setDescription(description);
        // Assert
        assertEquals(TEXT_TOO_LONG_ERROR_MESSAGE, SUT.showDescriptionError.get());
    }

    @Test
    public void startNewIngredientId_validNameInvalidDescription_useButtonNotShown() {
        // Arrange
        whenIdProviderGetIdReturnNewId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME.getLastUpdate());

        String description = new StringMaker().
                makeStringOfExactLength(longTextMaxLength).
                thenAddOneCharacter().
                build();
        // Act
        SUT.start();
        SUT.setName(NEW_VALID_NAME_INVALID_DESCRIPTION.getName());
        SUT.setDescription(description);
        // Assert
        verify(useButtonVisibilityObserverMock, times((3))).onChanged(false);
    }

    @Test
    public void startNewIngredientId_invalidNameValidDescription_useButtonNotShown() {
        // Arrange
        whenIdProviderGetIdReturnNewId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                NEW.getCreateDate(), NEW.getLastUpdate());

        // Act
        SUT.start();
        SUT.setName(NEW_INVALID_NAME_VALID_DESCRIPTION.getName());
        SUT.setDescription(NEW_INVALID_NAME_VALID_DESCRIPTION.getDescription());
        // Assert
        verify(useButtonVisibilityObserverMock, times((4))).onChanged(false);
    }

    @Test
    public void startNewIngredientId_validNameValidDescription_errorMessageObservablesNull() {
        // Arrange
        whenIdProviderGetIdReturnNewId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME_VALID_DESCRIPTION.getLastUpdate());

        // Act
        SUT.start();
        SUT.setName(NEW_VALID_NAME_VALID_DESCRIPTION.getName());
        SUT.setDescription(NEW_VALID_NAME_VALID_DESCRIPTION.getDescription());
        // Assert
        assertNull(SUT.showNameError.get());
        assertNull(SUT.showDescriptionError.get());
    }

    @Test
    public void startNewIngredientId_validNameValidDescription_useButtonShown() {
        // Arrange
        whenIdProviderGetIdReturnNewId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME_VALID_DESCRIPTION.getLastUpdate());

        // Act
        SUT.start();
        SUT.setName(NEW_VALID_NAME_VALID_DESCRIPTION.getName());
        whenDuplicateNameCheckForNewIngredientReturnNoneFound();
        SUT.setDescription(NEW_VALID_NAME_VALID_DESCRIPTION.getDescription());
        // Assert
        verify(useButtonVisibilityObserverMock, times((2))).onChanged(eq(true));
    }

    @Test
    public void startNewIngredientId_validNameValidDescription_saved() {
        // Arrange
        whenIdProviderGetIdReturnNewId();
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                NEW.getCreateDate(), NEW_VALID_NAME_VALID_DESCRIPTION.getLastUpdate());
        // Act
        SUT.start();
        SUT.setName(NEW_VALID_NAME_VALID_DESCRIPTION.getName());
        whenDuplicateNameCheckForNewIngredientReturnNoneFound();
        SUT.setDescription(NEW_VALID_NAME_VALID_DESCRIPTION.getDescription());

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
        assertEquals(VALID_EXISTING_COMPLETE.getName(), SUT.getName());
    }

    @Test
    public void startExistingId_validNameValidDescription_descriptionSetToObserver() {
        // Arrange

        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertEquals(VALID_EXISTING_COMPLETE.getDescription(), SUT.getDescription());
    }

    @Test
    public void startExistingId_validNameValidDescription_noErrorMessagesSet() {
        // Arrange

        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Assert
        assertNull(SUT.showNameError.get());
        assertNull(SUT.showDescriptionError.get());
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
        assertNull(SUT.showNameError.get());
    }

    @Test
    public void startExistingId_validNameValidDescriptionNameUpdatedWithInvalidValue_useButtonNotShown() {
        // Arrange


        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.setName(VALID_EXISTING_INVALID_NAME_UPDATE.getName());
        // Assert
        verify(useButtonVisibilityObserverMock, times((3))).onChanged(eq(false));
    }

    @Test
    public void startExistingId_validNameValidDescriptionNameUpdatedWithInvalidValue_nameErrorMessageSet() {
        // Arrange
        String nameTooShort = new StringMaker().
                makeStringOfExactLength(shortTextMinLength).
                thenRemoveOneCharacter().
                build();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.setName(nameTooShort);
        // Assert
        assertEquals(TEXT_TOO_SHORT_ERROR_MESSAGE, SUT.showNameError.get());
    }

    @Test
    public void startExistingId_validNameValidDescriptionNameUpdatedWithValidValue_useButtonShown() {
        // Arrange
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.setName(VALID_EXISTING_VALID_NAME_UPDATE.getName());
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
        SUT.setName(VALID_EXISTING_VALID_NAME_UPDATE.getName());
        whenDuplicateNameCheckForNewIngredientReturnNoneFound();
        // Assert
        verify(useButtonVisibilityObserverMock).onChanged(eq(true));
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
        SUT.setName(VALID_EXISTING_VALID_NAME_UPDATE.getName());
        // Assert
        assertNull(SUT.showNameError.get());
    }

    @Test
    public void startExistingId_validNameValidDescriptionDuplicateName_useButtonNotShown() {
        // Arrange
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.setName(VALID_EXISTING_FROM_ANOTHER_USER.getName());

        verify(duplicateCheckerMock).checkForDuplicateAndNotify(
                eq(VALID_EXISTING_FROM_ANOTHER_USER.getName()),
                eq(VALID_EXISTING_COMPLETE.getId()),
                duplicateCallbackCaptor.capture());
        duplicateCallbackCaptor.getValue().duplicateCheckResult(
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
        SUT.setName(VALID_EXISTING_FROM_ANOTHER_USER.getName());

        verify(duplicateCheckerMock).checkForDuplicateAndNotify(
                eq(VALID_EXISTING_FROM_ANOTHER_USER.getName()),
                eq(VALID_EXISTING_COMPLETE.getId()),
                duplicateCallbackCaptor.capture());
        duplicateCallbackCaptor.getValue().duplicateCheckResult(
                VALID_EXISTING_FROM_ANOTHER_USER.getId());
        // Assert
        assertEquals(DUPLICATE_ERROR_MESSAGE, SUT.showNameError.get());
    }

    @Test
    public void startExistingId_validNameValidDescriptionDescriptionUpdatedWithInvalidValue_useButtonNotShown() {
        // Arrange
        String description = new StringMaker().
                makeStringOfExactLength(longTextMaxLength).
                thenAddOneCharacter().
                build();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.setDescription(description);
        // Assert
        verify(useButtonVisibilityObserverMock, times((3))).onChanged(false);
    }

    @Test
    public void startExistingId_validNameValidDescriptionDescriptionUpdatedWithInvalidValue_descriptionErrorMessageShown() {
        // Arrange
        String description = new StringMaker().
                makeStringOfExactLength(longTextMaxLength).
                thenAddOneCharacter().
                build();
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.setDescription(description);
        // Assert
        assertEquals(TEXT_TOO_LONG_ERROR_MESSAGE, SUT.showDescriptionError.get());
    }

    @Test
    public void startExistingId_validNameValidDescriptionDescriptionUpdatedWithValidValue_useButtonShown() {
        // Arrange
        // Act
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        SUT.setDescription(VALID_EXISTING_VALID_DESCRIPTION_UPDATE.getName());
        // Assert
        verify(useButtonVisibilityObserverMock).onChanged(true);
    }

    @Test
    public void startExistingId_validNameValidDescriptionDescriptionUpdatedWithValidValue_saved() {
        // Arrange
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                VALID_EXISTING_VALID_DESCRIPTION_UPDATE.getLastUpdate());
        SUT.start(VALID_EXISTING_COMPLETE.getId());
        simulateGetValidExistingCompleteFromDatabase();
        // Act
        SUT.setDescription(VALID_EXISTING_VALID_DESCRIPTION_UPDATE.getDescription());
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
        SUT.setDescription(VALID_EXISTING_VALID_DESCRIPTION_UPDATE.getDescription());
        // Assert
        assertNull(SUT.showDescriptionError.get());
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
        verify(navigatorMock).finishActivity(eq(""));
    }

    // region helper methods -----------------------------------------------------------------------
    private void observeUseButtonLiveData() {
        SUT.showUseButton.observeForever(useButtonVisibilityObserverMock);
    }

    private void whenIdProviderGetIdReturnNewId() {
        when(idProviderMock.getUId()).thenReturn(NEW.getId());
    }

    private void simulateGetValidExistingCompleteFromDatabase() {
        verify(repoMock).getByDataId(eq(VALID_EXISTING_COMPLETE.getId()), repoCallbackCaptor.capture());
        repoCallbackCaptor.getValue().onEntityLoaded(VALID_EXISTING_COMPLETE);
    }

    private void simulateGetValidExistingFromAnotherUserFromDatabase() {
        verify(repoMock).getByDataId(eq(VALID_EXISTING_FROM_ANOTHER_USER.getId()),
                repoCallbackCaptor.capture());
        repoCallbackCaptor.getValue().onEntityLoaded(VALID_EXISTING_FROM_ANOTHER_USER);
    }

    private void whenDuplicateNameCheckForNewIngredientReturnNoneFound() {
        verify(duplicateCheckerMock).checkForDuplicateAndNotify(
                anyString(),
                anyString(),
                duplicateCallbackCaptor.capture());
        duplicateCallbackCaptor.getValue().duplicateCheckResult(
                IngredientDuplicateChecker.NO_DUPLICATE_FOUND);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private class ResourcesMock extends Resources {
        ResourcesMock() {
            super(null, null, null);
        }

        @Nonnull
        @Override
        public String getString(int id) throws NotFoundException {
            if (id == R.string.ingredient_name_duplicate_error_message) {
                return DUPLICATE_ERROR_MESSAGE;

            }
            return "";
        }

        @Nonnull
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