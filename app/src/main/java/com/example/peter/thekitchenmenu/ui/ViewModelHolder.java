package com.example.peter.thekitchenmenu.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ViewModelHolder<VM> extends Fragment {

    private VM viewModel;
    public ViewModelHolder(){}

    public static <M> ViewModelHolder createContainer(@NonNull M viewModel) {
        ViewModelHolder<M> viewModelContainer = new ViewModelHolder<>();
        viewModelContainer.setViewModel(viewModel);
        return viewModelContainer;
    }

    @Nullable
    public VM getViewModel() {
        return viewModel;
    }

    public void setViewModel(@NonNull VM viewModel) {
        this.viewModel = viewModel;
    }
}
