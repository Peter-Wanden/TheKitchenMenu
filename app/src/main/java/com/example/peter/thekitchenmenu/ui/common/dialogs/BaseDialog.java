package com.example.peter.thekitchenmenu.ui.common.dialogs;

import androidx.fragment.app.DialogFragment;

import com.example.peter.thekitchenmenu.common.CustomApplication;
import com.example.peter.thekitchenmenu.common.dependencyinjection.ControllerCompositionRoot;

public abstract class BaseDialog extends DialogFragment {

    private ControllerCompositionRoot mControllerCompositionRoot;

    protected ControllerCompositionRoot getCompositionRoot() {
        if (mControllerCompositionRoot == null) {
            mControllerCompositionRoot = new ControllerCompositionRoot(
                    ((CustomApplication) requireActivity().getApplication()).getCompositionRoot(),
                    requireActivity()
            );
        }
        return mControllerCompositionRoot;
    }
}
