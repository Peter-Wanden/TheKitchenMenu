package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource;

import com.example.peter.thekitchenmenu.data.repository.recipe.course.TestDataRecipeCoursePersistenceModel;
import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;

import java.util.ArrayList;
import java.util.List;

public class TestDataRecipeCourseEntity {

    private static final String NEW_RECIPE_ID =
            TestDataRecipeMetadata.getDataUnavailable().getDomainId();

    public static final String EXISTING_RECIPE_ID =
            TestDataRecipeMetadata.getValidUnchanged().getDomainId();

    public static RecipeCourseEntity getRecipeCourseZero() {
        return convertToDatabaseEntity(TestDataRecipeCoursePersistenceModel.getRecipeCourseZero());
    }

    public static RecipeCourseEntity getRecipeCourseOne() {
        return convertToDatabaseEntity(TestDataRecipeCoursePersistenceModel.getRecipeCourseOne());
    }

    public static RecipeCourseEntity getRecipeCourseTwo() {
        return convertToDatabaseEntity(TestDataRecipeCoursePersistenceModel.getRecipeCourseTwo());
    }

    public static RecipeCourseEntity getRecipeCourseThree() {
        return convertToDatabaseEntity(TestDataRecipeCoursePersistenceModel.getRecipeCourseThree());
    }

    public static RecipeCourseEntity getRecipeCourseFour() {
        return convertToDatabaseEntity(TestDataRecipeCoursePersistenceModel.getRecipeCourseFour());
    }

    public static RecipeCourseEntity getRecipeCourseFive() {
        return convertToDatabaseEntity(TestDataRecipeCoursePersistenceModel.getRecipeCourseFive());
    }

    public static RecipeCourseEntity getRecipeCourseSix() {
        return convertToDatabaseEntity(TestDataRecipeCoursePersistenceModel.getRecipeCourseSix());
    }

    public static RecipeCourseEntity getRecipeCourseSeven() {
        return convertToDatabaseEntity(TestDataRecipeCoursePersistenceModel.getRecipeCourseSeven());
    }

    public static List<RecipeCourseEntity> getAllRecipeCourses() {
        List<RecipeCourseEntity> entities = new ArrayList<>();
        for (RecipeCoursePersistenceModel m : TestDataRecipeCoursePersistenceModel.
                getAllRecipeCourses()) {
            entities.add(convertToDatabaseEntity(m));
        }
        return entities;
    }

    public static List<RecipeCourseEntity> getAllEvenRecipeCourses() {
        List<RecipeCourseEntity> entities = new ArrayList<>();
        for (RecipeCoursePersistenceModel m : TestDataRecipeCoursePersistenceModel.
                getAllEvenRecipeCourses()) {
            entities.add(convertToDatabaseEntity(m));
        }
        return entities;
    }

    public static List<RecipeCourseEntity> getAllByDomainId(String recipeId) {
        if ("idFromAnotherUser".equals(recipeId)) {
            return getAllRecipeCourseCopies();
        }
        List<RecipeCourseEntity> listToReturn = new ArrayList<>();
        for (RecipeCourseEntity entity : getAllRecipeCourses()) {
            if (entity.getRecipeId().equals(recipeId)) {
                listToReturn.add(entity);
            }
        }
        return listToReturn;
    }

    public static RecipeCourseEntity getCopiedRecipeCourseZero() {
        return convertToDatabaseEntity(TestDataRecipeCoursePersistenceModel.
                getCopiedRecipeCourseZero());
    }

    public static RecipeCourseEntity getCopiedRecipeCourseOne() {
        return convertToDatabaseEntity(TestDataRecipeCoursePersistenceModel.
                getCopiedRecipeCourseOne());
    }

    public static RecipeCourseEntity getCopiedRecipeCourseTwo() {
        return convertToDatabaseEntity(TestDataRecipeCoursePersistenceModel.
                getCopiedRecipeCourseTwo());
    }

    public static RecipeCourseEntity getCopiedRecipeCourseFour() {
        return convertToDatabaseEntity(TestDataRecipeCoursePersistenceModel.
                getCopiedRecipeCourseFour());
    }

    public static RecipeCourseEntity getCopiedRecipeCourseSix() {
        return convertToDatabaseEntity(TestDataRecipeCoursePersistenceModel.
                getCopiedRecipeCourseSix());
    }

    public static List<RecipeCourseEntity> getAllRecipeCourseCopies() {
        List<RecipeCourseEntity> copies = new ArrayList<>();
        for (RecipeCoursePersistenceModel m : TestDataRecipeCoursePersistenceModel.
                getAllRecipeCourseCopies()) {
            copies.add(convertToDatabaseEntity(m));
        }
        return copies;
    }

    private static RecipeCourseEntity convertToDatabaseEntity(RecipeCoursePersistenceModel m) {
        return new RecipeCourseEntity(
                m.getDataId(),
                m.getDomainId(),
                m.getCourse().getCourseNo(),
                m.isActive(),
                m.getCreateDate(),
                m.getLastUpdate()
        );
    }
}
