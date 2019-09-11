package com.example.peter.thekitchenmenu.ui.catalog.recipe;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.entity.RecipeEntity;
import com.example.peter.thekitchenmenu.databinding.RecipeListItemBinding;

import java.util.ArrayList;
import java.util.List;

public class RecipeCatalogAllRecyclerAdapter
        extends RecyclerView.Adapter<RecipeCatalogAllRecyclerAdapter.ViewHolder>
        implements Filterable {

    private static final String TAG = "tkm-CatalogAdapter";

    private final RecipeCatalogViewModel viewModel;
    private List<RecipeEntity> recipeModelList;
    private List<RecipeEntity> recipeModelListFull;

    RecipeCatalogAllRecyclerAdapter(RecipeCatalogViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /* View holder */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        RecipeListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.recipe_list_item,
                viewGroup,
                false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final RecipeEntity recipeModel = recipeModelList.get(position);
        holder.bind(recipeModel);
    }

    @Override
    public int getItemCount() {
        if (recipeModelList == null)
            return 0;
        return recipeModelList.size();
    }

    @Override
    public Filter getFilter() {
        return filterResults;
    }

    private Filter filterResults = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<RecipeEntity> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(recipeModelListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (RecipeEntity model : recipeModelListFull) {
//                    String title = model.getTitle.toLowerCase();
//
//                    if (title.contains(filterPattern)) {
//                        filteredList.add(model);
//                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            recipeModelList.clear();
            recipeModelList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    /* Getter for the current list of products */
    public List<RecipeEntity> getRecipes() {
        return recipeModelList;
    }

    void setRecipeModels(List<RecipeEntity> recipeModelList) {
        this.recipeModelList = recipeModelList;
        recipeModelListFull = new ArrayList<>(recipeModelList);
        notifyDataSetChanged();
    }

    /* Inner class for creating ViewHolders */
    class ViewHolder extends RecyclerView.ViewHolder {
        RecipeListItemBinding binding;

        RecipeItemUserActionsListener listener = new RecipeItemUserActionsListener() {
            @Override
            public void onRecipeClicked(RecipeEntity recipeEntity) {
            }

            @Override
            public void onAddToFavoritesClicked(RecipeEntity recipeEntity) {
            }

            @Override
            public void onRemoveFromFavoritesClicked(RecipeEntity recipeEntity) {
            }
        };

        ViewHolder(RecipeListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(RecipeEntity recipeModel) {
            binding.setRecipeModel(recipeModel);
            binding.setListener(listener);
            binding.executePendingBindings();
        }
    }
}




