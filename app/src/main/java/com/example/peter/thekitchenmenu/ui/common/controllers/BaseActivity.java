package com.example.peter.thekitchenmenu.ui.common.controllers;

import androidx.appcompat.app.AppCompatActivity;

import com.example.peter.thekitchenmenu.common.CustomApplication;
import com.example.peter.thekitchenmenu.common.dependencyinjection.ControllerCompositionRoot;

public class BaseActivity extends AppCompatActivity {

    private ControllerCompositionRoot controllerCompositionRoot;

    protected ControllerCompositionRoot getCompositionRoot() {
        if (controllerCompositionRoot == null) {
            controllerCompositionRoot = new ControllerCompositionRoot(
                    ((CustomApplication) getApplication()).getCompositionRoot(),
                    this
            );
        }
        return controllerCompositionRoot;
    }
}
