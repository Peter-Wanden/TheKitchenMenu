package com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.metadata.RecipeMetadata.ComponentName;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeResponse;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class RecipeListItemViewImpl implements RecipeListItemView {

    @Nonnull
    private View rootView;
    @Nonnull
    private final List<RecipeListItemUserActionsListener> listeners;

    private RecipeResponse recipeResponse;
    private TextView title;
    private TextView description;
    private TextView prepTime;
    private TextView cookTime;
    private TextView totalTime;
    private ImageView recipeImage;
    private ImageView addToFavorites;
    private ImageView removeFromFavorites;

    public RecipeListItemViewImpl(LayoutInflater inflater, ViewGroup parent) {
        listeners = new ArrayList<>();

        rootView = inflater.inflate(
                R.layout.recipe_list_item,
                parent,
                false
        );
        initialiseViews();
        setViewClickListeners();
    }

    @Nonnull
    @Override
    public View getRootView() {
        return rootView;
    }

    private <T extends View> T findViewById(int id) {
        return getRootView().findViewById(id);
    }

    private void initialiseViews() {
        title = findViewById(R.id.recipe_list_item_title);
        description = findViewById(R.id.recipe_list_item_description);
        prepTime = findViewById(R.id.recipe_list_item_prep_time);
        cookTime = findViewById(R.id.recipe_list_item_cook_time);
        totalTime = findViewById(R.id.recipe_list_item_total_time);
        addToFavorites = findViewById(R.id.recipe_list_item_add_to_favorites);
        removeFromFavorites = findViewById(R.id.recipe_list_item_remove_from_favorites);
        recipeImage = findViewById(R.id.recipe_list_item_image);
    }

    private void setViewClickListeners() {
        rootView.setOnClickListener(v ->
                listeners.forEach((listener) ->
                        listener.onRecipeClicked(recipeResponse.getDomainId()))
        );
        addToFavorites.setOnClickListener(
                v -> listeners.forEach((listener) ->
                        listener.onAddToFavoritesClicked(recipeResponse.getDomainId()))
        );
        removeFromFavorites.setOnClickListener(
                v -> listeners.forEach((listener) ->
                        listener.onRemoveFromFavoritesClicked(recipeResponse.getDomainId()))
                );
    }

    @Override
    public void bindRecipe(RecipeResponse recipeResponse) {
        this.recipeResponse = recipeResponse;

        RecipeIdentityResponse identity = (RecipeIdentityResponse) recipeResponse.
                getModel().
                getComponentResponses().
                get(ComponentName.IDENTITY);
        title.setText(identity.getModel().getTitle()
        );
        description.setText(identity.getModel().getDescription()
        );

        RecipeDurationResponse duration = (RecipeDurationResponse) recipeResponse.
                getModel().
                getComponentResponses().
                get(ComponentName.DURATION);
        prepTime.setText(duration.getModel().getTotalPrepTime()
        );
        cookTime.setText(duration.getModel().getTotalCookTime()
        );
        totalTime.setText(duration.getModel().getTotalCookTime()
        );
    }

    @Override
    public void registerListener(RecipeListItemUserActionsListener listener) {
        listeners.add(listener);
    }

    @Override
    public void unregisterListener(RecipeListItemUserActionsListener listener) {
        listeners.remove(listener);
    }
}
