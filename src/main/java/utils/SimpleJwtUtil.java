package utils;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleJwtUtil {
    private static final String SECRET = "YourSuperSecretKeyThatIsLongEnoughForHS256";
    private static final long EXPIRATION_TIME = 86400000;

    public static String generateToken(int userId, String role) {
        long now = System.currentTimeMillis();
        long exp = now + EXPIRATION_TIME;

        String header = base64UrlEncode("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes());
        String payload = String.format("{\"sub\":%d,\"role\":\"%s\",\"iat\":%d,\"exp\":%d}",
                userId, role, now / 1000, exp / 1000);
        String encodedPayload = base64UrlEncode(payload.getBytes());

        String data = header + "." + encodedPayload;
        String signature = hmacSha256(data, SECRET);

        return data + "." + signature;
    }

    public static boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return false;

            String signatureCheck = hmacSha256(parts[0] + "." + parts[1], SECRET);
            if (!parts[2].equals(signatureCheck)) return false;

            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            long exp = extractLong(payload, "\"exp\":(\\d+)");
            long now = System.currentTimeMillis() / 1000;

            return now < exp;
        } catch (Exception e) {
            System.err.println("❌ Token validation error: " + e.getMessage());
            return false;
        }
    }

    public static int getUserIdFromToken(String token) {
        String payload = new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]));
        return (int) extractLong(payload, "\"sub\":(\\d+)");
    }

    public static String getRoleFromToken(String token) {
        String payload = new String(Base64.getUrlDecoder().decode(token.split("\\.")[1]));
        Matcher matcher = Pattern.compile("\"role\":\"(\\w+)\"").matcher(payload);
        return matcher.find() ? matcher.group(1) : null;
    }

    private static String hmacSha256(String data, String key) {
        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKey);
            return base64UrlEncode(mac.doFinal(data.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException("HMAC error", e);
        }
    }

    private static String base64UrlEncode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private static long extractLong(String json, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(json);
        if (matcher.find()) return Long.parseLong(matcher.group(1));
        throw new RuntimeException("Value not found in token");
    }
}
