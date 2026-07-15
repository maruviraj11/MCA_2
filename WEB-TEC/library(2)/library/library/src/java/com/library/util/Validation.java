package com.library.util;

public final class Validation {
    private Validation() {}

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    public static boolean isValidEmail(String email) {
        if (isBlank(email)) return false;
        // Practical server-side validation (avoid being overly strict)
        return email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
    }

    public static boolean isAlphaSpace(String value) {
        if (isBlank(value)) return false;
        return value.trim().matches("^[A-Za-z ]+$");
    }

    public static boolean isStrongPassword(String password, int minLen) {
        if (password == null) return false;
        if (password.length() < minLen) return false;
        if (!password.matches(".*[A-Z].*")) return false;
        if (!password.matches(".*[a-z].*")) return false;
        if (!password.matches(".*[0-9].*")) return false;
        return true;
    }

    public static String passwordRuleMessage(int minLen) {
        return "Password must be at least " + minLen + " characters and contain uppercase, lowercase, and a number.";
    }

    public static Integer parsePositiveInt(String value) {
        if (isBlank(value)) return null;
        try {
            int v = Integer.parseInt(value.trim());
            return v > 0 ? v : null;
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}

