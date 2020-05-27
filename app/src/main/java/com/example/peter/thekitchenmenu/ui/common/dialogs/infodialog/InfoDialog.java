package com.example.peter.thekitchenmenu.ui.common.dialogs.infodialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.ui.common.dialogs.BaseDialog;

public class InfoDialog
        extends BaseDialog {

    protected static final String ARG_TITLE = "ARG_TITLE";
    protected static final String ARG_MESSAGE = "ARG_MESSAGE";
    protected static final String ARG_BUTTON_CAPTION = "ARG_BUTTON_CAPTION";

    public static InfoDialog newInfoDialog(String title, String message, String buttonCaption) {
        InfoDialog infoDialog = new InfoDialog();
        Bundle args = new Bundle(3);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        args.putString(ARG_BUTTON_CAPTION, buttonCaption);
        infoDialog.setArguments(args);
        return infoDialog;
    }

    private TextView titleTextView;
    private TextView messageTextView;
    private AppCompatButton positiveButton;

    @NonNull
    @Override
    public final Dialog onCreateDialog(Bundle savedInstanceState) {
        if (getArguments() == null) {
            throw new IllegalStateException("arguments mustn't be null");
        }

        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.dialog_info);

        titleTextView = dialog.findViewById(R.id.txt_title);
        messageTextView = dialog.findViewById(R.id.txt_message);
        positiveButton = dialog.findViewById(R.id.btn_positive);

        titleTextView.setText(getArguments().getString(ARG_TITLE));
        messageTextView.setText(getArguments().getString(ARG_MESSAGE));
        positiveButton.setText(getArguments().getString(ARG_BUTTON_CAPTION));

        positiveButton.setOnClickListener(v -> onButtonClicked());

        return dialog;
    }

    protected void onButtonClicked() {
        dismiss();
    }
}
