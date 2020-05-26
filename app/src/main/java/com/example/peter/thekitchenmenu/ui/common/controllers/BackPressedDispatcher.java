package com.example.peter.thekitchenmenu.ui.common.controllers;

public interface BackPressedDispatcher {

    void registerListener(BackPressedListener listener);

    void unregisterListener(BackPressedListener listener);
}
