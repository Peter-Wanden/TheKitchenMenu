package com.example.peter.thekitchenmenu.common.dependencyinjection;

import com.example.peter.thekitchenmenu.ui.common.dialogs.DialogsEventBus;

public class CompositionRoot {

    private DialogsEventBus dialogsEventBus;

    public DialogsEventBus getDialogsEventBus() {
        if (dialogsEventBus == null) {
            dialogsEventBus = new DialogsEventBus();
        }
        return dialogsEventBus;
    }
}
