package com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe;

import com.example.peter.thekitchenmenu.commonmocks.UseCaseSchedulerMock;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetAllDomainModelsCallback;
import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess.GetDomainModelCallback;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeCourse;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipeMetadata;
import com.example.peter.thekitchenmenu.data.repository.recipe.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCourse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.course.RecipeCoursePersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDuration;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityTest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsPersistenceModel;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.portions.RecipePortionsTest;
import com.example.peter.thekitchenmenu.domain.usecase.textvalidation.TextValidator;
import com.example.peter.thekitchenmenu.domain.utils.TimeProvider;
import com.example.peter.thekitchenmenu.domain.utils.UniqueIdProvider;

import org.junit.Before;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

public class RecipeTestBase {

    @Mock
    public RepositoryRecipeMetadata repoMetadataMock;
    @Captor
    public ArgumentCaptor<GetDomainModelCallback<RecipeMetadataPersistenceModel>> repoMetadataCallback;
    @Mock
    public RepositoryRecipeIdentity repoIdentityMock;
    @Captor
    public ArgumentCaptor<GetDomainModelCallback<RecipeIdentityPersistenceModel>> repoIdentityCallback;
    @Mock
    public RepositoryRecipeCourse repoCourseMock;
    @Captor
    public ArgumentCaptor<GetAllDomainModelsCallback<RecipeCoursePersistenceModel>> repoCourseCallback;
    @Mock
    public RepositoryRecipeDuration repoDurationMock;
    @Captor
    public ArgumentCaptor<GetDomainModelCallback<RecipeDurationPersistenceModel>> repoDurationCallback;
    @Mock
    public RepositoryRecipePortions repoPortionsMock;
    @Captor
    public ArgumentCaptor<GetDomainModelCallback<RecipePortionsPersistenceModel>> repoPortionsCallback;

    @Mock
    private UniqueIdProvider idProviderMock;
    @Mock
    private TimeProvider timeProviderMock;

    public UseCaseHandler handler;
    private int noOfRequiredComponents;
    public Recipe SUT;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        SUT = givenUseCase();
    }

    public Recipe givenUseCase() {
        handler = new UseCaseHandler(new UseCaseSchedulerMock());

        TextValidator textValidator = new TextValidator.Builder().
                setShortTextMinLength(RecipeIdentityTest.TITLE_MIN_LENGTH).
                setShortTextMaxLength(RecipeIdentityTest.TITLE_MAX_LENGTH).
                setLongTextMinLength(RecipeIdentityTest.DESCRIPTION_MIN_LENGTH).
                setLongTextMaxLength(RecipeIdentityTest.DESCRIPTION_MAX_LENGTH).
                build();

        final Set<RecipeMetadata.ComponentName> requiredComponents = new HashSet<>();
        requiredComponents.add(RecipeMetadata.ComponentName.IDENTITY);
        requiredComponents.add(RecipeMetadata.ComponentName.COURSE);
        requiredComponents.add(RecipeMetadata.ComponentName.DURATION);
        requiredComponents.add(RecipeMetadata.ComponentName.PORTIONS);

        noOfRequiredComponents = requiredComponents.size();

        RecipeMetadata recipeMetadata = new RecipeMetadata(
                repoMetadataMock,
                idProviderMock,
                timeProviderMock,
                requiredComponents
        );
        RecipeIdentity identity = new RecipeIdentity(
                repoIdentityMock,
                idProviderMock,
                timeProviderMock,
                textValidator
        );
        RecipeCourse course = new RecipeCourse(
                repoCourseMock,
                idProviderMock,
                timeProviderMock
        );
        RecipeDuration duration = new RecipeDuration(
                repoDurationMock,
                timeProviderMock,
                idProviderMock,
                RecipeDurationTest.MAX_PREP_TIME,
                RecipeDurationTest.MAX_COOK_TIME
        );
        RecipePortions portions = new RecipePortions(
                repoPortionsMock,
                idProviderMock,
                timeProviderMock,
                RecipePortionsTest.MAX_SERVINGS,
                RecipePortionsTest.MAX_SITTINGS
        );

        return new Recipe(
                handler,
                recipeMetadata,
                identity,
                course,
                duration,
                portions);
    }

    public Recipe getRecipe() {
        return SUT;
    }

    public UseCaseHandler getHandler() {
        return handler;
    }

    public UniqueIdProvider getIdProviderMock() {
        return idProviderMock;
    }

    int getNoOfRequiredComponents() {
        return noOfRequiredComponents;
    }

    public TimeProvider getTimeProviderMock() {
        return timeProviderMock;
    }
}
