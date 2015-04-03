package com.piotapps.blendle.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.piotapps.blendle.api.APIConstants;

/**
 * Utility class for some common used functions and checks
 */
public class Utils {

    /**
     * Gets the URL at which the logo for the given provider is located.
     * @param providerId the id of the provider
     * @return the URL poining to the logo of the provider
     */
    public static String getLogoUrl(final String providerId) {
        return String.format(APIConstants.PROVIDER_LOGO_URL, providerId);
    }

    /**
     * Sets the given text to the given TextView as HTML text. If the text is
     * empty the view is automatically hidden.
     * @param textView the textview to display text
     * @param text the text that needs to be displayed (may be null)
     */
    public static void setTextOrHide(@NonNull final TextView textView, final String text) {
        if (text == null) {
            textView.setText("");
        } else {
            textView.setText(Html.fromHtml(text));
        }
        textView.setVisibility(text != null ? View.VISIBLE : View.GONE);
    }

    /**
     * Check the Android version of this device if it can hazz Animations.
     * ([view].animate is introduced in Android 3.1 Honeycomb (MR1; SDK level 12).
     * @return true is this device can do view.Animate() according to Android version
     */
    public static boolean canAnimate() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * Checks the device if it has an internet connection. (needs permission)
     * @param c the context used to check the connectivity
     * @return true if there's an internet connection
     */
    public static boolean hasInternetConnection(final Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
