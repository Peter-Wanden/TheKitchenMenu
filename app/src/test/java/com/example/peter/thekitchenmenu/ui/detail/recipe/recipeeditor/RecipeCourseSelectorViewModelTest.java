package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.peter.thekitchenmenu.data.entity.*;
import com.example.peter.thekitchenmenu.data.repository.*;
import com.example.peter.thekitchenmenu.testdata.RecipeEntityTestData;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

import org.junit.*;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.peter.thekitchenmenu.testdata.RecipeCourseTestData.*;
import static com.example.peter.thekitchenmenu.testdata.RecipeEntityTestData.*;
import static com.example.peter.thekitchenmenu.testdata.RecipeValidatorTestData.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RecipeCourseSelectorViewModelTest {

    // region constants ----------------------------------------------------------------------------
    private RecipeEntity VALID_EXISTING_RECIPE_ENTITY = getValidExisting();
    private String EXISTING_RECIPE_ID = VALID_EXISTING_RECIPE_ENTITY.getId();
    private String NEW_RECIPE_ID = RecipeEntityTestData.getInvalidNew().getId();

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
    DataSourceRecipeCourse dataSourceRecipeCourseMock;
    @Mock
    UniqueIdProvider idProviderMock;
    @Mock
    RecipeValidation.RecipeValidatorModelSubmission modelSubmissionMock;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeCourseSelectorViewModel SUT;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        SUT = new RecipeCourseSelectorViewModel(dataSourceRecipeCourseMock, idProviderMock);
        SUT.setModelValidationSubmitter(modelSubmissionMock);
    }

    @Test
    public void start_recipeIdSupplied_databaseCalledForListOfCourses() throws Exception {
        // Arrange
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        // Assert
        verify(dataSourceRecipeCourseMock).getCoursesForRecipe(eq(EXISTING_RECIPE_ID),
                getEntityCallbackArgumentCaptor.capture());
    }

    @Test
    public void start_recipeIdSupplied_observersCalled() throws Exception {
        // Arrange
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateGetAllCoursesForRecipeFromDatabase();
        // Assert
        assertTrue(SUT.courseZeroObservable.get());
        assertTrue(SUT.courseOneObservable.get());
        assertTrue(SUT.courseTwoObservable.get());
        assertTrue(SUT.courseThreeObservable.get());
        assertTrue(SUT.courseFourObservable.get());
        assertTrue(SUT.courseFiveObservable.get());
        assertTrue(SUT.courseSixObservable.get());
        assertTrue(SUT.courseSevenObservable.get());
    }

    @Test
    public void start_recipeIdSupplied_evenObserversCalledOnly() throws Exception {
        // Arrange
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateGetEvenCoursesForRecipeFromDatabase();
        // Assert
        assertTrue(SUT.courseZeroObservable.get());
        assertFalse(SUT.courseOneObservable.get());
        assertTrue(SUT.courseTwoObservable.get());
        assertFalse(SUT.courseThreeObservable.get());
        assertTrue(SUT.courseFourObservable.get());
        assertFalse(SUT.courseFiveObservable.get());
        assertTrue(SUT.courseSixObservable.get());
        assertFalse(SUT.courseSevenObservable.get());
    }

    @Test
    public void startWithClonedModel_oldAndNewRecipeIdSupplied_databaseCalledForListOfOldCourses() throws Exception {
        // Arrange
        // Act
        SUT.startByCloningModel(EXISTING_RECIPE_ID, NEW_RECIPE_ID);
        // Assert
        verify(dataSourceRecipeCourseMock).getCoursesForRecipe(eq(EXISTING_RECIPE_ID), anyObject());
    }

    @Test
    public void startWithClonedModel_oldAndNewRecipeIdSupplied_oldCoursesSavedWithNewRecipeId() throws Exception {
        ArgumentCaptor<RecipeCourseEntity> ac = ArgumentCaptor.forClass(RecipeCourseEntity.class);
        WhenUniqueIdProviderMockCalledReturnClonedEvenIds();
        // Act
        SUT.startByCloningModel(EXISTING_RECIPE_ID, NEW_RECIPE_ID);
        simulateGetEvenCoursesForRecipeFromDatabase();
        // Assert
        verify(dataSourceRecipeCourseMock, times(4)).save(ac.capture());
        List<RecipeCourseEntity> courseEntities = ac.getAllValues();
        assertEquals(getClonedRecipeCourseZero(), courseEntities.get(0));
        assertEquals(getClonedRecipeCourseTwo(), courseEntities.get(1));
        assertEquals(getClonedRecipeCourseFour(), courseEntities.get(2));
        assertEquals(getClonedRecipeCourseSix(), courseEntities.get(3));
    }

    @Test
    public void startWithClonedModel_oldAndNewRecipeIdSupplied_courseSubtractionsAreDeletedFromNewCourseId() throws Exception {
        // Arrange
        WhenUniqueIdProviderMockCalledReturnClonedEvenIds();
        // Act
        SUT.startByCloningModel(EXISTING_RECIPE_ID, NEW_RECIPE_ID);
        simulateGetEvenCoursesForRecipeFromDatabase();
        // Assert
        assertTrue(SUT.courseZeroObservable.get());
        SUT.courseZeroObservable.set(false);
        verify(dataSourceRecipeCourseMock).deleteById(eq(getClonedRecipeCourseZero().getId()));
        SUT.courseFourObservable.set(false);
        verify(dataSourceRecipeCourseMock).deleteById(eq(getClonedRecipeCourseFour().getId()));
    }

    @Test
    public void startWithClonedModel_oldAndNewRecipeIdSupplied_courseAdditionsSavedWithNewRecipeId() throws Exception {
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
        assertFalse(SUT.courseOneObservable.get());
        SUT.courseOneObservable.set(true);
        // Assert
        verify(dataSourceRecipeCourseMock).save(eq(newRecipeCourseOne));
    }

     @Test
    public void courseZeroSelected_true_courseZeroAndRecipeIdSavedToDatabase() throws Exception {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseZero().getId());
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        SUT.courseZeroObservable.set(true);
        // Assert
        verify(dataSourceRecipeCourseMock).save(getRecipeCourseZero());
    }

    @Test
    public void courseZeroSelected_false_courseZeroForRecipeRemovedFromDatabase() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateGetAllCoursesForRecipeFromDatabase();
        SUT.courseZeroObservable.set(false);
        // Assert
        verify(dataSourceRecipeCourseMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(0).getId(), ac.getValue());
    }

    @Test
    public void courseOneSelected_true_courseOneAndRecipeIdSavedToDatabase() throws Exception {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseOne().getId());
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        SUT.courseOneObservable.set(true);
        // Assert
        verify(dataSourceRecipeCourseMock).save(getRecipeCourseOne());
    }

    @Test
    public void courseOneSelected_false_courseOneAndRecipeIdRemovedFromDatabase() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateGetAllCoursesForRecipeFromDatabase();
        SUT.courseOneObservable.set(false);
        // Assert
        verify(dataSourceRecipeCourseMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(1).getId(), ac.getValue());
    }

    @Test
    public void courseTwoSelected_true_courseId2AndRecipeIdSavedToDatabase() throws Exception {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseTwo().getId());
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        SUT.courseTwoObservable.set(true);
        // Assert
        verify(dataSourceRecipeCourseMock).save(getRecipeCourseTwo());
    }

    @Test
    public void courseTwoSelected_false_courseId2AndRecipeIdRemovedFromDatabase() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateGetAllCoursesForRecipeFromDatabase();
        SUT.courseTwoObservable.set(false);
        // Assert
        verify(dataSourceRecipeCourseMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(2).getId(), ac.getValue());
    }

    @Test
    public void courseThreeSelected_true_courseId3AndRecipeIdSavedToDatabase() throws Exception {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseThree().getId());
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        SUT.courseThreeObservable.set(true);
        // Assert
        verify(dataSourceRecipeCourseMock).save(getRecipeCourseThree());
    }

    @Test
    public void courseThreeSelected_false_courseId3AndRecipeIdRemovedFromDatabase() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateGetAllCoursesForRecipeFromDatabase();
        SUT.courseThreeObservable.set(false);
        // Assert
        verify(dataSourceRecipeCourseMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(3).getId(), ac.getValue());
    }

    @Test
    public void courseFourSelected_true_courseId4AndRecipeIdSavedToDatabase() throws Exception {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseFour().getId());
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        SUT.courseFourObservable.set(true);
        // Assert
        verify(dataSourceRecipeCourseMock).save(getRecipeCourseFour());
    }

    @Test
    public void courseFourSelected_false_courseId4AndRecipeIdRemovedFromDatabase() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateGetAllCoursesForRecipeFromDatabase();
        SUT.courseFourObservable.set(false);
        // Assert
        verify(dataSourceRecipeCourseMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(4).getId(), ac.getValue());
    }

    @Test
    public void courseFiveSelected_true_courseId5AndRecipeIdSavedToDatabase() throws Exception {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseFive().getId());
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        SUT.courseFiveObservable.set(true);
        // Assert
        verify(dataSourceRecipeCourseMock).save(getRecipeCourseFive());
    }

    @Test
    public void courseFiveSelected_false_courseId5AndRecipeIdRemovedFromDatabase() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateGetAllCoursesForRecipeFromDatabase();
        SUT.courseFiveObservable.set(false);
        // Assert
        verify(dataSourceRecipeCourseMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(5).getId(), ac.getValue());
    }

    @Test
    public void courseSixSelected_true_courseId6AndRecipeIdSavedToDatabase() throws Exception {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseSix().getId());
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        SUT.courseSixObservable.set(true);
        // Assert
        verify(dataSourceRecipeCourseMock).save(getRecipeCourseSix());
    }

    @Test
    public void courseSixSelected_false_courseId6AndRecipeIdRemovedFromDatabase() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateGetAllCoursesForRecipeFromDatabase();
        SUT.courseSixObservable.set(false);
        // Assert
        verify(dataSourceRecipeCourseMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(6).getId(), ac.getValue());
    }

    @Test
    public void courseSevenSelected_true_courseId7AndRecipeIdSavedToDatabase() throws Exception {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseSeven().getId());
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        SUT.courseSevenObservable.set(true);
        // Assert
        verify(dataSourceRecipeCourseMock).save(getRecipeCourseSeven());
    }

    @Test
    public void courseSevenSelected_false_courseId7AndRecipeIdRemovedFromDatabase() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateGetAllCoursesForRecipeFromDatabase();
        SUT.courseSevenObservable.set(false);
        // Assert
        verify(dataSourceRecipeCourseMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(7).getId(), ac.getValue());
    }

    @Test(expected = RuntimeException.class)
    public void start_noRecipeIdSupplied_throwsRuntimeException() throws Exception {
        // Arrange
        // Act
        SUT.start(null);
        // Assert
        ArrayList list = new ArrayList();
        RuntimeException exception = (RuntimeException) list.get(0);
        assertEquals(exception.getMessage(), "This should throw an error!");
    }

    @Test
    public void start_recipeIdSupplied_RecipeModelStatus_COURSES_MODEL_UNCHANGED_INVALID() throws Exception {
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
    public void start_recipeIdSupplied_RecipeModelStatus_COURSES_MODEL_UNCHANGED_VALID() throws Exception {
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
    public void allOptionsDeselected_recipeIdSupplied_RecipeModelStatus_COURSES_MODEL_CHANGED_INVALID() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateGetAllCoursesForRecipeFromDatabase();
        SUT.courseZeroObservable.set(false);
        SUT.courseOneObservable.set(false);
        SUT.courseTwoObservable.set(false);
        SUT.courseThreeObservable.set(false);
        SUT.courseFourObservable.set(false);
        SUT.courseFiveObservable.set(false);
        SUT.courseSixObservable.set(false);
        SUT.courseSevenObservable.set(false);
        // Assert
        verify(modelSubmissionMock, times(9)).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(COURSES_MODEL_CHANGED_INVALID, modelStatus);
    }

    @Test
    public void allButOneOptionsDeselected_recipeIdSupplied_RecipeModelStatus_COURSES_MODEL_CHANGED_VALID() throws Exception {
        // Arrange
        ArgumentCaptor<RecipeModelStatus> ac = ArgumentCaptor.forClass(RecipeModelStatus.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateGetAllCoursesForRecipeFromDatabase();
        SUT.courseZeroObservable.set(false);
        SUT.courseOneObservable.set(false);
        SUT.courseTwoObservable.set(false);
        SUT.courseThreeObservable.set(false);
        SUT.courseFourObservable.set(false);
        SUT.courseFiveObservable.set(false);
        SUT.courseSixObservable.set(false);
        // Assert
        verify(modelSubmissionMock, times(8)).submitModelStatus(ac.capture());
        RecipeModelStatus modelStatus = ac.getValue();
        assertEquals(COURSES_MODEL_CHANGED_VALID, modelStatus);
    }

    // region helper methods -----------------------------------------------------------------------
    private void simulateGetAllCoursesForRecipeFromDatabase() {
        // verify call database with recipe id
        verify(dataSourceRecipeCourseMock).getCoursesForRecipe(eq(EXISTING_RECIPE_ID),
                getEntityCallbackArgumentCaptor.capture());
        // simulate list of courses returned
        getEntityCallbackArgumentCaptor.getValue().onAllLoaded(
                getAllRecipeCoursesDatabaseResponse());
    }

    private void simulateGetEvenCoursesForRecipeFromDatabase() {
        // verify call database with recipe id
        verify(dataSourceRecipeCourseMock).getCoursesForRecipe(eq(EXISTING_RECIPE_ID),
                getEntityCallbackArgumentCaptor.capture());
        // simulate list of courses returned
        getEntityCallbackArgumentCaptor.getValue().onAllLoaded(
                getEvenRecipeCoursesDatabaseResponse());
    }

    private void simulateReturnNoCoursesForRecipeFromDatabase() {
        // verify call database with recipe id
        verify(dataSourceRecipeCourseMock).getCoursesForRecipe(eq(EXISTING_RECIPE_ID),
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