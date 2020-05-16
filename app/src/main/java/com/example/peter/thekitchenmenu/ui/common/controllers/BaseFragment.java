package com.example.peter.thekitchenmenu.ui.common.controllers;

import androidx.fragment.app.Fragment;

import com.example.peter.thekitchenmenu.common.dependencyinjection.ControllerCompositionRoot;

public class BaseFragment extends Fragment {

    private ControllerCompositionRoot controllerCompositionRoot;

    protected ControllerCompositionRoot getCompositionRoot() {
        if (controllerCompositionRoot == null) {
            return new ControllerCompositionRoot(requireActivity());
        }
        return controllerCompositionRoot;
    }
}
