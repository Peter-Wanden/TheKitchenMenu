package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor;

import com.example.peter.thekitchenmenu.commonmocks.RecipeComponents;
import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.data.repository.recipe.course.TestDataRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.TestDataRecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.TestDataRecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.TestDataRecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.TestDataRecipePortionsEntity;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModelItem;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourseResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeResponse;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import static com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetAllDomainModelsCallback;
import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.TestDataRecipeCourseEntity.getCopiedRecipeCourseFour;
import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.TestDataRecipeCourseEntity.getCopiedRecipeCourseSix;
import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.TestDataRecipeCourseEntity.getCopiedRecipeCourseTwo;
import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.TestDataRecipeCourseEntity.getCopiedRecipeCourseZero;
import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.TestDataRecipeCourseEntity.getExistingActiveRecipeCourseFive;
import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.TestDataRecipeCourseEntity.getExistingActiveRecipeCourseFour;
import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.TestDataRecipeCourseEntity.getExistingActiveRecipeCourseOne;
import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.TestDataRecipeCourseEntity.getExistingActiveRecipeCourseSeven;
import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.TestDataRecipeCourseEntity.getExistingActiveRecipeCourseSix;
import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.TestDataRecipeCourseEntity.getExistingActiveRecipeCourseThree;
import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.TestDataRecipeCourseEntity.getExistingActiveRecipeCourseTwo;
import static com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.TestDataRecipeCourseEntity.getExistingActiveRecipeCourseZero;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RecipeCourseEditorViewModelTest {

    private static final String TAG = "tkm-" + RecipeCourseEditorViewModelTest.class.
            getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    private static final RecipeMetadataPersistenceModel RECIPE_METADATA_VALID_UNCHANGED =
            TestDataRecipeMetadata.getValidUnchanged();
    private static final String EXISTING_RECIPE_ID = RECIPE_METADATA_VALID_UNCHANGED.getDomainId();
    private static final String NEW_RECIPE_ID = TestDataRecipeMetadata.getDataUnavailable().
            getDomainId();

    private static final RecipeMetadataParentEntity RECIPE_INVALID_NEW = null;
    //            TestDataRecipeMetadataEntity.getNewInvalidParent();
    private static final RecipeMetadataParentEntity RECIPE_VALID_EXISTING = null;
    //            TestDataRecipeMetadataEntity.getValidExisting();
    private static final RecipeIdentityEntity IDENTITY_VALID_EXISTING_COMPLETE =
            TestDataRecipeIdentityEntity.getValidExistingTitleValidDescriptionValid();
    private static final RecipeDurationEntity DURATION_VALID_EXISTING_COMPLETE =
            TestDataRecipeDurationEntity.getValidExistingComplete();
    private static final RecipePortionsEntity PORTIONS_VALID_EXISTING_NINE =
            TestDataRecipePortionsEntity.getExistingValidNinePortions();
    private static final List<RecipeCourseEntity> COURSES_VALID_EXISTING_ALL =
            TestDataRecipeCourseEntity.getAllExistingActiveRecipeCourses();

    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RepositoryRecipeMetadata repoMetadataMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<RecipeMetadataPersistenceModel>> repoMetadataCallback;
    @Mock
    RepositoryRecipeIdentity repoIdentityMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<RecipeIdentityPersistenceModel>> repoIdentityCallback;
    @Mock
    RepositoryRecipeCourse repoCourseMock;
    @Captor
    ArgumentCaptor<GetAllDomainModelsCallback<RecipeCoursePersistenceModelItem>> repoCourseCallback;
    @Mock
    RepositoryRecipeDuration repoDurationMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<RecipeDurationPersistenceModel>> repoDurationCallback;
    @Mock
    RepositoryRecipePortions repoPortionsMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<RecipePortionsPersistenceModel>> repoPortionsCallback;
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

        RecipeMetadata recipeMetadata = new RecipeMetadata(
                repoMetadataMock,
                idProviderMock,
                timeProviderMock,
                RecipeComponents.requiredComponents
        );

        RecipeIdentity identity = new RecipeIdentity(
                repoIdentityMock,
                idProviderMock,
                timeProviderMock,
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
                idProviderMock,
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

        recipeMacro = new Recipe(
                handler,
                recipeMetadata,
                identity,
                course,
                duration,
                portions);

        return new RecipeCourseEditorViewModel(handler, recipeMacro);
    }

    @Test
    public void initialRequest_recipeId_databaseCalledForListOfCourses() {
        // Arrange
        String recipeId = RECIPE_VALID_EXISTING.getDataId();

        // An initial request to load the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDomainId(recipeId).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(recipeId);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(recipeId);
        verify(repoCourseMock).getAllByDomainId(eq(recipeId), repoCourseCallback.capture());
    }

    @Test
    public void start_recipeIdSupplied_observersCalled() {
        // Arrange
        String recipeId = RECIPE_VALID_EXISTING.getDataId();

        // An initial request to load the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(recipeId).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

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

        // An initial request to load the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(recipeId).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

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

    @Test
    public void courseZeroSelected_true_courseZeroAndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getExistingActiveRecipeCourseZero().getDataId());
        whenTimeProviderCalledReturnTime(getExistingActiveRecipeCourseZero().getCreateDate());

        // An initial request to load the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnDataUnavailableForId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseZero(true);

        // Assert
        verify(repoCourseMock).save(TestDataRecipeCourse.getExistingActiveRecipeCourseZero());
    }

    @Test
    public void courseZeroDeselected_false_courseZeroForRecipeIdRemovedFromDatabase() {
        // Arrange
        // An initial request to load the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        SUT.setCourseZero(false);
        verify(repoCourseMock).deleteByDataId(eq(TestDataRecipeCourse.
                getExistingActiveRecipeCourseZero().
                getDataId()));
    }

    @Test
    public void courseOneSelected_true_courseOneSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getExistingActiveRecipeCourseOne().getDataId());
        whenTimeProviderCalledReturnTime(getExistingActiveRecipeCourseFour().getCreateDate());
        // Act
        // An initial request to load the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnDataUnavailableForId(EXISTING_RECIPE_ID);

        SUT.setCourseOne(true);
        // Assert
        verify(repoCourseMock).save(TestDataRecipeCourse.getExistingActiveRecipeCourseOne());
    }

    @Test
    public void courseOneSelected_false_courseOneAndRecipeIdRemovedFromDatabase() {
        // Arrange

        // An initial request to load the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseOne(false);

        // Assert
        verify(repoCourseMock).deleteByDataId(eq(TestDataRecipeCourse.
                getExistingActiveRecipeCourseOne().
                getDataId()));
    }

    @Test
    public void courseTwoSelected_true_courseId2AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getExistingActiveRecipeCourseTwo().getDataId());
        whenTimeProviderCalledReturnTime(getExistingActiveRecipeCourseTwo().getCreateDate());

        // An initial request to load the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnDataUnavailableForId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseTwo(true);
        // Assert
        verify(repoCourseMock).save(TestDataRecipeCourse.getExistingActiveRecipeCourseTwo());
    }

    @Test
    public void courseTwoSelected_false_courseId2AndRecipeIdRemovedFromDatabase() {
        // Arrange

        // An initial request to load the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseTwo(false);
        verify(repoCourseMock).deleteByDataId(eq(TestDataRecipeCourse.
                getExistingActiveRecipeCourseTwo().
                getDataId()));
    }

    @Test
    public void courseThreeSelected_true_courseId3AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getExistingActiveRecipeCourseThree().getDataId());
        whenTimeProviderCalledReturnTime(getExistingActiveRecipeCourseThree().getCreateDate());

        // An initial request to load the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnDataUnavailableForId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseThree(true);

        // Assert
        verify(repoCourseMock).save(TestDataRecipeCourse.getExistingActiveRecipeCourseThree());
    }

    @Test
    public void courseThreeSelected_false_courseId3AndRecipeIdRemovedFromDatabase() {
        // Arrange
        // An initial request to load the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseThree(false);

        // Assert
        verify(repoCourseMock).deleteByDataId(eq(TestDataRecipeCourse.
                getExistingActiveRecipeCourseThree().
                getDataId()));
    }

    @Test
    public void courseFourSelected_true_courseId4AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getExistingActiveRecipeCourseFour().getDataId());
        whenTimeProviderCalledReturnTime(getExistingActiveRecipeCourseFour().getCreateDate());

        // An initial request to load the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnDataUnavailableForId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseFour(true);

        // Assert
        verify(repoCourseMock).save(TestDataRecipeCourse.getExistingActiveRecipeCourseFour());
    }

    @Test
    public void courseFourSelected_false_courseId4AndRecipeIdRemovedFromDatabase() {
        // Arrange
        // An initial request to load the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseFour(false);

        // Assert
        verify(repoCourseMock).deleteByDataId(eq(TestDataRecipeCourse.
                getExistingActiveRecipeCourseFour().
                getDataId()));
    }

    @Test
    public void courseFiveSelected_true_courseId5AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getExistingActiveRecipeCourseFive().getDataId());
        whenTimeProviderCalledReturnTime(getExistingActiveRecipeCourseFive().getCreateDate());

        // An initial request to load the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnDataUnavailableForId(EXISTING_RECIPE_ID);

        //Act
        SUT.setCourseFive(true);

        // Assert
        verify(repoCourseMock).save(TestDataRecipeCourse.getExistingActiveRecipeCourseFive());
    }

    @Test
    public void courseFiveSelected_false_courseId5AndRecipeIdRemovedFromDatabase() {
        // Arrange
        // An initial request to load the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseFive(false);

        // Assert
        verify(repoCourseMock).deleteByDataId(eq(TestDataRecipeCourse.
                getExistingActiveRecipeCourseFive().
                getDataId()));
    }

    @Test
    public void courseSixSelected_true_courseId6AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getExistingActiveRecipeCourseSix().getDataId());
        whenTimeProviderCalledReturnTime(getExistingActiveRecipeCourseSix().getCreateDate());
        // An initial request to load the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnDataUnavailableForId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseSix(true);
        // Assert
        verify(repoCourseMock).save(TestDataRecipeCourse.getExistingActiveRecipeCourseSix());
    }

    @Test
    public void courseSixSelected_false_courseId6AndRecipeIdRemovedFromDatabase() {
        // Arrange
        ArgumentCaptor<String> ac = ArgumentCaptor.forClass(String.class);
        // An initial request to load the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseSix(false);

        // Assert
        verify(repoCourseMock).deleteByDataId(eq(TestDataRecipeCourse.
                getExistingActiveRecipeCourseSix().
                getDataId()));
    }

    @Test
    public void courseSevenSelected_true_courseId7AndRecipeIdSavedToDatabase() {
        // Arrange
        when(idProviderMock.getUId()).thenReturn(getExistingActiveRecipeCourseSeven().getDataId());
        whenTimeProviderCalledReturnTime(getExistingActiveRecipeCourseSeven().getCreateDate());

        // An initial request to load the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnDataUnavailableForId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseSeven(true);

        // Assert
        verify(repoCourseMock).save(TestDataRecipeCourse.getExistingActiveRecipeCourseSeven());
    }

    @Test
    public void courseSevenSelected_false_courseId7AndRecipeIdRemovedFromDatabase() {
        // Arrange

        // An initial request to load the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                build();

        // Act
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        // Act
        SUT.setCourseSeven(false);

        // Assert
        verify(repoCourseMock).deleteByDataId(TestDataRecipeCourse.
                getExistingActiveRecipeCourseSeven().
                getDataId());
    }

    @Test
    public void start_recipeIdSupplied_INVALID_UNCHANGED() {
        // Arrange
        // An initial request to load the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                build();
        RecipeMacroResponseCallback macroCallback = new RecipeMacroResponseCallback();

        // Act
        recipeMacro.registerRecipeListener(macroCallback);
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnDataUnavailableForId(EXISTING_RECIPE_ID);

        RecipeCourseResponse r = (RecipeCourseResponse) macroCallback.
                getResponse().
                getDomainModel().
                getComponentResponses().
                get(RecipeMetadata.ComponentName.COURSE);

        RecipeMetadata.ComponentState expectedState = RecipeMetadata.ComponentState.INVALID_UNCHANGED;
        RecipeMetadata.ComponentState actualState = r.getMetadata().getComponentState();

        assertEquals(expectedState, actualState);

        assertTrue(r.getMetadata().
                getFailReasons().
                contains(CommonFailReason.DATA_UNAVAILABLE));
    }

    @Test
    public void start_recipeIdSupplied_VALID_UNCHANGED() {
        // Arrange

        // An initial request to load the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                build();
        RecipeMacroResponseCallback macroCallback = new RecipeMacroResponseCallback();

        // Act
        recipeMacro.registerRecipeListener(macroCallback);
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

        // Assert
        verifyAllOtherComponentReposCalledAndReturnValidExisting(EXISTING_RECIPE_ID);
        verifyRepoCourseCalledAndReturnCoursesMatchingId(EXISTING_RECIPE_ID);

        RecipeCourseResponse r = (RecipeCourseResponse) macroCallback.
                getResponse().
                getDomainModel().
                getComponentResponses().
                get(RecipeMetadata.ComponentName.COURSE);

        RecipeMetadata.ComponentState expectedState = RecipeMetadata.ComponentState.VALID_UNCHANGED;
        RecipeMetadata.ComponentState actualState = r.getMetadata().getComponentState();

        assertEquals(expectedState, actualState);
    }

    @Test
    public void allOptionsDeselected_recipeIdSupplied_INVALID_CHANGED() {
        // Arrange

        // An initial request to load the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                build();
        RecipeMacroResponseCallback macroCallback = new RecipeMacroResponseCallback();

        // Act
        recipeMacro.registerRecipeListener(macroCallback);
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

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
        RecipeCourseResponse r = (RecipeCourseResponse) macroCallback.
                getResponse().
                getDomainModel().
                getComponentResponses().
                get(RecipeMetadata.ComponentName.COURSE);

        RecipeMetadata.ComponentState expectedState = RecipeMetadata.ComponentState.INVALID_CHANGED;
        RecipeMetadata.ComponentState actualState = r.getMetadata().getComponentState();

        assertEquals(expectedState, actualState);
    }

    @Test
    public void allButOneOptionsDeselected_recipeIdSupplied_VALID_CHANGED() {
        // Arrange

        // An initial request to load the recipe
        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
                setDataId(EXISTING_RECIPE_ID).
                build();
        RecipeMacroResponseCallback macroCallback = new RecipeMacroResponseCallback();

        // Act
        recipeMacro.registerRecipeListener(macroCallback);
        handler.executeAsync(recipeMacro, request, new RecipeResponseCallback());

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
        RecipeCourseResponse r = (RecipeCourseResponse) macroCallback.
                getResponse().
                getDomainModel().
                getComponentResponses().
                get(RecipeMetadata.ComponentName.COURSE);

        RecipeMetadata.ComponentState expectedState = RecipeMetadata.ComponentState.VALID_CHANGED;
        RecipeMetadata.ComponentState actualState = r.getMetadata().getComponentState();

        assertEquals(expectedState, actualState);
    }

    // region helper methods -----------------------------------------------------------------------
    private void verifyRepoMetadataCalledAndReturnValidUnchanged(String recipeId) {
        verify(repoMetadataMock).getActiveByDomainId((eq(recipeId)), repoMetadataCallback.capture());
        repoMetadataCallback.getValue().onDomainModelLoaded(TestDataRecipeMetadata.getValidUnchanged());
    }

    private void verifyRepoCourseCalledAndReturnCoursesMatchingId(String recipeId) {
        verify(repoCourseMock).getAllByDomainId(eq(recipeId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onAllDomainModelsLoaded(new ArrayList<>(TestDataRecipeCourse.
                getAllExistingActiveByDomainId(recipeId)));
    }

    private void verifyRepoCourseCalledAndReturnEvenCoursesForId(String recipeId) {
        verify(repoCourseMock).getAllByDomainId(eq(recipeId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onAllDomainModelsLoaded(new ArrayList<>(TestDataRecipeCourse.
                getAllExistingActiveEvenRecipeCourses()));
    }

    private void verifyRepoCourseCalledAndReturnDataUnavailableForId(String recipeId) {
        verify(repoCourseMock).getAllByDomainId(eq(recipeId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onDomainModelsUnavailable();
    }

    private void verifyAllOtherComponentReposCalledAndReturnValidExisting(String recipeId) {
//        verify(repoRecipeMock).getById(eq(recipeId), repoRecipeCallback.capture());
//        repoMetadataCallback.getValue().onModelLoaded(RECIPE_VALID_EXISTING);

//        verify(repoIdentityMock).getByDataId(eq(recipeId), repoIdentityCallback.capture());
//        repoIdentityCallback.getValue().onModelLoaded(IDENTITY_VALID_EXISTING_COMPLETE);

//        verify(repoDurationMock).getByDataId(eq(recipeId), repoDurationCallback.capture());
//        repoDurationCallback.getValue().onModelLoaded(DURATION_VALID_EXISTING_COMPLETE);

//        verify(repoPortionsMock).getAllByDomainId(eq(recipeId), repoPortionsCallback.capture());
//        repoPortionsCallback.getValue().onModelLoaded(PORTIONS_VALID_EXISTING_NINE);
    }

    private void whenIdProviderReturnClonedEvenIds() {
        when(idProviderMock.getUId()).thenReturn(
                getCopiedRecipeCourseZero().getDataId(),
                getCopiedRecipeCourseZero().getDataId(),
                getCopiedRecipeCourseTwo().getDataId(),
                getCopiedRecipeCourseFour().getDataId(),
                getCopiedRecipeCourseSix().getDataId());
    }

    private void whenTimeProviderCalledReturnTime(long time) {
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(time);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class RecipeResponseCallback implements UseCaseBase.Callback<RecipeMetadataResponse> {

        private static final String TAG = "tkm-" + RecipeCourseEditorViewModelTest.
                RecipeResponseCallback.class.getSimpleName() + ": ";

        private RecipeMetadataResponse response;

        @Override
        public void onUseCaseSuccess(RecipeMetadataResponse response) {
            System.out.println(RecipeCourseEditorViewModelTest.TAG + TAG + "onSuccess:" + response);
            this.response = response;
        }

        @Override
        public void onUseCaseError(RecipeMetadataResponse response) {
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

    private static class RecipeMacroResponseCallback implements UseCaseBase.Callback<RecipeResponse> {

        RecipeResponse response;

        @Override
        public void onUseCaseSuccess(RecipeResponse response) {
            this.response = response;
        }

        @Override
        public void onUseCaseError(RecipeResponse response) {
            this.response = response;
        }

        public RecipeResponse getResponse() {
            return response;
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}