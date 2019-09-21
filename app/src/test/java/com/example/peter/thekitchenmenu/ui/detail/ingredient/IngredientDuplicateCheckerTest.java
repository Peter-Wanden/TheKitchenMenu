package com.example.peter.thekitchenmenu.ui.detail.ingredient;

import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.testdata.IngredientEntityTestData;

import org.junit.*;
import org.mockito.*;

import java.util.List;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

public class IngredientDuplicateCheckerTest {

    // region constants ----------------------------------------------------------------------------
    private List<IngredientEntity> LIST_OF_ALL_INGREDIENTS =
            IngredientEntityTestData.getAllIngredients();
    private String VALID_NAME_NO_DUPLICATE =
            IngredientEntityTestData.getValidNameNoDuplicate();
    private String VALID_NAME_DUPLICATE =
            IngredientEntityTestData.getExistingValidNameValidDescription().getName();
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
        SUT.checkForDuplicateAndNotify("", callbackMock);
        // Assert
        verifyNoMoreInteractions(dataSourceMock);
    }

    @Test
    public void checkForDuplicate_validName_databaseRequest() {
        // Arrange
        // Act
        SUT.checkForDuplicateAndNotify("validName", callbackMock);
        // Assert
        verify(dataSourceMock).getAll(eq(SUT));
    }

    @Test
    public void checkForDuplicate_validNameNoDuplicate_callbackNotifiedFalse() {
        // Arrange
        // Act
        SUT.checkForDuplicateAndNotify(VALID_NAME_NO_DUPLICATE, callbackMock);
        // Assert
        simulateGetAllFromDatabase();
        verify(callbackMock).duplicateCheckResult(eq(false));
    }

    @Test
    public void checkForDuplicate_validNameDuplicate_callbackNotifiedTrue() {
        // Arrange
        // Act
        SUT.checkForDuplicateAndNotify(VALID_NAME_DUPLICATE, callbackMock);
        // Assert
        simulateGetAllFromDatabase();
        verify(callbackMock).duplicateCheckResult(eq(true));
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