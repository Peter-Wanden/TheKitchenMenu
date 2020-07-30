package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.identity;

import com.example.peter.thekitchenmenu.commonmocks.StringMaker;
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

    private UseCaseResponse<RecipeIdentityResponseModel> onSuccessResponse;
    private UseCaseResponse<RecipeIdentityResponseModel> onErrorResponse;
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
    public void newRequest_stateINVALID_DEFAULT_failReasonTITLE_TOO_SHORT_DATA_UNAVAILABLE() {
        // Arrange
        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder()
                .getDefault()
                .build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert
        UseCaseResponse<RecipeIdentityResponseModel> response = onErrorResponse;
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
    public void newRequest_stateINVALID_DEFAULT_failReasonDATA_UNAVAILABLE() {
        // Arrange
        // This is the initial pre-test setup request for other tests cases, so check all
        // return values
        RecipeIdentityUseCasePersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getNewInvalidActiveDefault();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder()
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

        UseCaseResponse<RecipeIdentityResponseModel> response = onErrorResponse;
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
    public void newRequest_stateINVALID_CHANGED_failReasonsTITLE_TOO_LONG_DESCROPTION_TOO_LONG() {
        // Arrange
        String expectedTitleTooLong = new StringMaker()
                .makeStringOfLength(TextValidationBusinessEntityTest.SHORT_TEXT_MAX_LENGTH)
                .thenAddOneCharacter()
                .build();
        String expectedDescriptionTooLong = new StringMaker()
                .makeStringOfLength(TextValidationBusinessEntityTest.LONG_TEXT_MAX_LENGTH)
                .thenAddOneCharacter()
                .build();

        // simulate use case data unavailable to initialise the use case
        newRequest_stateINVALID_DEFAULT_failReasonDATA_UNAVAILABLE();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipeIdentityRequestModel.Builder()
                        .setTitle(expectedTitleTooLong)
                        .setDescription(expectedDescriptionTooLong)
                        .build())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        UseCaseResponse<RecipeIdentityResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeIdentityResponseModel responseModel = response.getResponseModel();

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
                expectedTitleTooLong,
                responseModel.getTitle()
        );
        assertEquals(
                expectedDescriptionTooLong,
                responseModel.getDescription()
        );
    }

    @Test
    public void newRequest_stateINVALID_CHANGED_failReasonsTITLE_TOO_SHORT() {
        // Arrange
        String expectedTitleTooShort = new StringMaker()
                .makeStringOfLength(TextValidationBusinessEntityTest.SHORT_TEXT_MIN_LENGTH)
                .thenRemoveOneCharacter()
                .build();
        String expectedValidDescription = new StringMaker()
                .makeStringOfLength(TextValidationBusinessEntityTest.LONG_TEXT_MAX_LENGTH)
                .build();

        newRequest_stateINVALID_DEFAULT_failReasonDATA_UNAVAILABLE();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipeIdentityRequestModel.Builder()
                        .setTitle(expectedTitleTooShort)
                        .setDescription(expectedValidDescription)
                        .build())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        UseCaseResponse<RecipeIdentityResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeIdentityResponseModel domainModel = response.getResponseModel();

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
                expectedTitleTooShort,
                domainModel.getTitle()
        );

        assertEquals(
                expectedValidDescription,
                domainModel.getDescription()
        );
    }

    @Test
    public void newRequest_stateVALID_CHANGED_validTitlePersisted() {
        // Arrange
        RecipeIdentityUseCasePersistenceModel modelUnderTest = TestDataRecipeIdentity.
                getValidNewTitleValid();

        // initialise use case
        newRequest_stateINVALID_DEFAULT_failReasonDATA_UNAVAILABLE();

        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipeIdentityRequestModel.Builder()
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

        UseCaseResponse<RecipeIdentityResponseModel> response = onSuccessResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeIdentityResponseModel responseModel = response.getResponseModel();

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

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private final class UseCaseCallbackImplementer
            implements
            UseCaseCallback<UseCaseResponse<RecipeIdentityResponseModel>> {

        @Override
        public void onSuccess(UseCaseResponse<RecipeIdentityResponseModel> response) {
            onSuccessResponse = response;
        }

        @Override
        public void onError(UseCaseResponse<RecipeIdentityResponseModel> response) {
            onErrorResponse = response;
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}