package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.*;
import com.example.peter.thekitchenmenu.data.repository.*;
import com.example.peter.thekitchenmenu.domain.usecase.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipecourse.RecipeCourseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipecourse.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeduration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeidentity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeportions.RecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateCalculator;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeduration.RecipeDurationTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeidentity.RecipeIdentityTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.recipeportions.RecipePortionsTest;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeCourseEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeDurationEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeEntity;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeIdentityEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipePortionsEntity;

import org.junit.*;
import org.mockito.*;

import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.state.RecipeStateCalculator.*;
import static com.example.peter.thekitchenmenu.testdata.TestDataRecipeCourseEntity.*;
import static com.example.peter.thekitchenmenu.testdata.TestDataRecipeEntity.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RecipeCourseEditorViewModelTest {

    private static final String TAG = "tkm-" + RecipeCourseEditorViewModelTest.class.
            getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    private RecipeEntity VALID_EXISTING_RECIPE_ENTITY = getValidExisting();
    private String EXISTING_RECIPE_ID = VALID_EXISTING_RECIPE_ENTITY.getId();
    private String NEW_RECIPE_ID = TestDataRecipeEntity.getNewInvalid().getId();

    private static final RecipeEntity RECIPE_INVALID_NEW =
            TestDataRecipeEntity.getNewInvalid();
    private static final RecipeEntity RECIPE_VALID_EXISTING =
            TestDataRecipeEntity.getValidExisting();
    private static final RecipeIdentityEntity IDENTITY_VALID_EXISTING_COMPLETE =
            TestDataRecipeIdentityEntity.getValidExistingTitleValidDescriptionValid();
    private static final RecipeDurationEntity DURATION_VALID_EXISTING_COMPLETE =
            TestDataRecipeDurationEntity.getValidExistingComplete();
    private static final RecipePortionsEntity PORTIONS_VALID_EXISTING_NINE =
            TestDataRecipePortionsEntity.getExistingValidNinePortions();
    private static final List<RecipeCourseEntity> COURSES_VALID_EXISTING_ALL =
            TestDataRecipeCourseEntity.getAllRecipeCourses();

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

    private UseCaseHandler handler;
    private Recipe recipeMacro;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeCourseEditorViewModel SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenViewModel();
    }

    private RecipeCourseEditorViewModel givenViewModel() {
        handler = new UseCaseHandler(new UseCaseSchedulerMock()
        );

        TextValidator textValidator = new TextValidator.Builder().
                setShortTextMinLength(RecipeIdentityTest.TITLE_MIN_LENGTH).
                setShortTextMaxLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                setLongTextMinLength(RecipeIdentityTest.DESCRIPTION_MIN_LENGTH).
                setLongTextMaxLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                build();

        RecipeMetadata recipeMetaData = new RecipeMetadata(
                timeProviderMock,
                repoRecipeMock
        );

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

        recipeMacro = new Recipe(
                handler,
                stateCalculator,
                recipeMetaData,
                identity,
                course,
                duration,
                portions);

        return new RecipeCourseEditorViewModel(handler, recipeMacro);
    }

    @Test
    public void start_recipeIdSupplied_databaseCalledForListOfCourses() {
        // Arrange
        String recipeId = RECIPE_VALID_EXISTING.getId();

        // An external request that starts loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setId(recipeId).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(recipeId);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(recipeId);
        verify(repoCourseMock).getCoursesForRecipe(eq(recipeId), repoCourseCallback.capture());
    }

    @Test
    public void start_recipeIdSupplied_observersCalled() {
        // Arrange
        String recipeId = RECIPE_VALID_EXISTING.getId();

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setId(recipeId).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(recipeId);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(recipeId);
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
        String recipeId = RECIPE_VALID_EXISTING.getId();

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setId(recipeId).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnEvenCoursesForId(recipeId);
        assertTrue(SUT.isCourseZero());
        assertFalse(SUT.isCourseOne());
        assertTrue(SUT.isCourseTwo());
        assertFalse(SUT.isCourseThree());
        assertTrue(SUT.isCourseFour());
        assertFalse(SUT.isCourseFive());
        assertTrue(SUT.isCourseSix());
        assertFalse(SUT.isCourseSeven());
    }

//    @Test
//    public void startWithClone_cloneFromAndToIds_persistenceCalledWithCloneFromId() {
//        // Arrange
//        String cloneFromRecipeId = RECIPE_VALID_EXISTING.getId();
//        String cloneToRecipeId = RECIPE_INVALID_NEW.getId();
//        // An external request that starts/loads the recipe
//        RecipeRequest request = new RecipeRequest.Builder().
//                setId(cloneFromRecipeId).
//                setCloneToId(cloneToRecipeId).
//                build();
//
//        // Act
//        handler.execute(recipeMacro, request, new RecipeResponseCallback());
//
//        // Assert
//        verifyAllOtherComponentReposCalledAndReturnValidExisting(cloneFromRecipeId);
//        verifyRepoCourseCalledAndReturnCoursesMatchingId(cloneFromRecipeId);
//    }

//    @Test
//    public void startWithClonedModel_cloneFromAndToIds_whenDeleteCourse_courseDeletedFromCloneToId() {
//        // Arrange
//        String cloneFromRecipeId = RECIPE_VALID_EXISTING.getId();
//        String cloneToRecipeId = RECIPE_INVALID_NEW.getId();
//        whenIdProviderReturnClonedEvenIds();
//
//        // An external request that starts/loads the recipe
//        RecipeRequest request = new RecipeRequest.Builder().
//                setId(cloneFromRecipeId).
//                setCloneToId(cloneToRecipeId).
//                build();
//
//        // Act
//        handler.execute(recipeMacro, request, new RecipeResponseCallback());
//
//        // Assert
//        verifyAllOtherComponentReposCalledAndReturnValidExisting(cloneFromRecipeId);
//        verifyRepoCourseCalledAndReturnEvenCoursesForId(cloneFromRecipeId);
//
//        assertTrue(SUT.isCourseZero());
//        SUT.setCourseZero(false);
//        verify(repoCourseMock).deleteById(eq(getClonedRecipeCourseZero().getId()));
//        assertFalse(SUT.isCourseZero());
//        SUT.setCourseFour(false);
//        verify(repoCourseMock).deleteById(eq(getClonedRecipeCourseFour().getId()));
//    }

    @Test
    public void courseZeroSelected_true_courseZeroAndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseZero().getId());
        whenTimeProviderCalledReturnTime(getRecipeCourseZero().getCreateDate());

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnDataUnavailableForId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseZero(true);

        // Assert
        verify(repoCourseMock).save(getRecipeCourseZero());
    }

    @Test
    public void courseZeroSelected_false_courseZeroForRecipeRemovedFromDatabase() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        SUT.setCourseZero(false);
        verify(repoCourseMock).deleteById(eq(TestDataRecipeCourseEntity.getRecipeCourseZero().getId()));
    }

    @Test
    public void courseOneSelected_true_courseOneAndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseOne().getId());
        whenTimeProviderCalledReturnTime(getRecipeCourseFour().getCreateDate());
        // Act
        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnDataUnavailableForId(EXISTING_RECIPE_ID);

        SUT.setCourseOne(true);
        // Assert
        verify(repoCourseMock).save(getRecipeCourseOne());
    }

    @Test
    public void courseOneSelected_false_courseOneAndRecipeIdRemovedFromDatabase() {
        // Arrange

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseOne(false);

        // Assert
        verify(repoCourseMock).deleteById(eq(getRecipeCourseOne().getId()));
    }

    @Test
    public void courseTwoSelected_true_courseId2AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseTwo().getId());
        whenTimeProviderCalledReturnTime(getRecipeCourseTwo().getCreateDate());

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnDataUnavailableForId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseTwo(true);
        // Assert
        verify(repoCourseMock).save(getRecipeCourseTwo());
    }

    @Test
    public void courseTwoSelected_false_courseId2AndRecipeIdRemovedFromDatabase() {
        // Arrange

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseTwo(false);
        verify(repoCourseMock).deleteById(eq(getRecipeCourseTwo().getId()));
    }

    @Test
    public void courseThreeSelected_true_courseId3AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseThree().getId());
        whenTimeProviderCalledReturnTime(getRecipeCourseThree().getCreateDate());

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnDataUnavailableForId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseThree(true);

        // Assert
        verify(repoCourseMock).save(getRecipeCourseThree());
    }

    @Test
    public void courseThreeSelected_false_courseId3AndRecipeIdRemovedFromDatabase() {
        // Arrange
        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseThree(false);

        // Assert
        verify(repoCourseMock).deleteById(eq(getRecipeCourseThree().getId()));
    }

    @Test
    public void courseFourSelected_true_courseId4AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseFour().getId());
        whenTimeProviderCalledReturnTime(getRecipeCourseFour().getCreateDate());

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnDataUnavailableForId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseFour(true);

        // Assert
        verify(repoCourseMock).save(getRecipeCourseFour());
    }

    @Test
    public void courseFourSelected_false_courseId4AndRecipeIdRemovedFromDatabase() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // Act
        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseFour(false);

        // Assert
        verify(repoCourseMock).deleteById(eq(getRecipeCourseFour().getId()));
    }

    @Test
    public void courseFiveSelected_true_courseId5AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseFive().getId());
        whenTimeProviderCalledReturnTime(getRecipeCourseFive().getCreateDate());

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnDataUnavailableForId(EXISTING_RECIPE_ID);

        //Act
        SUT.setCourseFive(true);

        // Assert
        verify(repoCourseMock).save(getRecipeCourseFive());
    }

    @Test
    public void courseFiveSelected_false_courseId5AndRecipeIdRemovedFromDatabase() {
        // Arrange
        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseFive(false);

        // Assert
        verify(repoCourseMock).deleteById(eq(getRecipeCourseFive().getId()));
    }

    @Test
    public void courseSixSelected_true_courseId6AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseSix().getId());
        whenTimeProviderCalledReturnTime(getRecipeCourseSix().getCreateDate());
        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnDataUnavailableForId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseSix(true);
        // Assert
        verify(repoCourseMock).save(getRecipeCourseSix());
    }

    @Test
    public void courseSixSelected_false_courseId6AndRecipeIdRemovedFromDatabase() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseSix(false);

        // Assert
        verify(repoCourseMock).deleteById(eq(getRecipeCourseSix().getId()));
    }

    @Test
    public void courseSevenSelected_true_courseId7AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseSeven().getId());
        whenTimeProviderCalledReturnTime(getRecipeCourseSeven().getCreateDate());

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnDataUnavailableForId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseSeven(true);

        // Assert
        verify(repoCourseMock).save(getRecipeCourseSeven());
    }

    @Test
    public void courseSevenSelected_false_courseId7AndRecipeIdRemovedFromDatabase() {
        // Arrange

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseSeven(false);

        // Assert
        verify(repoCourseMock).deleteById(getRecipeCourseSeven().getId());
    }

    @Test
    public void start_recipeIdSupplied_RecipeModelStatus_COURSES_MODEL_INVALID_UNCHANGED() {
        // Arrange
        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setId(EXISTING_RECIPE_ID).
                build();
        RecipeMacroResponseCallback macroCallback = new RecipeMacroResponseCallback();

        // Act
        recipeMacro.registerMacroCallback(macroCallback);
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnDataUnavailableForId(EXISTING_RECIPE_ID);

        ComponentState expectedState = ComponentState.INVALID_UNCHANGED;
        ComponentState actualState = macroCallback.
                getResponse().
                getRecipeStateResponse().
                getComponentStates().
                get(ComponentName.COURSE);
        assertEquals(expectedState, actualState);

        assertTrue(((RecipeCourseResponse) macroCallback.
                getResponse().
                getComponentResponses().
                get(ComponentName.COURSE)).
                getMetadata().
                getFailReasons().
                contains(CommonFailReason.DATA_UNAVAILABLE));
    }

    @Test
    public void start_recipeIdSupplied_RecipeModelStatus_COURSES_MODEL_VALID_UNCHANGED() {
        // Arrange

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setId(EXISTING_RECIPE_ID).
                build();
        RecipeMacroResponseCallback macroCallback = new RecipeMacroResponseCallback();

        // Act
        recipeMacro.registerMacroCallback(macroCallback);
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        ComponentState expectedState = ComponentState.VALID_UNCHANGED;
        ComponentState actualState = macroCallback.
                getResponse().
                getRecipeStateResponse().
                getComponentStates().
                get(ComponentName.COURSE);

        assertEquals(expectedState, actualState);
    }

    @Test
    public void allOptionsDeselected_recipeIdSupplied_RecipeModelStatus_COURSES_MODEL_CHANGED_INVALID() {
        // Arrange

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setId(EXISTING_RECIPE_ID).
                build();
        RecipeMacroResponseCallback macroCallback = new RecipeMacroResponseCallback();

        // Act
        recipeMacro.registerMacroCallback(macroCallback);
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseZero(false);
        SUT.setCourseOne(false);
        SUT.setCourseTwo(false);
        SUT.setCourseThree(false);
        SUT.setCourseFour(false);
        SUT.setCourseFive(false);
        SUT.setCourseSix(false);
        SUT.setCourseSeven(false);

        // Assert
        ComponentState expectedState = ComponentState.INVALID_CHANGED;
        ComponentState actualState = macroCallback.
                getResponse().
                getRecipeStateResponse().
                getComponentStates().
                get(ComponentName.COURSE);

        assertEquals(expectedState, actualState);
    }

    @Test
    public void allButOneOptionsDeselected_recipeIdSupplied_RecipeModelStatus_COURSES_MODEL_VALID_CHANGED() {
        // Arrange

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setId(EXISTING_RECIPE_ID).
                build();
        RecipeMacroResponseCallback macroCallback = new RecipeMacroResponseCallback();

        // Act
        recipeMacro.registerMacroCallback(macroCallback);
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseZero(false);
        SUT.setCourseOne(false);
        SUT.setCourseTwo(false);
        SUT.setCourseThree(false);
        SUT.setCourseFour(false);
        SUT.setCourseFive(false);
        SUT.setCourseSix(false);

        // Assert
        ComponentState expectedState = ComponentState.VALID_CHANGED;
        ComponentState actualState = macroCallback.
                getResponse().
                getRecipeStateResponse().
                getComponentStates().
                get(ComponentName.COURSE);

        assertEquals(expectedState, actualState);
    }

    // region helper methods -----------------------------------------------------------------------
    private void verifyRepoRecipeCalledAndReturnValidExistingRecipe(String recipeId) {
        verify(repoRecipeMock).getById(eq(recipeId), repoRecipeCallback.capture());
        repoRecipeCallback.getValue().onEntityLoaded(RECIPE_VALID_EXISTING);
    }

    private void verifyRepoCourseCalledAndReturnCoursesMatchingId(String recipeId) {
        verify(repoCourseMock).getCoursesForRecipe(eq(recipeId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onAllLoaded(getAllByRecipeId(recipeId));
    }

    private void verifyRepoCourseCalledAndReturnEvenCoursesForId(String recipeId) {
        verify(repoCourseMock).getCoursesForRecipe(eq(recipeId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onAllLoaded(getEvenRecipeCoursesDatabaseResponse());
    }

    private void verifyRepoCourseCalledAndReturnDataUnavailableForId(String recipeId) {
        verify(repoCourseMock).getCoursesForRecipe(eq(recipeId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onDataNotAvailable();
    }

    private void verifyAllOtherComponentReposCalledAndReturnValidExisting(String recipeId) {
        verify(repoRecipeMock).getById(eq(recipeId), repoRecipeCallback.capture());
        repoRecipeCallback.getValue().onEntityLoaded(RECIPE_VALID_EXISTING);

        verify(repoIdentityMock).getById(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(IDENTITY_VALID_EXISTING_COMPLETE);

        verify(repoDurationMock).getById(eq(recipeId), repoDurationCallback.capture());
        repoDurationCallback.getValue().onEntityLoaded(DURATION_VALID_EXISTING_COMPLETE);

        verify(repoPortionsMock).getPortionsForRecipe(eq(recipeId), repoPortionsCallback.capture());
        repoPortionsCallback.getValue().onEntityLoaded(PORTIONS_VALID_EXISTING_NINE);
    }

    private void whenIdProviderReturnClonedEvenIds() {
        when(idProviderMock.getUId()).thenReturn(
                getClonedRecipeCourseZero().getId(),
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
    private static class RecipeResponseCallback implements UseCase.Callback<RecipeMetadataResponse> {

        private static final String TAG = "tkm-" + RecipeCourseEditorViewModelTest.
                RecipeResponseCallback.class.getSimpleName() + ": ";

        private RecipeMetadataResponse response;

        @Override
        public void onSuccess(RecipeMetadataResponse response) {
            System.out.println(RecipeCourseEditorViewModelTest.TAG + TAG + "onSuccess:" + response);
            this.response = response;
        }

        @Override
        public void onError(RecipeMetadataResponse response) {
            System.out.println(RecipeCourseEditorViewModelTest.TAG + TAG + "onError:" + response);
            this.response = response;
        }

        public RecipeMetadataResponse getResponse() {
            return response;
        }

        @Nonnull
        @Override
        public String toString() {
            return "RecipeResponseCallback{" +
                    "response=" + response +
                    '}';
        }
    }

    private static class RecipeMacroResponseCallback implements UseCase.Callback<RecipeResponse> {

        RecipeResponse response;

        @Override
        public void onSuccess(RecipeResponse response) {
            this.response = response;
        }

        @Override
        public void onError(RecipeResponse response) {
            this.response = response;
        }

        public RecipeResponse getResponse() {
            return response;
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}