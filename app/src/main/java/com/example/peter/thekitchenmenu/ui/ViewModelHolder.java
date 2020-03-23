package com.example.peter.thekitchenmenu.ui;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import javax.annotation.Nonnull;

public class ViewModelHolder<VM> extends Fragment {

    private VM viewModel;
    public ViewModelHolder(){}

    public static <M> ViewModelHolder createContainer(@Nonnull M viewModel) {
        ViewModelHolder<M> viewModelContainer = new ViewModelHolder<>();
        viewModelContainer.setViewModel(viewModel);
        return viewModelContainer;
    }

    @Nullable
    public VM getViewModel() {
        return viewModel;
    }

    public void setViewModel(@Nonnull VM viewModel) {
        this.viewModel = viewModel;
    }
}
