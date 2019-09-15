package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import android.content.res.Resources;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.testdata.RecipeDurationTestData;
import com.example.peter.thekitchenmenu.testdata.RecipeEntityTestData;
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

import static com.example.peter.thekitchenmenu.testdata.RecipeIdentityEntityTestData.*;
import static com.example.peter.thekitchenmenu.testdata.RecipeEntityTestData.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RecipeIdentityViewModelTest {

    // region constants ----------------------------------------------------------------------------
    private static final RecipeIdentityEntity INVALID_NEW_EMPTY_ENTITY = RecipeIdentityEntityTestData.getInvalidNewEmpty();
    private static final String INVALID_NEW_EMPTY_RECIPE_ID = INVALID_NEW_EMPTY_ENTITY.getId();
    private static final String INVALID_NEW_EMPTY_TITLE = INVALID_NEW_EMPTY_ENTITY.getTitle();
    private static final String INVALID_NEW_EMPTY_DESCRIPTION = INVALID_NEW_EMPTY_ENTITY.getDescription();
    private static final int INVALID_NEW_EMPTY_PREP_TIME_HOURS = INVALID_NEW_EMPTY_ENTITY.getPrepTime() / 60;
    private static final int INVALID_NEW_EMPTY_PREP_TIME_MINUTES = INVALID_NEW_EMPTY_ENTITY.getPrepTime() % 60;
    private static final int INVALID_NEW_EMPTY_COOK_TIME_HOURS = INVALID_NEW_EMPTY_ENTITY.getCookTime() / 60;
    private static final int INVALID_NEW_EMPTY_COOK_TIME_MINUTES = INVALID_NEW_EMPTY_ENTITY.getCookTime() % 60;
    private static final long INVALID_NEW_EMPTY_CREATE_DATE = INVALID_NEW_EMPTY_ENTITY.getCreateDate();
    private static final long INVALID_NEW_EMPTY_LAST_UPDATE = INVALID_NEW_EMPTY_ENTITY.getLastUpdate();

    private static final RecipeIdentityEntity INVALID_NEW_ENTITY_TITLE_INVALID_VALUE =
            RecipeIdentityEntityTestData.getInvalidNewTitleUpdatedWithInvalidValue();
    private static final RecipeIdentityEntity INVALID_NEW_ENTITY_TITLE_VALID_VALUE =
            RecipeIdentityEntityTestData.getInvalidNewTitleUpdatedWithInvalidValue();
    private static final RecipeIdentityEntity INVALID_NEW_ENTITY_TITLE_INVALID_DESCRIPTION_VALID =
            RecipeIdentityEntityTestData.getInvalidNewTitleInvalidDescriptionValid();
    private static final RecipeIdentityEntity VALID_NEW_COMPLETE_ENTITY =
            RecipeIdentityEntityTestData.getValidNewComplete();

    private static final RecipeIdentityEntity VALID_EXISTING_ENTITY =
            RecipeIdentityEntityTestData.getValidExistingComplete();
    private static final String VALID_EXISTING_ID = VALID_EXISTING_ENTITY.getId();
    private static final String VALID_EXISTING_TITLE = VALID_EXISTING_ENTITY.getTitle();
    private static final String VALID_EXISTING_DESCRIPTION = VALID_EXISTING_ENTITY.getDescription();
    private static final int VALID_EXISTING_PREP_TIME = VALID_EXISTING_ENTITY.getPrepTime();
    private static final int VALID_EXISTING_COOK_TIME = VALID_EXISTING_ENTITY.getCookTime();
    private static final long VALID_EXISTING_CREATE_DATE = VALID_EXISTING_ENTITY.getCreateDate();
    private static final long VALID_EXISTING_LAST_UPDATE = VALID_EXISTING_ENTITY.getLastUpdate();


    private static final RecipeEntity VALID_EXISTING_RECIPE_ENTITY = getValidExisting();
    private static final String VALID_EXISTING_RECIPE_ID = VALID_EXISTING_RECIPE_ENTITY.getId();

    private static final RecipeEntity VALID_ENTITY_FROM_ANOTHER_USER =
            RecipeEntityTestData.getValidFromAnotherUser();
    private static final String VALID_ID_FROM_ANOTHER_USER = VALID_ENTITY_FROM_ANOTHER_USER.getId();









    private static final String RECIPE_ID_FROM_ANOTHER_USER =
            getValidFromAnotherUser().getId();
    private static final RecipeDurationEntity VALID_RECIPE_DURATION =
            RecipeDurationTestData.getValidExistingComplete();
    private static final String NEW_RECIPE_ID = "newId";
    private static final String VALIDATED = TextValidationHandler.VALIDATED;

    private static final String INVALID_TITLE = "ti";
    private static final String INVALID_DESCRIPTION = "de";
    private static final String ERROR_MESSAGE_TOO_LONG = "error_message_too_long";
    private static final String ERROR_MESSAGE_TOO_SHORT = "error_message_too_short";

    private static final int MAX_PREP_TIME = RecipeDurationTestData.getMaxPrepTime();
    private static final int MAX_COOK_TIME = RecipeDurationTestData.getMaxCookTime();
    private static final String LONG_TEXT_VALIDATION_ERROR = "long_text_validation_error";
    private static final String SHORT_TEXT_VALIDATION_ERROR = "short_text_validation_error";


    private static final RecipeIdentityEntity VALID_IDENTITY_ENTITY_FROM_ANOTHER_USER =
            getValidCompleteFromAnotherUser();
    private static final String VALID_IDENTITY_ID_FROM_ANOTHER_USER =
            VALID_IDENTITY_ENTITY_FROM_ANOTHER_USER.getId();

    private static final RecipeIdentityEntity VALID_CLONED_RECIPE_IDENTITY_ENTITY =
            RecipeIdentityEntityTestData.getValidNewCloned();

    private static final String NEW_IDENTITY_ID = VALID_CLONED_RECIPE_IDENTITY_ENTITY.getId();
    private static final int PREP_TIME_FROM_ANOTHER_USER = VALID_IDENTITY_ENTITY_FROM_ANOTHER_USER.getPrepTime();
    private static final int COOK_TIME_FROM_ANOTHER_USER = VALID_IDENTITY_ENTITY_FROM_ANOTHER_USER.getCookTime();

    private RecipeModelStatus IDENTITY_MODEL_UNCHANGED_INVALID = RecipeValidatorTestData.getUnchangedInvalid();
    private RecipeModelStatus IDENTITY_MODEL_UNCHANGED_VALID = RecipeValidatorTestData.getUnchangedValid();
    private RecipeModelStatus IDENTITY_MODEL_CHANGED_INVALID = RecipeValidatorTestData.getChangedInvalid();
    private RecipeModelStatus IDENTITY_MODEL_CHANGED_VALID = RecipeValidatorTestData.getChangedValid();

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Mock
    Resources resourcesMock;
    @Mock
    TextValidationHandler textValidationHandlerMock;
    @Mock
    ParseIntegerFromObservableHandler intFromObservableMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeIdentityEntity>> getEntityCallbackArgumentCaptor;
    @Mock
    DataSource<RecipeIdentityEntity> identityDataSourceMock;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    UniqueIdProvider uniqueIdProviderMock;
    @Mock
    RecipeValidation.RecipeValidatorModelSubmission modelSubmissionMock;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeIdentityViewModel SUT;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        setupResourceMockReturnValues();
        when(uniqueIdProviderMock.getUId()).thenReturn(VALID_EXISTING_RECIPE_ID);

        SUT = new RecipeIdentityViewModel(
                identityDataSourceMock,
                timeProviderMock,
                resourcesMock,
                textValidationHandlerMock,
                intFromObservableMock);

        SUT.setModelValidationSubmitter(modelSubmissionMock);
    }

    @Test
    public void start_recipeId_databaseCalledWithRecipeId() throws Exception {
        // Arrange
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        // Assert
        verify(identityDataSourceMock).getById(eq(INVALID_NEW_EMPTY_RECIPE_ID),
                getEntityCallbackArgumentCaptor.capture());
    }

    @Test
    public void start_recipeIdWithNoRecipe_nothingSetToObservers() throws Exception {
        // Arrange
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        // Assert
        assertEquals(INVALID_NEW_EMPTY_TITLE, SUT.titleObservable.get());
        assertEquals(INVALID_NEW_EMPTY_DESCRIPTION, SUT.descriptionObservable.get());
        assertEquals(String.valueOf(INVALID_NEW_EMPTY_PREP_TIME_HOURS), SUT.prepHoursObservable.get());
        assertEquals(String.valueOf(INVALID_NEW_EMPTY_PREP_TIME_MINUTES), SUT.prepMinutesObservable.get());
        assertEquals(String.valueOf(INVALID_NEW_EMPTY_COOK_TIME_HOURS), SUT.cookHoursObservable.get());
        assertEquals(String.valueOf(INVALID_NEW_EMPTY_COOK_TIME_MINUTES), SUT.cookMinutesObservable.get());
        assertNull(SUT.titleErrorMessage.get());
        assertNull(SUT.descriptionErrorMessage.get());
        assertNull(SUT.prepTimeErrorMessage.get());
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void start_recipeIdWithNoRecipe_RecipeModelStatusIDENTITY_MODEL_UNCHANGED_INVALID() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        // Assert
        verify(modelSubmissionMock).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(IDENTITY_MODEL_UNCHANGED_INVALID, modelStatus);
    }

    @Test
    public void start_validRecipeId_titleSetToObservable() throws Exception {
        // Arrange
        whenTextValidationValidateTitleAndDescription();
        // Act
        SUT.start(VALID_EXISTING_ID);
        simulateGetValidRecipeIdentityEntityFromDatabase();
        // Assert
        assertEquals(VALID_EXISTING_TITLE, SUT.titleObservable.get());
    }

    @Test
    public void start_validRecipeId_descriptionSetToObservable() throws Exception {
        // Arrange
        whenTextValidationValidateTitleAndDescription();
        // Act
        SUT.start(VALID_EXISTING_ID);
        simulateGetValidRecipeIdentityEntityFromDatabase();
        // Assert
        assertEquals(VALID_EXISTING_DESCRIPTION, SUT.descriptionObservable.get());
    }

    @Test
    public void start_validRecipeId_prepHoursSetToObservable() throws Exception {
        // Arrange
        whenTextValidationValidateTitleAndDescription();
        String prepHours = String.valueOf(VALID_EXISTING_PREP_TIME / 60);
        // Act
        SUT.start(VALID_EXISTING_ID);
        simulateGetValidRecipeIdentityEntityFromDatabase();
        // Assert
        assertEquals(prepHours, SUT.prepHoursObservable.get());
    }

    @Test
    public void start_validRecipeId_prepMinutesSetToObservable() throws Exception {
        // Arrange
        whenTextValidationValidateTitleAndDescription();
        String prepMinutes = String.valueOf(VALID_EXISTING_PREP_TIME % 60);
        // Act
        SUT.start(VALID_EXISTING_ID);
        simulateGetValidRecipeIdentityEntityFromDatabase();
        // Assert
        assertEquals(prepMinutes, SUT.prepMinutesObservable.get());
    }

    @Test
    public void start_validRecipeId_cookHoursSetToObservable() throws Exception {
        // Arrange
        whenTextValidationValidateTitleAndDescription();
        String cookHours = String.valueOf(VALID_EXISTING_COOK_TIME / 60);
        // Act
        SUT.start(VALID_EXISTING_ID);
        simulateGetValidRecipeIdentityEntityFromDatabase();
        // Assert
        assertEquals(cookHours, SUT.cookHoursObservable.get());
    }

    @Test
    public void start_validRecipeId_cookMinutesSetToObservable() throws Exception {
        // Arrange
        whenTextValidationValidateTitleAndDescription();
        String cookMinutes = String.valueOf(VALID_EXISTING_COOK_TIME % 60);
        // Act
        SUT.start(VALID_EXISTING_ID);
        simulateGetValidRecipeIdentityEntityFromDatabase();
        // Assert
        assertEquals(cookMinutes, SUT.cookMinutesObservable.get());
    }

    @Test
    public void start_validRecipeId_RecipeModelStatusIDENTITY_MODEL_UNCHANGED_VALID() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        whenTextValidationValidateTitleAndDescription();
        whenIntFromObserverMockReturnValidRecipeTimes();
        // Act
        SUT.start(VALID_EXISTING_ID);
        // Assert
        simulateGetValidRecipeIdentityEntityFromDatabase();
        verify(modelSubmissionMock).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(IDENTITY_MODEL_UNCHANGED_VALID, modelStatus);
    }

    @Test
    public void startWithClonedModel_oldAndNewRecipeIdSupplied_databaseCalledWithOldId() throws Exception {
        // Arrange
        // Act
        SUT.startByCloningModel(VALID_ID_FROM_ANOTHER_USER,
                INVALID_NEW_EMPTY_RECIPE_ID);
        // Assert
        verify(identityDataSourceMock).getById(eq(VALID_ID_FROM_ANOTHER_USER), eq(SUT));
    }

    @Test
    public void startWithClonedModel_oldAndNewRecipeIdSupplied_oldModelSavedWithNewId() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeIdentityEntity> ac = ArgumentCaptor.forClass(RecipeIdentityEntity.class);
        when(uniqueIdProviderMock.getUId()).thenReturn(NEW_IDENTITY_ID);
        whenTimeProviderMockThenReturnClonedRecipeIdentityEntityTimes();
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        whenIntFromObserverMockReturnRecipeTimesFromAnotherUser();
        // Act
        SUT.startByCloningModel(RECIPE_ID_FROM_ANOTHER_USER, VALID_CLONED_RECIPE_IDENTITY_ENTITY.getId());
        simulateGetValidRecipeIdentityEntityFromAnotherUserFromDatabase();
        // Assert
        verify(identityDataSourceMock).save(eq(VALID_CLONED_RECIPE_IDENTITY_ENTITY));
    }

    @Test
    public void startWithClonedModel_descriptionChanged_modelSavedWithUpdatedDescription() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeIdentityEntity> ac = ArgumentCaptor.forClass(RecipeIdentityEntity.class);
        when(uniqueIdProviderMock.getUId()).thenReturn(
                getValidNewClonedDescriptionUpdatedComplete().getId());
        whenTimeProviderMockThenReturnClonedRecipeIdentityEntityTimes();
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        whenIntFromObserverMockReturnRecipeTimesFromAnotherUser();
        // Act
        SUT.startByCloningModel(
                RECIPE_ID_FROM_ANOTHER_USER,
                getValidNewClonedDescriptionUpdatedComplete().
                        getId());
        simulateGetValidRecipeIdentityEntityFromAnotherUserFromDatabase();
        SUT.descriptionObservable.set(getValidNewClonedDescriptionUpdatedComplete().getDescription());
        // Assert
        verify(identityDataSourceMock, times(2)).save(ac.capture());
        assertEquals(getValidNewClonedDescriptionUpdatedComplete(), ac.getAllValues().get(1));
    }

    @Test
    public void startWithClonedModel_originalModelNotAvailable_newModelCreatedAndSaved() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeIdentityEntity> ac = ArgumentCaptor.forClass(RecipeIdentityEntity.class);
        when(uniqueIdProviderMock.getUId()).thenReturn(
                getInvalidNewEmpty().getId());
        whenShortTextValidationReturnErrorMessage();
        whenLongTextValidationReturnValidated();
        when(intFromObservableMock.parseInt(anyObject(), anyObject(), anyInt())).thenReturn(0);
        whenTimeProviderMockThenReturnClonedRecipeIdentityEntityTimes();
        // Act
        SUT.startByCloningModel(RECIPE_ID_FROM_ANOTHER_USER,
                getInvalidNewEmpty().getId());

        verify(identityDataSourceMock).getById(eq(RECIPE_ID_FROM_ANOTHER_USER),
                getEntityCallbackArgumentCaptor.capture());
        getEntityCallbackArgumentCaptor.getValue().onDataNotAvailable();

        // Assert
        verify(identityDataSourceMock).save(eq(getInvalidNewEmpty()));
    }

    @Test
    public void observableTitleUpdatedWithInvalidValue_allOtherFieldsEmpty_errorMessageObservableUpdatedWithErrorMessage() throws Exception {
        // Arrange
        whenShortTextValidationReturnErrorMessage();
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set("ti");
        // Assert
        assertEquals(SUT.titleErrorMessage.get(), SHORT_TEXT_VALIDATION_ERROR);
    }

    @Test
    public void observableTitleUpdatedWithInvalidValue_allOtherFieldsValid_errorMessageObservableUpdatedWithErrorMessage() throws Exception {
        // Arrange
        whenShortTextValidationReturnErrorMessage();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(VALID_EXISTING_RECIPE_ID);
        simulateGetValidRecipeIdentityEntityFromDatabase();
        SUT.titleObservable.set("ti");
        // Assert
        assertEquals(SUT.titleErrorMessage.get(), SHORT_TEXT_VALIDATION_ERROR);
    }

    @Test
    public void observableTitleUpdatedWithInvalidValue_allOtherFieldsEmpty_RecipeModelStatus_IDENTITY_MODEL_CHANGED_INVALID() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        whenShortTextValidationReturnErrorMessage();
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(INVALID_TITLE);
        // Assert
        verify(modelSubmissionMock, times(2)).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(IDENTITY_MODEL_CHANGED_INVALID, modelStatus);
    }

    @Test
    public void observableTitleUpdatedWithValidValue_allOtherFieldsEmpty_errorMessageObservableUpdatedWithNull() throws Exception {
        // Arrange
        whenShortTextValidationReturnValidated();
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE);
        // Assert
        assertNull(SUT.titleErrorMessage.get());
    }

    @Test
    public void observableTitleUpdatedWithValidValue_allOtherFieldsEmpty_entitySavedToDatabase() throws Exception {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenIdProviderThenReturnExistingRecipeId();
        whenTimeProviderMockThenReturnNewIdentityEntityCreateDate();
        // Act
        SUT.start(INVALID_NEW_ENTITY_TITLE_VALID_VALUE.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(INVALID_NEW_ENTITY_TITLE_VALID_VALUE.getTitle());
        // Assert
        verify(identityDataSourceMock).save(eq(INVALID_NEW_ENTITY_TITLE_VALID_VALUE));
    }

    @Test
    public void observableTitleUpdatedWithValidValue_allOtherFieldsEmpty_RecipeModelStatus_IDENTITY_MODEL_CHANGED_VALID() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        whenShortTextValidationReturnValidated();
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(RecipeIdentityEntityTestData.
                getInvalidNewTitleUpdatedWithInvalidValue().getTitle());
        // Assert
        verify(modelSubmissionMock, times(2)).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(IDENTITY_MODEL_CHANGED_VALID, modelStatus);
    }

    @Test
    public void observableDescriptionUpdatedWithInvalidValue_titleValid_errorMessageObservableUpdatedWithErrorMessage() throws Exception {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnErrorMessage();
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE);
        SUT.descriptionObservable.set(INVALID_DESCRIPTION);
        // Assert
        assertEquals(LONG_TEXT_VALIDATION_ERROR, SUT.descriptionErrorMessage.get());
    }

    @Test
    public void observableDescriptionUpdatedWithInvalidValue_titleValid_entityNotSavedToDatabase() throws Exception {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnErrorMessage();
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE);
        SUT.descriptionObservable.set(INVALID_DESCRIPTION);
        // Assert (only one save for new Identity, one for the valid title, no save for
        // invalid description)
        verify(identityDataSourceMock, times(2)).save(anyObject());
    }

    @Test
    public void observableDescriptionUpdatedWithInvalidValue_titleValid_RecipeModelStatus_IDENTITY_MODEL_CHANGED_INVALID() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnErrorMessage();
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE);
        SUT.descriptionObservable.set(INVALID_DESCRIPTION);
        // Assert
        verify(modelSubmissionMock, times(3)).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(IDENTITY_MODEL_CHANGED_INVALID, modelStatus);
    }

    @Test
    public void observableDescriptionUpdatedWithValidValue_titleValid_errorMessageObservableUpdatedWithNull() throws Exception {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE);
        SUT.descriptionObservable.set(VALID_EXISTING_DESCRIPTION);
        // Assert
        assertNull(SUT.titleErrorMessage.get());
        assertNull(SUT.descriptionErrorMessage.get());
    }

    @Test
    public void observableDescriptionUpdatedWithValidValue_titleValid_entitySavedToDatabase() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeIdentityEntity> ac = ArgumentCaptor.forClass(RecipeIdentityEntity.class);
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        whenTimeProviderThenReturnExistingIdentityEntityCreateDate();
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE);
        SUT.descriptionObservable.set(VALID_EXISTING_DESCRIPTION);
        // Assert
        verify(identityDataSourceMock, times(3)).save(ac.capture());
        List<RecipeIdentityEntity> identityEntities = ac.getAllValues();
        RecipeIdentityEntity identityEntity = identityEntities.get(2);
        assertEquals(VALID_EXISTING_TITLE, identityEntity.getTitle());
        assertEquals(VALID_EXISTING_DESCRIPTION, identityEntity.getDescription());
    }

    @Test
    public void observableDescriptionUpdatedWithValidValue_titleValid_RecipeModelStatus_IDENTITY_MODEL_CHANGED_VALID() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE);
        SUT.descriptionObservable.set(VALID_EXISTING_DESCRIPTION);
        // Assert
        verify(modelSubmissionMock, times(3)).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(IDENTITY_MODEL_CHANGED_VALID, modelStatus);
    }

    @Test
    public void observableDescriptionUpdatedWithValidValue_titleInvalid_entityNotSavedToDatabase() throws Exception {
        ArgumentCaptor<RecipeIdentityEntity> ac = ArgumentCaptor.forClass(RecipeIdentityEntity.class);
        whenShortTextValidationReturnErrorMessage();
        whenLongTextValidationReturnValidated();
        whenTimeProviderThenReturnExistingIdentityEntityCreateDate();
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(INVALID_NEW_ENTITY_TITLE_INVALID_VALUE.getTitle());
        SUT.descriptionObservable.set(INVALID_NEW_ENTITY_TITLE_INVALID_DESCRIPTION_VALID.getDescription());
        // Assert
        verify(identityDataSourceMock).save(ac.capture());
        for (RecipeIdentityEntity identityEntity : ac.getAllValues())
            System.out.println(identityEntity);
    }

    @Test
    public void observablePrepMinutes_validValue_prepTimeErrorMessageObservableNull() throws Exception {
        whenIntFromObserver_thenReturn(VALID_EXISTING_PREP_TIME % 60);
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.prepMinutesObservable.set(String.valueOf(VALID_EXISTING_PREP_TIME % 60));
        // Assert
        assertNull(SUT.prepTimeErrorMessage.get());
    }

    @Test
    public void observablePrepMinutes_invalidValue_prepTimeErrorMessageObservableErrorMessage() throws Exception {
        whenIntFromObserver_thenReturn(MAX_PREP_TIME + 1);
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.prepMinutesObservable.set(String.valueOf(MAX_PREP_TIME + 1));
        // Assert
        assertEquals(ERROR_MESSAGE_TOO_LONG, SUT.prepTimeErrorMessage.get());
    }

    @Test
    public void observablePrepHours_validValue_prepTimeErrorMessageObservableNull() throws Exception {
        whenIntFromObserver_thenReturn(MAX_PREP_TIME / 600);
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.prepHoursObservable.set(String.valueOf(MAX_PREP_TIME / 60));
        // Assert
        assertNull(SUT.prepTimeErrorMessage.get());
    }

    @Test
    public void observablePrepHours_invalidValue_prepTimeErrorMessageObservableErrorMessage() throws Exception {
        whenIntFromObserver_thenReturn(MAX_PREP_TIME / 60 + 1);
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.prepHoursObservable.set(String.valueOf(MAX_PREP_TIME / 60 + 1));
        // Assert
        assertEquals(ERROR_MESSAGE_TOO_LONG, SUT.prepTimeErrorMessage.get());
    }

    @Test
    public void observablePrepHoursMinutes_validPrepTime_prepTimeSaved() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeIdentityEntity> ac = ArgumentCaptor.forClass(RecipeIdentityEntity.class);
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        whenIntFromObserverMockReturnMaxPrepAndCookTimes();
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE); // makes the model valid
        SUT.prepHoursObservable.set(String.valueOf(MAX_PREP_TIME / 60));
        SUT.prepMinutesObservable.set(String.valueOf(MAX_PREP_TIME % 60));
        // Assert
        verify(identityDataSourceMock, times(3)).save(ac.capture());
    }

    @Test
    public void observablePrepHoursMinutes_invalidPrepTime_invalidTimeNotSaved() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeIdentityEntity> ac = ArgumentCaptor.forClass(RecipeIdentityEntity.class);
        whenShortTextValidationReturnValidated();
        whenIntFromObserverMockReturnMaxPrepAndCookTimesPlusOne();
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE); // makes the model valid
        SUT.prepHoursObservable.set(String.valueOf(MAX_PREP_TIME / 60));
        SUT.prepMinutesObservable.set(String.valueOf(MAX_PREP_TIME % 60 + 1));
        // Assert (save title, save valid prep hours, don't save invalid prep time when minutes added)
        verify(identityDataSourceMock, times(3)).save(ac.capture());
        RecipeIdentityEntity identityEntity = ac.getAllValues().get(2);
        assertTrue(identityEntity.getPrepTime() != 6001);
    }

    @Test
    public void observablePrepHoursMinutes_inValidPrepTime_RecipeModelStatus_IDENTITY_MODEL_CHANGED_INVALID() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        whenShortTextValidationReturnValidated();
        whenIntFromObserverMockReturnMaxPrepAndCookTimesPlusOne();
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE); // makes the model valid
        SUT.prepHoursObservable.set(String.valueOf(MAX_PREP_TIME / 60));
        SUT.prepMinutesObservable.set(String.valueOf(MAX_PREP_TIME % 60 + 1));

        verify(modelSubmissionMock, times(4)).submitModelStatus(ac.capture());
        verify(modelSubmissionMock).submitModelStatus(eq(IDENTITY_MODEL_CHANGED_INVALID));
    }

    @Test
    public void observablePrepHoursMinutes_validPrepTime_RecipeModelStatus_IDENTITY_MODEL_CHANGED_VALID() throws Exception {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenIntFromObserverMockReturnMaxPrepAndCookTimes();
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE); // makes the model valid
        SUT.prepHoursObservable.set(String.valueOf(MAX_PREP_TIME / 60));
        SUT.prepMinutesObservable.set(String.valueOf(MAX_PREP_TIME % 60));
        verify(modelSubmissionMock, times(2)).
                submitModelStatus(IDENTITY_MODEL_CHANGED_VALID);
    }

    @Test
    public void observableCookMinutes_validValue_cookTimeErrorMessageObservableNull() throws Exception {
        whenShortTextValidationReturnValidated();
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookMinutesObservable), anyInt())).
                thenReturn(0, MAX_COOK_TIME);
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE); // make the model valid
        SUT.cookMinutesObservable.set(String.valueOf(MAX_COOK_TIME));
        // Assert
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void observableCookMinutes_invalidValue_cookTimeErrorMessageObservableErrorMessage() throws Exception {
        whenShortTextValidationReturnValidated();
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookMinutesObservable), anyInt())).
                thenReturn(0, MAX_COOK_TIME + 1);
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE); // make the model valid
        SUT.cookMinutesObservable.set(String.valueOf(MAX_COOK_TIME + 1));
        // Assert
        assertEquals(ERROR_MESSAGE_TOO_LONG, SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void observableCookHours_validValue_cookTimeErrorMessageObservableNull() throws Exception {
        whenShortTextValidationReturnValidated();
        whenIntFromObserverMockReturnMaxPrepAndCookTimes();
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE); // make the model valid
        SUT.cookHoursObservable.set(String.valueOf(MAX_COOK_TIME / 60));
        // Assert
        assertNull(SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void observableCookHours_invalidValue_cookTimeErrorMessageObservableErrorMessage() throws Exception {
        whenShortTextValidationReturnValidated();
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookHoursObservable), anyInt())).
                thenReturn(MAX_COOK_TIME / 60 + 1);
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE); // make the model valid
        SUT.cookHoursObservable.set(String.valueOf(MAX_COOK_TIME / 60 + 1));
        // Assert
        assertEquals(ERROR_MESSAGE_TOO_LONG, SUT.cookTimeErrorMessage.get());
    }

    @Test
    public void observableCookHoursMinutes_validTime_savedToDatabase() throws Exception {
        // Arrange
        whenShortTextValidationReturnValidated();
        whenIntFromObserverMockReturnMaxPrepAndCookTimes();
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE); // make the model valid
        SUT.cookHoursObservable.set(String.valueOf(MAX_COOK_TIME / 60));
        SUT.cookMinutesObservable.set(String.valueOf(MAX_COOK_TIME % 60));
        // Assert
        verify(identityDataSourceMock, times(3)).save(anyObject());
    }

    @Test
    public void observableCookHoursMinutes_invalidValue_prepTimeErrorMessageObservableErrorString() throws Exception {
        whenShortTextValidationReturnValidated();
        whenIntFromObserverMockReturnMaxPrepAndCookTimesPlusOne();
        // Act
        SUT.start(INVALID_NEW_EMPTY_RECIPE_ID);
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_EXISTING_TITLE); // make the model valid
        SUT.cookHoursObservable.set(String.valueOf(MAX_COOK_TIME / 60));
        SUT.cookMinutesObservable.set(String.valueOf(MAX_COOK_TIME % 60 + 1));
        // Assert
        assertEquals(ERROR_MESSAGE_TOO_LONG, SUT.cookTimeErrorMessage.get());
    }

    @Test // Simulates user input for a complete new recipe IdentityModel
    public void allObservablesUpdatedWithValidData_newRecipeIdSupplied_identityModelSavedToDatabase() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeIdentityEntity> ac = ArgumentCaptor.forClass(RecipeIdentityEntity.class);
        whenShortTextValidationReturnValidated();
        whenLongTextValidationReturnValidated();
        whenIdProviderMockThenReturnNewEmptyDraftRecipeId();
        whenTimeProviderMockThenReturnNewIdentityEntityCreateDate();
        whenIntFromObserverMockReturnValidRecipeTimes();

        // Act
        SUT.start(INVALID_NEW_EMPTY_ENTITY.getId());
        simulateNothingReturnedFromDatabase();
        SUT.titleObservable.set(VALID_NEW_COMPLETE_ENTITY.getTitle());
        SUT.descriptionObservable.set(VALID_NEW_COMPLETE_ENTITY.getDescription());
        SUT.prepHoursObservable.set(String.valueOf(VALID_NEW_COMPLETE_ENTITY.getPrepTime() / 60));
        SUT.prepMinutesObservable.set(String.valueOf(VALID_NEW_COMPLETE_ENTITY.getPrepTime() % 60));
        SUT.cookHoursObservable.set(String.valueOf(VALID_NEW_COMPLETE_ENTITY.getCookTime() / 60));
        SUT.cookMinutesObservable.set(String.valueOf(VALID_NEW_COMPLETE_ENTITY.getCookTime() % 60));
        // Assert
        verify(identityDataSourceMock, times(5)).save(ac.capture());
        List<RecipeIdentityEntity> identityList = ac.getAllValues();
        RecipeIdentityEntity newCompleteIdentityEntity = identityList.get(4);

        assertEquals(VALID_NEW_COMPLETE_ENTITY, newCompleteIdentityEntity);
    }

    @Test(expected = RuntimeException.class)
    public void start_nullRecipe_runtimeExceptionThrown() throws Exception {
        // Arrange
        // Act
        SUT.start(null);
        // Assert
        ArrayList list = new ArrayList();
        RuntimeException exception = (RuntimeException) list.get(0);
        assertEquals(exception.getMessage(), "Recipe id cannot be null");
    }

    // region helper methods -------------------------------------------------------------------
    private void simulateGetValidRecipeIdentityEntityFromDatabase() {
        verify(identityDataSourceMock).getById(eq(
                VALID_EXISTING_ID),
                getEntityCallbackArgumentCaptor.capture());

        getEntityCallbackArgumentCaptor.getValue().onEntityLoaded(
                VALID_EXISTING_ENTITY);
    }

    private void simulateGetValidRecipeIdentityEntityFromAnotherUserFromDatabase() {
        verify(identityDataSourceMock).getById(eq(
                VALID_IDENTITY_ID_FROM_ANOTHER_USER),
                getEntityCallbackArgumentCaptor.capture());

        getEntityCallbackArgumentCaptor.getValue().onEntityLoaded(
                VALID_IDENTITY_ENTITY_FROM_ANOTHER_USER);
    }

    private void whenTextValidationValidateTitleAndDescription() {
        when(textValidationHandlerMock.validateShortText(
                eq(resourcesMock),
                eq(VALID_EXISTING_ENTITY.getTitle()))).
                thenReturn(VALIDATED);

        when(textValidationHandlerMock.validateLongText(
                eq(resourcesMock),
                eq(VALID_EXISTING_ENTITY.getDescription()))).
                thenReturn(VALIDATED);
    }

    private void simulateNothingReturnedFromDatabase() {
        verify(identityDataSourceMock).getById(eq(INVALID_NEW_EMPTY_RECIPE_ID),
                getEntityCallbackArgumentCaptor.capture());
        getEntityCallbackArgumentCaptor.getValue().onDataNotAvailable();
    }

    private void setupResourceMockReturnValues() {
        when(resourcesMock.getInteger(R.integer.recipe_max_cook_time_in_minutes)).thenReturn(MAX_PREP_TIME);
        when(resourcesMock.getInteger(R.integer.recipe_max_prep_time_in_minutes)).thenReturn(MAX_COOK_TIME);
        when(resourcesMock.getString(R.string.input_error_recipe_prep_time_too_long)).thenReturn(ERROR_MESSAGE_TOO_LONG);
        when(resourcesMock.getString(R.string.input_error_recipe_cook_time_too_long)).thenReturn(ERROR_MESSAGE_TOO_LONG);
    }

    private void whenShortTextValidationReturnValidated() {
        when(textValidationHandlerMock.validateShortText(anyObject(), anyString())).
                thenReturn(VALIDATED);
    }

    private void whenShortTextValidationReturnErrorMessage() {
        when(textValidationHandlerMock.validateShortText(anyObject(), anyString())).
                thenReturn(SHORT_TEXT_VALIDATION_ERROR);
    }

    private void whenLongTextValidationReturnValidated() {
        when(textValidationHandlerMock.validateLongText(anyObject(), anyString())).
                thenReturn(VALIDATED);
    }

    private void whenLongTextValidationReturnErrorMessage() {
        when(textValidationHandlerMock.validateLongText(anyObject(), anyString())).
                thenReturn(LONG_TEXT_VALIDATION_ERROR);
    }

    private void whenIntFromObserver_thenReturn(int returnValue) {
        when(intFromObservableMock.parseInt(anyObject(), anyObject(), anyInt())).thenReturn(
                returnValue);
    }

    private void whenIntFromObserverMockReturnValidRecipeTimes() {
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepHoursObservable), anyInt())).
                thenReturn(VALID_EXISTING_PREP_TIME / 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepMinutesObservable), anyInt())).
                thenReturn(VALID_EXISTING_PREP_TIME % 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookHoursObservable), anyInt())).
                thenReturn(VALID_EXISTING_COOK_TIME / 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookMinutesObservable), anyInt())).
                thenReturn(VALID_EXISTING_COOK_TIME % 60);
    }

    private void whenIntFromObserverMockReturnMaxPrepAndCookTimes() {
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepHoursObservable), anyInt())).
                thenReturn(0, MAX_PREP_TIME / 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepMinutesObservable), anyInt())).
                thenReturn(0, MAX_PREP_TIME % 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookHoursObservable), anyInt())).
                thenReturn(0, MAX_COOK_TIME / 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookMinutesObservable), anyInt())).
                thenReturn(0, MAX_COOK_TIME % 60);
    }

    private void whenIntFromObserverMockReturnMaxPrepAndCookTimesPlusOne() {
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepHoursObservable), anyInt())).
                thenReturn(0, MAX_PREP_TIME / 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepMinutesObservable), anyInt())).
                thenReturn(0, MAX_PREP_TIME % 60 + 1);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookHoursObservable), anyInt())).
                thenReturn(0, MAX_COOK_TIME / 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookMinutesObservable), anyInt())).
                thenReturn(0, MAX_COOK_TIME % 60 + 1);
    }

    private void whenIntFromObserverMockReturnRecipeTimesFromAnotherUser() {
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookHoursObservable), anyInt())).
                thenReturn(COOK_TIME_FROM_ANOTHER_USER / 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.cookMinutesObservable), anyInt())).
                thenReturn(COOK_TIME_FROM_ANOTHER_USER % 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepHoursObservable), anyInt())).
                thenReturn(PREP_TIME_FROM_ANOTHER_USER / 60);
        when(intFromObservableMock.parseInt(anyObject(), eq(SUT.prepMinutesObservable), anyInt())).
                thenReturn(PREP_TIME_FROM_ANOTHER_USER % 60);
    }

    private void whenIdProviderMockThenReturnNewEmptyDraftRecipeId() {
        when(uniqueIdProviderMock.getUId()).thenReturn(INVALID_NEW_EMPTY_RECIPE_ID);
    }

    private void whenIdProviderThenReturnExistingRecipeId() {
        when(uniqueIdProviderMock.getUId()).thenReturn(
                VALID_EXISTING_ENTITY.getId());
    }

    private void whenTimeProviderMockThenReturnNewIdentityEntityCreateDate() {
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                INVALID_NEW_EMPTY_ENTITY.getCreateDate());
    }

    private void whenTimeProviderThenReturnExistingIdentityEntityCreateDate() {
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                VALID_EXISTING_ENTITY.getCreateDate());
    }

    private void whenTimeProviderMockThenReturnClonedRecipeIdentityEntityTimes() {
        when(timeProviderMock.getCurrentTimestamp()).thenReturn(
                VALID_CLONED_RECIPE_IDENTITY_ENTITY.getLastUpdate());
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------

    // endregion helper classes --------------------------------------------------------------------

}