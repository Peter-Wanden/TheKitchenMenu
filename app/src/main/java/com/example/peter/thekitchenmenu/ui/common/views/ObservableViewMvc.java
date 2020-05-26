package com.example.peter.thekitchenmenu.ui.common.views;

/**
 * Interface from which all observable views inherit.
 * @param <LISTENER_TYPE> the listener interface specific to the view
 */
public interface ObservableViewMvc<LISTENER_TYPE> extends ViewMvc {

    void registerListener(LISTENER_TYPE listener);

    void unregisterListener(LISTENER_TYPE listener);
}