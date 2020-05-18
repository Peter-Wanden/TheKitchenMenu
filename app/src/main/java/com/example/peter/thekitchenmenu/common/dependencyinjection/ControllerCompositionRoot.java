package com.example.peter.thekitchenmenu.common.dependencyinjection;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.peter.thekitchenmenu.domain.usecase.UseCaseFactory;
import com.example.peter.thekitchenmenu.ui.common.controllers.ControllerFactory;
import com.example.peter.thekitchenmenu.ui.common.views.ViewFactory;

public class ControllerCompositionRoot {

    private final CompositionRoot compositionRoot;
    private final FragmentActivity activity;

    public ControllerCompositionRoot(CompositionRoot compositionRoot, FragmentActivity activity) {
        this.compositionRoot = compositionRoot;
        this.activity = activity;
    }

    public FragmentActivity getActivity() {
        return activity;
    }

    public Context getContext() {
        return activity;
    }

    private FragmentManager getFragmentManager() {
        return getActivity().getSupportFragmentManager();
    }

    private LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(getContext());
    }

    public ViewFactory getViewMvcFactory() {
        return new ViewFactory(getLayoutInflater());
    }

    public UseCaseFactory getUseCaseFactory() {
        return UseCaseFactory.getInstance(getActivity().getApplication());
    }

    public ControllerFactory getControllerFactory() {
        return new ControllerFactory();
    }
}