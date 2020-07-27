package com.example.peter.thekitchenmenu.domain.usecase.common;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.commonmocks.StringMaker;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.domain.model.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseResult.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses.TestDomainModelConverter;
import com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses.TestUseCase;
import com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses.TestUseCaseDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses.TestUseCasePersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses.TestUseCaseRequest;
import com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses.TestUseCaseResponse;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UseCaseResultTest {

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    TestUseCaseDataAccess dataAccessMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<TestUseCasePersistenceModel>> dataAccessCallbackCaptor;
    @Mock
    UniqueIdProvider idProviderMock;
    @Mock
    TimeProvider timeProviderMock;

    private TestUseCaseResponse onSuccessResponse;
    private TestUseCaseResponse onErrorResponse;
    // endregion helper fields ---------------------------------------------------------------------

    private TestUseCase SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();

    }

    private TestUseCase givenUseCase() {
        TestDomainModelConverter converter = new TestDomainModelConverter(
                timeProviderMock, idProviderMock
        );
        return new TestUseCase(
                dataAccessMock,
                converter,
                idProviderMock,
                timeProviderMock
        );
    }

    @Test
    public void newRequest_stateINVALID_DEFAULT_failReasonDATA_UNAVAILABLE() {
        // Arrange
        // an invalid default state occurs when the default values in the use case are invalid and
        // and the request has no data and a persistence model has not loaded
        TestUseCaseRequest request = new TestUseCaseRequest.Builder()
                .getDefault()
                .build();
        // set the default use case domain model value to be invalid
        SUT.useCaseModelDefaultValue = new StringMaker()
                .makeStringOfExactLength(TestUseCase.MIN_STRING_LENGTH)
                .thenRemoveOneCharacter()
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        TestUseCaseResponse response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getMetadata();
        // assert fail reasons
        List<FailReasons> expectedFailReasons = Arrays.asList(
                TestUseCase.FailReason.TEXT_TOO_SHORT,
                CommonFailReason.DATA_UNAVAILABLE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
        // assert state
        ComponentState expectedState = ComponentState.INVALID_UNCHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );
        // assert dates. Dates are only generated when a valid persistence model is saved. In this
        // case a save has not taken place, therefore dates will be 0L
        assertEquals(0L, metadata.getCreateDate());
        assertEquals(0L, metadata.getLastUpdate());
        // assert creator, this will always be the current user
        assertEquals(Constants.getUserId(), metadata.getCreatedBy());
    }

    @Test
    public void newRequest_stateVALID_DEFAULT_failReasonNONE() {
        // Arrange
        // an valid default state occurs when the default values in the use case are valid and
        // and the request has no data and a persistence model has not loaded
        TestUseCaseRequest request = new TestUseCaseRequest.Builder()
                .getDefault()
                .build();
        // set the default use case domain model value to be within a valid range
        SUT.useCaseModelDefaultValue = new StringMaker()
                .makeStringOfExactLength(TestUseCase.MIN_STRING_LENGTH)
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        TestUseCaseResponse response = onSuccessResponse;
        UseCaseMetadataModel metadata = response.getMetadata();
        // assert fail reasons
        List<FailReasons> expectedFailReasons = Arrays.asList(
                CommonFailReason.NONE,
                CommonFailReason.DATA_UNAVAILABLE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );
        // assert state
        ComponentState expectedState = ComponentState.VALID_UNCHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );
        // assert dates. Dates are only generated when a valid persistence model is saved. In this
        // case a save has not taken place, therefore dates will be 0L
        assertEquals(0L, metadata.getCreateDate());
        assertEquals(0L, metadata.getLastUpdate());
        // assert creator, this will always be the current user
        assertEquals(Constants.getUserId(), metadata.getCreatedBy());
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------

    private final class UseCaseCallbackImplementer
            implements
            UseCaseBase.Callback<TestUseCaseResponse> {
        @Override
        public void onUseCaseSuccess(TestUseCaseResponse response) {
            onSuccessResponse = response;
        }

        @Override
        public void onUseCaseError(TestUseCaseResponse response) {
            onErrorResponse = response;
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}