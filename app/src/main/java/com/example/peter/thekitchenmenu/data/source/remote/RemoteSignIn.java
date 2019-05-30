package com.example.peter.thekitchenmenu.data.source.remote;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;

import com.example.peter.thekitchenmenu.ui.main.MainActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.BuildConfig;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.Arrays;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.example.peter.thekitchenmenu.app.Constants.ANONYMOUS;
import static com.example.peter.thekitchenmenu.app.Constants.REQUEST_CODE_SIGN_IN;

// For more examples of sign in see:
// https://github.com/firebase/FirebaseUI-Android/tree/master/auth#sign-in
public class RemoteSignIn {

    private static final String TAG = "RemoteSignIn";
    private MainActivity mainActivity;
    private FirebaseAuth fbAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    // TODO - Implement proper sign out
    public RemoteSignIn(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        initialiseFirebase();
    }

    private void initialiseFirebase() {
        fbAuth = FirebaseAuth.getInstance();
        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        authStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null) {
                onSignedInInitialise(user.getUid());

            } else {
                Constants.getUserId().setValue(ANONYMOUS);
                mainActivity.startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(Arrays.asList(
                                        new AuthUI.IdpConfig.GoogleBuilder().build(),
                                        new AuthUI.IdpConfig.EmailBuilder().build()))
                                .build(),
                        REQUEST_CODE_SIGN_IN);
            }
        };

        // Creates a Remote Config Setting to enable developer mode.
        // Fetching configs from the server is normally limited to 5 requests per hour.
        // Enabling developer mode allows many more requests to be made per hour, so developers
        // can test different config values during development.
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        remoteConfig.setConfigSettings(configSettings);
    }

    private void onSignedInInitialise(String userUid) {
        Constants.getUserId().setValue(userUid);
    }

    public void authStateListener(boolean attachState) {
        if (authStateListener != null) {
            if (attachState) {
                fbAuth.addAuthStateListener(authStateListener);
            } else {
                fbAuth.removeAuthStateListener(authStateListener);
            }
        }
    }

    public void signInResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(mainActivity,
                        R.string.sign_in_conformation, Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(mainActivity,
                        R.string.sign_in_canceled, Toast.LENGTH_SHORT).show();
                mainActivity.finish();
            }
        }
    }

    public void signOut(Context context) {
        Log.i(TAG, "--- User signed out from remote database");
        Constants.getUserId().setValue(ANONYMOUS);
        AuthUI.getInstance().signOut(context);
    }
}
