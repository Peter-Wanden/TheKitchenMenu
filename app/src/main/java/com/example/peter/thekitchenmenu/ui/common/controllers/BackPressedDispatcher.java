package com.example.peter.thekitchenmenu.ui.common.controllers;

public interface BackPressedDispatcher {

    void registerBackPressedListener(BackPressedListener listener);

    void unregisterBackPressedListener(BackPressedListener listener);
}
