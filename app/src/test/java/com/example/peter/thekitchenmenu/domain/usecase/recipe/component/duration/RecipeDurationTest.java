package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.DataAccessRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.recipe.duration.TestDataRecipeDuration;
import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDuration.FailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseMetadata.ComponentState;
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
    DataAccessRecipeDuration repoMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<RecipeDurationPersistenceModel>> repoDurationCallback;
    @Mock
    UniqueIdProvider idProviderMock;
    @Mock
    TimeProvider timeProviderMock;

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
                idProviderMock,
                timeProviderMock,
                MAX_PREP_TIME,
                MAX_COOK_TIME
        );
    }

    @Test
    public void newRequest_componentStateVALID_DEFAULT() {
        // Arrange
        // This is the initial pre-test setup request for most tests cases, so check all return
        // values
        RecipeDurationPersistenceModel expectedDefaultValues = TestDataRecipeDuration.
                getNewActiveDefault();

        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                getDefault().
                setDomainId(expectedDefaultValues.getDomainId()).
                build();

        // Act
        SUT.execute(request, new DurationCallbackClient());

        // Assert
        // assert persistence calls
        verify(repoMock).getActiveByDomainId(eq(expectedDefaultValues.getDomainId()),
                repoDurationCallback.capture());
        repoDurationCallback.getValue().onPersistenceModelUnavailable();

        // assert default values not saved
        verifyNoMoreInteractions(repoMock);

        // assert response values
        RecipeDurationResponse response = durationOnErrorResponse;

        verifyNoMoreInteractions(idProviderMock); // nothing saved so no data id generated

        assertEquals(
                expectedDefaultValues.getDomainId(),
                response.getDomainId()
        );

        // assert response metadata
        UseCaseMetadataModel metadata = durationOnErrorResponse.getMetadata();

        ComponentState expectedState = UseCaseMetadata.ComponentState.VALID_DEFAULT;
        ComponentState actualComponentState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualComponentState
        );

        // assert fail reasons
        List<FailReasons> expectedFailReasons = Arrays.asList(
                CommonFailReason.NONE, CommonFailReason.DATA_UNAVAILABLE
        );
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_invalidPrepHours_invalidValueNotSaved() {
        // Arrange
        // execute and test a new domain Id only request that returns default state
        newRequest_componentStateVALID_DEFAULT();

        // arrange persistent model that SHOULD NOT be saved as represents error state
        RecipeDurationPersistenceModel expectedInvalidStateModel = TestDataRecipeDuration.
                getNewInvalidPrepHours();

        // arrange invalid prep hours request
        RecipeDurationRequest.DomainModel invalidModel = new RecipeDurationRequest.DomainModel.
                Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setPrepHours(getHours(expectedInvalidStateModel.getPrepTime())).
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
        // Arrange / Act
        // execute and test invalid prep hours state
        newRequest_invalidPrepHours_invalidValueNotSaved();

        // Assert state
        assertEquals(
                UseCaseMetadata.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void newRequest_invalidPrepHours_FaiReasonINVALID_PREP_TIME() {
        // Arrange / Act
        // execute and test invalid prep hours state
        newRequest_invalidPrepHours_invalidValueNotSaved();

        // Assert fail reasons
        UseCaseMetadataModel metadata = durationOnErrorResponse.getMetadata();

        List<FailReasons> expectedFailReasons = Arrays.asList(
                FailReason.INVALID_PREP_TIME, CommonFailReason.DATA_UNAVAILABLE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                actualFailReasons,
                expectedFailReasons
        );
    }

    @Test
    public void newRequest_validPrepHours_prepHoursSaved() {
        // Arrange
        // execute and test a new domain Id only request that returns default state
        newRequest_componentStateVALID_DEFAULT();

        // arrange expected save
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getNewValidPrepTime();

        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());
        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());

        RecipeDurationRequest.DomainModel model = new RecipeDurationRequest.DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setPrepHours(getHours(MAX_PREP_TIME)).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(model).
                build();

        // Act
        SUT.execute(validRequest, new DurationCallbackClient());
        // Assert
        verify(repoMock).save(eq(modelUnderTest));
    }

    @Test
    public void newRequest_validPrepHours_resultVALID_CHANGED() {
        // Arrange / Act
        newRequest_validPrepHours_prepHoursSaved();

        // Assert
        assertEquals(
                UseCaseMetadata.ComponentState.VALID_CHANGED,
                durationOnSuccessResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void newRequest_validPrepHours_failReasonNONE() {
        // Arrange / Act
        newRequest_validPrepHours_prepHoursSaved();

        // Assert fail reasons
        UseCaseMetadataModel metadata = durationOnSuccessResponse.getMetadata();
        List<FailReasons> expectedFailReasons = Collections.singletonList(CommonFailReason.NONE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_invalidPrepMinutes_invalidValueNotSaved() {
        // Arrange
        // execute and test a new domain Id only request that returns default state
        newRequest_componentStateVALID_DEFAULT();

        // arrange persistent model that SHOULD NOT be saved as represents error state
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getNewInvalidPrepMinutes();

        // arrange invalid prep minutes
        RecipeDurationRequest.DomainModel model = new RecipeDurationRequest.DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setPrepMinutes(modelUnderTest.getPrepTime()).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(model).
                build();
        // Act
        SUT.execute(request, new DurationCallbackClient());
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void newRequest_invalidPrepMinutes_resultINVALID_CHANGED() {
        // Arrange
        // execute and test a new domain Id only request that returns default state
        newRequest_componentStateVALID_DEFAULT();

        // arrange persistent model that SHOULD NOT be saved as represents error state
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getNewInvalidPrepMinutes();

        // arrange invalid prep minutes
        RecipeDurationRequest.DomainModel model = new RecipeDurationRequest.DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setPrepMinutes(modelUnderTest.getPrepTime()).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(model).
                build();
        // Act
        SUT.execute(request, new DurationCallbackClient());
        // Assert
        assertEquals(
                UseCaseMetadata.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void newRequest_invalidPrepMinutes_FaiReasonINVALID_PREP_TIME() {
        // Arrange
        // execute and test a new domain Id only request that returns default state
        newRequest_componentStateVALID_DEFAULT();

        // arrange persistent model that SHOULD NOT be saved as represents error state
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getNewInvalidPrepMinutes();

        // arrange invalid prep minutes request
        RecipeDurationRequest.DomainModel model = new RecipeDurationRequest.DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setPrepMinutes(modelUnderTest.getPrepTime()).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(model).
                build();
        // Act
        SUT.execute(request, new DurationCallbackClient());
        // Assert
        UseCaseMetadataModel metadata = durationOnErrorResponse.getMetadata();

        List<FailReasons> expectedFailReasons = Arrays.asList(
                FailReason.INVALID_PREP_TIME,
                CommonFailReason.DATA_UNAVAILABLE
        );
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_validPrepMinutes_resultVALID_CHANGED() {
        // Arrange
        // execute and test a new domain Id only request that returns default state
        newRequest_componentStateVALID_DEFAULT();

        // Arrange persistence model that should be saved
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getNewValidPrepTime();

        // arrange valid prep minutes request
        RecipeDurationRequest.DomainModel model = new RecipeDurationRequest.DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setPrepMinutes(modelUnderTest.getPrepTime()).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(model).
                build();
        // Act
        SUT.execute(validRequest, new DurationCallbackClient());
        // Assert
        assertEquals(
                UseCaseMetadata.ComponentState.VALID_CHANGED,
                durationOnSuccessResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void newRequest_validPrepMinutes_failReasonNONE() {
        // Arrange
        // execute and test a new domain Id only request that returns default state
        newRequest_componentStateVALID_DEFAULT();

        // Arrange persistence model that should be saved
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getNewValidPrepTime();

        // arrange valid prep minutes request
        RecipeDurationRequest.DomainModel model = new RecipeDurationRequest.DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setPrepMinutes(modelUnderTest.getPrepTime()).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(model).
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
        // Arrange
        // execute and test a new domain Id only request that returns default state
        newRequest_componentStateVALID_DEFAULT();

        // Arrange persistence model that should be saved
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getNewValidPrepTime();

        // arrange valid prep minutes request
        RecipeDurationRequest.DomainModel model = new RecipeDurationRequest.DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setPrepMinutes(modelUnderTest.getPrepTime()).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(model).
                build();

        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getLastUpdate());

        // Act
        SUT.execute(validRequest, new DurationCallbackClient());
        // Assert
        verify(repoMock).save(eq(modelUnderTest));
    }

    @Test
    public void newRequest_validPrepMinutes_previousSavedStateArchived() {
        // Arrange
        // execute and test a new domain Id only request that returns default state
        newRequest_componentStateVALID_DEFAULT();

        // arrange persistence model that should be saved
        RecipeDurationPersistenceModel savedModel = TestDataRecipeDuration.
                getNewValidPrepTime();

        // arrange valid prep minutes request
        RecipeDurationRequest.DomainModel model = new RecipeDurationRequest.DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setPrepMinutes(savedModel.getPrepTime()).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(model).
                build();

        when(idProviderMock.getUId()).thenReturn(savedModel.getDataId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(savedModel.getCreateDate());

        // Act
        SUT.execute(validRequest, new DurationCallbackClient());

        // Assert
        verify(repoMock).save(eq(savedModel));
    }

    @Test
    public void newRequest_invalidCookHours_invalidValueNotSaved() {
        // Arrange
        // execute and test a new domain Id only request that returns default state
        newRequest_componentStateVALID_DEFAULT();

        // arrange persistence model that SHOULD NOT BE SAVED
        RecipeDurationPersistenceModel invalidCookHoursModel = TestDataRecipeDuration.
                getNewInvalidCookHours();

        // arrange invalid request
        RecipeDurationRequest.DomainModel model = new RecipeDurationRequest.DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setCookHours(getHours(invalidCookHoursModel.getCookTime())).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(model).
                build();
        // Act
        SUT.execute(invalidRequest, new DurationCallbackClient());
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void newRequest_invalidCookHours_resultINVALID_CHANGED() {
        // Arrange
        // execute and test a new domain Id only request that returns default state
        newRequest_componentStateVALID_DEFAULT();

        // arrange persistence model that SHOULD NOT BE SAVED
        RecipeDurationPersistenceModel invalidCookHoursModel = TestDataRecipeDuration.
                getNewInvalidCookHours();

        // arrange invalid request
        RecipeDurationRequest.DomainModel model = new RecipeDurationRequest.DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setCookHours(getHours(invalidCookHoursModel.getCookTime())).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(model).
                build();
        // Act
        SUT.execute(invalidRequest, new DurationCallbackClient());
        // Assert
        assertEquals(
                UseCaseMetadata.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void newRequest_invalidCookHours_failReasonINVALID_COOK_TIME() {
        // Arrange
        // execute and test a new domain Id only request that returns default state
        newRequest_componentStateVALID_DEFAULT();

        // arrange persistence model that SHOULD NOT BE SAVED
        RecipeDurationPersistenceModel invalidCookHoursModel = TestDataRecipeDuration.
                getNewInvalidCookHours();

        // arrange invalid request
        RecipeDurationRequest.DomainModel model = new RecipeDurationRequest.DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setCookHours(getHours(invalidCookHoursModel.getCookTime())).
                build();
        RecipeDurationRequest invalidRequest = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(model).
                build();
        // Act
        SUT.execute(invalidRequest, new DurationCallbackClient());
        UseCaseMetadataModel metadata = durationOnErrorResponse.getMetadata();

        List<FailReasons> expectedFailReasons = Arrays.asList(
                FailReason.INVALID_COOK_TIME,
                CommonFailReason.DATA_UNAVAILABLE
        );
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                actualFailReasons,
                expectedFailReasons
        );
    }

    @Test
    public void newRequest_validCookHours_validValueSaved() {
        // Arrange
        // execute and test a new domain Id only request that returns default state
        newRequest_componentStateVALID_DEFAULT();

        // arrange valid persistence model
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getNewValidCookTime();

        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getCreateDate());

        RecipeDurationRequest.DomainModel model = new RecipeDurationRequest.DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setCookHours(getHours(modelUnderTest.getCookTime())).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(model).
                build();
        // Act
        SUT.execute(validRequest, new DurationCallbackClient());
        // Assert
        verify(repoMock).save(eq(modelUnderTest));
    }

    @Test
    public void newRequest_validCookHours_resultVALID_CHANGED() {
        // Arrange
        // execute and test a new domain Id only request that returns default state
        newRequest_componentStateVALID_DEFAULT();

        // arrange valid persistence model
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getNewValidCookTime();

        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getCreateDate());

        RecipeDurationRequest.DomainModel model = new RecipeDurationRequest.DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setCookHours(getHours(modelUnderTest.getCookTime())).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(model).
                build();
        // Act
        SUT.execute(validRequest, new DurationCallbackClient());
        // Assert
        assertEquals(
                UseCaseMetadata.ComponentState.VALID_CHANGED,
                durationOnSuccessResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void newRequest_validCookHours_failReasonNONE() {
        // Arrange
        // execute and test a new domain Id only request that returns default state
        newRequest_componentStateVALID_DEFAULT();

        // arrange valid persistence model
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getNewValidCookTime();

        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getCreateDate());

        RecipeDurationRequest.DomainModel model = new RecipeDurationRequest.DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setCookHours(getHours(modelUnderTest.getCookTime())).
                build();
        RecipeDurationRequest validRequest = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(model).
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
        // execute and test a new domain Id only request that returns default state
        newRequest_componentStateVALID_DEFAULT();

        // arrange valid persistence model that SHOULD NOT be saved
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getInvalidNewCookMinutes();

        RecipeDurationRequest.DomainModel model = new RecipeDurationRequest.DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setCookMinutes(modelUnderTest.getCookTime()).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(model).
                build();
        // Act
        SUT.execute(request, new DurationCallbackClient());
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void newRequest_invalidCookMinutes_resultINVALID_CHANGED() {
        // Arrange
        // execute and test a new domain Id only request that returns default state
        newRequest_componentStateVALID_DEFAULT();

        // arrange valid persistence model that SHOULD NOT be saved
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getInvalidNewCookMinutes();

        RecipeDurationRequest.DomainModel model = new RecipeDurationRequest.DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setCookMinutes(modelUnderTest.getCookTime()).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(model).
                build();
        // Act
        SUT.execute(request, new DurationCallbackClient());
        // Assert
        assertEquals(
                UseCaseMetadata.ComponentState.INVALID_CHANGED,
                durationOnErrorResponse.getMetadata().getComponentState());
    }

    @Test
    public void newRequest_invalidCookMinutes_failReasonINVALID_COOK_TIME() {
        // Arrange
        // execute and test a new domain Id only request that returns default state
        newRequest_componentStateVALID_DEFAULT();

        // arrange valid persistence model that SHOULD NOT be saved
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getInvalidNewCookMinutes();

        RecipeDurationRequest.DomainModel model = new RecipeDurationRequest.DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setCookMinutes(modelUnderTest.getCookTime()).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(model).
                build();
        // Act
        SUT.execute(request, new DurationCallbackClient());
        // Assert
        UseCaseMetadataModel metadata = durationOnErrorResponse.getMetadata();

        List<FailReasons> expectedFailReasons = Arrays.asList(
                FailReason.INVALID_COOK_TIME,
                CommonFailReason.DATA_UNAVAILABLE
        );
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_validCookMinutes_validValueSaved() {
        // Arrange
        // execute and test a new domain Id only request that returns default state
        newRequest_componentStateVALID_DEFAULT();

        // arrange valid persistence model
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getNewValidCookTime();

        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId()
        );
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getCreateDate()
        );

        RecipeDurationRequest.DomainModel model = new RecipeDurationRequest.DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setCookMinutes(MAX_COOK_TIME)
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(model).
                build();
        // Act
        SUT.execute(request, new DurationCallbackClient());
        // Assert
        verify(repoMock).save(eq(modelUnderTest));
    }

    @Test
    public void newRequest_validCookMinutes_stateVALID_CHANGED() {
        // Arrange
        // execute and test a new domain Id only request that returns default state
        newRequest_componentStateVALID_DEFAULT();

        // arrange valid persistence model
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getNewValidCookTime();

        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getCreateDate());
        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());

        RecipeDurationRequest.DomainModel model = new RecipeDurationRequest.DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setCookMinutes(modelUnderTest.getCookTime())
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(model).
                build();
        // Act
        SUT.execute(request, new DurationCallbackClient());
        // Assert
        assertEquals(
                UseCaseMetadata.ComponentState.VALID_CHANGED,
                durationOnSuccessResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void newRequest_validCookMinutes_failReasonNONE() {
        // Arrange
        // execute and test a new domain Id only request that returns default state
        newRequest_componentStateVALID_DEFAULT();

        // arrange valid persistence model
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getNewValidCookTime();

        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getCreateDate());
        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());

        RecipeDurationRequest.DomainModel model = new RecipeDurationRequest.DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setCookMinutes(modelUnderTest.getCookTime())
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(model).
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
        // execute and test a new domain Id only request that returns default state
        newRequest_componentStateVALID_DEFAULT();

        // arrange invalid persistence model
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getNewInvalidPrepTimeInvalidCookTime();

        RecipeDurationRequest.DomainModel model = new RecipeDurationRequest.DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setPrepHours(getHours(modelUnderTest.getPrepTime())).
                setPrepMinutes(getMinutes(modelUnderTest.getPrepTime())).
                setCookHours(getHours(modelUnderTest.getCookTime())).
                setCookMinutes(getMinutes(modelUnderTest.getCookTime())).
                build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(model).
                build();
        // Act
        SUT.execute(request, new DurationCallbackClient());
        // Assert
        UseCaseMetadataModel metadata = durationOnErrorResponse.getMetadata();

        assertEquals(
                UseCaseMetadata.ComponentState.INVALID_CHANGED,
                metadata.getComponentState()
        );

        List<FailReasons> expectedFailReasons = Arrays.asList(
                FailReason.INVALID_PREP_TIME,
                FailReason.INVALID_COOK_TIME,
                CommonFailReason.DATA_UNAVAILABLE
        );
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_validPrepAndValidCookTime_failReasonsNONE() {
        // Arrange
        // execute and test a new domain Id only request that returns default state
        newRequest_componentStateVALID_DEFAULT();

        // arrange valid persistence model
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getNewValidPrepTimeValidCookTime();

        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getCreateDate());
        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());

        RecipeDurationRequest.DomainModel model = new RecipeDurationRequest.DomainModel.Builder().
                basedOnResponseModel(durationOnErrorResponse.getDomainModel()).
                setPrepHours(getHours(modelUnderTest.getPrepTime())).
                setPrepMinutes(getMinutes(modelUnderTest.getPrepTime())).
                setCookHours(getHours(modelUnderTest.getCookTime())).
                setCookMinutes(getMinutes(modelUnderTest.getCookTime()))
                .build();
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
                basedOnResponse(durationOnErrorResponse).
                setDomainModel(model).
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
    public void existingRequest_validDomainId_domainIdSentToRepo() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getExistingValidPrepTimeValidCookTime();

        // arrange request to load existing model
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().getDefault().
                setDomainId(modelUnderTest.getDomainId()).
                build();

        // Act
        SUT.execute(request, new DurationCallbackClient());

        // Assert
        verify(repoMock).getActiveByDomainId(
                eq(modelUnderTest.getDomainId()),
                repoDurationCallback.capture()
        );
        repoDurationCallback.getValue().onPersistenceModelLoaded(modelUnderTest);
    }

    @Test
    public void existingRequest_validPrepAndCookTime_correctValuesReturned() {
        // Arrange
        // arrange and return data from persistence
        existingRequest_validDomainId_domainIdSentToRepo();

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
        // Arrange
        // arrange and return data from persistence
        existingRequest_validDomainId_domainIdSentToRepo();
        // Assert
        assertEquals(
                UseCaseMetadata.ComponentState.VALID_UNCHANGED,
                durationOnSuccessResponse.getMetadata().getComponentState()
        );
    }

    @Test
    public void existingRequest_invalidPrepAndCookTime_statusINVALID_UNCHANGED() {
        // Arrange
        RecipeDurationPersistenceModel modelUnderTest = TestDataRecipeDuration.
                getExistingInvalidPreAndCookTime();

        // arrange request to load existing model
        RecipeDurationRequest request = new RecipeDurationRequest.Builder().getDefault().
                setDomainId(modelUnderTest.getDomainId()).
                build();

        // Act
        SUT.execute(request, new DurationCallbackClient());

        // Assert
        verify(repoMock).getActiveByDomainId(
                eq(modelUnderTest.getDomainId()),
                repoDurationCallback.capture()
        );
        repoDurationCallback.getValue().onPersistenceModelLoaded(modelUnderTest);

        assertEquals(
                UseCaseMetadata.ComponentState.INVALID_UNCHANGED,
                durationOnErrorResponse.getMetadata().getComponentState()
        );
    }

    // region helper methods -----------------------------------------------------------------------
    private int getHours(int totalTime) {
        return totalTime / 60;
    }

    private int getMinutes(int totalTime) {
        return totalTime % 60;
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
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
    // endregion helper classes --------------------------------------------------------------------
}