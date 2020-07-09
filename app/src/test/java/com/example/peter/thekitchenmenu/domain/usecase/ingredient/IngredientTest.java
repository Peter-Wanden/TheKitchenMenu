package com.example.peter.thekitchenmenu.domain.usecase.ingredient;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.ingredient.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.ingredient.TestDataIngredient;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidatorTest;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class IngredientTest {

    private static final String TAG = "tkm-" + IngredientTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    public static final int NAME_MIN_LENGTH = TextValidatorTest.SHORT_TEXT_MIN_LENGTH;
    public static final int NAME_MAX_LENGTH = TextValidatorTest.SHORT_TEXT_MAX_LENGTH;
//    public static final int DESCRIPTION_MIN_LENGTH = TextValidatorTest.LONG_TEXT_MIN_LENGTH;
    public static final int DESCRIPTION_MAX_LENGTH = TextValidatorTest.LONG_TEXT_MAX_LENGTH;
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RepositoryIngredient repoMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<IngredientPersistenceModel>> repoCallbackCaptor;
    @Mock
    UniqueIdProvider idProviderMock;
    @Mock
    IngredientDuplicateChecker duplicateCheckerMock;
    @Mock
    TimeProvider timeProviderMock;
    @Captor
    ArgumentCaptor<IngredientDuplicateChecker.DuplicateCallback> duplicateCallbackCaptor;

    private UseCaseHandler handler;
    private GetDomainModelCallbackClient callbackClient;
    private int expectedNoOfFailReasons = 1;
    private int expectedNoOfDuplicateChecks = 1;

    // endregion helper fields ---------------------------------------------------------------------

    private Ingredient SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenSystemUnderTest();
    }

    private Ingredient givenSystemUnderTest() {
        handler = new UseCaseHandler(new UseCaseSchedulerMock());
        callbackClient = new GetDomainModelCallbackClient();
        expectedNoOfDuplicateChecks = 1;
        expectedNoOfFailReasons = 1;

        TextValidator textValidator = new TextValidator.Builder().
                setShortTextMinLength(TextValidatorTest.SHORT_TEXT_MIN_LENGTH).
                setShortTextMaxLength(TextValidatorTest.SHORT_TEXT_MAX_LENGTH).
                setLongTextMinLength(TextValidatorTest.LONG_TEXT_MIN_LENGTH).
                setLongTextMaxLength(TextValidatorTest.LONG_TEXT_MAX_LENGTH).
                build();

        return new Ingredient(
                repoMock,
                idProviderMock,
                timeProviderMock,
                duplicateCheckerMock,
                textValidator
        );
    }

    @Test
    public void newRequest_stateINVALID_UNCHANGED_failReasonDATA_UNAVAILABLE() {
        // Arrange
        IngredientPersistenceModel modelUnderTest = TestDataIngredient.getInvalidNewEmpty();
        // Act
        simulateNewInitialisationRequest(modelUnderTest);
        // Assert
        assertEquals(
                UseCaseMetadata.ComponentState.INVALID_UNCHANGED,
                callbackClient.onErrorResponse.getMetadata().getComponentState()
        );

        assertTrue(
                callbackClient.onErrorResponse.
                        getMetadata().
                        getFailReasons().
                        contains(CommonFailReason.DATA_UNAVAILABLE)
        );
    }

    @Test
    public void newRequest_nameTooLongDescriptionTooLong_stateINVALID_CHANGED() {
        // Arrange
        IngredientPersistenceModel modelUnderTest = TestDataIngredient.
                getInvalidNewNameTooLongDescriptionTooLong();

        simulateNewInitialisationRequest(modelUnderTest);

        IngredientRequest.Model model = new IngredientRequest.Model.Builder().
                getDefault().
                setName(modelUnderTest.getName()).
                setDescription(modelUnderTest.getDescription()).
                build();

        IngredientRequest request = new IngredientRequest.Builder().
                setDataId(callbackClient.onErrorResponse.getDataId()).
                setDomainId(callbackClient.onErrorResponse.getDomainId()).
                setDomainModel(model).
                build();
        // Act
        handler.executeAsync(SUT, request, callbackClient);
        // Assert
        assertNoDuplicateFound(modelUnderTest);

        assertEquals(
                UseCaseMetadata.ComponentState.INVALID_CHANGED,
                callbackClient.onErrorResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void newRequest_nameTooLongDescriptionTooLong_failReasonsNAME_TOO_LONG_DESCRIPTION_TOO_LONG() {
        // Arrange
        IngredientPersistenceModel modelUnderTest = TestDataIngredient.
                getInvalidNewNameTooLongDescriptionTooLong();
        expectedNoOfDuplicateChecks = 1;
        expectedNoOfFailReasons = 2;

        simulateNewInitialisationRequest(modelUnderTest);

        IngredientRequest.Model model = new IngredientRequest.Model.Builder().
                getDefault().
                setName(modelUnderTest.getName()).
                setDescription(modelUnderTest.getDescription()).
                build();

        IngredientRequest request = new IngredientRequest.Builder().
                setDataId(callbackClient.onErrorResponse.getDataId()).
                setDomainId(callbackClient.onErrorResponse.getDomainId()).
                setDomainModel(model).
                build();
        // Act
        handler.executeAsync(SUT, request, callbackClient);
        // Assert
        assertNoDuplicateFound(modelUnderTest);

        List<FailReasons> failReasons = callbackClient.onErrorResponse.
                getMetadata().
                getFailReasons();

        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(Ingredient.FailReason.NAME_TOO_LONG));
        assertTrue(failReasons.contains(Ingredient.FailReason.DESCRIPTION_TOO_LONG));
    }

    @Test
    public void newRequest_nameTooShortDescriptionValid_stateINVALID_CHANGED() {
        // Arrange
        IngredientPersistenceModel modelUnderTest = TestDataIngredient.
                getInvalidNewNameTooShortDescriptionValid();
        expectedNoOfFailReasons = 1;

        simulateNewInitialisationRequest(modelUnderTest);

        IngredientRequest.Model model = new IngredientRequest.Model.Builder().
                getDefault().
                setName(modelUnderTest.getName()).
                setDescription(modelUnderTest.getDescription()).
                build();

        IngredientRequest request = new IngredientRequest.Builder().
                setDataId(callbackClient.onErrorResponse.getDataId()).
                setDomainId(callbackClient.onErrorResponse.getDomainId()).
                setDomainModel(model).
                build();
        // Act
        handler.executeAsync(SUT, request, callbackClient);
        // Assert
        assertNoDuplicateFound(modelUnderTest);

        assertEquals(
                UseCaseMetadata.ComponentState.INVALID_CHANGED,
                callbackClient.onErrorResponse.getMetadata().getComponentState()
        );

        List<FailReasons> failReasons = callbackClient.onErrorResponse.
                getMetadata().
                getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(Ingredient.FailReason.NAME_TOO_SHORT));
    }

    @Test
    public void newRequest_nameValid_valuesPersisted() {
        // Arrange
        IngredientPersistenceModel modelUnderTest = TestDataIngredient.
                getValidNewNameValid();

        simulateNewInitialisationRequest(modelUnderTest);

        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());

        IngredientRequest.Model model = new IngredientRequest.Model.Builder().
                getDefault().
                setName(modelUnderTest.getName()).
                build();

        IngredientRequest request = new IngredientRequest.Builder().
                setDataId(modelUnderTest.getDataId()).
                setDomainId(modelUnderTest.getDomainId()).
                setDomainModel(model).
                build();

        // Act
        handler.executeAsync(SUT, request, callbackClient);
        // Assert
        assertNoDuplicateFound(modelUnderTest);

        verify(repoMock).save(modelUnderTest);
    }

    @Test
    public void newRequest_nameValid_state_VALID_CHANGED() {
        // Arrange
        IngredientPersistenceModel modelUnderTest = TestDataIngredient.
                getValidNewNameValid();
        expectedNoOfFailReasons = 1;

        simulateNewInitialisationRequest(modelUnderTest);

        IngredientRequest.Model model = new IngredientRequest.Model.Builder().
                getDefault().
                setName(modelUnderTest.getName()).
                build();

        IngredientRequest request = new IngredientRequest.Builder().
                setDataId(modelUnderTest.getDataId()).
                setDomainId(modelUnderTest.getDomainId()).
                setDomainModel(model).
                build();

        //
        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());

        // Act
        handler.executeAsync(SUT, request, callbackClient);
        // Assert
        assertNoDuplicateFound(modelUnderTest);

        List<FailReasons> failReasons = callbackClient.onSuccessResponse.
                getMetadata().
                getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(CommonFailReason.NONE));
        assertEquals(
                UseCaseMetadata.ComponentState.VALID_CHANGED,
                callbackClient.onSuccessResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void newRequest_nameValidDescriptionValid_valuesPersisted() {
        // Arrange
        IngredientPersistenceModel modelUnderTest = TestDataIngredient.
                getValidNewNameValidDescriptionValid();

        simulateNewInitialisationRequest(modelUnderTest);

        IngredientRequest.Model model = new IngredientRequest.Model.Builder().
                getDefault().
                setName(modelUnderTest.getName()).
                setDescription(modelUnderTest.getDescription()).
                build();

        IngredientRequest request = new IngredientRequest.Builder().
                setDataId(modelUnderTest.getDataId()).
                setDomainId(modelUnderTest.getDomainId()).
                setDomainModel(model).
                build();

        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());

        // Act
        handler.executeAsync(SUT, request, callbackClient);
        // Assert
        assertNoDuplicateFound(modelUnderTest);

        verify(repoMock).save(eq(modelUnderTest));
    }

    @Test
    public void newRequest_titleValid_then_descriptionValid_stateVALID_CHANGED() {
        // Arrange
        IngredientPersistenceModel modelUnderTest = TestDataIngredient.
                getValidNewNameValidDescriptionValid();

        // Request 1: initialisation
        simulateNewInitialisationRequest(modelUnderTest);

        // Request 2: add valid title
        IngredientRequest.Model nameModel = new IngredientRequest.Model.Builder().
                getDefault().
                setName(modelUnderTest.getName()).
                build();

        IngredientRequest titleRequest = new IngredientRequest.Builder().
                setDataId(modelUnderTest.getDataId()).
                setDomainId(modelUnderTest.getDomainId()).
                setDomainModel(nameModel).
                build();

        // new persistence model created, requires new data id and date stamp
        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());

        // Act
        handler.executeAsync(SUT, titleRequest, callbackClient);
        // Assert
        expectedNoOfDuplicateChecks = 1; // for name
        assertNoDuplicateFound(modelUnderTest);

        // Arrange
        // Request 3: add valid description
        IngredientRequest.Model descModel = new IngredientRequest.Model.Builder().
                getDefault().
                setName(callbackClient.onSuccessResponse.getDomainModel().getName()).
                setDescription(modelUnderTest.getDescription()).
                build();
        IngredientRequest descRequest = new IngredientRequest.Builder().
                setDataId(callbackClient.onSuccessResponse.getDataId()).
                setDomainId(callbackClient.onSuccessResponse.getDomainId()).
                setDomainModel(descModel).
                build();

        // second persistence model created, new data Id and timestamp required
        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());

        // Act
        handler.executeAsync(SUT, descRequest, callbackClient);

        // Assert
        expectedNoOfDuplicateChecks = 2; // for name and description
        assertNoDuplicateFound(modelUnderTest);
        // Assert model saved
        verify(repoMock).save(eq(modelUnderTest));
    }

    @Test
    public void existingRequest_nameValidDescriptionValid_stateVALID_UNCHANGED() {
        // Arrange
        IngredientPersistenceModel modelUnderTest = TestDataIngredient.
                getValidExistingNameValidDescriptionValid();

        // Act
        simulateExistingInitialisationRequest(modelUnderTest);

        // Assert metadata
        assertEquals(
                modelUnderTest.getDomainId(),
                callbackClient.onSuccessResponse.getDomainId()
        );
        assertEquals(
                modelUnderTest.getDataId(),
                callbackClient.onSuccessResponse.getDataId()
        );

        UseCaseMetadataModel metadata = callbackClient.onSuccessResponse.getMetadata();
        assertEquals(
                UseCaseMetadata.ComponentState.VALID_UNCHANGED,
                metadata.getComponentState()
        );
        assertTrue(metadata.getFailReasons().contains(CommonFailReason.NONE)
        );
        assertEquals(
                modelUnderTest.getCreateDate(),
                metadata.getCreateDate()
        );
        assertEquals(
                modelUnderTest.getLastUpdate(),
                metadata.getLasUpdate()
        );
        assertEquals(
                modelUnderTest.getCreatedBy(),
                metadata.getCreatedBy()
        );

        // Assert domain data Model
        IngredientResponse.Model model = callbackClient.onSuccessResponse.getDomainModel();
        assertEquals(
                modelUnderTest.getName(),
                model.getName()
        );
        assertEquals(
                modelUnderTest.getDescription(),
                model.getDescription()
        );
        assertEquals(
                modelUnderTest.getConversionFactor(),
                model.getConversionFactor(),
                0
        );
    }

    @Test
    public void existingRequest_nameTooShort_stateINVALID_UNCHANGED() {
        // Arrange
        IngredientPersistenceModel modelUnderTest = TestDataIngredient.
                getInvalidExistingNameTooShort();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        assertEquals(
                UseCaseMetadata.ComponentState.INVALID_UNCHANGED,
                callbackClient.onErrorResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void existingRequest_nameTooShort_failReasonsNAME_TOO_SHORT() {
        // Arrange
        IngredientPersistenceModel modelUnderTest = TestDataIngredient.
                getInvalidExistingNameTooShort();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        assertTrue(
                callbackClient.onErrorResponse.
                        getMetadata().
                        getFailReasons().
                        contains(Ingredient.FailReason.NAME_TOO_SHORT)
        );
    }

    @Test
    public void existingRequest_nameTooLong_stateINVALID_UNCHANGED() {
        // Arrange
        IngredientPersistenceModel modelUnderTest = TestDataIngredient.
                getInvalidExistingNameTooLong();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        assertEquals(
                UseCaseMetadata.ComponentState.INVALID_UNCHANGED,
                callbackClient.onErrorResponse.getMetadata().getComponentState()
        );
        assertTrue(
                callbackClient.onErrorResponse.getMetadata().getFailReasons().
                        contains(Ingredient.FailReason.NAME_TOO_LONG)
        );
    }

    @Test
    public void existingRequest_nameValidDescriptionTooLong_stateINVALID_UNCHANGED() {
        // Arrange
        IngredientPersistenceModel modelUnderTest = TestDataIngredient.
                getInvalidExistingNameValidDescriptionTooLong();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        assertEquals(
                UseCaseMetadata.ComponentState.INVALID_UNCHANGED,
                callbackClient.onErrorResponse.getMetadata().getComponentState()
        );
        assertTrue(
                callbackClient.onErrorResponse.getMetadata().getFailReasons().
                        contains(Ingredient.FailReason.DESCRIPTION_TOO_LONG)
        );
    }

    @Test
    public void existingRequest_nameTooShortDescriptionTooLong_INVALID_UNCHANGED() {
        // Arrange
        IngredientPersistenceModel modelUnderTest = TestDataIngredient.
                getInvalidExistingNameTooShortDescriptionTooLong();
        expectedNoOfFailReasons = 2;
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        assertEquals(
                UseCaseMetadata.ComponentState.INVALID_UNCHANGED,
                callbackClient.onErrorResponse.getMetadata().getComponentState()
        );

        List<FailReasons> failReasons = callbackClient.onErrorResponse.
                getMetadata().
                getFailReasons();

        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(Ingredient.FailReason.NAME_TOO_SHORT));
        assertTrue(failReasons.contains(Ingredient.FailReason.DESCRIPTION_TOO_LONG));
    }

    @Test
    public void existingRequest_titleValid_stateVALID_UNCHANGED() {
        // Arrange
        IngredientPersistenceModel modelUnderTest = TestDataIngredient.
                getValidExistingNameValid();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        assertEquals(
                UseCaseMetadata.ComponentState.VALID_UNCHANGED,
                callbackClient.onSuccessResponse.getMetadata().getComponentState()
        );
        assertTrue(
                callbackClient.onSuccessResponse.getMetadata().getFailReasons().
                        contains(CommonFailReason.NONE)
        );
    }

    // region helper methods -----------------------------------------------------------------------
    private void simulateNewInitialisationRequest(IngredientPersistenceModel modelUnderTest) {
        // Arrange - data id requested when creating a new persistence model
        when(idProviderMock.getUId()).thenReturn(TestDataIngredient.
                getInvalidNewEmpty().getDataId());
        // create date and last update requested when creating a new persistence model
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(TestDataIngredient.
                getInvalidNewEmpty().getCreateDate());

        IngredientRequest initialisationRequest = new IngredientRequest.Builder().
                getDefault().
                setDomainId(modelUnderTest.getDomainId()).
                build();

        // Act
        handler.executeAsync(SUT, initialisationRequest, callbackClient);
        // Assert repo called, no model found, return model unavailable
        verify(repoMock).getActiveByDomainId(
                eq(modelUnderTest.getDomainId()),
                repoCallbackCaptor.capture()
        );
        repoCallbackCaptor.getValue().onPersistenceModelUnavailable();
    }

    private void simulateExistingInitialisationRequest(IngredientPersistenceModel modelUnderTest) {
        // Arrange
        IngredientRequest initialisationRequest = new IngredientRequest.Builder().
                getDefault().
                setDomainId(modelUnderTest.getDomainId()).
                build();

        handler.executeAsync(SUT, initialisationRequest, callbackClient);
        // Assert
        verify(repoMock).getActiveByDomainId(eq(modelUnderTest.getDomainId()),
                repoCallbackCaptor.capture()
        );
        repoCallbackCaptor.getValue().onPersistenceModelLoaded(modelUnderTest);
    }

    private void assertNoDuplicateFound(IngredientPersistenceModel modelUnderTest) {
        verify(duplicateCheckerMock, times(expectedNoOfDuplicateChecks)).
                checkForDuplicateAndNotify(
                        eq(modelUnderTest.getName()),
                        eq(modelUnderTest.getDomainId()),
                        duplicateCallbackCaptor.capture()
                );
        duplicateCallbackCaptor.getValue().duplicateCheckResult(
                IngredientDuplicateChecker.NO_DUPLICATE_FOUND);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class GetDomainModelCallbackClient
            implements UseCaseBase.Callback<IngredientResponse> {

        private static final String TAG = IngredientTest.TAG +
                GetDomainModelCallbackClient.class.getSimpleName() + ": ";

        private IngredientResponse onSuccessResponse;
        private IngredientResponse onErrorResponse;

        @Override
        public void onUseCaseSuccess(IngredientResponse response) {
            System.out.println(TAG + response);
            onSuccessResponse = response;
        }

        @Override
        public void onUseCaseError(IngredientResponse response) {
            System.out.println(TAG + response);
            onErrorResponse = response;
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}