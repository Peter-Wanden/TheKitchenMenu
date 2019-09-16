package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.data.entity.RecipeCourseEntity;

import java.util.ArrayList;
import java.util.List;

public class RecipeCourseEntityTestData {

    private static final String EXISTING_RECIPE_ID =
            RecipeEntityTestData.getValidExisting().getId();

    private static final String NEW_RECIPE_ID =
            RecipeEntityTestData.getInvalidNew().getId();

    public static RecipeCourseEntity getRecipeCourseZero() {
        return new RecipeCourseEntity(
                "id0",
                0,
                EXISTING_RECIPE_ID
        );
    }

    public static RecipeCourseEntity getRecipeCourseOne() {
        return new RecipeCourseEntity(
                "id1",
                1,
                EXISTING_RECIPE_ID
        );
    }

    public static RecipeCourseEntity getRecipeCourseTwo() {
        return new RecipeCourseEntity(
                "id2",
                2,
                EXISTING_RECIPE_ID
        );
    }

    public static RecipeCourseEntity getRecipeCourseThree() {
        return new RecipeCourseEntity(
                "id3",
                3,
                EXISTING_RECIPE_ID
        );
    }

    public static RecipeCourseEntity getRecipeCourseFour() {
        return new RecipeCourseEntity(
                "id4",
                4,
                EXISTING_RECIPE_ID
        );
    }

    public static RecipeCourseEntity getRecipeCourseFive() {
        return new RecipeCourseEntity(
                "id5",
                5,
                EXISTING_RECIPE_ID
        );
    }

    public static RecipeCourseEntity getRecipeCourseSix() {
        return new RecipeCourseEntity(
                "id6",
                6,
                EXISTING_RECIPE_ID
        );
    }

    public static RecipeCourseEntity getRecipeCourseSeven() {
        return new RecipeCourseEntity(
                "id7",
                7,
                EXISTING_RECIPE_ID
        );
    }

    public static List<RecipeCourseEntity> getAllRecipeCoursesDatabaseResponse() {
        List<RecipeCourseEntity> courseEntities = new ArrayList<>();
        courseEntities.add(getRecipeCourseZero());
        courseEntities.add(getRecipeCourseOne());
        courseEntities.add(getRecipeCourseTwo());
        courseEntities.add(getRecipeCourseThree());
        courseEntities.add(getRecipeCourseFour());
        courseEntities.add(getRecipeCourseFive());
        courseEntities.add(getRecipeCourseSix());
        courseEntities.add(getRecipeCourseSeven());
        return courseEntities;
    }

    public static List<RecipeCourseEntity> getEvenRecipeCoursesDatabaseResponse() {
        List<RecipeCourseEntity> courseEntities = new ArrayList<>();
        courseEntities.add(getRecipeCourseZero());
        courseEntities.add(getRecipeCourseTwo());
        courseEntities.add(getRecipeCourseFour());
        courseEntities.add(getRecipeCourseSix());
        return courseEntities;
    }

    public static RecipeCourseEntity getClonedRecipeCourseZero() {
        return new RecipeCourseEntity(
                "id0",
                getRecipeCourseZero().getCourseNo(),
                NEW_RECIPE_ID
        );
    }

    public static RecipeCourseEntity getClonedRecipeCourseOne() {
        return new RecipeCourseEntity(
                "id1",
                getRecipeCourseOne().getCourseNo(),
                NEW_RECIPE_ID
        );
    }

    public static RecipeCourseEntity getClonedRecipeCourseTwo() {
        return new RecipeCourseEntity(
                "id2",
                getRecipeCourseTwo().getCourseNo(),
                NEW_RECIPE_ID
        );
    }

    public static RecipeCourseEntity getClonedRecipeCourseFour() {
        return new RecipeCourseEntity(
                "id4",
                getRecipeCourseFour().getCourseNo(),
                NEW_RECIPE_ID
        );
    }

    public static RecipeCourseEntity getClonedRecipeCourseSix() {
        return new RecipeCourseEntity(
                "id6",
                getRecipeCourseSix().getCourseNo(),
                NEW_RECIPE_ID
        );
    }

    public static List<RecipeCourseEntity> getAllEvenRecipeCourseClones() {
        List<RecipeCourseEntity> clonedRecipeCourseEntities = new ArrayList<>();
        clonedRecipeCourseEntities.add(getClonedRecipeCourseZero());
        clonedRecipeCourseEntities.add(getClonedRecipeCourseTwo());
        clonedRecipeCourseEntities.add(getClonedRecipeCourseFour());
        clonedRecipeCourseEntities.add(getClonedRecipeCourseSix());
        return clonedRecipeCourseEntities;
    }
}
