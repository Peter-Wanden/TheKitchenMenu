package com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.recipelistitem;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.duration.RecipeDurationResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecasenew.recipe.component.RecipeComponentNameName;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.RecipeUseCaseCallback;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeResponse;
import com.example.peter.thekitchenmenu.ui.common.views.BaseObservableViewMvc;

public class RecipeListItemViewImpl
        extends
        BaseObservableViewMvc<RecipeListItemView.RecipeListItemUserActions>
        implements
        RecipeListItemView {

    private UseCaseHandler handler;
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
        handler = UseCaseHandler.getInstance();
        setRootView(inflater.inflate(
                R.layout.recipe_list_item,
                parent,
                false)
        );

        initialiseViews();
        setViewClickListeners();
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
        getRootView().setOnClickListener(v ->
                getListeners().forEach((listener) ->
                        listener.onRecipeClicked(recipeResponse.getDomainId()))
        );
        addToFavorites.setOnClickListener(v ->
                getListeners().forEach((listener) ->
                        listener.onAddToFavoritesClicked(recipeResponse.getDomainId()))
        );
        removeFromFavorites.setOnClickListener(v ->
                getListeners().forEach((listener) ->
                        listener.onRemoveFromFavoritesClicked(recipeResponse.getDomainId()))
        );
    }

    @Override
    public void bindRecipe(Recipe recipe) {
        RecipeRequest request = new RecipeRequest.Builder().getDefault().build();
        handler.executeAsync(recipe, request, new RecipeUseCaseCallback<RecipeResponse>() {
            @Override
            protected void processResponse(RecipeResponse recipeResponse) {
                RecipeListItemViewImpl.this.recipeResponse = recipeResponse;
            }
        });

        RecipeIdentityResponse identity = (RecipeIdentityResponse) recipeResponse.
                getDomainModel().
                getComponentResponses().
                get(RecipeComponentNameName.IDENTITY);

        title.setText(identity.getDomainModel().getTitle());
        description.setText(identity.getDomainModel().getDescription());

        RecipeDurationResponse duration = (RecipeDurationResponse) recipeResponse.
                getDomainModel().getComponentResponses().get(RecipeComponentNameName.DURATION);

        prepTime.setText(duration.getDomainModel().getTotalPrepTime());
        cookTime.setText(duration.getDomainModel().getTotalCookTime());
        totalTime.setText(duration.getDomainModel().getTotalCookTime());
    }
}
