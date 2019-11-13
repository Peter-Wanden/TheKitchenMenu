package com.example.peter.thekitchenmenu.ui.detail.ingredient;

import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.testdata.IngredientEntityTestData;

import org.junit.*;
import org.mockito.*;

import java.util.List;

import static org.mockito.Mockito.*;

public class IngredientDuplicateCheckerTest {

    // region constants ----------------------------------------------------------------------------
    private List<IngredientEntity> LIST_OF_ALL_INGREDIENTS =
            IngredientEntityTestData.getAllIngredients();
    private String VALID_NAME_NO_DUPLICATE =
            IngredientEntityTestData.getValidNameNoDuplicate();
    private IngredientEntity VALID_DUPLICATE =
            IngredientEntityTestData.getExistingValidNameValidDescriptionNoConversionFactor();
    private String VALID_NAME_DUPLICATE_IS_BEING_EDITED =
            IngredientEntityTestData.getNewValidName().getName();
    private String INGREDIENT_ID =
            IngredientEntityTestData.getNewValidName().getId();
    private String NO_DUPLICATE_FOUND = IngredientDuplicateChecker.NO_DUPLICATE_FOUND;
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    DataSource<IngredientEntity> dataSourceMock;
    @Captor
    ArgumentCaptor<DataSource.GetAllCallback<IngredientEntity>> getAllCallbackArgumentCaptor;
    @Mock
    IngredientDuplicateChecker.DuplicateCallback callbackMock;
    // endregion helper fields ---------------------------------------------------------------------

    private IngredientDuplicateChecker SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = new IngredientDuplicateChecker(dataSourceMock);
    }

    @Test
    public void checkForDuplicate_invalidName_noDatabaseRequest() {
        // Arrange
        // Act
        SUT.checkForDuplicatesAndNotify("", INGREDIENT_ID, callbackMock);
        // Assert
        verifyNoMoreInteractions(dataSourceMock);
    }

    @Test
    public void checkForDuplicate_validName_databaseRequest() {
        // Arrange
        // Act
        SUT.checkForDuplicatesAndNotify("validName", INGREDIENT_ID, callbackMock);
        // Assert
        verify(dataSourceMock).getAll(eq(SUT));
    }

    @Test
    public void checkForDuplicate_validNameNoDuplicate_callbackFalse() {
        // Arrange
        // Act
        SUT.checkForDuplicatesAndNotify(VALID_NAME_NO_DUPLICATE, INGREDIENT_ID, callbackMock);
        // Assert
        simulateGetAllFromDatabase();
        verify(callbackMock).duplicateCheckResult(eq(NO_DUPLICATE_FOUND));
    }

    @Test
    public void checkForDuplicate_validNameDuplicateIsIngredientBeingEdited_callbackFalse() {
        // Arrange
        // Act
        SUT.checkForDuplicatesAndNotify(
                VALID_NAME_DUPLICATE_IS_BEING_EDITED,
                INGREDIENT_ID,
                callbackMock);
        // Assert
        simulateGetAllFromDatabase();
        verify(callbackMock).duplicateCheckResult(eq(NO_DUPLICATE_FOUND));
    }

    @Test
    public void checkForDuplicate_validNameDuplicateIsNotIngredientBeingEdited_callbackTrue() {
        // Arrange
        // Act
        SUT.checkForDuplicatesAndNotify(VALID_DUPLICATE.getName(), INGREDIENT_ID, callbackMock);
        // Assert
        simulateGetAllFromDatabase();
        verify(callbackMock).duplicateCheckResult(eq(VALID_DUPLICATE.getId()));
    }

    // region helper methods -----------------------------------------------------------------------
    private void simulateGetAllFromDatabase() {
        verify(dataSourceMock).getAll(getAllCallbackArgumentCaptor.capture());
        getAllCallbackArgumentCaptor.getValue().onAllLoaded(LIST_OF_ALL_INGREDIENTS);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}