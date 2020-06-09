package com.example.peter.thekitchenmenu.data.repository.recipe.course;

import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModelItem;

import java.util.HashSet;
import java.util.Set;

public class TestDataRecipeCourse {

    public static final String NEW_RECIPE_ID =
            TestDataRecipeMetadata.getDataUnavailable().getDomainId();

    public static final String EXISTING_RECIPE_ID =
            TestDataRecipeMetadata.getValidUnchanged().getDomainId();

    // first course added
    public static RecipeCoursePersistenceModelItem getNewActiveRecipeCourseZero() {
        return new RecipeCoursePersistenceModelItem.Builder().
                setDataId("dataId-recipeCourse-new-id0").
                setDomainId(NEW_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_ZERO).
                setIsActive(true).
                setCreateDate(10L).
                setLastUpdate(10L).
                build();
    }

    // Second course added
    public static RecipeCoursePersistenceModelItem getNewActiveRecipeCourseOne() {
        return new RecipeCoursePersistenceModelItem.Builder().
                setDataId("dataId-recipeCourse-new-id1").
                setDomainId(NEW_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_ONE).
                setIsActive(true).
                setCreateDate(10L).
                setLastUpdate(10L).
                build();
    }

    // State of recipe course data after new courses added
    public static Set<RecipeCoursePersistenceModelItem> getNewActiveCourses() {
        Set<RecipeCoursePersistenceModelItem> models = new HashSet<>();
        models.add(getNewActiveRecipeCourseZero());
        models.add(getNewActiveRecipeCourseOne());
        return models;
    }

    // First course updated to deactivated
    public static RecipeCoursePersistenceModelItem getNewArchivedRecipeCourseZero() {
        return new RecipeCoursePersistenceModelItem.Builder().
                setDataId("dataId-recipeCourse-new-id0").
                setDomainId(NEW_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_ZERO).
                setIsActive(false).
                setCreateDate(10L).
                setLastUpdate(100L).
                build();
    }

    // Second course updated to deactivated
    public static RecipeCoursePersistenceModelItem getNewArchivedRecipeCourseOne() {
        return new RecipeCoursePersistenceModelItem.Builder().
                setDataId("dataId-recipeCourse-new-id1").
                setDomainId(NEW_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_ONE).
                setIsActive(false).
                setCreateDate(10L).
                setLastUpdate(100L).
                build();
    }

    // State of data after courses removed
    public static Set<RecipeCoursePersistenceModelItem> getNewArchivedRecipeCourses() {
        Set<RecipeCoursePersistenceModelItem> models = new HashSet<>();
        models.add(getNewArchivedRecipeCourseZero());
        models.add(getNewArchivedRecipeCourseOne());
        return models;
    }

    public static RecipeCoursePersistenceModelItem getExistingActiveRecipeCourseZero() {
        return new RecipeCoursePersistenceModelItem.Builder().
                setDataId("dataId-recipeCourse-existing-id0").
                setDomainId(EXISTING_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_ZERO).
                setIsActive(true).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceModelItem getExistingActiveRecipeCourseOne() {
        return new RecipeCoursePersistenceModelItem.Builder().
                setDataId("dataId-recipeCourse-existing-id1").
                setDomainId(EXISTING_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_ONE).
                setIsActive(true).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceModelItem getExistingActiveRecipeCourseTwo() {
        return new RecipeCoursePersistenceModelItem.Builder().
                setDataId("dataId-recipeCourse-existing-id2").
                setDomainId(EXISTING_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_TWO).
                setIsActive(true).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceModelItem getExistingActiveRecipeCourseThree() {
        return new RecipeCoursePersistenceModelItem.Builder().
                setDataId("dataId-recipeCourse-existing-id3").
                setDomainId(EXISTING_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_THREE).
                setIsActive(true).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceModelItem getExistingActiveRecipeCourseFour() {
        return new RecipeCoursePersistenceModelItem.Builder().
                setDataId("dataId-recipeCourse-existing-id4").
                setDomainId(EXISTING_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_FOUR).
                setIsActive(true).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceModelItem getExistingActiveRecipeCourseFive() {
        return new RecipeCoursePersistenceModelItem.Builder().
                setDataId("dataId-recipeCourse-existing-id5").
                setDomainId(EXISTING_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_FIVE).
                setIsActive(true).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceModelItem getExistingActiveRecipeCourseSix() {
        return new RecipeCoursePersistenceModelItem.Builder().
                setDataId("dataId-recipeCourse-existing-id6").
                setDomainId(EXISTING_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_SIX).
                setIsActive(true).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static RecipeCoursePersistenceModelItem getExistingActiveRecipeCourseSeven() {
        return new RecipeCoursePersistenceModelItem.Builder().
                setDataId("dataId-recipeCourse-existing-id7").
                setDomainId(EXISTING_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_SEVEN).
                setIsActive(true).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();
    }

    public static Set<RecipeCoursePersistenceModelItem> getAllExistingActiveRecipeCourses() {
        Set<RecipeCoursePersistenceModelItem> models = new HashSet<>();
        models.add(getExistingActiveRecipeCourseZero());
        models.add(getExistingActiveRecipeCourseOne());
        models.add(getExistingActiveRecipeCourseTwo());
        models.add(getExistingActiveRecipeCourseThree());
        models.add(getExistingActiveRecipeCourseFour());
        models.add(getExistingActiveRecipeCourseFive());
        models.add(getExistingActiveRecipeCourseSix());
        models.add(getExistingActiveRecipeCourseSeven());
        return models;
    }

    public static Set<RecipeCoursePersistenceModelItem> getAllExistingActiveEvenRecipeCourses() {
        Set<RecipeCoursePersistenceModelItem> evenModels = new HashSet<>();
        for (RecipeCoursePersistenceModelItem model : getAllExistingActiveRecipeCourses()) {
            if (model.getCourse().getCourseNo() % 2 == 0) {
                evenModels.add(model);
            }
        }
        return evenModels;
    }

    //TODO - "idFromAnotherUser" ??
    public static Set<RecipeCoursePersistenceModelItem> getAllExistingActiveByDomainId(String domainId) {
        if (domainId.equals("idFromAnotherUser")) {
            return getAllRecipeCourseCopies();
        } else {
            Set<RecipeCoursePersistenceModelItem> models = new HashSet<>();
            for (RecipeCoursePersistenceModelItem m : getAllExistingActiveRecipeCourses()) {
                if (domainId.equals(m.getDomainId())) {
                    models.add(m);
                }
            }
            return models;
        }
    }

    public static RecipeCoursePersistenceModel getActiveByDomainId(String domainId) {
        Set<RecipeCoursePersistenceModelItem> persistenceModelItems =
                getAllExistingActiveByDomainId(domainId);

        // The create date of the oldest active model
        long createDate = persistenceModelItems.isEmpty() ? 0L : Long.MAX_VALUE;
        // LastUpdate: If there are no current items; the last update of the most recently
        // archived item, else 0L if there are current items.
        long lastUpdate = 0L;

        for (RecipeCoursePersistenceModelItem item : persistenceModelItems) {
            createDate = Math.min(createDate, item.getCreateDate());
            lastUpdate = Math.max(lastUpdate, item.getLastUpdate());
        }

        return new RecipeCoursePersistenceModel.Builder().getDefault().
                setDomainId(domainId).
                setPersistenceModelItems(persistenceModelItems).
                setCreateDate(createDate).
                setLastUpdate(lastUpdate).
                build();
    }

    public static RecipeCoursePersistenceModelItem getCopiedRecipeCourseZero() {
        return new RecipeCoursePersistenceModelItem.Builder().
                setDataId("dataId-recipeCourse-copy-id0").
                setDomainId(NEW_RECIPE_ID).
                setCourse(getExistingActiveRecipeCourseZero().getCourse()).
                setIsActive(true).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static RecipeCoursePersistenceModelItem getCopiedRecipeCourseOne() {
        return new RecipeCoursePersistenceModelItem.Builder().
                setDataId("dataId-recipeCourse-copy-id1").
                setDomainId(NEW_RECIPE_ID).
                setCourse(getExistingActiveRecipeCourseOne().getCourse()).
                setIsActive(true).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static RecipeCoursePersistenceModelItem getCopiedRecipeCourseTwo() {
        return new RecipeCoursePersistenceModelItem.Builder().
                setDataId("dataId-recipeCourse-copy-id2").
                setDomainId(NEW_RECIPE_ID).
                setCourse(getExistingActiveRecipeCourseOne().getCourse()).
                setIsActive(true).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static RecipeCoursePersistenceModelItem getCopiedRecipeCourseFour() {
        return new RecipeCoursePersistenceModelItem.Builder().
                setDataId("dataId-recipeCourse-copy-id4").
                setDomainId(NEW_RECIPE_ID).
                setCourse(getExistingActiveRecipeCourseFour().getCourse()).
                setIsActive(true).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static RecipeCoursePersistenceModelItem getCopiedRecipeCourseSix() {
        return new RecipeCoursePersistenceModelItem.Builder().
                setDataId("dataId-recipeCourse-copy-id6").
                setDomainId(NEW_RECIPE_ID).
                setCourse(RecipeCourse.Course.COURSE_SIX).
                setIsActive(true).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    public static Set<RecipeCoursePersistenceModelItem> getAllRecipeCourseCopies() {
        Set<RecipeCoursePersistenceModelItem> models = new HashSet<>();
        models.add(getCopiedRecipeCourseZero());
        models.add(getCopiedRecipeCourseOne());
        models.add(getCopiedRecipeCourseTwo());
        models.add(getCopiedRecipeCourseFour());
        models.add(getCopiedRecipeCourseSix());
        return models;
    }

    public static Set<RecipeCoursePersistenceModelItem> getAllByCourse(RecipeCourse.Course c) {
        Set<RecipeCoursePersistenceModelItem> models = new HashSet<>();
        for (RecipeCoursePersistenceModelItem m : getAll()) {
            if (c == m.getCourse()) {
                models.add(m);
            }
        }
        return models;
    }

    public static Set<RecipeCoursePersistenceModelItem> getAll() {
        Set<RecipeCoursePersistenceModelItem> models = new HashSet<>();
        models.add(getNewActiveRecipeCourseZero());
        models.add(getNewActiveRecipeCourseOne());
        models.add(getNewArchivedRecipeCourseZero());
        models.add(getNewArchivedRecipeCourseOne());
        models.add(getExistingActiveRecipeCourseZero());
        models.add(getExistingActiveRecipeCourseOne());
        models.add(getExistingActiveRecipeCourseTwo());
        models.add(getExistingActiveRecipeCourseThree());
        models.add(getExistingActiveRecipeCourseFour());
        models.add(getExistingActiveRecipeCourseFive());
        models.add(getExistingActiveRecipeCourseSix());
        models.add(getExistingActiveRecipeCourseSeven());
        models.add(getCopiedRecipeCourseZero());
        models.add(getCopiedRecipeCourseOne());
        models.add(getCopiedRecipeCourseTwo());
        models.add(getCopiedRecipeCourseFour());
        models.add(getCopiedRecipeCourseSix());
        return models;
    }
}
