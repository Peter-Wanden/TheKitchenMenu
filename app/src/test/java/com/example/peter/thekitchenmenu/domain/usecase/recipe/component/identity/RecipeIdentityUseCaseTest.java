package com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RecipeIdentityUseCaseDataAccess;
import com.example.peter.thekitchenmenu.domain.businessentity.textvalidation.TextValidationBusinessEntity;
import com.example.peter.thekitchenmenu.domain.businessentity.textvalidation.TextValidationBusinessEntityTest;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RecipeIdentityUseCaseTest {

    // region constants
    // endregion constants

    // region helper fields
    @Mock
    RecipeIdentityUseCaseDataAccess dataAccessMock;
    @Captor
    ArgumentCaptor<GetDomainModelCallback<RecipeIdentityUseCasePersistenceModel>> dataAccessCallback;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    UniqueIdProvider idProviderMock;
    // endregion helper fields

    RecipeIdentityUseCase SUT;

    @Before
    public void setup() {
        SUT = givenUseCase();

    }

    private RecipeIdentityUseCase givenUseCase() {
        TextValidationBusinessEntity textValidator = new TextValidationBusinessEntity(
                TextValidationBusinessEntityTest.SHORT_TEXT_MIN_LENGTH,
                TextValidationBusinessEntityTest.SHORT_TEXT_MAX_LENGTH,
                TextValidationBusinessEntityTest.LONG_TEXT_MIN_LENGTH,
                TextValidationBusinessEntityTest.LONG_TEXT_MAX_LENGTH
        );

        RecipeIdentityDomainModelConverter converter = new RecipeIdentityDomainModelConverter(
                timeProviderMock, idProviderMock
        );
        return new RecipeIdentityUseCase(
                dataAccessMock,
                converter,
                idProviderMock,
                timeProviderMock,
                textValidator
        );
    }

    // region helper methods
    // endregion helper methods

    // region helper classes
    // endregion helper classes
}