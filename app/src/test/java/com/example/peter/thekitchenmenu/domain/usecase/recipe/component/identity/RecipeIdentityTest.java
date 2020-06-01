package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.recipe.identity.TestDataRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentity.FailReason;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;
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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecipeIdentityTest {

    private static final String TAG = "tkm-" + RecipeIdentityTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    public static final int TITLE_MIN_LENGTH = TextValidatorTest.SHORT_TEXT_MIN_LENGTH;
    public static final int TITLE_MAX_LENGTH = TextValidatorTest.SHORT_TEXT_MAX_LENGTH;
    public static final int DESCRIPTION_MIN_LENGTH = TextValidatorTest.LONG_TEXT_MIN_LENGTH;
    public static final int DESCRIPTION_MAX_LENGTH = TextValidatorTest.LONG_TEXT_MAX_LENGTH;
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RepositoryRecipeIdentity repoIdentityMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<RecipeIdentityPersistenceModel>> repoCallback;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    UniqueIdProvider idProvider;

    private IdentityCallbackClient callbackClient;
    private int expectedNoOfFailReasons;

    // endregion helper fields ---------------------------------------------------------------------

    private RecipeIdentity SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();
    }

    private RecipeIdentity givenUseCase() {
        callbackClient = new IdentityCallbackClient();

        TextValidator textValidator = new TextValidator.Builder().
                setShortTextMinLength(TITLE_MIN_LENGTH).
                setShortTextMaxLength(TITLE_MAX_LENGTH).
                setLongTextMinLength(DESCRIPTION_MIN_LENGTH).
                setLongTextMaxLength(DESCRIPTION_MAX_LENGTH).
                build();

        return new RecipeIdentity(
                repoIdentityMock,
                idProvider,
                timeProviderMock,
                textValidator);
    }

    @Test
    public void newRequest_stateINVALID_UNCHANGED_failReasonDATA_UNAVAILABLE() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidNewEmpty();
        // Act
        simulateNewInitialisationRequest(modelUnderTest);

        // Assert - get elements to assert
        RecipeIdentityResponse response = callbackClient.onErrorResponse;
        UseCaseMetadataModel metadata = response.getMetadata();

        // Assert component state
        ComponentState expectedComponentState = ComponentState.INVALID_UNCHANGED;
        ComponentState actualComponentState = response.getMetadata().getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );

        // Assert failReasons
        FailReasons[] expectedFailReasons = new FailReasons[]{CommonFailReason.DATA_UNAVAILABLE};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_titleTooLongDescriptionTooLong_stateINVALID_CHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidNewTitleTooLongDescriptionTooLong();

        simulateNewInitialisationRequest(modelUnderTest);

        RecipeIdentityRequest.DomainModel model = new RecipeIdentityRequest.DomainModel.Builder().
                setTitle(modelUnderTest.getTitle()).
                setDescription(modelUnderTest.getDescription()).
                build();

        RecipeIdentityResponse initialisationResponse = callbackClient.onErrorResponse;
        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                basedOnResponse(initialisationResponse).
                setDomainModel(model).
                build();

        // Act
        SUT.execute(request, callbackClient);

        // Assert
        RecipeIdentityResponse response = callbackClient.onErrorResponse;

        // Assert component state
        ComponentState expectedComponentState = ComponentState.INVALID_CHANGED;
        ComponentState actualComponentState = response.getMetadata().getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );
    }

    @Test
    public void newRequest_titleTooLongDescriptionTooLong_failReasonsTITLE_TOO_LONG_DESCRIPTION_TOO_LONG() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidNewTitleTooLongDescriptionTooLong();

        simulateNewInitialisationRequest(modelUnderTest);

        RecipeIdentityRequest.DomainModel model = new RecipeIdentityRequest.DomainModel.Builder().
                setTitle(modelUnderTest.getTitle()).
                setDescription(modelUnderTest.getDescription()).
                build();

        RecipeIdentityResponse initialisationResponse = callbackClient.onErrorResponse;
        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                basedOnResponse(initialisationResponse).
                setDomainModel(model).
                build();

        // Act
        SUT.execute(request, callbackClient);

        // Assert
        RecipeIdentityResponse response = callbackClient.onErrorResponse;
        UseCaseMetadataModel metadata = response.getMetadata();

        // Assert fail reasons
        FailReasons[] expectedFailReasons = new FailReasons[]{
                FailReason.TITLE_TOO_LONG,
                FailReason.DESCRIPTION_TOO_LONG
        };
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_titleTooShortDescriptionValid_stateINVALID_CHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidNewTitleTooShortDescriptionValid();

        simulateNewInitialisationRequest(modelUnderTest);

        RecipeIdentityRequest.DomainModel model = new RecipeIdentityRequest.DomainModel.Builder().
                setTitle(modelUnderTest.getTitle()).
                setDescription(modelUnderTest.getDescription()).
                build();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                setDataId(callbackClient.onErrorResponse.getDataId()).
                setDomainId(callbackClient.onErrorResponse.getDomainId()).
                setDomainModel(model).
                build();

        // Act
        SUT.execute(request, callbackClient);

        // Assert
        RecipeIdentityResponse response = callbackClient.onErrorResponse;
        UseCaseMetadataModel metadata = response.getMetadata();

        ComponentState expectedComponentState = ComponentState.INVALID_CHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );

        FailReasons[] expectedFailReasons = new FailReasons[]{FailReason.TITLE_TOO_SHORT};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_titleTooShort_stateINVALID_CHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidNewTitleTooShort();

        simulateNewInitialisationRequest(modelUnderTest);

        RecipeIdentityRequest.DomainModel model = new RecipeIdentityRequest.DomainModel.Builder().
                setTitle(modelUnderTest.getTitle()).
                setDescription(modelUnderTest.getDescription()).
                build();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                setDataId(callbackClient.onErrorResponse.getDataId()).
                setDomainId(callbackClient.onErrorResponse.getDomainId()).
                setDomainModel(model).
                build();
        // Act
        SUT.execute(request, callbackClient);

        // Assert
        RecipeIdentityResponse response = callbackClient.onErrorResponse;
        UseCaseMetadataModel metadata = response.getMetadata();

        ComponentState expectedComponentState = ComponentState.INVALID_CHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );

        FailReasons[] expectedFailReasons = new FailReasons[]{FailReason.TITLE_TOO_SHORT};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_titleValid_valuesPersisted() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidNewTitleValid();

        simulateNewInitialisationRequest(modelUnderTest);

        RecipeIdentityRequest.DomainModel model = new RecipeIdentityRequest.DomainModel.Builder().
                setTitle(modelUnderTest.getTitle()).
                setDescription(modelUnderTest.getDescription()).
                build();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                basedOnResponse(callbackClient.onErrorResponse).
                setDomainModel(model).
                build();

        // title updated, new persistence model created
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());
        when(idProvider.getUId()).thenReturn(modelUnderTest.getDataId());
        // Act
        SUT.execute(request, callbackClient);
        // Assert
        verify(repoIdentityMock).save(eq(modelUnderTest));
    }

    @Test
    public void newRequest_titleValid_stateVALID_CHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidNewTitleValid();

        simulateNewInitialisationRequest(modelUnderTest);

        RecipeIdentityRequest.DomainModel model = new RecipeIdentityRequest.DomainModel.Builder().
                setTitle(modelUnderTest.getTitle()).
                setDescription(modelUnderTest.getDescription()).
                build();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                setDataId(callbackClient.onErrorResponse.getDataId()).
                setDomainId(callbackClient.onErrorResponse.getDomainId()).
                setDomainModel(model).
                build();

        // new persistence model created, requires date stamp and new data id
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());
        when(idProvider.getUId()).thenReturn(modelUnderTest.getDataId());
        // Act
        SUT.execute(request, callbackClient);

        // Assert
        RecipeIdentityResponse response = callbackClient.onSuccessResponse;
        UseCaseMetadataModel metadata = response.getMetadata();

        ComponentState expectedComponentState = ComponentState.VALID_CHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );

        FailReasons[] expectedFailReasons = new FailReasons[]{CommonFailReason.NONE};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_titleValidDescriptionValid_valuesPersisted() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidNewComplete();

        simulateNewInitialisationRequest(modelUnderTest);

        RecipeIdentityRequest.DomainModel model = new RecipeIdentityRequest.DomainModel.Builder().
                setTitle(modelUnderTest.getTitle()).
                setDescription(modelUnderTest.getDescription()).
                build();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                setDataId(callbackClient.onErrorResponse.getDataId()).
                setDomainId(callbackClient.onErrorResponse.getDomainId()).
                setDomainModel(model).
                build();

        // new persistence model created, requires date stamp and new data id
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());
        when(idProvider.getUId()).thenReturn(modelUnderTest.getDataId());
        // Act
        SUT.execute(request, callbackClient);

        // Assert
        verify(repoIdentityMock).save(modelUnderTest);

        RecipeIdentityResponse response = callbackClient.onSuccessResponse;
        UseCaseMetadataModel metadata = response.getMetadata();

        ComponentState expectedComponentState = ComponentState.VALID_CHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                actualComponentState,
                expectedComponentState
        );
    }

    @Test
    public void newRequest_titleValid_then_descriptionValid_stateVALID_CHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidNewComplete();
        expectedNoOfFailReasons = 1;

        // Request 1: initialisation request
        simulateNewInitialisationRequest(modelUnderTest);

        // Request 2: Valid new title request
        RecipeIdentityRequest.DomainModel titleModel = new RecipeIdentityRequest.DomainModel.Builder().
                getDefault().
                setTitle(modelUnderTest.getTitle()).
                build();
        RecipeIdentityRequest titleRequest = new RecipeIdentityRequest.Builder().
                setDataId(callbackClient.onErrorResponse.getDataId()).
                setDomainId(callbackClient.onErrorResponse.getDomainId()).
                setDomainModel(titleModel).
                build();

        // new persistence model created, requires date stamp and new data id
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());
        when(idProvider.getUId()).thenReturn(modelUnderTest.getDataId());
        // Act
        SUT.execute(titleRequest, callbackClient);

        // Request 3: valid new description request, copy previous values from last response
        RecipeIdentityRequest.DomainModel descriptionModel = new RecipeIdentityRequest.DomainModel.Builder().
                basedOnResponseModel(callbackClient.onSuccessResponse.getDomainModel()).
                setDescription(modelUnderTest.getDescription()).
                build();
        RecipeIdentityRequest descriptionRequest = new RecipeIdentityRequest.Builder().
                setDataId(callbackClient.onSuccessResponse.getDataId()).
                setDomainId(callbackClient.onSuccessResponse.getDomainId()).
                setDomainModel(descriptionModel).
                build();

        // 2nd persistence model created, requires date stamp and new data id
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());
        when(idProvider.getUId()).thenReturn(modelUnderTest.getDataId());
        // Act
        SUT.execute(descriptionRequest, callbackClient);

        // Assert second save equal to test model
        verify(repoIdentityMock).save(eq(modelUnderTest));

        RecipeIdentityResponse response = callbackClient.onSuccessResponse;
        UseCaseMetadataModel metadata = response.getMetadata();

        // Assert state
        ComponentState expectedComponentState = ComponentState.VALID_CHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );

        FailReasons[] expectedFailReasons = new FailReasons[]{CommonFailReason.NONE};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void existingRequest_titleValidDescriptionValid_VALID_UNCHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidExistingTitleValidDescriptionValid();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);

        // Assert
        RecipeIdentityResponse response = callbackClient.onSuccessResponse;
        UseCaseMetadataModel metadata = response.getMetadata();

        ComponentState expectedComponentState = ComponentState.VALID_UNCHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                actualComponentState,
                expectedComponentState
        );
    }

    @Test
    public void existingRequest_titleValidDescriptionValid_correctValuesReturned() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidExistingTitleValidDescriptionValid();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);

        // Assert
        RecipeIdentityResponse response = callbackClient.onSuccessResponse;
        UseCaseMetadataModel metadata = response.getMetadata();
        RecipeIdentityResponse.DomainModel domainModel = response.getDomainModel();

        String expectedDataId = modelUnderTest.getDataId();
        String actualDataId = response.getDataId();
        assertEquals(
                expectedDataId,
                actualDataId
        );

        String expectedDomainId = modelUnderTest.getDomainId();
        String actualDomainId = response.getDomainId();
        assertEquals(
                expectedDomainId,
                actualDomainId
        );

        long expectedCreateDate = modelUnderTest.getCreateDate();
        long actualCreateDate = metadata.getCreateDate();
        assertEquals(
                expectedCreateDate,
                actualCreateDate
        );

        long expectedLastUpdate = modelUnderTest.getLastUpdate();
        long actualLastUpdate = metadata.getLasUpdate();
        assertEquals(
                expectedLastUpdate,
                actualLastUpdate
        );

        // Assert domain data
        String expectedTitle = modelUnderTest.getTitle();
        String actualTitle = domainModel.getTitle();
        assertEquals(
                expectedTitle,
                actualTitle
        );

        String expectedDescription = modelUnderTest.getDescription();
        String actualDescription = domainModel.getDescription();
        assertEquals(
                expectedDescription,
                actualDescription
        );
    }

    @Test
    public void existingRequest_titleTooShort_stateINVALID_UNCHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidExistingTitleTooShort();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);

        // Assert response values
        RecipeIdentityResponse response = callbackClient.onErrorResponse;

        ComponentState expectedComponentState = ComponentState.INVALID_UNCHANGED;
        ComponentState actualComponentState = response.getMetadata().getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );
    }

    @Test
    public void existingRequest_titleTooShort_failReasonsTITLE_TOO_SHORT() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidExistingTitleTooShort();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);

        // Assert
        RecipeIdentityResponse response = callbackClient.onErrorResponse;
        UseCaseMetadataModel metadata = response.getMetadata();

        FailReasons[] expectedFailReasons = new FailReasons[]{FailReason.TITLE_TOO_SHORT};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void existingRequest_titleTooLong_stateINVALID_UNCHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidExistingTitleTooLong();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);

        // Assert
        UseCaseMetadataModel metadata = callbackClient.onErrorResponse.getMetadata();

        ComponentState expectedComponentState = ComponentState.INVALID_UNCHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );
    }

    @Test
    public void existingRequest_titleValidDescriptionTooLong_stateINVALID_UNCHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidExistingTitleValidDescriptionTooLong();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);

        // Assert
        UseCaseMetadataModel metadata = callbackClient.onErrorResponse.getMetadata();

        ComponentState expectedComponentState = ComponentState.INVALID_UNCHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );
    }

    @Test
    public void existingRequest_titleValidDescriptionTooLong_failReasonsDESCRIPTION_TOO_LONG() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidExistingTitleValidDescriptionTooLong();
        expectedNoOfFailReasons = 1;
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        List<FailReasons> failReasons = callbackClient.onErrorResponse.
                getMetadata().
                getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG));
    }

    @Test
    public void existingRequest_titleTooShortDescriptionTooLong_stateINVALID_UNCHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidExistingTitleTooShortDescriptionTooLong();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.INVALID_UNCHANGED,
                callbackClient.onErrorResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void existingRequest_titleTooShortDescriptionTooLong_failReasonsTITLE_TOO_SHORT_DESCRIPTION_TOO_LONG() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidExistingTitleTooShortDescriptionTooLong();
        expectedNoOfFailReasons = 2;
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        List<FailReasons> failReasons = callbackClient.onErrorResponse.getMetadata().getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(RecipeIdentity.FailReason.TITLE_TOO_SHORT));
        assertTrue(failReasons.contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG));
    }

    @Test
    public void existingRequest_titleTooLongDescriptionTooLong_stateINVALID_UNCHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidExistingTitleTooLongDescriptionTooLong();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.INVALID_UNCHANGED,
                callbackClient.onErrorResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void existingRequest_titleTooLongDescriptionTooLong_failReasonsTITLE_TOO_LONG_DESCRIPTION_TOO_LONG() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidExistingTitleTooLongDescriptionTooLong();
        expectedNoOfFailReasons = 2;
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        List<FailReasons> failReasons = callbackClient.onErrorResponse.
                getMetadata().
                getFailReasons();
        assertEquals(
                expectedNoOfFailReasons, failReasons.size()
        );
        assertTrue(failReasons.contains(RecipeIdentity.FailReason.TITLE_TOO_LONG));
        assertTrue(failReasons.contains(RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG));
    }

    @Test
    public void existingRequest_titleValidDescriptionDefault_stateVAILD_UNCHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidExistingTitleValid();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.VALID_UNCHANGED,
                callbackClient.onSuccessResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void existingRequest_titleValidDescriptionDefault_failReasonsNONE() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidExistingTitleValid();
        expectedNoOfFailReasons = 1;
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        List<FailReasons> failReasons = callbackClient.onSuccessResponse.
                getMetadata().
                getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(CommonFailReason.NONE));
    }

    @Test
    public void existingRequest_titleValidDescriptionValid_stateVALID_UNCHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidExistingTitleValid();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.VALID_UNCHANGED,
                callbackClient.onSuccessResponse.getMetadata().getComponentState());
    }

    @Test
    public void existingRequest_titleValidDescriptionValid_failReasonsNONE() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidExistingTitleValidDescriptionValid();
        expectedNoOfFailReasons = 1;
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        List<FailReasons> failReasons = callbackClient.onSuccessResponse.
                getMetadata().
                getFailReasons();
        assertEquals(
                expectedNoOfFailReasons,
                failReasons.size()
        );
        assertTrue(failReasons.contains(CommonFailReason.NONE));
    }

    @Test
    public void existingRequest_titleTooLong_failReasonsTITLE_TOO_LONG() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidExistingTitleTooLong();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        assertEquals(1, callbackClient.onErrorResponse.getMetadata().getFailReasons().size());
        assertTrue(callbackClient.onErrorResponse.
                getMetadata().
                getFailReasons().
                contains(RecipeIdentity.FailReason.TITLE_TOO_LONG));
    }

    // region helper methods -----------------------------------------------------------------------
    private void simulateNewInitialisationRequest(
            RecipeIdentityPersistenceModel modelUnderTest) {
        // Arrange - data id requested when creating a new persistence model
        when(idProvider.getUId()).thenReturn(TestDataRecipeIdentity.
                getInvalidNewEmpty().
                getDataId());
        // create date and last update requested when creating a new persistence model
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(TestDataRecipeIdentity.
                getInvalidNewEmpty().
                getCreateDate());

        RecipeIdentityRequest initialisationRequest = new RecipeIdentityRequest.Builder().
                getDefault().
                setDomainId(modelUnderTest.getDomainId()).
                build();
        // Act
        SUT.execute(initialisationRequest, callbackClient);

        // Assert repo called, no model found, return model unavailable
        verify(repoIdentityMock).getActiveByDomainId(
                eq(modelUnderTest.getDomainId()),
                repoCallback.capture()
        );
        repoCallback.getValue().onModelUnavailable();
    }

    private void simulateExistingInitialisationRequest(
            RecipeIdentityPersistenceModel modelUnderTest) {
        // Arrange
        RecipeIdentityRequest initialisationRequest = new RecipeIdentityRequest.Builder().
                getDefault().
                setDomainId(modelUnderTest.getDomainId()).
                build();
        // Act
        SUT.execute(initialisationRequest, callbackClient);
        // Assert
        verify(repoIdentityMock).getActiveByDomainId(
                eq(modelUnderTest.getDomainId()),
                repoCallback.capture()
        );
        repoCallback.getValue().onModelLoaded(modelUnderTest);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class IdentityCallbackClient
            implements UseCaseBase.Callback<RecipeIdentityResponse> {

        private final String TAG = RecipeIdentityTest.TAG +
                IdentityCallbackClient.class.getSimpleName() + ": ";

        private RecipeIdentityResponse onSuccessResponse;
        private RecipeIdentityResponse onErrorResponse;

        @Override
        public void onUseCaseSuccess(RecipeIdentityResponse r) {
            System.out.println(TAG + "onSuccess: " + r);
            onSuccessResponse = r;
        }

        @Override
        public void onUseCaseError(RecipeIdentityResponse r) {
            System.out.println(TAG + "onError: " + r);
            onErrorResponse = r;
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}