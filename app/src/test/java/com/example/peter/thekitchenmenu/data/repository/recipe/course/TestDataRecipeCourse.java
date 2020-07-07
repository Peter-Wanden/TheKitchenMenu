package com.example.peter.thekitchenmenu.data.repository.recipe.course;

import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceDomainModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse.Course;

public class TestDataRecipeCourse {

    public static final String NEW_RECIPE_ID =
            TestDataRecipeMetadata.getInvalidDefault().getDomainId();

    public static final String EXISTING_RECIPE_ID =
            TestDataRecipeMetadata.getValidUnchanged().getDomainId();

    // region Persistence models for testing adding and removing data elements
    // Course 'DEFAULT_NO_COURSES' is added as the default state when no courses have been added.
    // This is a
    public static RecipeCoursePersistenceDomainModel getNewActiveDefaultNoCourses() {
        return new RecipeCoursePersistenceDomainModel.Builder().
                setDataId("dataId-recipeCourse-new-id0"). // new data id added
                setDomainId(NEW_RECIPE_ID).
                setCourses(new ArrayList<>()).
                setCreateDate(10L). // updated to current time
                setLastUpdate(10L). // updated to current time
                build();
    }

    // When state changed (course added or removed), the old persistence model is archived
    public static RecipeCoursePersistenceDomainModel getNewArchivedDefaultNoCourses() {
        return new RecipeCoursePersistenceDomainModel.Builder().
                basedOnModel(getNewActiveDefaultNoCourses()).
                setLastUpdate(20L). // updated to current time
                build();
    }

    // A course has been added, a new active persistence model is saved
    public static RecipeCoursePersistenceDomainModel getNewActiveCourseOne() {
        return new RecipeCoursePersistenceDomainModel.Builder().
                basedOnModel(getNewActiveDefaultNoCourses()). // only have to add changed data
                setDataId("dataId-recipeCourse-new-id1"). // new data id added
                setCourses(Collections.singletonList(Course.COURSE_ONE)). // new course added
                setCreateDate(20L). // current time added
                setLastUpdate(20L). // current time added
                build();
    }

    // When state changed, old persistence model is archived
    public static RecipeCoursePersistenceDomainModel getNewArchivedCourseOne() {
        return new RecipeCoursePersistenceDomainModel.Builder().
                basedOnModel(getNewActiveCourseOne()).
                setLastUpdate(30L). // current time added
                build();
    }

    // A second course is added, a new persistence model is saved
    public static RecipeCoursePersistenceDomainModel getNewActiveCourseOneAndTwo() {
        return new RecipeCoursePersistenceDomainModel.Builder().
                basedOnModel(getNewActiveCourseOne()).
                setDataId("dataId-recipeCourse-new-id2"). // new data id added
                setCourses(Arrays.asList(Course.COURSE_ONE, Course.COURSE_TWO)). // 2nd course added
                setCreateDate(30L). // current time added
                setLastUpdate(30L). // current time added
                build();
    }

    // When state changed, old persistence model is archived
    public static RecipeCoursePersistenceDomainModel getNewArchivedCourseOneAndTwo() {
        return new RecipeCoursePersistenceDomainModel.Builder().
                basedOnModel(getNewActiveCourseOneAndTwo()).
                setLastUpdate(40L). // current time added
                build();
    }

    // The first course is now removed, new active persistence model saved
    public static RecipeCoursePersistenceDomainModel getNewActiveAfterCourseOneRemoved() {
        return new RecipeCoursePersistenceDomainModel.Builder().
                basedOnModel(getNewActiveCourseOneAndTwo()).
                setDataId("dataId-recipeCourse-new-id3"). // new data id added
                setCourses(Collections.singletonList(Course.COURSE_TWO)).
                setCreateDate(40L). // current time added
                setLastUpdate(40L). // current time added
                build();
    }

    // When state changed, old persistence model is archived
    public static RecipeCoursePersistenceDomainModel getNewArchivedAfterCourseOneRemoved() {
        return new RecipeCoursePersistenceDomainModel.Builder().
                basedOnModel(getNewActiveAfterCourseOneRemoved()).
                setLastUpdate(50L). // current time added
                build();
    }

    // The second course is now removed, new active persistence model saved. As all courses have
    // been removed, state reverts to default
    public static RecipeCoursePersistenceDomainModel getNewActiveCourseDefaultAfterAllCoursesRemoved() {
        return new RecipeCoursePersistenceDomainModel.Builder().
                basedOnModel(getNewActiveDefaultNoCourses()).
                setDataId("dataId-recipeCourse-new-id4"). // new data id for new state
                setCourses(new ArrayList<>()). // default added
                setCreateDate(50L). // current time added
                setLastUpdate(50L). // current time added
                build();
    }
    // endregion Persistence models for testing adding and removing data elements

    // region Persistence models for testing getting and modifying existing courses
    public static RecipeCoursePersistenceDomainModel getExistingActiveRecipeCourseZero() {
        return new RecipeCoursePersistenceDomainModel.Builder().
                setDataId("dataId-recipeCourse-existing-id0").
                setDomainId(EXISTING_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_ZERO)).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceDomainModel getExistingActiveRecipeCourseOne() {
        return new RecipeCoursePersistenceDomainModel.Builder().
                setDataId("dataId-recipeCourse-existing-id1").
                setDomainId(EXISTING_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_ONE)).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceDomainModel getExistingActiveRecipeCourseTwo() {
        return new RecipeCoursePersistenceDomainModel.Builder().
                setDataId("dataId-recipeCourse-existing-id2").
                setDomainId(EXISTING_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_TWO)).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceDomainModel getExistingActiveRecipeCourseThree() {
        return new RecipeCoursePersistenceDomainModel.Builder().
                setDataId("dataId-recipeCourse-existing-id3").
                setDomainId(EXISTING_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_THREE)).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceDomainModel getExistingActiveRecipeCourseFour() {
        return new RecipeCoursePersistenceDomainModel.Builder().
                setDataId("dataId-recipeCourse-existing-id4").
                setDomainId(EXISTING_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_FOUR)).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceDomainModel getExistingActiveRecipeCourseFive() {
        return new RecipeCoursePersistenceDomainModel.Builder().
                setDataId("dataId-recipeCourse-existing-id5").
                setDomainId(EXISTING_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_FIVE)).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceDomainModel getExistingActiveRecipeCourseSix() {
        return new RecipeCoursePersistenceDomainModel.Builder().
                setDataId("dataId-recipeCourse-existing-id6").
                setDomainId(EXISTING_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_SIX)).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceDomainModel getExistingActiveRecipeCourseSeven() {
        return new RecipeCoursePersistenceDomainModel.Builder().
                setDataId("dataId-recipeCourse-existing-id7").
                setDomainId(EXISTING_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_SEVEN)).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceDomainModel getExistingActiveWithAllCourses() {
        return new RecipeCoursePersistenceDomainModel.Builder().
                setDataId("dataId=recipeCourse-existing-id8").
                setDomainId(EXISTING_RECIPE_ID).
                setCourses(
                        Arrays.asList(
                                Course.COURSE_ZERO,
                                Course.COURSE_ONE,
                                Course.COURSE_TWO,
                                Course.COURSE_THREE,
                                Course.COURSE_FOUR,
                                Course.COURSE_FIVE,
                                Course.COURSE_SIX,
                                Course.COURSE_SEVEN,
                                Course.COURSE_EIGHT
                        )).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static RecipeCoursePersistenceDomainModel getExistingArchivedWithAllCourses() {
        return new RecipeCoursePersistenceDomainModel.Builder().
                basedOnModel(getExistingActiveWithAllCourses()).
                setCreateDate(30L). // current time added
                setLastUpdate(40L). // current time added
                build();
    }

    public static RecipeCoursePersistenceDomainModel getExistingActiveAfterAllCoursesRemoved() {
        return new RecipeCoursePersistenceDomainModel.Builder().
                basedOnModel(getExistingArchivedWithAllCourses()).
                setDataId("dataId=recipeCourse-existing-id9"). // new state new id
                setCourses(new ArrayList<>()). // all courses removed
                setCreateDate(40L). // current time added
                setLastUpdate(40L). // current time added
                build();
    }

    public static List<RecipeCoursePersistenceDomainModel> getAllExistingActiveCoursePersistentDomainModels() {
        return Arrays.asList(
                getExistingActiveRecipeCourseZero(),
                getExistingActiveRecipeCourseOne(),
                getExistingActiveRecipeCourseTwo(),
                getExistingActiveRecipeCourseThree(),
                getExistingActiveRecipeCourseFour(),
                getExistingActiveRecipeCourseFive(),
                getExistingActiveRecipeCourseSix(),
                getExistingActiveRecipeCourseSeven(),
                getExistingActiveWithAllCourses()
        );
    }

    public static List<RecipeCoursePersistenceDomainModel> getAllExistingActiveEvenPersistenceDomainModels() {
        List<RecipeCoursePersistenceDomainModel> evenModels = new ArrayList<>();
        for (RecipeCoursePersistenceDomainModel m : getAllExistingActiveCoursePersistentDomainModels()) {
            for (Course course : m.getCourses()) {
                if (course.getId() % 2 == 0) {
                    evenModels.add(m);
                }
            }
        }
        return evenModels;
    }

    public static RecipeCoursePersistenceDomainModel getActiveByDomainId(String domainId) {
        RecipeCoursePersistenceDomainModel persistenceDomainModel = null;
        long lastUpdate = 0L;
        for (RecipeCoursePersistenceDomainModel m : getAllExistingActiveCoursePersistentDomainModels()) {
            if (m.getDomainId().equals(domainId) && m.getLastUpdate() > lastUpdate) {
                lastUpdate = m.getLastUpdate();
                persistenceDomainModel = m;
            }
        }
        return persistenceDomainModel;
    }
    // endregion Persistence models for testing getting and modifying existing courses

    public static RecipeCoursePersistenceDomainModel getCopiedRecipeCourseZero() {
        return new RecipeCoursePersistenceDomainModel.Builder().
                setDataId("dataId-recipeCourse-copy-id0").
                setDomainId(NEW_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_ZERO)).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static RecipeCoursePersistenceDomainModel getCopiedRecipeCourseOne() {
        return new RecipeCoursePersistenceDomainModel.Builder().
                setDataId("dataId-recipeCourse-copy-id1").
                setDomainId(NEW_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_ONE)).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static RecipeCoursePersistenceDomainModel getCopiedRecipeCourseTwo() {
        return new RecipeCoursePersistenceDomainModel.Builder().
                setDataId("dataId-recipeCourse-copy-id2").
                setDomainId(NEW_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_TWO)).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static RecipeCoursePersistenceDomainModel getCopiedRecipeCourseFour() {
        return new RecipeCoursePersistenceDomainModel.Builder().
                setDataId("dataId-recipeCourse-copy-id4").
                setDomainId(NEW_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_FOUR)).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static RecipeCoursePersistenceDomainModel getCopiedRecipeCourseSix() {
        return new RecipeCoursePersistenceDomainModel.Builder().
                setDataId("dataId-recipeCourse-copy-id6").
                setDomainId(NEW_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_SIX)).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static List<RecipeCoursePersistenceDomainModel> getAllRecipeCourseCopies() {
        List<RecipeCoursePersistenceDomainModel> models = new ArrayList<>();
        models.add(getCopiedRecipeCourseZero());
        models.add(getCopiedRecipeCourseOne());
        models.add(getCopiedRecipeCourseTwo());
        models.add(getCopiedRecipeCourseFour());
        models.add(getCopiedRecipeCourseSix());
        return models;
    }

    public static List<RecipeCoursePersistenceDomainModel> getAllByCourse(Course course) {
        List<RecipeCoursePersistenceDomainModel> models = new ArrayList<>();
        for (RecipeCoursePersistenceDomainModel persistenceModel : getAll()) {
            for (Course c : persistenceModel.getCourses()) {
                if (course.equals(c)) {
                    models.add(persistenceModel);
                }
            }
        }
        return models;
    }

    public static List<RecipeCoursePersistenceDomainModel> getAll() {
        List<RecipeCoursePersistenceDomainModel> models = new ArrayList<>();
        models.add(getNewActiveDefaultNoCourses());
        models.add(getNewActiveCourseOne());
        models.add(getNewActiveCourseOneAndTwo());
        models.add(getNewArchivedCourseOne());
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
