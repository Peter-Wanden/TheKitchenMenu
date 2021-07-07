package com.example.peter.thekitchenmenu.ui.common.views;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Extended by all observable views in the application
 * @param <ListenerType> the listener interface specific to the view
 */
public abstract class BaseObservableViewMvc<ListenerType>
        extends
        BaseViewMvc
        implements
        ObservableViewMvc<ListenerType> {

    private Set<ListenerType> mListeners = new HashSet<>();

    @Override
    public final void registerListener(ListenerType listener) {
        mListeners.add(listener);
    }

    @Override
    public final void unregisterListener(ListenerType listener) {
        mListeners.remove(listener);
    }

    protected final Set<ListenerType> getListeners() {
        return Collections.unmodifiableSet(mListeners);
    }
}
