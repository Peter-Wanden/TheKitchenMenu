package com.example.peter.thekitchenmenu.ui;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.peter.thekitchenmenu.data.repository.DatabaseInjection;
import com.example.peter.thekitchenmenu.data.repository.ingredient.RepositoryIngredient;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseFactory;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.ui.detail.ingredient.IngredientEditorViewModel;
import com.example.peter.thekitchenmenu.ui.detail.ingredient.IngredientViewerViewModel;

import javax.annotation.Nonnull;

public class ViewModelFactoryIngredient extends ViewModelProvider.NewInstanceFactory {

    @SuppressLint("StaticFieldLeak")
    private static volatile ViewModelFactoryIngredient INSTANCE;

    private final Application application;
    private final UseCaseHandler useCaseHandler;
    private final UseCaseFactory useCaseFactory;
    private final RepositoryIngredient repositoryIngredient;

    private ViewModelFactoryIngredient(Application application,
                                       UseCaseFactory useCaseFactory,
                                       UseCaseHandler useCaseHandler,
                                       RepositoryIngredient repositoryIngredient) {
        this.application = application;
        this.useCaseFactory = useCaseFactory;
        this.useCaseHandler = useCaseHandler;
        this.repositoryIngredient = repositoryIngredient;
    }

    public static ViewModelFactoryIngredient getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactoryIngredient.class) {
                if (INSTANCE == null)
                    INSTANCE = new ViewModelFactoryIngredient(
                            application,
                            UseCaseFactory.getInstance(application),
                            UseCaseHandler.getInstance(),
                            DatabaseInjection.provideIngredientDataSource(
                                    application.getApplicationContext()
                            ));
            }
        }
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    @Override
    public <T extends ViewModel> T create(@Nonnull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(IngredientEditorViewModel.class)) {
            return (T) new IngredientEditorViewModel(
                    application.getResources(),
                    useCaseHandler,
                    useCaseFactory.getTextValidatorUseCase(),
                    useCaseFactory.getIngredientUseCase());

        } else if (modelClass.isAssignableFrom(IngredientViewerViewModel.class)) {
            return (T) new IngredientViewerViewModel(
                    repositoryIngredient
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
