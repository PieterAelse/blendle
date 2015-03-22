package com.piotapps.blendle.pojo;

import com.google.gson.annotations.SerializedName;
import com.piotapps.blendle.api.APIConstants;

public class Links {

    @SerializedName(APIConstants.KEY_SELF)
    public Stringie self;

    @SerializedName(APIConstants.KEY_PREVIOUS)
    public Stringie previous;

    @SerializedName(APIConstants.KEY_NEXT)
    public Stringie next;

    private class Stringie {
        @SerializedName(APIConstants.KEY_HREF)
        String value;
    }
}
