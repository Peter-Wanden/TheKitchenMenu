package com.example.peter.thekitchenmenu.ui.common.dialogs.promptdialog;

import android.app.Dialog;
import android.os.Bundle;

import com.example.peter.thekitchenmenu.ui.common.dialogs.BaseDialog;
import com.example.peter.thekitchenmenu.ui.common.dialogs.DialogsEventBus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PromptDialog
        extends
        BaseDialog
        implements
        PromptView.Listener {

    protected static final String ARG_TITLE = "ARG_TITLE";
    protected static final String ARG_MESSAGE = "ARG_MESSAGE";
    protected static final String ARG_POSITIVE_BUTTON_CAPTION = "ARG_POSITIVE_BUTTON_CAPTION";
    protected static final String ARG_NEGATIVE_BUTTON_CAPTION = "ARG_NEGATIVE_BUTTON_CAPTION";

    public static PromptDialog newPromptDialog(String title,
                                               String message,
                                               String positiveButtonCaption,
                                               String negativeButtonCaption) {
        PromptDialog promptDialog = new PromptDialog();
        Bundle args = new Bundle(4);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        args.putString(ARG_POSITIVE_BUTTON_CAPTION, positiveButtonCaption);
        args.putString(ARG_NEGATIVE_BUTTON_CAPTION, negativeButtonCaption);
        promptDialog.setArguments(args);
        return promptDialog;
    }

    private DialogsEventBus dialogsEventBus;
    private PromptView view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogsEventBus = getCompositionRoot().getDialogsEventBus();
    }

    @Nonnull
    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() == null) {
            throw new IllegalStateException("arguments mustn't be null");
        }

        view = getCompositionRoot().getViewFactory().getPromptView(null);

        view.setTitle(getArguments().getString(ARG_TITLE));
        view.setMessage(getArguments().getString(ARG_MESSAGE));
        view.setPositiveButtonCaption(getArguments().getString(ARG_POSITIVE_BUTTON_CAPTION));
        view.setNegativeButtonCaption(getArguments().getString(ARG_NEGATIVE_BUTTON_CAPTION));

        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(view.getRootView());

        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        view.registerListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        view.unregisterListener(this);
    }

    @Override
    public void onPositiveButtonClicked() {
        dismiss();
        dialogsEventBus.postEvent(new PromptDialogEvent(PromptDialogEvent.Button.POSITIVE));
    }

    @Override
    public void onNegativeButtonClicked() {
        dismiss();
        dialogsEventBus.postEvent(new PromptDialogEvent(PromptDialogEvent.Button.NEGATIVE));
    }
}
