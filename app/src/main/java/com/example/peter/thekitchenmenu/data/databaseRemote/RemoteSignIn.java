package com.example.peter.thekitchenmenu.data.databaseRemote;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.peter.thekitchenmenu.R;
import com.example.peter.thekitchenmenu.app.Constants;
import com.example.peter.thekitchenmenu.ui.catalog.ProductCatalog;

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

public class RemoteSignIn {

    private static final String TAG = RemoteSignIn.class.getSimpleName();

    private ProductCatalog mProductCatalog;

    // Authentication instance.
    private FirebaseAuth mFBAuth;

    // Authentication state listener.
    private FirebaseAuth.AuthStateListener mFBAuthStateListener;

    public RemoteSignIn(ProductCatalog productCatalog) {
        this.mProductCatalog = productCatalog;
        initialiseFirebase();
    }

    private void initialiseFirebase() {

        // Gets an instance of Firebase authentication.
        mFBAuth = FirebaseAuth.getInstance();

        // Gets a remote configuration instance.
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        // Firebase authentication listener (attached in onResume and detached in onPause).
        // From: Udacity AND Firebase.
        mFBAuthStateListener = firebaseAuth -> {
            // Find out if the user is logged in
            FirebaseUser user = firebaseAuth.getCurrentUser();

            if (user != null) {
                // Update the MutableLiveData user ID static field.
                onSignedInInitialise(user.getUid());
            } else {
                // User is signed out.
                Constants.getUserId().setValue(ANONYMOUS);
                // For more examples of sign in see:
                // https://github.com/firebase/FirebaseUI-Android/tree/master/auth#sign-in
                mProductCatalog.startActivityForResult(
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
        firebaseRemoteConfig.setConfigSettings(configSettings);
    }

    /**
     *  Called by Firebase when user is signed in.
     * @param userUid - The unique User ID issued by Google (remote database unique ID)
     */
    private void onSignedInInitialise(String userUid) {

        // Update the MutableLiveData user ID static field. Any listeners to this field will now
        // spring into action!
        Constants.getUserId().setValue(userUid);
    }

    // Adds or removes the firebase authentication state listener from the authentication instance.
    public void authStateListener(boolean attachState) {

        if (mFBAuthStateListener != null) {
            if (attachState) {
                mFBAuth.addAuthStateListener(mFBAuthStateListener);
            } else {
                mFBAuth.removeAuthStateListener(mFBAuthStateListener);
            }
        }
    }

    // Processes the sign in result from the host activity.
    public void signInResult (int requestCode, int resultCode, Intent data) {

        if (requestCode == Constants.REQUEST_CODE_SIGN_IN) {

            if (resultCode == RESULT_OK) {

                // Sign-in succeeded, set up the UI
                Toast.makeText(mProductCatalog,
                        R.string.sign_in_conformation, Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {

                // Sign in was canceled by the user, finish the activity, exit the app
                Toast.makeText(mProductCatalog,
                        R.string.sign_in_canceled, Toast.LENGTH_SHORT).show();
                mProductCatalog.finish();
            }
        }
    }

    // Handles sign out
    public void signOut(Context context) {
        Log.i(TAG, "--- User signed out from remote database");
        // User is signed out.
        Constants.getUserId().setValue(ANONYMOUS);
        AuthUI.getInstance().signOut(context);
    }
}
