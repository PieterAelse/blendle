package com.piotapps.blendle.pojo;


import com.google.gson.annotations.SerializedName;
import com.piotapps.blendle.api.APIConstants;

public class PopularItems {

    @SerializedName(APIConstants.KEY_LINKS)
    public Links links;
}
