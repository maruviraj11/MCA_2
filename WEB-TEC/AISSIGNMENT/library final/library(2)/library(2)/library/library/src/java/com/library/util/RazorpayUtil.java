package com.library.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class RazorpayUtil {

    public static String createOrder(String keyId, String keySecret, int amountPaise, String currency, String receipt) throws Exception {
        String endpoint = "https://api.razorpay.com/v1/orders";
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");

        String auth = keyId + ":" + keySecret;
        String encoded = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        conn.setRequestProperty("Authorization", "Basic " + encoded);

        String body = "{"
                + "\"amount\":" + amountPaise + ","
                + "\"currency\":\"" + escape(currency) + "\","
                + "\"receipt\":\"" + escape(receipt) + "\""
                + "}";

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));
        }

        int code = conn.getResponseCode();
        BufferedReader br = new BufferedReader(new InputStreamReader(
                code >= 200 && code < 300 ? conn.getInputStream() : conn.getErrorStream(),
                StandardCharsets.UTF_8
        ));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        String json = sb.toString();

        if (code < 200 || code >= 300) {
            throw new RuntimeException("Razorpay order create failed: HTTP " + code + " " + json);
        }

        String orderId = extractJsonString(json, "id");
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new RuntimeException("Razorpay order_id not found in response");
        }
        return orderId;
    }

    public static boolean verifySignature(String orderId, String razorpayPaymentId, String razorpaySignature, String secret) throws Exception {
        if (orderId == null || razorpayPaymentId == null || razorpaySignature == null || secret == null) return false;
        String payload = orderId + "|" + razorpayPaymentId;
        String expected = hmacSha256Hex(payload, secret);
        return safeEquals(expected, razorpaySignature);
    }

    private static String hmacSha256Hex(String data, String secret) throws Exception {
        Mac sha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256.init(keySpec);
        byte[] hash = sha256.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return toHex(hash);
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static boolean safeEquals(String a, String b) {
        if (a == null || b == null) return false;
        if (a.length() != b.length()) return false;
        int res = 0;
        for (int i = 0; i < a.length(); i++) {
            res |= a.charAt(i) ^ b.charAt(i);
        }
        return res == 0;
    }

    // Minimal JSON string extractor (no external deps)
    private static String extractJsonString(String json, String key) {
        if (json == null || key == null) return null;
        String needle = "\"" + key + "\"";
        int idx = json.indexOf(needle);
        if (idx < 0) return null;
        int colon = json.indexOf(":", idx + needle.length());
        if (colon < 0) return null;
        int firstQuote = json.indexOf("\"", colon + 1);
        if (firstQuote < 0) return null;
        int endQuote = json.indexOf("\"", firstQuote + 1);
        if (endQuote < 0) return null;
        return json.substring(firstQuote + 1, endQuote);
    }

    private static String escape(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}

