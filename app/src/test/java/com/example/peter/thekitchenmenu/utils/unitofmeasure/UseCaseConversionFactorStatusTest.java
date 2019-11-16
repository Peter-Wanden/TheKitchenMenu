package com.example.peter.thekitchenmenu.utils.unitofmeasure;

import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.domain.unitofmeasureentities.MeasurementSubtype;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseConversionFactorStatus;
import com.example.peter.thekitchenmenu.testdata.IngredientEntityTestData;

import org.junit.*;
import org.mockito.*;

import static com.example.peter.thekitchenmenu.domain.usecase.UseCaseConversionFactorStatus.UseCaseConversionFactorResult.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class UseCaseConversionFactorStatusTest {

    // region constants ----------------------------------------------------------------------------
    private IngredientEntity INGREDIENT_NEW_VALID_NAME_DESCRIPTION =
            IngredientEntityTestData.getNewInvalidNameValidDescription();
    private IngredientEntity INGREDIENT_VALID_FROM_ANOTHER_USER =
            IngredientEntityTestData.getExistingValidNameValidDescriptionFromAnotherUser();
    private IngredientEntity INGREDIENT_VALID_WITH_CONVERSION_FACTOR =
            IngredientEntityTestData.getExistingValidWithConversionFactor();
    private MeasurementSubtype UNEDITABLE_SUBTYPE = MeasurementSubtype.METRIC_MASS;
    private MeasurementSubtype EDITABLE_SUBTYPE = MeasurementSubtype.IMPERIAL_SPOON;
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RepositoryIngredient repoMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<IngredientEntity>> getEntityCallbackCaptor;
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
        // Act
        SUT.
        SUT.getStatus(UNEDITABLE_SUBTYPE, ingredientId);
        // Assert
        verifyNoMoreInteractions(repoMock);
        verify(viewModelMock).useCaseConversionFactorResult(DISABLED);
    }

    @Test
    public void getConversionFactorStatus_userIsNotCreator_ENABLED_UNEDITABLE() {
        // Arrange
        String ingredientId = INGREDIENT_VALID_FROM_ANOTHER_USER.getId();
        // Act
        SUT.getStatus(EDITABLE_SUBTYPE, ingredientId);
        // Assert
        verifyRepoCalledWithIngredientValidFromAnotherUser();
        verify(viewModelMock).useCaseConversionFactorResult(ENABLED_UNEDITABLE);
    }

    @Test
    public void getConversionFactorStatus_noConversionFactorSet_ENADLED_EDITABLE_UNSET() {
        // Arrange
        String ingredientId = INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId();
        // Act
        SUT.getStatus(EDITABLE_SUBTYPE, ingredientId);
        // Assert
        verifyRepoCalledWithIngredientNewValidNameDescription();
        verify(viewModelMock).useCaseConversionFactorResult(ENABLED_EDITABLE_UNSET);
    }

    @Test
    public void getConversionFactorStatus_conversionFactorSet_ENABLED_EDITABLE_SET() {
        // Arrange
        String ingredientId = INGREDIENT_VALID_WITH_CONVERSION_FACTOR.getId();
        // Act
        SUT.getStatus(EDITABLE_SUBTYPE, ingredientId);
        verifyRepoCalledWithIngredientValidWithConversionFactor();
        // Assert
        verify(viewModelMock).useCaseConversionFactorResult(ENABLED_EDITABLE_SET);
    }

    // region helper methods -----------------------------------------------------------------------
    private void verifyRepoCalledWithIngredientNewValidNameDescription() {
        verify(repoMock).getById(eq(INGREDIENT_NEW_VALID_NAME_DESCRIPTION.getId()),
                getEntityCallbackCaptor.capture());
        getEntityCallbackCaptor.getValue().onEntityLoaded(INGREDIENT_NEW_VALID_NAME_DESCRIPTION);
    }

    private void verifyRepoCalledWithIngredientValidFromAnotherUser() {
        verify(repoMock).getById(eq(INGREDIENT_VALID_FROM_ANOTHER_USER.getId()),
                getEntityCallbackCaptor.capture());
        getEntityCallbackCaptor.getValue().onEntityLoaded(INGREDIENT_VALID_FROM_ANOTHER_USER);
    }

    private void verifyRepoCalledWithIngredientValidWithConversionFactor() {
        verify(repoMock).getById(eq(INGREDIENT_VALID_WITH_CONVERSION_FACTOR.getId()),
                getEntityCallbackCaptor.capture());
        getEntityCallbackCaptor.getValue().onEntityLoaded(INGREDIENT_VALID_WITH_CONVERSION_FACTOR);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}