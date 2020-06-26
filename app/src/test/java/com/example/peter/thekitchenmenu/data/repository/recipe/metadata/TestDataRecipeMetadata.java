package com.example.peter.thekitchenmenu.data.repository.recipe.metadata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.usecase.common.failreasons.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadataPersistenceModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentName;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentState;
import static com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.FailReason;

/**
 * Persistence stores the state of an object (data model) each time it changes. Therefore there can
 * be many copies of an object with the same domain id, each distinguished by a unique data id.
 * In the case of recipe metadata, the ACTIVE model is the most recent according to lastUpdate()
 */
public class TestDataRecipeMetadata {

    public static final String NEW_RECIPE_DOMAIN_ID = getDataUnavailable().getDomainId();

    /*
    RecipeState.DATA_UNAVAILABLE - Represents the state of a recipe that:
    1. is newly created
    2. has one or more components reporting data unavailable (as it may have not been entered yet).
     */
    public static RecipeMetadataPersistenceModel getDataUnavailable() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-id0").
                setDomainId("domainId-recipe-id0").
                setParentDomainId("").
                setRecipeState(ComponentState.INVALID_CHANGED). // changed as it prompts a new save
                setComponentStates(getInvalidUnchangedComponentStates()).
                setFailReasons(getDataUnavailableFailReasons()).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(0L).
                setLastUpdate(0L).
                build();
    }

    /*
    RecipeState.INVALID_UNCHANGED - Represents the state of a recipe that:
     1. has all required components
     2. has one or more components reporting invalid unchanged data
     3. remains unchanged by the current session
     */
    public static RecipeMetadataPersistenceModel getInvalidUnchanged() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-id1").
                setDomainId("domainId-recipe-Id1").
                setParentDomainId("").
                setRecipeState(ComponentState.INVALID_UNCHANGED).
                setFailReasons(getInvalidComponentsFailReasons()).
                setComponentStates(getInvalidUnchangedComponentStates()).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(20L).
                setLastUpdate(20L).
                build();

    }

    /*
    RecipeState.INVALID_CHANGED - Represents a recipe state that:
    1. has all required components
    2. one or more components is reporting invalid data
    3. one or more components data has been changed in the current session
     */
    public static RecipeMetadataPersistenceModel getInvalidChanged() {
        RecipeMetadataPersistenceModel validModel = getValidChangedThree();
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-Id2").
                setDomainId(validModel.getDomainId()).
                setParentDomainId(validModel.getParentDomainId()).
                setRecipeState(ComponentState.INVALID_CHANGED).
                setFailReasons(getInvalidComponentsFailReasons()).
                setComponentStates(getInvalidChangedComponentStates()).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    /*
    RecipeState.VALID_CHANGED - Represents the state of a recipe which:
    1. is newly created
    2. has all required components
    3. all completed components are reporting valid data.
     */
    // getValidChanged() final state
    public static RecipeMetadataPersistenceModel getValidChangedThree() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-id5").
                setDomainId(getDataUnavailable().getDomainId()).
                setParentDomainId(getDataUnavailable().getParentDomainId()).
                setRecipeState(ComponentState.VALID_CHANGED).
                setFailReasons(getFailReasonsNone()).
                setComponentStates(getValidChangedComponentStates()).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(40L).
                setLastUpdate(60L).
                build();
    }

    // getValidChanged() previous state 1
    public static RecipeMetadataPersistenceModel getValidChangedTwo() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-id4").
                setDomainId(getDataUnavailable().getDomainId()).
                setParentDomainId(getDataUnavailable().getParentDomainId()).
                setRecipeState(ComponentState.INVALID_CHANGED).
                setFailReasons(getInvalidComponentsFailReasons()).
                setComponentStates(getInvalidChangedComponentStates()).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(40L).
                setLastUpdate(50L).
                build();
    }

    // getValidChanged() previous state 2
    public static RecipeMetadataPersistenceModel getValidChangedOne() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-id3").
                setDomainId(getDataUnavailable().getDomainId()).
                setParentDomainId(getDataUnavailable().getParentDomainId()).
                setRecipeState(ComponentState.INVALID_UNCHANGED).
                setFailReasons(getDataUnavailableFailReasons()).
                setComponentStates(getInvalidUnchangedComponentStates()).
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
                setFailReasons(getFailReasonsNone()).
                setComponentStates(getValidUnchangedComponentStates()).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(50L).
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
                setFailReasons(getFailReasonsNone()).
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
                setFailReasons(getInvalidComponentsFailReasons()).
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
                setRecipeState(validParentFromAnotherUser.getRecipeState()).
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
                setRecipeState(invalidParentFromAnotherUser.getRecipeState()).
                setFailReasons(invalidParentFromAnotherUser.getFailReasons()).
                setComponentStates(invalidParentFromAnotherUser.getComponentStates()).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(110L).
                setLastUpdate(120L).
                build();
    }

    private static List<FailReasons> getDataUnavailableFailReasons() {
        List<FailReasons> f = new ArrayList<>();
        f.add(CommonFailReason.DATA_UNAVAILABLE);
        return f;
    }

    private static List<FailReasons> getInvalidComponentsFailReasons() {
        List<FailReasons> f = new ArrayList<>();
        f.add(FailReason.INVALID_COMPONENTS);
        return f;
    }

    private static List<FailReasons> getFailReasonsNone() {
        List<FailReasons> f = new ArrayList<>();
        f.add(CommonFailReason.NONE);
        return f;
    }

    public static HashMap<ComponentName, ComponentState> getInvalidMissingComponentsStates() {
        HashMap<ComponentName, ComponentState> s = new HashMap<>();
        s.put(ComponentName.IDENTITY, ComponentState.VALID_UNCHANGED);
        s.put(ComponentName.DURATION, ComponentState.VALID_UNCHANGED);
        s.put(ComponentName.COURSE, ComponentState.VALID_UNCHANGED);
        return s;
    }

    public static HashMap<ComponentName, ComponentState> getInvalidUnchangedComponentStates() {
        HashMap<ComponentName, ComponentState> componentStates = new HashMap<>();
        componentStates.put(ComponentName.COURSE, ComponentState.INVALID_UNCHANGED);
        componentStates.put(ComponentName.DURATION, ComponentState.INVALID_UNCHANGED);
        componentStates.put(ComponentName.IDENTITY, ComponentState.INVALID_UNCHANGED);
        componentStates.put(ComponentName.PORTIONS, ComponentState.INVALID_UNCHANGED);
        return componentStates;
    }

    public static HashMap<ComponentName, ComponentState> getInvalidChangedComponentStates() {
        HashMap<ComponentName, ComponentState> s = new HashMap<>();
        s.put(ComponentName.COURSE, ComponentState.VALID_UNCHANGED);
        s.put(ComponentName.DURATION, ComponentState.INVALID_CHANGED);
        s.put(ComponentName.IDENTITY, ComponentState.VALID_UNCHANGED);
        s.put(ComponentName.PORTIONS, ComponentState.VALID_UNCHANGED);
        return s;
    }

    public static HashMap<ComponentName, ComponentState> getValidChangedComponentStates() {
        HashMap<ComponentName, ComponentState> s = new HashMap<>();
        s.put(ComponentName.COURSE, ComponentState.VALID_UNCHANGED);
        s.put(ComponentName.DURATION, ComponentState.VALID_CHANGED);
        s.put(ComponentName.IDENTITY, ComponentState.VALID_CHANGED);
        s.put(ComponentName.PORTIONS, ComponentState.VALID_CHANGED);
        return s;
    }

    public static HashMap<ComponentName, ComponentState> getValidUnchangedComponentStates() {
        HashMap<ComponentName, ComponentState> s = new HashMap<>();
        s.put(ComponentName.COURSE, ComponentState.VALID_UNCHANGED);
        s.put(ComponentName.DURATION, ComponentState.VALID_UNCHANGED);
        s.put(ComponentName.IDENTITY, ComponentState.VALID_UNCHANGED);
        s.put(ComponentName.PORTIONS, ComponentState.VALID_UNCHANGED);
        return s;
    }

    public static List<RecipeMetadataPersistenceModel> getAll() {
        return Arrays.asList(
                getDataUnavailable(),
                getInvalidUnchanged(),
                getInvalidChanged(),
                getValidChangedThree(),
                getValidChangedTwo(),
                getValidChangedOne(),
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
