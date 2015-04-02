package com.piotapps.blendle.api;

import android.support.annotation.NonNull;

import com.piotapps.blendle.interfaces.BlendleApiCallback;
import com.piotapps.blendle.pojo.PopularItems;

/**
 * Usage of the {@link BlendleApiTask} for retrieving the list of {@link PopularItems}.
 */
public class GetItemsTask extends BlendleApiTask<PopularItems> {

    public GetItemsTask(@NonNull BlendleApiCallback callback) {
        super(callback, PopularItems.class);
    }

    @Override
    protected void onPostExecute(PopularItems popularItems) {
        // Super checks for errors
        if (super.checkPostExecute(popularItems)) {
            callback.endedItemList(popularItems);
        }
    }
}
