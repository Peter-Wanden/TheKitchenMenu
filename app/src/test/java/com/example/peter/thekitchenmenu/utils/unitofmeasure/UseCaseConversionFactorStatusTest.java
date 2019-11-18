package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseConversionFactorStatus;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseScheduler;
import com.example.peter.thekitchenmenu.testdata.TestDataIngredientEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataUseCaseConversionFactorStatusRequestResponse;

import org.junit.*;
import org.mockito.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class UseCaseConversionFactorStatusTest {

    // region constants ----------------------------------------------------------------------------
    private IngredientEntity INGREDIENT_NEW_VALID_NAME_DESCRIPTION =
            TestDataIngredientEntity.getNewInvalidNameValidDescription();
    private UseCaseConversionFactorStatus.RequestValues REQUEST_METRIC_NO_CONVERSION_FACTOR =
            TestDataUseCaseConversionFactorStatusRequestResponse.
                    getRequestMetricNoConversionFactor();
    private UseCaseConversionFactorStatus.ResponseValues RESPONSE_DISABLED =
            TestDataUseCaseConversionFactorStatusRequestResponse.
                    getResponseMetricNoConversionFactor();

    private IngredientEntity INGREDIENT_VALID_FROM_ANOTHER_USER =
            TestDataIngredientEntity.getExistingValidNameValidDescriptionFromAnotherUser();
    private UseCaseConversionFactorStatus.RequestValues REQUEST_METRIC_CONVERSION_FACTOR_UNEDITABLE =
            TestDataUseCaseConversionFactorStatusRequestResponse.
                    getRequestWithConversionFactorFromAnotherUser();
    private UseCaseConversionFactorStatus.ResponseValues RESPONSE_ENABLED_UNEDITABLE =
            TestDataUseCaseConversionFactorStatusRequestResponse.
                    getResponseConversionFactorUneditable();

    private IngredientEntity INGREDIENT_VALID_WITH_CONVERSION_FACTOR_UNSET =
            TestDataIngredientEntity.getNewInvalidNameValidDescription();
    private UseCaseConversionFactorStatus.RequestValues REQUEST_CONVERSION_FACTOR_UNSET =
            TestDataUseCaseConversionFactorStatusRequestResponse.
                    getRequestWithConversionFactorEnabledUnset();
    private UseCaseConversionFactorStatus.ResponseValues RESPONSE_ENABLED_UNSET =
            TestDataUseCaseConversionFactorStatusRequestResponse.
                    getResponseConversionFactorEnabledUnset();

    private IngredientEntity INGREDIENT_VALID_WITH_CONVERSION_FACTOR =
            TestDataIngredientEntity.getExistingValidWithConversionFactor();
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    private UseCaseHandlerMock handlerMock = new UseCaseHandlerMock(new UseCaseSchedulerMock());
    @Mock
    RepositoryIngredient repoMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<IngredientEntity>> getEntityCallbackCaptor;

    private UseCaseConversionFactorStatus.ResponseValues actualResponse;

    // endregion helper fields ---------------------------------------------------------------------

    private UseCaseConversionFactorStatus SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = new UseCaseConversionFactorStatus(repoMock);
    }

    @Test
    public void getConversionFactorStatus_disabledForUnitOfMeasure_DISABLED() {
        // Arrange
        String ingredientId = INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId();
        MeasurementSubtype subtype = REQUEST_METRIC_NO_CONVERSION_FACTOR.getSubtype();
        // Act
        handlerMock.execute(SUT, getRequestValues(subtype, ingredientId), getResponseCallback());
        // Assert
        verifyNoMoreInteractions(repoMock);
        assertEquals(RESPONSE_DISABLED, actualResponse);
    }

    @Test
    public void getConversionFactorStatus_userIsNotCreator_ENABLED_UNEDITABLE() {
        // Arrange
        String ingredientId = INGREDIENT_VALID_FROM_ANOTHER_USER.getId();
        MeasurementSubtype subtype = REQUEST_METRIC_CONVERSION_FACTOR_UNEDITABLE.getSubtype();
        // Act
        handlerMock.execute(SUT, getRequestValues(subtype, ingredientId), getResponseCallback());
        // Assert
        verifyRepoCalledWithIngredientValidFromAnotherUser();
        assertEquals(RESPONSE_ENABLED_UNEDITABLE, actualResponse);
    }

    @Test
    public void getConversionFactorStatus_noConversionFactorSet_ENABLED_EDITABLE_UNSET() {
        // Arrange
        String ingredientId = INGREDIENT_VALID_WITH_CONVERSION_FACTOR_UNSET.getId();
        MeasurementSubtype subtype = REQUEST_CONVERSION_FACTOR_UNSET.getSubtype();
        // Act
        handlerMock.execute(SUT, getRequestValues(subtype, ingredientId), getResponseCallback());
        // Assert
        verifyRepoCalledWithIngredientNewValidNameDescription();
        assertEquals(RESPONSE_ENABLED_UNSET, actualResponse);
    }

//    @Test
//    public void getConversionFactorStatus_conversionFactorSet_ENABLED_EDITABLE_SET() {
//        // Arrange
//        String ingredientId = INGREDIENT_VALID_WITH_CONVERSION_FACTOR.getId();
//        // Act
//        SUT.getStatus(EDITABLE_SUBTYPE, ingredientId);
//        verifyRepoCalledWithIngredientValidWithConversionFactor();
//        // Assert
//        verify(viewModelMock).useCaseConversionFactorResult(ENABLED_EDITABLE_SET);
//    }

    // region helper methods -----------------------------------------------------------------------
    private UseCaseConversionFactorStatus.RequestValues getRequestValues(MeasurementSubtype subtype,
                                                                         String ingredientId) {
        return new UseCaseConversionFactorStatus.RequestValues(subtype, ingredientId);
    }

    private UseCase.UseCaseCallback<UseCaseConversionFactorStatus.ResponseValues> getResponseCallback() {
        return new UseCase.UseCaseCallback<UseCaseConversionFactorStatus.ResponseValues>() {
            @Override
            public void onSuccess(UseCaseConversionFactorStatus.ResponseValues response) {
                UseCaseConversionFactorStatusTest.this.actualResponse = response;
            }

            @Override
            public void onError(UseCaseConversionFactorStatus.ResponseValues response) {
                UseCaseConversionFactorStatusTest.this.actualResponse = response;
            }
        };
    }

    private void verifyRepoCalledWithIngredientValidFromAnotherUser() {
        verify(repoMock).getById(eq(INGREDIENT_VALID_FROM_ANOTHER_USER.getId()),
                getEntityCallbackCaptor.capture());
        getEntityCallbackCaptor.getValue().onEntityLoaded(INGREDIENT_VALID_FROM_ANOTHER_USER);
    }

    private void verifyRepoCalledWithIngredientNewValidNameDescription() {
        verify(repoMock).getById(eq(INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId()),
                getEntityCallbackCaptor.capture());
        getEntityCallbackCaptor.getValue().onEntityLoaded(INGREDIENT_NEW_VALID_NAME_DESCRIPTION);
    }

    private void verifyRepoCalledWithIngredientValidWithConversionFactor() {
        verify(repoMock).getById(eq(INGREDIENT_VALID_WITH_CONVERSION_FACTOR.getId()),
                getEntityCallbackCaptor.capture());
        getEntityCallbackCaptor.getValue().onEntityLoaded(INGREDIENT_VALID_WITH_CONVERSION_FACTOR);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    public class UseCaseHandlerMock {
        private final UseCaseScheduler mUseCaseScheduler;

        UseCaseHandlerMock(UseCaseScheduler useCaseScheduler) {
            mUseCaseScheduler = useCaseScheduler;
        }

        <T extends UseCase.RequestValues, R extends UseCase.ResponseValues> void execute(
                final UseCase<T, R> useCase, T requestValues, UseCase.UseCaseCallback<R> callback) {

            useCase.setRequestValues(requestValues);
            useCase.setUseCaseCallback(new UiCallbackWrapper(callback, this));

            mUseCaseScheduler.execute(useCase::run);
        }

        public <V extends UseCase.ResponseValues> void notifyResponse(
                final V response,
                final UseCase.UseCaseCallback<V> useCaseCallback) {

            mUseCaseScheduler.notifyResponse(response, useCaseCallback);
        }

        private <V extends UseCase.ResponseValues> void notifyError(
                final V response,
                final UseCase.UseCaseCallback<V> useCaseCallback) {

            mUseCaseScheduler.onError(response, useCaseCallback);
        }

        private final class UiCallbackWrapper<V extends UseCase.ResponseValues> implements
                UseCase.UseCaseCallback<V> {

            private final UseCase.UseCaseCallback<V> mCallback;
            private final UseCaseHandlerMock mUseCaseHandler;

            public UiCallbackWrapper(UseCase.UseCaseCallback<V> callback,
                                     UseCaseHandlerMock useCaseHandler) {
                mCallback = callback;
                mUseCaseHandler = useCaseHandler;
            }

            @Override
            public void onSuccess(V response) {
                mUseCaseHandler.notifyResponse(response, mCallback);
            }

            @Override
            public void onError(V response) {
                mUseCaseHandler.notifyError(response, mCallback);
            }
        }
    }

    public class UseCaseSchedulerMock implements UseCaseScheduler {
        @Override
        public void execute(Runnable runnable) {
            runnable.run();
        }

        @Override
        public <V extends UseCase.ResponseValues> void notifyResponse(
                V response, UseCase.UseCaseCallback<V> useCaseCallback) {
            useCaseCallback.onSuccess(response);
        }

        @Override
        public <V extends UseCase.ResponseValues> void onError(
                V response, UseCase.UseCaseCallback<V> useCaseCallback) {
            useCaseCallback.onError(response);
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}