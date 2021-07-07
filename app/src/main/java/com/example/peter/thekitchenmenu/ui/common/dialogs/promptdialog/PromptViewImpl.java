package com.example.peter.thekitchenmenu.ui.common.dialogs.promptdialog;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.ui.common.views.BaseObservableViewMvc;

public class PromptViewImpl extends
        BaseObservableViewMvc<PromptView.Listener>
        implements
        PromptView {

    private TextView titleText;
    private TextView messageText;
    private AppCompatButton positiveButton;
    private AppCompatButton negativeButton;

    public PromptViewImpl(LayoutInflater inflater,
                             @Nullable ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.dialog_prompt, parent, false));

        titleText = findViewById(R.id.txt_title);
        messageText = findViewById(R.id.txt_message);
        positiveButton = findViewById(R.id.btn_positive);
        negativeButton = findViewById(R.id.btn_negative);

        positiveButton.setOnClickListener(v -> getListeners().
                forEach(Listener::onPositiveButtonClicked));

        negativeButton.setOnClickListener(v -> getListeners().forEach(Listener::onNegativeButtonClicked));
    }

    @Override
    public void setTitle(String title) {
        titleText.setText(title);
    }

    @Override
    public void setMessage(String message) {
        messageText.setText(message);
    }

    @Override
    public void setPositiveButtonCaption(String caption) {
        positiveButton.setText(caption);
    }

    @Override
    public void setNegativeButtonCaption(String caption) {
        negativeButton.setText(caption);
    }
}
