package com.example.peter.thekitchenmenu.ui.common.toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.ui.common.views.BaseViewMvc;

public class ToolbarView extends BaseViewMvc {

    public interface NavigateUpClickListener {
        void onNavigateUpClicked();
    }

    public interface HamburgerClickListener {
        void onHamburgerClicked();
    }

    private final TextView title;
    private final ImageButton backButton;
    private final ImageButton hamburgerButton;

    private NavigateUpClickListener mNavigateUpClickListener;
    private HamburgerClickListener mHamburgerClickListener;

    public ToolbarView(LayoutInflater inflater, ViewGroup parent) {
        setRootView(inflater.inflate(R.layout.layout_toolbar, parent, false));

        title = findViewById(R.id.txt_toolbar_title);

        hamburgerButton = findViewById(R.id.btn_hamburger);
        hamburgerButton.setOnClickListener(view -> mHamburgerClickListener.onHamburgerClicked());

        backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(view -> mNavigateUpClickListener.onNavigateUpClicked());
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void enableHamburgerButtonAndListen(HamburgerClickListener hamburgerClickListener) {
        if (mNavigateUpClickListener != null) {
            throw new RuntimeException("hamburger and up shouldn't be shown together");
        }
        mHamburgerClickListener = hamburgerClickListener;
        hamburgerButton.setVisibility(View.VISIBLE);
    }

    public void enableUpButtonAndListen(NavigateUpClickListener navigateUpClickListener) {
        if (mHamburgerClickListener != null) {
            throw new RuntimeException("hamburger and up shouldn't be shown together");
        }
        mNavigateUpClickListener = navigateUpClickListener;
        backButton.setVisibility(View.VISIBLE);
    }
}
