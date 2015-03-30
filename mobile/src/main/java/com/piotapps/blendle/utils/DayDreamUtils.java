package com.piotapps.blendle.utils;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.piotapps.blendle.BaseActivity;

/**
 * TODO
 */
public class DayDreamUtils {

    // Gotta love that Android is open source :)

    /**
     * Whether screensavers are enabled. (integer value, 1 if enabled)
     */
    private static final String SCREENSAVER_ENABLED = "screensaver_enabled";

    /**
     * The user's chosen screensaver components. (string value)
     *
     * These will be launched by the PhoneWindowManager after a timeout when not on
     * battery, or upon dock insertion (if SCREENSAVER_ACTIVATE_ON_DOCK is set to 1).
     */
    private static final String SCREENSAVER_COMPONENTS = "screensaver_components";

    /**
     * Activity Action: Show Daydream settings.
     * <p>
     * In some cases, a matching Activity may not exist, so ensure you
     * safeguard against this.
     */
    private static final String ACTION_DREAM_SETTINGS = "android.settings.DREAM_SETTINGS";

    public static boolean isDayDreamEnabled(final Context c) {
        return 1 == Settings.Secure.getInt(c.getContentResolver(), SCREENSAVER_ENABLED, -1);
    }

    public static String getSelectedDayDreamClassName(final Context c) {
        String names = Settings.Secure.getString(c.getContentResolver(), SCREENSAVER_COMPONENTS);
        return names == null ? null : componentsFromString(names)[0].getClassName();
    }

    public static void startDayDreamsSettings(final BaseActivity a) throws ActivityNotFoundException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            a.startActivity(new Intent(Settings.ACTION_DREAM_SETTINGS));
        } else {
            // Running JB_MR1, fall back to constant string
            a.startActivity(new Intent(ACTION_DREAM_SETTINGS));
        }
    }

    private static ComponentName[] componentsFromString(String names) {
        String[] namesArray = names.split(",");
        ComponentName[] componentNames = new ComponentName[namesArray.length];
        for (int i = 0; i < namesArray.length; i++) {
            componentNames[i] = ComponentName.unflattenFromString(namesArray[i]);
        }
        return componentNames;
    }
}
