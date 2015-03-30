package com.piotapps.blendle;

import android.graphics.PorterDuff;
import android.os.Bundle;
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

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.squareup.picasso.Picasso;

/**
 * Base activity for general functions like menu items, showing messages, logging etc..
 * <p>
 * Extends {@link ActionBarActivity} for backwards compatibility
 * </p>
 */
public class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Workaround for showing an indeterminate progressbar in the support actionbar..
        // (since this has been removed/deprecated)
        ProgressBar progressBar = new ProgressBar(this);
        progressBar.setVisibility(View.GONE);
        progressBar.setIndeterminate(true);
        // Make it dark red
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.blende_red_dark), PorterDuff.Mode.MULTIPLY);
        // Align it to the right
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, GravityCompat.END);
        // Add to actionbar as customview
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(progressBar, layoutParams);
        // Hide at start
        setSupportProgressBarIndeterminateVisibility(false);

        // Set debug flag to Picasso instance
        Picasso.with(getApplicationContext()).setIndicatorsEnabled(BuildConfig.DEBUG);
    }

    @Override
    public void setSupportProgressBarIndeterminateVisibility(boolean visible) {
        // Override default (deprecated) method to show the custom progressbar
        getSupportActionBar().getCustomView().setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_madeBy:
                // Don't do anything
                return true;
            case android.R.id.home:
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

    protected void log(final String message) {
        Log.v(BuildConfig.TAG, message);
    }

    protected void showMessage(@NonNull @StringRes final int resourceId) {
        SnackbarManager.show(Snackbar.with(this).text(resourceId).colorResource(R.color.blende_red));
    }

    // Should only be used on debug occasions :)
    protected void showMessage(@NonNull final String message) {
        SnackbarManager.show(Snackbar.with(this).text(message).colorResource(R.color.blende_red));
    }
}
