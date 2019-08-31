package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.example.peter.thekitchenmenu.data.entity.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RecipeCourseDataSource;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

import org.junit.*;
import org.mockito.*;

import java.util.ArrayList;

import static com.example.peter.thekitchenmenu.testdata.RecipeCourseTestData.*;
import static com.example.peter.thekitchenmenu.testdata.RecipeTestData.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RecipeCourseSelectorViewModelTest {

    // region constants ----------------------------------------------------------------------------
    private static final RecipeEntity VALID_EXISTING_RECIPE_ENTITY = getValidExistingRecipeEntity();
    private static final String VALID_EXISTING_RECIPE_ID = VALID_EXISTING_RECIPE_ENTITY.getId();
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Captor
    ArgumentCaptor<DataSource.GetAllCallback<RecipeCourseEntity>> getEntityCallbackArgumentCaptor;
    @Mock
    RecipeCourseDataSource recipeCourseDataSourceMock;
    @Mock
    UniqueIdProvider idProviderMock;

    // endregion helper fields ---------------------------------------------------------------------

    private RecipeCourseSelectorViewModel SUT;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        SUT = new RecipeCourseSelectorViewModel(recipeCourseDataSourceMock, idProviderMock);
    }

    @Test
    public void onStart_recipeIdSupplied_databaseCalledForListOfCourses() throws Exception {
        // Arrange
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        // Assert
        verify(recipeCourseDataSourceMock).getCoursesForRecipe(eq(VALID_EXISTING_RECIPE_ID),
                getEntityCallbackArgumentCaptor.capture());
    }

    @Test
    public void onStart_recipeIdSupplied_observersCalled() throws Exception {
        // Arrange
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateGetCoursesForRecipeFromDatabase();
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
    public void onStart_recipeIdSupplied_evenObserversCalledOnly() throws Exception {
        // Arrange
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
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
    public void courseZeroSelected_true_courseZeroAndRecipeIdSavedToDatabase() throws Exception {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseZero().getId());
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        SUT.courseZeroObservable.set(true);
        // Assert
        verify(recipeCourseDataSourceMock).save(getRecipeCourseZero());
    }

    @Test
    public void courseZeroSelected_false_courseZeroForRecipeRemovedFromDatabase() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateGetCoursesForRecipeFromDatabase();
        SUT.courseZeroObservable.set(false);
        // Assert
        verify(recipeCourseDataSourceMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(0).getId(), ac.getValue());
    }

    @Test
    public void courseOneSelected_true_courseOneAndRecipeIdSavedToDatabase() throws Exception {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseOne().getId());
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        SUT.courseOneObservable.set(true);
        // Assert
        verify(recipeCourseDataSourceMock).save(getRecipeCourseOne());
    }

    @Test
    public void courseOneSelected_false_courseOneAndRecipeIdRemovedFromDatabase() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateGetCoursesForRecipeFromDatabase();
        SUT.courseOneObservable.set(false);
        // Assert
        verify(recipeCourseDataSourceMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(1).getId(), ac.getValue());
    }

    @Test
    public void courseTwoSelected_true_courseId2AndRecipeIdSavedToDatabase() throws Exception {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseTwo().getId());
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        SUT.courseTwoObservable.set(true);
        // Assert
        verify(recipeCourseDataSourceMock).save(getRecipeCourseTwo());
    }

    @Test
    public void courseTwoSelected_false_courseId2AndRecipeIdRemovedFromDatabase() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateGetCoursesForRecipeFromDatabase();
        SUT.courseTwoObservable.set(false);
        // Assert
        verify(recipeCourseDataSourceMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(2).getId(), ac.getValue());
    }

    @Test
    public void courseThreeSelected_true_courseId3AndRecipeIdSavedToDatabase() throws Exception {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseThree().getId());
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        SUT.courseThreeObservable.set(true);
        // Assert
        verify(recipeCourseDataSourceMock).save(getRecipeCourseThree());
    }

    @Test
    public void courseThreeSelected_false_courseId3AndRecipeIdRemovedFromDatabase() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateGetCoursesForRecipeFromDatabase();
        SUT.courseThreeObservable.set(false);
        // Assert
        verify(recipeCourseDataSourceMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(3).getId(), ac.getValue());
    }

    @Test
    public void courseFourSelected_true_courseId4AndRecipeIdSavedToDatabase() throws Exception {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseFour().getId());
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        SUT.courseFourObservable.set(true);
        // Assert
        verify(recipeCourseDataSourceMock).save(getRecipeCourseFour());
    }

    @Test
    public void courseFourSelected_false_courseId4AndRecipeIdRemovedFromDatabase() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateGetCoursesForRecipeFromDatabase();
        SUT.courseFourObservable.set(false);
        // Assert
        verify(recipeCourseDataSourceMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(4).getId(), ac.getValue());
    }

    @Test
    public void courseFiveSelected_true_courseId5AndRecipeIdSavedToDatabase() throws Exception {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseFive().getId());
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        SUT.courseFiveObservable.set(true);
        // Assert
        verify(recipeCourseDataSourceMock).save(getRecipeCourseFive());
    }

    @Test
    public void courseFiveSelected_false_courseId5AndRecipeIdRemovedFromDatabase() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateGetCoursesForRecipeFromDatabase();
        SUT.courseFiveObservable.set(false);
        // Assert
        verify(recipeCourseDataSourceMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(5).getId(), ac.getValue());
    }

    @Test
    public void courseSixSelected_true_courseId6AndRecipeIdSavedToDatabase() throws Exception {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseSix().getId());
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        SUT.courseSixObservable.set(true);
        // Assert
        verify(recipeCourseDataSourceMock).save(getRecipeCourseSix());
    }

    @Test
    public void courseSixSelected_false_courseId6AndRecipeIdRemovedFromDatabase() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateGetCoursesForRecipeFromDatabase();
        SUT.courseSixObservable.set(false);
        // Assert
        verify(recipeCourseDataSourceMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(6).getId(), ac.getValue());
    }

    @Test
    public void courseSevenSelected_true_courseId7AndRecipeIdSavedToDatabase() throws Exception {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseSeven().getId());
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        SUT.courseSevenObservable.set(true);
        // Assert
        verify(recipeCourseDataSourceMock).save(getRecipeCourseSeven());
    }

    @Test
    public void courseSevenSelected_false_courseId7AndRecipeIdRemovedFromDatabase() throws Exception {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.onStart(VALID_EXISTING_RECIPE_ID);
        simulateGetCoursesForRecipeFromDatabase();
        SUT.courseSevenObservable.set(false);
        // Assert
        verify(recipeCourseDataSourceMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(7).getId(), ac.getValue());
    }

    @Test(expected = RuntimeException.class)
    public void onStart_noRecipeIdSupplied_throwsRuntimeException() throws Exception {
        // Arrange
        // Act
        SUT.onStart(null);
        // Assert
        ArrayList list = new ArrayList();
        RuntimeException exception = (RuntimeException) list.get(0);
        assertEquals(exception.getMessage(), "This should throw an error!");
    }

    // region helper methods -----------------------------------------------------------------------
    private void simulateGetCoursesForRecipeFromDatabase() {
        // call database with recipe id
        verify(recipeCourseDataSourceMock).getCoursesForRecipe(eq(VALID_EXISTING_RECIPE_ID),
                getEntityCallbackArgumentCaptor.capture());
        // simulate list of courses returned
        getEntityCallbackArgumentCaptor.getValue().onAllLoaded(
                getAllRecipeCoursesDatabaseResponse());
    }

    private void simulateGetEvenCoursesForRecipeFromDatabase() {
        // call database with recipe id
        verify(recipeCourseDataSourceMock).getCoursesForRecipe(eq(VALID_EXISTING_RECIPE_ID),
                getEntityCallbackArgumentCaptor.capture());
        // simulate list of courses returned
        getEntityCallbackArgumentCaptor.getValue().onAllLoaded(
                getEvenRecipeCoursesDatabaseResponse());
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}