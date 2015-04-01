package com.piotapps.blendle.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;

public class Utils {

    private static final String IMAGE_URL = "https://blendle.com/img/providers/%s/logo.png";

    public static String getLogoUrl(final String providerId) {
        return String.format(IMAGE_URL, providerId);
    }

    public static boolean canDaydream() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean canAnimate() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasInternetConnection(final Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
