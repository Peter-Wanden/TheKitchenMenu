package com.example.peter.thekitchenmenu.data.repository.recipe.metadata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataPersistenceModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.peter.thekitchenmenu.domain.usecase.common.usecasemessage.UseCaseMessageModelDataId.NO_ID;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentName;

import com.example.peter.thekitchenmenu.domain.usecasenew.common.metadata.ComponentState;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.FailReason;

/**
 * Persistence stores the state of an object (data model) each time it changes. Therefore there can
 * be many copies of an object with the same domain id, each distinguished by a unique data id.
 * In the case of recipe metadata, the ACTIVE model is the most recent according to lastUpdate()
 */
public class TestDataRecipeMetadata {

    public static final String NEW_RECIPE_DOMAIN_ID = getInvalidDefault().getDomainId();

    public static final Set<ComponentName> requiredComponentNames = new HashSet<>();
    static {
        requiredComponentNames.add(ComponentName.COURSE);
        requiredComponentNames.add(ComponentName.IDENTITY);
        requiredComponentNames.add(ComponentName.PORTIONS);
    }

    public static final Set<ComponentName> additionalComponentNames = new HashSet<>();
    static {
        additionalComponentNames.add(ComponentName.DURATION);
    }

    /*
    RecipeState.INVALID_DEFAULT - Represents the default state of the RecipeMetadata use case when
    a default request is sent.
    This state should never be saved. It is stored in this persistence model as it's a
    convenient place to retrieve values for testing.
     */
    public static RecipeMetadataPersistenceModel getDefaultState() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId(NO_ID).
                setDomainId(NO_ID).
                setParentDomainId(NO_ID).
                setRecipeState(ComponentState.INVALID_DEFAULT).
                setComponentStates(new HashMap<>()). // no component state data received
                setFailReasons(Arrays.asList(
                        FailReason.MISSING_REQUIRED_COMPONENTS,
                        CommonFailReason.DATA_UNAVAILABLE)).
                setCreatedBy(Constants.getUserId()).
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
    public static RecipeMetadataPersistenceModel getInvalidDefault() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId(NO_ID).
                setDomainId("domainId-recipeMetadata-id0").
                setParentDomainId(NO_ID).
                setRecipeState(ComponentState.INVALID_DEFAULT).
                setComponentStates(new HashMap<>()).
                setFailReasons(getDefaultState().getFailReasons()).
                setCreatedBy(Constants.getUserId()).
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
    public static RecipeMetadataPersistenceModel getInvalidMissingComponents() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-id0").
                setDomainId(getInvalidDefault().getDomainId()).
                setParentDomainId(NO_ID).
                setRecipeState(ComponentState.INVALID_CHANGED). // changed from invalid default
                setComponentStates(getInvalidMissingComponentsStates()).
                setFailReasons(Arrays.asList(
                        FailReason.MISSING_REQUIRED_COMPONENTS,
                        FailReason.INVALID_COMPONENTS
                )).
                setCreatedBy(Constants.getUserId()).
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
    public static RecipeMetadataPersistenceModel getInvalidUnchanged() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-id1").
                setDomainId(getInvalidDefault().getDomainId()).
                setParentDomainId(NO_ID).
                setRecipeState(ComponentState.INVALID_CHANGED). // changed from invalid default
                setFailReasons(Collections.singletonList(FailReason.INVALID_COMPONENTS)).
                setComponentStates(getInvalidUnchangedComponentStates()).
                setCreatedBy(Constants.getUserId()).
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
    public static RecipeMetadataPersistenceModel getInvalidChanged() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-Id2").
                setDomainId(getInvalidDefault().getDomainId()).
                setParentDomainId(NO_ID).
                setRecipeState(ComponentState.INVALID_CHANGED).
                setFailReasons(Collections.singletonList(FailReason.INVALID_COMPONENTS)).
                setComponentStates(getInvalidChangedComponentStates()).
                setCreatedBy(Constants.getUserId()).
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
    public static RecipeMetadataPersistenceModel getValidChanged() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-id5").
                setDomainId(getInvalidDefault().getDomainId()).
                setParentDomainId(getInvalidDefault().getParentDomainId()).
                setRecipeState(ComponentState.VALID_CHANGED).
                setFailReasons(Collections.singletonList(CommonFailReason.NONE)).
                setComponentStates(getValidChangedComponentStates()).
                setCreatedBy(Constants.getUserId()).
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
    public static RecipeMetadataPersistenceModel getValidUnchanged() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-Id6").
                setDomainId("domainId-recipe-Id2").
                setParentDomainId("").
                setRecipeState(ComponentState.VALID_UNCHANGED).
                setFailReasons(Collections.singletonList(CommonFailReason.NONE)).
                setComponentStates(getValidUnchangedComponentStates()).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(40L).
                setLastUpdate(50L).
                build();
    }

    /*
    Represents a valid recipe created using an user Id that is different from the one in the 
    current session.
     */
    public static RecipeMetadataPersistenceModel getValidFromAnotherUser() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-Id7").
                setDomainId("domainId-recipe-Id20").
                setParentDomainId("").
                setRecipeState(ComponentState.VALID_UNCHANGED).
                setFailReasons(Collections.singletonList(CommonFailReason.NONE)).
                setComponentStates(getValidUnchangedComponentStates()).
                setCreatedBy("anotherUsersId").
                setCreateDate(60L).
                setLastUpdate(70L).
                build();
    }

    /*
    Represents an invalid recipe created using an user Id that is different from the one in the 
    current session.  
     */
    public static RecipeMetadataPersistenceModel getInvalidFromAnotherUser() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId(getValidFromAnotherUser().getDataId()).
                setDomainId("domainId-recipe-Id21").
                setParentDomainId("").
                setRecipeState(ComponentState.INVALID_UNCHANGED).
                setFailReasons(Collections.singletonList(FailReason.INVALID_COMPONENTS)).
                setComponentStates(getInvalidUnchangedComponentStates()).
                setCreatedBy(getValidFromAnotherUser().getCreatedBy()).
                setCreateDate(80L).
                setLastUpdate(90L).
                build();
    }

    /*
    Represents a valid recipe that has been copied from another user to the user in the 
    current session.  
     */
    public static RecipeMetadataPersistenceModel getValidCopied() {
        RecipeMetadataPersistenceModel validParentFromAnotherUser = getValidFromAnotherUser();
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-Id30").
                setDomainId("domainId-recipe-Id30").
                setParentDomainId(validParentFromAnotherUser.getDomainId()).
                setRecipeState(validParentFromAnotherUser.getComponentState()).
                setFailReasons(validParentFromAnotherUser.getFailReasons()).
                setComponentStates(validParentFromAnotherUser.getComponentStates()).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(90L).
                setLastUpdate(100L).
                build();
    }

    /*
    Represents an invalid recipe that has been copied from another user to the user in the 
    current session.
     */
    public static RecipeMetadataPersistenceModel getInvalidCopied() {
        RecipeMetadataPersistenceModel invalidParentFromAnotherUser = getInvalidFromAnotherUser();
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-Idd31").
                setDomainId("domainId-recipe-Id40").
                setParentDomainId(invalidParentFromAnotherUser.getDomainId()).
                setRecipeState(invalidParentFromAnotherUser.getComponentState()).
                setFailReasons(invalidParentFromAnotherUser.getFailReasons()).
                setComponentStates(invalidParentFromAnotherUser.getComponentStates()).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(110L).
                setLastUpdate(120L).
                build();
    }

    // Missing required component recipe identity
    private static HashMap<ComponentName, ComponentState> getInvalidMissingComponentsStates() {
        HashMap<ComponentName, ComponentState> componentStates = new HashMap<>();
        componentStates.put(ComponentName.COURSE, ComponentState.INVALID_UNCHANGED);
        componentStates.put(ComponentName.DURATION, ComponentState.VALID_UNCHANGED);
        componentStates.put(ComponentName.PORTIONS, ComponentState.INVALID_UNCHANGED);
        return componentStates;
    }

    // Default required components
    private static HashMap<ComponentName, ComponentState> getDefaultRequiredComponents() {
        HashMap<ComponentName, ComponentState> defaultComponentStates = new HashMap<>();
        requiredComponentNames.forEach(componentName ->
                defaultComponentStates.put(componentName, ComponentState.INVALID_UNCHANGED)
        );
        return defaultComponentStates;
    }

    private static HashMap<ComponentName, ComponentState> getDefaultComponentStates() {
        HashMap<ComponentName, ComponentState> defaultComponentStates = new HashMap<>();
        defaultComponentStates.put(ComponentName.COURSE, ComponentState.INVALID_DEFAULT);
        defaultComponentStates.put(ComponentName.IDENTITY, ComponentState.INVALID_DEFAULT);
        defaultComponentStates.put(ComponentName.PORTIONS, ComponentState.VALID_DEFAULT);
        return defaultComponentStates;
    }

    private static HashMap<ComponentName, ComponentState> getInvalidUnchangedComponentStates() {
        HashMap<ComponentName, ComponentState> componentStates = new HashMap<>();
        componentStates.put(ComponentName.COURSE, ComponentState.INVALID_UNCHANGED);
        componentStates.put(ComponentName.DURATION, ComponentState.INVALID_UNCHANGED);
        componentStates.put(ComponentName.IDENTITY, ComponentState.INVALID_UNCHANGED);
        componentStates.put(ComponentName.PORTIONS, ComponentState.INVALID_UNCHANGED);
        return componentStates;
    }

    private static HashMap<ComponentName, ComponentState> getInvalidChangedComponentStates() {
        HashMap<ComponentName, ComponentState> s = new HashMap<>();
        s.put(ComponentName.COURSE, ComponentState.VALID_CHANGED);
        s.put(ComponentName.IDENTITY, ComponentState.VALID_UNCHANGED);
        s.put(ComponentName.PORTIONS, ComponentState.VALID_DEFAULT);
        s.put(ComponentName.DURATION, ComponentState.INVALID_CHANGED);
        return s;
    }

    private static HashMap<ComponentName, ComponentState> getValidChangedComponentStates() {
        HashMap<ComponentName, ComponentState> s = new HashMap<>();
        s.put(ComponentName.COURSE, ComponentState.VALID_CHANGED);
        s.put(ComponentName.DURATION, ComponentState.VALID_CHANGED);
        s.put(ComponentName.IDENTITY, ComponentState.VALID_CHANGED);
        s.put(ComponentName.PORTIONS, ComponentState.VALID_CHANGED);
        return s;
    }

    private static HashMap<ComponentName, ComponentState> getValidUnchangedComponentStates() {
        HashMap<ComponentName, ComponentState> s = new HashMap<>();
        s.put(ComponentName.COURSE, ComponentState.VALID_UNCHANGED);
        s.put(ComponentName.DURATION, ComponentState.VALID_UNCHANGED);
        s.put(ComponentName.IDENTITY, ComponentState.VALID_UNCHANGED);
        s.put(ComponentName.PORTIONS, ComponentState.VALID_UNCHANGED);
        return s;
    }

    public static List<RecipeMetadataPersistenceModel> getAll() {
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

    public static List<RecipeMetadataPersistenceModel> getAllByDomainId(String domainId) {
        List<RecipeMetadataPersistenceModel> models = new ArrayList<>();
        for (RecipeMetadataPersistenceModel m : getAll()) {
            if (domainId.equals(m.getDomainId())) {
                models.add(m);
            }
        }
        return models;
    }

    public static RecipeMetadataPersistenceModel getActiveByDomainId(String domainId) {
        long lastUpdate = 0;
        RecipeMetadataPersistenceModel model = new RecipeMetadataPersistenceModel.Builder().
                getDefault().build();
        for (RecipeMetadataPersistenceModel m : getAllByDomainId(domainId)) {
            if (lastUpdate < m.getLastUpdate()) {
                model = m;
                lastUpdate = m.getLastUpdate();
            }
        }
        return model;
    }
}
