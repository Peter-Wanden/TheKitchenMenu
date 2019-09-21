package com.example.peter.thekitchenmenu.ui.detail.ingredient;

import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.google.android.gms.common.util.Strings;

import java.util.LinkedHashSet;
import java.util.List;

public class IngredientDuplicateChecker implements DataSource.GetAllCallback<IngredientEntity> {

    interface DuplicateCallback {
        void duplicateCheckResult(boolean isDuplicate);
    }

    private DataSource<IngredientEntity> dataSource;
    private LinkedHashSet<String> noDuplicateList = new LinkedHashSet<>();
    private DuplicateCallback callback;

    private String nameToCheck;

    public IngredientDuplicateChecker(DataSource<IngredientEntity> dataSource) {
        this.dataSource = dataSource;
    }

    void checkForDuplicateAndNotify(String nameToCheck, DuplicateCallback callback) {
        if (!Strings.isEmptyOrWhitespace(nameToCheck)) {
            this.callback = callback;
            this.nameToCheck = nameToCheck;
            dataSource.getAll(this);
        }
    }

    @Override
    public void onAllLoaded(List<IngredientEntity> entities) {
        if (!entities.isEmpty()) {
            for (IngredientEntity entity : entities)
                noDuplicateList.add(entity.getName());

            if (noDuplicateList.add(nameToCheck))
                notifyCallback(false);
            else
                notifyCallback(true);
        }
    }

    private void notifyCallback(boolean isDuplicate) {
        callback.duplicateCheckResult(isDuplicate);
    }

    @Override
    public void onDataNotAvailable() {
        notifyCallback(false);
    }
}
