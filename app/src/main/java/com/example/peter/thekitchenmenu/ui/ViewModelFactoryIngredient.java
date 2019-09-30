package com.example.peter.thekitchenmenu.ui;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.peter.thekitchenmenu.data.repository.DatabaseInjection;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.ui.detail.ingredient.IngredientDuplicateChecker;
import com.example.peter.thekitchenmenu.ui.detail.ingredient.IngredientEditorViewModel;
import com.example.peter.thekitchenmenu.ui.detail.ingredient.IngredientViewerViewModel;
import com.example.peter.thekitchenmenu.utils.TextValidationHandler;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

public class ViewModelFactoryIngredient extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactoryIngredient INSTANCE;
    private final Application application;
    private final RepositoryIngredient repositoryIngredient;

    private ViewModelFactoryIngredient(
            Application application,
            RepositoryIngredient repositoryIngredient) {
        this.application = application;
        this.repositoryIngredient = repositoryIngredient;
    }

    public static ViewModelFactoryIngredient getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactoryIngredient.class) {
                if (INSTANCE == null)
                    INSTANCE = new ViewModelFactoryIngredient(
                            application,
                            DatabaseInjection.provideIngredientDataSource(
                                    application.getApplicationContext()
                            ));
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(IngredientEditorViewModel.class)) {
            // noinspection unchecked
            return (T) new IngredientEditorViewModel(
                    application.getResources(),
                    repositoryIngredient,
                    new TextValidationHandler(),
                    new UniqueIdProvider(),
                    new TimeProvider(),
                    new IngredientDuplicateChecker(repositoryIngredient));

        } else if (modelClass.isAssignableFrom(IngredientViewerViewModel.class)) {
            // noinspection unchecked
            return (T) new IngredientViewerViewModel(
                    repositoryIngredient
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
