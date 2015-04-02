package com.piotapps.blendle.interfaces;

import com.piotapps.blendle.pojo.PopularItem;
import com.piotapps.blendle.pojo.PopularItems;

/**
 * Small interface to support all current Blendle API tasks
 */
public interface BlendleApiCallback {

    /**
     * Gets called when starting to call the API
     * <p>
     * Runs on the UI thread
     */
    void started();

    /**
     * Gets called when an error occurred and there's no useable result
     * <p>
     * Runs on the UI thread
     */
    void onError();

    /**
     * Gets called when the list of popular items has been retrieved
     * <p>
     * Runs on the UI thread
     */
    void endedItemList(final PopularItems popularItems);

    /**
     * Gets called when a single popular item has been retrieved
     * <p>
     * Runs on the UI thread
     */
    void endedSingleItem(final PopularItem popularItem);
}