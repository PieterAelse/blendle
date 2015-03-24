package com.piotapps.blendle.api;

public class APIConstants {

    // API URL
    public static final String URL_ALL = "https://static.blendle.nl/api.json"; // Don't use this one for now
    public static final String URL_POPULAR_ITEMS = "https://ws.blendle.nl/items/popular"; // Instead we'll use this one :) #lazy

    // Sub pages
    public static final String PAGE_POPULAR_ITEMS = "items_popular";

    // General Keys
    public static final String KEY_ID = "id";
    public static final String KEY_DATE = "date";
    public static final String KEY_LINKS = "_links";
    public static final String KEY_HREF = "href";
    public static final String KEY_SELF = "self";
    public static final String KEY_PREVIOUS = "prev";
    public static final String KEY_NEXT = "next";
    public static final String KEY_ITEM_CONTENT = "item_content";

    // Items keys
    public static final String KEY_EMBEDDED = "_embedded";
    public static final String KEY_ITEMS = "items";
    public static final String KEY_PRICE = "price";
    public static final String KEY_MANIFEST = "manifest";
    public static final String KEY_FORMAT_VERSION = "format_version";
    public static final String KEY_PROVIDER = "provider";
    public static final String KEY_BODY = "body";
    public static final String KEY_TYPE = "type";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_IMAGES = "images";
    public static final String KEY_IMAGE_SMALL = "small";
    public static final String KEY_IMAGE_MEDIUM = "medium";
    public static final String KEY_IMAGE_LARGE = "large";
    public static final String KEY_IMAGE_ORIGINAL = "original";
    public static final String KEY_IMAGE_WIDTH = "width";
    public static final String KEY_IMAGE_HEIGHT = "height";
    public static final String KEY_ITEM_INDEX = "item_index";

}
