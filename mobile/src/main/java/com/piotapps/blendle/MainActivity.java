package com.piotapps.blendle;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.piotapps.blendle.fragments.PopularTodayFragment;
import com.piotapps.blendle.services.BlendleDreamService;
import com.piotapps.blendle.utils.DaydreamUtils;

/**
 * Main screen of this app
 * <p>
 * Shows the popular items using a {@link PopularTodayFragment}
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Only 'add' fragment and check daydream and internet when not recreated
        if (savedInstanceState == null) {
            showMessage(getString(R.string.message_welcome));

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_fragmentHolder, PopularTodayFragment.newInstance())
                    .commit();

            if (DaydreamUtils.canDaydream()) {
                // Check Daydream settings after some delay, that way the content loading is visible
                // and doesn't get interrupted by a dialog popping up. (better UX)
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkDayDreamSettings();
                    }
                }, UI_MESSAGES_DELAY);
            }

            checkInternetConnection();
        }
    }

    private void checkDayDreamSettings() {
        if (DaydreamUtils.isDaydreamEnabled(getApplicationContext())) {
            // Daydreams are enabled, checked if ours is selected!
            if (!DaydreamUtils.getSelectedDaydreamClassName(getApplicationContext()).equals(BlendleDreamService.class.getName())) {
                // Show instructions to select Blendle
                final AlertDialog.Builder adBuilder = createAlertDialog(R.string.dialog_title_daydream_select, R.string.dialog_message_daydream_select);
                adBuilder.setPositiveButton(R.string.dialog_btn_yes, openDayDreamSettingsClicked).setNegativeButton(R.string.dialog_btn_cancel, null);
                adBuilder.show();
            } // Else : Good job user! Blendle daydream is active. We will not bother you :)
        } else {
            // Daydreams are disabled, show instructions to enable
            final AlertDialog.Builder adBuilder = createAlertDialog(R.string.dialog_title_daydream_enable, R.string.dialog_message_daydream_enable);
            adBuilder.setPositiveButton(R.string.dialog_btn_yes, openDayDreamSettingsClicked).setNegativeButton(R.string.dialog_btn_no, null);
            adBuilder.show();
        }
    }

    // OnClickListener which is used in both Daydream popups to start the Daydream settings screen and show a hint on how to enable Daydream.
    private final DialogInterface.OnClickListener openDayDreamSettingsClicked = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            try {
                DaydreamUtils.startDaydreamsSettings(MainActivity.this);
                showThemedToast(R.string.message_daydream_hint, Toast.LENGTH_LONG);
            } catch (ActivityNotFoundException e) {
                showMessage(getString(R.string.error_daydream_startsettings));
            }
        }
    };
}
