package com.piotapps.blendle.api;

import android.support.annotation.NonNull;

import com.piotapps.blendle.interfaces.BlendleApiCallback;
import com.piotapps.blendle.pojo.PopularItem;

/**
 * Usage of the {@link BlendleApiTask} for retrieving a single {@link PopularItem}
 */
public class GetOneItemTask extends BlendleApiTask<PopularItem> {

    public GetOneItemTask(@NonNull BlendleApiCallback callback) {
        super(callback, PopularItem.class);
    }

    @Override
    protected void onPostExecute(PopularItem popularItem) {
        // Super checks for errors
        if (super.checkPostExecute(popularItem)) {
            callback.endedSingleItem(popularItem);
        }
    }
}
