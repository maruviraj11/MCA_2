package com.library.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public final class WebUtil {
    private WebUtil() {}

    public static String enc(String value) {
        try {
            return URLEncoder.encode(value == null ? "" : value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }
}

