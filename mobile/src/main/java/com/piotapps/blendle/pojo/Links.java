package com.piotapps.blendle.pojo;

import com.google.gson.annotations.SerializedName;
import com.piotapps.blendle.api.APIConstants;

import java.io.Serializable;

public class Links implements Serializable {

    @SerializedName(APIConstants.KEY_SELF)
    private Href self;

    @SerializedName(APIConstants.KEY_PREVIOUS)
    private Href previous;

    @SerializedName(APIConstants.KEY_NEXT)
    private Href next;

    @SerializedName(APIConstants.KEY_ITEM_CONTENT)
    private Href itemContent;

    public String getSelf() {
        return self.value;
    }

    public String getPrevious() {
        return previous.value;
    }

    public String getNext() {
        return next.value;
    }

    public String getItemContent() {
        return itemContent.value;
    }

    private class Href implements Serializable {
        @SerializedName(APIConstants.KEY_HREF)
        String value;
    }
}
