package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.recipe.identity.TestDataRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentity.FailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseMetadata.ComponentState;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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

    private RecipeIdentityResponse identityOnErrorResponse;
    private RecipeIdentityResponse identityOnSuccessResponse;

    // endregion helper fields ---------------------------------------------------------------------

    private RecipeIdentity SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();
    }

    private RecipeIdentity givenUseCase() {
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
    public void newRequest_stateINVALID_DEFAULT_failReasonDATA_UNAVAILABLE() {
        // Arrange
        // This is the initial pre-test setup request for most tests cases, so check all return
        // values
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getNewInvalidActiveDefault();
        // Act
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                getDefault().
                setDomainId(modelUnderTest.getDomainId()).
                build();

        // Act
        SUT.execute(request, new IdentityCallbackClient());

        // Assert
        // assert database called with correct domain id
        verify(repoIdentityMock).getActiveByDomainId(
                eq(modelUnderTest.getDomainId()),
                repoCallback.capture()
        );
        repoCallback.getValue().onPersistenceModelUnavailable();

        // verify no save
        verifyNoMoreInteractions(repoIdentityMock);

        // Assert - get elements to assert
        RecipeIdentityResponse response = identityOnErrorResponse;
        UseCaseMetadataModel metadata = response.getMetadata();

        // Assert component state
        ComponentState expectedComponentState = UseCaseMetadata.ComponentState.INVALID_DEFAULT;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );

        // Assert failReasons
        List<FailReasons> expectedFailReasons = Arrays.asList(
                FailReason.TITLE_TOO_SHORT,
                CommonFailReason.DATA_UNAVAILABLE
        );
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
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

        RecipeIdentityResponse initialisationResponse = identityOnErrorResponse;

        RecipeIdentityRequestModel model = new RecipeIdentityRequestModel.Builder().
                setTitle(modelUnderTest.getTitle()).
                setDescription(modelUnderTest.getDescription()).
                build();
        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                basedOnResponse(initialisationResponse).
                setDomainModel(model).
                build();

        // Act
        SUT.execute(request, new IdentityCallbackClient());

        // Assert
        RecipeIdentityResponse response =identityOnErrorResponse;

        // Assert component state
        ComponentState expectedComponentState = UseCaseMetadata.ComponentState.INVALID_CHANGED;
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

        simulateNewInitialisationRequest(modelUnderTest); // first request
        RecipeIdentityResponse initialisationResponse = identityOnErrorResponse;

        RecipeIdentityRequestModel model = new RecipeIdentityRequestModel.Builder().
                setTitle(modelUnderTest.getTitle()).
                setDescription(modelUnderTest.getDescription()).
                build();
        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                basedOnResponse(initialisationResponse).
                setDomainModel(model).
                build();

        // Act
        SUT.execute(request, new IdentityCallbackClient());

        // Assert
        RecipeIdentityResponse response = identityOnErrorResponse;
        UseCaseMetadataModel metadata = response.getMetadata();

        // Assert fail reasons
        FailReasons[] expectedFailReasons = new FailReasons[]{
                FailReason.TITLE_TOO_LONG,
                FailReason.DESCRIPTION_TOO_LONG,
                CommonFailReason.DATA_UNAVAILABLE // Data has never been in a valid state to save
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
        RecipeIdentityResponse initialisationResponse = identityOnErrorResponse;

        RecipeIdentityRequestModel model = new RecipeIdentityRequestModel.Builder().
                setTitle(modelUnderTest.getTitle()).
                setDescription(modelUnderTest.getDescription()).
                build();
        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                basedOnResponse(initialisationResponse).
                setDomainModel(model).
                build();

        // Act
        SUT.execute(request, new IdentityCallbackClient());

        // Assert
        RecipeIdentityResponse response = identityOnErrorResponse;
        UseCaseMetadataModel metadata = response.getMetadata();

        ComponentState expectedComponentState = UseCaseMetadata.ComponentState.INVALID_CHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );

        FailReasons[] expectedFailReasons = new FailReasons[]{
                FailReason.TITLE_TOO_SHORT,
                CommonFailReason.DATA_UNAVAILABLE
        };
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
        RecipeIdentityResponse initialisationResponse = identityOnErrorResponse;

        RecipeIdentityRequestModel model = new RecipeIdentityRequestModel.Builder().
                setTitle(modelUnderTest.getTitle()).
                setDescription(modelUnderTest.getDescription()).
                build();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                basedOnResponse(initialisationResponse).
                setDomainModel(model).
                build();
        // Act
        SUT.execute(request, new IdentityCallbackClient());

        // Assert
        RecipeIdentityResponse response = identityOnErrorResponse;
        UseCaseMetadataModel metadata = response.getMetadata();

        ComponentState expectedComponentState = UseCaseMetadata.ComponentState.INVALID_CHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
        );

        FailReasons[] expectedFailReasons = new FailReasons[]{
                FailReason.TITLE_TOO_SHORT,
                CommonFailReason.DATA_UNAVAILABLE
        };
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
        RecipeIdentityResponse initialisationResponse = identityOnErrorResponse;

        RecipeIdentityRequestModel model = new RecipeIdentityRequestModel.Builder().
                setTitle(modelUnderTest.getTitle()).
                setDescription(modelUnderTest.getDescription()).
                build();
        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                basedOnResponse(initialisationResponse).
                setDomainModel(model).
                build();

        // title updated, new persistence model created
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getCreateDate());
        when(idProvider.getUId()).thenReturn(modelUnderTest.getDataId());
        // Act
        SUT.execute(request, new IdentityCallbackClient());
        // Assert
        verify(repoIdentityMock).save(eq(modelUnderTest));
    }

    @Test
    public void newRequest_titleValid_stateVALID_CHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidNewTitleValid();

        simulateNewInitialisationRequest(modelUnderTest);
        RecipeIdentityResponse initialisationResponse = identityOnErrorResponse;

        RecipeIdentityRequestModel model = new RecipeIdentityRequestModel.Builder().
                setTitle(modelUnderTest.getTitle()).
                setDescription(modelUnderTest.getDescription()).
                build();
        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                basedOnResponse(initialisationResponse).
                setDomainModel(model).
                build();

        // new persistence model created, requires date stamp and new data id
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());
        when(idProvider.getUId()).thenReturn(modelUnderTest.getDataId());

        // Act
        SUT.execute(request, new IdentityCallbackClient());

        // Assert
        RecipeIdentityResponse response = identityOnSuccessResponse;
        UseCaseMetadataModel metadata = response.getMetadata();

        ComponentState expectedComponentState = UseCaseMetadata.ComponentState.VALID_CHANGED;
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
                getValidNewTitleValid();

        simulateNewInitialisationRequest(modelUnderTest);
        RecipeIdentityResponse initialisationResponse = identityOnErrorResponse;

        RecipeIdentityRequestModel model = new RecipeIdentityRequestModel.Builder().
                setTitle(modelUnderTest.getTitle()).
                setDescription(modelUnderTest.getDescription()).
                build();
        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                basedOnResponse(initialisationResponse).
                setDomainModel(model).
                build();

        // new persistence model created, requires date stamp and new data id
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());
        when(idProvider.getUId()).thenReturn(modelUnderTest.getDataId());

        // Act
        SUT.execute(request, new IdentityCallbackClient());

        // Assert
        verify(repoIdentityMock).save(modelUnderTest);

        RecipeIdentityResponse response = identityOnSuccessResponse;
        UseCaseMetadataModel metadata = response.getMetadata();

        ComponentState expectedComponentState = UseCaseMetadata.ComponentState.VALID_CHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                actualComponentState,
                expectedComponentState
        );
    }

    @Test
    public void newRequest_titleValidThenDescriptionValid_stateVALID_CHANGED() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidNewComplete();

        // Request 1: initialisation request
        simulateNewInitialisationRequest(modelUnderTest);
        RecipeIdentityResponse initialisationResponse = identityOnErrorResponse;

        // Request 2: valid new title request
        RecipeIdentityRequestModel titleModel = new RecipeIdentityRequestModel.
                Builder().
                getDefault().
                setTitle(modelUnderTest.getTitle()).
                build();
        RecipeIdentityRequest titleRequest = new RecipeIdentityRequest.Builder().
                basedOnResponse(initialisationResponse).
                setDomainModel(titleModel).
                build();

        // new persistence model created, requires date stamp and new data id
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getCreateDate());
        when(idProvider.getUId()).thenReturn(modelUnderTest.getDataId());

        // Act
        SUT.execute(titleRequest, new IdentityCallbackClient());

        // Request 3: valid new description request, copy values we are not going to change from
        // last response, only add those we are going to change
        RecipeIdentityResponse titleResponse = identityOnSuccessResponse;

        RecipeIdentityRequestModel descriptionModel = new RecipeIdentityRequestModel.Builder().
                basedOnResponseModel(titleResponse.getDomainModel()).
                setDescription(modelUnderTest.getDescription()).
                build();
        RecipeIdentityRequest descriptionRequest = new RecipeIdentityRequest.Builder().
                basedOnResponse(titleResponse).
                setDomainModel(descriptionModel).
                build();

        // 2nd persistence model created, requires date stamp and new data id
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());
        when(idProvider.getUId()).thenReturn(modelUnderTest.getDataId());
        // Act
        SUT.execute(descriptionRequest, new IdentityCallbackClient());

        // Assert
        // There are 3 saves. Once for a valid title, second save to archive first save, third save
        // to save the valid description
        int expectedNumberOfSaves = 3;

        // Assert data saved
        ArgumentCaptor<RecipeIdentityPersistenceModel> ac = ArgumentCaptor.
                forClass(RecipeIdentityPersistenceModel.class);
        verify(repoIdentityMock, times(expectedNumberOfSaves)).save(ac.capture());

        RecipeIdentityPersistenceModel lastSavedDomainModel = ac.getValue();
        assertEquals(
                modelUnderTest,
                lastSavedDomainModel
        );

        RecipeIdentityResponse descriptionResponse = identityOnSuccessResponse;
        UseCaseMetadataModel metadata = descriptionResponse.getMetadata();

        // Assert state
        ComponentState expectedComponentState = UseCaseMetadata.ComponentState.VALID_CHANGED;
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
        RecipeIdentityResponse response = identityOnSuccessResponse;
        UseCaseMetadataModel metadata = response.getMetadata();

        ComponentState expectedComponentState = UseCaseMetadata.ComponentState.VALID_UNCHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedComponentState,
                actualComponentState
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
        RecipeIdentityResponse response = identityOnSuccessResponse;
        UseCaseMetadataModel metadata = response.getMetadata();
        RecipeIdentityResponseModel domainModel = response.getDomainModel();

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
        RecipeIdentityResponse response = identityOnErrorResponse;

        ComponentState expectedComponentState = UseCaseMetadata.ComponentState.INVALID_UNCHANGED;
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
        RecipeIdentityResponse response = identityOnErrorResponse;
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
        UseCaseMetadataModel metadata = identityOnErrorResponse.getMetadata();

        ComponentState expectedComponentState = UseCaseMetadata.ComponentState.INVALID_UNCHANGED;
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
        UseCaseMetadataModel metadata =identityOnErrorResponse.getMetadata();

        ComponentState expectedComponentState = UseCaseMetadata.ComponentState.INVALID_UNCHANGED;
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

        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        UseCaseMetadataModel metadata = identityOnErrorResponse.getMetadata();

        FailReasons[] expectedFailReasons = new FailReasons[]{FailReason.DESCRIPTION_TOO_LONG};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);

        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
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
                UseCaseMetadata.ComponentState.INVALID_UNCHANGED,
                identityOnErrorResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void existingRequest_titleTooShortDescriptionTooLong_failReasonsTITLE_TOO_SHORT_DESCRIPTION_TOO_LONG() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidExistingTitleTooShortDescriptionTooLong();

        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        List<FailReasons> expectedFailReasons = Arrays.asList(
                RecipeIdentity.FailReason.TITLE_TOO_SHORT,
                RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG
        );
        List<FailReasons> actualFailReasons = identityOnErrorResponse.getMetadata().getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
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
                UseCaseMetadata.ComponentState.INVALID_UNCHANGED,
                identityOnErrorResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void existingRequest_titleTooLongDescriptionTooLong_failReasonsTITLE_TOO_LONG_DESCRIPTION_TOO_LONG() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidExistingTitleTooLongDescriptionTooLong();

        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        List<FailReasons> expectedFailReasons = Arrays.asList(
                RecipeIdentity.FailReason.TITLE_TOO_LONG,
                RecipeIdentity.FailReason.DESCRIPTION_TOO_LONG
        );
        List<FailReasons> actualFailReasons = identityOnErrorResponse.getMetadata().getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
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
                UseCaseMetadata.ComponentState.VALID_UNCHANGED,
                identityOnSuccessResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void existingRequest_titleValidDescriptionDefault_failReasonsNONE() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidExistingTitleValid();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        List<FailReasons> expectedFailReasons = Collections.singletonList(
                CommonFailReason.NONE
        );
        List<FailReasons> actualFailReasons = identityOnSuccessResponse.getMetadata().getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
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
                UseCaseMetadata.ComponentState.VALID_UNCHANGED,
                identityOnSuccessResponse.getMetadata().getComponentState());
    }

    @Test
    public void existingRequest_titleValidDescriptionValid_failReasonsNONE() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidExistingTitleValidDescriptionValid();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        List<FailReasons> expectedFailReasons = Collections.singletonList(CommonFailReason.NONE);
        List<FailReasons> actualFailReasons = identityOnSuccessResponse.getMetadata().getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void existingRequest_titleTooLong_failReasonsTITLE_TOO_LONG() {
        // Arrange
        RecipeIdentityPersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidExistingTitleTooLong();
        // Act
        simulateExistingInitialisationRequest(modelUnderTest);
        // Assert
        List<FailReasons> expectedFailReasons = Collections.singletonList(
                RecipeIdentity.FailReason.TITLE_TOO_LONG);
        List<FailReasons> actualFailReasons = identityOnErrorResponse.getMetadata().getFailReasons();
        assertEquals(
                actualFailReasons,
                expectedFailReasons
        );
    }

    // region helper methods -----------------------------------------------------------------------
    private void simulateNewInitialisationRequest(
            RecipeIdentityPersistenceModel modelUnderTest) {

        // Arrange - data id requested when creating a new persistence model
        when(idProvider.getUId()).thenReturn(modelUnderTest.getDataId());
        // create date and last update requested when creating a new persistence model
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getCreateDate());

        RecipeIdentityRequest initialisationRequest = new RecipeIdentityRequest.Builder().
                getDefault().
                setDomainId(modelUnderTest.getDomainId()).
                build();
        // Act
        SUT.execute(initialisationRequest, new IdentityCallbackClient());

        // Assert repo called, no model found, return model unavailable
        verify(repoIdentityMock).getActiveByDomainId(
                eq(modelUnderTest.getDomainId()),
                repoCallback.capture()
        );
        repoCallback.getValue().onPersistenceModelUnavailable();
    }

    private void simulateExistingInitialisationRequest(
            RecipeIdentityPersistenceModel modelUnderTest) {
        // Arrange
        RecipeIdentityRequest initialisationRequest = new RecipeIdentityRequest.Builder().
                getDefault().
                setDomainId(modelUnderTest.getDomainId()).
                build();
        // Act
        SUT.execute(initialisationRequest, new IdentityCallbackClient());
        // Assert
        verify(repoIdentityMock).getActiveByDomainId(
                eq(modelUnderTest.getDomainId()),
                repoCallback.capture()
        );
        repoCallback.getValue().onPersistenceModelLoaded(modelUnderTest);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private final class IdentityCallbackClient
            implements UseCaseBase.Callback<RecipeIdentityResponse> {

        @Override
        public void onUseCaseSuccess(RecipeIdentityResponse response) {
            System.out.println(TAG + "onSuccess: " + response);
            identityOnSuccessResponse = response;
        }

        @Override
        public void onUseCaseError(RecipeIdentityResponse response) {
            System.out.println(TAG + "onError: " + response);
            identityOnErrorResponse = response;
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}