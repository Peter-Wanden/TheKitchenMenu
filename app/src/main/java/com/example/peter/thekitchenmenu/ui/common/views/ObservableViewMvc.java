package com.example.peter.thekitchenmenu.ui.common.views;

/**
 * Interface from which all observable views inherit.
 * @param <ListenerType> the listener interface specific to the view
 */
public interface ObservableViewMvc<ListenerType> extends ViewMvc {

    void registerListener(ListenerType listener);

    void unregisterListener(ListenerType listener);
}