package com.piotapps.blendle.pojo;

import com.google.gson.annotations.SerializedName;
import com.piotapps.blendle.api.APIConstants;

import java.io.Serializable;

public class PopularItem implements Serializable {
    @SerializedName(APIConstants.KEY_ID)
    private String id;

    @SerializedName(APIConstants.KEY_LINKS)
    private Links links;

    @SerializedName(APIConstants.KEY_EMBEDDED)
    private Embedded embedded;

    @SerializedName(APIConstants.KEY_PRICE)
    private String price;

    public String getId() {
        return id;
    }

    public Links getLinks() {
        return links;
    }

    public String getItemProvider() {
        return embedded.manifest.getProvider();
    }

    public String getHeadline1() {
        return findInBody(APIConstants.KEY_BODY_HEADLINE_1);
    }

    public String getContent() {
        return findInBody(APIConstants.KEY_BODY_PARAGRAPH);
    }

    public ItemManifest.ImageHolder.ImageSizes getFirstImage() {
        return embedded.manifest.getImages().length > 0 ? embedded.manifest.getImages()[0].getImages() : null;
    }

    public String getPrice() {
        return "\u20ac " + price.replace('.', ',');
    }

    private String findInBody(final String type) {
        for (ItemManifest.Body b : embedded.manifest.getBodies()) {
            if (b.getType().equals(type)) {
                return b.getContent();
            }
        }
        return null;
    }

    public class Embedded implements Serializable {

        @SerializedName(APIConstants.KEY_MANIFEST)
        public ItemManifest manifest;

    }
}
