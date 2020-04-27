package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipecopy;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.RecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.RecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.RecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.RecipePortionsEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsTest;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.TestDataRecipeCourseEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.duration.datasource.TestDataRecipeDurationEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.identity.datasource.TestDataRecipeIdentityEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.portions.datasource.TestDataRecipePortionsEntity;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentName.COURSE;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentName.DURATION;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentName.IDENTITY;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentName.PORTIONS;

public class RecipeCopyTest {

    private static final String TAG = "tkm-" + RecipeCopyTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    private static final RecipeIdentityEntity IDENTITY_VALID_FROM_ANOTHER_USER =
            TestDataRecipeIdentityEntity.getValidCompleteFromAnotherUser();
    private static final RecipeIdentityEntity IDENTITY_INVALID_FROM_ANOTHER_USER =
            TestDataRecipeIdentityEntity.getInvalidFromAnotherUser();
    private static final RecipeIdentityEntity IDENTITY_VALID_NEW_CLONED =
            TestDataRecipeIdentityEntity.getValidCompleteAfterCopied();
    private static final RecipeIdentityEntity IDENTITY_INVALID_NEW_CLONED =
            TestDataRecipeIdentityEntity.getValidAfterInvalidCopy();
    private static final RecipeIdentityEntity IDENTITY_VALID_CLONED_DESCRIPTION_UPDATED =
            TestDataRecipeIdentityEntity.getValidCopiedDescriptionUpdated();
    private static final RecipeDurationEntity IDENTITY_DURATION_VALID_COMPLETE_FROM_ANOTHER_USER =
            TestDataRecipeDurationEntity.getValidCompleteFromAnotherUser();
    private static final RecipeDurationEntity IDENTITY_DURATION_VALID_NEW_CLONED =
            TestDataRecipeDurationEntity.getValidNewCopied();
    private static final RecipeDurationEntity IDENTITY_DURATION_VALID_NEW_CLONED_PREP_TIME_UPDATED =
            TestDataRecipeDurationEntity.getValidNewCopiedPrepTimeUpdated();
    private static final RecipeDurationEntity DURATION_VALID_COMPLETE_FROM_ANOTHER_USER =
            TestDataRecipeDurationEntity.getValidCompleteFromAnotherUser();
    private static final RecipeDurationEntity DURATION_INVALID_COMPLETE_FROM_ANOTHER_USER =
            TestDataRecipeDurationEntity.getInvalidCompleteFromAnotherUser();
    private static final RecipeDurationEntity DURATION_VALID_NEW_CLONED =
            TestDataRecipeDurationEntity.getValidNewCopied();
    private static final RecipeDurationEntity DURATION_INVALID_NEW_CLONED =
            TestDataRecipeDurationEntity.getInvalidNewCopied();
    private static final RecipeDurationEntity DURATION_VALID_NEW_CLONED_PREP_TIME_UPDATED =
            TestDataRecipeDurationEntity.getValidNewCopiedPrepTimeUpdated();
    private static final RecipePortionsEntity PORTION_VALID_EXISTING_CLONE =
            TestDataRecipePortionsEntity.getExistingValidCopied();
    private static final RecipePortionsEntity PORTION_VALID_EXISTING_CLONE_UPDATED_SITTINGS_SERVINGS =
            TestDataRecipePortionsEntity.getExistingCopiedUpdatedSittingsServings();
    private static final RecipePortionsEntity PORTIONS_EXISTING_VALID_CLONE =
            TestDataRecipePortionsEntity.getExistingValidCopied();
    private static final RecipePortionsEntity PORTIONS_EXISTING_VALID_CLONE_UPDATED_SITTINGS_SERVINGS =
            TestDataRecipePortionsEntity.getExistingCopiedUpdatedSittingsServings();
    private static final RecipePortionsEntity PORTIONS_EXISTING_VALID_FROM_ANOTHER_USER =
            TestDataRecipePortionsEntity.getValidCopiedFromAnotherUser();
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
//    RepositoryRecipeComponentState repoRecipeMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetPrimitiveCallback<RecipeMetadataParentEntity>> repoRecipeCallback;
    @Mock
    RepositoryRecipeIdentity repoIdentityMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetPrimitiveCallback<RecipeIdentityEntity>> repoIdentityCallback;
    @Mock
    RepositoryRecipeCourse repoCourseMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetAllPrimitiveCallback<RecipeCourseEntity>> repoCourseCallback;
    @Mock
    RepositoryRecipeDuration repoDurationMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetPrimitiveCallback<RecipeDurationEntity>> repoDurationCallback;
    @Mock
    RepositoryRecipePortions repoPortionsMock;
    @Captor
    ArgumentCaptor<PrimitiveDataSource.GetPrimitiveCallback<RecipePortionsEntity>> repoPortionsCallback;
    @Mock
    UniqueIdProvider idProviderMock;
    @Mock
    TimeProvider timeProviderMock;
    private UseCaseHandler handler;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeCopy SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();
    }

    private RecipeCopy givenUseCase() {
        handler = new UseCaseHandler(new UseCaseSchedulerMock());

//        RecipeStateCalculator stateCalculator = new RecipeStateCalculator();

        TextValidator textValidator = new TextValidator.Builder().
                setShortTextMinLength(RecipeIdentityTest.TITLE_MIN_LENGTH).
                setShortTextMaxLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                setLongTextMinLength(RecipeIdentityTest.DESCRIPTION_MIN_LENGTH).
                setLongTextMaxLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                build();

        final Set<com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.ComponentName> requiredComponents = new HashSet<>();
        requiredComponents.add(IDENTITY);
        requiredComponents.add(COURSE);
        requiredComponents.add(DURATION);
        requiredComponents.add(PORTIONS);

        // Source
//        com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata recipeMetadataSource = new com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata(
//                timeProviderMock,
//                repoRecipeMock,
//                requiredComponents
//        );
//        RecipeIdentity identitySource = new RecipeIdentity(
//                repoIdentityMock,
//                timeProviderMock,
//                handler,
//                textValidator
//        );
        RecipeCourse courseSource = new RecipeCourse(
                repoCourseMock,
                idProviderMock,
                timeProviderMock
        );
//        RecipeDuration durationSource = new RecipeDuration(
//                repoDurationMock,
//                timeProviderMock,
//                RecipeDurationTest.MAX_PREP_TIME,
//                RecipeDurationTest.MAX_COOK_TIME
//        );
        RecipePortions portionsSource = new RecipePortions(
                repoPortionsMock,
                idProviderMock,
                timeProviderMock,
                RecipePortionsTest.MAX_SERVINGS,
                RecipePortionsTest.MAX_SITTINGS
        );
//        Recipe recipeSource = new Recipe(
//                handler,
//                stateCalculator,
//                recipeMetadataSource,
//                identitySource,
//                courseSource,
//                durationSource,
//                portionsSource
//        );

        // Destination
//        com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata recipeMetadataDestination = new com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata(
//                timeProviderMock,
//                repoRecipeMock,
//                requiredComponents
//        );
//        RecipeIdentity identityDestination = new RecipeIdentity(
//                repoIdentityMock,
//                timeProviderMock,
//                handler,
//                textValidator
//        );
        RecipeCourse courseDestination = new RecipeCourse(
                repoCourseMock,
                idProviderMock,
                timeProviderMock
        );
//        RecipeDuration durationDestination = new RecipeDuration(
//                repoDurationMock,
//                timeProviderMock,
//                RecipeDurationTest.MAX_PREP_TIME,
//                RecipeDurationTest.MAX_COOK_TIME
//        );
        RecipePortions portionsDestination = new RecipePortions(
                repoPortionsMock,
                idProviderMock,
                timeProviderMock,
                RecipePortionsTest.MAX_SERVINGS,
                RecipePortionsTest.MAX_SITTINGS
        );
//        Recipe recipeDestination = new Recipe(
//                handler,
//                stateCalculator,
//                recipeMetadataDestination,
//                identityDestination,
//                courseDestination,
//                durationDestination,
//                portionsDestination
//        );

//        return new RecipeCopy(
//                handler,
//                idProviderMock,
//                recipeSource,
//                recipeDestination
//        );
        return null;
    }

    @Test
    public void cloneFromAnotherUser_validRecipe_objectsRequestedFromDataLayer() {
        // Arrange
//        String cloneFromId = TestDataRecipeMetadataEntity.getValidFromAnotherUser().getDataId();

//        RecipeCopyRequest request = new RecipeCopyRequest(cloneFromId);
        // Act
//        handler.execute(SUT, request, new RecipeCloneCallBackListener());

        // Assert
//        verifyAllReposCalledAndReturnValidFromAnotherUser(cloneFromId);
    }

    @Test
    public void cloneFromAnotherUser_validRecipe_objectsClonedAndSavedToDataLayer() {
        // Arrange
//        String cloneFromId = TestDataRecipeMetadataEntity.getValidFromAnotherUser().getDataId();
//        String copyToId = TestDataRecipeMetadataEntity.getValidNewCloned().getDataId();
//        when(idProviderMock.getUId()).thenReturn(copyToId);

        ArgumentCaptor<RecipeCourseEntity> courseEntity = ArgumentCaptor.
                forClass(RecipeCourseEntity.class);

//        RecipeCopyRequest request = new RecipeCopyRequest(cloneFromId);
        // Act
//        handler.execute(SUT, request, new RecipeCloneCallBackListener());

        // Assert data requested from data layer
//        verifyAllReposCalledAndReturnValidFromAnotherUser(cloneFromId);



        // Assert data saved
//        verify(repoCourseMock).save(courseEntity.capture());
//        System.out.println(courseEntity.getValue());

    }

    //    @Test
//    public void existingIdCloneToId_dataClonedAndSavedToCloneToId() {
//        // Arrange
//        whenTimeProviderReturnTime(VALID_NEW_COMPLETE.getCreateDate());
//        String cloneFromRecipeId = VALID_COMPLETE_FROM_ANOTHER_USER.getId();
//        String cloneToRecipeId = VALID_COMPLETE_AFTER_CLONED.getId();
//        // Act
//        handler.execute(
//                SUT,
//                getRequest(cloneFromRecipeId, cloneToRecipeId, getDefaultModel()),
//                getCallback()
//        );
//        // Assert repo called with correct id
//        verify(repoIdentityMock).getById(eq(cloneFromRecipeId), repoCallback.capture());
//        repoCallback.getValue().onEntityLoaded(VALID_COMPLETE_FROM_ANOTHER_USER);
//        // Assert data cloned and saved with copy to id
//        verify(repoIdentityMock).save(eq(VALID_COMPLETE_AFTER_CLONED));
//    }
//
//    @Test
//    public void existingIdCloneToId_descriptionUpdatedAfterClone_updatesSavedToCloneToId() {
//        // Arrange, 1st request, copy data
//        whenTimeProviderReturnTime(VALID_NEW_COMPLETE.getCreateDate());
//        String cloneFromRecipeId = VALID_COMPLETE_FROM_ANOTHER_USER.getId();
//        String cloneToRecipeId = VALID_COMPLETE_AFTER_CLONED.getId();
//        // Act
//        handler.execute(
//                SUT,
//                getRequest(cloneFromRecipeId, cloneToRecipeId, getDefaultModel()),
//                getCallback()
//        );
//        // Assert repo called with correct id
//        verify(repoIdentityMock).getById(eq(cloneFromRecipeId), repoCallback.capture());
//        repoCallback.getValue().onEntityLoaded(VALID_COMPLETE_FROM_ANOTHER_USER);
//
//        // Arrange, 2nd request, update description
//        String updatedDescription = VALID_COMPLETE_AFTER_CLONE_DESCRIPTION_UPDATED.getDescription();
//        RecipeIdentityRequest.Model modelWithUpdatedDescription = RecipeIdentityRequest.Model.Builder.
//                basedOnIdentityResponseModel(onSuccessResponse.getModel()).
//                setDescription(updatedDescription).
//                build();
//
//        // Act
//        handler.execute(
//                SUT,
//                getRequest(cloneToRecipeId, DO_NOT_CLONE, modelWithUpdatedDescription),
//                getCallback()
//        );
//        // Assert
//        verify(repoIdentityMock).save(eq(VALID_COMPLETE_AFTER_CLONE_DESCRIPTION_UPDATED));
//    }
//
//    @Test
//    public void existingIdCloneToId_titleTooLongDescriptionTooLongInClone_failReasonTITLE_TOO_LONG_DESCRIPTION_TOO_LONG() {
//        // Arrange
//        // Arrange, 1st request, copy data
//        whenTimeProviderReturnTime(VALID_NEW_COMPLETE.getCreateDate());
//        String cloneFromRecipeId = INVALID_FROM_ANOTHER_USER_TITLE_TOO_LONG_DESCRIPTION_TOO_LONG.getId();
//        String cloneToRecipeId = VALID_AFTER_INVALID_FROM_ANOTHER_USER.getId();
//        // Act
//        handler.execute(
//                SUT,
//                getRequest(cloneFromRecipeId, cloneToRecipeId, getDefaultModel()),
//                getCallback()
//        );
//        // Assert
//        verify(repoIdentityMock).getById(eq(cloneFromRecipeId), repoCallback.capture());
//        repoCallback.getValue().onEntityLoaded(INVALID_FROM_ANOTHER_USER_TITLE_TOO_LONG_DESCRIPTION_TOO_LONG);
//
//    }
    // @Test
    ////    public void recipeRequest_clone_allComponentsCloned() {
    ////        // Arrange
    ////        RecipeEntity cloneFromRecipe = TestDataRecipeEntity.getValidFromAnotherUser();
    ////        RecipeEntity cloneToRecipe = TestDataRecipeEntity.getValidNewCloned();
    ////
    ////        RecipeIdentityEntity cloneFromIdentity = TestDataRecipeIdentityEntity.
    ////                getValidCompleteFromAnotherUser();
    ////        RecipeIdentityEntity cloneToIdentity = TestDataRecipeIdentityEntity.
    ////                getValidCompleteAfterCloned();
    ////
    ////        List<RecipeCourseEntity> cloneFromCourses = TestDataRecipeCourseEntity.
    ////                getAllByRecipeId(cloneFromRecipe.getId());
    ////
    ////        RecipeDurationEntity cloneFromDuration = TestDataRecipeDurationEntity.
    ////                getValidCompleteFromAnotherUser();
    ////        RecipeDurationEntity cloneToDuration = TestDataRecipeDurationEntity.getValidNewCloned();
    ////
    ////        RecipePortionsEntity cloneFromPortions = TestDataRecipePortionsEntity.
    ////                getExistingValidNinePortions();
    ////        RecipePortionsEntity cloneToPortions = TestDataRecipePortionsEntity.
    ////                getExistingValidClone();
    ////        when(idProviderMock.getUId()).thenReturn(cloneToPortions.getId());
    ////
    ////        when(timeProviderMock.getCurrentTimeInMills()).
    ////                thenReturn(cloneToRecipe.getCreateDate());
    ////
    ////        RecipeRequest cloneRequest = new RecipeRequest.Builder().
    ////                setId(cloneFromRecipe.getId()).
    ////                setCloneToId(cloneToRecipe.getId()).
    ////                build();
    ////        RecipeCallbackClient recipeCallback = new RecipeCallbackClient();
    ////
    ////        // Act
    ////        handler.execute(SUT, cloneRequest, recipeCallback);
    ////
    ////        // Assert
    ////        verify(repoRecipeMock).getById(eq(cloneFromRecipe.getId()),
    ////                repoRecipeCallback.capture());
    ////        repoRecipeCallback.getValue().onEntityLoaded(cloneFromRecipe);
    ////        // Assert recipe entity cloned to new ID
    ////        verify(repoRecipeMock).save(cloneToRecipe);
    ////
    ////        verify(repoIdentityMock).getById(eq(cloneFromRecipe.getId()),
    ////                repoIdentityCallback.capture());
    ////        repoIdentityCallback.getValue().onEntityLoaded(cloneFromIdentity);
    ////        // Assert identity cloned to new ID
    ////        verify(repoIdentityMock).save(cloneToIdentity);
    ////
    ////        ArgumentCaptor<RecipeCourseEntity> courseEntityCaptor = ArgumentCaptor.
    ////                forClass(RecipeCourseEntity.class);
    ////        verify(repoCourseMock).getCoursesForRecipe(eq(cloneFromRecipe.getId()),
    ////                repoCourseCallback.capture());
    ////        repoCourseCallback.getValue().onAllLoaded(cloneFromCourses);
    ////        // Assert courses cloned to new ID
    ////        verify(repoCourseMock, times(cloneFromCourses.size())).save(courseEntityCaptor.capture());
    ////
    ////        verify(repoDurationMock).getById(eq(cloneFromRecipe.getId()),
    ////                repoDurationCallback.capture());
    ////        repoDurationCallback.getValue().onEntityLoaded(cloneFromDuration);
    ////        // Assert duration cloned to new ID
    ////        verify(repoDurationMock).save(cloneToDuration);
    ////
    ////        verify(repoPortionsMock).getPortionsForRecipe(eq(cloneFromRecipe.getId()),
    ////                repoPortionsCallback.capture());
    ////        repoPortionsCallback.getValue().onEntityLoaded(cloneFromPortions);
    ////        // Assert portions cloned to new ID
    ////        verify(repoPortionsMock).save(eq(cloneToPortions));
    ////    }

//    @Test
//    public void cloneRequest_persistenceCalledWithCloneFromId() {
//        // Arrange
//        RecipeCourseRequest initialiseComponentRequest = new RecipeCourseRequest.Builder().
//                setId(EXISTING_RECIPE_ID).
//                setCourse(null).
//                setAddCourse(false).
//                build();
//
//        request = getRequest(EXISTING_RECIPE_ID, NEW_RECIPE_ID, null, false);
//        // Act
//        handler.execute(SUT, request, getCallback());
//        // Assert
//        verify(repoCourseMock).getCoursesForRecipe(eq(EXISTING_RECIPE_ID), anyObject());
//    }
//
//    @Test
//    public void cloneRequest_dataClonedToNewId_VALID_CHANGED() {
//        // Arrange
//        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(10L);
//        when(idProviderMock.getUId()).thenReturn(NEW_RECIPE_ID);
//
//        request = getRequest(EXISTING_RECIPE_ID, NEW_RECIPE_ID, null, false);
//        // Act
//        handler.execute(SUT, request, getCallback());
//        // Assert
//        verifyRepoCalledAndReturnMatchingCourses(request.getId());
//        // Confirm the correct number of entities have been cloned
//        int expectedNumberOfClonesSaved = TestDataRecipeCourseEntity.
//                getAllByRecipeId(EXISTING_RECIPE_ID).
//                size();
//        verify(repoCourseMock, times(expectedNumberOfClonesSaved)).save(entityCaptor.capture());
//        // Confirm all entities have been saved with the cloneToId
//        for (RecipeCourseEntity entity : entityCaptor.getAllValues()) {
//            assertEquals(NEW_RECIPE_ID, entity.getRecipeId());
//        }
//        assertEquals(ComponentState.VALID_CHANGED, onSuccessResponse.getState());
//    }
//
//    @Test
//    public void cloneRequest_whenDeleteCourse_courseDeletedFromCloneToId() {
//        // Arrange
//        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(10L);
//        whenIdProviderReturnMockDatabaseIds();
//
//        request = getRequest(EXISTING_RECIPE_ID, NEW_RECIPE_ID, null, false);
//        // Act
//        handler.execute(SUT, request, getCallback());
//        // Assert
//        verifyRepoCalledAndReturnMatchingCourses(request.getId());
//        // confirm target is in results
//        assertTrue(onSuccessResponse.getCourseList().containsKey(Course.COURSE_ONE));
//        // confirm target has correct recipeId
//        String expectedRecipeId = onSuccessResponse.getCourseList().
//                get(Course.COURSE_ONE).getRecipeId();
//        assertEquals(NEW_RECIPE_ID, expectedRecipeId);
//        // Arrange request to delete target
//        // Get targets database id
//        String targetsDataBaseId = onSuccessResponse.getCourseList().
//                get(Course.COURSE_ONE).getId();
//        // request delete target
//        request = getRequest(
//                NEW_RECIPE_ID,
//                Course.COURSE_ONE,
//                false);
//        // Act
//        handler.execute(SUT, request, getCallback());
//        // Assert - confirm target deleted from database and list
//        verify(repoCourseMock).deleteById(eq(targetsDataBaseId));
//        assertNull(onSuccessResponse.getCourseList().get(Course.COURSE_ONE));
//        // confirm data has changed
//        if (onSuccessResponse.getCourseList().size() > 0) {
//            assertEquals(ComponentState.VALID_CHANGED, onSuccessResponse.getState());
//        } else {
//            assertEquals(ComponentState.INVALID_CHANGED, onSuccessResponse.getState());
//        }
//    }
//    @Test
//    public void existingAndCloneToId_existingSavedWithCloneToRecipeId() {
//        // Arrange
//        String cloneFromRecipeId = VALID_EXISTING.getRecipeId();
//        String cloneToRecipeId = VALID_EXISTING_CLONE.getRecipeId();
//
//        whenIdProviderReturn(VALID_EXISTING_CLONE.getId());
//        whenTimeProviderReturn(VALID_EXISTING_CLONE.getCreateDate());
//
//        RecipePortionsRequest request = RecipePortionsRequest.Builder.getDefault().
//                setId(cloneFromRecipeId).
//                setCloneToId(cloneToRecipeId).
//                build();
//        // Act
//        handler.execute(SUT, request, getCallback());
//        simulateValidExistingReturnedFromDatabase(cloneFromRecipeId);
//        // Assert
//        verify(repoPortionsMock).save(VALID_EXISTING_CLONE);
//    }
//
//    @Test
//    public void existingAndCloneToId_recipeModelStatusVALID_UNCHANGED() {
//        // Arrange
//        String cloneFromRecipeId = VALID_EXISTING.getRecipeId();
//        String cloneToRecipeId = VALID_EXISTING_CLONE.getRecipeId();
//
//        whenIdProviderReturn(VALID_EXISTING_CLONE.getId());
//        whenTimeProviderReturn(VALID_EXISTING_CLONE.getCreateDate());
//
//        RecipePortionsRequest request = RecipePortionsRequest.Builder.getDefault().
//                setId(cloneFromRecipeId).
//                setCloneToId(cloneToRecipeId).
//                build();
//        // Act
//        handler.execute(SUT, request, getCallback());
//        simulateValidExistingReturnedFromDatabase(cloneFromRecipeId);
//        // Assert
//        assertEquals(RecipeStateCalculator.ComponentState.VALID_UNCHANGED,
//                portionsOnSuccessResponse.getState());
//    }
//
//    @Test
//    public void existingAndCloneToId_savedWithUpdatedSittingsServings() {
//        String cloneFromRecipeId = VALID_EXISTING.getRecipeId();
//        String cloneToRecipeId = VALID_EXISTING_CLONE.getRecipeId();
//
//        whenIdProviderReturn(VALID_EXISTING_CLONE.getId());
//        whenTimeProviderReturn(VALID_EXISTING_CLONE.getCreateDate());
//
//        RecipePortionsRequest request = RecipePortionsRequest.Builder.getDefault().
//                setId(cloneFromRecipeId).
//                setCloneToId(cloneToRecipeId).
//                build();
//        handler.execute(SUT, request, getCallback()); // perform copy
//        simulateValidExistingReturnedFromDatabase(cloneFromRecipeId);
//        // Act
//        RecipePortionsRequest.Model updatedServingsSittingsModel = RecipePortionsRequest.Model.Builder.
//                basedOnPortionsResponseModel(portionsOnSuccessResponse.getModel()).
//                setServings(VALID_EXISTING_CLONE_UPDATED_SITTINGS_SERVINGS.getServings()).
//                setSittings(VALID_EXISTING_CLONE_UPDATED_SITTINGS_SERVINGS.getSittings()).
//                build();
//
//        RecipePortionsRequest updatedServingsRequest = new RecipePortionsRequest.Builder().
//                setId(cloneToRecipeId).
//                setCloneToId(DO_NOT_CLONE).
//                setModel(updatedServingsSittingsModel).
//                build();
//        handler.execute(SUT, updatedServingsRequest, getCallback());
//        // Assert
//        verify(repoPortionsMock).save(eq(VALID_EXISTING_CLONE_UPDATED_SITTINGS_SERVINGS));
//    }
//@Test
//public void startWithCloned_existingAndNewId_persistenceCalledWithExistingId() {
//    // Arrange
//    String cloneFromRecipeId = VALID_FROM_ANOTHER_USER.getId();
//    String cloneToRecipeId = INVALID_NEW_EMPTY.getId();
//
//    // An external request that starts/loads the recipe
//    RecipeRequest request = new RecipeRequest.Builder().
//            setId(cloneFromRecipeId).
//            setCloneToId(cloneToRecipeId).
//            build();
//
//    // Act
//    handler.execute(recipeMacro, request, new RecipeResponseCallback());
//    verifyAllReposCalledAndReturnValidExisting(cloneFromRecipeId);
//
//    // Assert
//    verify(repoIdentityMock).getById(eq(cloneFromRecipeId), anyObject());
//}
//
//    @Test
//    public void startWithCloned_existingAndNewId_existingCopiedAndSavedWithNewId() {
//        // Arrange
//        String cloneFromRecipeId = VALID_FROM_ANOTHER_USER.getId();
//        String cloneToRecipeId = INVALID_NEW_EMPTY.getId();
//        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_CLONED.getCreateDate());
//
//        // An external request that starts/loads the recipe
//        RecipeRequest request = new RecipeRequest.Builder().
//                setId(cloneFromRecipeId).
//                setCloneToId(cloneToRecipeId).
//                build();
//
//        // Act
//        handler.execute(recipeMacro, request, new RecipeResponseCallback());
//        verifyAllReposCalledAndReturnValidExisting(cloneFromRecipeId);
//        // Assert
//        verify(repoIdentityMock).save(eq(VALID_NEW_CLONED));
//    }
//
//    @Test
//    public void startWithCloned_validExistingIdAndNewId_descriptionUpdatedCopiedAndSavedWithUpdatedDescription() {
//        // Arrange
//        String cloneFromRecipeId = VALID_FROM_ANOTHER_USER.getId();
//        String cloneToRecipeId = INVALID_NEW_EMPTY.getId();
//        whenTimeProviderReturnTime(VALID_CLONED_DESCRIPTION_UPDATED.getCreateDate());
//
//        // An external request that starts/loads the recipe
//        RecipeRequest request = new RecipeRequest.Builder().
//                setId(cloneFromRecipeId).
//                setCloneToId(cloneToRecipeId).
//                build();
//
//        // Act
//        handler.execute(recipeMacro, request, new RecipeResponseCallback());
//        verifyAllReposCalledAndReturnValidExisting(cloneFromRecipeId);
//
//        SUT.setDescription(VALID_CLONED_DESCRIPTION_UPDATED.getDescription());
//
//        // Assert
//        verify(repoIdentityMock).save(VALID_CLONED_DESCRIPTION_UPDATED);
//    }
//@Test
//public void startClone_existingAndNewId_databaseCalledWithExistingId() {
//    // Act
////    givenClonedModel();
//}

//    @Test
////    public void startClone_existingAndNewId_existingClonedAndSavedWithNewRecipeId() {
////        // Arrange
////        // Act
////        givenClonedModel();
////        // Assert
////        verify(repoMock).save(eq(VALID_NEW_CLONED));
////    }
////
////    @Test
////    public void startClone_prepTimeChanged_savedWithUpdatedPrepTime() {
////        // Arrange
////        whenTimeProviderCalledReturn(VALID_NEW_CLONED_PREP_TIME_UPDATED.getCreateDate());
////        givenClonedModel();
////        RecipeDurationRequest.Model model = RecipeDurationRequest.Model.Builder.
////                basedOnDurationResponseModel(durationOnSuccessResponse.getModel()).
////                setPrepHours(VALID_NEW_CLONED_PREP_TIME_UPDATED.getPrepTime() / 60).
////                setPrepMinutes(VALID_NEW_CLONED_PREP_TIME_UPDATED.getPrepTime() % 60).
////                build();
////        RecipeDurationRequest request = new RecipeDurationRequest.Builder().
////                setId(VALID_NEW_EMPTY.getId()).
////                setModel(model).
////                build();
////        // Act
////        handler.execute(SUT, request, getUseCaseCallback());
////        // Assert
////        verify(repoMock).save(VALID_NEW_CLONED_PREP_TIME_UPDATED);
////    }
//@Test
//public void startClone_attemptCloneFromDataUnavailable_componentStateDATA_UNAVAILABLE() {
//    // Arrange
//    String cloneFromRecipeId = VALID_COMPLETE_FROM_ANOTHER_USER.getId();
//    String cloneToRecipeId = VALID_NEW_EMPTY.getId();
//    whenTimeProviderCalledReturn(VALID_NEW_EMPTY.getCreateDate());
//
//    RecipeDurationRequest request = RecipeDurationRequest.Builder.
//            getDefault().
//            setId(cloneFromRecipeId).
//            build();
//    // Act
//    handler.execute(SUT, request, getUseCaseCallback());
//    // Assert
//    verify(repoMock).getById(eq(cloneFromRecipeId),repoCallback.capture());
//    repoCallback.getValue().onDataNotAvailable();
//    verifyNoMoreInteractions(repoMock);
//    assertEquals(RecipeStateCalculator.ComponentState.INVALID_UNCHANGED, durationOnErrorResponse.getState());
//    assertTrue(durationOnErrorResponse.getFailReasons().contains(CommonFailReason.DATA_UNAVAILABLE));
//}

//    @Test
//    public void startWithCloned_existingAndNewRecipeId_databaseCalledWithExistingId() {
//        // Arrange
//        String cloneFromId = VALID_COMPLETE_FROM_ANOTHER_USER.getId();
//        String cloneToId = VALID_NEW_EMPTY.getId();
//
//        // An external request that starts/loads the recipe
//        RecipeRequest request = new RecipeRequest.Builder().
//                setId(cloneFromId).
//                setCloneToId(cloneToId).
//                build();
//
//        // Act
//        handler.execute(recipeMacro, request, new RecipeResponseCallback());
//
//        verifyAllOtherReposCalledAndReturnValidExisting(cloneFromId);
//        verifyReposDurationCalledAndReturnValidExisting(cloneFromId);
//
//        // Assert
//        verify(repoDurationMock).getById(eq(cloneFromId), repoDurationCallback.capture());
//    }
//
//    @Test
//    public void startWithCloned_existingAndNewRecipeId_existingFromAnotherUserCopiedAndSavedWithNewId() {
//        // Arrange
//        String cloneFromId = VALID_COMPLETE_FROM_ANOTHER_USER.getId();
//        String cloneToId = VALID_NEW_EMPTY.getId();
//
//        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(VALID_NEW_CLONED.getCreateDate());
//
//        // An external request that starts/loads the recipe
//        RecipeRequest request = new RecipeRequest.Builder().
//                setId(cloneFromId).
//                setCloneToId(cloneToId).
//                build();
//
//        // Act
//        handler.execute(recipeMacro, request, new RecipeResponseCallback());
//
//        verifyAllOtherReposCalledAndReturnValidExisting(cloneFromId);
//        verifyRepoDurationCalledAndReturnValidFromAnotherUser();
//
//        // Assert
//        verify(repoDurationMock).save(eq(VALID_NEW_CLONED));
//    }
//
//    @Test
//    public void startWithCloned_prepTimeChanged_savedWithUpdatedPrepTime() {
//        // Arrange
//
//        // Arrange
//        String cloneFromId = VALID_COMPLETE_FROM_ANOTHER_USER.getId();
//        String cloneToId = VALID_NEW_EMPTY.getId();
//
//        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
//                VALID_NEW_CLONED_PREP_TIME_UPDATED.getCreateDate());
//
//        // An external request that starts/loads the recipe
//        RecipeRequest request = new RecipeRequest.Builder().
//                setId(cloneFromId).
//                setCloneToId(cloneToId).
//                build();
//
//        // Act
//        handler.execute(recipeMacro, request, new RecipeResponseCallback());
//        // Assert
//        verifyAllOtherReposCalledAndReturnValidExisting(cloneFromId);
//        verifyRepoDurationCalledAndReturnValidFromAnotherUser();
//
//        SUT.setPrepHoursInView(String.valueOf(VALID_NEW_CLONED_PREP_TIME_UPDATED.getPrepTime() / 60));
//        SUT.setPrepMinutesInView(String.valueOf(VALID_NEW_CLONED_PREP_TIME_UPDATED.getPrepTime() % 60));
//
//        // Assert
//        verify(repoDurationMock, times((2))).save(durationEntityCaptor.capture());
//        assertEquals(VALID_NEW_CLONED_PREP_TIME_UPDATED, durationEntityCaptor.getValue());
//    }

//    @Test
//    public void startByCloningModel_existingAndCloneToRecipeId_existingSavedWithNewId() {
//        // Arrange
//        String cloneFromRecipeId = EXISTING_VALID.getRecipeId();
//        String cloneToRecipeId = NEW_EMPTY.getRecipeId();
//
//        whenIdProviderReturn(EXISTING_VALID_CLONE.getId());
//        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
//                EXISTING_VALID_CLONE.getLastUpdate());
//
//        // An external request that starts/loads the recipe
//        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
//                setId(cloneFromRecipeId).
//                build();
//
//        // Act
//        handler.execute(recipeMacro, request, new RecipePortionsEditorViewModelTest.RecipeResponseCallback());
//        // Assert
//        verifyAllOtherComponentReposCalledAndReturnDataUnavailable(cloneFromRecipeId);
//        verifyRepoPortionsCalledAndReturnExistingValid(cloneFromRecipeId);
//
//        // Assert
//        verify(repoPortionsMock).save(EXISTING_VALID_CLONE);
//    }
//
//    @Test
//    public void startByCloningModel_existingAndNewRecipeId_recipeModelStatusVALID_UNCHANGED() {
//        // Arrange
//        String cloneFromRecipeId = EXISTING_VALID.getRecipeId();
//        String cloneToRecipeId = NEW_EMPTY.getRecipeId();
//
//        whenIdProviderReturn(cloneToRecipeId);
//        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
//                EXISTING_VALID_CLONE.getLastUpdate());
//
//        // An external request that starts/loads the recipe
//        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
//                setId(cloneFromRecipeId).
//                build();
//
//        // Act
//        handler.execute(recipeMacro, request, new RecipePortionsEditorViewModelTest.RecipeResponseCallback());
//
//        // Assert
//        verifyAllOtherComponentReposCalledAndReturnDataUnavailable(cloneFromRecipeId);
//        verifyRepoPortionsCalledAndReturnExistingValid(cloneFromRecipeId);
//
//        RecipeStateCalculator.ComponentState expectedState = RecipeStateCalculator.ComponentState.VALID_UNCHANGED;
//        RecipeStateCalculator.ComponentState actualState = recipeStateListener.
//                getResponse().
//                getComponentStates().
//                get(RecipeStateCalculator.ComponentName.PORTIONS);
//        assertEquals(expectedState, actualState);
//    }
//
//    @Test
//    public void startByCloningModel_existingAndNewRecipeId_cloneSavedWithUpdatedSittingsServings() {
//        // Arrange
//        String cloneFromRecipeId = EXISTING_VALID.getRecipeId();
//        String cloneToRecipeId = NEW_EMPTY.getRecipeId();
//
//        whenIdProviderReturn(EXISTING_VALID_CLONE_UPDATED_SITTINGS_SERVINGS.getId());
//        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
//                EXISTING_VALID_CLONE.getLastUpdate());
//
//        // An external request that starts/loads the recipe
//        RecipeMetadataRequest request = new RecipeMetadataRequest.Builder().
//                setId(cloneFromRecipeId).
//                build();
//
//        // Act
//        handler.execute(recipeMacro, request, new RecipePortionsEditorViewModelTest.RecipeResponseCallback());
//
//        // Assert
//        verifyAllOtherComponentReposCalledAndReturnExistingValid(cloneFromRecipeId);
//        verifyRepoPortionsCalledAndReturnExistingValid(cloneFromRecipeId);
//
//        // Act
//        SUT.setServingsInView(String.valueOf(EXISTING_VALID_UPDATED_SERVINGS.getServings()));
//        SUT.setSittingsInView(String.valueOf(EXISTING_VALID_UPDATED_SITTINGS.getSittings()));
//        // Assert
//        verify(repoPortionsMock).save(eq(EXISTING_VALID_CLONE_UPDATED_SITTINGS_SERVINGS));
//    }

    // region helper methods -----------------------------------------------------------------------
    private void verifyAllReposCalledAndReturnValidFromAnotherUser(String recipeId) {

//        verify(repoRecipeMock).getById(eq(recipeId), repoRecipeCallback.capture());
//        repoRecipeCallback.getValue().onEntityLoaded(TestDataRecipeMetadataEntity.
//                getValidFromAnotherUser());

//        verify(repoIdentityMock).getByDataId(eq(recipeId), repoIdentityCallback.capture());
        repoIdentityCallback.getValue().onEntityLoaded(TestDataRecipeIdentityEntity.
                getValidCompleteFromAnotherUser());

//        verify(repoCourseMock).getAllByDomainId(eq(recipeId), repoCourseCallback.capture());
        repoCourseCallback.getValue().onAllLoaded(TestDataRecipeCourseEntity.
                getAllExistingActiveByDomainId(recipeId));

//        verify(repoPortionsMock).getAllByDomainId(eq(recipeId), repoPortionsCallback.capture());
        repoPortionsCallback.getValue().onEntityLoaded(TestDataRecipePortionsEntity.
                getValidFromAnotherUser());

//        verify(repoDurationMock).getByDataId(eq(recipeId), repoDurationCallback.capture());
        repoDurationCallback.getValue().onEntityLoaded(TestDataRecipeDurationEntity.
                getValidCompleteFromAnotherUser());
    }

//    private void givenClonedModel() {
//        // Arrange
//        whenTimeProviderCalledReturn(VALID_NEW_CLONED.getCreateDate());
//        RecipeDurationRequest request = RecipeDurationRequest.Builder.
//                getDefault().
//                setId(VALID_COMPLETE_FROM_ANOTHER_USER.getId()).
//                build();
//        // Act
//        handler.execute(SUT, request, getUseCaseCallback());
//        verify(repoMock).getById(eq(VALID_COMPLETE_FROM_ANOTHER_USER.getId()),
//                repoCallback.capture());
//        repoCallback.getValue().onEntityLoaded(VALID_COMPLETE_FROM_ANOTHER_USER);
//    }

//    private void verifyRepoDurationCalledAndReturnValidFromAnotherUser() {
//        verify(repoDurationMock).getByDataId(
//                eq(DURATION_VALID_COMPLETE_FROM_ANOTHER_USER.getDataId()),
//                repoDurationCallback.capture());
//
//        repoDurationCallback.getValue().onEntityLoaded(
//                DURATION_VALID_COMPLETE_FROM_ANOTHER_USER);
//    }
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
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class RecipeCloneCallBackListener implements UseCase.Callback<RecipeCopyResponse> {

        private static final String TAG = "tkm-" + RecipeCloneCallBackListener.class.
                getSimpleName() + ": ";

        private RecipeCopyResponse response;

        @Override
        public void onSuccess(RecipeCopyResponse response) {
            System.out.println(RecipeCopyTest.TAG + TAG + "onSuccess:" + response);
            this.response = response;
        }

        @Override
        public void onError(RecipeCopyResponse response) {
            System.out.println(RecipeCopyTest.TAG + TAG + "onError:" + response);
            this.response = response;
        }
    }
    // endregion helper classes --------------------------------------------------------------------


}