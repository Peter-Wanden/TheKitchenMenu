package com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.dataadapter;

import com.example.peter.thekitchenmenu.data.repository.recipe.metadata.TestDataRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.source.local.dataadapter.PrimitiveDataSource.GetAllPrimitiveCallback;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.TestDataRecipeMetadataEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonsLocalDataSource;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentLocalDataSource;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RecipeMetadataLocalDeleteAdapterTest {

    // region constants ----------------------------------------------------------------------------
    // endregion constants -------------------------------------------------------------------------

    // region helper fields ------------------------------------------------------------------------
    @Mock
    RecipeMetadataParentLocalDataSource repoParentMock;
    @Captor
    ArgumentCaptor<GetAllPrimitiveCallback<RecipeMetadataParentEntity>> repoParentCallback;
    @Mock
    RecipeComponentStateLocalDataSource repoComponentStateMock;
    @Mock
    RecipeFailReasonsLocalDataSource repoFailReasonsMock;

    private String dataId = TestDataRecipeMetadata.getValidChangedThree().getDataId();
    private String domainId = TestDataRecipeMetadata.getValidChangedThree().getDomainId();
    // endregion helper fields ---------------------------------------------------------------------

    private RecipeMetadataLocalDeleteAdapter SUT;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SUT = givenSystemUnderTest();
    }

    private RecipeMetadataLocalDeleteAdapter givenSystemUnderTest() {
        return new RecipeMetadataLocalDeleteAdapter(
                repoParentMock,
                repoComponentStateMock,
                repoFailReasonsMock
        );
    }

    @Test
    public void deleteDataId() {
        // Arrange
        // Act
        SUT.deleteByDataId(dataId);
        // Assert
        verify(repoParentMock).deleteByDataId(dataId);
        verify(repoComponentStateMock).deleteAllByParentDataId(dataId);
        verify(repoFailReasonsMock).deleteAllByParentDataId(dataId);
    }

    @Test
    public void deleteAllByDomainId() {
        // Arrange
        List<RecipeMetadataParentEntity> parentEntities = TestDataRecipeMetadataEntity.
                getAllByDomainId(domainId);
        int noOfParents = parentEntities.size();
        // Act
        SUT.deleteAllByDomainId(domainId);
        // Assert
        verify(repoParentMock).getAllByDomainId(eq(domainId), repoParentCallback.capture());
        repoParentCallback.getValue().onAllLoaded(parentEntities);

        verify(repoParentMock, times((4))).
                deleteByDataId(parentEntities.get(0).getDataId());
        verify(repoFailReasonsMock, times((4))).
                deleteAllByParentDataId(parentEntities.get(0).getDataId());
        verify(repoComponentStateMock, times((4))).
                deleteAllByParentDataId(parentEntities.get(0).getDataId());
    }

    @Test
    public void deleteAll() {
        // Arrange
        // Act
        SUT.deleteAll();
        // Assert
        verify(repoParentMock).deleteAll();
        verify(repoComponentStateMock).deleteAll();
        verify(repoFailReasonsMock).deleteAll();
    }

    // region helper methods -----------------------------------------------------------------------
    // endregion helper methods --------------------------------------------------------------------

    // region helper classes -----------------------------------------------------------------------

    // endregion helper classes --------------------------------------------------------------------
}