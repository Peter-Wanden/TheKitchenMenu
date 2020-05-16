package com.example.peter.thekitchenmenu.utils;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import javax.annotation.Nonnull;

import static androidx.core.util.Preconditions.checkNotNull;

/**
 * Provides methods to help Activities load their UI.
 */
public class ActivityUtils {

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     *
     */
    public static void replaceFragmentInActivity(@Nonnull FragmentManager fragmentManager,
                                                 @Nonnull Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.commit();
    }

    /**
     * The {@code fragment} is added to the container view with id {@code frameId}. The operation is
     * performed by the {@code fragmentManager}.
     *
     */
    public static void replaceFragmentInActivity(@Nonnull FragmentManager fragmentManager,
                                                 @Nonnull Fragment fragment, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(fragment, tag);
        transaction.commit();
    }
}
