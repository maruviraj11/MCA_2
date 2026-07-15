package com.cmp.util;

import java.util.regex.Pattern;

public final class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");

    private ValidationUtil() {
    }

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        String value = email.trim().toLowerCase();
        if (value.length() == 0) {
            return false;
        }
        return EMAIL_PATTERN.matcher(value).matches();
    }

    public static String normalizeEmailOrThrow(String email) {
        String value = email == null ? "" : email.trim().toLowerCase();
        if (!isValidEmail(value)) {
            throw new RuntimeException("Enter a valid email address.");
        }
        return value;
    }
}

