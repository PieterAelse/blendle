package com.piotapps.blendle;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.piotapps.blendle.utils.DaydreamUtils;
import com.piotapps.blendle.utils.Utils;
import com.squareup.picasso.Picasso;

/**
 * Base activity for general functions like menu items, showing messages, logging etc..
 * <p>
 * Extends {@link ActionBarActivity} for backwards compatibility
 * </p>
 */
public class BaseActivity extends ActionBarActivity {

    /** Used as a base delay for showing messages. This way the user won't be prompted with a dialog immediately */
    protected static final int UI_MESSAGES_DELAY = 3000;

    protected Handler delayHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get handler for delayed UI operations
        delayHandler = new Handler(Looper.getMainLooper());

        // Workaround for showing an indeterminate progressbar in the support actionbar..
        // (since this has been removed/deprecated)
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setVisibility(View.GONE);
        progressBar.setIndeterminate(true);
        // Make it dark red
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.blende_red_dark), PorterDuff.Mode.MULTIPLY);
        // Align it to the right
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.MATCH_PARENT, GravityCompat.END);
        // Add to actionbar as customview
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(progressBar, layoutParams);
        // Hide at start
        setSupportProgressBarIndeterminateVisibility(false);

        // Forward debug flag to Picasso instance
        Picasso.with(getApplicationContext()).setIndicatorsEnabled(BuildConfig.DEBUG);
    }

    @Override
    public void setSupportProgressBarIndeterminateVisibility(boolean visible) {
        // Override the default (deprecated) method to show the custom progressbar
        getSupportActionBar().getCustomView().setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem daydreamItem = menu.findItem(R.id.action_setup_daydream);
        if (daydreamItem != null) {
            daydreamItem.setVisible(DaydreamUtils.canDaydream());
        }

        // Return true to show the menu
        return true;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_madeBy:
                // Don't do anything
                return true;
            case R.id.action_setup_daydream:
                // Try to start the Daydream settings screen
                try {
                    DaydreamUtils.startDaydreamsSettings(this);
                } catch (ActivityNotFoundException e) {
                    showMessage(R.string.error_daydream_startsettings);
                }
                return true;
            case android.R.id.home:
                // Override the up/home button for best behavior
                if(!isTaskRoot()) {
                    onBackPressed();
                } else {
                    // For when started by NFC
                    NavUtils.navigateUpFromSameTask(this);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Cancel all handler stuff
        delayHandler.removeCallbacksAndMessages(null);
    }

    /**
     * Check the internet connection and if there's none shows a dialog with an error
     * @return true if there is an internet connection
     */
    protected boolean checkInternetConnection() {
        if (!Utils.hasInternetConnection(getApplicationContext())) {
            final AlertDialog.Builder adBuilder = createAlertDialog(R.string.dialog_title_no_internet, R.string.dialog_message_no_internet);
            adBuilder.setPositiveButton(R.string.dialog_btn_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            adBuilder.show();
            return false;
        }

        return true;
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
    protected void showMessage(@StringRes final int resourceId) {
        showMessage(getString(resourceId));
    }

    /**
     ** Show a message to the user using a SnackBar
     * @param message the message to show
     */
    protected void showMessage(@NonNull final String message) {
        SnackbarManager.show(Snackbar.with(this).text(message).colorResource(R.color.blende_red).swipeToDismiss(true));
    }

    /**
     * Shows a Blendle themed Toast to the user
     * @param resourceId the message to show
     */
    protected void showThemedToast(@StringRes final int resourceId, final int length) {
        Toast hint = Toast.makeText(getApplicationContext(), resourceId, length);
        final View hintView = hint.getView();
        hintView.setBackgroundColor(getResources().getColor(R.color.blende_red));
        if (hintView instanceof TextView) {
            ((TextView) hintView).setTextColor(getResources().getColor(android.R.color.white));
        }
        hint.show();
    }

    /**
     * Creates an alertdialog to show to the user.
     * @param titleId title of the dialog
     * @param messageId message of the dialog
     * @return the alertdialog to add buttons and show
     */
    protected AlertDialog.Builder createAlertDialog(@StringRes final int titleId, @StringRes final int messageId) {
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
        return adBuilder.setTitle(titleId).setMessage(messageId);
    }
}
