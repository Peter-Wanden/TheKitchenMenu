package com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc;

import androidx.viewpager.widget.ViewPager;

import com.example.peter.thekitchenmenu.ui.catalog.recipe.RecipeNavigator;
import com.example.peter.thekitchenmenu.ui.common.views.ObservableViewMvc;

public interface RecipeCatalogView extends
        ObservableViewMvc<RecipeNavigator> {

    ViewPager getViewPager();
}
