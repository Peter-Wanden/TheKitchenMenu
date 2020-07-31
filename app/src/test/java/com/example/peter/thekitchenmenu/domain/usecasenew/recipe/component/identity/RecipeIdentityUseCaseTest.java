package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.identity;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RecipeIdentityUseCaseDataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.identity.TestDataRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.businessentity.textvalidation.TextValidationBusinessEntity;
import com.example.peter.thekitchenmenu.domain.businessentity.textvalidation.TextValidationBusinessEntityTest;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseCallback;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseResponse;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.UseCaseMetadataModel;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RecipeIdentityUseCaseTest {

//    private static final String TAG = "tkm-" + RecipeIdentityUseCaseTest.class.getSimpleName() +
//            ": ";

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RecipeIdentityUseCaseDataAccess dataAccessMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<RecipeIdentityUseCasePersistenceModel>> dataAccessCallback;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    UniqueIdProvider idProviderMock;

    private UseCaseResponse<RecipeIdentityUseCaseResponseModel> onSuccessResponse;
    private UseCaseResponse<RecipeIdentityUseCaseResponseModel> onErrorResponse;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeIdentityUseCase SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();
    }

    private RecipeIdentityUseCase givenUseCase() {
        TextValidationBusinessEntity textValidator = new TextValidationBusinessEntity(
                TextValidationBusinessEntityTest.SHORT_TEXT_MIN_LENGTH,
                TextValidationBusinessEntityTest.SHORT_TEXT_MAX_LENGTH,
                TextValidationBusinessEntityTest.LONG_TEXT_MIN_LENGTH,
                TextValidationBusinessEntityTest.LONG_TEXT_MAX_LENGTH
        );
        RecipeIdentityDomainModelConverter converter = new RecipeIdentityDomainModelConverter(
                timeProviderMock, idProviderMock
        );
        return new RecipeIdentityUseCase(dataAccessMock, converter, textValidator);
    }

    @Test
    public void emptyRequest_stateINVALID_DEFAULT_failReasonDATA_UNAVAILABLE_TITLE_TOO_SHORT() {
        // Arrange
        RecipeIdentityUseCaseRequest request = new RecipeIdentityUseCaseRequest.Builder()
                .getDefault()
                .build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert
        UseCaseResponse<RecipeIdentityUseCaseResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();

        ComponentState expectedState = ComponentState.INVALID_DEFAULT;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Arrays.asList(
                RecipeIdentityUseCaseFailReason.TITLE_TOO_SHORT,
                CommonFailReason.DATA_UNAVAILABLE
        );
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_stateINVALID_DEFAULT_failReasonDATA_UNAVAILABLE_TITLE_TOO_SHORT() {
        // Arrange
        // return values
        RecipeIdentityUseCasePersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getNewInvalidActiveDefault();

        RecipeIdentityUseCaseRequest request = new RecipeIdentityUseCaseRequest.Builder()
                .getDefault()
                .setDomainId(modelUnderTest.getDomainId())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        // assert persistence called with correct id
        verify(dataAccessMock).getByDomainId(eq(modelUnderTest.getDomainId()),
                dataAccessCallback.capture());
        dataAccessCallback.getValue().onPersistenceModelUnavailable();

        // verify no save
        verifyNoMoreInteractions(dataAccessMock);

        UseCaseResponse<RecipeIdentityUseCaseResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();

        ComponentState expectedState = ComponentState.INVALID_DEFAULT;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Arrays.asList(
                RecipeIdentityUseCaseFailReason.TITLE_TOO_SHORT,
                CommonFailReason.DATA_UNAVAILABLE
        );
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
    }

    @Test
    public void newRequest_stateINVALID_CHANGED_failReasons_DATA_UNAVAILABLE_TITLE_TOO_LONG_DESCRIPTION_TOO_LONG() {
        // Arrange
        RecipeIdentityUseCasePersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidNewTitleTooLongDescriptionTooLong();

        // simulate use case data unavailable to initialise the use case
        simulateDataUnavailable(modelUnderTest);

        RecipeIdentityUseCaseRequest request = new RecipeIdentityUseCaseRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipeIdentityUseCaseRequestModel.Builder()
                        .setTitle(modelUnderTest.getTitle())
                        .setDescription(modelUnderTest.getDescription())
                        .build())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        UseCaseResponse<RecipeIdentityUseCaseResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeIdentityUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Arrays.asList(
                RecipeIdentityUseCaseFailReason.TITLE_TOO_LONG,
                RecipeIdentityUseCaseFailReason.DESCRIPTION_TOO_LONG,
                CommonFailReason.DATA_UNAVAILABLE
        );
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

        // assert domain model values
        assertEquals(
                modelUnderTest.getTitle(),
                responseModel.getTitle()
        );
        assertEquals(
                modelUnderTest.getDescription(),
                responseModel.getDescription()
        );
    }

    @Test
    public void newRequest_stateINVALID_CHANGED_failReasonsDATA_UNAVAILABLE_TITLE_TOO_SHORT() {
        // Arrange
        RecipeIdentityUseCasePersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidNewTitleTooShortDescriptionValid();

        simulateDataUnavailable(modelUnderTest);

        RecipeIdentityUseCaseRequest request = new RecipeIdentityUseCaseRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipeIdentityUseCaseRequestModel.Builder()
                        .setTitle(modelUnderTest.getTitle())
                        .setDescription(modelUnderTest.getDescription())
                        .build())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        UseCaseResponse<RecipeIdentityUseCaseResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeIdentityUseCaseResponseModel domainModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Arrays.asList(
                RecipeIdentityUseCaseFailReason.TITLE_TOO_SHORT,
                CommonFailReason.DATA_UNAVAILABLE
        );
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

        assertEquals(
                modelUnderTest.getTitle(),
                domainModel.getTitle()
        );
        assertEquals(
                modelUnderTest.getDescription(),
                domainModel.getDescription()
        );
    }

    @Test
    public void newRequest_stateVALID_CHANGED_validTitlePersisted_failReasonNONE() {
        // Arrange
        RecipeIdentityUseCasePersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidNewTitleValid();

        // initialise use case
        simulateDataUnavailable(modelUnderTest);

        RecipeIdentityUseCaseRequest request = new RecipeIdentityUseCaseRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipeIdentityUseCaseRequestModel.Builder()
                        .setTitle(modelUnderTest.getTitle())
                        .setDescription(modelUnderTest.getDescription())
                        .build())
                .build();

        // expected save, will need time and data id
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getCreateDate());
        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        verify(dataAccessMock).save(eq(modelUnderTest));

        UseCaseResponse<RecipeIdentityUseCaseResponseModel> response = onSuccessResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeIdentityUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.VALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Collections.singletonList(CommonFailReason.NONE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

        assertEquals(
                modelUnderTest.getTitle(),
                responseModel.getTitle()
        );
        assertEquals(
                modelUnderTest.getDescription(),
                responseModel.getDescription()
        );
    }

    @Test
    public void existingRequest_stateVALID_UNCHANGED_titleValidDescriptionValid_failReasonNONE() {
        // Arrange
        RecipeIdentityUseCasePersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidExistingTitleValidDescriptionValid();

        // Act
        simulateLoadPersistenceModel(modelUnderTest);

        // Assert
        UseCaseResponse<RecipeIdentityUseCaseResponseModel> response = onSuccessResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeIdentityUseCaseResponseModel model = response.getResponseModel();

        ComponentState expectedState = ComponentState.VALID_UNCHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Collections.singletonList(CommonFailReason.NONE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

        assertEquals(
                modelUnderTest.getTitle(),
                model.getTitle()
        );
        assertEquals(
                modelUnderTest.getDescription(),
                model.getDescription()
        );
    }

    @Test
    public void existingRequest_stateINVALID_UNCHANGED_failReasonTITLE_TOO_SHORT() {
        // Arrange
        RecipeIdentityUseCasePersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getInvalidExistingTitleTooShort();

        // Act
        simulateLoadPersistenceModel(modelUnderTest);

        // Assert
        UseCaseResponse<RecipeIdentityUseCaseResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeIdentityUseCaseResponseModel model = response.getResponseModel();

        ComponentState expectedState = ComponentState.INVALID_UNCHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Collections.singletonList(
                RecipeIdentityUseCaseFailReason.TITLE_TOO_SHORT);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

        assertEquals(
                modelUnderTest.getTitle(),
                model.getTitle()
        );
        assertEquals(
                modelUnderTest.getDescription(),
                model.getDescription()
        );
    }

    @Test
    public void newRequest_stateINVALID_CHANGED_failReasonDATA_UNAVAILABLE_TITLE_NULL_DESCRIPTION_NULL() {
        // Arrange
        newRequest_stateINVALID_DEFAULT_failReasonDATA_UNAVAILABLE_TITLE_TOO_SHORT();

        RecipeIdentityUseCaseRequest request = new RecipeIdentityUseCaseRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipeIdentityUseCaseRequestModel.Builder()
                        .setTitle(null)
                        .setDescription(null)
                        .build())
                .build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        UseCaseResponse<RecipeIdentityUseCaseResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeIdentityUseCaseResponseModel model = response.getResponseModel();

        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Arrays.asList(
                RecipeIdentityUseCaseFailReason.TITLE_NULL,
                RecipeIdentityUseCaseFailReason.DESCRIPTION_NULL,
                CommonFailReason.DATA_UNAVAILABLE
        );
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

        assertNull(model.getTitle());
        assertNull(model.getDescription());
    }

    // region helper methods -----------------------------------------------------------------------
    private void simulateDataUnavailable(RecipeIdentityUseCasePersistenceModel modelUnderTest) {
        // Arrange
        RecipeIdentityUseCaseRequest request = new RecipeIdentityUseCaseRequest.Builder()
                .getDefault()
                .setDomainId(modelUnderTest.getDomainId())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        verify(dataAccessMock).getByDomainId(eq(modelUnderTest.getDomainId()),
                dataAccessCallback.capture()
        );
        dataAccessCallback.getValue().onPersistenceModelUnavailable();
    }

    private void simulateLoadPersistenceModel(RecipeIdentityUseCasePersistenceModel modelUnderTest) {
        // Arrange
        RecipeIdentityUseCaseRequest request = new RecipeIdentityUseCaseRequest.Builder()
                .getDefault()
                .setDomainId(modelUnderTest.getDomainId())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        verify(dataAccessMock).getByDomainId(eq(modelUnderTest.getDomainId()),
                dataAccessCallback.capture()
        );
        dataAccessCallback.getValue().onPersistenceModelLoaded(modelUnderTest);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private final class UseCaseCallbackImplementer
            implements
            UseCaseCallback<UseCaseResponse<RecipeIdentityUseCaseResponseModel>> {

        @Override
        public void onSuccess(UseCaseResponse<RecipeIdentityUseCaseResponseModel> response) {
            onSuccessResponse = response;
        }

        @Override
        public void onError(UseCaseResponse<RecipeIdentityUseCaseResponseModel> response) {
            onErrorResponse = response;
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}