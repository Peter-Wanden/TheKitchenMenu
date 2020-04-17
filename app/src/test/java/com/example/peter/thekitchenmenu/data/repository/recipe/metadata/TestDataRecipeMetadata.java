package com.example.peter.thekitchenmenu.data.repository.recipe.metadata;

import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.componentstate.RecipeComponentStateEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.failreason.RecipeFailReasonEntity;
import com.example.peter.thekitchenmenu.data.repository.source.local.recipe.metadata.datasource.parent.RecipeMetadataParentEntity;
import com.example.peter.thekitchenmenu.domain.model.CommonFailReason;
import com.example.peter.thekitchenmenu.domain.model.FailReasons;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadataPersistenceModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.peter.thekitchenmenu.domain.usecase.recipe.metadata.RecipeMetadata.*;

/**
 * Persistence works by storing the state of an object each time it changes. Therefore there can be
 * many copies of an object with the same domain id, each distinguished by a unique data id.
 * In the case of recipe metadata, the ACTIVE model is the most recent according to lastUpdate()
 */
public class TestDataRecipeMetadata {

    /*
    RecipeState.DATA_UNAVAILABLE - Represents the state of a recipe that:
    1. is newly created
    2. has one or more components reporting data unavailable (as it has not been entered yet).
     */
    public static RecipeMetadataPersistenceModel getDataUnavailablePersistentModel() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-Id0").
                setDomainId("domainId-recipe-Id0").
                setParentDomainId("").
                setRecipeState(RecipeState.DATA_UNAVAILABLE).
                setComponentStates(getDataUnavailableComponentStates()).
                setFailReasons(getDataUnavailableFailReasons()).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(10L).
                setLastUpdate(10L).
                build();
    }

    public static RecipeMetadataParentEntity getDataUnavailableParentEntity() {
        return getParentEntityFromPersistenceModel(getDataUnavailablePersistentModel());
    }

    public static List<RecipeFailReasonEntity> getDataUnavailableFailReasonEntities() {
        return getFailReasonEntitiesFromPersistentModel(getDataUnavailablePersistentModel());
    }

    public static List<RecipeComponentStateEntity> getDataUnavailableComponentStateEntities() {
        return getComponentStateEntitiesFromPersistentModel(getDataUnavailablePersistentModel());
    }

    /*
    RecipeState.INVALID_UNCHANGED - Represents the state of a recipe that:
     1. has all required components
     2. has one or more components reporting invalid unchanged data
     3. remains unchanged by the current session
     */
    public static RecipeMetadataPersistenceModel getInvalidUnchangedPersistentModel() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId("dataId-recipeMetadata-Id1").
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

    public static RecipeMetadataParentEntity getInvalidUnchangedParentEntity() {
        return getParentEntityFromPersistenceModel(getInvalidUnchangedPersistentModel());
    }

    public static List<RecipeFailReasonEntity> getInvalidUnchangedFailReasonEntities() {
        return getFailReasonEntitiesFromPersistentModel(getValidUnchanged());
    }

    public static List<RecipeComponentStateEntity> getInvalidUnchangedComponentStateEntities() {
        return getComponentStateEntitiesFromPersistentModel(
                getInvalidUnchangedPersistentModel());
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

    public static RecipeMetadataParentEntity getInvalidChangedParentEntity() {
        return getParentEntityFromPersistenceModel(getInvalidChanged());
    }

    public static List<RecipeFailReasonEntity> getInvalidChangedFailReasonEntities() {
        return getFailReasonEntitiesFromPersistentModel(getInvalidChanged());
    }

    public static List<RecipeComponentStateEntity> getInvalidChangedComponentStateEntities() {
        return getComponentStateEntitiesFromPersistentModel(getInvalidChanged());
    }

    /*
    RecipeState.VALID_CHANGED - Represents the state of a recipe that:
    1. is newly created
    2. has all required components
    3. all completed components are reporting valid data.
     */
    public static RecipeMetadataPersistenceModel getValidChanged0() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId(getDataUnavailablePersistentModel().getDataId()).
                setDomainId(getDataUnavailablePersistentModel().getDomainId()).
                setParentDomainId(getDataUnavailablePersistentModel().getParentDomainId()).
                setRecipeState(RecipeState.VALID_CHANGED).
                setFailReasons(getFailReasonsNone()).
                setComponentStates(getValidChangedComponentStates()).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(40L).
                setLastUpdate(60L).
                build();
    }

    public static RecipeMetadataParentEntity getValidChanged0ParentEntity() {
        return getParentEntityFromPersistenceModel(getValidChanged0());
    }

    public static List<RecipeFailReasonEntity> getValidChanged0FailReasonEntities() {
        return getFailReasonEntitiesFromPersistentModel(getValidChanged0());
    }

    public static List<RecipeComponentStateEntity> getValidChanged0ComponentStateEntities() {
        return getComponentStateEntitiesFromPersistentModel(getValidChanged0());
    }

    // getValidChanged() previous state
    public static RecipeMetadataPersistenceModel getValidChanged1() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId(getDataUnavailablePersistentModel().getDataId()).
                setDomainId(getDataUnavailablePersistentModel().getDomainId()).
                setParentDomainId(getDataUnavailablePersistentModel().getParentDomainId()).
                setRecipeState(RecipeState.VALID_CHANGED).
                setFailReasons(getFailReasonsNone()).
                setComponentStates(getValidChangedComponentStates()).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(40L).
                setLastUpdate(50L).
                build();
    }

    public static RecipeMetadataParentEntity getValidChanged1ParentEntity() {
        return getParentEntityFromPersistenceModel(getValidChanged1());
    }

    public static List<RecipeFailReasonEntity> getValidChanged1FailReasonEntities() {
        return getFailReasonEntitiesFromPersistentModel(getValidChanged1());
    }

    public static List<RecipeComponentStateEntity> getValidChanged1ComponentStateEntities() {
        return getComponentStateEntitiesFromPersistentModel(getValidChanged1());
    }

    // getValidChanged() previous state
    public static RecipeMetadataPersistenceModel getValidChanged2() {
        return new RecipeMetadataPersistenceModel.Builder().
                setDataId(getDataUnavailablePersistentModel().getDataId()).
                setDomainId(getDataUnavailablePersistentModel().getDomainId()).
                setParentDomainId(getDataUnavailablePersistentModel().getParentDomainId()).
                setRecipeState(RecipeState.VALID_CHANGED).
                setFailReasons(getFailReasonsNone()).
                setComponentStates(getValidChangedComponentStates()).
                setCreatedBy(Constants.getUserId()).
                setCreateDate(40L).
                setLastUpdate(40L).
                build();
    }

    public static RecipeMetadataParentEntity getValidChanged2ParentEntity() {
        return getParentEntityFromPersistenceModel(getValidChanged2());
    }

    public static List<RecipeFailReasonEntity> getValidChanged2ParentEntities() {
        return getFailReasonEntitiesFromPersistentModel(getValidChanged2());
    }

    public static List<RecipeComponentStateEntity> getValidChangedComponentStateEntities() {
        return getComponentStateEntitiesFromPersistentModel(getValidChanged2());
    }

    // getValidChanged() previous states
    public static List<RecipeMetadataPersistenceModel> getValidChangedList() {
        List<RecipeMetadataPersistenceModel> models = new ArrayList<>();
        models.add(0, getValidChanged0());
        models.add(1, getValidChanged1());
        models.add(2, getValidChanged2());
        return models;
    }

    public static List<RecipeMetadataParentEntity> getValidChangedParentEntityList() {
        List<RecipeMetadataParentEntity> entities = new ArrayList<>();
        entities.add(0, getValidChanged0ParentEntity());
        entities.add(1, getValidChanged1ParentEntity());
        entities.add(2, getValidChanged2ParentEntity());
        return entities;
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

    public static RecipeMetadataParentEntity getValidUnchangedParentEntity() {
        return getParentEntityFromPersistenceModel(getValidUnchanged());
    }

    public static List<RecipeFailReasonEntity> getValidUnchangedFailReasonEntities() {
        return getFailReasonEntitiesFromPersistentModel(getValidUnchanged());
    }

    public static List<RecipeComponentStateEntity> getValidUnchangedComponentStateEntities() {
        return getComponentStateEntitiesFromPersistentModel(getValidUnchanged());
    }

    /*
    Represents a valid recipe created using an user Id that is different from the one in the 
    current session.
     */
    private static RecipeMetadataPersistenceModel getValidFromAnotherUser() {
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

    public static RecipeMetadataParentEntity getValidFromAnotherUserParentEntity() {
        return getParentEntityFromPersistenceModel(getValidFromAnotherUser());
    }

    public static List<RecipeFailReasonEntity> getValidFromAnotherUserFailReasonEntities() {
        return getFailReasonEntitiesFromPersistentModel(getValidFromAnotherUser());
    }

    public static List<RecipeComponentStateEntity>
    getValidFromAnotherUserComponentStateEntities() {
        return getComponentStateEntitiesFromPersistentModel(getValidFromAnotherUser());
    }

    /*
    Represents an invalid recipe created using an user Id that is different from the one in the 
    current session.  
     */
    private static RecipeMetadataPersistenceModel getInvalidFromAnotherUser() {
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

    public static RecipeMetadataParentEntity getInvalidFromAnotherUserParentEntity() {
        return getParentEntityFromPersistenceModel(getInvalidFromAnotherUser());
    }

    public static List<RecipeFailReasonEntity> getInvalidFromAnotherUserFailReasonEntities() {
        return getFailReasonEntitiesFromPersistentModel(getInvalidFromAnotherUser());
    }

    public static List<RecipeComponentStateEntity>
    getInvalidFromAnotherUserComponentStateEntities() {
        return getComponentStateEntitiesFromPersistentModel(getInvalidFromAnotherUser());
    }

    /*
    Represents a valid recipe that has been copied from another user to the user in the 
    current session.  
     */
    private static RecipeMetadataPersistenceModel getValidCopied() {
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

    public static RecipeMetadataParentEntity getValidCopiedParentEntity() {
        return getParentEntityFromPersistenceModel(getValidCopied());
    }

    public static List<RecipeFailReasonEntity> getValidCopiedFailReasonEntities() {
        return getFailReasonEntitiesFromPersistentModel(getValidCopied());
    }

    public static List<RecipeComponentStateEntity> getValidCopiedComponentStateEntities() {
        return getComponentStateEntitiesFromPersistentModel(getValidCopied());
    }

    /*
    Represents an invalid recipe that has been copied from another user to the user in the 
    current session.
     */
    private static RecipeMetadataPersistenceModel getInvalidCopied() {
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

    public static RecipeMetadataParentEntity getInvalidCopiedParentEntity() {
        return getParentEntityFromPersistenceModel(getInvalidCopied());
    }

    public static List<RecipeFailReasonEntity> getInvalidCopiedFailReasonEntities() {
        return getFailReasonEntitiesFromPersistentModel(getInvalidCopied());
    }

    public static List<RecipeComponentStateEntity> getInvalidCopiedComponentStateEntities() {
        return getComponentStateEntitiesFromPersistentModel(getInvalidCopied());
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

    public static List<RecipeFailReasonEntity> getInvalidComponentsFailReasonsEntities(
            String parentDataId) {
        List<RecipeFailReasonEntity> e = new ArrayList<>();
        int dataId = 0;
        for (FailReasons f : getInvalidComponentsFailReasons()) {
            e.add(new RecipeFailReasonEntity(
                            String.valueOf(dataId),
                            parentDataId,
                            f.getId()
                    )
            );
            dataId++;
        }
        return e;
    }

    private static List<FailReasons> getFailReasonsNone() {
        List<FailReasons> f = new ArrayList<>();
        f.add(CommonFailReason.NONE);
        return f;
    }

    private static List<RecipeFailReasonEntity> getFailReasonsNoneEntities(String parentDataId) {
        List<RecipeFailReasonEntity> e = new ArrayList<>();
        int dataId = 0;
        for (FailReasons f : getFailReasonsNone()) {
            e.add(new RecipeFailReasonEntity(
                            String.valueOf(dataId),
                            parentDataId,
                            f.getId()
                    )
            );
            dataId++;
        }
        return e;
    }

    private static HashMap<ComponentName, ComponentState> getDataUnavailableComponentStates() {
        HashMap<ComponentName, ComponentState> s = new HashMap<>();
        s.put(ComponentName.COURSE, ComponentState.DATA_UNAVAILABLE);
        s.put(ComponentName.DURATION, ComponentState.DATA_UNAVAILABLE);
        s.put(ComponentName.IDENTITY, ComponentState.DATA_UNAVAILABLE);
        s.put(ComponentName.PORTIONS, ComponentState.DATA_UNAVAILABLE);
        return s;
    }

    public static List<RecipeComponentStateEntity> getDataUnavailableComponentStateEntities(
            String parentDataId) {
        return getComponentStateEntities(parentDataId, getDataUnavailableComponentStates());
    }

    private static HashMap<ComponentName, ComponentState> getInvalidUnchangedComponentStates() {
        HashMap<ComponentName, ComponentState> s = new HashMap<>();
        s.put(ComponentName.COURSE, ComponentState.INVALID_UNCHANGED);
        s.put(ComponentName.DURATION, ComponentState.INVALID_UNCHANGED);
        s.put(ComponentName.IDENTITY, ComponentState.INVALID_UNCHANGED);
        s.put(ComponentName.PORTIONS, ComponentState.INVALID_UNCHANGED);
        return s;
    }

    public static List<RecipeComponentStateEntity> getInvalidUnchangedComponentStateEntities(
            String parentDataId) {
        return getComponentStateEntities(parentDataId, getInvalidUnchangedComponentStates());
    }

    private static HashMap<ComponentName, ComponentState> getInvalidChangedComponentStates() {
        HashMap<ComponentName, ComponentState> s = new HashMap<>();
        s.put(ComponentName.COURSE, ComponentState.INVALID_CHANGED);
        s.put(ComponentName.DURATION, ComponentState.INVALID_CHANGED);
        s.put(ComponentName.IDENTITY, ComponentState.INVALID_CHANGED);
        s.put(ComponentName.PORTIONS, ComponentState.INVALID_CHANGED);
        return s;
    }

    public static List<RecipeComponentStateEntity> getInvalidChangedComponentStateEntities(
            String parentDataId) {
        return getComponentStateEntities(parentDataId, getInvalidChangedComponentStates());
    }

    private static HashMap<ComponentName, ComponentState> getValidChangedComponentStates() {
        HashMap<ComponentName, ComponentState> s = new HashMap<>();
        s.put(ComponentName.COURSE, ComponentState.VALID_CHANGED);
        s.put(ComponentName.DURATION, ComponentState.VALID_CHANGED);
        s.put(ComponentName.IDENTITY, ComponentState.VALID_CHANGED);
        s.put(ComponentName.PORTIONS, ComponentState.VALID_CHANGED);
        return s;
    }

    public static List<RecipeComponentStateEntity> getValidChangedComponentStateEntities(
            String parentDataId) {
        return getComponentStateEntities(parentDataId, getValidChangedComponentStates());
    }

    private static HashMap<ComponentName, ComponentState> getValidUnchangedComponentStates() {
        HashMap<ComponentName, ComponentState> s = new HashMap<>();
        s.put(ComponentName.COURSE, ComponentState.VALID_UNCHANGED);
        s.put(ComponentName.DURATION, ComponentState.VALID_UNCHANGED);
        s.put(ComponentName.IDENTITY, ComponentState.VALID_UNCHANGED);
        s.put(ComponentName.PORTIONS, ComponentState.VALID_UNCHANGED);
        return s;
    }

    private static List<RecipeComponentStateEntity> getValidUnchangedComponentStateEntities(
            String parentDataId) {
        return getComponentStateEntities(parentDataId, getValidUnchangedComponentStates());
    }

    private static List<RecipeComponentStateEntity> getComponentStateEntities(
            String parentDataId,
            HashMap<ComponentName, ComponentState> componentStates) {
        int dataId = 0;
        List<RecipeComponentStateEntity> e = new ArrayList<>();
        for (ComponentName name : componentStates.keySet()) {
            e.add(new RecipeComponentStateEntity(
                            String.valueOf(dataId),
                            parentDataId,
                            name.getId(),
                            componentStates.get(name).getId()
                    )
            );
        }
        return e;
    }

    private static RecipeMetadataParentEntity getParentEntityFromPersistenceModel(
            RecipeMetadataPersistenceModel m) {
        return new RecipeMetadataParentEntity.Builder().
                setDataId(m.getDataId()).
                setDomainId(m.getDomainId()).
                setRecipeParentDomainId(m.getParentDomainId()).
                setRecipeStateId(m.getRecipeState().getId()).
                setCreatedBy(m.getCreatedBy()).
                setCreateDate(m.getCreateDate()).
                setLastUpdate(m.getLastUpdate()).
                build();
    }

    private static List<RecipeFailReasonEntity> getFailReasonEntitiesFromPersistentModel(
            RecipeMetadataPersistenceModel m) {
        List<RecipeFailReasonEntity> e = new ArrayList<>();
        int dataId = 0;
        for (FailReasons f : m.getFailReasons()) {
            e.add(new RecipeFailReasonEntity(
                            String.valueOf(dataId),
                            m.getDataId(),
                            f.getId()
                    )
            );
            dataId++;
        }
        return e;
    }

    private static List<RecipeComponentStateEntity> getComponentStateEntitiesFromPersistentModel(
            RecipeMetadataPersistenceModel model) {
        List<RecipeComponentStateEntity> e = new ArrayList<>();
        int dataId = 0;
        for (ComponentName name : model.getComponentStates().keySet()) {
            e.add(new RecipeComponentStateEntity(
                            String.valueOf(dataId),
                            model.getDataId(),
                            name.getId(),
                            model.getComponentStates().get(name).getId()
                    )
            );
        }
        return e;
    }
}
