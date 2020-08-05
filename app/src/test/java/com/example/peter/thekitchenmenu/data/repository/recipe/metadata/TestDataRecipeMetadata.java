package com.example.peter.thekitchenmenu.data.repository.recipe.metadata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.metadata.RecipeMetadataUseCasePersistenceModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId.NO_ID;

import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentName;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.metadata.RecipeMetadataUseCaseFailReason;

/**
 * Persistence stores the state of an object (data model) each time it changes. Therefore there can
 * be many copies of an object with the same domain id, each distinguished by a unique data id.
 * In the case of recipe metadata, the ACTIVE model is the most recent according to lastUpdate()
 */
public class TestDataRecipeMetadata {

    public static final String NEW_RECIPE_DOMAIN_ID = getInvalidDefault().getDomainId();

    public static final Set<RecipeComponentName> requiredComponentNames = new HashSet<>();
    static {
        requiredComponentNames.add(RecipeComponentName.COURSE);
        requiredComponentNames.add(RecipeComponentName.IDENTITY);
        requiredComponentNames.add(RecipeComponentName.PORTIONS);
    }

    public static final Set<RecipeComponentName> additionalComponentNames = new HashSet<>();
    static {
        additionalComponentNames.add(RecipeComponentName.DURATION);
    }

    /*
    RecipeState.INVALID_DEFAULT - Represents the default state of the RecipeMetadata use case when
    a default request is sent.
    This state should never be saved. It is stored in this persistence model as it's a
    convenient place to retrieve values for testing.
     */
    public static RecipeMetadataUseCasePersistenceModel getDefaultState() {
        return new RecipeMetadataUseCasePersistenceModel.Builder().
                setDataId(NO_ID).
                setDomainId(NO_ID).
                setComponentState(ComponentState.INVALID_DEFAULT).
                setComponentStates(new HashMap<>()). // no component state data received
                setFailReasons(Arrays.asList(
                        RecipeMetadataUseCaseFailReason.MISSING_REQUIRED_COMPONENTS,
                        CommonFailReason.DATA_UNAVAILABLE)).
                setCreateDate(0L).
                setLastUpdate(0L).
                build();
    }

    /*
    RecipeState.INVALID_DEFAULT - Represents the state of a recipe whose:
      - request has no data id and has a domain id
      - is a newly created recipe
      - has one or more components reporting INVALID_DEFAULT state.
     */
    public static RecipeMetadataUseCasePersistenceModel getInvalidDefault() {
        return new RecipeMetadataUseCasePersistenceModel.Builder().
                setDataId(NO_ID).
                setDomainId("domainId-recipeMetadata-id0").
                setComponentState(ComponentState.INVALID_DEFAULT).
                setComponentStates(new HashMap<>()).
                setFailReasons(getDefaultState().getFailReasons()).
                setCreateDate(0L).
                setLastUpdate(0L).
                build();
    }

    /*
    RecipeState.INVALID_CHANGED - Represents the state of a recipe whose:
      - the default state has changed
      - Either or:
        - one or more components are invalid
        - one or more required components are missing
    It is valid to save as the user will have been entering data to get to this state
     */
    public static RecipeMetadataUseCasePersistenceModel getInvalidMissingComponents() {
        return new RecipeMetadataUseCasePersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-id0").
                setDomainId(getInvalidDefault().getDomainId()).
                setComponentState(ComponentState.INVALID_CHANGED). // changed from invalid default
                setComponentStates(getInvalidMissingComponentsStates()).
                setFailReasons(Arrays.asList(
                        RecipeMetadataUseCaseFailReason.MISSING_REQUIRED_COMPONENTS,
                        RecipeMetadataUseCaseFailReason.INVALID_COMPONENTS
                )).
                setCreateDate(10L).
                setLastUpdate(10L).
                build();
    }

    /*
    RecipeState.INVALID_UNCHANGED - Represents the state of a recipe where:
     - all required components have reported their state
     - all required components are reporting UNCHANGED
     - all additional components are reporting DEFAULT or UNCHANGED
     - has one or more components reporting INVALID
     */
    public static RecipeMetadataUseCasePersistenceModel getInvalidUnchanged() {
        return new RecipeMetadataUseCasePersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-id1").
                setDomainId(getInvalidDefault().getDomainId()).
                setComponentState(ComponentState.INVALID_CHANGED). // changed from invalid default
                setFailReasons(Collections.singletonList(RecipeMetadataUseCaseFailReason.INVALID_COMPONENTS)).
                setComponentStates(getInvalidUnchangedComponentStates()).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();


        // todo, can't have an invalid unchanged state from a new request, only loaded data
    }

    /*
    RecipeState.INVALID_CHANGED - Represents the state of a recipe where:
    1. all required components have reported their state
    2. one or more components is reporting INVALID
    3. one or more components is reporting CHANGED
     */
    public static RecipeMetadataUseCasePersistenceModel getInvalidChanged() {
        return new RecipeMetadataUseCasePersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-Id2").
                setDomainId(getInvalidDefault().getDomainId()).
                setComponentState(ComponentState.INVALID_CHANGED).
                setFailReasons(Collections.singletonList(RecipeMetadataUseCaseFailReason.INVALID_COMPONENTS)).
                setComponentStates(getInvalidChangedComponentStates()).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    /*
    RecipeState.VALID_CHANGED - Represents the state of a recipe which:
    2. has all required components reporting VALID
    3. all additional components are reporting valid or DEFAULT
    4. At least one component is reporting CHANGED
     */
    public static RecipeMetadataUseCasePersistenceModel getValidChanged() {
        return new RecipeMetadataUseCasePersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-id5").
                setDomainId(getInvalidDefault().getDomainId()).
                setComponentState(ComponentState.VALID_CHANGED).
                setFailReasons(Collections.singletonList(CommonFailReason.NONE)).
                setComponentStates(getValidChangedComponentStates()).
                setCreateDate(40L).
                setLastUpdate(40L).
                build();
    }

    /*
    RecipeState.VALID_UNCHANGED - Represents the state of a recipe that:
     1. has been loaded from persistence
     2. has all components report their data valid
     3. remains unchanged by the current session.
     */
    public static RecipeMetadataUseCasePersistenceModel getValidUnchanged() {
        return new RecipeMetadataUseCasePersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-Id6").
                setDomainId("domainId-recipe-Id2").
                setComponentState(ComponentState.VALID_UNCHANGED).
                setFailReasons(Collections.singletonList(CommonFailReason.NONE)).
                setComponentStates(getValidUnchangedComponentStates()).
                setCreateDate(40L).
                setLastUpdate(50L).
                build();
    }

    /*
    Represents a valid recipe created using an user Id that is different from the one in the 
    current session.
     */
    public static RecipeMetadataUseCasePersistenceModel getValidFromAnotherUser() {
        return new RecipeMetadataUseCasePersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-Id7").
                setDomainId("domainId-recipe-Id20").
                setComponentState(ComponentState.VALID_UNCHANGED).
                setFailReasons(Collections.singletonList(CommonFailReason.NONE)).
                setComponentStates(getValidUnchangedComponentStates()).
                setCreateDate(60L).
                setLastUpdate(70L).
                build();
    }

    /*
    Represents an invalid recipe created using an user Id that is different from the one in the 
    current session.  
     */
    public static RecipeMetadataUseCasePersistenceModel getInvalidFromAnotherUser() {
        return new RecipeMetadataUseCasePersistenceModel.Builder().
                setDataId(getValidFromAnotherUser().getDataId()).
                setDomainId("domainId-recipe-Id21").
                setComponentState(ComponentState.INVALID_UNCHANGED).
                setFailReasons(Collections.singletonList(RecipeMetadataUseCaseFailReason.INVALID_COMPONENTS)).
                setComponentStates(getInvalidUnchangedComponentStates()).
                setCreateDate(80L).
                setLastUpdate(90L).
                build();
    }

    /*
    Represents a valid recipe that has been copied from another user to the user in the 
    current session.  
     */
    public static RecipeMetadataUseCasePersistenceModel getValidCopied() {
        RecipeMetadataUseCasePersistenceModel validParentFromAnotherUser = getValidFromAnotherUser();
        return new RecipeMetadataUseCasePersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-Id30").
                setDomainId("domainId-recipe-Id30").
                setComponentState(validParentFromAnotherUser.getComponentState()).
                setFailReasons(validParentFromAnotherUser.getFailReasons()).
                setComponentStates(validParentFromAnotherUser.getComponentStates()).
                setCreateDate(90L).
                setLastUpdate(100L).
                build();
    }

    /*
    Represents an invalid recipe that has been copied from another user to the user in the 
    current session.
     */
    public static RecipeMetadataUseCasePersistenceModel getInvalidCopied() {
        RecipeMetadataUseCasePersistenceModel invalidParentFromAnotherUser = getInvalidFromAnotherUser();
        return new RecipeMetadataUseCasePersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-Idd31").
                setDomainId("domainId-recipe-Id40").
                setComponentState(invalidParentFromAnotherUser.getComponentState()).
                setFailReasons(invalidParentFromAnotherUser.getFailReasons()).
                setComponentStates(invalidParentFromAnotherUser.getComponentStates()).
                setCreateDate(110L).
                setLastUpdate(120L).
                build();
    }

    // Missing required component recipe identity
    private static HashMap<RecipeComponentName, ComponentState> getInvalidMissingComponentsStates() {
        HashMap<RecipeComponentName, ComponentState> componentStates = new HashMap<>();
        componentStates.put(RecipeComponentName.COURSE, ComponentState.INVALID_UNCHANGED);
        componentStates.put(RecipeComponentName.DURATION, ComponentState.VALID_UNCHANGED);
        componentStates.put(RecipeComponentName.PORTIONS, ComponentState.INVALID_UNCHANGED);
        return componentStates;
    }

    // Default required components
    private static HashMap<RecipeComponentName, ComponentState> getDefaultRequiredComponents() {
        HashMap<RecipeComponentName, ComponentState> defaultComponentStates = new HashMap<>();
        requiredComponentNames.forEach(componentName ->
                defaultComponentStates.put(componentName, ComponentState.INVALID_UNCHANGED)
        );
        return defaultComponentStates;
    }

    private static HashMap<RecipeComponentName, ComponentState> getDefaultComponentStates() {
        HashMap<RecipeComponentName, ComponentState> defaultComponentStates = new HashMap<>();
        defaultComponentStates.put(RecipeComponentName.COURSE, ComponentState.INVALID_DEFAULT);
        defaultComponentStates.put(RecipeComponentName.IDENTITY, ComponentState.INVALID_DEFAULT);
        defaultComponentStates.put(RecipeComponentName.PORTIONS, ComponentState.VALID_DEFAULT);
        return defaultComponentStates;
    }

    private static HashMap<RecipeComponentName, ComponentState> getInvalidUnchangedComponentStates() {
        HashMap<RecipeComponentName, ComponentState> componentStates = new HashMap<>();
        componentStates.put(RecipeComponentName.COURSE, ComponentState.INVALID_UNCHANGED);
        componentStates.put(RecipeComponentName.DURATION, ComponentState.INVALID_UNCHANGED);
        componentStates.put(RecipeComponentName.IDENTITY, ComponentState.INVALID_UNCHANGED);
        componentStates.put(RecipeComponentName.PORTIONS, ComponentState.INVALID_UNCHANGED);
        return componentStates;
    }

    private static HashMap<RecipeComponentName, ComponentState> getInvalidChangedComponentStates() {
        HashMap<RecipeComponentName, ComponentState> s = new HashMap<>();
        s.put(RecipeComponentName.COURSE, ComponentState.VALID_CHANGED);
        s.put(RecipeComponentName.IDENTITY, ComponentState.VALID_UNCHANGED);
        s.put(RecipeComponentName.PORTIONS, ComponentState.VALID_DEFAULT);
        s.put(RecipeComponentName.DURATION, ComponentState.INVALID_CHANGED);
        return s;
    }

    private static HashMap<RecipeComponentName, ComponentState> getValidChangedComponentStates() {
        HashMap<RecipeComponentName, ComponentState> s = new HashMap<>();
        s.put(RecipeComponentName.COURSE, ComponentState.VALID_CHANGED);
        s.put(RecipeComponentName.DURATION, ComponentState.VALID_CHANGED);
        s.put(RecipeComponentName.IDENTITY, ComponentState.VALID_CHANGED);
        s.put(RecipeComponentName.PORTIONS, ComponentState.VALID_CHANGED);
        return s;
    }

    private static HashMap<RecipeComponentName, ComponentState> getValidUnchangedComponentStates() {
        HashMap<RecipeComponentName, ComponentState> s = new HashMap<>();
        s.put(RecipeComponentName.COURSE, ComponentState.VALID_UNCHANGED);
        s.put(RecipeComponentName.DURATION, ComponentState.VALID_UNCHANGED);
        s.put(RecipeComponentName.IDENTITY, ComponentState.VALID_UNCHANGED);
        s.put(RecipeComponentName.PORTIONS, ComponentState.VALID_UNCHANGED);
        return s;
    }

    public static List<RecipeMetadataUseCasePersistenceModel> getAll() {
        return Arrays.asList(
                getInvalidDefault(),
                getInvalidUnchanged(),
                getInvalidChanged(),
                getValidChanged(),
                getValidUnchanged(),
                getValidFromAnotherUser(),
                getInvalidFromAnotherUser(),
                getValidCopied(),
                getInvalidCopied()
        );
    }

    public static List<RecipeMetadataUseCasePersistenceModel> getAllByDomainId(String domainId) {
        List<RecipeMetadataUseCasePersistenceModel> models = new ArrayList<>();
        for (RecipeMetadataUseCasePersistenceModel m : getAll()) {
            if (domainId.equals(m.getDomainId())) {
                models.add(m);
            }
        }
        return models;
    }

    public static RecipeMetadataUseCasePersistenceModel getActiveByDomainId(String domainId) {
        long lastUpdate = 0;
        RecipeMetadataUseCasePersistenceModel model = new RecipeMetadataUseCasePersistenceModel.Builder().
                getDefault().build();
        for (RecipeMetadataUseCasePersistenceModel m : getAllByDomainId(domainId)) {
            if (lastUpdate < m.getLastUpdate()) {
                model = m;
                lastUpdate = m.getLastUpdate();
            }
        }
        return model;
    }
}
