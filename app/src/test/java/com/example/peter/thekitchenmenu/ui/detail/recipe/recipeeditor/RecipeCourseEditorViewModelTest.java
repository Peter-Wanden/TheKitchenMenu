package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import com.example.peter.thekitchenmenu.commonmocks.RecipeComponents;
import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.*;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeComponentState;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
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
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeduration.RecipeDurationTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeidentity.RecipeIdentityTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.recipeportions.RecipePortionsTest;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeCourseEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeDurationEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeMetadataEntity;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipeIdentityEntity;
import com.example.peter.thekitchenmenu.testdata.TestDataRecipePortionsEntity;

import org.junit.*;
import org.mockito.*;

import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.testdata.TestDataRecipeCourseEntity.*;
import static com.example.peter.thekitchenmenu.testdata.TestDataRecipeMetadataEntity.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RecipeCourseEditorViewModelTest {

    private static final String TAG = "tkm-" + RecipeCourseEditorViewModelTest.class.
            getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    private RecipeMetadataParentEntity VALID_EXISTING_RECIPE_ENTITY = getValidExisting();
    private String EXISTING_RECIPE_ID = VALID_EXISTING_RECIPE_ENTITY.getDataId();
    private String NEW_RECIPE_ID = TestDataRecipeMetadataEntity.getNewInvalid().getDataId();

    private static final RecipeMetadataParentEntity RECIPE_INVALID_NEW =
            TestDataRecipeMetadataEntity.getNewInvalid();
    private static final RecipeMetadataParentEntity RECIPE_VALID_EXISTING =
            TestDataRecipeMetadataEntity.getValidExisting();
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
    RepositoryRecipeComponentState repoRecipeMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetEntityCallback<RecipeMetadataParentEntity>> repoRecipeCallback;
    @Mock
    RepositoryRecipeIdentity repoIdentityMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetEntityCallback<RecipeIdentityEntity>> repoIdentityCallback;
    @Mock
    RepositoryRecipeCourse repoCourseMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetAllCallback<RecipeCourseEntity>> repoCourseCallback;
    @Mock
    RepositoryRecipeDuration repoDurationMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetEntityCallback<RecipeDurationEntity>> repoDurationCallback;
    @Mock
    RepositoryRecipePortions repoPortionsMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetEntityCallback<RecipePortionsEntity>> repoPortionsCallback;
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

        com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata recipeMetadata = new com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata(
                timeProviderMock,
                repoRecipeMock,
                RecipeComponents.requiredComponents
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
                recipeMetadata,
                identity,
                course,
                duration,
                portions);

        return new RecipeCourseEditorViewModel(handler, recipeMacro);
    }

    @Test
    public void start_recipeIdSupplied_databaseCalledForListOfCourses() {
        // Arrange
        String recipeId = RECIPE_VALID_EXISTING.getDataId();

        // An external request that starts loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(recipeId).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(recipeId);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(recipeId);
        verify(repoCourseMock).getAllByRecipeId(eq(recipeId), repoCourseCallback.capture());
    }

    @Test
    public void start_recipeIdSupplied_observersCalled() {
        // Arrange
        String recipeId = RECIPE_VALID_EXISTING.getDataId();

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(recipeId).
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
        String recipeId = RECIPE_VALID_EXISTING.getDataId();

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(recipeId).
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
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseZero().getDataId());
        whenTimeProviderCalledReturnTime(getRecipeCourseZero().getCreateDate());

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
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
                setDataId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        SUT.setCourseZero(false);
        verify(repoCourseMock).deleteByDataId(eq(TestDataRecipeCourseEntity.getRecipeCourseZero().getDataId()));
    }

    @Test
    public void courseOneSelected_true_courseOneAndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseOne().getDataId());
        whenTimeProviderCalledReturnTime(getRecipeCourseFour().getCreateDate());
        // Act
        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
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
                setDataId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseOne(false);

        // Assert
        verify(repoCourseMock).deleteByDataId(eq(getRecipeCourseOne().getDataId()));
    }

    @Test
    public void courseTwoSelected_true_courseId2AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseTwo().getDataId());
        whenTimeProviderCalledReturnTime(getRecipeCourseTwo().getCreateDate());

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
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
                setDataId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseTwo(false);
        verify(repoCourseMock).deleteByDataId(eq(getRecipeCourseTwo().getDataId()));
    }

    @Test
    public void courseThreeSelected_true_courseId3AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseThree().getDataId());
        whenTimeProviderCalledReturnTime(getRecipeCourseThree().getCreateDate());

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
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
                setDataId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseThree(false);

        // Assert
        verify(repoCourseMock).deleteByDataId(eq(getRecipeCourseThree().getDataId()));
    }

    @Test
    public void courseFourSelected_true_courseId4AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseFour().getDataId());
        whenTimeProviderCalledReturnTime(getRecipeCourseFour().getCreateDate());

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
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
                setDataId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseFour(false);

        // Assert
        verify(repoCourseMock).deleteByDataId(eq(getRecipeCourseFour().getDataId()));
    }

    @Test
    public void courseFiveSelected_true_courseId5AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseFive().getDataId());
        whenTimeProviderCalledReturnTime(getRecipeCourseFive().getCreateDate());

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
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
                setDataId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseFive(false);

        // Assert
        verify(repoCourseMock).deleteByDataId(eq(getRecipeCourseFive().getDataId()));
    }

    @Test
    public void courseSixSelected_true_courseId6AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseSix().getDataId());
        whenTimeProviderCalledReturnTime(getRecipeCourseSix().getCreateDate());
        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
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
                setDataId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseSix(false);

        // Assert
        verify(repoCourseMock).deleteByDataId(eq(getRecipeCourseSix().getDataId()));
    }

    @Test
    public void courseSevenSelected_true_courseId7AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getRecipeCourseSeven().getDataId());
        whenTimeProviderCalledReturnTime(getRecipeCourseSeven().getCreateDate());

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
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
                setDataId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseSeven(false);

        // Assert
        verify(repoCourseMock).deleteByDataId(getRecipeCourseSeven().getDataId());
    }

    @Test
    public void start_recipeIdSupplied_RecipeModelStatus_COURSES_MODEL_INVALID_UNCHANGED() {
        // Arrange
        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                build();
        RecipeMacroResponseCallback macroCallback = new RecipeMacroResponseCallback();

        // Act
        recipeMacro.registerRecipeCallback(macroCallback);
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnDataUnavailableForId(EXISTING_RECIPE_ID);

        com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState expectedState = com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState.INVALID_UNCHANGED;
        com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState actualState = macroCallback.
                getResponse().
                getModel().
                getRecipeStateResponse().
                getComponentStates().
                get(com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentName.COURSE);
        assertEquals(expectedState, actualState);

        assertTrue(((RecipeCourseResponse) macroCallback.
                getResponse().
                getModel().
                getComponentResponses().
                get(com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentName.COURSE)).
                getMetadata().
                getFailReasons().
                contains(CommonFailReason.DATA_UNAVAILABLE));
    }

    @Test
    public void start_recipeIdSupplied_RecipeModelStatus_COURSES_MODEL_VALID_UNCHANGED() {
        // Arrange

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                build();
        RecipeMacroResponseCallback macroCallback = new RecipeMacroResponseCallback();

        // Act
        recipeMacro.registerRecipeCallback(macroCallback);
        handler.execute(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState expectedState = com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState.VALID_UNCHANGED;
        com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState actualState = macroCallback.
                getResponse().
                getModel().
                getRecipeStateResponse().
                getComponentStates().
                get(com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentName.COURSE);

        assertEquals(expectedState, actualState);
    }

    @Test
    public void allOptionsDeselected_recipeIdSupplied_RecipeModelStatus_COURSES_MODEL_CHANGED_INVALID() {
        // Arrange

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                build();
        RecipeMacroResponseCallback macroCallback = new RecipeMacroResponseCallback();

        // Act
        recipeMacro.registerRecipeCallback(macroCallback);
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
        com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState expectedState = com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState.INVALID_CHANGED;
        com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState actualState = macroCallback.
                getResponse().
                getModel().
                getRecipeStateResponse().
                getComponentStates().
                get(com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentName.COURSE);

        assertEquals(expectedState, actualState);
    }

    @Test
    public void allButOneOptionsDeselected_recipeIdSupplied_RecipeModelStatus_COURSES_MODEL_VALID_CHANGED() {
        // Arrange

        // An external request that starts/loads the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                build();
        RecipeMacroResponseCallback macroCallback = new RecipeMacroResponseCallback();

        // Act
        recipeMacro.registerRecipeCallback(macroCallback);
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
        com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState expectedState = com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState.VALID_CHANGED;
        com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentState actualState = macroCallback.
                getResponse().
                getModel().
                getRecipeStateResponse().
                getComponentStates().
                get(com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentName.COURSE);

        assertEquals(expectedState, actualState);
    }

    // region helper methods -----------------------------------------------------------------------
    private void verifyRepoRecipeCalledAndReturnValidExistingRecipe(String recipeId) {
        verify(repoRecipeMock).getById(eq(recipeId), repoRecipeCallback.capture());
        repoRecipeCallback.getValue().onEntityLoaded(RECIPE_VALID_EXISTING);
    }

    private void verifyRepoCourseCalledAndReturnCoursesMatchingId(String recipeId) {
        verify(repoCourseMock).getAllByRecipeId(eq(recipeId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onAllLoaded(getAllByRecipeId(recipeId));
    }

    private void verifyRepoCourseCalledAndReturnEvenCoursesForId(String recipeId) {
        verify(repoCourseMock).getAllByRecipeId(eq(recipeId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onAllLoaded(getEvenRecipeCoursesDatabaseResponse());
    }

    private void verifyRepoCourseCalledAndReturnDataUnavailableForId(String recipeId) {
        verify(repoCourseMock).getAllByRecipeId(eq(recipeId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onDataUnavailable();
    }

    private void verifyAllOtherComponentReposCalledAndReturnValidExisting(String recipeId) {
        verify(repoRecipeMock).getById(eq(recipeId), repoRecipeCallback.capture());
        repoRecipeCallback.getValue().onEntityLoaded(RECIPE_VALID_EXISTING);

        verify(repoIdentityMock).getByDataId(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(IDENTITY_VALID_EXISTING_COMPLETE);

        verify(repoDurationMock).getByDataId(eq(recipeId), repoDurationCallback.capture());
        repoDurationCallback.getValue().onEntityLoaded(DURATION_VALID_EXISTING_COMPLETE);

        verify(repoPortionsMock).getByRecipeId(eq(recipeId), repoPortionsCallback.capture());
        repoPortionsCallback.getValue().onEntityLoaded(PORTIONS_VALID_EXISTING_NINE);
    }

    private void whenIdProviderReturnClonedEvenIds() {
        when(idProviderMock.getUId()).thenReturn(
                getClonedRecipeCourseZero().getDataId(),
                getClonedRecipeCourseZero().getDataId(),
                getClonedRecipeCourseTwo().getDataId(),
                getClonedRecipeCourseFour().getDataId(),
                getClonedRecipeCourseSix().getDataId());
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