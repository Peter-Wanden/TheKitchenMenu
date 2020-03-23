package com.example.peter.thekitchenmenu.testdata;

import com.example.peter.thekitchenmenu.data.primitivemodel.recipe.RecipeCourseEntity;

import java.util.ArrayList;
import java.util.List;

public class TestDataRecipeCourseEntity {

    public static final String EXISTING_RECIPE_ID =
            TestDataRecipeMetaDataEntity.getValidExisting().getId();

    private static final String NEW_RECIPE_ID =
            TestDataRecipeMetaDataEntity.getNewInvalid().getId();

    public static RecipeCourseEntity getRecipeCourseZero() {
        return new RecipeCourseEntity(
                "id0",
                0,
                EXISTING_RECIPE_ID,
                10,
                10
        );
    }

    public static RecipeCourseEntity getRecipeCourseOne() {
        return new RecipeCourseEntity(
                "id1",
                1,
                EXISTING_RECIPE_ID,
                10,
                10
        );
    }

    public static RecipeCourseEntity getRecipeCourseTwo() {
        return new RecipeCourseEntity(
                "id2",
                2,
                EXISTING_RECIPE_ID,
                10,
                10
        );
    }

    public static RecipeCourseEntity getRecipeCourseThree() {
        return new RecipeCourseEntity(
                "id3",
                3,
                EXISTING_RECIPE_ID,
                10,
                10
        );
    }

    public static RecipeCourseEntity getRecipeCourseFour() {
        return new RecipeCourseEntity(
                "id4",
                4,
                EXISTING_RECIPE_ID,
                10,
                10
        );
    }

    public static RecipeCourseEntity getRecipeCourseFive() {
        return new RecipeCourseEntity(
                "id5",
                5,
                EXISTING_RECIPE_ID,
                10,
                10
        );
    }

    public static RecipeCourseEntity getRecipeCourseSix() {
        return new RecipeCourseEntity(
                "id6",
                6,
                EXISTING_RECIPE_ID,
                10,
                10
        );
    }

    public static RecipeCourseEntity getRecipeCourseSeven() {
        return new RecipeCourseEntity(
                "id7",
                7,
                EXISTING_RECIPE_ID,
                10,
                10
        );
    }

    public static List<RecipeCourseEntity> getAllRecipeCourses() {
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
        List<RecipeCourseEntity> evenCourseEntities = new ArrayList<>();
        for (RecipeCourseEntity entity : getAllRecipeCourses()) {
            if (entity.getCourseNo() % 2 == 0) {
                evenCourseEntities.add(entity);
            }
        }
        return evenCourseEntities;
    }

    public static List<RecipeCourseEntity> getAllByRecipeId(String recipeId) {
        if ("idFromAnotherUser".equals(recipeId)) {
            return getAllRecipeCourseClones();
        }
        List<RecipeCourseEntity> listToReturn = new ArrayList<>();
        for (RecipeCourseEntity entity : getAllRecipeCourses()) {
            if (entity.getRecipeId().equals(recipeId)) {
                listToReturn.add(entity);
            }
        }
        return listToReturn;
    }

    public static RecipeCourseEntity getClonedRecipeCourseZero() {
        return new RecipeCourseEntity(
                "id0",
                getRecipeCourseZero().getCourseNo(),
                NEW_RECIPE_ID,
                20,
                20
        );
    }

    public static RecipeCourseEntity getClonedRecipeCourseOne() {
        return new RecipeCourseEntity(
                "id1",
                getRecipeCourseOne().getCourseNo(),
                NEW_RECIPE_ID,
                20,
                20
        );
    }

    public static RecipeCourseEntity getClonedRecipeCourseTwo() {
        return new RecipeCourseEntity(
                "id2",
                getRecipeCourseTwo().getCourseNo(),
                NEW_RECIPE_ID,
                20,
                20
        );
    }

    public static RecipeCourseEntity getClonedRecipeCourseFour() {
        return new RecipeCourseEntity(
                "id4",
                getRecipeCourseFour().getCourseNo(),
                NEW_RECIPE_ID,
                20,
                20
        );
    }

    public static RecipeCourseEntity getClonedRecipeCourseSix() {
        return new RecipeCourseEntity(
                "id6",
                getRecipeCourseSix().getCourseNo(),
                NEW_RECIPE_ID,
                20,
                20
        );
    }

    public static List<RecipeCourseEntity> getAllRecipeCourseClones() {
        List<RecipeCourseEntity> allClones = new ArrayList<>();
        allClones.add(getClonedRecipeCourseZero());
        allClones.add(getClonedRecipeCourseOne());
        allClones.add(getClonedRecipeCourseTwo());
        allClones.add(getClonedRecipeCourseFour());
        allClones.add(getClonedRecipeCourseSix());
        return allClones;
    }

    public static List<RecipeCourseEntity> getAllEvenRecipeCourseClones() {
        List<RecipeCourseEntity> clonedRecipeCourseEntities = new ArrayList<>();
        for (RecipeCourseEntity entity : getAllRecipeCourseClones()) {
            if (entity.getCourseNo() % 2 == 0) {
                clonedRecipeCourseEntities.add(entity);
            }
        }
        return clonedRecipeCourseEntities;
    }
}
