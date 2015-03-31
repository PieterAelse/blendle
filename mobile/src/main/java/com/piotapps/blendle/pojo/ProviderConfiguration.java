package com.piotapps.blendle.pojo;

import com.google.gson.annotations.SerializedName;
import com.piotapps.blendle.api.APIConstants;

import java.io.Serializable;

public class ProviderConfiguration implements Serializable {

    private static final String IMAGE_URL = "https://blendle.com/img/providers/%s/logo.png";

    @SerializedName(APIConstants.KEY_ID)
    private String id;

    @SerializedName(APIConstants.KEY_NAME)
    private String name;

    @SerializedName(APIConstants.KEY_TEMPLATES)
    private Templates templates;

    public String getLogoUrl() {
        return String.format(IMAGE_URL, id);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTileTemplates() {
        return templates.tile;
    }

    public String getContentTemplates() {
        return templates.itemContent;
    }

    private class Templates implements Serializable {
        @SerializedName(APIConstants.KEY_TEMPLATE_TILE)
        private String tile;

        @SerializedName(APIConstants.KEY_TEMPLATE_ITEM_CONTENT)
        private String itemContent;
    }
}
