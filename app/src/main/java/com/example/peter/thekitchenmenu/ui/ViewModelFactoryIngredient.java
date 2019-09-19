package com.example.peter.thekitchenmenu.ui;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.peter.thekitchenmenu.data.entity.IngredientEntity;
import com.example.peter.thekitchenmenu.data.repository.DataSource;
import com.example.peter.thekitchenmenu.data.repository.DatabaseInjection;
import com.example.peter.thekitchenmenu.ui.detail.ingredient.IngredientViewModel;
import com.example.peter.thekitchenmenu.utils.TextValidationHandler;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;

public class ViewModelFactoryIngredient extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactoryIngredient INSTANCE;
    private final Application application;
    private final DataSource<IngredientEntity> ingredientEntityDataSource;

    private ViewModelFactoryIngredient(
            Application application,
            DataSource<IngredientEntity> ingredientEntityDataSource) {
        this.application = application;
        this.ingredientEntityDataSource = ingredientEntityDataSource;
    }

    public static ViewModelFactoryIngredient getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactoryIngredient.class) {
                if (INSTANCE == null)
                    INSTANCE = new ViewModelFactoryIngredient(
                            application,
                            DatabaseInjection.provideIngredientEntityDataSource(
                                    application.getApplicationContext()
                            ));
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(IngredientViewModel.class)) {
            // noinspection unchecked
            return (T) new IngredientViewModel(
                    application.getResources(),
                    ingredientEntityDataSource,
                    new TextValidationHandler(),
                    new UniqueIdProvider(),
                    new TimeProvider());
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
