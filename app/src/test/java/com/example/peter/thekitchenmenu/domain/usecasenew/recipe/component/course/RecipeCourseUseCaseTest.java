package com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.course;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RecipeCourseUseCaseDataAccess;
import com.example.peter.thekitchenmenu.data.repository.recipe.course.TestDataRecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseCallback;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.message.UseCaseResponse;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.UseCaseMetadataModel;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class RecipeCourseUseCaseTest {

    private static final String TAG = "tkm-" + RecipeCourseUseCaseTest.class.getSimpleName() + ": ";

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RecipeCourseUseCaseDataAccess dataAccessMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<RecipeCourseUseCasePersistenceModel>> dataAccessCallback;
    @Mock
    UniqueIdProvider idProviderMock;
    @Mock
    TimeProvider timeProviderMock;

    private UseCaseResponse<RecipeCourseUseCaseResponseModel> onSuccessResponse;
    private UseCaseResponse<RecipeCourseUseCaseResponseModel> onErrorResponse;
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeCourseUseCase SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();
    }

    private RecipeCourseUseCase givenUseCase() {
        RecipeCourseDomainModelConverter converter = new RecipeCourseDomainModelConverter(
                timeProviderMock,
                idProviderMock
        );
        return new RecipeCourseUseCase(
                dataAccessMock,
                converter
        );
    }

    @Test
    public void emptyRequest_stateINVALID_DEFAULT_failReasonsNO_COURSE_SELECTED_DATA_UNAVAILABLE() {
        // Arrange
        RecipeCourseUseCaseRequest request = new RecipeCourseUseCaseRequest.Builder()
                .getDefault()
                .build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        UseCaseResponse<RecipeCourseUseCaseResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeCourseUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.INVALID_DEFAULT;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Arrays.asList(
                RecipeCourseUseCaseFailReason.NO_COURSE_SELECTED,
                CommonFailReason.DATA_UNAVAILABLE
        );
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

        assertTrue(responseModel.getCourseList().isEmpty());
    }

    @Test
    public void newRequest_addCourse_StateVALID_CHANGED_failReasonsNONE() {
        // Arrange
        // arrange persistent model that represents state after adding domain data
        RecipeCourseUseCasePersistenceModel modelUnderTest = TestDataRecipeCourse.
                getNewActiveCourseOne();

        // execute and test a new domain Id only request that returns data unavailable
        simulateDataUnavailable(modelUnderTest);

        // request add a course
        RecipeCourseUseCaseRequest request = new RecipeCourseUseCaseRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipeCourseUseCaseRequestModel.Builder()
                        .setCourseList(modelUnderTest.getCourses())
                        .build())
                .build();

        // as the domain data is valid there will be a save to persistence
        // - new time for persistence model
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(modelUnderTest.getCreateDate());
        // - new data id for persistence model
        when(idProviderMock.getUId()).thenReturn(modelUnderTest.getDataId());

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        verify(dataAccessMock).save(eq(modelUnderTest));

        UseCaseResponse<RecipeCourseUseCaseResponseModel> response = onSuccessResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeCourseUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.VALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Collections.singletonList(CommonFailReason.NONE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

        assertEquals(
                modelUnderTest.getCourses(),
                responseModel.getCourseList()
        );
    }

    @Test
    public void newRequest_addTwoCourses_stateVALID_CHANGED() {
        // Arrange
        // arrange persistence model that represents data after adding first course
        RecipeCourseUseCasePersistenceModel firstCoursePersistence = TestDataRecipeCourse.
                getNewActiveCourseOne();
        // Arrange persistence model that represents the archiving of the first course state
        RecipeCourseUseCasePersistenceModel expectedArchivedCourseOne = TestDataRecipeCourse.
                getNewArchivedCourseOne();
        // arrange persistence model that represents data after adding second course
        RecipeCourseUseCasePersistenceModel secondCoursePersistence = TestDataRecipeCourse.
                getNewActiveCourseOneAndTwo();

        // initialise use case
        simulateDataUnavailable(firstCoursePersistence);

        // add first course
        RecipeCourseUseCaseRequest addFirstCourseRequest = new RecipeCourseUseCaseRequest.Builder()
                .basedOnResponse(onErrorResponse)
                .setRequestModel(new RecipeCourseUseCaseRequestModel.Builder()
                        .setCourseList(firstCoursePersistence.getCourses())
                        .build())
                .build();
        // arrange id and timestamp for first course save
        when(idProviderMock.getUId()).thenReturn(firstCoursePersistence.getDataId());
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(firstCoursePersistence.getCreateDate());

        // execute add first course request
        SUT.execute(addFirstCourseRequest, new UseCaseCallbackImplementer());

        // verify first course saved correctly
        verify(dataAccessMock).save(eq(firstCoursePersistence));

        // add second course
        RecipeCourseUseCaseRequest addSecondCourseRequest = new RecipeCourseUseCaseRequest.Builder()
                .basedOnResponse(onSuccessResponse)
                .setRequestModel(new RecipeCourseUseCaseRequestModel.Builder()
                        .setCourseList(secondCoursePersistence.getCourses())
                        .build())
                .build();

        // arrange timestamp for archiving of old persistence model
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(expectedArchivedCourseOne.getLastUpdate());
        // arrange new data id and timestamp for second course persistence save
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(secondCoursePersistence.getCreateDate());
        when(idProviderMock.getUId()).thenReturn(secondCoursePersistence.getDataId());

        // Act
        // execute second course request
        SUT.execute(addSecondCourseRequest, new UseCaseCallbackImplementer());

        // Assert
        // verify first persistence model archived
        verify(dataAccessMock).save(eq(expectedArchivedCourseOne));
        // verify second persistence course saved
        verify(dataAccessMock).save(eq(secondCoursePersistence));

        // assert response values
        UseCaseResponse<RecipeCourseUseCaseResponseModel> response = onSuccessResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeCourseUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.VALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Collections.singletonList(CommonFailReason.NONE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

        assertEquals(
                secondCoursePersistence.getCourses(),
                responseModel.getCourseList()
        );
    }

    @Test
    public void existingRequest_removeCourse_stateVALID_CHANGED_failReasonsNONE() {
        // Arrange
        // arrange persistence model that represents two courses
        RecipeCourseUseCasePersistenceModel startingPersistenceModel =
                TestDataRecipeCourse.getNewActiveCourseOneAndTwo();
        // arrange persistent model for archiving the two courses persistence model
        RecipeCourseUseCasePersistenceModel expectedArchivedStartingPersistenceModel =
                TestDataRecipeCourse.getNewArchivedCourseOneAndTwo();
        // arrange persistent model that represents state after removing course one
        RecipeCourseUseCasePersistenceModel expectedModelSavedAfterCourseRemoved =
                TestDataRecipeCourse.getNewActiveAfterCourseOneRemoved();

        // load up the use case with the starting persistence model
        simulateLoadPersistenceModel(startingPersistenceModel);

        // arrange remove a course request
        RecipeCourseUseCaseRequest removeCourseRequest = new RecipeCourseUseCaseRequest.Builder()
                .basedOnResponse(onSuccessResponse)
                .setRequestModel(new RecipeCourseUseCaseRequestModel.Builder()
                        .setCourseList(expectedModelSavedAfterCourseRemoved.getCourses())
                        .build())
                .build();

        // arrange timestamp for archiving of old persistence model
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                expectedArchivedStartingPersistenceModel.getLastUpdate());

        // arrange new data id and timestamp for second course persistence save
        when(timeProviderMock.getCurrentTimeInMills()).thenReturn(
                expectedModelSavedAfterCourseRemoved.getCreateDate());
        when(idProviderMock.getUId()).thenReturn(
                expectedModelSavedAfterCourseRemoved.getDataId());

        // execute second course request
        SUT.execute(removeCourseRequest, new UseCaseCallbackImplementer());

        // Assert
        // verify first persistence model archived
        verify(dataAccessMock).save(eq(expectedArchivedStartingPersistenceModel));
        // verify second persistence course saved
        verify(dataAccessMock).save(eq(expectedModelSavedAfterCourseRemoved));

        // Assert
        // assert response values
        UseCaseResponse<RecipeCourseUseCaseResponseModel> response = onSuccessResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeCourseUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.VALID_CHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Collections.singletonList(CommonFailReason.NONE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

        assertEquals(
                expectedModelSavedAfterCourseRemoved.getCourses(),
                responseModel.getCourseList()
        );
    }

    @Test
    public void existingRequest_removeAllCourses_stateINVALID_DEFAULT_failReasonNO_COURSE_SELECTED() {
        // Arrange
        RecipeCourseUseCasePersistenceModel modelUnderTest = TestDataRecipeCourse.
                getExistingActiveRecipeCourseZero();
        // load up the use case with the persistence model
        simulateLoadPersistenceModel(modelUnderTest);

        // arrange request to remove all courses
        RecipeCourseUseCaseRequest request = new RecipeCourseUseCaseRequest.Builder()
                .basedOnResponse(onSuccessResponse)
                .setRequestModel(new RecipeCourseUseCaseRequestModel.Builder()
                        .setCourseList(new ArrayList<>())
                        .build())
                .build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        // assert response values
        UseCaseResponse<RecipeCourseUseCaseResponseModel> response = onErrorResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeCourseUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.INVALID_DEFAULT;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Collections.singletonList(
                RecipeCourseUseCaseFailReason.NO_COURSE_SELECTED);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

        assertTrue(responseModel.getCourseList().isEmpty());
    }

    @Test
    public void existingRequest_allCoursesLoaded_stateVALID_UNCHANGED_failReasonsNONE() {
        // Arrange
        // arrange persistence model representing all courses
        RecipeCourseUseCasePersistenceModel expectedAllCoursesModel = TestDataRecipeCourse.
                getExistingActiveWithAllCourses();

        RecipeCourseUseCaseRequest request = new RecipeCourseUseCaseRequest.Builder()
                .setDomainId(expectedAllCoursesModel.getDomainId())
                .build();
        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        // assert data called for, and return expected persistence model
        verify(dataAccessMock).getByDomainId(eq(expectedAllCoursesModel.getDomainId()),
                dataAccessCallback.capture());
        dataAccessCallback.getValue().onPersistenceModelLoaded(expectedAllCoursesModel);

        // assert response values
        UseCaseResponse<RecipeCourseUseCaseResponseModel> response = onSuccessResponse;
        UseCaseMetadataModel metadata = response.getUseCaseMetadataModel();
        RecipeCourseUseCaseResponseModel responseModel = response.getResponseModel();

        ComponentState expectedState = ComponentState.VALID_UNCHANGED;
        ComponentState actualState = metadata.getComponentState();
        assertEquals(
                expectedState,
                actualState
        );

        List<FailReasons> expectedFailReasons = Collections.singletonList(CommonFailReason.NONE);
        List<FailReasons> actualFailReasons = metadata.getFailReasons();
        assertEquals(
                expectedFailReasons,
                actualFailReasons
        );

        assertEquals(
                expectedAllCoursesModel.getCourses(),
                responseModel.getCourseList()
        );
    }

    // region helper methods -----------------------------------------------------------------------
    private void simulateDataUnavailable(RecipeCourseUseCasePersistenceModel modelUnderTest) {
        // Arrange
        RecipeCourseUseCaseRequest request = new RecipeCourseUseCaseRequest.Builder()
                .getDefault()
                .setDomainId(modelUnderTest.getDomainId())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        verify(dataAccessMock).getByDomainId(eq(modelUnderTest.getDomainId()),
                dataAccessCallback.capture()
        );
        dataAccessCallback.getValue().onPersistenceModelUnavailable();
    }

    private void simulateLoadPersistenceModel(RecipeCourseUseCasePersistenceModel modelUnderTest) {
        // Arrange
        RecipeCourseUseCaseRequest request = new RecipeCourseUseCaseRequest.Builder()
                .getDefault()
                .setDomainId(modelUnderTest.getDomainId())
                .build();

        // Act
        SUT.execute(request, new UseCaseCallbackImplementer());

        // Assert
        verify(dataAccessMock).getByDomainId(eq(modelUnderTest.getDomainId()),
                dataAccessCallback.capture()
        );
        dataAccessCallback.getValue().onPersistenceModelLoaded(modelUnderTest);
    }
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private final class UseCaseCallbackImplementer
            implements
            UseCaseCallback<UseCaseResponse<RecipeCourseUseCaseResponseModel>> {

        @Override
        public void onSuccess(UseCaseResponse<RecipeCourseUseCaseResponseModel> response) {
            onSuccessResponse = response;
        }

        @Override
        public void onError(UseCaseResponse<RecipeCourseUseCaseResponseModel> response) {
            onErrorResponse = response;
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}