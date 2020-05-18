package com.example.peter.thekitchenmenu.ui.common.controllers;

import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.RecipeCatalogController;

public class ControllerFactory {

    public RecipeCatalogController getRecipeCatalogController() {
        return new RecipeCatalogController();
    }
}
