package com.example.peter.thekitchenmenu.data.repository.recipe.course;

import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;

import java.util.ArrayList;
import java.util.List;

public class TestDataRecipeCoursePersistenceModel {

    public static final String NEW_RECIPE_ID =
            TestDataRecipeMetadata.getDataUnavailable().getDomainId();

    public static final String EXISTING_RECIPE_ID =
            TestDataRecipeMetadata.getValidUnchanged().getDomainId();

    public static RecipeCoursePersistenceModel getRecipeCourseZero() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-id0").
                setDomainId(EXISTING_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_ZERO).
                setIsActive(true).
                setCreateDate(10L).
                setLastUpdate(10L).
                build();
    }

    public static RecipeCoursePersistenceModel getRecipeCourseOne() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-id1").
                setDomainId(EXISTING_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_ONE).
                setIsActive(true).
                setCreateDate(10L).
                setLastUpdate(10L).
                build();
    }

    public static RecipeCoursePersistenceModel getRecipeCourseTwo() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-id2").
                setDomainId(EXISTING_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_TWO).
                setIsActive(true).
                setCreateDate(10L).
                setLastUpdate(10L).
                build();
    }

    public static RecipeCoursePersistenceModel getRecipeCourseThree() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-id3").
                setDomainId(EXISTING_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_THREE).
                setIsActive(true).
                setCreateDate(10L).
                setLastUpdate(10L).
                build();
    }

    public static RecipeCoursePersistenceModel getRecipeCourseFour() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-id4").
                setDomainId(EXISTING_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_FOUR).
                setIsActive(true).
                setCreateDate(10L).
                setLastUpdate(10L).
                build();
    }

    public static RecipeCoursePersistenceModel getRecipeCourseFive() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-id5").
                setDomainId(EXISTING_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_FIVE).
                setIsActive(true).
                setCreateDate(10L).
                setLastUpdate(10L).
                build();
    }

    public static RecipeCoursePersistenceModel getRecipeCourseSix() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-id6").
                setDomainId(EXISTING_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_SIX).
                setIsActive(true).
                setCreateDate(10L).
                setLastUpdate(10L).
                build();
    }

    public static RecipeCoursePersistenceModel getRecipeCourseSeven() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourse-id7").
                setDomainId(EXISTING_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_SEVEN).
                setIsActive(true).
                setCreateDate(10L).
                setLastUpdate(10L).
                build();
    }

    public static List<RecipeCoursePersistenceModel> getAllRecipeCourses() {
        List<RecipeCoursePersistenceModel> m = new ArrayList<>();
        m.add(getRecipeCourseZero());
        m.add(getRecipeCourseOne());
        m.add(getRecipeCourseTwo());
        m.add(getRecipeCourseThree());
        m.add(getRecipeCourseFour());
        m.add(getRecipeCourseFive());
        m.add(getRecipeCourseSix());
        m.add(getRecipeCourseSeven());
        return m;
    }

    public static List<RecipeCoursePersistenceModel> getAllEvenRecipeCourses() {
        List<RecipeCoursePersistenceModel> evenModels = new ArrayList<>();
        for (RecipeCoursePersistenceModel model : evenModels) {
            if (model.getCourse().getCourseNo() % 2 == 0) {
                evenModels.add(model);
            }
        }
        return evenModels;
    }

    // TODO
//    public static List<RecipeCoursePersistenceModel> getAllByDomainId(String domainId) {
//        if (domainId.equals())
//    }

    public static RecipeCoursePersistenceModel getCopiedRecipeCourseZero() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourseCopy-id0").
                setDomainId(NEW_RECIPE_ID).
                setCourse(getRecipeCourseZero().getCourse()).
                setIsActive(true).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceModel getCopiedRecipeCourseOne() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourseCopy-id1").
                setDomainId(NEW_RECIPE_ID).
                setCourse(getRecipeCourseOne().getCourse()).
                setIsActive(true).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceModel getCopiedRecipeCourseTwo() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourseCopy-id2").
                setDomainId(NEW_RECIPE_ID).
                setCourse(getRecipeCourseOne().getCourse()).
                setIsActive(true).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceModel getCopiedRecipeCourseFour() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourseCopy-id4").
                setDomainId(NEW_RECIPE_ID).
                setCourse(getRecipeCourseFour().getCourse()).
                setIsActive(true).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceModel getCopiedRecipeCourseSix() {
        return new RecipeCoursePersistenceModel.Builder().
                setDataId("dataId-recipeCourseCopy-id6").
                setDomainId(NEW_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_SIX).
                setIsActive(true).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static List<RecipeCoursePersistenceModel> getAllRecipeCourseCopies() {
        List<RecipeCoursePersistenceModel> copies = new ArrayList<>();
        copies.add(getCopiedRecipeCourseZero());
        copies.add(getCopiedRecipeCourseOne());
        copies.add(getCopiedRecipeCourseTwo());
        copies.add(getCopiedRecipeCourseFour());
        copies.add(getCopiedRecipeCourseSix());
        return copies;
    }
}
