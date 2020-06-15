package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.domain.model.UseCaseDomainModel;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseElement;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Nonnull;

public class RecipeCourse
        extends
        UseCaseElement<RecipeCoursePersistenceModel, RecipeCourse.DomainModel>
        implements
        DomainDataAccess.GetDomainModelCallback<RecipeCoursePersistenceModel> {

    private static final String TAG = "tkm-" + RecipeCourse.class.getSimpleName() + ": ";

    static final int MINIMUM_COURSE_LIST_SIZE = 1;

    public enum Course {
        COURSE_ZERO(0),
        COURSE_ONE(1),
        COURSE_TWO(2),
        COURSE_THREE(3),
        COURSE_FOUR(4),
        COURSE_FIVE(5),
        COURSE_SIX(6),
        COURSE_SEVEN(7);

        private final int id;
        @SuppressLint("UseSparseArrays")
        private static Map<Integer, Course> courseMap = new HashMap<>();

        Course(int id) {
            this.id = id;
        }

        static {
            for (Course course : Course.values()) courseMap.put(course.id, course);
        }

        public static Course fromId(int courseNo) {
            return courseMap.get(courseNo);
        }

        public int getId() {
            return id;
        }
    }

    protected static final class DomainModel
            extends
            ArrayList<Course>
            implements
            UseCaseDomainModel {
    }

    @Nonnull
    private RepositoryRecipeCourse repository;
    @Nonnull
    private UniqueIdProvider idProvider;
    @Nonnull
    private TimeProvider timeProvider;

    private long createDate;

    public RecipeCourse(@Nonnull RepositoryRecipeCourse repository,
                        @Nonnull UniqueIdProvider idProvider,
                        @Nonnull TimeProvider timeProvider) {
        this.repository = repository;
        this.idProvider = idProvider;
        this.timeProvider = timeProvider;

        activeDomainModel = new DomainModel();
        updatedDomainModel = new DomainModel();
    }

    @Override
    protected void loadDomainModelByDataId() {
        System.out.println(TAG + "loadDomainModelByDataId called, this shouldn't be called");
        // Currently only uses domain id's, this shouldn't be called
        repository.getByDataId(useCaseDataId, this);
    }

    @Override
    protected void loadDomainModelByDomainId() {
        System.out.println(TAG + "loadDomainModelByDomainId called");
        repository.getActiveByDomainId(useCaseDomainId, this);
    }

    @Override
    public void onDomainModelUnavailable() {
        System.out.println(TAG + "onDomainModelsUnavailable");
        isDomainDataUnavailable = true;
        updatedDomainModel = activeDomainModel;

        reprocessCurrentDomainModel();
    }

    @Override
    public void onDomainModelLoaded(RecipeCoursePersistenceModel persistenceModel) {
        System.out.println(TAG + "onAllDomainModelsLoaded=" + persistenceModel);
        isDomainDataUnavailable = false;
        createDate = persistenceModel.getCreateDate();

        createUpdatedDomainModelFromPersistenceModel(persistenceModel);

        activeDomainModel = updatedDomainModel;

        processUpdatedDomainModel();
    }

    private void createUpdatedDomainModelFromPersistenceModel(
            RecipeCoursePersistenceModel persistenceModel) {
        updatedDomainModel.addAll(persistenceModel.getCourses());

        System.out.println(TAG + "getDomainModelFromPersistenceModel: updatedDomainModel=" +
                updatedDomainModel);
    }

    @Override
    protected void processRequestDomainModel() {
        System.out.println(TAG + "processRequestDomainModel");
        getDomainModelFromRequestModel();
        processUpdatedDomainModel();
    }

    private void getDomainModelFromRequestModel() {
        RecipeCourseRequest request = (RecipeCourseRequest) getRequest();
        updatedDomainModel.addAll(request.getDomainModel().getCourseList());

        System.out.println(TAG + "getDomainModelFromRequestModel: updatedDomainModel=" +
                updatedDomainModel);
    }

    @Override
    protected void reprocessCurrentDomainModel() {
        System.out.println(TAG + "reprocessCurrentDomainModel");
        processUpdatedDomainModel();
    }

    private void processUpdatedDomainModel() {
        isChanged = !activeDomainModel.equals(updatedDomainModel);

        if (!isChanged) {
            updatedDomainModel = activeDomainModel;
        }

        System.out.println(TAG + "processNewDomainModel: isChanged=" + isChanged);
        validateNewDomainModelElements();
    }

    private void validateNewDomainModelElements() {
        System.out.println(TAG + "validateNewDomainModelElements");
        setupDomainModelProcessing();
        processCourseAdditions();
        processCourseSubtractions();
        if (isChanged) {
            save();
        }
        buildResponse();
    }

    private void setupDomainModelProcessing() {
        System.out.println(TAG + "setupDomainModelProcessing");
        failReasons.clear();
        isChanged = false;
    }

    private void processCourseAdditions() {
        System.out.println(TAG + "processCourseAdditions");
        for (Course course : updatedDomainModel) {
            if (isCourseAdded(course)) {
                activeDomainModel.add(course);
                isChanged = true;
                addCommonFailReasonNone();
            }
        }
    }

    private void addCommonFailReasonNone(){
        if (!failReasons.contains(CommonFailReason.NONE)) {
            failReasons.add(CommonFailReason.NONE);
        }
    }

    private boolean isCourseAdded(Course course) {
        System.out.println(TAG + "isCourseAdded=" + !activeDomainModel.contains(course));
        return !activeDomainModel.contains(course);
    }

    private void processCourseSubtractions() {
        Iterator<Course> i = activeDomainModel.iterator();
        while (i.hasNext()) {
            Course course = i.next();

            if (isCourseRemoved(course)) {
                i.remove();
                isChanged = true;
            }
        }
        if (activeDomainModel.size() < MINIMUM_COURSE_LIST_SIZE) {
            addCommonFailReasonDataUnavailable();
        }
        System.out.println(TAG + "processCourseSubtractions: courses=" + activeDomainModel);
    }

    private void addCommonFailReasonDataUnavailable() {
        if (!failReasons.contains(CommonFailReason.DATA_UNAVAILABLE)) {
            failReasons.add(CommonFailReason.DATA_UNAVAILABLE);
        }
    }

    private boolean isCourseRemoved(Course course) {
        System.out.println(TAG + "isCourseRemoved: " + course + " " +
                !updatedDomainModel.contains(course));
        return !updatedDomainModel.contains(course);
    }

    private void save() {
        persistenceModel = new RecipeCoursePersistenceModel.Builder().
                setDataId(idProvider.getUId()).
                setDomainId(useCaseDomainId).
                setCourses(activeDomainModel).
                setCreateDate(createDate).
                setLastUpdate(timeProvider.getCurrentTimeInMills()).
                build();

        System.out.println(TAG + "save: saving persistence model=" + persistenceModel);
        repository.save(persistenceModel);
    }

    protected void buildResponse() {
        RecipeCourseResponse response = new RecipeCourseResponse.Builder().
                setDataId(persistenceModel.getDataId()).
                setDomainId(useCaseDomainId).
                setMetadata(getMetadata()).
                setDomainModel(getResponseModel()).
                build();

        System.out.println(TAG + "Response No:" + accessCount + " - " + response);

        sendResponse(response);
    }

    private RecipeCourseResponse.Model getResponseModel() {
        System.out.println(TAG + "getResponseModel");
        return new RecipeCourseResponse.Model.Builder().
                setCourseList(new ArrayList<>(activeDomainModel)).
                build();
    }
}
