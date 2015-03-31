package com.piotapps.blendle.utils;

import android.os.Build;

public class Utils {

    private static final String IMAGE_URL = "https://blendle.com/img/providers/%s/logo.png";

    public static String getLogoUrl(final String providerId) {
        return String.format(IMAGE_URL, providerId);
    }

    public static boolean canAnimate() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }
}
