package com.example.peter.thekitchenmenu.ui.catalog.recipe;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import com.example.peter.thekitchenmenu.domain.usecase.UseCase;
import com.example.peter.thekitchenmenu.domain.usecase.UseCaseHandler;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.component.identity.RecipeIdentityResponse;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.Recipe;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeRequest;
import com.example.peter.thekitchenmenu.domain.usecase.recipe.macro.recipe.RecipeResponse;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.recipelistitem.RecipeListItemView;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.recipelistitem.RecipeListItemView.RecipeListItemUserActionsListener;
import com.example.peter.thekitchenmenu.ui.catalog.recipe.mvc.recipelistitem.RecipeListItemViewImpl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class RecipeCatalogFavoritesRecyclerAdapter
        extends
        RecyclerView.Adapter<RecipeCatalogFavoritesRecyclerAdapter.ViewHolder>
        implements
        Filterable,
        RecipeListItemView.RecipeListItemUserActionsListener {

    private static final String TAG = "tkm-" + RecipeCatalogFavoritesRecyclerAdapter.class.
            getSimpleName() + ":";

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final RecipeListItemView viewMvc;

        private ViewHolder(RecipeListItemView viewMvc) {
            super(viewMvc.getRootView());
            this.viewMvc = viewMvc;
        }
    }

    @Nonnull
    private final UseCaseHandler handler;
    private List<Recipe> recipeList = new ArrayList<>();
    private List<Recipe> recipeListFull = new ArrayList<>();
    private RecipeListItemUserActionsListener listener;

    RecipeCatalogFavoritesRecyclerAdapter(RecipeListItemUserActionsListener listener) {
        handler = UseCaseHandler.getInstance();
        this.listener = listener;
    }

    void bindRecipes(List<Recipe> recipes) {
        recipeList = new ArrayList<>(recipes);
        recipeListFull = new ArrayList<>(recipes);
        notifyDataSetChanged();
    }

    @Nonnull
    @Override
    public ViewHolder onCreateViewHolder(@Nonnull ViewGroup parent, int viewType) {

        RecipeListItemView viewMvc = new RecipeListItemViewImpl(
                LayoutInflater.from(parent.getContext()), parent);
        viewMvc.registerListener(this);

        return new ViewHolder(viewMvc);
    }

    @Override
    public void onBindViewHolder(@Nonnull ViewHolder holder, int position) {
        final Recipe recipe = recipeList.get(position);

        RecipeRequest request = new RecipeRequest.Builder().getDefault().build();
        handler.execute(recipe, request, new UseCase.Callback<RecipeResponse>() {
            @Override
            public void onSuccess(RecipeResponse response) {
                holder.viewMvc.bindRecipe(response);
            }

            @Override
            public void onError(RecipeResponse response) {
                holder.viewMvc.bindRecipe(response);
            }
        });
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
                    handler.execute(recipe, request, new UseCase.Callback<RecipeIdentityResponse>() {
                        @Override
                        public void onSuccess(RecipeIdentityResponse response) {
                            title = response.getModel().getTitle();
                            addRecipe(recipe);
                        }

                        @Override
                        public void onError(RecipeIdentityResponse response) {
                            title = response.getModel().getTitle();
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
