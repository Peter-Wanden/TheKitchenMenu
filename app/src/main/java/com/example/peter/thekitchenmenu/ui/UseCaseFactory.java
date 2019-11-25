package com.example.peter.thekitchenmenu.ui;

import android.app.Application;

import com.example.peter.thekitchenmenu.data.repository.DatabaseInjection;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseConversionFactorStatus;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseRecipeIngredientList;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.domain.usecase.UseCasePortionCalculator;

public class UseCaseFactory {

    private static volatile UseCaseFactory INSTANCE;
    private final Application application;
    private final RepositoryRecipePortions portionsRepository;
    private final RepositoryRecipeIngredient recipeIngredientRepository;
    private final RepositoryIngredient ingredientRepository;

    private UseCaseFactory(Application application,
                          RepositoryRecipePortions portionsRepository,
                          RepositoryRecipeIngredient recipeIngredientRepository,
                          RepositoryIngredient ingredientRepository) {
        this.application = application;
        this.portionsRepository = portionsRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public static UseCaseFactory getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (UseCaseFactory.class) {
                if (INSTANCE == null)
                    INSTANCE = new UseCaseFactory(
                            application,
                            DatabaseInjection.provideRecipePortionsDataSource(
                                    application.getApplicationContext()
                            ),
                            DatabaseInjection.provideRecipeIngredientDataSource(
                                    application.getApplicationContext()
                            ),
                            DatabaseInjection.provideIngredientDataSource(
                                    application.getApplicationContext()
                            )
                    );
            }
        }
        return INSTANCE;
    }

    public UseCasePortionCalculator providePortionsUseCase() {
        return new UseCasePortionCalculator(
                portionsRepository,
                recipeIngredientRepository,
                ingredientRepository,
                new UniqueIdProvider(),
                new TimeProvider()
        );
    }

    public UseCaseConversionFactorStatus provideConversionFactorUseCase() {
        return new UseCaseConversionFactorStatus(
                ingredientRepository
        );
    }

    public UseCaseRecipeIngredientList provideRecipeIngredientListUseCase() {
        return new UseCaseRecipeIngredientList(
                recipeIngredientRepository,
                ingredientRepository,
                portionsRepository
        );
    }
}
