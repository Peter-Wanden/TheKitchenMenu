package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.recipe.duration.TestDataRecipeDuration;
import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDuration.FailReason;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipeDurationTest {

//    private static final String TAG = "tag-" + RecipeDurationTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    public static int MAX_PREP_TIME = TestDataRecipeDuration.MAX_PREP_TIME;
    public static int MAX_COOK_TIME = TestDataRecipeDuration.MAX_COOK_TIME;
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RepositoryRecipeDuration repoMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<RecipeDurationPersistenceModel>> repoCallback;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    UniqueIdProvider idProviderMock;

    private RecipeDurationResponse durationOnSuccessResponse;
    private RecipeDurationResponse durationOnErrorResponse;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeDuration SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();
    }

    private RecipeDuration givenUseCase() {
        return new RecipeDuration(
                repoMock,
                timeProviderMock,
                idProviderMock,
                MAX_PREP_TIME,
                MAX_COOK_TIME);
    }

    @Test
    public void newRequest_componentStateINVALID_UNCHANGED() {
        // Arrange
        // This is the initial request for most tests, so check all return values
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.getValidNew();

        // Act
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        // Assert
        RecipeDurationResponse response = durationOnErrorResponse;

        assertEquals(
                modelUnderTest.getDataId(),
                response.getDataId()
        );
        assertEquals(
                modelUnderTest.getDomainId(),
                response.getDomainId()
        );

        UseCaseMetadataModel metadata = durationOnErrorResponse.getMetadata();

        ComponentState expectedState = ComponentState.INVALID_UNCHANGED;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualComponentState
        );

        FailReasons[] expectedFailReasons = new FailReasons[]{CommonFailReason.DATA_UNAVAILABLE};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_invalidPrepHours_invalidValueNotSaved() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.getValidNew();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        RecipeDurationRequest.DomainModel invalidModel =
                new RecipeDurationRequest.DomainModel.Builder().
                        basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                        setPrepHours(MAX_PREP_TIME / 60 + 1).
                        build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(invalidModel).
                build();

        // Act
        SUT.execute(request, new DurationCallbackClient());
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void newRequest_invalidPrepHours_resultINVALID_CHANGED() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getInvalidNewPrepTimeInvalid();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        RecipeDurationRequest.DomainModel invalidDomainModel = new RecipeDurationRequest.
                DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setPrepHours(MAX_PREP_TIME / 60 + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(invalidDomainModel).
                build();
        // Act
        SUT.execute(request, new DurationCallbackClient());
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void newRequest_invalidPrepHours_FaiReasonINVALID_PREP_TIME() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getInvalidNewPrepTimeInvalid();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        RecipeDurationRequest.DomainModel invalidDomainModel = new RecipeDurationRequest.
                DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setPrepHours(MAX_PREP_TIME / 60 + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(invalidDomainModel).
                build();
        // Act
        SUT.execute(request, new DurationCallbackClient());
        // Assert

        RecipeDurationResponse response = durationOnErrorResponse;
        UseCaseMetadataModel metadata = response.getMetadata();

        FailReasons[] expectedFailReasons = new FailReasons[]{FailReason.INVALID_PREP_TIME};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_validPrepHours_resultVALID_CHANGED() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getValidNew();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        RecipeDurationRequest.DomainModel validDomainModel = new RecipeDurationRequest.DomainModel.
                Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setPrepHours(MAX_PREP_TIME / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(validDomainModel).
                build();
        // Act
        SUT.execute(validRequest, new DurationCallbackClient());
        // Assert
        assertEquals(
                ComponentState.VALID_CHANGED,
                durationOnSuccessResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void newRequest_validPrepHours_failReasonNONE() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getValidNew();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        RecipeDurationRequest.DomainModel validDomainModel = new RecipeDurationRequest.DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setPrepHours(MAX_PREP_TIME / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(validDomainModel).
                build();
        // Act
        SUT.execute(validRequest, new DurationCallbackClient());
        // Assert
        assertTrue(
                durationOnSuccessResponse.
                        getMetadata().
                        getFailReasons().
                        contains(CommonFailReason.NONE));
    }

    @Test
    public void newRequest_validPrepHours_prepHoursSaved() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getValidNewPrepTimeValid();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        whenTimeProviderCalledReturn(modelUnderTest.getLastUpdate());
        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());

        RecipeDurationRequest.DomainModel validDomainModel = new RecipeDurationRequest.DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setPrepHours(MAX_PREP_TIME / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(validDomainModel).
                build();

        // Act
        SUT.execute(validRequest, new DurationCallbackClient());
        // Assert
        verify(repoMock).save(eq(modelUnderTest));
    }

    @Test
    public void newRequest_invalidPrepMinutes_invalidValueNotSaved() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getValidNewPrepTimeValid();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        RecipeDurationRequest.DomainModel invalidDomainModel = new RecipeDurationRequest.
                DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setPrepMinutes(MAX_PREP_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(invalidDomainModel).
                build();
        // Act
        SUT.execute(request, new DurationCallbackClient());
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void newRequest_invalidPrepMinutes_resultINVALID_CHANGED() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getValidNewPrepTimeValid();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        RecipeDurationRequest.DomainModel invalidDomainModel = new RecipeDurationRequest.
                DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setPrepMinutes(MAX_PREP_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(invalidDomainModel).
                build();
        // Act
        SUT.execute(request, new DurationCallbackClient());
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void newRequest_invalidPrepMinutes_FaiReasonINVALID_PREP_TIME() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getValidNewPrepTimeValid();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        RecipeDurationRequest.DomainModel invalidDomainModel = new RecipeDurationRequest.
                DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setPrepMinutes(MAX_PREP_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(invalidDomainModel).
                build();
        // Act
        SUT.execute(request, new DurationCallbackClient());
        // Assert
        UseCaseMetadataModel metadata = durationOnErrorResponse.getMetadata();

        FailReasons[] expectedFailReasons = new FailReasons[]{FailReason.INVALID_PREP_TIME};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_validPrepMinutes_resultVALID_CHANGED() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getValidNewPrepTimeValid();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        RecipeDurationRequest.DomainModel validDomainModel = new RecipeDurationRequest.
                DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setPrepMinutes(MAX_PREP_TIME).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(validDomainModel).
                build();
        // Act
        SUT.execute(validRequest, new DurationCallbackClient());
        // Assert
        assertEquals(
                ComponentState.VALID_CHANGED,
                durationOnSuccessResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void newRequest_validPrepMinutes_failReasonNONE() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getValidNewPrepTimeValid();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        RecipeDurationRequest.DomainModel validDomainModel = new RecipeDurationRequest.
                DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setPrepMinutes(MAX_PREP_TIME).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(validDomainModel).
                build();
        // Act
        SUT.execute(validRequest, new DurationCallbackClient());
        // Assert
        UseCaseMetadataModel metadata = durationOnSuccessResponse.getMetadata();

        FailReasons[] expectedFailReasons = new FailReasons[]{CommonFailReason.NONE};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_validPrepMinutes_prepMinutesSaved() {

        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getValidNewPrepTimeValid();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());
        whenTimeProviderCalledReturn(modelUnderTest.getLastUpdate());

        RecipeDurationRequest.DomainModel validDomainModel = new RecipeDurationRequest.
                DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setPrepMinutes(MAX_PREP_TIME).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(validDomainModel).
                build();
        // Act
        SUT.execute(validRequest, new DurationCallbackClient());
        // Assert
        verify(repoMock).save(eq(modelUnderTest));
    }

    @Test
    public void newRequest_invalidCookHours_invalidValueNotSaved() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getValidNewPrepTimeValid();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        RecipeDurationRequest.DomainModel invalidDomainModel = new RecipeDurationRequest.
                DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setCookHours(MAX_COOK_TIME / 60 + 1).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(invalidDomainModel).
                build();
        // Act
        SUT.execute(invalidRequest, new DurationCallbackClient());
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void newRequest_invalidCookHours_resultINVALID_CHANGED() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getValidNewPrepTimeValid();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        RecipeDurationRequest.DomainModel invalidDomainModel = new RecipeDurationRequest.
                DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setCookHours(MAX_COOK_TIME / 60 + 1).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(invalidDomainModel).
                build();
        // Act
        SUT.execute(invalidRequest, new DurationCallbackClient());
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void newRequest_invalidCookHours_failReasonINVALID_COOK_TIME() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getValidNewPrepTimeValid();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        RecipeDurationRequest.DomainModel invalidDomainModel = new RecipeDurationRequest.
                DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setCookHours(MAX_COOK_TIME / 60 + 1).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(invalidDomainModel).
                build();
        // Act
        SUT.execute(invalidRequest, new DurationCallbackClient());
        // Assert
        UseCaseMetadataModel metadata = durationOnErrorResponse.getMetadata();

        FailReasons[] expectedFailReasons = new FailReasons[]{FailReason.INVALID_COOK_TIME};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                actualFailReasons,
                expectedFailReasons
        );
    }

    @Test
    public void newRequest_validCookHours_validValueSaved() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getValidNewCookTimeValid();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        whenTimeProviderCalledReturn(modelUnderTest.getLastUpdate());
        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());

        RecipeDurationRequest.DomainModel validDomainModel = new RecipeDurationRequest.
                DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setCookHours(MAX_COOK_TIME / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(validDomainModel).
                build();
        // Act
        SUT.execute(validRequest, new DurationCallbackClient());
        // Assert
        verify(repoMock).save(eq(modelUnderTest));
    }

    @Test
    public void newRequest_validCookHours_resultVALID_CHANGED() {
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getValidNewCookTimeValid();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        RecipeDurationRequest.DomainModel validDomainModel = new RecipeDurationRequest.
                DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setCookHours(modelUnderTest.getCookTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(validDomainModel).
                build();
        // Act
        SUT.execute(validRequest, new DurationCallbackClient());
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.VALID_CHANGED,
                durationOnSuccessResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void newRequest_validCookHours_failReasonNONE() {
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getValidNewCookTimeValid();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        RecipeDurationRequest.DomainModel validDomainModel = new RecipeDurationRequest.
                DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setCookHours(modelUnderTest.getCookTime() / 60).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(validDomainModel).
                build();
        // Act
        SUT.execute(validRequest, new DurationCallbackClient());
        // Assert
        UseCaseMetadataModel metadata = durationOnSuccessResponse.getMetadata();

        FailReasons[] expectedFailReasons = new FailReasons[]{CommonFailReason.NONE};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_invalidCookMinutes_invalidValueNotSaved() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getInvalidNewCookTimeInvalid();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        RecipeDurationRequest.DomainModel invalidDomainModel = new RecipeDurationRequest.
                DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setCookMinutes(MAX_COOK_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(invalidDomainModel).
                build();
        // Act
        SUT.execute(request, new DurationCallbackClient());
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void newRequest_invalidCookMinutes_resultINVALID_CHANGED() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getInvalidNewCookTimeInvalid();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        RecipeDurationRequest.DomainModel invalidDomainModel = new RecipeDurationRequest.
                DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setCookMinutes(MAX_COOK_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(invalidDomainModel).
                build();
        // Act
        SUT.execute(request, new DurationCallbackClient());
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getMetadata().getComponentState());
    }

    @Test
    public void newRequest_invalidCookMinutes_failReasonINVALID_COOK_TIME() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getInvalidNewCookTimeInvalid();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        RecipeDurationRequest.DomainModel invalidDomainModel = new RecipeDurationRequest.DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setCookMinutes(MAX_COOK_TIME + 1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(invalidDomainModel).
                build();
        // Act
        SUT.execute(request, new DurationCallbackClient());
        // Assert
        UseCaseMetadataModel metadata = durationOnErrorResponse.getMetadata();

        FailReasons[] expectedFailReasons = new FailReasons[]{FailReason.INVALID_COOK_TIME};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void startNewId_validCookMinutes_validValueSaved() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getValidNewCookTimeValid();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        whenTimeProviderCalledReturn(modelUnderTest.getLastUpdate());
        whenIdProviderThenReturn(modelUnderTest.getDataId());

        RecipeDurationRequest.DomainModel domainModel = new RecipeDurationRequest.
                DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setCookMinutes(MAX_COOK_TIME)
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(domainModel).
                build();
        // Act
        SUT.execute(request, new DurationCallbackClient());
        // Assert
        verify(repoMock).save(eq(modelUnderTest));
    }

    @Test
    public void newRequest_validCookMinutes_stateVALID_CHANGED() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getValidNewCookTimeValid();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        whenTimeProviderCalledReturn(modelUnderTest.getCreateDate());
        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());

        RecipeDurationRequest.DomainModel domainModel = new RecipeDurationRequest.
                DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setCookMinutes(MAX_COOK_TIME)
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(domainModel).
                build();
        // Act
        SUT.execute(request, new DurationCallbackClient());
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.VALID_CHANGED,
                durationOnSuccessResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void newRequest_validCookMinutes_failReasonNONE() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getValidNewCookTimeValid();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        whenTimeProviderCalledReturn(modelUnderTest.getCreateDate());
        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());

        RecipeDurationRequest.DomainModel domainModel = new RecipeDurationRequest.
                DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setCookMinutes(MAX_COOK_TIME)
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(domainModel).
                build();
        // Act
        SUT.execute(request, new DurationCallbackClient());
        // Assert
        UseCaseMetadataModel metadata = durationOnSuccessResponse.getMetadata();

        FailReasons[] expectedFailReasons = new FailReasons[]{CommonFailReason.NONE};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_invalidPrepAndInvalidCookTime_failReasonsINVALID_PREP_TIME_INVALID_COOK_TIME() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getInvalidExistingComplete();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        RecipeDurationRequest.DomainModel domainModel = new RecipeDurationRequest.
                DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setPrepHours(MAX_PREP_TIME / 60).
                setPrepMinutes(1).
                setCookHours(MAX_COOK_TIME / 60).
                setCookMinutes(1).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(domainModel).
                build();
        // Act
        SUT.execute(request, new DurationCallbackClient());
        // Assert
        UseCaseMetadataModel metadata = durationOnErrorResponse.getMetadata();

        assertEquals(
                ComponentState.INVALID_CHANGED,
                metadata.getComponentState()
        );

        FailReasons[] expectedFailReasons = new FailReasons[]
                {FailReason.INVALID_PREP_TIME, FailReason.INVALID_COOK_TIME};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);
        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_validPrepTimeValidCookTime_failReasonsNONE() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getValidNewComplete();
        simulateInitialisationRequestReturnModelUnavailable(modelUnderTest);

        whenTimeProviderCalledReturn(modelUnderTest.getCreateDate());
        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());

        RecipeDurationRequest.DomainModel domainModel = new RecipeDurationRequest.DomainModel.
                Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setPrepHours(MAX_PREP_TIME / 60 - 1).
                setPrepMinutes(59).
                setCookHours(MAX_COOK_TIME / 60 - 1).
                setCookMinutes(59)
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(domainModel).
                build();
        // Act
        SUT.execute(request, new DurationCallbackClient());
        // Assert
        UseCaseMetadataModel metadata = durationOnSuccessResponse.getMetadata();

        FailReasons[] expectedFailReasons = new FailReasons[]{CommonFailReason.NONE};
        FailReasons[] actualFailReasons = metadata.getFailReasons().toArray(new FailReasons[0]);

        assertArrayEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void existingRequest_validPrepAndCookTime_correctValuesReturned() {
        // Arrange / Act
        sendInitialiseRequestForValidExistingModel();
        // Assert
        assertEquals(
                MAX_PREP_TIME,
                durationOnSuccessResponse.getDomainModel().getTotalPrepTime()
        );
        assertEquals(
                MAX_COOK_TIME,
                durationOnSuccessResponse.getDomainModel().getTotalCookTime()
        );
    }

    @Test
    public void existingRequest_validPrepAndCookTime_statusVALID_UNCHANGED() {
        // Arrange / Act
        sendInitialiseRequestForValidExistingModel();
        // Assert
        assertEquals(
                RecipeMetadata.ComponentState.VALID_UNCHANGED,
                durationOnSuccessResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void existingRequest_invalidPrepAndCookTime_statusINVALID_UNCHANGED() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getInvalidExistingComplete();

        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                getDefault().
                setDomainId(modelUnderTest.getDomainId()).
                build();
        // Act
        SUT.execute(request, new DurationCallbackClient());
        // Assert
        verify(repoMock).getActiveByDomainId(eq(modelUnderTest.getDomainId()),
                repoCallback.capture());
        repoCallback.getValue().dataSourceOnDomainModelLoaded(modelUnderTest);

        assertEquals(
                RecipeMetadata.ComponentState.INVALID_UNCHANGED,
                durationOnErrorResponse.getMetadata().getComponentState()
        );
    }

    // region helper methods -----------------------------------------------------------------------
    private class DurationCallbackClient implements UseCaseBase.Callback<RecipeDurationResponse> {
        @Override
        public void onUseCaseSuccess(RecipeDurationResponse response) {
            durationOnSuccessResponse = response;
        }

        @Override
        public void onUseCaseError(RecipeDurationResponse response) {
            durationOnErrorResponse = response;
        }
    }

    private void whenTimeProviderCalledReturn(long time) {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
    }

    private void whenIdProviderThenReturn(String dataId) {
        when(idProviderMock.getUId()).thenReturn(dataId);
    }

    private void simulateInitialisationRequestReturnModelUnavailable(
            RecipeDurationPersistenceModel modelUnderTest) {
        String domainId = modelUnderTest.getDomainId();
        String dataId = modelUnderTest.getDataId();

        RecipeDurationRequest initialisationRequest = new RecipeDurationRequest.Builder().
                getDefault().
                setDomainId(domainId).
                build();

        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getCreateDate());
        when(idProviderMock.getUId()).thenReturn(dataId);

        // Act
        SUT.execute(initialisationRequest, new DurationCallbackClient());

        verify(repoMock).getActiveByDomainId(eq(domainId), repoCallback.capture());
        repoCallback.getValue().dataSourceOnDomainModelUnavailable();
    }

    private void sendInitialiseRequestForValidExistingModel() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getValidExistingComplete();

        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                getDefault().
                setDomainId(modelUnderTest.getDomainId()).
                build();
        // Act
        SUT.execute(request, new DurationCallbackClient());
        verify(repoMock).getActiveByDomainId(eq(modelUnderTest.getDomainId()),
                repoCallback.capture());
        repoCallback.getValue().dataSourceOnDomainModelLoaded(modelUnderTest);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}