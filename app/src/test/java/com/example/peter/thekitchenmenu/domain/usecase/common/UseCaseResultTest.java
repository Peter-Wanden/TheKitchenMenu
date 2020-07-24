package com.example.peter.thekitchenmenu.domain.usecase.common;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses.TestDomainModelConverter;
import com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses.TestUseCase;
import com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses.TestUseCaseDataAccess;
import com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses.TestUseCasePersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses.TestUseCaseResponse;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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
    // endregion helper fields ---------------------------------------------------------------------

    private TestUseCase SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();

    }

    private TestUseCase givenUseCase() {
        return new TestUseCase(
                dataAccessMock,
                new TestDomainModelConverter(timeProviderMock, idProviderMock),
                idProviderMock,
                timeProviderMock
        );
    }



    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------

    private static final class UseCaseCallbackImplementer
            implements
            UseCaseBase.Callback<TestUseCaseResponse> {
        @Override
        public void onUseCaseSuccess(TestUseCaseResponse testUseCaseResponse) {

        }

        @Override
        public void onUseCaseError(TestUseCaseResponse testUseCaseResponse) {

        }
    }
    // endregion helper classes --------------------------------------------------------------------
}