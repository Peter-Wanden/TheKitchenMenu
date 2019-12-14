package com.example.peter.thekitchenmenu.ui.detail.ingredient;

import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.google.android.gms.common.util.Strings;

import java.util.LinkedHashMap;
import java.util.List;

public class IngredientDuplicateChecker implements DataSource.GetAllCallback<IngredientEntity> {

    interface DuplicateCallback {
        void duplicateCheckResult(String duplicateId);
    }

    private RepositoryIngredient repository;
    private LinkedHashMap<String, String> existingIngredients;
    private DuplicateCallback callback;
    private String keyToCheck;
    private String ingredientId;
    static final String NO_DUPLICATE_FOUND = "";

    public IngredientDuplicateChecker(RepositoryIngredient repository) {
        this.repository = repository;
    }

    void checkForDuplicatesAndNotify(String nameToCheck,
                                     String ingredientId,
                                     DuplicateCallback callback) {
        if (!Strings.isEmptyOrWhitespace(nameToCheck)) {
            this.callback = callback;
            this.keyToCheck = makeKey(nameToCheck);
            this.ingredientId = ingredientId;
            repository.getAll(this);
        }
    }

    @Override
    public void onAllLoaded(List<IngredientEntity> entities) {
        if (!entities.isEmpty()) {
            createExistingIngredientList(entities);
            checkForDuplicates();
        }
    }

    private void createExistingIngredientList(List<IngredientEntity> entities) {
        if (existingIngredients == null) {
            existingIngredients = new LinkedHashMap<>();
        }

        existingIngredients.clear();

        for (IngredientEntity entity : entities) {
            String key = makeKey(entity.getName());
            existingIngredients.put(key, entity.getId());
        }
    }

    private String makeKey(String ingredientName) {
        return ingredientName.toLowerCase().trim();
    }

    private void checkForDuplicates() {
        String ingredientId = existingIngredients.get(keyToCheck);

        if (duplicateFound() && duplicateIsNotIngredientBeingEdited(ingredientId)) {
            notifyCallback(existingIngredients.get(keyToCheck));
        } else {
            notifyCallback(NO_DUPLICATE_FOUND);
        }
    }

    private boolean duplicateFound() {
        return existingIngredients.containsKey(keyToCheck);
    }

    private boolean duplicateIsNotIngredientBeingEdited(String ingredientId) {
        return !ingredientId.equals(this.ingredientId);
    }

    private void notifyCallback(String duplicateId) {
        callback.duplicateCheckResult(duplicateId);
    }

    @Override
    public void onDataNotAvailable() {
        notifyCallback(NO_DUPLICATE_FOUND);
    }
}
