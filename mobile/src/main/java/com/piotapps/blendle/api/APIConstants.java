package com.piotapps.blendle.api;

/**
 * Set of constants used for accessing and parsing the Blendle API.
 */
public class APIConstants {

    // API URL
    public static final String URL_ALL = "https://static.blendle.nl/api.json"; // Don't use this one for now
    public static final String URL_POPULAR_ITEMS = "https://ws.blendle.nl/items/popular"; // Instead we'll use this one :) #lazy

    // Link for provider logo's
    public static final String PROVIDER_LOGO_URL = "https://blendle.com/img/providers/%s/logo.png";

    // Sub pages
    public static final String PAGE_POPULAR_ITEMS = "items_popular";

    // Keys, keys everywhere!
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_TEMPLATES = "templates";
    public static final String KEY_TEMPLATE_TILE = "tile";
    public static final String KEY_TEMPLATE_ITEM_CONTENT = "item_content";
    public static final String KEY_DATE = "date";
    public static final String KEY_LINKS = "_links";
    public static final String KEY_HREF = "href";
    public static final String KEY_SELF = "self";
    public static final String KEY_PREVIOUS = "prev";
    public static final String KEY_NEXT = "next";
    public static final String KEY_ITEM_CONTENT = "item_content";
    public static final String KEY_EMBEDDED = "_embedded";
    public static final String KEY_PROVIDER_CONFIGURATIONS = "provider_configurations";
    public static final String KEY_CONFIGURATIONS= "configurations";
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

    // Body types
    public static final String KEY_BODY_KICKER = "kicker";
    public static final String KEY_BODY_HEADLINE_1 = "hl1";
    public static final String KEY_BODY_HEADLINE_2 = "hl2";
    public static final String KEY_BODY_INTRO = "intro";
    public static final String KEY_BODY_BYLINE = "kicker";
    public static final String KEY_BODY_PARAGRAPH = "p";
    public static final String KEY_BODY_PARAGRAPH_HEADER = "ph";
    public static final String KEY_BODY_LEAD = "lead";
    // TODO: implement the other body types
}
