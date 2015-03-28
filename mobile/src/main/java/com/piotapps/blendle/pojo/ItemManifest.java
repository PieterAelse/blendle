package com.piotapps.blendle.pojo;

import com.google.gson.annotations.SerializedName;
import com.piotapps.blendle.api.APIConstants;

import java.io.Serializable;

public class ItemManifest implements Serializable {

    @SerializedName(APIConstants.KEY_ITEM_INDEX)
    private int index;

    @SerializedName(APIConstants.KEY_LINKS)
    private Links links;

    @SerializedName(APIConstants.KEY_FORMAT_VERSION)
    private int formatVersion;

    @SerializedName(APIConstants.KEY_ID)
    private String id;

    @SerializedName(APIConstants.KEY_DATE)
    private String date; // TODO: make Date object using GsonBuilder().setDateFormat or a deserializer

    @SerializedName(APIConstants.KEY_PROVIDER)
    private Provider provider;

    @SerializedName(APIConstants.KEY_BODY)
    private Body[] bodies;

    @SerializedName(APIConstants.KEY_IMAGES)
    private ImageHolder[] images;

    public int getIndex() {
        return index;
    }

    public Links getLinks() {
        return links;
    }

    public int getFormatVersion() {
        return formatVersion;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
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

    public ImageHolder.ImageSizes getFirstImage() {
        return images[0].images;
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
            private Image small;

            @SerializedName(APIConstants.KEY_IMAGE_MEDIUM)
            private Image medium;

            @SerializedName(APIConstants.KEY_IMAGE_LARGE)
            private Image large;

            @SerializedName(APIConstants.KEY_IMAGE_ORIGINAL)
            private Image original;

            public String getUrlSmall() {
                return small.href;
            }

            public String getUrlMedium() {
                return medium.href;
            }

            public String getUrlLarge() {
                return large.href;
            }

            public String getUrlOriginal() {
                return original.href;
            }

            private class Image implements Serializable {
                @SerializedName(APIConstants.KEY_HREF)
                String href;

                @SerializedName(APIConstants.KEY_IMAGE_WIDTH)
                int width;

                @SerializedName(APIConstants.KEY_IMAGE_HEIGHT)
                int height;
            }
        }
    }

    // TODO: add?
    // length: words: value
    // issue : id: value
}
