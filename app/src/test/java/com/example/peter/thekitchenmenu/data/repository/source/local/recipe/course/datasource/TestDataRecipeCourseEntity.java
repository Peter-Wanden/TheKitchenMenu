package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource;

import com.example.peter.thekitchenmenu.data.repository.recipe.course.TestDataRecipeCoursePersistenceModel;
import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;

import java.util.ArrayList;
import java.util.List;

public class TestDataRecipeCourseEntity {
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
        List<RecipeCourseEntity> es = new ArrayList<>();
        for (RecipeCoursePersistenceModel m : TestDataRecipeCoursePersistenceModel.
                getAllRecipeCourses()) {
            es.add(convertToDatabaseEntity(m));
        }
        return es;
    }

    public static List<RecipeCourseEntity> getAllEvenRecipeCourses() {
        List<RecipeCourseEntity> es = new ArrayList<>();
        for (RecipeCoursePersistenceModel m : TestDataRecipeCoursePersistenceModel.
                getAllEvenRecipeCourses()) {
            es.add(convertToDatabaseEntity(m));
        }
        return es;
    }

    public static List<RecipeCourseEntity> getAllByDomainId(String recipeId) {
        if ("idFromAnotherUser".equals(recipeId)) {
            return getAllRecipeCourseCopies();
        }
        List<RecipeCourseEntity> es = new ArrayList<>();
        for (RecipeCourseEntity e : getAllRecipeCourses()) {
            if (e.getRecipeId().equals(recipeId)) {
                es.add(e);
            }
        }
        return es;
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
        List<RecipeCourseEntity> es = new ArrayList<>();
        for (RecipeCoursePersistenceModel m : TestDataRecipeCoursePersistenceModel.
                getAllRecipeCourseCopies()) {
            es.add(convertToDatabaseEntity(m));
        }
        return es;
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
