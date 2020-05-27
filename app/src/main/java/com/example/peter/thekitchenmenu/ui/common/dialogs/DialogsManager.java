package com.example.peter.thekitchenmenu.ui.common.dialogs;

import android.content.Context;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.ui.common.dialogs.promptdialog.PromptDialog;

import javax.annotation.Nullable;

public class DialogsManager {

    private final Context context;
    private final FragmentManager fragmentManager;

    public DialogsManager(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    public void showUseCaseErrorDialog(@Nullable String tag) {
        DialogFragment dialogFragment = PromptDialog.newPromptDialog(
                getString(R.string.error_dialog_data_unavailable_title),
                getString(R.string.error_dialog_data_unavailable_message),
                getString(R.string.error_dialog_data_unavailable_positive_button_caption),
                getString(R.string.error_dialog_data_unavailable_negative_button_caption)
        );
        dialogFragment.show(fragmentManager, tag);
    }

    private String getString(int stringId) {
        return context.getString(stringId);
    }

    public @Nullable String getShownDialogTag() {
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment instanceof BaseDialog) {
                return fragment.getTag();
            }
        }
        return null;
    }
}
