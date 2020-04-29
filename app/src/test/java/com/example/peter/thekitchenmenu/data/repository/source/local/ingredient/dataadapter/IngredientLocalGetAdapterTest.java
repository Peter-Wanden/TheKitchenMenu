package com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.ingredient.datasource.IngredientLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter.RecipeMetadataLocalGetAdapterTest;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.recipeingredient.datasource.RecipeIngredientLocalDataSource;
import com.example.peter.thekitchenmenu.domain.usecase.ingredient.IngredientPersistenceModel;
import com.google.android.gms.common.util.WorkSourceUtil;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

public class IngredientLocalGetAdapterTest {

    private static final String TAG = "tkm-" + IngredientLocalGetAdapterTest.class.getSimpleName()
            + ": ";

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    IngredientLocalDataSource repoMock;
    @Captor
    ArgumentCaptor<GetPrimitiveCallback<IngredientEntity>> repoCallback;
    @Captor
    ArgumentCaptor<GetAllPrimitiveCallback<IngredientEntity>> repoGetAllCallback;
    // endregion helper fields ---------------------------------------------------------------------

    private IngredientLocalGetAdapter SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenSystemUnderTest();
    }

    private IngredientLocalGetAdapter givenSystemUnderTest() {
        return new IngredientLocalGetAdapter(
                repoMock
        );
    }

    @Test
    public void getByDataId_MODELS_UNAVAILABLE() {
        // Arrange
        // Act
        // Assert
    }

    @Test
    public void getAllByDomainId() {
        // Arrange
        // Act
        // Assert
    }

    @Test
    public void getActiveByDomainId() {
        // Arrange
        // Act
        // Assert
    }

    @Test
    public void getAll() {
        // Arrange
        // Act
        // Assert
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------
    private static class GetDomainModelCallback implements
            DomainDataAccess.GetDomainModelCallback<IngredientPersistenceModel> {

        private static final String TAG = IngredientLocalGetAdapterTest.TAG +
                GetDomainModelCallback.class.getSimpleName() + ": ";

        private IngredientPersistenceModel model;
        private boolean isModelUnavailable;

        @Override
        public void onModelLoaded(IngredientPersistenceModel m) {
            System.out.println(TAG + m);
            model = m;
        }

        @Override
        public void onModelUnavailable() {
            isModelUnavailable = true;
            System.out.println(TAG + "isModelUnavailable=" + isModelUnavailable);
        }
    }

    private static class GetAllDomainModelsCallback
            implements DomainDataAccess.GetAllDomainModelsCallback<IngredientPersistenceModel> {

        private static final String TAG = IngredientLocalGetAdapterTest.TAG +
                GetAllDomainModelsCallback.class.getSimpleName() + ": ";

        private List<IngredientPersistenceModel> models;
        private boolean isModelsUnavailable;

        @Override
        public void onAllLoaded(List<IngredientPersistenceModel> m) {
            System.out.println(TAG + m);
            models = m;
        }

        @Override
        public void onModelsUnavailable() {
            isModelsUnavailable = true;
            System.out.println(TAG + "isModelsUnavailable=" + isModelsUnavailable);
        }
    }
    // endregion helper classes --------------------------------------------------------------------
}