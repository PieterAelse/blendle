package com.piotapps.blendle.pojo;

import com.google.gson.annotations.SerializedName;
import com.piotapps.blendle.api.APIConstants;

import java.io.Serializable;

public class ItemManifest implements Serializable {

    @SerializedName(APIConstants.KEY_ID)
    private String id;

    @SerializedName(APIConstants.KEY_PROVIDER)
    private Provider provider;

    @SerializedName(APIConstants.KEY_BODY)
    private Body[] bodies;

    @SerializedName(APIConstants.KEY_IMAGES)
    private ImageHolder[] images;

    public String getId() {
        return id;
    }

    public String getProvider() {
        return provider.value;
    }

    public Body[] getBodies() {
        return bodies;
    }

    public ImageHolder[] getImages() {
        return images;
    }

    private class Provider implements Serializable {
        @SerializedName(APIConstants.KEY_ID)
        String value;
    }

    public class Body implements Serializable {
        @SerializedName(APIConstants.KEY_TYPE)
        private String type;

        @SerializedName(APIConstants.KEY_CONTENT)
        private String content;

        public String getType() {
            return type;
        }

        public String getContent() {
            return content;
        }
    }

    public class ImageHolder implements Serializable {

        @SerializedName(APIConstants.KEY_LINKS)
        private ImageSizes images;

        public ImageSizes getImages() {
            return images;
        }

        public class ImageSizes implements Serializable {

            @SerializedName(APIConstants.KEY_IMAGE_SMALL)
            private Href small;

            @SerializedName(APIConstants.KEY_IMAGE_MEDIUM)
            private Href medium;

            @SerializedName(APIConstants.KEY_IMAGE_LARGE)
            private Href large;

            @SerializedName(APIConstants.KEY_IMAGE_ORIGINAL)
            private Href original;

            public String getUrlSmall() {
                return small.getHref();
            }

            public String getUrlMedium() {
                return medium.getHref();
            }

            public String getUrlLarge() {
                return large.getHref();
            }

            public String getUrlOriginal() {
                return original.getHref();
            }

        }
    }
}
