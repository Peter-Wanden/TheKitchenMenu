package com.example.peter.thekitchenmenu.ui.common.dialogs.promptdialog;

import com.example.peter.thekitchenmenu.ui.common.views.ObservableViewMvc;

public interface PromptView
        extends
        ObservableViewMvc<PromptView.Listener> {

    public interface Listener {
        void onPositiveButtonClicked();

        void onNegativeButtonClicked();
    }

    void setTitle(String title);

    void setMessage(String message);

    void setPositiveButtonCaption(String caption);

    void setNegativeButtonCaption(String caption);
}
