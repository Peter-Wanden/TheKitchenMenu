package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource;

import com.example.peter.thekitchenmenu.data.repository.recipe.course.TestDataRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseItemEntity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModelItem;

import java.util.ArrayList;
import java.util.List;

public class TestDataRecipeCourseEntity {
    public static RecipeCourseItemEntity getExistingActiveRecipeCourseZero() {
        return convertToDatabaseEntity(TestDataRecipeCourse.getExistingActiveRecipeCourseZero()
        );
    }

    public static RecipeCourseItemEntity getExistingActiveRecipeCourseOne() {
        return convertToDatabaseEntity(TestDataRecipeCourse.getExistingActiveRecipeCourseOne());
    }

    public static RecipeCourseItemEntity getExistingActiveRecipeCourseTwo() {
        return convertToDatabaseEntity(TestDataRecipeCourse.getExistingActiveRecipeCourseTwo());
    }

    public static RecipeCourseItemEntity getExistingActiveRecipeCourseThree() {
        return convertToDatabaseEntity(TestDataRecipeCourse.getExistingActiveRecipeCourseThree());
    }

    public static RecipeCourseItemEntity getExistingActiveRecipeCourseFour() {
        return convertToDatabaseEntity(TestDataRecipeCourse.getExistingActiveRecipeCourseFour());
    }

    public static RecipeCourseItemEntity getExistingActiveRecipeCourseFive() {
        return convertToDatabaseEntity(TestDataRecipeCourse.getExistingActiveRecipeCourseFive());
    }

    public static RecipeCourseItemEntity getExistingActiveRecipeCourseSix() {
        return convertToDatabaseEntity(TestDataRecipeCourse.getExistingActiveRecipeCourseSix());
    }

    public static RecipeCourseItemEntity getExistingActiveRecipeCourseSeven() {
        return convertToDatabaseEntity(TestDataRecipeCourse.getExistingActiveRecipeCourseSeven());
    }

    public static List<RecipeCourseItemEntity> getAllExistingActiveRecipeCourses() {
        List<RecipeCourseItemEntity> es = new ArrayList<>();
        for (RecipeCoursePersistenceModelItem m : TestDataRecipeCourse.
                getAllExistingActiveRecipeCourses()) {
            es.add(convertToDatabaseEntity(m));
        }
        return es;
    }

    public static List<RecipeCourseItemEntity> getAllExistingActiveEvenRecipeCourses() {
        List<RecipeCourseItemEntity> es = new ArrayList<>();
        for (RecipeCoursePersistenceModelItem m : TestDataRecipeCourse.
                getAllExistingActiveEvenRecipeCourses()) {
            es.add(convertToDatabaseEntity(m));
        }
        return es;
    }

    public static List<RecipeCourseItemEntity> getAllExistingActiveByDomainId(String recipeId) {
        if ("idFromAnotherUser".equals(recipeId)) {
            return getAllRecipeCourseCopies();
        }
        List<RecipeCourseItemEntity> es = new ArrayList<>();
        for (RecipeCourseItemEntity e : getAllExistingActiveRecipeCourses()) {
            if (e.getDomainId().equals(recipeId)) {
                es.add(e);
            }
        }
        return es;
    }

    public static RecipeCourseItemEntity getCopiedRecipeCourseZero() {
        return convertToDatabaseEntity(TestDataRecipeCourse.
                getCopiedRecipeCourseZero());
    }

    public static RecipeCourseItemEntity getCopiedRecipeCourseOne() {
        return convertToDatabaseEntity(TestDataRecipeCourse.
                getCopiedRecipeCourseOne());
    }

    public static RecipeCourseItemEntity getCopiedRecipeCourseTwo() {
        return convertToDatabaseEntity(TestDataRecipeCourse.
                getCopiedRecipeCourseTwo());
    }

    public static RecipeCourseItemEntity getCopiedRecipeCourseFour() {
        return convertToDatabaseEntity(TestDataRecipeCourse.
                getCopiedRecipeCourseFour());
    }

    public static RecipeCourseItemEntity getCopiedRecipeCourseSix() {
        return convertToDatabaseEntity(TestDataRecipeCourse.
                getCopiedRecipeCourseSix());
    }

    public static List<RecipeCourseItemEntity> getAllRecipeCourseCopies() {
        List<RecipeCourseItemEntity> es = new ArrayList<>();
        for (RecipeCoursePersistenceModelItem m : TestDataRecipeCourse.
                getAllRecipeCourseCopies()) {
            es.add(convertToDatabaseEntity(m));
        }
        return es;
    }

    public static List<RecipeCourseItemEntity> getAllByCourseNo(int courseNo) {
        List<RecipeCourseItemEntity> entities = new ArrayList<>();
        for (RecipeCoursePersistenceModelItem m : TestDataRecipeCourse.getAll()) {
            if (courseNo == m.getCourse().getId()) {
                entities.add(convertToDatabaseEntity(m));
            }
        }
        return entities;
    }

    public static List<RecipeCourseItemEntity> getAll() {
        List<RecipeCourseItemEntity> entities = new ArrayList<>();
        for (RecipeCoursePersistenceModelItem m : TestDataRecipeCourse.getAll()) {
            entities.add(convertToDatabaseEntity(m));
        }
        return entities;
    }

    private static RecipeCourseItemEntity convertToDatabaseEntity(RecipeCoursePersistenceModelItem m) {
        return new RecipeCourseItemEntity(
                m.getDataId(),
                parentDataId, m.getDomainId(),
                m.getCourse().getId(),
                m.isActive(),
                m.getCreateDate(),
                m.getLastUpdate()
        );
    }
}
