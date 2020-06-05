package com.example.peter.thekitchenmenu.domain.usecase.ingredient;

import com.example.peter.thekitchenmenu.data.repository.DomainDataAccess;
import com.example.peter.thekitchenmenu.data.repository.ingredient.RepositoryIngredient;
import com.google.android.gms.common.util.Strings;

import java.util.LinkedHashMap;
import java.util.List;
// TODO - Modify, database get where name = name, instead of getAll()
public class IngredientDuplicateChecker
        implements DomainDataAccess.GetAllDomainModelsCallback<IngredientPersistenceModel> {

    public interface DuplicateCallback {
        void duplicateCheckResult(String duplicateId);
    }

    private RepositoryIngredient repository;
    private LinkedHashMap<String, String> existingIngredients;
    private DuplicateCallback callback;
    private String keyToCheck;
    private String ingredientId;
    public static final String NO_DUPLICATE_FOUND = "";

    public IngredientDuplicateChecker(RepositoryIngredient repository) {
        this.repository = repository;
    }

    public void checkForDuplicateAndNotify(String nameToCheck,
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
    public void onAllDomainModelsLoaded(List<IngredientPersistenceModel> ingredients) {
        if (!ingredients.isEmpty()) {
            createExistingIngredientList(ingredients);
            checkForDuplicates();
        }
    }

    private void createExistingIngredientList(List<IngredientPersistenceModel> ingredients) {
        if (existingIngredients == null) {
            existingIngredients = new LinkedHashMap<>();
        }

        existingIngredients.clear();

        for (IngredientPersistenceModel ingredient : ingredients) {
            String key = makeKey(ingredient.getName());
            existingIngredients.put(key, ingredient.getDomainId());
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
    public void onDomainModelsUnavailable() {
        notifyCallback(NO_DUPLICATE_FOUND);
    }
}
