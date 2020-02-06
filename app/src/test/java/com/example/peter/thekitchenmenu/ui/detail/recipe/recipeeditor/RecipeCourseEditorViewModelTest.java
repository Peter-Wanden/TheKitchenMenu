package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.*;
import com.example.peter.thekitchenmenu.data.repository.*;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipecourse.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions.RecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipestate.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.domain.usecase.recipeduration.RecipeDurationTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeidentity.RecipeIdentityTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportions.RecipePortionsTest;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeEntity;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.*;
import org.mockito.*;

import java.util.ArrayList;

import static com.example.peter.thekitchenmenu.testdata.TestDataRecipeCourseEntity.*;
import static com.example.peter.thekitchenmenu.testdata.TestDataRecipeEntity.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RecipeCourseEditorViewModelTest {

    // region constants ----------------------------------------------------------------------------
    private RecipeEntity VALID_EXISTING_RECIPE_ENTITY = getValidExisting();
    private String EXISTING_RECIPE_ID = VALID_EXISTING_RECIPE_ENTITY.getId();
    private String NEW_RECIPE_ID = TestDataRecipeEntity.getNewInvalid().getId();

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RepositoryRecipe repoRecipeMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeEntity>> repoRecipeCallback;
    @Mock
    RepositoryRecipeIdentity repoIdentityMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeIdentityEntity>> repoIdentityCallback;
    @Mock
    RepositoryRecipeCourse repoCourseMock;
    @Captor
    ArgumentCaptor<DataSource.GetAllCallback<RecipeCourseEntity>> repoCourseCallback;
    @Mock
    RepositoryRecipeDuration repoDurationMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipeDurationEntity>> repoDurationCallback;
    @Mock
    RepositoryRecipePortions repoPortionsMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<RecipePortionsEntity>> repoPortionsCallback;
    @Mock
    UniqueIdProvider idProviderMock;
    @Mock
    TimeProvider timeProviderMock;
    @Captor
//    ArgumentCaptor<RecipeComponentStateModel> componentStatus;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeCourseEditorViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenViewModel();
    }

    private RecipeCourseEditorViewModel givenViewModel() {
        UseCaseHandler handler = new UseCaseHandler(new UseCaseSchedulerMock()
        );

        TextValidator textValidator = new TextValidator.Builder().
                setShortTextMinLength(RecipeIdentityTest.TITLE_MIN_LENGTH).
                setShortTextMaxLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                setLongTextMinLength(RecipeIdentityTest.DESCRIPTION_MIN_LENGTH).
                setLongTextMaxLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                build();

        RecipeIdentity identity = new RecipeIdentity(
                repoIdentityMock,
                timeProviderMock,
                handler,
                textValidator
        );

        RecipeCourse course = new RecipeCourse(
                repoCourseMock,
                idProviderMock,
                timeProviderMock
        );

        RecipeDuration duration = new RecipeDuration(
                repoDurationMock,
                timeProviderMock,
                RecipeDurationTest.MAX_PREP_TIME,
                RecipeDurationTest.MAX_COOK_TIME
        );

        RecipePortions portions = new RecipePortions(
                repoPortionsMock,
                idProviderMock,
                timeProviderMock,
                RecipePortionsTest.MAX_SERVINGS,
                RecipePortionsTest.MAX_SITTINGS
        );

        RecipeStateCalculator stateCalculator = new RecipeStateCalculator();

        Recipe recipe = new Recipe(
                repoRecipeMock,
                handler,
                stateCalculator,
                identity,
                course,
                duration,
                portions);

        return new RecipeCourseEditorViewModel(handler, recipe);
    }

    @Test
    public void start_recipeIdSupplied_databaseCalledForListOfCourses() {
        // Arrange
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        // Assert
        verify(repoCourseMock).getCoursesForRecipe(eq(EXISTING_RECIPE_ID), repoCourseCallback.capture());
    }

    @Test
    public void start_recipeIdSupplied_observersCalled() {
        // Arrange
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        confirmRepoCalledAndReturnMatchingCourses(EXISTING_RECIPE_ID);
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
    public void startWithClone_cloneFromAndToIds_persistenceCalledWithCloneFromId() {
        // Arrange
        // Act
        SUT.startByCloningModel(EXISTING_RECIPE_ID, NEW_RECIPE_ID);
        // Assert
        verify(repoCourseMock).getCoursesForRecipe(eq(EXISTING_RECIPE_ID), anyObject());
    }

    @Test
    public void startWithClonedModel_cloneFromAndToIds_whenDeleteCourse_courseDeletedFromCloneToId() {
        // Arrange
        whenUniqueIdProviderMockCalledReturnClonedEvenIds();
        // Act
        SUT.startByCloningModel(EXISTING_RECIPE_ID, NEW_RECIPE_ID);
        simulateGetEvenCoursesForRecipeFromDatabase();
        // Assert
        assertTrue(SUT.isCourseZero());
        SUT.setCourseZero(false);
        verify(repoCourseMock).deleteById(eq(getClonedRecipeCourseZero().getId()));
        SUT.setCourseFour(false);
        verify(repoCourseMock).deleteById(eq(getClonedRecipeCourseFour().getId()));
    }

     @Test
    public void courseZeroSelected_true_courseZeroAndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseZero().getId());
        whenTimeProviderCalledReturnTime(getRecipeCourseZero().getCreateDate());
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        SUT.setCourseZero(true);
        // Assert
        verify(repoCourseMock).save(getRecipeCourseZero());
    }

    @Test
    public void courseZeroSelected_false_courseZeroForRecipeRemovedFromDatabase() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        confirmRepoCalledAndReturnMatchingCourses(EXISTING_RECIPE_ID);
        SUT.setCourseZero(false);
        // Assert
        verify(repoCourseMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(0).getId(), ac.getValue());
    }

    @Test
    public void courseOneSelected_true_courseOneAndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseOne().getId());
        whenTimeProviderCalledReturnTime(getRecipeCourseFour().getCreateDate());
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        SUT.setCourseOne(true);
        // Assert
        verify(repoCourseMock).save(getRecipeCourseOne());
    }

    @Test
    public void courseOneSelected_false_courseOneAndRecipeIdRemovedFromDatabase() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        confirmRepoCalledAndReturnMatchingCourses(EXISTING_RECIPE_ID);
        SUT.setCourseOne(false);
        // Assert
        verify(repoCourseMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(1).getId(), ac.getValue());
    }

    @Test
    public void courseTwoSelected_true_courseId2AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseTwo().getId());
        whenTimeProviderCalledReturnTime(getRecipeCourseTwo().getCreateDate());
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        SUT.setCourseTwo(true);
        // Assert
        verify(repoCourseMock).save(getRecipeCourseTwo());
    }

    @Test
    public void courseTwoSelected_false_courseId2AndRecipeIdRemovedFromDatabase() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        confirmRepoCalledAndReturnMatchingCourses(EXISTING_RECIPE_ID);
        SUT.setCourseTwo(false);
        // Assert
        verify(repoCourseMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(2).getId(), ac.getValue());
    }

    @Test
    public void courseThreeSelected_true_courseId3AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseThree().getId());
        whenTimeProviderCalledReturnTime(getRecipeCourseThree().getCreateDate());
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        SUT.setCourseThree(true);
        // Assert
        verify(repoCourseMock).save(getRecipeCourseThree());
    }

    @Test
    public void courseThreeSelected_false_courseId3AndRecipeIdRemovedFromDatabase() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        confirmRepoCalledAndReturnMatchingCourses(EXISTING_RECIPE_ID);
        SUT.setCourseThree(false);
        // Assert
        verify(repoCourseMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(3).getId(), ac.getValue());
    }

    @Test
    public void courseFourSelected_true_courseId4AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseFour().getId());
        whenTimeProviderCalledReturnTime(getRecipeCourseFour().getCreateDate());
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        SUT.setCourseFour(true);
        // Assert
        verify(repoCourseMock).save(getRecipeCourseFour());
    }

    @Test
    public void courseFourSelected_false_courseId4AndRecipeIdRemovedFromDatabase() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        confirmRepoCalledAndReturnMatchingCourses(EXISTING_RECIPE_ID);
        SUT.setCourseFour(false);
        // Assert
        verify(repoCourseMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(4).getId(), ac.getValue());
    }

    @Test
    public void courseFiveSelected_true_courseId5AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseFive().getId());
        whenTimeProviderCalledReturnTime(getRecipeCourseFive().getCreateDate());
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        SUT.setCourseFive(true);
        // Assert
        verify(repoCourseMock).save(getRecipeCourseFive());
    }

    @Test
    public void courseFiveSelected_false_courseId5AndRecipeIdRemovedFromDatabase() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        confirmRepoCalledAndReturnMatchingCourses(EXISTING_RECIPE_ID);
        SUT.setCourseFive(false);
        // Assert
        verify(repoCourseMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(5).getId(), ac.getValue());
    }

    @Test
    public void courseSixSelected_true_courseId6AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseSix().getId());
        whenTimeProviderCalledReturnTime(getRecipeCourseSix().getCreateDate());
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        SUT.setCourseSix(true);
        // Assert
        verify(repoCourseMock).save(getRecipeCourseSix());
    }

    @Test
    public void courseSixSelected_false_courseId6AndRecipeIdRemovedFromDatabase() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        confirmRepoCalledAndReturnMatchingCourses(EXISTING_RECIPE_ID);
        SUT.setCourseSix(false);
        // Assert
        verify(repoCourseMock).deleteById(ac.capture());
        assertEquals(getAllRecipeCoursesDatabaseResponse().get(6).getId(), ac.getValue());
    }

    @Test
    public void courseSevenSelected_true_courseId7AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseSeven().getId());
        whenTimeProviderCalledReturnTime(getRecipeCourseSeven().getCreateDate());
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        SUT.setCourseSeven(true);
        // Assert
        verify(repoCourseMock).save(getRecipeCourseSeven());
    }

    @Test
    public void courseSevenSelected_false_courseId7AndRecipeIdRemovedFromDatabase() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        confirmRepoCalledAndReturnMatchingCourses(EXISTING_RECIPE_ID);
        SUT.setCourseSeven(false);
        // Assert
        verify(repoCourseMock).deleteById(ac.capture());
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
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        simulateReturnNoCoursesForRecipeFromDatabase();
        // Assert

    }

    @Test
    public void start_recipeIdSupplied_RecipeModelStatus_COURSES_MODEL_UNCHANGED_VALID() {
        // Arrange
        // Act
        SUT.start(EXISTING_RECIPE_ID);
        confirmRepoCalledAndReturnMatchingCourses(EXISTING_RECIPE_ID);
        // Assert

    }

    @Test
    public void allOptionsDeselected_recipeIdSupplied_RecipeModelStatus_COURSES_MODEL_CHANGED_INVALID() {
        // Arrange

        // Act
        SUT.start(EXISTING_RECIPE_ID);
        confirmRepoCalledAndReturnMatchingCourses(EXISTING_RECIPE_ID);
        SUT.setCourseZero(false);
        SUT.setCourseOne(false);
        SUT.setCourseTwo(false);
        SUT.setCourseThree(false);
        SUT.setCourseFour(false);
        SUT.setCourseFive(false);
        SUT.setCourseSix(false);
        SUT.setCourseSeven(false);
        // Assert
    }

    @Test
    public void allButOneOptionsDeselected_recipeIdSupplied_RecipeModelStatus_COURSES_MODEL_CHANGED_VALID() {
        // Arrange

        // Act
        SUT.start(EXISTING_RECIPE_ID);
        confirmRepoCalledAndReturnMatchingCourses(EXISTING_RECIPE_ID);
        SUT.setCourseZero(false);
        SUT.setCourseOne(false);
        SUT.setCourseTwo(false);
        SUT.setCourseThree(false);
        SUT.setCourseFour(false);
        SUT.setCourseFive(false);
        SUT.setCourseSix(false);
        // Assert
    }

    // region helper methods -----------------------------------------------------------------------
    private void confirmRepoCalledAndReturnMatchingCourses(String recipeId) {
        // verify call database with recipe id
        verify(repoCourseMock).getCoursesForRecipe(eq(recipeId), repoCourseCallback.capture());
        // simulate list of courses returned
        repoCourseCallback.getValue().onAllLoaded(getAllByRecipeId(recipeId));
    }

    private void simulateGetEvenCoursesForRecipeFromDatabase() {
        // verify call database with recipe id
        verify(repoCourseMock).getCoursesForRecipe(eq(EXISTING_RECIPE_ID), repoCourseCallback.capture());
        // simulate list of courses returned
        repoCourseCallback.getValue().onAllLoaded(getEvenRecipeCoursesDatabaseResponse());
    }

    private void simulateReturnNoCoursesForRecipeFromDatabase() {
        // verify call database with recipe id
        verify(repoCourseMock).getCoursesForRecipe(eq(EXISTING_RECIPE_ID),
                repoCourseCallback.capture());
        // simulate list no courses returned
        repoCourseCallback.getValue().onDataNotAvailable();
    }

    private void whenUniqueIdProviderMockCalledReturnClonedEvenIds() {
        when(idProviderMock.getUId()).thenReturn(
                getClonedRecipeCourseZero().getId(),
                getClonedRecipeCourseTwo().getId(),
                getClonedRecipeCourseFour().getId(),
                getClonedRecipeCourseSix().getId());
    }

    private void whenTimeProviderCalledReturnTime(long time) {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}