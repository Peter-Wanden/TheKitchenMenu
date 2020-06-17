package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource;

import com.example.peter.thekitchenmenu.data.repository.recipe.course.TestDataRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.course.datasource.courseitem.RecipeCourseEntity;

import java.util.ArrayList;
import java.util.List;

public class TestDataRecipeCourseEntity {
//    public static RecipeCourseEntity getExistingActiveRecipeCourseZero() {
//        return convertToDatabaseEntity(TestDataRecipeCourse.getExistingActiveRecipeCourseZero()
//        );
//    }
//
//    public static RecipeCourseEntity getExistingActiveRecipeCourseOne() {
//        return convertToDatabaseEntity(TestDataRecipeCourse.getExistingActiveRecipeCourseOne());
//    }
//
//    public static RecipeCourseEntity getExistingActiveRecipeCourseTwo() {
//        return convertToDatabaseEntity(TestDataRecipeCourse.getExistingActiveRecipeCourseTwo());
//    }
//
//    public static RecipeCourseEntity getExistingActiveRecipeCourseThree() {
//        return convertToDatabaseEntity(TestDataRecipeCourse.getExistingActiveRecipeCourseThree());
//    }
//
//    public static RecipeCourseEntity getExistingActiveRecipeCourseFour() {
//        return convertToDatabaseEntity(TestDataRecipeCourse.getExistingActiveRecipeCourseFour());
//    }
//
//    public static RecipeCourseEntity getExistingActiveRecipeCourseFive() {
//        return convertToDatabaseEntity(TestDataRecipeCourse.getExistingActiveRecipeCourseFive());
//    }
//
//    public static RecipeCourseEntity getExistingActiveRecipeCourseSix() {
//        return convertToDatabaseEntity(TestDataRecipeCourse.getExistingActiveRecipeCourseSix());
//    }
//
//    public static RecipeCourseEntity getExistingActiveRecipeCourseSeven() {
//        return convertToDatabaseEntity(TestDataRecipeCourse.getExistingActiveRecipeCourseSeven());
//    }
//
//    public static List<RecipeCourseEntity> getAllExistingActiveRecipeCourses() {
//        List<RecipeCourseEntity> es = new ArrayList<>();
//        for (RecipeCoursePersistenceModelItem m : TestDataRecipeCourse.
//                getAllExistingActiveCoursePersistentDomainModels()) {
//            es.add(convertToDatabaseEntity(m));
//        }
//        return es;
//    }
//
//    public static List<RecipeCourseEntity> getAllExistingActiveEvenRecipeCourses() {
//        List<RecipeCourseEntity> es = new ArrayList<>();
//        for (RecipeCoursePersistenceModelItem m : TestDataRecipeCourse.
//                getAllExistingActiveEvenPersistenceDomainModels()) {
//            es.add(convertToDatabaseEntity(m));
//        }
//        return es;
//    }
//
//    public static List<RecipeCourseEntity> getAllExistingActiveByDomainId(String recipeId) {
//        if ("idFromAnotherUser".equals(recipeId)) {
//            return getAllRecipeCourseCopies();
//        }
//        List<RecipeCourseEntity> es = new ArrayList<>();
//        for (RecipeCourseEntity e : getAllExistingActiveRecipeCourses()) {
//            if (e.getDomainId().equals(recipeId)) {
//                es.add(e);
//            }
//        }
//        return es;
//    }
//
//    public static RecipeCourseEntity getCopiedRecipeCourseZero() {
//        return convertToDatabaseEntity(TestDataRecipeCourse.
//                getCopiedRecipeCourseZero());
//    }
//
//    public static RecipeCourseEntity getCopiedRecipeCourseOne() {
//        return convertToDatabaseEntity(TestDataRecipeCourse.
//                getCopiedRecipeCourseOne());
//    }
//
//    public static RecipeCourseEntity getCopiedRecipeCourseTwo() {
//        return convertToDatabaseEntity(TestDataRecipeCourse.
//                getCopiedRecipeCourseTwo());
//    }
//
//    public static RecipeCourseEntity getCopiedRecipeCourseFour() {
//        return convertToDatabaseEntity(TestDataRecipeCourse.
//                getCopiedRecipeCourseFour());
//    }
//
//    public static RecipeCourseEntity getCopiedRecipeCourseSix() {
//        return convertToDatabaseEntity(TestDataRecipeCourse.
//                getCopiedRecipeCourseSix());
//    }
//
//    public static List<RecipeCourseEntity> getAllRecipeCourseCopies() {
//        List<RecipeCourseEntity> es = new ArrayList<>();
//        for (RecipeCoursePersistenceModelItem m : TestDataRecipeCourse.
//                getAllRecipeCourseCopies()) {
//            es.add(convertToDatabaseEntity(m));
//        }
//        return es;
//    }
//
//    public static List<RecipeCourseEntity> getAllByCourseNo(int courseNo) {
//        List<RecipeCourseEntity> entities = new ArrayList<>();
//        for (RecipeCoursePersistenceModelItem m : TestDataRecipeCourse.getAll()) {
//            if (courseNo == m.getCourse().getId()) {
//                entities.add(convertToDatabaseEntity(m));
//            }
//        }
//        return entities;
//    }
//
//    public static List<RecipeCourseEntity> getAll() {
//        List<RecipeCourseEntity> entities = new ArrayList<>();
//        for (RecipeCoursePersistenceModelItem m : TestDataRecipeCourse.getAll()) {
//            entities.add(convertToDatabaseEntity(m));
//        }
//        return entities;
//    }
//
//    private static RecipeCourseEntity convertToDatabaseEntity(RecipeCoursePersistenceModelItem m) {
//        return new RecipeCourseEntity(
//                m.getDataId(),
//                parentDataId, m.getDomainId(),
//                m.getCourse().getId(),
//                m.isActive(),
//                m.getCreateDate(),
//                m.getLastUpdate()
//        );
//    }
}
