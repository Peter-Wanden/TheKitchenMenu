package com.example.peter.thekitchenmenu.ui;

import androidx.appcompat.app.AppCompatActivity;

public abstract class AppCompatActivityDialogActions extends AppCompatActivity {

    public abstract void showUnsavedChangesDialog();

    public abstract void discardChanges();
}
