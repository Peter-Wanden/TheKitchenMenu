package com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.ingredient.RepositoryIngredient;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.businessentity.unitofmeasure.MeasurementSubtype;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource.TestDataIngredientEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataUseCaseConversionFactorStatusRequestResponse;

import org.junit.*;
import org.mockito.*;

import static org.junit.Assert.assertEquals;

public class ConversionFactorStatusTest {

    // region constants ----------------------------------------------------------------------------
//    private IngredientEntity INGREDIENT_NEW_VALID_NAME_DESCRIPTION =
//            TestDataIngredientEntity.getNewInvalidNameValidDescription();
    private ConversionFactorStatusRequest REQUEST_DISABLED =
            TestDataUseCaseConversionFactorStatusRequestResponse.
                    getRequestMetricNoConversionFactor();
    private ConversionFactorStatusResponse RESPONSE_DISABLED =
            TestDataUseCaseConversionFactorStatusRequestResponse.
                    getResponseMetricNoConversionFactor();

    private IngredientEntity INGREDIENT_VALID_FROM_ANOTHER_USER =
            TestDataIngredientEntity.getExistingValidNameValidDescriptionValidFromAnotherUser();
    private ConversionFactorStatusRequest REQUEST_ENABLED_UNEDITABLE =
            TestDataUseCaseConversionFactorStatusRequestResponse.
                    getRequestWithConversionFactorFromAnotherUser();
    private ConversionFactorStatusResponse RESPONSE_ENABLED_UNEDITABLE =
            TestDataUseCaseConversionFactorStatusRequestResponse.
                    getResponseConversionFactorUneditable();

    private IngredientEntity INGREDIENT_NEW_VALID_WITH_CONVERSION_FACTOR_UNSET =
            TestDataIngredientEntity.getValidNewNameValidDescriptionValid();
    private ConversionFactorStatusRequest REQUEST_ENABLED_EDITABLE_UNSET =
            TestDataUseCaseConversionFactorStatusRequestResponse.
                    getRequestWithConversionFactorEnabledUnset();
    private ConversionFactorStatusResponse RESPONSE_ENABLED_EDITABLE_UNSET =
            TestDataUseCaseConversionFactorStatusRequestResponse.
                    getResponseConversionFactorEnabledUnset();

    private IngredientEntity INGREDIENT_VALID_WITH_CONVERSION_FACTOR =
            TestDataIngredientEntity.getExistingValidMinimumConversionFactor();
    private ConversionFactorStatusRequest REQUEST_ENABLED_EDITABLE_SET =
            TestDataUseCaseConversionFactorStatusRequestResponse.
                    getRequestWithConversionFactorEnabledSet();
    private ConversionFactorStatusResponse RESPONSE_ENABLED_EDITABLE_SET =
            TestDataUseCaseConversionFactorStatusRequestResponse.
                    getResponseConversionFactorEnabledSet();

    private ConversionFactorStatusRequest REQUEST_NO_DATA_AVAILABLE =
            TestDataUseCaseConversionFactorStatusRequestResponse.getRequestForIngredientNotFound();
    private ConversionFactorStatusResponse RESPONSE_NO_DATA_AVAILABLE =
            TestDataUseCaseConversionFactorStatusRequestResponse.getResponseForIngredientNotFound();

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    private UseCaseHandler handler = new UseCaseHandler(new UseCaseSchedulerMock());
    @Mock
    RepositoryIngredient repoMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetPrimitiveCallback<IngredientEntity>> getEntityCallbackCaptor;

    private ConversionFactorStatusResponse actualResponse;

    // endregion helper fields ---------------------------------------------------------------------

    private ConversionFactorStatus SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = new ConversionFactorStatus(repoMock);
    }

    @Test
    public void getConversionFactorStatus_ingredientNotFound_NO__DATA_AVAILABLE() {
        // Arrange
        String ingredientId = REQUEST_NO_DATA_AVAILABLE.getIngredientId();
        MeasurementSubtype subtype = REQUEST_NO_DATA_AVAILABLE.getSubtype();
        // Act
        handler.executeAsync(SUT, getRequestValues(subtype, ingredientId), getResponseCallback());
        // Assert
//        verify(repoMock).getByDataId(eq(ingredientId), getEntityCallbackCaptor.capture());
        getEntityCallbackCaptor.getValue().onDataUnavailable();

        assertEquals(RESPONSE_NO_DATA_AVAILABLE, actualResponse);
    }

    @Test
    public void getConversionFactorStatus_disabledForUnitOfMeasure_DISABLED() {
        // Arrange
//        String ingredientId = INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getDataId();
//        MeasurementSubtype subtype = REQUEST_DISABLED.getSubtype();
//        // Act
//        handler.execute(SUT, getRequestValues(subtype, ingredientId), getResponseCallback());
//        // Assert
//        verifyNoMoreInteractions(repoMock);
//        assertEquals(RESPONSE_DISABLED, actualResponse);
    }

    @Test
    public void getConversionFactorStatus_userIsNotCreator_ENABLED_UNEDITABLE() {
        // Arrange
        String ingredientId = INGREDIENT_VALID_FROM_ANOTHER_USER.getDataId();
        MeasurementSubtype subtype = REQUEST_ENABLED_UNEDITABLE.getSubtype();
        // Act
        handler.executeAsync(SUT, getRequestValues(subtype, ingredientId), getResponseCallback());
        // Assert
        verifyRepoCalledWithIngredientValidFromAnotherUser();
        assertEquals(RESPONSE_ENABLED_UNEDITABLE, actualResponse);
    }

    @Test
    public void getConversionFactorStatus_noConversionFactorSet_ENABLED_EDITABLE_UNSET() {
        // Arrange
        String ingredientId = INGREDIENT_NEW_VALID_WITH_CONVERSION_FACTOR_UNSET.getDataId();
        MeasurementSubtype subtype = REQUEST_ENABLED_EDITABLE_UNSET.getSubtype();
        // Act
        handler.executeAsync(SUT, getRequestValues(subtype, ingredientId), getResponseCallback());
        // Assert
        verifyRepoCalledWithIngredientNewValidNameDescription();
        assertEquals(RESPONSE_ENABLED_EDITABLE_UNSET, actualResponse);
    }

    @Test
    public void getConversionFactorStatus_conversionFactorSet_ENABLED_EDITABLE_SET() {
        // Arrange
        String ingredientId = INGREDIENT_VALID_WITH_CONVERSION_FACTOR.getDataId();
        MeasurementSubtype subtype = REQUEST_ENABLED_EDITABLE_SET.getSubtype();
        // Act
        handler.executeAsync(SUT, getRequestValues(subtype, ingredientId), getResponseCallback());
        verifyRepoCalledWithIngredientValidWithConversionFactor();
        // Assert
        assertEquals(RESPONSE_ENABLED_EDITABLE_SET, actualResponse);
    }

    // region helper methods -----------------------------------------------------------------------
    private ConversionFactorStatusRequest getRequestValues(MeasurementSubtype subtype,
                                                           String ingredientId) {
        return new ConversionFactorStatusRequest(subtype, ingredientId);
    }

    private UseCaseBase.Callback<ConversionFactorStatusResponse> getResponseCallback() {
        return new UseCaseBase.Callback<ConversionFactorStatusResponse>() {
            @Override
            public void onUseCaseSuccess(ConversionFactorStatusResponse response) {
                ConversionFactorStatusTest.this.actualResponse = response;
            }

            @Override
            public void onUseCaseError(ConversionFactorStatusResponse response) {
                ConversionFactorStatusTest.this.actualResponse = response;
            }
        };
    }

    private void verifyRepoCalledWithIngredientValidFromAnotherUser() {
//        verify(repoMock).getByDataId(eq(INGREDIENT_VALID_FROM_ANOTHER_USER.getDataId()),
//                getEntityCallbackCaptor.capture());
        getEntityCallbackCaptor.getValue().onEntityLoaded(INGREDIENT_VALID_FROM_ANOTHER_USER);
    }

    private void verifyRepoCalledWithIngredientNewValidNameDescription() {
//        verify(repoMock).getByDataId(eq(INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getDataId()),
//                getEntityCallbackCaptor.capture());
//        getEntityCallbackCaptor.getValue().onEntityLoaded(INGREDIENT_NEW_VALID_NAME_DESCRIPTION);
    }

    private void verifyRepoCalledWithIngredientValidWithConversionFactor() {
//        verify(repoMock).getByDataId(eq(INGREDIENT_VALID_WITH_CONVERSION_FACTOR.getDataId()),
//                getEntityCallbackCaptor.capture());
        getEntityCallbackCaptor.getValue().onEntityLoaded(INGREDIENT_VALID_WITH_CONVERSION_FACTOR);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}