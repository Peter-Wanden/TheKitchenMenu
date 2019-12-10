package com.example.peter.thekitchenmenu.domain;

import android.app.Application;

import com.example.peter.thekitchenmenu.data.repository.DatabaseInjection;
import com.example.peter.thekitchenmenu.data.repository.RepositoryIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeDuration;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIdentity;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipeIngredient;
import com.example.peter.thekitchenmenu.data.repository.RepositoryRecipePortions;
import com.example.peter.thekitchenmenu.domain.usecase.conversionfactorstatus.UseCaseConversionFactorStatus;
import com.example.peter.thekitchenmenu.domain.usecase.recipeIdentity.UseCaseRecipeIdentity;
import com.example.peter.thekitchenmenu.domain.usecase.recipeIdentityandduration.UseCaseRecipeIdentityAndDurationList;
import com.example.peter.thekitchenmenu.domain.usecase.recipeingredientlist.UseCaseRecipeIngredientListItems;
import com.example.peter.thekitchenmenu.utils.TimeProvider;
import com.example.peter.thekitchenmenu.utils.UniqueIdProvider;
import com.example.peter.thekitchenmenu.domain.usecase.recipeportioncalculator.UseCasePortionCalculator;

public class UseCaseFactory {

    private static volatile UseCaseFactory INSTANCE;
    private final Application application;
    private final RepositoryRecipePortions portionsRepository;
    private final RepositoryRecipeIngredient recipeIngredientRepository;
    private final RepositoryIngredient ingredientRepository;
    private final RepositoryRecipeIdentity recipeIdentityRepository;
    private final RepositoryRecipeDuration recipeDurationRepository;

    private UseCaseFactory(Application application,
                           RepositoryRecipePortions portionsRepository,
                           RepositoryRecipeIngredient recipeIngredientRepository,
                           RepositoryIngredient ingredientRepository,
                           RepositoryRecipeIdentity recipeIdentityRepository,
                           RepositoryRecipeDuration recipeDurationRepository) {
        this.application = application;
        this.portionsRepository = portionsRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.ingredientRepository = ingredientRepository;
        this.recipeIdentityRepository = recipeIdentityRepository;
        this.recipeDurationRepository = recipeDurationRepository;
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
                            ),
                            DatabaseInjection.provideRecipeIdentityDataSource(
                                    application.getApplicationContext()
                            ),
                            DatabaseInjection.provideRecipeDurationDataSource(
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

    public UseCaseRecipeIngredientListItems provideRecipeIngredientListUseCase() {
        return new UseCaseRecipeIngredientListItems(
                recipeIngredientRepository,
                ingredientRepository,
                portionsRepository
        );
    }

    public UseCaseRecipeIdentityAndDurationList provideRecipeIdentityAndDurationListUseCase() {
        return new UseCaseRecipeIdentityAndDurationList(
                recipeIdentityRepository,
                recipeDurationRepository
        );
    }

    public UseCaseRecipeIdentity provideRecipeIdentityUseCase() {
        return new UseCaseRecipeIdentity(recipeIdentityRepository, new TimeProvider());
    }
}
