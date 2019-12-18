package com.example.peter.thekitchenmenu.domain.usecase.ingredient;

import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.UseCaseIngredientDuplicateChecker;
import com.example.peter.thekitchenmenu.testdata.TestDataIngredientEntity;

import org.junit.*;
import org.mockito.*;

import java.util.List;

import static org.mockito.Mockito.*;

public class UseCaseIngredientDuplicateCheckerTest {

    // region constants ----------------------------------------------------------------------------
    private List<IngredientEntity> LIST_OF_ALL_INGREDIENTS =
            TestDataIngredientEntity.getAllIngredients();
    private String VALID_NAME_NO_DUPLICATE =
            TestDataIngredientEntity.getValidNameNoDuplicate();
    private IngredientEntity VALID_DUPLICATE =
            TestDataIngredientEntity.getExistingValidWithConversionFactor();
    private String VALID_NAME_DUPLICATE_IS_BEING_EDITED =
            TestDataIngredientEntity.getNewValidName().getName();
    private String INGREDIENT_ID =
            TestDataIngredientEntity.getNewValidName().getId();
    private String NO_DUPLICATE_FOUND = UseCaseIngredientDuplicateChecker.NO_DUPLICATE_FOUND;
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RepositoryIngredient repoMock;
    @Captor
    ArgumentCaptor<DataSource.GetAllCallback<IngredientEntity>> getRepoCallbackCaptor;
    @Mock
    UseCaseIngredientDuplicateChecker.DuplicateCallback callbackMock;
    // endregion helper fields ---------------------------------------------------------------------

    private UseCaseIngredientDuplicateChecker SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = new UseCaseIngredientDuplicateChecker(repoMock);
    }

    @Test
    public void checkForDuplicate_invalidName_noDatabaseRequest() {
        // Arrange
        // Act
        SUT.checkForDuplicateAndNotify("", INGREDIENT_ID, callbackMock);
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void checkForDuplicate_validName_databaseRequest() {
        // Arrange
        // Act
        SUT.checkForDuplicateAndNotify("validName", INGREDIENT_ID, callbackMock);
        // Assert
        verify(repoMock).getAll(eq(SUT));
    }

    @Test
    public void checkForDuplicate_validNameNoDuplicate_callbackFalse() {
        // Arrange
        // Act
        SUT.checkForDuplicateAndNotify(VALID_NAME_NO_DUPLICATE, INGREDIENT_ID, callbackMock);
        // Assert
        simulateGetAllFromDatabase();
        verify(callbackMock).duplicateCheckResult(eq(NO_DUPLICATE_FOUND));
    }

    @Test
    public void checkForDuplicate_validNameDuplicateIsIngredientBeingEdited_callbackFalse() {
        // Arrange
        // Act
        SUT.checkForDuplicateAndNotify(
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
        SUT.checkForDuplicateAndNotify(VALID_DUPLICATE.getName(), INGREDIENT_ID, callbackMock);
        // Assert
        simulateGetAllFromDatabase();
        verify(callbackMock).duplicateCheckResult(eq(VALID_DUPLICATE.getId()));
    }

    // region helper methods -----------------------------------------------------------------------
    private void simulateGetAllFromDatabase() {
        verify(repoMock).getAll(getRepoCallbackCaptor.capture());
        getRepoCallbackCaptor.getValue().onAllLoaded(LIST_OF_ALL_INGREDIENTS);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}