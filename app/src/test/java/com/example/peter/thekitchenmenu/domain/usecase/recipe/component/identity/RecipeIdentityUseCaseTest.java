package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RecipeIdentityUseCaseDataAccess;
import com.example.peter.thekitchenmenu.domain.businessentity.textvalidation.TextValidationBusinessEntity;
import com.example.peter.thekitchenmenu.domain.businessentity.textvalidation.TextValidationBusinessEntityTest;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

public class RecipeIdentityUseCaseTest {

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

    private RecipeIdentityResponse onSuccessResponse;
    private RecipeIdentityResponse onErrorResponse;
    // endregion helper fields ---------------------------------------------------------------------

    RecipeIdentityUseCase SUT;

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
    public void newRequest_stateINVALID_DEFAULT_failReasonDATA_UNAVAILABLE() {
        // Arrange
        RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().getDefault().build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());
        // Assert

    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private final class UseCaseCallbackImplementer
            implements
            UseCaseBase.Callback<RecipeIdentityResponse> {
        @Override
        public void onUseCaseSuccess(RecipeIdentityResponse response) {
            onSuccessResponse = response;
        }

        @Override
        public void onUseCaseError(RecipeIdentityResponse response) {
            onErrorResponse = response;
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}