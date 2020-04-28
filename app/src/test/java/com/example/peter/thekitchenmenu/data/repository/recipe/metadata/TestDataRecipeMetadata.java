package com.example.peter.thekitchenmenu.data.repository.recipe.metadata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.*;

/**
 * Persistence stores the state of an object (data model) each time it changes. Therefore there can
 * be many copies of an object with the same domain id, each distinguished by a unique data id.
 * In the case of recipe metadata, the ACTIVE model is the most recent according to lastUpdate()
 */
public class TestDataRecipeMetadata {

    /*
    RecipeState.DATA_UNAVAILABLE - Represents the state of a recipe that:
    1. is newly created
    2. has one or more components reporting data unavailable (as it has not been entered yet).
     */
    public static RecipeMetadataPersistenceModel getDataUnavailable() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-id0").
                setDomainId("domainId-recipe-id0").
                setParentDomainId("").
                setRecipeState(RecipeState.DATA_UNAVAILABLE).
                setComponentStates(getDataUnavailableComponentStates()).
                setFailReasons(getDataUnavailableFailReasons()).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(10L).
                setLastUpdate(10L).
                build();
    }

    /*
    RecipeState.INVALID_UNCHANGED - Represents the state of a recipe that:
     1. has all required components
     2. has one or more components reporting invalid unchanged data
     3. remains unchanged by the current session
     */
    public static RecipeMetadataPersistenceModel getInvalidUnchangedPersistentModel() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-id1").
                setDomainId("domainId-recipe-Id1").
                setParentDomainId("").
                setRecipeState(RecipeState.INVALID_UNCHANGED).
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
        RecipeMetadataPersistenceModel validModel = getValidChanged0();
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-Id2").
                setDomainId(validModel.getDomainId()).
                setParentDomainId(validModel.getParentDomainId()).
                setRecipeState(RecipeState.INVALID_CHANGED).
                setFailReasons(getInvalidComponentsFailReasons()).
                setComponentStates(getInvalidChangedComponentStates()).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(30L).
                setLastUpdate(30L).
                build();
    }

    /*
    RecipeState.VALID_CHANGED - Represents the state of a recipe that:
    1. is newly created
    2. has all required components
    3. all completed components are reporting valid data.
     */
    public static RecipeMetadataPersistenceModel getValidChanged0() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId(getDataUnavailable().getDataId()).
                setDomainId(getDataUnavailable().getDomainId()).
                setParentDomainId(getDataUnavailable().getParentDomainId()).
                setRecipeState(RecipeState.VALID_CHANGED).
                setFailReasons(getFailReasonsNone()).
                setComponentStates(getValidChangedComponentStates()).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(40L).
                setLastUpdate(60L).
                build();
    }

    // getValidChanged() previous state
    public static RecipeMetadataPersistenceModel getValidChanged1() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId(getDataUnavailable().getDataId()).
                setDomainId(getDataUnavailable().getDomainId()).
                setParentDomainId(getDataUnavailable().getParentDomainId()).
                setRecipeState(RecipeState.VALID_CHANGED).
                setFailReasons(getFailReasonsNone()).
                setComponentStates(getValidChangedComponentStates()).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(40L).
                setLastUpdate(50L).
                build();
    }

    // getValidChanged() previous state
    public static RecipeMetadataPersistenceModel getValidChanged2() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId(getDataUnavailable().getDataId()).
                setDomainId(getDataUnavailable().getDomainId()).
                setParentDomainId(getDataUnavailable().getParentDomainId()).
                setRecipeState(RecipeState.VALID_CHANGED).
                setFailReasons(getFailReasonsNone()).
                setComponentStates(getValidChangedComponentStates()).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(40L).
                setLastUpdate(40L).
                build();
    }

    // getValidChanged() previous states
    public static List<RecipeMetadataPersistenceModel> getValidChangedList() {
        List<RecipeMetadataPersistenceModel> models = new ArrayList<>();
        models.add(0, getValidChanged0());
        models.add(1, getValidChanged1());
        models.add(2, getValidChanged2());
        return models;
    }

    /*
    RecipeState.VALID_UNCHANGED - Represents the state of a recipe that:
     1. has been loaded from persistence
     2. has all components report their data valid
     3. remains unchanged by the current session.
     */
    public static RecipeMetadataPersistenceModel getValidUnchanged() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-Id3").
                setDomainId("domainId-recipe-Id2").
                setParentDomainId("").
                setRecipeState(RecipeState.VALID_UNCHANGED).
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
                setDataId("dataId-recipeMetadata-Id4").
                setDomainId("domainId-recipe-Id20").
                setParentDomainId("").
                setRecipeState(RecipeState.VALID_UNCHANGED).
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
                setRecipeState(RecipeState.INVALID_UNCHANGED).
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
                setDataId("dataId-recipeMetadata-Id5").
                setDomainId("dataId-recipeMetadata-Id3").
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
                setDataId("dataId-recipeMetadata-Idd6").
                setDomainId("domainId-recipeMetadata-Id4").
                setParentDomainId(invalidParentFromAnotherUser.getDomainId()).
                setRecipeState(invalidParentFromAnotherUser.getRecipeState()).
                setFailReasons(invalidParentFromAnotherUser.getFailReasons()).
                setComponentStates(invalidParentFromAnotherUser.getComponentStates()).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(110L).
                setLastUpdate(120L).
                build();
    }

    // TODO - Valid and invalid parent from same user!!
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

    private static HashMap<ComponentName, ComponentState> getDataUnavailableComponentStates() {
        HashMap<ComponentName, ComponentState> s = new HashMap<>();
        s.put(ComponentName.COURSE, ComponentState.DATA_UNAVAILABLE);
        s.put(ComponentName.DURATION, ComponentState.DATA_UNAVAILABLE);
        s.put(ComponentName.IDENTITY, ComponentState.DATA_UNAVAILABLE);
        s.put(ComponentName.PORTIONS, ComponentState.DATA_UNAVAILABLE);
        return s;
    }

    private static HashMap<ComponentName, ComponentState> getInvalidUnchangedComponentStates() {
        HashMap<ComponentName, ComponentState> s = new HashMap<>();
        s.put(ComponentName.COURSE, ComponentState.INVALID_UNCHANGED);
        s.put(ComponentName.DURATION, ComponentState.INVALID_UNCHANGED);
        s.put(ComponentName.IDENTITY, ComponentState.INVALID_UNCHANGED);
        s.put(ComponentName.PORTIONS, ComponentState.INVALID_UNCHANGED);
        return s;
    }

    private static HashMap<ComponentName, ComponentState> getInvalidChangedComponentStates() {
        HashMap<ComponentName, ComponentState> s = new HashMap<>();
        s.put(ComponentName.COURSE, ComponentState.INVALID_CHANGED);
        s.put(ComponentName.DURATION, ComponentState.INVALID_CHANGED);
        s.put(ComponentName.IDENTITY, ComponentState.INVALID_CHANGED);
        s.put(ComponentName.PORTIONS, ComponentState.INVALID_CHANGED);
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
                getDataUnavailable(),
                getInvalidUnchangedPersistentModel(),
                getInvalidChanged(),
                getValidChanged0(),
                getValidChanged1(),
                getValidChanged2(),
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
}
