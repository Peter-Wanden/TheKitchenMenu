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

    // first course added, this is the default course which creates this default persistence model
    public static RecipeCoursePersistenceModel getNewActiveRecipeCourseZero() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-new-id0").
                setDomainId(NEW_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_ZERO)).
                setCreateDate(10L).
                setLastUpdate(10L).
                build();
    }

    // Second course added, old persistence model is archived
    public static RecipeCoursePersistenceModel getNewArchivedAfterSecondCourseAdded() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-new-id0").
                setDomainId(NEW_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_ZERO)).
                setCreateDate(10L).
                setLastUpdate(20L). // should be updated
                build();
    }

    // Second course added, new active persistence model saved
    public static RecipeCoursePersistenceModel getNewActiveRecipeCourseZeroAndOne() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-new-id1").
                setDomainId(NEW_RECIPE_ID).
                setCourses(Arrays.asList(Course.COURSE_ZERO, Course.COURSE_ONE)). // new course added
                setCreateDate(10L).
                setLastUpdate(30L). // should be updated
                build();
    }

    // First course deactivated, old persistence model is archived
    public static RecipeCoursePersistenceModel getNewArchivedAfterCourseZeroDeactivated() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-new-id0").
                setDomainId(NEW_RECIPE_ID).
                setCourses(Arrays.asList(Course.COURSE_ZERO, Course.COURSE_ONE)).
                setCreateDate(10L).
                setLastUpdate(40L). // should be updated to current time
                build();
    }

    // First course deactivated, new active persistence model saved
    public static RecipeCoursePersistenceModel getNewActiveModelAfterCourseZeroDeactivated() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-new-id0").
                setDomainId(NEW_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_ONE)).
                setCreateDate(40L). // should be updated to current time
                setLastUpdate(40L). // should be same as create date
                build();
    }

    // Second course deactivated
    public static RecipeCoursePersistenceModel getNewArchivedRecipeCourseOne() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-new-id1").
                setDomainId(NEW_RECIPE_ID).
                setCourses(new ArrayList<>()).
                setCreateDate(10L).
                setLastUpdate(100L).
                build();
    }

    public static RecipeCoursePersistenceModel getExistingActiveRecipeCourseZero() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-existing-id0").
                setDomainId(EXISTING_RECIPE_ID).
                setCourses(Collections.singletonList(Course.COURSE_ZERO)).
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
                setCourses(Collections.singletonList(Course.COURSE_ZERO)).
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
        models.add(getNewActiveRecipeCourseZero());
        models.add(getNewActiveRecipeCourseZeroAndOne());
        models.add(getNewArchivedAfterCourseZeroDeactivated());
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
