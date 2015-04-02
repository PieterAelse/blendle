package com.piotapps.blendle.pojo;

import com.google.gson.annotations.SerializedName;
import com.piotapps.blendle.api.APIConstants;

import java.io.Serializable;

public class Links implements Serializable {

    @SerializedName(APIConstants.KEY_SELF)
    private Href self;

    @SerializedName(APIConstants.KEY_NEXT)
    private Href next;

    public String getSelf() {
        return self.getHref();
    }

    public String getNext() {
        return next.getHref();
    }

}
