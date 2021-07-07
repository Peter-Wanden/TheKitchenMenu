package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.invoker.metadata;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RecipeMetadataUseCaseDataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
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

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class RecipeMacroMetadataUseCaseTest {

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RecipeMetadataUseCaseDataAccess dataAccessMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<RecipeMacroMetadataUseCasePersistenceModel>> dataAccessCallback;
    @Mock
    UniqueIdProvider idProviderMock;
    @Mock
    TimeProvider timeProviderMock;

    private UseCaseResponse<RecipeMacroMetadataUseCaseResponseModel> onSuccessResponse;
    private UseCaseResponse<RecipeMacroMetadataUseCaseResponseModel> onErrorResponse;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeMacroMetadataUseCase SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();
    }

    @Test
    public void emptyRequest_stateINVALID_DEFAULT_failReasonDATA_UNAVAILABLE() {
        // Arrange
        RecipeMacroMetadataUseCaseRequest request = new RecipeMacroMetadataUseCaseRequest.Builder()
                .getDefault()
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        // Assert response values
        UseCaseResponse<RecipeMacroMetadataUseCaseResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeMacroMetadataUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.VALID_DEFAULT;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Collections.singletonList(
                CommonFailReason.DATA_UNAVAILABLE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

        RecipeMacroMetadataUseCaseResponseModel expectedResponseModel =
                new RecipeMacroMetadataUseCaseResponseModel.Builder()
                .getDefault()
                .build();
        RecipeMacroMetadataUseCaseResponseModel actualResponseModel = response.getResponseModel();
        assertEquals(
                expectedResponseModel,
                actualResponseModel
        );

    }

    private RecipeMacroMetadataUseCase givenUseCase() {
        RecipeMacroMetadataDomainModelConverter converter = new RecipeMacroMetadataDomainModelConverter(
                timeProviderMock, idProviderMock
        );
        return new RecipeMacroMetadataUseCase(
                dataAccessMock,
                converter,
                TestDataRecipeMetadata.requiredComponentNames,
                TestDataRecipeMetadata.additionalComponentNames
        );
    }

    // region helper methods -----------------------------------------------------------------------
    private void simulateDataUnavailable(RecipeMacroMetadataUseCasePersistenceModel modelUnderTest) {
        // Arrange initialise use case
        SUT.execute(
                new RecipeMacroMetadataUseCaseRequest.Builder()
                        .getDefault()
                        .setDomainId(modelUnderTest.getDomainId())
                        .build(),

                new UseCaseCallbackImplementer()
        );
        // Assert
        verify(dataAccessMock).getByDomainId(eq(modelUnderTest.getDomainId()),
                dataAccessCallback.capture()
        );
        dataAccessCallback.getValue().onPersistenceModelUnavailable();
    }

    private void simulateLoadPersistenceModel(RecipeMacroMetadataUseCasePersistenceModel modelUnderTest) {
        // Arrange
        SUT.execute(
                new RecipeMacroMetadataUseCaseRequest.Builder()
                        .getDefault()
                        .setDomainId(modelUnderTest.getDomainId())
                        .build(),

                new UseCaseCallbackImplementer()
        );
        // Assert
        verify(dataAccessMock).getByDomainId(eq(modelUnderTest.getDomainId()),
                dataAccessCallback.capture());
        dataAccessCallback.getValue().onPersistenceModelLoaded(modelUnderTest);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private final class UseCaseCallbackImplementer
            implements
            UseCaseCallback<UseCaseResponse<RecipeMacroMetadataUseCaseResponseModel>> {
        @Override
        public void onSuccess(UseCaseResponse<RecipeMacroMetadataUseCaseResponseModel> response) {
            onSuccessResponse = response;
        }

        @Override
        public void onError(UseCaseResponse<RecipeMacroMetadataUseCaseResponseModel> response) {
            onErrorResponse = response;
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}