package com.example.peter.thekitchenmenu.domain.usecase.ingredient;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.domain.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.UseCaseTextValidator;
import com.example.peter.thekitchenmenu.ui.detail.ingredient.AddEditIngredientNavigator;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

import org.junit.*;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class UseCaseIngredientInteractorTest {

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RepositoryIngredient repoMock;
    @Captor
    ArgumentCaptor<DataSource.GetEntityCallback<IngredientEntity>> repoCallbackCaptor;
    @Mock
    UniqueIdProvider idProviderMock;
    @Mock
    TimeProvider timeProviderMock;
    @Mock
    UseCaseIngredientDuplicateChecker duplicateCheckerMock;
    UseCaseHandler handler;
    private int shortTextMinLength = 3;
    private int shortTextMaxLength = 70;
    private int longTextMinLength = 0;
    private int longTextMaxLength = 500;
    // endregion helper fields ---------------------------------------------------------------------

    private UseCaseIngredientInteractor SUT;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        handler = new UseCaseHandler(new UseCaseSchedulerMock());
        SUT = givenUseCase();

    }

    private UseCaseIngredientInteractor givenUseCase() {

        UseCaseIngredient useCaseIngredient = new UseCaseIngredient(
                repoMock,
                idProviderMock,
                timeProviderMock,
                duplicateCheckerMock);

        UseCaseTextValidator useCaseTextValidator = new UseCaseTextValidator.Builder().
                setShortTextMinLength(shortTextMinLength).
                setShrotTextMaxLength(shortTextMaxLength).
                setLongTextMinLength(longTextMinLength).
                setLongTextMaxLength(longTextMaxLength).
                build();

        return new UseCaseIngredientInteractor(
                useCaseIngredient,
                useCaseTextValidator
        );
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    // endregion helper classes --------------------------------------------------------------------


}