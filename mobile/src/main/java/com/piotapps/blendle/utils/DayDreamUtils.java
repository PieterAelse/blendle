package com.piotapps.blendle.utils;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.piotapps.blendle.BaseActivity;

/**
 * Utility class for Daydream checks and settings
 */
public class DaydreamUtils {

    /**
     * Check the Android version of this device if it can hazz Daydream.
     * (Daydream is introduced in Android 4.2 Jelly Bean (MR1; SDK level 17)
     * @return true is this device can Daydream according to Android version
     */
    public static boolean canDaydream() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    /**
     * Gets whether Daydream is enabled in the Android settings menu.
     * @param c the context to use for checking
     * @return true if Daydream is enabled
     */
    public static boolean isDaydreamEnabled(final Context c) {
        return 1 == Settings.Secure.getInt(c.getContentResolver(), SCREENSAVER_ENABLED, -1);
    }

    /**
     * Gets the classname of the selected Daydream in the Android settings menu
     * @param c the context to use for checking
     * @return the classname, as string, of the selected Daydream
     */
    public static String getSelectedDaydreamClassName(final Context c) {
        String names = Settings.Secure.getString(c.getContentResolver(), SCREENSAVER_COMPONENTS);
        return names == null ? null : componentsFromString(names)[0].getClassName();
    }

    /**
     * Starts the Daydream settings menu page of the Android settings menu
     * @param a the context to use
     * @throws ActivityNotFoundException when there's no Daydream settings page found
     */
    public static void startDaydreamsSettings(final BaseActivity a) throws ActivityNotFoundException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            a.startActivity(new Intent(Settings.ACTION_DREAM_SETTINGS));
        } else {
            // Running JB_MR1, fall back to constant string
            a.startActivity(new Intent(ACTION_DREAM_SETTINGS));
        }
    }

    // Copied from Android source code. Gets the ComponentNames for a given name
    private static ComponentName[] componentsFromString(String names) {
        String[] namesArray = names.split(",");
        ComponentName[] componentNames = new ComponentName[namesArray.length];
        for (int i = 0; i < namesArray.length; i++) {
            componentNames[i] = ComponentName.unflattenFromString(namesArray[i]);
        }
        return componentNames;
    }

    /** *******************************************
     * Gotta love that Android is open source :)
     * ********************************************/

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
}
