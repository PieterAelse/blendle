package com.piotapps.blendle;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

/**
 * Base activity for general functions like menu items, showing messages, logging etc..
 * <p>
 * Extends {@link ActionBarActivity} for backwards comparability
 * </p>
 */
public class BaseActivity extends ActionBarActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void showMessage(@NonNull @StringRes final int resourceId) {
        SnackbarManager.show(Snackbar.with(this).text(resourceId).colorResource(R.color.blende_red));
    }

    // Should only be used on debug occasions :)
    protected void showMessage(@NonNull final String message) {
        SnackbarManager.show(Snackbar.with(this).text(message).colorResource(R.color.blende_red));
    }
}
