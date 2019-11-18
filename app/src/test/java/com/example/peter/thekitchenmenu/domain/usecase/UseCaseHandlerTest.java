package com.example.peter.thekitchenmenu.domain.usecase;

import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.MeasurementSubtype;
import com.example.peter.thekitchenmenu.testdata.TestDataIngredientEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataUseCaseConversionFactorStatusRequestResponse;

import org.junit.*;
import org.mockito.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class UseCaseHandlerTest {

    // region constants ----------------------------------------------------------------------------
    private UseCaseConversionFactorStatus cFactorStatus;

    private IngredientEntity INGREDIENT_NEW_VALID_NAME_DESCRIPTION =
            TestDataIngredientEntity.getNewInvalidNameValidDescription();
    private UseCaseConversionFactorStatus.RequestValues REQUEST_METRIC_NO_CONV_FACTOR_VALID_INGREDIENT =
            TestDataUseCaseConversionFactorStatusRequestResponse.getRequestMetricNoConversionFactor();
    private UseCaseConversionFactorStatus.ResponseValues RESPONSE_DISABLED =
            TestDataUseCaseConversionFactorStatusRequestResponse.getResponseMetricNoConversionFactor();

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    //    @Rule
//    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Mock
    UseCaseThreadPoolScheduler schedulerMock;
    @Mock
    RepositoryIngredient repoIngredientMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<IngredientEntity>> getEntityCallbackCaptor;
    @Captor
    ArgumentCaptor<UseCase.UseCaseCallback<UseCaseConversionFactorStatus.ResponseValues>> responseCallback;
    // endregion helper fields ---------------------------------------------------------------------

    private UseCaseHandler SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = new UseCaseHandler(schedulerMock);

        cFactorStatus = new UseCaseConversionFactorStatus(repoIngredientMock);
    }

    @Test
    public void getConversionFactorStatus_disabled() {
        // Arrange
        MeasurementSubtype subtype = REQUEST_METRIC_NO_CONV_FACTOR_VALID_INGREDIENT.getSubtype();
        String ingredientId = REQUEST_METRIC_NO_CONV_FACTOR_VALID_INGREDIENT.getIngredientId();
        // Act
        SUT.execute(cFactorStatus, getRequestValues(subtype, ingredientId), getResponseCallback());
        // Assert
        verifyNoMoreInteractions(repoIngredientMock);

        verify(responseCallback.getValue()).onSuccess(eq(RESPONSE_DISABLED));
    }

    // region helper methods -----------------------------------------------------------------------
    private UseCaseConversionFactorStatus.RequestValues getRequestValues(MeasurementSubtype subtype,
                                                                         String ingredientId) {
        return new UseCaseConversionFactorStatus.RequestValues(subtype, ingredientId);
    }

    private UseCase.UseCaseCallback<UseCaseConversionFactorStatus.ResponseValues> getResponseCallback() {
        return new UseCase.UseCaseCallback<UseCaseConversionFactorStatus.ResponseValues>() {
            @Override
            public void onSuccess(UseCaseConversionFactorStatus.ResponseValues response) {

            }

            @Override
            public void onError(UseCaseConversionFactorStatus.ResponseValues response) {

            }
        };
    }

    private void verifyRepoCalledWithIngredientNewValidNameDescription() {
        String ingredientId = INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId();
        verify(repoIngredientMock).getById(eq(ingredientId), getEntityCallbackCaptor.capture());
        getEntityCallbackCaptor.getValue().onEntityLoaded(INGREDIENT_NEW_VALID_NAME_DESCRIPTION);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}