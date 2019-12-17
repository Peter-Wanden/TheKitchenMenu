package com.example.peter.thekitchenmenu.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.peter.thekitchenmenu.R;

public class UnsavedChangesDialogFragment extends DialogFragment {

    public static final String TAG = "tkm-" + UnsavedChangesDialogFragment.class.getSimpleName() + ":";

    public static final int RESULT_CANCELED_AFTER_EDIT = 500;

    public static UnsavedChangesDialogFragment newInstance(String title) {
        UnsavedChangesDialogFragment dialogFragment = new UnsavedChangesDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        dialogFragment.setArguments(args);
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String title = getArguments().getString("title");

        Dialog alertDialog = new AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.ic_warning_primary_dark_color_24dp)
                .setTitle(title)
                .setMessage(R.string.unsaved_changes_dialog_msg)
                .setNegativeButton(R.string.keep_editing, (dialog, id) -> {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.discard, (dialogInterface, i) ->
                        ((AppCompatActivityDialogActions)getActivity()).discardChanges())
                .create();

        alertDialog.setOnShowListener(dialogInterface -> {
            // Make a small gap between the buttons
            Button keepEditingButton = ((AlertDialog) dialogInterface).getButton(
                    DialogInterface.BUTTON_NEGATIVE);

            LinearLayout.LayoutParams negativeLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            negativeLayoutParams.setMargins(0, 0, 10, 0);
            keepEditingButton.setLayoutParams(negativeLayoutParams);
        });

        return alertDialog;
    }
}
