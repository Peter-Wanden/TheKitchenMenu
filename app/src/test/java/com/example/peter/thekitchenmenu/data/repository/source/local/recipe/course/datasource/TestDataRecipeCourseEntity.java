package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource;

import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.TestDataRecipeMetadataEntity;

import java.util.ArrayList;
import java.util.List;

public class TestDataRecipeCourseEntity {

    private static final String NEW_RECIPE_ID = null;
//            TestDataRecipeMetadataEntity.getNewInvalidParent().getDataId();

    public static final String EXISTING_RECIPE_ID = null;
//            TestDataRecipeMetadataEntity.getValidExisting().getDataId();

    public static RecipeCourseEntity getRecipeCourseZero() {
        return new RecipeCourseEntity(
                "dataId0",
                EXISTING_RECIPE_ID,
                0,
                true,
                10L,
                10L
        );
    }

    public static RecipeCourseEntity getRecipeCourseOne() {
        return new RecipeCourseEntity(
                "dataId1",
                EXISTING_RECIPE_ID,
                1,
                true,
                10L,
                10L
        );
    }

    public static RecipeCourseEntity getRecipeCourseTwo() {
        return new RecipeCourseEntity(
                "dataId2",
                EXISTING_RECIPE_ID,
                2,
                true,
                10L,
                10L
        );
    }

    public static RecipeCourseEntity getRecipeCourseThree() {
        return new RecipeCourseEntity(
                "dataId3",
                EXISTING_RECIPE_ID,
                3,
                true,
                10L,
                10L
        );
    }

    public static RecipeCourseEntity getRecipeCourseFour() {
        return new RecipeCourseEntity(
                "dataId4",
                EXISTING_RECIPE_ID,
                4,
                true,
                10L,
                10L
        );
    }

    public static RecipeCourseEntity getRecipeCourseFive() {
        return new RecipeCourseEntity(
                "dataId5",
                EXISTING_RECIPE_ID,
                5,
                true,
                10L,
                10L
        );
    }

    public static RecipeCourseEntity getRecipeCourseSix() {
        return new RecipeCourseEntity(
                "dataId6",
                EXISTING_RECIPE_ID,
                6,
                true,
                10L,
                10L
        );
    }

    public static RecipeCourseEntity getRecipeCourseSeven() {
        return new RecipeCourseEntity(
                "dataId7",
                EXISTING_RECIPE_ID,
                7,
                true,
                10L,
                10L
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
                "dataId0Clone",
                NEW_RECIPE_ID,
                getRecipeCourseZero().getCourseNo(),
                true,
                20L,
                20L
        );
    }

    public static RecipeCourseEntity getClonedRecipeCourseOne() {
        return new RecipeCourseEntity(
                "dataId1Clone",
                NEW_RECIPE_ID,
                getRecipeCourseOne().getCourseNo(),
                true,
                20L,
                20L
        );
    }

    public static RecipeCourseEntity getClonedRecipeCourseTwo() {
        return new RecipeCourseEntity(
                "dataId2Clone",
                NEW_RECIPE_ID,
                getRecipeCourseTwo().getCourseNo(),
                true,
                20L,
                20L
        );
    }

    public static RecipeCourseEntity getClonedRecipeCourseFour() {
        return new RecipeCourseEntity(
                "dataId4Clone",
                NEW_RECIPE_ID,
                getRecipeCourseFour().getCourseNo(),
                true,
                20L,
                20L
        );
    }

    public static RecipeCourseEntity getClonedRecipeCourseSix() {
        return new RecipeCourseEntity(
                "dataId6Clone",
                NEW_RECIPE_ID,
                getRecipeCourseSix().getCourseNo(),
                true,
                20L,
                20L
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
