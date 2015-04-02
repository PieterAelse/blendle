package com.piotapps.blendle.fragments;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.piotapps.blendle.BuildConfig;
import com.piotapps.blendle.R;

import butterknife.ButterKnife;

/**
 * A fragment containing some base functions like logging, showing messages/dialogs etc.
 */
public class BaseFragment extends Fragment {

    protected Handler uiHandler;

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Reset views for ButterKnife.
        // Do in try/catch just in case fragment doesn't use ButterKnife.
        try {
            ButterKnife.reset(this);
        } catch (Exception e) {
            // Ignore
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Get a handler on the UI thread to post messages to
        uiHandler = new Handler(Looper.getMainLooper());

        // Don't contribute to the optionsmenu
        setHasOptionsMenu(false);
    }

    /**
     * Add a message to the log
     * @param message message to add
     */
    protected void log(final String message) {
        Log.v(BuildConfig.TAG, message);
    }

    /**
     * Show a message to the user using a SnackBar
     * @param resourceId the message to show
     */
    protected void showMessage(@NonNull @StringRes final int resourceId) {
        showMessage(getString(resourceId));
    }

    /**
     ** Show a message to the user using a SnackBar
     * @param message the message to show
     */
    protected void showMessage(@NonNull final String message) {
        SnackbarManager.show(Snackbar.with(getActivity()).text(message).colorResource(R.color.blende_red).swipeToDismiss(true));
    }
}
