package com.example.peter.thekitchenmenu.domain.usecase.ingredient;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetAllDomainModelsCallback;
import com.example.peter.thekitchenmenu.data.repository.ingredient.TestDataIngredient;
import com.example.peter.thekitchenmenu.data.repository.ingredient.RepositoryIngredient;

import org.junit.*;
import org.mockito.*;

import static org.mockito.Mockito.*;

public class IngredientDuplicateCheckerTest {

    // region constants ----------------------------------------------------------------------------
    private String VALID_NAME_NO_DUPLICATE = TestDataIngredient.
            getValidNonDuplicatedName();

    private IngredientPersistenceDomainModel VALID_DUPLICATE = TestDataIngredient.
            getExistingValidDefaultConversionFactor();

    private String VALID_NAME_DUPLICATE_IS_BEING_EDITED = TestDataIngredient.
            getValidNewNameValidDescriptionValid().getName();
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RepositoryIngredient repoMock;
    @Captor
    ArgumentCaptor<GetAllDomainModelsCallback<IngredientPersistenceDomainModel>> getRepoCallbackCaptor;
    @Mock
    IngredientDuplicateChecker.DuplicateCallback callbackMock;
    // endregion helper fields ---------------------------------------------------------------------

    private IngredientDuplicateChecker SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = new IngredientDuplicateChecker(repoMock);
    }

    @Test
    public void checkForDuplicate_emptyName_noPersistenceRequest() {
        // Arrange
        String emptyName = "";
        // Act
        SUT.checkForDuplicateAndNotify(
                emptyName,
                TestDataIngredient.NEW_INGREDIENT_DOMAIN_ID,
                callbackMock
        );
        // Assert
        verifyNoMoreInteractions(repoMock);
    }

    @Test
    public void checkForDuplicate_validName_persistenceDataRequested() {
        // Arrange
        String validName = "validName";
        // Act
        SUT.checkForDuplicateAndNotify(
                validName,
                TestDataIngredient.NEW_INGREDIENT_DOMAIN_ID,
                callbackMock
        );
        // Assert
        verify(repoMock).getAll(eq(SUT));
    }

    @Test
    public void checkForDuplicate_validNameNoDuplicate_NO_DUPLICATE_FOUND() {
        // Arrange
        // Act
        SUT.checkForDuplicateAndNotify(
                VALID_NAME_NO_DUPLICATE,
                TestDataIngredient.NEW_INGREDIENT_DOMAIN_ID,
                callbackMock
        );
        // Assert
        simulateGetAllFromDatabase();
        verify(callbackMock).duplicateCheckResult(eq(
                IngredientDuplicateChecker.NO_DUPLICATE_FOUND)
        );
    }

    @Test
    public void checkForDuplicate_validNameDuplicateIsIngredientBeingEdited_callbackFalse() {
        // Arrange, the duplicate found in the database has the same domain Id as the ingredient
        // being edited, it is the same data, therefore it is not a duplicate.
        // Act
        SUT.checkForDuplicateAndNotify(
                VALID_NAME_DUPLICATE_IS_BEING_EDITED,
                TestDataIngredient.EXISTING_INGREDIENT_DOMAIN_ID,
                callbackMock
        );
        // Assert
        simulateGetAllFromDatabase();
        verify(callbackMock).duplicateCheckResult(eq(
                IngredientDuplicateChecker.NO_DUPLICATE_FOUND)
        );
    }

    @Test
    public void checkForDuplicate_validNameDuplicateIsNotIngredientBeingEdited_callbackTrue() {
        // Arrange
        // Act
        SUT.checkForDuplicateAndNotify(
                VALID_DUPLICATE.getName(),
                TestDataIngredient.NEW_INGREDIENT_DOMAIN_ID,
                callbackMock
        );
        // Assert
        simulateGetAllFromDatabase();
        verify(callbackMock).duplicateCheckResult(eq(VALID_DUPLICATE.getDomainId()));
    }

    // region helper methods -----------------------------------------------------------------------
    private void simulateGetAllFromDatabase() {
        verify(repoMock).getAll(getRepoCallbackCaptor.capture());
        getRepoCallbackCaptor.getValue().onAllDomainModelsLoaded(TestDataIngredient.getAll());
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------
}