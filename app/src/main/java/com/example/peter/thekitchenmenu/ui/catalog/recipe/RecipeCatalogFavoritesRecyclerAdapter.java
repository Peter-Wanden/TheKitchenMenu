package com.example.peter.thekitchenmenu.ui.catalog.recipe;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.data.model.FavoriteRecipeModel;
import com.example.peter.thekitchenmenu.databinding.FavoriteRecipeListItemBinding;

import java.util.ArrayList;
import java.util.List;

public class RecipeCatalogFavoritesRecyclerAdapter
        extends RecyclerView.Adapter<RecipeCatalogFavoritesRecyclerAdapter.ViewHolder>
        implements Filterable {

    private static final String TAG = "tkm-CatalogFavAdapter";

    private final RecipeCatalogViewModel viewModel;
    private List<FavoriteRecipeModel> favoriteRecipeModels;
    private List<FavoriteRecipeModel> favoriteRecipeModelListFull;

    RecipeCatalogFavoritesRecyclerAdapter(RecipeCatalogViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        FavoriteRecipeListItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.favorite_recipe_list_item,
                viewGroup,
                false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final FavoriteRecipeModel favoriteRecipeModel = favoriteRecipeModels.get(position);
        holder.bind(favoriteRecipeModel);
    }

    @Override
    public int getItemCount() {
        if (favoriteRecipeModels == null)
            return 0;
        return favoriteRecipeModels.size();
    }

    @Override
    public Filter getFilter() {
        return filterResults;
    }

    private Filter filterResults = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<FavoriteRecipeModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(favoriteRecipeModelListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (FavoriteRecipeModel model : favoriteRecipeModelListFull) {
//                    String title = model.getTitle().toLowerCase();
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
            favoriteRecipeModels.clear();
            favoriteRecipeModels.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public List<FavoriteRecipeModel> getFavoriteRecipeModels() {
        return favoriteRecipeModels;
    }

    void setFavoriteRecipeModels(List<FavoriteRecipeModel> favoriteRecipeModels) {
        this.favoriteRecipeModels = favoriteRecipeModels;
        favoriteRecipeModelListFull = new ArrayList<>(favoriteRecipeModels);
        notifyDataSetChanged();
    }

    /* Inner class for creating ViewHolders */
    class ViewHolder extends RecyclerView.ViewHolder {
        FavoriteRecipeListItemBinding binding;

        FavoriteRecipeItemUserActionsListener listener = new FavoriteRecipeItemUserActionsListener() {
            @Override
            public void onFavoriteRecipeClicked(FavoriteRecipeModel favoriteRecipeModel) {
            }

            @Override
            public void onRemoveFromFavoritesClicked(FavoriteRecipeModel favoriteRecipeModel) {
            }
        };

        ViewHolder(FavoriteRecipeListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(FavoriteRecipeModel favoriteRecipeModel) {
            binding.setFavoriteRecipeModel(favoriteRecipeModel);
            binding.setListener(listener);
            binding.executePendingBindings();
        }
    }
}
