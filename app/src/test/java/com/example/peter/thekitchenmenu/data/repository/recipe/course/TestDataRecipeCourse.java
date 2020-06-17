package com.example.peter.thekitchenmenu.data.repository.recipe.course;

import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse.Course;

public class TestDataRecipeCourse {

    public static final String NEW_RECIPE_ID =
            TestDataRecipeMetadata.getDataUnavailable().getDomainId();

    public static final String EXISTING_RECIPE_ID =
            TestDataRecipeMetadata.getValidUnchanged().getDomainId();

    // region Persistence models for testing adding and removing courses
    // Course 'DEFAULT_NO_COURSES' is added as the default state when no courses have been added
    public static RecipeCoursePersistenceModel getNewActiveDefaultNoCourses() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-new-id0"). // new data id added
                setDomainId(NEW_RECIPE_ID).
                setCourses(Collections.singletonList(Course.DEFAULT_NO_COURSES)).
                setCreateDate(10L). // updated to current time
                setLastUpdate(10L). // updated to current time
                build();
    }
    // When state changed (course added or removed), the old persistence model is archived
    public static RecipeCoursePersistenceModel getNewArchivedDefaultNoCourses() {
        return new RecipeCoursePersistenceModel.Builder().
                basedOnModel(getNewActiveDefaultNoCourses()).
                setLastUpdate(20L). // updated to current time
                build();
    }

    // A course has been added, a new active persistence model is saved
    public static RecipeCoursePersistenceModel getNewActiveRecipeCourseOne() {
        return new RecipeCoursePersistenceModel.Builder().
                basedOnModel(getNewActiveDefaultNoCourses()). // only have to add changed data
                setDataId("dataId-recipeCourse-new-id1"). // new data id added
                setCourses(Collections.singletonList(Course.COURSE_ONE)). // new course added
                setCreateDate(20L). // current time added
                setLastUpdate(20L). // current time added
                build();
    }
    // When state changed, old persistence model is archived
    public static RecipeCoursePersistenceModel getNewArchivedRecipeCourseOne() {
        return new RecipeCoursePersistenceModel.Builder().
                basedOnModel(getNewActiveRecipeCourseOne()).
                setLastUpdate(30L). // current time added
                build();
    }

    // A second course is added, a new persistence model is saved
    public static RecipeCoursePersistenceModel getNewActiveCourseOneAndTwo() {
        return new RecipeCoursePersistenceModel.Builder().
                basedOnModel(getNewActiveRecipeCourseOne()).
                setDataId("dataId-recipeCourse-new-id2"). // new data id added
                setCourses(Arrays.asList(Course.COURSE_ONE, Course.COURSE_TWO)). // 2nd course added
                setCreateDate(30L). // current time added
                setLastUpdate(30L). // current time added
                build();
    }
    // When state changed, old persistence model is archived
    public static RecipeCoursePersistenceModel getNewArchivedCourseOneAndTwo() {
        return new RecipeCoursePersistenceModel.Builder().
                basedOnModel(getNewActiveCourseOneAndTwo()).
                setLastUpdate(40L). // current time added
                build();
    }

    // The first course is now removed, new active persistence model saved
    public static RecipeCoursePersistenceModel getNewActiveAfterCourseOneRemoved() {
        return new RecipeCoursePersistenceModel.Builder().
                basedOnModel(getNewActiveCourseOneAndTwo()).
                setDataId("dataId-recipeCourse-new-id3"). // new data id added
                setCourses(Collections.singletonList(Course.COURSE_TWO)).
                setCreateDate(40L). // current time added
                setLastUpdate(40L). // current time added
                build();
    }
    // When state changed, old persistence model is archived
    public static RecipeCoursePersistenceModel getNewArchivedAfterCourseOneRemoved() {
        return new RecipeCoursePersistenceModel.Builder().
                basedOnModel(getNewActiveAfterCourseOneRemoved()).
                setLastUpdate(50L). // current time added
                build();
    }

    // The second course is now removed, new active persistence model saved. As all courses have
    // been removed, state reverts to default
    public static RecipeCoursePersistenceModel getNewActiveCourseDefaultAfterAllCoursesRemoved() {
        return new RecipeCoursePersistenceModel.Builder().
                basedOnModel(getNewActiveDefaultNoCourses()).
                setDataId("dataId-recipeCourse-new-id4"). // new data id for new state
                setCourses(Collections.singletonList(Course.DEFAULT_NO_COURSES)). // default added
                setCreateDate(50L). // current time added
                setLastUpdate(50L). // current time added
                build();
    }
    // endregion Persistence models for testing adding and removing courses

    public static RecipeCoursePersistenceModel getExistingActiveRecipeCourseZero() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-existing-id0").
                setDomainId(EXISTING_RECIPE_ID).
                setCourses(Collections.singletonList(Course.DEFAULT_NO_COURSES)).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceModel getExistingActiveRecipeCourseOne() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-existing-id1").
                setDomainId(EXISTING_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_ONE)).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceModel getExistingActiveRecipeCourseTwo() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-existing-id2").
                setDomainId(EXISTING_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_TWO)).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceModel getExistingActiveRecipeCourseThree() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-existing-id3").
                setDomainId(EXISTING_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_THREE)).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceModel getExistingActiveRecipeCourseFour() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-existing-id4").
                setDomainId(EXISTING_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_FOUR)).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceModel getExistingActiveRecipeCourseFive() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-existing-id5").
                setDomainId(EXISTING_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_FIVE)).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceModel getExistingActiveRecipeCourseSix() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-existing-id6").
                setDomainId(EXISTING_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_SIX)).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceModel getExistingActiveRecipeCourseSeven() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-existing-id7").
                setDomainId(EXISTING_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_SEVEN)).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static List<RecipeCoursePersistenceModel> getAllExistingActiveCoursePersistentDomainModels() {
        return Arrays.asList(
                getExistingActiveRecipeCourseZero(),
                getExistingActiveRecipeCourseOne(),
                getExistingActiveRecipeCourseTwo(),
                getExistingActiveRecipeCourseThree(),
                getExistingActiveRecipeCourseFour(),
                getExistingActiveRecipeCourseFive(),
                getExistingActiveRecipeCourseSix(),
                getExistingActiveRecipeCourseSeven()
        );
    }

    public static List<RecipeCoursePersistenceModel> getAllExistingActiveEvenPersistenceDomainModels() {
        List<RecipeCoursePersistenceModel> evenModels = new ArrayList<>();
        for (RecipeCoursePersistenceModel m : getAllExistingActiveCoursePersistentDomainModels()) {
            for (Course course : m.getCourses()) {
                if (course.getId() % 2 == 0) {
                    evenModels.add(m);
                }
            }
        }
        return evenModels;
    }

    public static RecipeCoursePersistenceModel getActiveByDomainId(String domainId) {
        RecipeCoursePersistenceModel persistenceDomainModel = null;
        long lastUpdate = 0L;
        for (RecipeCoursePersistenceModel m : getAllExistingActiveCoursePersistentDomainModels()) {
            if (m.getDomainId().equals(domainId) && m.getLastUpdate() > lastUpdate) {
                lastUpdate = m.getLastUpdate();
                persistenceDomainModel = m;
            }
        }
        return persistenceDomainModel;
    }

    public static RecipeCoursePersistenceModel getCopiedRecipeCourseZero() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-copy-id0").
                setDomainId(NEW_RECIPE_ID).
                setCourses(Collections.singletonList(Course.DEFAULT_NO_COURSES)).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static RecipeCoursePersistenceModel getCopiedRecipeCourseOne() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-copy-id1").
                setDomainId(NEW_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_ONE)).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static RecipeCoursePersistenceModel getCopiedRecipeCourseTwo() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-copy-id2").
                setDomainId(NEW_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_TWO)).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static RecipeCoursePersistenceModel getCopiedRecipeCourseFour() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-copy-id4").
                setDomainId(NEW_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_FOUR)).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static RecipeCoursePersistenceModel getCopiedRecipeCourseSix() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-copy-id6").
                setDomainId(NEW_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_SIX)).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static List<RecipeCoursePersistenceModel> getAllRecipeCourseCopies() {
        List<RecipeCoursePersistenceModel> models = new ArrayList<>();
        models.add(getCopiedRecipeCourseZero());
        models.add(getCopiedRecipeCourseOne());
        models.add(getCopiedRecipeCourseTwo());
        models.add(getCopiedRecipeCourseFour());
        models.add(getCopiedRecipeCourseSix());
        return models;
    }

    public static List<RecipeCoursePersistenceModel> getAllByCourse(Course course) {
        List<RecipeCoursePersistenceModel> models = new ArrayList<>();
        for (RecipeCoursePersistenceModel persistenceModel : getAll()) {
            for (Course c : persistenceModel.getCourses()) {
                if (course.equals(c)) {
                    models.add(persistenceModel);
                }
            }
        }
        return models;
    }

    public static List<RecipeCoursePersistenceModel> getAll() {
        List<RecipeCoursePersistenceModel> models = new ArrayList<>();
        models.add(getNewActiveDefaultNoCourses());
        models.add(getNewActiveRecipeCourseOne());
        models.add(getNewActiveCourseOneAndTwo());
        models.add(getNewArchivedRecipeCourseOne());
        models.add(getExistingActiveRecipeCourseZero());
        models.add(getExistingActiveRecipeCourseOne());
        models.add(getExistingActiveRecipeCourseTwo());
        models.add(getExistingActiveRecipeCourseThree());
        models.add(getExistingActiveRecipeCourseFour());
        models.add(getExistingActiveRecipeCourseFive());
        models.add(getExistingActiveRecipeCourseSix());
        models.add(getExistingActiveRecipeCourseSeven());
        models.add(getCopiedRecipeCourseZero());
        models.add(getCopiedRecipeCourseOne());
        models.add(getCopiedRecipeCourseTwo());
        models.add(getCopiedRecipeCourseFour());
        models.add(getCopiedRecipeCourseSix());
        return models;
    }
}
