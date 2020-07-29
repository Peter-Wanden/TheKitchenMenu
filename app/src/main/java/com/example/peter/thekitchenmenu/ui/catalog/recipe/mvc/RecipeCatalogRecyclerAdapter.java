package com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc;

import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import com.example.peter.thekitchenmenu.domain.usecase.common.UseCaseBase;
import com.example.peter.thekitchenmenu.domain.usecasenew.common.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.recipelistitem.RecipeListItemView;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.recipelistitem.RecipeListItemView.RecipeListItemUserActions;
import com.example.peter.thekitchenmenu.ui.common.views.ViewFactory;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class RecipeCatalogRecyclerAdapter
        extends
        RecyclerView.Adapter<RecipeCatalogRecyclerAdapter.ViewHolder>
        implements
        Filterable,
        RecipeListItemUserActions {

    private static final String TAG = "tkm-" + RecipeCatalogRecyclerAdapter.class.getSimpleName()
            + ": ";

    /* Inner class for creating ViewHolders */
    static class ViewHolder extends RecyclerView.ViewHolder {

        private final RecipeListItemView viewMvc;

        ViewHolder(RecipeListItemView viewMvc) {
            super(viewMvc.getRootView());
            this.viewMvc = viewMvc;
        }
    }

    @Nonnull
    private final UseCaseHandler handler;
    private final ViewFactory viewFactory;
    private RecipeListItemUserActions listener;

    private List<Recipe> recipeList = new ArrayList<>();
    private List<Recipe> recipeListFull = new ArrayList<>();

    RecipeCatalogRecyclerAdapter(RecipeListItemUserActions listener,
                                 ViewFactory viewFactory) {
        this.handler = UseCaseHandler.getInstance();
        this.listener = listener;
        this.viewFactory = viewFactory;
    }

    public void bindRecipes(List<Recipe> recipes) {
        recipeList = new ArrayList<>(recipes);
        recipeListFull = new ArrayList<>(recipes);
        notifyDataSetChanged();
    }

    @Nonnull
    @Override
    public ViewHolder onCreateViewHolder(@Nonnull ViewGroup parent, int viewType) {

        RecipeListItemView viewMvc = viewFactory.getRecipeListItemView(parent);
        viewMvc.registerListener(this);

        return new ViewHolder(viewMvc);
    }

    @Override
    public void onBindViewHolder(@Nonnull ViewHolder holder, int position) {
        holder.viewMvc.bindRecipe(recipeList.get(position));
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    @Override
    public Filter getFilter() {
        return filterResults;
    }

    private Filter filterResults = new Filter() {
        private String title;
        private List<Recipe> filteredList = new ArrayList<>();
        private String filterPattern;

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(recipeListFull);
            } else {
                filterPattern = constraint.toString().toLowerCase().trim();

                for (Recipe recipe : recipeListFull) {

                    RecipeIdentityRequest request = new RecipeIdentityRequest.Builder().
                            getDefault().build();
                    handler.executeAsync(recipe, request, new UseCaseBase.Callback<RecipeIdentityResponse>() {
                        @Override
                        public void onUseCaseSuccess(RecipeIdentityResponse response) {
                            title = response.getDomainModel().getTitle();
                            addRecipe(recipe);
                        }

                        @Override
                        public void onUseCaseError(RecipeIdentityResponse response) {
                            title = response.getDomainModel().getTitle();
                            addRecipe(recipe);
                        }
                    });
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        private void addRecipe(Recipe recipe) {
            if (title.toLowerCase().contains(filterPattern)) {
                filteredList.add(recipe);
            }
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            recipeList.clear();
            recipeList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public void onRecipeClicked(String recipeDomainId) {
        listener.onRecipeClicked(recipeDomainId);
    }

    @Override
    public void onAddToFavoritesClicked(String recipeDomainId) {
        listener.onAddToFavoritesClicked(recipeDomainId);
    }

    @Override
    public void onRemoveFromFavoritesClicked(String recipeDomainId) {
        listener.onRemoveFromFavoritesClicked(recipeDomainId);
    }
}