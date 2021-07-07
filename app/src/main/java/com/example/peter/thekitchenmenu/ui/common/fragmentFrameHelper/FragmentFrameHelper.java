package com.example.peter.thekitchenmenu.ui.common.fragmentFrameHelper;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentFrameHelper {

    private final Activity activity;
    private final FragmentFrameWrapper fragmentFrameWrapper;
    private final FragmentManager fragmentManager;

    public FragmentFrameHelper(Activity activity,
                               FragmentFrameWrapper fragmentFrameWrapper,
                               FragmentManager fragmentManager) {
        this.activity = activity;
        this.fragmentFrameWrapper = fragmentFrameWrapper;
        this.fragmentManager = fragmentManager;
    }

    public void replaceFragment(Fragment newFragment) {
        replaceFragment(newFragment, true, false);
    }

    public void replaceFragmentDoNotAddToBackStack(Fragment newFragment) {
        replaceFragment(newFragment, false, false);
    }

    public void replaceFragmentClearBackStack(Fragment newFragment) {
        replaceFragment(newFragment, false, true);
    }

    public void navigateUp() {

        // Some navigateUp calls can be "lost" if they happen after the state has been saved
        if (fragmentManager.isStateSaved()) {
            return;
        }

        Fragment currentFragment = getCurrentFragment();

        if (fragmentManager.getBackStackEntryCount() > 0) {

            // In a normal world, just popping back stack would be sufficient, but since android
            // is not normal, a call to popBackStack can leave the popped fragment on screen.
            // Therefore, we start with manual removal of the current fragment.
            // Description of the issue can be found here: https://stackoverflow.com/q/45278497/2463035
            removeCurrentFragment();

            if (fragmentManager.popBackStackImmediate()) {
                return; // navigated "up" in fragments back-stack
            }
        }

        if (HierarchicalFragment.class.isInstance(currentFragment)) {
            Fragment parentFragment =
                    ((HierarchicalFragment)currentFragment).getHierarchicalParentFragment();
            if (parentFragment != null) {
                replaceFragment(parentFragment, false, true);
                return; // navigate "up" to hierarchical parent fragment
            }
        }

        if (activity.onNavigateUp()) {
            return; // navigated "up" to hierarchical parent activity
        }

        activity.onBackPressed(); // no "up" navigation targets - just treat UP as back press
    }

    private Fragment getCurrentFragment() {
        return fragmentManager.findFragmentById(getFragmentFrameId());
    }

    private void replaceFragment(Fragment newFragment, boolean addToBackStack, boolean clearBackStack) {
        if (clearBackStack) {
            if (fragmentManager.isStateSaved()) {
                // If the state is saved we can't clear the back stack. Simply not doing this, but
                // still replacing fragment is a bad idea. Therefore we abort the entire operation.
                return;
            }
            // Remove all entries from back stack
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        FragmentTransaction ft = fragmentManager.beginTransaction();

        if (addToBackStack) {
            ft.addToBackStack(null);
        }

        // Change to a new fragment
        ft.replace(getFragmentFrameId(), newFragment, null);

        if (fragmentManager.isStateSaved()) {
            // We acknowledge the possibility of losing this transaction if the app undergoes
            // save&restore flow after it is committed.
            ft.commitAllowingStateLoss();
        } else {
            ft.commit();
        }
    }

    private void removeCurrentFragment() {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.remove(getCurrentFragment());
        ft.commit();

        // not sure it is needed; will keep it as a reminder to myself if there will be problems
        // mFragmentManager.executePendingTransactions();
    }

    private int getFragmentFrameId() {
        return fragmentFrameWrapper.getFragmentFrame().getId();
    }
}
