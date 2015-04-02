package com.piotapps.blendle.pojo;

import com.google.gson.annotations.SerializedName;
import com.piotapps.blendle.api.APIConstants;

public class PopularItems {

    @SerializedName(APIConstants.KEY_LINKS)
    private Links links;

    @SerializedName(APIConstants.KEY_EMBEDDED)
    private EmbeddedList embedded;

    public Links getLinks() {
        return links;
    }

    public PopularItem[] getPopularItems() {
        return embedded.items;
    }

    public class EmbeddedList {

        @SerializedName(APIConstants.KEY_ITEMS)
        private PopularItem[] items;

    }
}
