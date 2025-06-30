package utils;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SimpleJwtUtil {
    private static final String SECRET = "YourSuperSecretKeyThatIsLongEnoughForHS256"; // 32+ chars
    private static final long EXPIRATION_TIME = 86400000; // 24 hours

    public static String generateToken(int userId, String role) {
        long now = System.currentTimeMillis();
        String header = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payload = String.format(
                "{\"sub\":\"%d\",\"role\":\"%s\",\"iat\":%d,\"exp\":%d}",
                userId,
                role,
                now,
                now + EXPIRATION_TIME
        );

        String encodedHeader = Base64.getUrlEncoder().encodeToString(header.getBytes());
        String encodedPayload = Base64.getUrlEncoder().encodeToString(payload.getBytes());
        String signature = HmacSHA256(encodedHeader + "." + encodedPayload, SECRET);

        return encodedHeader + "." + encodedPayload + "." + signature;
    }

    public static boolean validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) return false;

            String signature = HmacSHA256(parts[0] + "." + parts[1], SECRET);
            return signature.equals(parts[2]);
        } catch (Exception e) {
            return false;
        }
    }

    private static String HmacSHA256(String data, String key) {
        try {
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
            mac.init(secretKey);
            byte[] signature = mac.doFinal(data.getBytes());
            return Base64.getUrlEncoder().encodeToString(signature);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate HMAC", e);
        }
    }
}
