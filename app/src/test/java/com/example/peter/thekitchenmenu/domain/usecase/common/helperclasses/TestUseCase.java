package com.example.peter.thekitchenmenu.domain.usecase.common.helperclasses;

import android.annotation.SuppressLint;

import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseResult;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseData;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Inherits from {@link UseCaseBase}, {@link UseCaseData} and {@link UseCaseResult} to
 * complete the common functionality of a use case.
 */
public class TestUseCase
        extends
        UseCaseResult<
                TestUseCaseDataAccess,
                TestUseCasePersistenceModel,
                TestUseCaseInternalModel,
                TestUseCaseRequestModel,
                TestUseCaseResponseModel> {

    private static final String TAG = "tkm-" + TestUseCase.class.getSimpleName() + ": ";

    /**
     * Fail reasons are metadata that provide a description as to why a domain data element of
     * a use case is invalid. As such, if a use case has one or more domain data elements that
     * can be invalid a fail reason must be added to reflect its invalid state.
     * For example: TEXT_TOO_LONG or TEXT_TOO_SHORT, VALUE_TOO_HIGH etc.
     */
    public enum FailReason
            implements
            FailReasons {
        TEXT_NULL(1000),
        TEXT_TOO_SHORT(1003),
        TEXT_TOO_LONG(1002);

        /* Enums must provide a numerical representation of themselves for persistence reasons */
        private final int id;

        @SuppressLint("UseSparseArrays")
        private static Map<Integer, FailReason> options = new HashMap<>();

        FailReason(int id) {
            this.id = id;
        }

        static {
            for (FailReason f : FailReason.values()) {
                options.put(f.getId(), f);
            }
        }

        public static FailReason getById(int id) {
            return options.get(id);
        }

        @Override
        public int getId() {
            return id;
        }
    }

    // arbitrary values for testing purposes
    public static final int MIN_STRING_LENGTH = 5;
    public static final int MAX_STRING_LENGTH = 50;

    // deliberately not final so can be changed between test cases
    public String useCaseModelDefaultValue = "useCaseModelDefaultValue";

    public TestUseCase(TestUseCaseDataAccess dataAccess,
                       TestDomainModelConverter converter,
                       UniqueIdProvider idProvider,
                       TimeProvider timeProvider) {
        super(dataAccess, converter, idProvider, timeProvider);
    }

    /**
     * This is where the business application logic goes, also any calls to business entities.
     * To keep things simple the use case application logic performs a length check on a string.
     *
     * @return return true only after all data elements have been processed.
     */
    @Override
    protected boolean isDomainDataElementsProcessed() {
        if (useCaseModel.getUseCaseModelString() == null) {
            failReasons.add(FailReason.TEXT_NULL);
        } else if (useCaseModel.getUseCaseModelString().length() < MIN_STRING_LENGTH) {
            failReasons.add(FailReason.TEXT_TOO_SHORT);
        } else if (useCaseModel.getUseCaseModelString().length() > MAX_STRING_LENGTH) {
            failReasons.add(FailReason.TEXT_TOO_LONG);
        }
        return true;
    }

    /**
     * @return the use case domain models default values, these are specific to each use case.
     */
    @Override
    protected TestUseCaseInternalModel createUseCaseModelFromDefaultValues() {
        return new TestUseCaseInternalModel(useCaseModelDefaultValue);
    }

    @Override
    protected void buildResponse() {
        sendResponse(new TestUseCaseResponse.Builder()
                .setDataId(useCaseDataId)
                .setDomainId(useCaseDomainId)
                .setMetadata(getMetadata())
                .setDomainModel(getResponseModel())
                .build()
        );
    }
}
