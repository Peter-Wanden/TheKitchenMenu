package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.peter.thekitchenmenu.data.entity.*;
import com.example.peter.thekitchenmenu.data.repository.*;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeEntity;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

import org.junit.*;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.peter.thekitchenmenu.testdata.TestDataRecipeCourseEntity.*;
import static com.example.peter.thekitchenmenu.testdata.TestDataRecipeEntity.*;
import static com.example.peter.thekitchenmenu.testdata.TestDataRecipeValidator.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RecipeCourseEditorViewModelTest {

    // region constants ----------------------------------------------------------------------------
    private RecipeEntity VALID_EXISTING_RECIPE_ENTITY = getValidExisting();
    private String EXISTING_RECIPE_ID = VALID_EXISTING_RECIPE_ENTITY.getId();
    private String NEW_RECIPE_ID = TestDataRecipeEntity.getNewInvalid().getId();

    private RecipeModelStatus COURSES_MODEL_UNCHANGED_INVALID = getCoursesModelStatusUnchangedInvalid();
    private RecipeModelStatus COURSES_MODEL_UNCHANGED_VALID = getCoursesModelStatusUnchangedValid();
    private RecipeModelStatus COURSES_MODEL_CHANGED_INVALID = getCoursesModelStatusChangedInvalid();
    private RecipeModelStatus COURSES_MODEL_CHANGED_VALID = getCoursesModelStatusChangedValid();
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Captor
    ArgumentCaptor<DataSource.GetAllCallback<RecipeCourseEntity>> getEntityCallbackArgumentCaptor;
    @Mock
    RepositoryRecipeCourse repoRecipeCourseMock;
    @Mock
    UniqueIdProvider idProviderMock;
    @Mock
    RecipeValidation.RecipeValidatorModelSubmission modelSubmissionMock;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeCourseEditorViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = new RecipeCourseEditorViewModel(repoRecipeCourseMock, idProviderMock);
        SUT.setModelValidationSubmitter(modelSubmissionMock);
    }

    @Test
    public void start_recipeIdSupplied_databaseCalledForListOfCourses() {
        // Arrange
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        // Assert
        verify(repoRecipeCourseMock).getCoursesForRecipe(eq(EXISTING_RECIPE_ID),
                getEntityCallbackArgumentCaptor.capture());
    }

    @Test
    public void start_recipeIdSupplied_observersCalled() {
        // Arrange
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateGetAllCoursesForRecipeFromDatabase();
        // Assert
        assertTrue(SUT.isCourseZero());
        assertTrue(SUT.isCourseOne());
        assertTrue(SUT.isCourseTwo());
        assertTrue(SUT.isCourseThree());
        assertTrue(SUT.isCourseFour());
        assertTrue(SUT.isCourseFive());
        assertTrue(SUT.isCourseSix());
        assertTrue(SUT.isCourseSeven());
    }

    @Test
    public void start_recipeIdSupplied_evenObserversCalledOnly() {
        // Arrange
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateGetEvenCoursesForRecipeFromDatabase();
        // Assert
        assertTrue(SUT.isCourseZero());
        assertFalse(SUT.isCourseOne());
        assertTrue(SUT.isCourseTwo());
        assertFalse(SUT.isCourseThree());
        assertTrue(SUT.isCourseFour());
        assertFalse(SUT.isCourseFive());
        assertTrue(SUT.isCourseSix());
        assertFalse(SUT.isCourseSeven());
    }

    @Test
    public void startWithClonedModel_oldAndNewRecipeIdSupplied_databaseCalledForListOfOldCourses() {
        // Arrange
        // Act
        SUT.startByCloningModel(EXISTING_RECIPE_ID, NEW_RECIPE_ID);
        // Assert
        verify(repoRecipeCourseMock).getCoursesForRecipe(eq(EXISTING_RECIPE_ID), anyObject());
    }

    @Test
    public void startWithClonedModel_oldAndNewRecipeIdSupplied_oldCoursesSavedWithNewRecipeId() {
        ArgumentCaptor<RecipeCourseEntity> ac = ArgumentCaptor.forClass(RecipeCourseEntity.class);
        WhenUniqueIdProviderMockCalledReturnClonedEvenIds();
        // Act
        SUT.startByCloningModel(EXISTING_RECIPE_ID, NEW_RECIPE_ID);
        simulateGetEvenCoursesForRecipeFromDatabase();
        // Assert
        verify(repoRecipeCourseMock, times(4)).save(ac.capture());
        List<RecipeCourseEntity> courseEntities = ac.getAllValues();
        assertEquals(getClonedRecipeCourseZero(), courseEntities.get(0));
        assertEquals(getClonedRecipeCourseTwo(), courseEntities.get(1));
        assertEquals(getClonedRecipeCourseFour(), courseEntities.get(2));
        assertEquals(getClonedRecipeCourseSix(), courseEntities.get(3));
    }

    @Test
    public void startWithClonedModel_oldAndNewRecipeIdSupplied_courseSubtractionsAreDeletedFromNewCourseId() {
        // Arrange
        WhenUniqueIdProviderMockCalledReturnClonedEvenIds();
        // Act
        SUT.startByCloningModel(EXISTING_RECIPE_ID, NEW_RECIPE_ID);
        simulateGetEvenCoursesForRecipeFromDatabase();
        // Assert
        assertTrue(SUT.isCourseZero());
        SUT.setCourseZero(false);
        verify(repoRecipeCourseMock).deleteById(eq(getClonedRecipeCourseZero().getId()));
        SUT.setCourseFour(false);
        verify(repoRecipeCourseMock).deleteById(eq(getClonedRecipeCourseFour().getId()));
    }

    @Test
    public void startWithClonedModel_oldAndNewRecipeIdSupplied_courseAdditionsSavedWithNewRecipeId() {
        // Arrange
        RecipeCourseEntity newRecipeCourseOne = new RecipeCourseEntity(
                "This is the id we're looking for!",
                getRecipeCourseOne().getCourseNo(),
                NEW_RECIPE_ID
        );
        when(idProviderMock.getUId()).thenReturn(
                getClonedRecipeCourseZero().getId(),
                getClonedRecipeCourseTwo().getId(),
                getClonedRecipeCourseFour().getId(),
                getClonedRecipeCourseSix().getId(),
                newRecipeCourseOne.getId());
        // Act
        SUT.startByCloningModel(EXISTING_RECIPE_ID, NEW_RECIPE_ID);
        simulateGetEvenCoursesForRecipeFromDatabase();
        assertFalse(SUT.isCourseOne());
        SUT.setCourseOne(true);
        // Assert
        verify(repoRecipeCourseMock).save(eq(newRecipeCourseOne));
    }

     @Test
    public void courseZeroSelected_true_courseZeroAndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseZero().getId());
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        SUT.setCourseZero(true);
        // Assert
        verify(repoRecipeCourseMock).save(getRecipeCourseZero());
    }

    @Test
    public void courseZeroSelected_false_courseZeroForRecipeRemovedFromDatabase() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateGetAllCoursesForRecipeFromDatabase();
        SUT.setCourseZero(false);
        // Assert
        verify(repoRecipeCourseMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(0).getId(), ac.getValue());
    }

    @Test
    public void courseOneSelected_true_courseOneAndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseOne().getId());
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        SUT.setCourseOne(true);
        // Assert
        verify(repoRecipeCourseMock).save(getRecipeCourseOne());
    }

    @Test
    public void courseOneSelected_false_courseOneAndRecipeIdRemovedFromDatabase() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateGetAllCoursesForRecipeFromDatabase();
        SUT.setCourseOne(false);
        // Assert
        verify(repoRecipeCourseMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(1).getId(), ac.getValue());
    }

    @Test
    public void courseTwoSelected_true_courseId2AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseTwo().getId());
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        SUT.setCourseTwo(true);
        // Assert
        verify(repoRecipeCourseMock).save(getRecipeCourseTwo());
    }

    @Test
    public void courseTwoSelected_false_courseId2AndRecipeIdRemovedFromDatabase() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateGetAllCoursesForRecipeFromDatabase();
        SUT.setCourseTwo(false);
        // Assert
        verify(repoRecipeCourseMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(2).getId(), ac.getValue());
    }

    @Test
    public void courseThreeSelected_true_courseId3AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseThree().getId());
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        SUT.setCourseThree(true);
        // Assert
        verify(repoRecipeCourseMock).save(getRecipeCourseThree());
    }

    @Test
    public void courseThreeSelected_false_courseId3AndRecipeIdRemovedFromDatabase() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateGetAllCoursesForRecipeFromDatabase();
        SUT.setCourseThree(false);
        // Assert
        verify(repoRecipeCourseMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(3).getId(), ac.getValue());
    }

    @Test
    public void courseFourSelected_true_courseId4AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseFour().getId());
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        SUT.setCourseFour(true);
        // Assert
        verify(repoRecipeCourseMock).save(getRecipeCourseFour());
    }

    @Test
    public void courseFourSelected_false_courseId4AndRecipeIdRemovedFromDatabase() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateGetAllCoursesForRecipeFromDatabase();
        SUT.setCourseFour(false);
        // Assert
        verify(repoRecipeCourseMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(4).getId(), ac.getValue());
    }

    @Test
    public void courseFiveSelected_true_courseId5AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseFive().getId());
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        SUT.setCourseFive(true);
        // Assert
        verify(repoRecipeCourseMock).save(getRecipeCourseFive());
    }

    @Test
    public void courseFiveSelected_false_courseId5AndRecipeIdRemovedFromDatabase() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateGetAllCoursesForRecipeFromDatabase();
        SUT.setCourseFive(false);
        // Assert
        verify(repoRecipeCourseMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(5).getId(), ac.getValue());
    }

    @Test
    public void courseSixSelected_true_courseId6AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseSix().getId());
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        SUT.setCourseSix(true);
        // Assert
        verify(repoRecipeCourseMock).save(getRecipeCourseSix());
    }

    @Test
    public void courseSixSelected_false_courseId6AndRecipeIdRemovedFromDatabase() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateGetAllCoursesForRecipeFromDatabase();
        SUT.setCourseSix(false);
        // Assert
        verify(repoRecipeCourseMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(6).getId(), ac.getValue());
    }

    @Test
    public void courseSevenSelected_true_courseId7AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseSeven().getId());
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        SUT.setCourseSeven(true);
        // Assert
        verify(repoRecipeCourseMock).save(getRecipeCourseSeven());
    }

    @Test
    public void courseSevenSelected_false_courseId7AndRecipeIdRemovedFromDatabase() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateGetAllCoursesForRecipeFromDatabase();
        SUT.setCourseSeven(false);
        // Assert
        verify(repoRecipeCourseMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(7).getId(), ac.getValue());
    }

    @Test(expected = RuntimeException.class)
    public void start_noRecipeIdSupplied_throwsRuntimeException() {
        // Arrange
        // Act
        SUT.start(null);
        // Assert
        ArrayList list = new ArrayList();
        RuntimeException exception = (RuntimeException) list.get(0);
        assertEquals(exception.getMessage(), "This should throw an error!");
    }

    @Test
    public void start_recipeIdSupplied_RecipeModelStatus_COURSES_MODEL_UNCHANGED_INVALID() {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateReturnNoCoursesForRecipeFromDatabase();
        // Assert
        verify(modelSubmissionMock).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(COURSES_MODEL_UNCHANGED_INVALID, modelStatus);
    }

    @Test
    public void start_recipeIdSupplied_RecipeModelStatus_COURSES_MODEL_UNCHANGED_VALID() {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateGetAllCoursesForRecipeFromDatabase();
        // Assert
        verify(modelSubmissionMock).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(COURSES_MODEL_UNCHANGED_VALID, modelStatus);
    }

    @Test
    public void allOptionsDeselected_recipeIdSupplied_RecipeModelStatus_COURSES_MODEL_CHANGED_INVALID() {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateGetAllCoursesForRecipeFromDatabase();
        SUT.setCourseZero(false);
        SUT.setCourseOne(false);
        SUT.setCourseTwo(false);
        SUT.setCourseThree(false);
        SUT.setCourseFour(false);
        SUT.setCourseFive(false);
        SUT.setCourseSix(false);
        SUT.setCourseSeven(false);
        // Assert
        verify(modelSubmissionMock, times(9)).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(COURSES_MODEL_CHANGED_INVALID, modelStatus);
    }

    @Test
    public void allButOneOptionsDeselected_recipeIdSupplied_RecipeModelStatus_COURSES_MODEL_CHANGED_VALID() {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateGetAllCoursesForRecipeFromDatabase();
        SUT.setCourseZero(false);
        SUT.setCourseOne(false);
        SUT.setCourseTwo(false);
        SUT.setCourseThree(false);
        SUT.setCourseFour(false);
        SUT.setCourseFive(false);
        SUT.setCourseSix(false);
        // Assert
        verify(modelSubmissionMock, times(8)).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(COURSES_MODEL_CHANGED_VALID, modelStatus);
    }

    // region helper methods -----------------------------------------------------------------------
    private void simulateGetAllCoursesForRecipeFromDatabase() {
        // verify call database with recipe id
        verify(repoRecipeCourseMock).getCoursesForRecipe(eq(EXISTING_RECIPE_ID),
                getEntityCallbackArgumentCaptor.capture());
        // simulate list of courses returned
        getEntityCallbackArgumentCaptor.getValue().onAllLoaded(
                getAllRecipeCoursesDatabaseResponse());
    }

    private void simulateGetEvenCoursesForRecipeFromDatabase() {
        // verify call database with recipe id
        verify(repoRecipeCourseMock).getCoursesForRecipe(eq(EXISTING_RECIPE_ID),
                getEntityCallbackArgumentCaptor.capture());
        // simulate list of courses returned
        getEntityCallbackArgumentCaptor.getValue().onAllLoaded(
                getEvenRecipeCoursesDatabaseResponse());
    }

    private void simulateReturnNoCoursesForRecipeFromDatabase() {
        // verify call database with recipe id
        verify(repoRecipeCourseMock).getCoursesForRecipe(eq(EXISTING_RECIPE_ID),
                getEntityCallbackArgumentCaptor.capture());
        // simulate list no courses returned
        getEntityCallbackArgumentCaptor.getValue().onDataNotAvailable();
    }

    private void WhenUniqueIdProviderMockCalledReturnClonedEvenIds() {
        when(idProviderMock.getUId()).thenReturn(
                getClonedRecipeCourseZero().getId(),
                getClonedRecipeCourseTwo().getId(),
                getClonedRecipeCourseFour().getId(),
                getClonedRecipeCourseSix().getId());
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}