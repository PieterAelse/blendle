package com.piotapps.blendle.pojo;


import com.google.gson.annotations.SerializedName;
import com.piotapps.blendle.api.APIConstants;

import java.io.Serializable;

public class PopularItems {

    @SerializedName(APIConstants.KEY_LINKS)
    private Links links;

    @SerializedName(APIConstants.KEY_EMBEDDED)
    private EmbeddedList embedded;

    public Links getLinks() {
        return links;
    }

    public EmbeddedList.PopularItem[] getPopularItems() {
        return embedded.items;
    }

    public class EmbeddedList {

        @SerializedName(APIConstants.KEY_ITEMS)
        private PopularItem[] items;

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

            public int getItemIndex() {
                return embedded.manifest.getIndex();
            }

            public Links getItemLinks() {
                return embedded.manifest.getLinks();
            }

            public int getItemFormatVersion() {
                return embedded.manifest.getFormatVersion();
            }

            public String getItemId() {
                return embedded.manifest.getId();
            }

            public String getItemDate() {
                return embedded.manifest.getDate();
            }

            public String getItemProvider() {
                return embedded.manifest.getProvider();
            }

            public ItemManifest.Body[] getBodies() {
                return embedded.manifest.getBodies(); // TODO split into getHeadline, getLead etc..?
            }

            public String getHeadline() {
                return embedded.manifest.getBodies()[0].getContent(); // TODO: fixme
            }

            public ItemManifest.ImageHolder[] getImages() {
                return embedded.manifest.getImages();
            }

            public ItemManifest.ImageHolder.ImageSizes getFirstImage() {
                return embedded.manifest.getImages().length > 0 ? embedded.manifest.getImages()[0].getImages() : null;
            }

            public String getPrice() {
                return price;
            }

            public class Embedded implements Serializable {

                @SerializedName(APIConstants.KEY_MANIFEST)
                public ItemManifest manifest;

            }
        }
    }
}
