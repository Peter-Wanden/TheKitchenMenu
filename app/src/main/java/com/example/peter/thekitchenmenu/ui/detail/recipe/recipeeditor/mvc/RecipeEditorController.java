package com.example.peter.thekitchenmenu.ui.detail.recipe.recipeeditor.mvc;

import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.ui.common.ScreensNavigator;
import com.example.peter.thekitchenmenu.ui.common.dialogs.DialogsEventBus;
import com.example.peter.thekitchenmenu.ui.common.dialogs.DialogsManager;

public class RecipeEditorController {

    private final ScreensNavigator screensNavigator;
    private final DialogsManager dialogsManager;
    private final DialogsEventBus dialogsEventBus;
    private final Recipe recipe;


    public RecipeEditorController(ScreensNavigator screensNavigator,
                                  DialogsManager dialogsManager,
                                  DialogsEventBus dialogsEventBus,
                                  Recipe recipe) {
        this.screensNavigator = screensNavigator;
        this.dialogsManager = dialogsManager;
        this.dialogsEventBus = dialogsEventBus;
        this.recipe = recipe;
    }
}
