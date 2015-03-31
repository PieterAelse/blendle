package com.piotapps.blendle.pojo;

import com.google.gson.annotations.SerializedName;
import com.piotapps.blendle.api.APIConstants;

import java.io.Serializable;

public class Api {

    @SerializedName(APIConstants.KEY_LINKS)
    private Links links;

    public String getPopularItemsUrl() {
        return links.popularItems.value;
    }

    public ProviderConfiguration[] getProviderConfigurations() {
        return embedded.providerConfigurations.embedded.providerConfigurations;
    }

    private class Links implements Serializable {

        @SerializedName(APIConstants.PAGE_POPULAR_ITEMS)
        private Href popularItems;

        private class Href implements Serializable {
            @SerializedName(APIConstants.KEY_HREF)
            String value;
        }
    }

    @SerializedName(APIConstants.KEY_EMBEDDED)
    private EmbeddedList embedded;

    public class EmbeddedList {

        @SerializedName(APIConstants.KEY_PROVIDER_CONFIGURATIONS)
        private ProviderConfigurations providerConfigurations;

        public class ProviderConfigurations implements Serializable {

            @SerializedName(APIConstants.KEY_EMBEDDED)
            private Embedded embedded;

            public class Embedded implements Serializable {

                @SerializedName(APIConstants.KEY_CONFIGURATIONS)
                public ProviderConfiguration[] providerConfigurations;

            }
        }
    }
}
