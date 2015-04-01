package com.piotapps.blendle;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.piotapps.blendle.fragments.PopularTodayFragment;
import com.piotapps.blendle.services.BlendleDreamService;
import com.piotapps.blendle.utils.DayDreamUtils;
import com.piotapps.blendle.utils.Utils;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Only 'add' fragment and check daydream when not rotated
        if (savedInstanceState == null) {
            showMessage(getString(R.string.message_welcome));
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragmentHolder, PopularTodayFragment.newInstance())
                    .commit();

            if (Utils.canDaydream()) {
                // Check Daydream settings after 4 seconds, that way the content should have already loaded :)
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkDayDreamSettings();
                    }
                }, 3000);
            }
        }

        checkInternetConnection();
    }

    private void checkDayDreamSettings() {
        if (DayDreamUtils.isDayDreamEnabled(getApplicationContext())) {
            // Daydreams are enabled, checked if ours is selected!
            if (DayDreamUtils.getSelectedDayDreamClassName(getApplicationContext()).equals(BlendleDreamService.class.getName())) {
                // Good job user! Blendle daydream is active. We will not bother you :)
            } else {
                // Show instructions to select Blendle
                AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
                adBuilder.setTitle(R.string.dialog_title_daydream_select).setMessage(R.string.dialog_message_daydream_select);
                adBuilder.setPositiveButton(R.string.dialog_btn_yes, openDayDreamSettingsClicked).setNegativeButton(R.string.dialog_btn_cancel, null);
                adBuilder.show();
            }
        } else {
            // Daydreams are disabled, show instructions to enable
            AlertDialog.Builder adBuilder = new AlertDialog.Builder(this);
            adBuilder.setTitle(R.string.dialog_title_daydream_enable).setMessage(R.string.dialog_message_daydream_enable);
            adBuilder.setPositiveButton(R.string.dialog_btn_yes, openDayDreamSettingsClicked).setNegativeButton(R.string.dialog_btn_no, null);
            adBuilder.show();
        }
    }

    private final DialogInterface.OnClickListener openDayDreamSettingsClicked = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            try {
                DayDreamUtils.startDayDreamsSettings(MainActivity.this);
                Toast hint = Toast.makeText(getApplicationContext(), R.string.message_daydream_hint, Toast.LENGTH_LONG);
                final View hintView = hint.getView();
                hintView.setBackgroundColor(getResources().getColor(R.color.blende_red));
                if (hintView instanceof TextView) {
                    ((TextView) hintView).setTextColor(getResources().getColor(android.R.color.white));
                }
                hint.show();
            } catch (ActivityNotFoundException e) {
                showMessage(getString(R.string.error_daydream_startsettings));
            }
        }
    };
}
