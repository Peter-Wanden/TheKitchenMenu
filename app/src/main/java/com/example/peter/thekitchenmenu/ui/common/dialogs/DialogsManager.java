package com.example.peter.thekitchenmenu.ui.common.dialogs;

import android.content.Context;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.ui.common.dialogs.promptdialog.PromptDialog;

import javax.annotation.Nullable;

public class DialogsManager {

    private final Context mContext;
    private final FragmentManager mFragmentManager;

    public DialogsManager(Context context, FragmentManager fragmentManager) {
        mContext = context;
        mFragmentManager = fragmentManager;
    }

    public void showUseCaseErrorDialog(@Nullable String tag) {
        DialogFragment dialogFragment = PromptDialog.newPromptDialog(
                getString(R.string.error_network_call_failed_title),
                getString(R.string.error_network_call_failed_message),
                getString(R.string.error_network_call_failed_positive_button_caption),
                getString(R.string.error_network_call_failed_negative_button_caption)
        );
        dialogFragment.show(mFragmentManager, tag);
    }

    private String getString(int stringId) {
        return mContext.getString(stringId);
    }

    public @Nullable String getShownDialogTag() {
        for (Fragment fragment : mFragmentManager.getFragments()) {
            if (fragment instanceof BaseDialog) {
                return fragment.getTag();
            }
        }
        return null;
    }
}
