package com.piotapps.blendle.pojo;

import com.google.gson.annotations.SerializedName;
import com.piotapps.blendle.api.APIConstants;

import java.io.Serializable;

public class Href implements Serializable {
    @SerializedName(APIConstants.KEY_HREF)
    private String href;

    public String getHref() {
        return href;
    }
}
