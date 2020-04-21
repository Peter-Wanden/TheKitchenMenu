package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource;

import com.example.peter.thekitchenmenu.data.repository.recipe.course.TestDataRecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;

import java.util.ArrayList;
import java.util.List;

public class TestDataRecipeCourseEntity {
    public static RecipeCourseEntity getRecipeCourseZero() {
        return convertToDatabaseEntity(TestDataRecipeCourse.getExistingActiveRecipeCourseZero()
        );
    }

    public static RecipeCourseEntity getRecipeCourseOne() {
        return convertToDatabaseEntity(TestDataRecipeCourse.getExistingActiveRecipeCourseOne());
    }

    public static RecipeCourseEntity getRecipeCourseTwo() {
        return convertToDatabaseEntity(TestDataRecipeCourse.getExistingActiveRecipeCourseTwo());
    }

    public static RecipeCourseEntity getRecipeCourseThree() {
        return convertToDatabaseEntity(TestDataRecipeCourse.getExistingActiveRecipeCourseThree());
    }

    public static RecipeCourseEntity getRecipeCourseFour() {
        return convertToDatabaseEntity(TestDataRecipeCourse.getExistingActiveRecipeCourseFour());
    }

    public static RecipeCourseEntity getRecipeCourseFive() {
        return convertToDatabaseEntity(TestDataRecipeCourse.getExistingActiveRecipeCourseFive());
    }

    public static RecipeCourseEntity getRecipeCourseSix() {
        return convertToDatabaseEntity(TestDataRecipeCourse.getExistingActiveRecipeCourseSix());
    }

    public static RecipeCourseEntity getRecipeCourseSeven() {
        return convertToDatabaseEntity(TestDataRecipeCourse.getExistingActiveRecipeCourseSeven());
    }

    public static List<RecipeCourseEntity> getAllRecipeCourses() {
        List<RecipeCourseEntity> es = new ArrayList<>();
        for (RecipeCoursePersistenceModel m : TestDataRecipeCourse.
                getAllExistingActiveRecipeCourses()) {
            es.add(convertToDatabaseEntity(m));
        }
        return es;
    }

    public static List<RecipeCourseEntity> getAllEvenRecipeCourses() {
        List<RecipeCourseEntity> es = new ArrayList<>();
        for (RecipeCoursePersistenceModel m : TestDataRecipeCourse.
                getAllExistingActiveEvenRecipeCourses()) {
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
        return convertToDatabaseEntity(TestDataRecipeCourse.
                getCopiedRecipeCourseZero());
    }

    public static RecipeCourseEntity getCopiedRecipeCourseOne() {
        return convertToDatabaseEntity(TestDataRecipeCourse.
                getCopiedRecipeCourseOne());
    }

    public static RecipeCourseEntity getCopiedRecipeCourseTwo() {
        return convertToDatabaseEntity(TestDataRecipeCourse.
                getCopiedRecipeCourseTwo());
    }

    public static RecipeCourseEntity getCopiedRecipeCourseFour() {
        return convertToDatabaseEntity(TestDataRecipeCourse.
                getCopiedRecipeCourseFour());
    }

    public static RecipeCourseEntity getCopiedRecipeCourseSix() {
        return convertToDatabaseEntity(TestDataRecipeCourse.
                getCopiedRecipeCourseSix());
    }

    public static List<RecipeCourseEntity> getAllRecipeCourseCopies() {
        List<RecipeCourseEntity> es = new ArrayList<>();
        for (RecipeCoursePersistenceModel m : TestDataRecipeCourse.
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
