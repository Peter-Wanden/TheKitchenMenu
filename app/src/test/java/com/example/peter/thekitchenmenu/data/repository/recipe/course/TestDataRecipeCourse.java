package com.example.peter.thekitchenmenu.data.repository.recipe.course;

import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestDataRecipeCourse {

    public static final String NEW_RECIPE_ID =
            TestDataRecipeMetadata.getDataUnavailable().getDomainId();

    public static final String EXISTING_RECIPE_ID =
            TestDataRecipeMetadata.getValidUnchanged().getDomainId();

    // first course added
    public static RecipeCoursePersistenceModel getNewActiveRecipeCourseZero() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-new-id0").
                setDomainId(NEW_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_ZERO).
                setIsActive(true).
                setCreateDate(10L).
                setLastUpdate(10L).
                build();
    }

    // Second course added
    public static RecipeCoursePersistenceModel getNewActiveRecipeCourseOne() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-new-id1").
                setDomainId(NEW_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_ONE).
                setIsActive(true).
                setCreateDate(10L).
                setLastUpdate(10L).
                build();
    }

    // State of recipe course data after new courses added
    public static List<RecipeCoursePersistenceModel> getNewActiveCourses() {
        return Arrays.asList(getNewActiveRecipeCourseZero(), getNewActiveRecipeCourseOne());
    }

    // First course updated to deactivated
    public static RecipeCoursePersistenceModel getNewDeactivatedRecipeCourseZero() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-new-id0").
                setDomainId(NEW_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_ZERO).
                setIsActive(false).
                setCreateDate(10L).
                setLastUpdate(100L).
                build();
    }

    // Second course updated to deactivated
    public static RecipeCoursePersistenceModel getNewDeactivatedRecipeCourseOne() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-new-id1").
                setDomainId(NEW_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_ONE).
                setIsActive(false).
                setCreateDate(10L).
                setLastUpdate(100L).
                build();
    }

    // State of data after courses removed
    public static List<RecipeCoursePersistenceModel> getNewDeactivatedRecipeCourses() {
        return new ArrayList<>(Arrays.asList(
                getNewDeactivatedRecipeCourseZero(),
                getNewDeactivatedRecipeCourseOne())
        );
    }

    public static RecipeCoursePersistenceModel getExistingActiveRecipeCourseZero() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-existing-id0").
                setDomainId(EXISTING_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_ZERO).
                setIsActive(true).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceModel getExistingActiveRecipeCourseOne() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-existing-id1").
                setDomainId(EXISTING_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_ONE).
                setIsActive(true).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceModel getExistingActiveRecipeCourseTwo() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-existing-id2").
                setDomainId(EXISTING_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_TWO).
                setIsActive(true).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceModel getExistingActiveRecipeCourseThree() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-existing-id3").
                setDomainId(EXISTING_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_THREE).
                setIsActive(true).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceModel getExistingActiveRecipeCourseFour() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-existing-id4").
                setDomainId(EXISTING_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_FOUR).
                setIsActive(true).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceModel getExistingActiveRecipeCourseFive() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-existing-id5").
                setDomainId(EXISTING_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_FIVE).
                setIsActive(true).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceModel getExistingActiveRecipeCourseSix() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-existing-id6").
                setDomainId(EXISTING_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_SIX).
                setIsActive(true).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceModel getExistingActiveRecipeCourseSeven() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-existing-id7").
                setDomainId(EXISTING_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_SEVEN).
                setIsActive(true).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static List<RecipeCoursePersistenceModel> getAllExistingActiveRecipeCourses() {
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

    public static List<RecipeCoursePersistenceModel> getAllExistingActiveEvenRecipeCourses() {
        List<RecipeCoursePersistenceModel> evenModels = new ArrayList<>();
        for (RecipeCoursePersistenceModel model : evenModels) {
            if (model.getCourse().getCourseNo() % 2 == 0) {
                evenModels.add(model);
            }
        }
        return evenModels;
    }

    //TODO - "idFromAnotherUser" ??
    public static List<RecipeCoursePersistenceModel> getAllExistingActiveByDomainId(String domainId) {
        if (domainId.equals("idFromAnotherUser")) {
            return getAllRecipeCourseCopies();
        } else {
            List<RecipeCoursePersistenceModel> models = new ArrayList<>();
            for (RecipeCoursePersistenceModel m : getAllExistingActiveRecipeCourses()) {
                if (domainId.equals(m.getDomainId())) {
                    models.add(m);
                }
            }
            return models;
        }
    }

    public static RecipeCoursePersistenceModel getCopiedRecipeCourseZero() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-copy-id0").
                setDomainId(NEW_RECIPE_ID).
                setCourse(getExistingActiveRecipeCourseZero().getCourse()).
                setIsActive(true).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static RecipeCoursePersistenceModel getCopiedRecipeCourseOne() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-copy-id1").
                setDomainId(NEW_RECIPE_ID).
                setCourse(getExistingActiveRecipeCourseOne().getCourse()).
                setIsActive(true).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static RecipeCoursePersistenceModel getCopiedRecipeCourseTwo() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-copy-id2").
                setDomainId(NEW_RECIPE_ID).
                setCourse(getExistingActiveRecipeCourseOne().getCourse()).
                setIsActive(true).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static RecipeCoursePersistenceModel getCopiedRecipeCourseFour() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-copy-id4").
                setDomainId(NEW_RECIPE_ID).
                setCourse(getExistingActiveRecipeCourseFour().getCourse()).
                setIsActive(true).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static RecipeCoursePersistenceModel getCopiedRecipeCourseSix() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-copy-id6").
                setDomainId(NEW_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_SIX).
                setIsActive(true).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static List<RecipeCoursePersistenceModel> getAllRecipeCourseCopies() {
        return Arrays.asList(
                getCopiedRecipeCourseZero(),
                getCopiedRecipeCourseOne(),
                getCopiedRecipeCourseTwo(),
                getCopiedRecipeCourseFour(),
                getCopiedRecipeCourseSix()
        );
    }
}
