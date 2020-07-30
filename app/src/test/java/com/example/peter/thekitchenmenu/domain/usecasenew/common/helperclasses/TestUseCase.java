package com.example.peter.thekitchenmenu.domain.usecasenew.common.helperclasses;

import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseData;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.UseCaseResult;

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
                TestRequestModel,
                TestResponseModel> {

    // arbitrary values for testing purposes
    public static final int MIN_STRING_LENGTH = 5;
    public static final int MAX_STRING_LENGTH = 50;

    // deliberately not final so can be changed between test cases
    public String useCaseModelDefaultValue = "useCaseModelDefaultValue";

    public TestUseCase(TestUseCaseDataAccess dataAccess,
                       TestDomainModelConverter converter) {
        super(dataAccess, converter);
    }

    /**
     * This is where the business application logic goes, also any calls to business entities.
     * To keep things simple the use case application logic in this test class performs a length
     * check on a string.
     *
     * When processing is complete call domainDataElementProcessingComplete()
     */
    @Override
    protected void beginProcessingDomainDataElements() {
        if (useCaseModel.getUseCaseModelString() == null) {
            failReasons.add(TestUseCaseFailReasons.TEXT_NULL);
        } else if (useCaseModel.getUseCaseModelString().length() < MIN_STRING_LENGTH) {
            failReasons.add(TestUseCaseFailReasons.TEXT_TOO_SHORT);
        } else if (useCaseModel.getUseCaseModelString().length() > MAX_STRING_LENGTH) {
            failReasons.add(TestUseCaseFailReasons.TEXT_TOO_LONG);
        }
        domainDataElementProcessingComplete();
    }

    /**
     * @return the use case domain models default values, these are specific to each use case.
     */
    @Override
    protected TestUseCaseInternalModel createUseCaseModelFromDefaultValues() {
        return new TestUseCaseInternalModel(useCaseModelDefaultValue);
    }
}
